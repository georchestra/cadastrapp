package org.georchestra.cadastrapp.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User as proxy for Html2Canvas javascript librairie
 * 
 * @author pierre
 *
 */
public class CanvaProxyController {

	static final Logger logger = LoggerFactory.getLogger(CanvaProxyController.class);

	final String secUsername = "sec-username";

	Pattern callbackPattern = Pattern.compile("[a-zA-Z_$][0-9a-zA-Z_$]*");

	@GET
	@Path("/canvaProxy")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 * Proxyfiing image for html2canvas
	 * 
	 * @param headers
	 *            http headers used to verify if user is connecter
	 * @param url
	 *            image to proxyfy
	 * @param callback
	 *            technical field need for html2canvas to set image in the good
	 *            place
	 * 
	 * @return Image in Response
	 */
	public Response proxyImage(@Context HttpHeaders headers, @QueryParam("url") final URL url,	@QueryParam("callback") final String callback) {

		String user = headers.getHeaderString(secUsername);

		// Return a response only if user
		if (user != null && user.length() > 0 && url != null && callback != null && callback.length() > 0) {

			try {
				final URLConnection connection = url.openConnection();
				final InputStream data = connection.getInputStream();
				final String contentType = connection.getContentType();

				if (!callbackPattern.matcher(callback).matches()) {
					throw new WebApplicationException("Invalid callback name");
				}
				byte[] bytes = IOUtils.toByteArray(data);
				byte[] encoded = java.util.Base64.getEncoder().encode(bytes);
				
				String result = callback + "(\"data:" + contentType + ";base64,";
				result = result + new String(encoded, StandardCharsets.UTF_8) + "\");";
				
				return Response.ok(result, "application/javascript").build();

			} catch (IOException e) {
				logger.error("Invalid call, wrong parameters values", e.getMessage());
				throw new WebApplicationException("Invalid input");
			}
		} else {
			logger.info("Invalid call, missing input parameters");
			ResponseBuilder response = Response.noContent();
			return response.build();
		}
	}

}
