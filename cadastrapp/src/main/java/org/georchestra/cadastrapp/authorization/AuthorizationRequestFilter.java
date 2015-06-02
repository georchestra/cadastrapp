package org.georchestra.cadastrapp.authorization;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author gfi
 *
 */
public class AuthorizationRequestFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		final Logger logger = LoggerFactory.getLogger(ContainerRequestContext.class);

		logger.info("Request filter");
		
		String rolesList = requestContext.getHeaderString("sec-roles");
		String userName = requestContext.getHeaderString("sec-username");
	
		// Check not empty roles
		if (rolesList == null || rolesList.isEmpty()) {
			
			logger.warn("Request filter : Not authenticated user");

			requestContext.abortWith(Response
					.status(Response.Status.UNAUTHORIZED)
					.entity("User cannot access the resource.").build());
		}
		else{
			logger.info("Request filter : user " + userName + " from role " + rolesList);
		}
	}
}