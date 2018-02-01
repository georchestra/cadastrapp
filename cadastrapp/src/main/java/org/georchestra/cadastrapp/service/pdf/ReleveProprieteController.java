package org.georchestra.cadastrapp.service.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.georchestra.cadastrapp.model.pdf.CompteCommunal;
import org.georchestra.cadastrapp.model.pdf.RelevePropriete;
import org.georchestra.cadastrapp.service.CadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;


public class ReleveProprieteController extends CadController {

	static final Logger logger = LoggerFactory.getLogger(ReleveProprieteController.class);
	
	@Autowired
	ReleveProprieteHelper releveProprieteHelper;

	/**
	 * Create a PDF using a list of comptecommunal
	 * 
	 * @param headers to verify CNIL level information
	 * @param compteCommunal List of ids proprietaires
	 * @return pdf
	 */
	@GET
	@Path("/createRelevePropriete")
	@Produces("application/pdf")
	public Response createRelevePDFPropriete(@Context HttpHeaders headers, @QueryParam("compteCommunal") List<String> comptesCommunaux, @QueryParam("parcelleId") String idParcelle) {

		ResponseBuilder response = Response.noContent();
		
		logger.debug("Controller Parcelle ID (param) : "+idParcelle);
		
		// Check if parcelle list is not empty
		if (comptesCommunaux != null && !comptesCommunaux.isEmpty()) {
			
			// Fixe #329 in some case Extjs return one string with data separated with , instead of a list of data.
			if(comptesCommunaux.size() ==1) {	
				comptesCommunaux = Arrays.asList(comptesCommunaux.get(0).split(","));
			}
			// Get information about releve de propriete
			RelevePropriete relevePropriete = releveProprieteHelper.getReleveProprieteInformation(comptesCommunaux, headers, idParcelle);

			File pdfResult = releveProprieteHelper.generatePDF(relevePropriete, false, false);
			
			// Create response
			response = Response.ok((Object) pdfResult);
			response.header("Content-Disposition", "attachment; filename=" + pdfResult.getName());

		} else {
			logger.warn("Required parameter missing");
		}
		return response.build();
	}
	
	
	/**
	 * Create a zip file, containint several csv using a list of comptecommunal
	 * 
	 * @param headers to verify CNIL level information
	 * @param compteCommunal List of ids proprietaires
	 * @param parcelleId
	 * @return zip 
	 * @throws IOException 
	 * @throws CsvRequiredFieldEmptyException 
	 * @throws CsvDataTypeMismatchException 
	 */
	@GET
	@Path("/createReleveProprieteAsCSV")
	@Produces("application/zip")
	public Response createReleveCSVPropriete(@Context HttpHeaders headers, @QueryParam("compteCommunal") List<String> comptesCommunaux, @QueryParam("parcelleId") String idParcelle) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

		ResponseBuilder response = Response.noContent();
		
		String tempFolder = CadastrappPlaceHolder.getProperty("tempFolder");
		
		logger.debug("createReleveProprieteAsCSV ");
		
