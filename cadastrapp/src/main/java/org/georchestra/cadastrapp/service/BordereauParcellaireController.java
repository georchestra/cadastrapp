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
import org.georchestra.cadastrapp.model.BordereauParcellaire;
import org.georchestra.cadastrapp.model.Parcelle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BordereauParcellaireController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(BordereauParcellaireController.class);

	@GET
	@Path("/createBordereauParcellaire")
	@Produces("application/pdf")
	public Response createBordereauParcellaire(@Context HttpHeaders headers, @QueryParam("parcelle") final List<String> parcelleList, @DefaultValue("0") @QueryParam("personaldata") int personalData) {

		// Construct a FopFactory
		// (reuse if you plan to render multiple documents!)
		FopFactory fopFactory = FopFactory.newInstance();
		InputStream xsl = Thread.currentThread().getContextClassLoader().getResourceAsStream("xsl/bordereauParcellaire.xsl");

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

			pdfResult = new File("/tmp/bordereauParcellaire.pdf");
			out = new BufferedOutputStream(new FileOutputStream(pdfResult));

			fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

			jaxbContext = JAXBContext.newInstance(BordereauParcellaire.class);
			jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Get bordereau parcellaire information
			BordereauParcellaire bordereauParcellaire = getBordereauParcellaireInformation(parcelleList, personalData);

			try {
				File xmlfile = new File("/tmp/bordereauParcellaire.xml");
				

				jaxbMarshaller.marshal(bordereauParcellaire, xmlfile);

				if (logger.isDebugEnabled()) {
					jaxbMarshaller.marshal(bordereauParcellaire, System.out);
				}

				// XML TO FO
				File foFile = new File("/tmp/bordereauParcellaire.fo");
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

		return null;
	}

	/**
	 * 
	 * @param parcelle
	 * @param personalData
	 */
	private BordereauParcellaire getBordereauParcellaireInformation(List<String> parcelleList, int personalData) {

		BordereauParcellaire bordereauParcellaire = new BordereauParcellaire();

		bordereauParcellaire.setDateDeCreation(new Date());
		bordereauParcellaire.setDateDeValidite(dataValiditeDonnees);
		bordereauParcellaire.setService(organisme);

		List<Parcelle> parcellesInformation = new ArrayList<Parcelle>();
		
		for (String parcelle : parcelleList) {

			Parcelle parcelleInformation = new Parcelle();
			parcelleInformation.setParcelleId(parcelle);
			parcelleInformation.setImage("image");
			parcelleInformation.setLibelleCommune("libelleCommune");
			parcelleInformation.setAdresseCadastrale("adresseCadastrale");
			parcelleInformation.setSection("section");
			parcelleInformation.setParcelle("parcelle");
			parcelleInformation.setCodeFantoir("codeFantoir");
			parcelleInformation.setSurfaceCadastrale("surfaceCadastrale");
			
			parcellesInformation.add(parcelleInformation);

		}
		bordereauParcellaire.setParcelleList(parcellesInformation);

		return bordereauParcellaire;
	}

}
