package org.georchestra.cadastrapp.service;

import java.io.File;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BordereauParcellaireController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(BordereauParcellaireController.class);
		
	@GET
	@Path("/createBordereauParcellaire")
	public Response createBordereauParcellaire(@Context HttpHeaders headers, 
			@FormParam("parcelle") String parcelle) {

		File file = new File("/tmp/test.pdf");
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=BP-" + parcelle + "-" + file.getName().substring(file.getName().lastIndexOf(".")));
		return response.build();
	}

}
