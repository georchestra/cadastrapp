package org.georchestra.cadastrapp.logging;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Utility class for loading an external config file for log4j
 *
 * inspired from https://bowerstudios.com/node/896
 */
public class Log4JConfigLoader {

	private Logger logger = LoggerFactory.getLogger(Log4JConfigLoader.class);
	
	public Log4JConfigLoader(String externalConfigFileLocation) throws IOException {
		File externalConfigFile = new File(externalConfigFileLocation);
		if (!externalConfigFile.exists()) {
			throw new IOException("Log4j External Config File Parameter does not reference a file that exists: " + externalConfigFileLocation);
		} else {
			if (!externalConfigFile.isFile()) {
				throw new IOException("Log4j External Config File Parameter exists, but does not reference a file:" + externalConfigFileLocation);
			} else {
				if (!externalConfigFile.canRead()) {
					throw new IOException("Log4j External Config File exists and is a file, but cannot be read: " + externalConfigFileLocation);
				} else {
					String log4jConfigurationFile = externalConfigFile.getAbsolutePath();
					LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
					File file = new File(log4jConfigurationFile);
					// this will force a reconfiguration
					context.setConfigLocation(file.toURI());
					logger.info("Configured Log4j with config file from: " + externalConfigFileLocation);
				}
			}
		}
	}
}
