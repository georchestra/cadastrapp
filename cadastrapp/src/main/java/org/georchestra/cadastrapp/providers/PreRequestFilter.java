package org.georchestra.cadastrapp.providers;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 *  Log all request
 * @author gfi
 *
 */
public class PreRequestFilter implements ContainerRequestFilter {
	
	final Logger logger = LoggerFactory.getLogger(PreRequestFilter.class);

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
			
		String rolesList = requestContext.getHeaderString("sec-roles");
		String userName = requestContext.getHeaderString("sec-username");
		MDC.put("user", userName);
		MDC.put("roles", rolesList);
		MDC.put("uri", requestContext.getUriInfo().getPath());	
		
		logger.info("Incomming request");
		
		if(logger.isDebugEnabled()){
			logger.debug("Parameter list : " + requestContext.getUriInfo().getQueryParameters());
		}
		
	}
}