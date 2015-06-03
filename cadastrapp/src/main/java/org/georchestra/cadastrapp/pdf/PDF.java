package org.georchestra.cadastrapp.pdf;

// Java
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.georchestra.cadastrapp.model.RelevePropriete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class demonstrates the conversion of an arbitrary object file to a
 * PDF using JAXP (XSLT) and FOP (XSL:FO).
 */
public class PDF {
	
	final static Logger logger = LoggerFactory.getLogger(PDF.class);

    // configure fopFactory as desired
    private final FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

    /**
     * Converts a ProjectTeam object to a PDF file.
     * @param team the ProjectTeam object
     * @param xslt the stylesheet file
     * @param pdf the target PDF file
     * @throws IOException In case of an I/O problem
     * @throws FOPException In case of a FOP problem
     * @throws TransformerException In case of a XSL transformation problem
     */
    public void convertRelevePropriete2PDF(RelevePropriete team, File xslt, File pdf)
                throws IOException, FOPException, TransformerException {

        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        // configure foUserAgent as desired

        // Setup output
        OutputStream out = new java.io.FileOutputStream(pdf);
        out = new java.io.BufferedOutputStream(out);
        try {
            // Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslt));

            // Setup input for XSLT transformation
            Source src = team.getSourceForRelevePropriete();

            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Start XSLT transformation and FOP processing
            transformer.transform(src, res);
        } finally {
            out.close();
        }
    }


    /**
     * 
     */
    public void launchConvertion(RelevePropriete relevePropriete){
        try {

        	logger.debug("Preparing...");

            // Setup directories
            File baseDir = new File(".");
            File outDir = new File(baseDir, "out");
            outDir.mkdirs();

            // Setup input and output
            File xsltfile = new File(baseDir, "xml/xslt/projectteam2fo.xsl");
            File pdffile = new File(outDir, "ResultObj2PDF.pdf");

            logger.debug("Stylesheet: " + xsltfile);
            logger.debug("Output: PDF (" + pdffile + ")");
            logger.debug("Transforming...");

            PDF app = new PDF();
            app.convertRelevePropriete2PDF(relevePropriete, xsltfile, pdffile);

            logger.debug(" PDF Generated !");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(-1);
        }
    }
}