		// Check if parcelle list is not empty
		if (comptesCommunaux != null && !comptesCommunaux.isEmpty()) {
			
			// Fixe #329 in some case Extjs return one string with data separated with , instead of a list of data.
			if(comptesCommunaux.size() ==1) {	
				comptesCommunaux = Arrays.asList(comptesCommunaux.get(0).split(","));
			}
			// Get information about releve de propriete
			RelevePropriete relevePropriete = releveProprieteHelper.getReleveProprieteInformation(comptesCommunaux, headers, idParcelle);

			// Define Date for all file name
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
			Date date = new Date();
			final String dateString = dateFormat.format(date);
			
			// Create Zip file
			logger.debug("Create Zip ");
			final String zipFileName = "RP-" + dateString + ".zip";
			final String zipFilePath = tempFolder + File.separator + zipFileName;
			File finalZip = new File(zipFilePath);
			finalZip.deleteOnExit();
			byte[] buffer = new byte[1024];
			
			// out put file to write all csv file
			FileOutputStream StreamZip;
			StreamZip = new FileOutputStream(finalZip);	
			ZipOutputStream out = new ZipOutputStream(StreamZip);
					  	   
			// Create csv with CompteCommunal information
			logger.debug("Create global information csv ");
			final String csvCCFileName = "RP-Comptecommunal-" + dateString + ".csv";
			final String csvCCPath = tempFolder + File.separator + csvCCFileName;
			
			Writer writer = new FileWriter(csvCCPath);
			StatefulBeanToCsv CCToCsv = new StatefulBeanToCsvBuilder(writer).build();
			CCToCsv.write(relevePropriete.getComptesCommunaux());
			writer.close();
			
			FileInputStream in = new FileInputStream(csvCCPath);
			out.putNextEntry(new ZipEntry(csvCCFileName)); 
			int count;
			while ((count = in.read(buffer)) > 0) {
				out.write(buffer, 0, count);
			}
			out.closeEntry();
			in.close();
			
			// For each CC get buildings and plots informations
			logger.debug("Loop on " + relevePropriete.getComptesCommunaux().size() + " comptes communaux");
			for(CompteCommunal cc : relevePropriete.getComptesCommunaux()){
				
				if(cc.getProprietesBaties() != null){
					// Buildings
					logger.debug("Create Batie csv for cc ! " + cc.getCompteCommunal());
					final String csvPBFileName = "RP-Batie-"+cc.getCompteCommunal() + "-" + dateString + ".csv";
					final String csvPBPath = tempFolder + File.separator + csvPBFileName;
					Writer writer1 = new FileWriter(csvPBPath);
					StatefulBeanToCsv PBToCsv = new StatefulBeanToCsvBuilder(writer1).build();
					PBToCsv.write(cc.getProprietesBaties().getProprietes());
					writer1.close();
					
					FileInputStream in1 = new FileInputStream(csvPBPath);
					out.putNextEntry(new ZipEntry(csvPBFileName)); 
					int count1;
					while ((count1 = in1.read(buffer)) > 0) {
						out.write(buffer, 0, count1);
					}
					out.closeEntry();
					in1.close();
				}
									
				// Owners
				if(cc.getProprietaires() != null){
					// Buildings
					logger.debug("Create owner csv for cc ! " + cc.getCompteCommunal());
					final String csvPropFileName = "RP-Proprietaire-"+cc.getCompteCommunal() + "-" + dateString + ".csv";
					final String csvPropPath = tempFolder + File.separator + csvPropFileName;
					Writer writer2 = new FileWriter(csvPropPath);
					StatefulBeanToCsv PropToCsv = new StatefulBeanToCsvBuilder(writer2).build();
					PropToCsv.write(cc.getProprietaires());
					writer2.close();
					
					FileInputStream in2 = new FileInputStream(csvPropPath);
					out.putNextEntry(new ZipEntry(csvPropFileName)); 
					int count2;
					while ((count2 = in2.read(buffer)) > 0) {
						out.write(buffer, 0, count2);
					}
					out.closeEntry();
					in2.close();
				}
					
				if(cc.getProprietesNonBaties() != null){
					//PNB
					logger.debug("Create NonBatie csv for cc ! " + cc.getCompteCommunal());
					final String csvPNBFileName = "RP-NonBatie-"+cc.getCompteCommunal() + "-" + dateString + ".csv";
					final String csvPNBPath = tempFolder + File.separator + csvPNBFileName;
					Writer writer3 = new FileWriter(csvPNBPath);
					StatefulBeanToCsv PNBToCsv = new StatefulBeanToCsvBuilder(writer).build();
					PNBToCsv.write(cc.getProprietesNonBaties().getProprietes());				
					writer3.close();
						
					FileInputStream in3 = new FileInputStream(csvPNBPath);
					out.putNextEntry(new ZipEntry(csvPNBFileName)); 
					int count3;
					while ((count3 = in3.read(buffer)) > 0) {
						out.write(buffer, 0, count3);
					}
					
					in3.close();
					out.closeEntry();
				}
			}
			
			out.close();
			
			// Create response
			response = Response.ok((Object) finalZip);
			response.header("Content-Disposition", "attachment; filename=" + zipFileName);

		} else {
			logger.warn("Required parameter missing");
		}
		return response.build();
	}

}
