package org.georchestra.cadastrapp.providers;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 *  Log all request
 * @author gfi
 *
 */
public class PreReponseFilter implements ContainerResponseFilter {
	
	final Logger logger = LoggerFactory.getLogger(PreReponseFilter.class);

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		
		logger.info("Sending response");
		
		MDC.remove("user");
		MDC.remove("roles");
		MDC.remove("uri");	

	}

}