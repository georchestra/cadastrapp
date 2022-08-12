package org.georchestra.cadastrapp.service.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.MimeConstants;
import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.georchestra.cadastrapp.model.pdf.BordereauParcellaire;
import org.georchestra.cadastrapp.model.pdf.Style;
import org.georchestra.cadastrapp.service.CadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BordereauParcellaireController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(BordereauParcellaireController.class);
	final static Logger docLogger = LoggerFactory.getLogger("org.georchestra.cadastrapp.loggers.documents");
		
	final String xslTemplate = "xsl/bordereauParcellaire.xsl";
	
	@Autowired
	BordereauParcellaireHelper bordereauParcellaireHelper;

	/**
	 * This will create a PDF file using apache FOP framework,
	 * data is retrieved from database and a FO is created using the xsl template.
	 * Once the fo file is create we then create the PDF file
	 * 
	 * @param parcelleList, list of parcelleId you want to export
	 * @param personalData, 0  no owners information
	 * 						1  owners information in page
	 * 						0 is set by default if empty
	 * @param styleFillColor style plot color
	 * @param styleFillOpacity style plot opacity
	 * @param styleStrokeColor style stroke color
	 * @param styleStrokeWidth style stroke width
	 * @param baseMapIndex corresponding of the wanted index in cadastrapp.properties
	 * @return BP pdf
	 */
	@RequestMapping(path = "/createBordereauParcellaire", produces ={MediaType.APPLICATION_PDF_VALUE}, method= {RequestMethod.GET})
	public ResponseEntity<byte[]> createBordereauParcellaire(
			@RequestParam(name= "parcelle") final List<String> parcelleList,
			@RequestParam(name= "personaldata", defaultValue = "0", required= false) int personalData,
			@RequestParam(name= "basemapindex", defaultValue = "0", required= false) int baseMapIndex,
			@RequestParam(name= "fillcolor", defaultValue = "#1446DE", required= false) String styleFillColor,
			@RequestParam(name= "opacity", defaultValue = "0.50", required= false) float styleFillOpacity,
			@RequestParam(name= "strokecolor", defaultValue = "#10259E", required= false) String styleStrokeColor,
			@RequestParam(name= "strokewidth", defaultValue = "2", required= false) int styleStrokeWidth) {

		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(HttpStatus.NO_CONTENT);
		
		// Check if parcelle list is not empty
		if (parcelleList != null && !parcelleList.isEmpty()) {
			
			List<String> newParcelleList = parcelleList;
			
			if(parcelleList.size() ==1) {	
				newParcelleList = Arrays.asList(parcelleList.get(0).split("\\s|;|,"));
			}
			String tempFolder = CadastrappPlaceHolder.getProperty("tempFolder");
			
			// Pdf temporary filename using tmp folder and timestamp
			final long key = new Date().getTime();
			final String pdfTmpFileName = tempFolder+File.separator+"BP-"+key;		
			InputStream xsl = Thread.currentThread().getContextClassLoader().getResourceAsStream(xslTemplate);

			// Setup XSLT
			TransformerFactory factory = TransformerFactory.newInstance();

			Transformer transformerXSLT;
			Transformer transformerPDF;
			JAXBContext jaxbContext;
			Marshaller jaxbMarshaller;
			ByteArrayOutputStream out;
			Fop fop;

			try {
				transformerXSLT = factory.newTransformer(new StreamSource(xsl));
				transformerPDF = factory.newTransformer();
				
				out = new ByteArrayOutputStream();
				
				FopFactoryBuilder builder = new FopFactoryBuilder(new File(tempFolder+File.separator+".").toURI());
				
				// get DPI from comfig file
				int dpi=Integer.parseInt(CadastrappPlaceHolder.getProperty("pdf.dpi"));
				
				builder.setSourceResolution(dpi);
				builder.setTargetResolution(dpi);
				
				FopFactory fopFactory = builder.build();

				fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
	

				jaxbContext = JAXBContext.newInstance(BordereauParcellaire.class);
				jaxbMarshaller = jaxbContext.createMarshaller();

				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				
				// Create plot style 
				Style plotsStyle = new Style();
				plotsStyle.setFillColor(styleFillColor);
				plotsStyle.setFillOpacity(styleFillOpacity);
				plotsStyle.setStrokeColor(styleStrokeColor);
				plotsStyle.setStrokeWidth(styleStrokeWidth);

				// Get bordereau parcellaire information
				BordereauParcellaire bordereauParcellaire = bordereauParcellaireHelper.getBordereauParcellaireInformation(newParcelleList, personalData, false, plotsStyle, baseMapIndex);
				File xmlfile = null;
				File foFile = null;
				OutputStream foOutPutStream = null;
				
				try {
					// Xml file will be deleted on JVM exit
					xmlfile = new File(pdfTmpFileName+".xml");
					xmlfile.deleteOnExit();
					
					jaxbMarshaller.marshal(bordereauParcellaire, xmlfile);

					// log on console marshaller only if debug log is one
					if (logger.isDebugEnabled()) {
						StringWriter stringWriter = new StringWriter();					
						jaxbMarshaller.marshal(bordereauParcellaire, stringWriter);
						logger.debug(stringWriter.toString());
					}

					// FO file will be deleted on JVM exit
					// XML TO FO
					foFile = new File(pdfTmpFileName+".fo");
					foFile.deleteOnExit();
					
					foOutPutStream = new java.io.FileOutputStream(foFile);

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
					byte[] bytes = out.toByteArray();
					out.close();

					ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
					.filename("BP-"+key+".pdf")
					.build();

					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_PDF);
					headers.setContentDisposition(contentDisposition);

					response = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);					
				} catch (JAXBException jaxbException) {
					logger.warn("Error during converting object to xml : " + jaxbException);
				} catch (TransformerException transformerException) {
					logger.warn("Error during converting object to xml : " + transformerException);
				} catch (FileNotFoundException fileNotFoundException) {
					logger.warn("Error when using temporary files : " + fileNotFoundException);
				} finally {
					if (out != null) {
						// Clean-up
						out.close();
					}
					if(xmlfile != null){
					xmlfile.delete();
					}
					if(foFile != null){
						foFile.delete();
					}
					if (foOutPutStream != null){
						foOutPutStream.close();
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
			}finally{
				// Could not delete pdfResult here because it's still used by cxf
			}
			
			for (String parcelle : newParcelleList) {
				docLogger.info("Bordereau Parcellaire - GenerationDirecte - null - "+parcelle+" - "+personalData+" - false" );
			}
		} else {
			logger.warn("Required parameter missing");
		}
		return response;
	}

}
