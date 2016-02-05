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
import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.georchestra.cadastrapp.model.pdf.CompteCommunal;
import org.georchestra.cadastrapp.model.pdf.ImpositionNonBatie;
import org.georchestra.cadastrapp.model.pdf.Proprietaire;
import org.georchestra.cadastrapp.model.pdf.ProprieteBatie;
import org.georchestra.cadastrapp.model.pdf.ProprieteNonBatie;
import org.georchestra.cadastrapp.model.pdf.RelevePropriete;
import org.georchestra.cadastrapp.service.CadController;
import org.georchestra.cadastrapp.service.constants.CadastrappConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class ReleveProprieteController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(ReleveProprieteController.class);

	final String xslTemplate = "xsl/relevePropriete.xsl";
	final String xslTemplateMinimal = "xsl/releveProprieteMinimal.xsl";
	
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
	public Response createRelevePDFPropriete(@Context HttpHeaders headers, @QueryParam("compteCommunal") final List<String> comptesCommunaux) {

		ResponseBuilder response = Response.noContent();
		
		// Check if parcelle list is not empty
		if (comptesCommunaux != null && !comptesCommunaux.isEmpty()) {
			
			String tempFolder = CadastrappPlaceHolder.getProperty("tempFolder");

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
				//RelevePropriete relevePropriete = getReleveProprieteInformation(comptesCommunaux, headers);
				RelevePropriete relevePropriete = releveProprieteHelper.getReleveProprieteInformation(comptesCommunaux, headers);

				File xmlfile = null;
				File foFile = null;
				
				try {
					// Xml file will be deleted on JVM exit
					xmlfile = new File(pdfTmpFileName + ".xml");
					xmlfile.deleteOnExit();

					jaxbMarshaller.marshal(relevePropriete, xmlfile);

					// log on console marshaller only if debug log is one
					if (logger.isDebugEnabled()) {
						jaxbMarshaller.marshal(relevePropriete, System.out);
					}

					// FO file will be deleted on JVM exit
					// XML TO FO
					foFile = new File(pdfTmpFileName + ".fo");
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
					
					out.close();

					// Create response
					response = Response.ok((Object) pdfResult);
					response.header("Content-Disposition", "attachment; filename=" + pdfResult.getName());

				} catch (TransformerException transformerException) {
					logger.error("Error during transformation : " + transformerException);
				} catch (FileNotFoundException fileNotFoundException) {
					logger.error("Error when using temporary files : " + fileNotFoundException);
				} finally {
					if (out != null) {
						// Clean-up
						out.close();
					}
					if (xmlfile != null){
						xmlfile.delete();
					}
					if (foFile != null){
						foFile.delete();
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
		return response.build();
	}


}
