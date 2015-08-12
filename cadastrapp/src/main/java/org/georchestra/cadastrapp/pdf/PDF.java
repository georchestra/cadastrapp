package org.georchestra.cadastrapp.pdf;

// Java
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.georchestra.cadastrapp.model.BordereauParcellaire;
import org.xml.sax.SAXException;

/**
 * This class demonstrates the conversion of an arbitrary object file to a PDF
 * using JAXP (XSLT) and FOP (XSL:FO).
 */
public class PDF {

	public static void main(String[] args) throws SAXException, IOException, TransformerException, JAXBException {
		
		// Create new BordereauParcellaire
		BordereauParcellaire bordereauParcellaire = new BordereauParcellaire();
		bordereauParcellaire.setId("idBordereau");
		bordereauParcellaire.setParcelleId("parcelleID");
		bordereauParcellaire.setImage("image");
		bordereauParcellaire.setLibelleCommune("libelleCommune");
		bordereauParcellaire.setAdresseCadastrale("adresseCadastrale");
		bordereauParcellaire.setSection("section");
		bordereauParcellaire.setParcelle("parcelle");
		bordereauParcellaire.setCodeFantoir("codeFantoir");
		bordereauParcellaire.setSurfaceCadastrale("surfaceCadastrale");
		bordereauParcellaire.setDateDeCreation(new Date());
		bordereauParcellaire.setDateDeValidite("01/01/2012");
		bordereauParcellaire.setService("Service de Rennes Métropole");

		// Step 1: Construct a FopFactory by specifying a reference to the
		// configuration file
		// (reuse if you plan to render multiple documents!)
		FopFactory fopFactory = FopFactory.newInstance();

		// Step 2: Set up output stream.
		// Note: Using BufferedOutputStream for performance reasons (helpful
		// with FileOutputStreams).
		OutputStream out = new BufferedOutputStream(new FileOutputStream(new File("/tmp/bordereauParcellaire.pdf")));

		try {
			// Step 3: Construct fop with desired output format
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

			// Step 4: Setup JAXP using identity transformer
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(); // identity
																// transformer

			File xmlfile = new File("/tmp/bordereauParcellaire.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(BordereauParcellaire.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(bordereauParcellaire, xmlfile);
			jaxbMarshaller.marshal(bordereauParcellaire, System.out);
			
			
			 OutputStream foFile = new java.io.FileOutputStream(new File("/tmp/bordereauParcellaire.fo"));
			 //Setup XSLT
			 TransformerFactory factoryXslt = TransformerFactory.newInstance();
			 Transformer transformerXSLT = factoryXslt.newTransformer(new StreamSource(new File("src/main/resources/xsl/bordereauParcellaire.xsl")));
			 	
			 //Setup input for XSLT transformation
			 Source srcXml = new StreamSource(xmlfile);
			 
			 //Resulting SAX events (the generated FO) must be piped through to FOP
			 Result resFo = new StreamResult(foFile);
			 
			 //Start XSLT transformation and FOP processing
			 transformerXSLT.transform(srcXml, resFo);
			
			 // Step 5: Setup input and output for XSLT transformation
			// Setup input stream
			Source src = new StreamSource(new File("/tmp/bordereauParcellaire.fo"));

			// Resulting SAX events (the generated FO) must be piped through to FOP
			Result res = new SAXResult(fop.getDefaultHandler());

			// Step 6: Start XSLT transformation and FOP processing
			transformer.transform(src, res);

		} finally {
			// Clean-up
			out.close();
		}

	}
}