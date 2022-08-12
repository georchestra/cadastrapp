package org.georchestra.cadastrapp.logging;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple utility listener to load certain properties before Spring Starts up.
 *
 * inspired from https://bowerstudios.com/node/896
 *
 */
public class ExternalConfigLoaderContextListener implements ServletContextListener {
	private static final Logger logger = LoggerFactory.getLogger(ExternalConfigLoaderContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String configLocation = System.getProperty("georchestra.datadir");
		if (configLocation != null) {
			try {
				new Log4JConfigLoader(configLocation + "/cadastrapp/log4j/log4j2.properties");
			} catch (Exception e) {
				logger.error("Unable to read config file", e);
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}
