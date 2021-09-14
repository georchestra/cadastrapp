package org.georchestra.cadastrapp.service.pdf;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Date;

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

import org.apache.commons.io.FileUtils;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.georchestra.cadastrapp.model.request.InformationRequest;
import org.georchestra.cadastrapp.repository.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RequestPDFController{

	static final Logger logger = LoggerFactory.getLogger(RequestPDFController.class);

	static final String xslTemplate = "xsl/request.xsl";

	@Autowired
	RequestRepository requestRepository;

	@RequestMapping(path = "/printPDFRequest", produces = {MediaType.APPLICATION_PDF_VALUE}, method= {RequestMethod.GET})
	public ResponseEntity<byte[]> printPDFRequest(@RequestParam(name = "requestid") long requestId) {

		ResponseEntity<byte[]> response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
		// Check if requestId exist
		if (requestId != 0) {

			// Get bordereau parcellaire information
			InformationRequest requestInformation = requestRepository.findByRequestId(requestId);

			if (requestInformation != null) {
				
				String tempFolder = CadastrappPlaceHolder.getProperty("tempFolder");

				// Pdf temporary filename using tmp folder and timestamp
				final String pdfTmpFileName = tempFolder + File.separator + "DemandeInformation" + new Date().getTime();

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

					FopFactory fopFactory = FopFactory.newInstance(pdfResult.toURI());
					fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

					jaxbContext = JAXBContext.newInstance(InformationRequest.class);
					jaxbMarshaller = jaxbContext.createMarshaller();

					jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

					File xmlfile = null;
					File foFile = null;
					try {
						// Xml file will be deleted on JVM exit
						xmlfile = new File(pdfTmpFileName + ".xml");
						xmlfile.deleteOnExit();

						jaxbMarshaller.marshal(requestInformation, xmlfile);

						// log on console marshaller only if debug log is one
						if (logger.isDebugEnabled()) {
							StringWriter stringWriter = new StringWriter();					
							jaxbMarshaller.marshal(requestInformation, stringWriter);
							logger.debug(stringWriter.toString());
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
						HttpHeaders headers = new HttpHeaders();
						headers.setContentType(MediaType.APPLICATION_PDF);
						headers.setContentDispositionFormData("filename", pdfResult.getName());

						response = new ResponseEntity<>(FileUtils.readFileToByteArray(pdfResult), headers, HttpStatus.OK);
						//response.header("Content-Disposition", "attachment; filename=" + pdfResult.getName());

					} catch (JAXBException jaxbException) {
						logger.warn("Error during converting object to xml", jaxbException);
					} catch (TransformerException transformerException) {
						logger.warn("Error during transforming xml", transformerException);
					} catch (FileNotFoundException fileNotFoundException) {
						logger.warn("Error when using temporary files", fileNotFoundException);
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

				} catch (TransformerConfigurationException e) {
					logger.warn("Error when initialize transformers", e);
				} catch (IOException ioException) {
					logger.warn("Error when creating output file", ioException);
				} catch (FOPException fopException) {
					logger.warn("Error when creationg FOP file type", fopException);
				} catch (JAXBException jaxbException) {
					logger.warn("Error creating Marsharller", jaxbException);
				}
			}
		} else {
			logger.warn("Required parameter missing");
		}
		return response;
	}

}
