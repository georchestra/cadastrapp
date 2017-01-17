package org.georchestra.cadastrapp.context;


import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.io.FileUtils;
import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 
 * Context listener specific for cadastrapp
 * 
 * @author pierrejego
 *
 */
public class CadastrappContextListener implements ServletContextListener{

	final static Logger logger = LoggerFactory.getLogger(CadastrappContextListener.class);

	@Override
	/**
	 * Check if folder is created before starting application, stop deploiement if folder is not writable
	 */
	public void contextInitialized(ServletContextEvent sce) {
		// Check if temp folder exist, create it otherwise
		String tempFolder = CadastrappPlaceHolder.getProperty("tempFolder");
		logger.debug("Init config - Test iftemporary folder exists : " + tempFolder);
		
		File cadastrappDir = new File(tempFolder);
		
		if (cadastrappDir.exists()) {
			logger.debug("Init config - folder exist - trying to write in it");
			
			File writeTest = new File(tempFolder+File.separator+"test");
			try {
				if(writeTest.createNewFile()){
					logger.debug("Init config - folder " + tempFolder + " exist and is writable");
					writeTest.delete();
				}else{
					logger.error("Init config - folder  " + tempFolder + " exist but is not writable");
					// Throw an exception to stop deploiement in init phase
					throw new RuntimeException("Could not write in temp folder"); 
				}
			} catch (IOException e) {
				logger.error("Init config - folder  " + tempFolder + " exist but is not writable", e.getMessage());
				throw new RuntimeException("Could not write in temp folder"); 
			}
			
		} else {
			// if folder doesn't exist create it.
			logger.warn("Init config - temporary folder  " + tempFolder + " does not exist - creating directory: " + tempFolder);
			
			try {
				FileUtils.forceMkdir(cadastrappDir);
			} catch (IOException e) {
				logger.error("Init config - could not create tempfolder  " + tempFolder + " ", e);
				// Throw an exception to stop deploiement in init phase
				throw new RuntimeException("Could not create temp folder"); 		
			}
		}				
	}

	@Override
	/**
	 * Close all current Quartz Thread
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		 
		// Use to stop all Quartz instance
		if (sce instanceof ConfigurableApplicationContext) {
				logger.info("Close all Quartz thread");
	           ((ConfigurableApplicationContext)sce).close();
	       }
		
	}

}