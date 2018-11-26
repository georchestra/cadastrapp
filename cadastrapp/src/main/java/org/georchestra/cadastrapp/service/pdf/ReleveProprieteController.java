package org.georchestra.cadastrapp.service.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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
import org.georchestra.cadastrapp.model.pdf.Proprietaire;
import org.georchestra.cadastrapp.model.pdf.ProprieteBatie;
import org.georchestra.cadastrapp.model.pdf.ProprieteNonBatie;
import org.georchestra.cadastrapp.model.pdf.RelevePropriete;
import org.georchestra.cadastrapp.service.CadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;


public class ReleveProprieteController extends CadController {

	static final Logger logger = LoggerFactory.getLogger(ReleveProprieteController.class);
	
	@Autowired
	ReleveProprieteHelper releveProprieteHelper;

	/**
	 * Create a PDF using a list of comptecommunal
	 * 
	 * @param headers to verify CNIL level information
	 * @param comptesCommunaux List of ids proprietaires
	 * @param idParcelle plot id
	 * @return pdf generated RP with database information
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
	 * @param comptesCommunaux List of ids proprietaires
	 * @param idParcelle plot id
	 * @return zip containing csv file
	 * @throws IOException if an input or output exception occured
	 */
	@GET
	@Path("/createReleveProprieteAsCSV")
	@Produces("application/zip")
	public Response createReleveCSVPropriete(@Context HttpHeaders headers, @QueryParam("compteCommunal") List<String> comptesCommunaux, @QueryParam("parcelleId") String idParcelle) throws IOException {

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
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH_mm_ss_SSS");
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
			ICsvBeanWriter beanWriter = null;
			try {
				beanWriter = new CsvBeanWriter(new FileWriter(csvCCPath), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
				
				// the header elements are used to map the bean values to each column (names must match)
				final String[] header = new String[] { "compteCommunal", "codeDepartement", "codeCommune", "libelleCommune" };
				final CellProcessor[] processors = new CellProcessor[] { 
		                new NotNull(), // compteCommunal 
		                new NotNull(), // codeDepartement
		                new NotNull(), // codeCommune
		                new NotNull()  // libelleCommune
		        };
		                
				// write the header
				beanWriter.writeHeader(header);
		                
				// write the beans
				for( final CompteCommunal cc : relevePropriete.getComptesCommunaux() ) {
					beanWriter.write(cc, header, processors);
				}	                
			}
			finally {
				if( beanWriter != null ) {
					beanWriter.close();
				}
			}
			
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
				
				if(cc.getProprietesBaties() != null && cc.getProprietesBaties().getProprietes()!= null){
					// Buildings
					logger.debug("Create Batie csv for cc ! " + cc.getCompteCommunal());
					final String csvPBFileName = "RP-Batie-"+cc.getCompteCommunal() + "-" + dateString + ".csv";
					final String csvPBPath = tempFolder + File.separator + csvPBFileName;
					
					ICsvBeanWriter pbBeanWriter = null;
					try {
						pbBeanWriter = new CsvBeanWriter(new FileWriter(csvPBPath), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
						
						// the header elements are used to map the bean values to each column (names must match)
						final String[] header = new String[] { "ccopre","ccosec","dnupla","dnvoiri","dindic","dvoilib","ccoaff","ccocac","ccoeva","cconad","cconlc","ccoriv",
								"dcapec","descr","dniv","dnubat","dpor","exonerations","gtauom","invar","jdatat","lots","revenuImposable","communeRevenuImposable","communeRevenuExonere","groupementCommuneRevenuImposable","groupementCommuneRevenuExonere","departementRevenuImposable","departementRevenuExonere","tseRevenuImposable"};
						final CellProcessor[] processors = new CellProcessor[] { 
				                new Optional(), // ccopre
				                new Optional(), // ccosec
				                new Optional(), // dnupla 
				                new Optional(), // dnvoiri
				                new Optional(), // dindic
				                new Optional(), // dvoilib
				                new Optional(), // ccoaff
				                new Optional(), // ccocac
				                new Optional(), // ccoeva
				                new Optional(), // cconad
				                new Optional(), // cconlc
				                new Optional(), // ccoriv
				                new Optional(), // dcapec
				                new Optional(), // descr
				                new Optional(), // dniv
				                new Optional(), // dnubat
				                new Optional(), // dpor
				                new Optional(), // exonerations
				                new Optional(), // gtauom
				                new Optional(), // invar
				                new Optional(), // jdatat
				                new Optional(), // lots
				                new Optional(), // revenuImposable
				                new Optional(), // communeRevenuImposable
				                new Optional(), // communeRevenuExonere
				                new Optional(), // groupementCommuneRevenuImposable
				                new Optional(), // groupementCommuneRevenuExonere
				                new Optional(), // departementRevenuImposable
				                new Optional(), // departementRevenuExonere
				                new Optional() // tseRevenuImposable
				        };
				                
						// write the header
						pbBeanWriter.writeHeader(header);
				                
						// write the beans
						for( final ProprieteBatie pb : cc.getProprietesBaties().getProprietes()){
							pbBeanWriter.write(pb, header, processors);
						}	                
					}
					finally {
						if( pbBeanWriter != null ) {
							pbBeanWriter.close();
						}
					}
					
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
					
					ICsvBeanWriter ownerBeanWriter = null;
					try {
						ownerBeanWriter = new CsvBeanWriter(new FileWriter(csvPropPath), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
						
						// the header elements are used to map the bean values to each column (names must match)
						final String[] header = new String[] { "compteCommunal", "droitReel", "codeDeDemembrement", "nom", "adresse", "nomNaissance", "dateNaissance" };
						final CellProcessor[] processors = new CellProcessor[] { 
				                new NotNull(), // compteCommunal 
				                new Optional(), // droitReel
				                new Optional(), // codeDeDemembrement
				                new Optional(), // nom
				                new Optional(), // adresse
				                new Optional(), // nomNaissance
				                new Optional() // dateNaissance
				        };
				                
						// write the header
						ownerBeanWriter.writeHeader(header);
				                
						// write the beans
						for( final Proprietaire prop : cc.getProprietaires()){
							ownerBeanWriter.write(prop, header, processors);
						}	                
					}
					finally {
						if( ownerBeanWriter != null ) {
							ownerBeanWriter.close();
						}
					}
					
					FileInputStream in2 = new FileInputStream(csvPropPath);
					out.putNextEntry(new ZipEntry(csvPropFileName)); 
					int count2;
					while ((count2 = in2.read(buffer)) > 0) {
						out.write(buffer, 0, count2);
					}
					out.closeEntry();
					in2.close();
				}
					
				if(cc.getProprietesNonBaties() != null && cc.getProprietesNonBaties().getProprietes()!= null){
					//PNB
					logger.debug("Create NonBatie csv for cc ! " + cc.getCompteCommunal());
					final String csvPNBFileName = "RP-NonBatie-"+cc.getCompteCommunal() + "-" + dateString + ".csv";
					final String csvPNBPath = tempFolder + File.separator + csvPNBFileName;
					
					ICsvBeanWriter pnbBeanWriter = null;
					try {
						pnbBeanWriter = new CsvBeanWriter(new FileWriter(csvPNBPath), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
						
						// the header elements are used to map the bean values to each column (names must match)
						final String[] header = new String[] { "ccopre","ccosec","dnupla","dnvoiri","dindic","dvoilib","ccosub","ccostn","cgrnum","cnatsp","dparpi","dclssf","dcntsf","dnulot","drcsuba","dreflf","dsgrpf","gparnf","pdl","exonerations","revenuImposable","communeRevenuImposable","communeRevenuExonere","groupementCommuneRevenuImposable","groupementCommuneRevenuExonere","departementRevenuImposable","departementRevenuExonere","tseRevenuImposable" };
						final CellProcessor[] processors = new CellProcessor[] { 
								new Optional(), // ccopre
				                new Optional(), // ccosec
				                new Optional(), // dnupla 
				                new Optional(), // dnvoiri
				                new Optional(), // dindic
				                new Optional(), // dvoilib
				                new Optional(), // ccosub
				                new Optional(), // ccostn
				                new Optional(), // cgrnum
				                new Optional(), // cnatsp
								new Optional(), // dparpi 
				                new Optional(), // dclssf
				                new Optional(), // dcntsf
				                new Optional(), // dnulot
				                new Optional(), // drcsuba
				                new Optional(), // dreflf
				                new Optional(), // dsgrpf 
				                new Optional(), // gparnf
				                new Optional(), // pdl   
				                new Optional(), // exonerations
				                new Optional(), // revenuImposable
				                new Optional(), // communeRevenuImposable
				                new Optional(), // communeRevenuExonere
				                new Optional(), // groupementCommuneRevenuImposable
				                new Optional(), // groupementCommuneRevenuExonere
				                new Optional(), // departementRevenuImposable
				                new Optional(), // departementRevenuExonere
				                new Optional() // tseRevenuImposable
				        };
				                
						// write the header
						pnbBeanWriter.writeHeader(header);
				                
						// write the beans
						for( final ProprieteNonBatie pnb : cc.getProprietesNonBaties().getProprietes()){
							pnbBeanWriter.write(pnb, header, processors);
						}	                
					}
					finally {
						if( pnbBeanWriter != null ) {
							pnbBeanWriter.close();
						}
					}
					
						
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
