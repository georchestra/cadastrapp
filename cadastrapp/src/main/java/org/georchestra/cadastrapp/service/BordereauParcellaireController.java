package org.georchestra.cadastrapp.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.georchestra.cadastrapp.model.pdf.BordereauParcellaire;
import org.georchestra.cadastrapp.model.pdf.Parcelle;
import org.georchestra.cadastrapp.model.pdf.Proprietaire;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class BordereauParcellaireController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(BordereauParcellaireController.class);
		
	final String xslTemplate = "xsl/bordereauParcellaire.xsl";

	/**
	 * This will create a PDF file using apache FOP framework,
	 * data is retrieved from database and a FO is created using the xsl template.
	 * Once the fo file is create we then create the PDF file
	 * 
	 * @param headers, use for CNIL level limitation
	 * 
	 * @param parcelleList, list of parcelleId you want to export
	 * 
	 * @param personalData, 0 -> no owners information
	 * 						1 -> owners information in page
	 * 						0 is set by default if empty
	 * 
	 * @return PDF file, one page by parcelle. Each page contains one "Bordereau Parcellaire" with or without owners information
	 */
	@GET
	@Path("/createBordereauParcellaire")
	@Produces("application/pdf")
	public Response createBordereauParcellaire(@Context HttpHeaders headers, @QueryParam("parcelle") final List<String> parcelleList, @DefaultValue("0") @QueryParam("personaldata") int personalData) {

		// Check if parcelle list is not empty
		if (parcelleList != null && !parcelleList.isEmpty()) {
			
			// Pdf temporary filename using tmp folder and timestamp
			final String pdfTmpFileName = tempFolder+File.separator+"BP"+new Date().getTime();

			// Construct a FopFactory (reuse if you plan to render multiple documents!)
			FopFactory fopFactory = FopFactory.newInstance();
			InputStream xsl = Thread.currentThread().getContextClassLoader().getResourceAsStream(xslTemplate);

			// Setup XSLT
			TransformerFactory factory = TransformerFactory.newInstance();

			Transformer transformerXSLT;
			Transformer transformerPDF;
			JAXBContext jaxbContext;
			Marshaller jaxbMarshaller;
			File pdfResult;
			OutputStream out;
			Fop fop;

			try {
				transformerXSLT = factory.newTransformer(new StreamSource(xsl));
				transformerPDF = factory.newTransformer();

				// Create Empyt PDF File will be erase after
				pdfResult = new File(pdfTmpFileName+".pdf");
				pdfResult.deleteOnExit();
				out = new BufferedOutputStream(new FileOutputStream(pdfResult));

				fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

				jaxbContext = JAXBContext.newInstance(BordereauParcellaire.class);
				jaxbMarshaller = jaxbContext.createMarshaller();

				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				// Get bordereau parcellaire information
				BordereauParcellaire bordereauParcellaire = getBordereauParcellaireInformation(parcelleList, personalData);

				
				try {
					// Xml file will be deleted on JVM exit
					File xmlfile = new File(pdfTmpFileName+".xml");
					xmlfile.deleteOnExit();
					
					jaxbMarshaller.marshal(bordereauParcellaire, xmlfile);

					// log on console marshaller only if debug log is one
					if (logger.isDebugEnabled()) {
						jaxbMarshaller.marshal(bordereauParcellaire, System.out);
					}

					// FO file will be deleted on JVM exit
					// XML TO FO
					File foFile = new File(pdfTmpFileName+".fo");
					foFile.deleteOnExit();
					
					OutputStream foOutPutStream = new java.io.FileOutputStream(foFile);

					// Setup input for XSLT transformation
					Source srcXml = new StreamSource(xmlfile);
					Result resFo = new StreamResult(foOutPutStream);

					// Start XSLT transformation and FOP processing
					transformerXSLT.transform(srcXml, resFo);
					foOutPutStream.close();

					// FO TO PDF
					Source src = new StreamSource(foFile);
					Result res = new SAXResult(fop.getDefaultHandler());

					// Start PDF transformation and FOP processing
					transformerPDF.transform(src, res);

					// Create response
					ResponseBuilder response = Response.ok((Object) pdfResult);
					response.header("Content-Disposition", "attachment; filename=" + pdfResult.getName());
					return response.build();

				} catch (JAXBException jaxbException) {
					logger.warn("Error during converting object to xml : " + jaxbException);
				} catch (TransformerException transformerException) {

				} catch (FileNotFoundException fileNotFoundException) {
					logger.warn("Error when using temporary files : " + fileNotFoundException);
				} finally {
					if (out != null) {
						// Clean-up
						out.close();
					}
				}

			} catch (TransformerConfigurationException e) {
				logger.warn("Error when initialize transformers : " + e);
			} catch (IOException ioException) {
				logger.warn("Error when creating output file : " + ioException);
			} catch (FOPException fopException) {
				logger.warn("Error when creationg FOP file type : " + fopException);
			} catch (JAXBException jaxbException) {
				logger.warn("Error creating Marsharller : " + jaxbException);
			}
		} else {
			logger.warn("Required parameter missing");
		}
		return null;
	}

	/**
	 * Get all information from database for all parcelle list
	 * 
	 * @param parcelle
	 *            List of parcelle id, like
	 * @param personalData
	 *            filter use to add owners information about parcelle, 1 to get
	 *            owner information
	 * 
	 * @return BordereauParcellaire witch contains list of parcelle
	 */
	private BordereauParcellaire getBordereauParcellaireInformation(List<String> parcelleList, int personalData) {

		BordereauParcellaire bordereauParcellaire = new BordereauParcellaire();

		bordereauParcellaire.setDateDeValidite(dateValiditeDonnees);
		bordereauParcellaire.setService(organisme);

		List<Parcelle> parcellesInformation = new ArrayList<Parcelle>();

		// Create query
		StringBuilder queryBuilder = new StringBuilder();

		queryBuilder.append("select p.parcelle, c.libcom, p.dcntpa, p.ccosec, p.dnupla, p.dnvoiri||' '||p.dindic||' '||p.cconvo||' '||dvoilib as adresse from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".parcelle p, ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".commune c ");
		queryBuilder.append(createWhereInQuery(parcelleList.size(), "parcelle"));
		queryBuilder.append("and p.cgocommune = c.cgocommune;");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> parcelles = jdbcTemplate.queryForList(queryBuilder.toString(), parcelleList.toArray());

		logger.debug("Parcelle size : " + parcelles.size());
		for (Map<String, Object> row : parcelles) {
			logger.debug("Parcelle information : " + row);
			Parcelle parcelle = new Parcelle();
			parcelle.setParcelleId((String) row.get("parcelle"));
			parcelle.setLibelleCommune((String) row.get("libcom"));
			parcelle.setAdresseCadastrale((String) row.get("adresse"));
			// parcelle.setCodeFantoir(rs.getString("CUST_ID"));
			parcelle.setParcelle((String) row.get("dnupla"));
			parcelle.setSection((String) row.get("ccosec"));
			//parcelle.setSurfaceCadastrale((Integer) row.get("dcntpa"));

			logger.debug("Parcelle information : " + parcelle);

			parcellesInformation.add(parcelle);

			if (personalData > 0) {
				List<Proprietaire> proprietaires = new ArrayList<Proprietaire>();
				Proprietaire proprietaire = new Proprietaire();

			}
		}
		bordereauParcellaire.setParcelleList(parcellesInformation);

		return bordereauParcellaire;
	}

}
