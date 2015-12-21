package org.georchestra.cadastrapp.servlet;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.io.FileUtils;
import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author gfi
 *
 */
public class InitFolderServlet extends HttpServlet {

	final static Logger logger = LoggerFactory.getLogger(InitFolderServlet.class);

	private static final long serialVersionUID = -8310750349814578427L;

	/**
	 * Init on startup
	 * @throws ServletException 
	 */
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		
		// Check if temp folder exist, create it otherwise
		String tempFolder = CadastrappPlaceHolder.getProperty("tempFolder");
		logger.debug("Cadastrapp - test if folder exists : " + tempFolder);

		File cadastrappDir = new File(tempFolder);

		if (cadastrappDir.exists()) {
			logger.debug("Cadastrapp - folder exist");

		} else {
			// if folder doesn't exist create it.
			logger.warn("Cadastrapp - folder does not exist");
			logger.info("creating directory: " + tempFolder);

			try {
				FileUtils.forceMkdir(cadastrappDir);
			} catch (IOException e) {
				logger.error("Cadastrapp - could not create tempfolder", e);
				// Throw an ServletException to stop deploiement in init phase
				throw new ServletException("Could not create temp folder"); 		
			}
		}
	}


}