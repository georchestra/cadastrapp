package org.georchestra.cadastrapp.service.pdf;

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
import org.georchestra.cadastrapp.model.pdf.Parcelle;
import org.georchestra.cadastrapp.model.pdf.Proprietaire;
import org.georchestra.cadastrapp.model.pdf.RelevePropriete;
import org.georchestra.cadastrapp.service.CadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class ReleveProprieteController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(ReleveProprieteController.class);

	final String xslTemplate = "xsl/relevePropriete.xsl";

	/**
	 * 
	 * @param headers
	 * @param parcelleList
	 * @return
	 */
	@GET
	@Path("/createRelevePropriete")
	@Produces("application/pdf")
	public Response createRelevePDFPropriete(@Context HttpHeaders headers, @QueryParam("parcelle") final List<String> parcelleList) {

		// Check if parcelle list is not empty
		if (parcelleList != null && !parcelleList.isEmpty()) {

			// Pdf temporary filename using tmp folder and timestamp
			final String pdfTmpFileName = tempFolder + File.separator + "RP" + new Date().getTime();

			// Construct a FopFactory (reuse if you plan to render multiple
			// documents!)
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
				pdfResult = new File(pdfTmpFileName + ".pdf");
				pdfResult.deleteOnExit();
				out = new BufferedOutputStream(new FileOutputStream(pdfResult));

				fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

				jaxbContext = JAXBContext.newInstance(RelevePropriete.class);
				jaxbMarshaller = jaxbContext.createMarshaller();

				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				// Get information about releve de propriete
				RelevePropriete relevePropriete = getReleveProprieteInformation(parcelleList, headers);

				try {
					// Xml file will be deleted on JVM exit
					File xmlfile = new File(pdfTmpFileName + ".xml");
					xmlfile.deleteOnExit();

					jaxbMarshaller.marshal(relevePropriete, xmlfile);

					// log on console marshaller only if debug log is one
					if (logger.isDebugEnabled()) {
						jaxbMarshaller.marshal(relevePropriete, System.out);
					}

					// FO file will be deleted on JVM exit
					// XML TO FO
					File foFile = new File(pdfTmpFileName + ".fo");
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

				} catch (TransformerException transformerException) {
					logger.error("Error during transformation : " + transformerException);
				} catch (FileNotFoundException fileNotFoundException) {
					logger.error("Error when using temporary files : " + fileNotFoundException);
				} finally {
					if (out != null) {
						// Clean-up
						out.close();
					}
				}
			} catch (JAXBException jaxbException) {
				logger.error("Error during converting object to xml : " + jaxbException);
			} catch (TransformerConfigurationException transformerConfigurationException) {
				logger.error("Error when initialize transformers : " + transformerConfigurationException);
			} catch (IOException ioException) {
				logger.error("Error when creating output file : " + ioException);
			} catch (FOPException fopException) {
				logger.error("Error when creationg FOP file type : " + fopException);
			}
		} else {
			logger.warn("Required parameter missing");
		}
		return null;
	}

	/**
	 * 
	 * @param parcelleList
	 * @param headers
	 * @return
	 */
	private RelevePropriete getReleveProprieteInformation(List<String> parcelleList, HttpHeaders headers) {

		RelevePropriete relevePropriete = new RelevePropriete();

		// TODO change this for year only
		relevePropriete.setAnneMiseAJour(dateValiditeDonnees);
		relevePropriete.setService(organisme);

		List<Parcelle> parcellesInformation = new ArrayList<Parcelle>();

		// Create query
		StringBuilder queryBuilder = new StringBuilder();

		queryBuilder.append("select p.parcelle, c.libcom, p.dcntpa, p.ccosec, p.dnupla, p.dnvoiri||' '||p.dindic||' '||p.cconvo||' '||dvoilib as adresse, p.ccoriv from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".parcelleDetails p, ");
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
			parcelle.setCodeFantoir((String) row.get("ccoriv"));
			parcelle.setParcelle((String) row.get("dnupla"));
			parcelle.setSection((String) row.get("ccosec"));
			parcelle.setSurfaceCadastrale((Integer) row.get("dcntpa"));

			logger.debug("Parcelle information : " + parcelle);

			if (getUserCNILLevel(headers) > 0) {
				List<Proprietaire> proprietaires = new ArrayList<Proprietaire>();

				StringBuilder queryBuilderProprietaire = new StringBuilder();
				queryBuilderProprietaire.append("select prop.comptecommunal, prop.ccoqua_lib||' '||prop.ddenom as nom, prop.dlign3||' '||prop.dlign4||' '||prop.dlign5||' '||prop.dlign6 as adresse ");
				queryBuilderProprietaire.append("from ");
				queryBuilderProprietaire.append(databaseSchema);
				queryBuilderProprietaire.append(".proprietaire prop, ");
				queryBuilderProprietaire.append(databaseSchema);
				queryBuilderProprietaire.append(".proprietaire_parcelle proparc ");
				queryBuilderProprietaire.append("where proparc.parcelle = ? and prop.comptecommunal = proparc.comptecommunal");
				queryBuilderProprietaire.append(addAuthorizationFiltering(headers));

				List<Map<String, Object>> proprietairesResult = jdbcTemplate.queryForList(queryBuilderProprietaire.toString(), row.get("parcelle"));

				for (Map<String, Object> prop : proprietairesResult) {
					Proprietaire proprietaire = new Proprietaire();
					proprietaire.setNom((String) prop.get("nom"));
					proprietaire.setAdresse((String) prop.get("adresse"));

					proprietaires.add(proprietaire);
				}
				parcelle.setProprietaires(proprietaires);
			}
			parcellesInformation.add(parcelle);
		}
		relevePropriete.setParcelleList(parcellesInformation);

		return relevePropriete;
	}

}
