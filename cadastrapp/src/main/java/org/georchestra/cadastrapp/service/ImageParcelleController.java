package org.georchestra.cadastrapp.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageParcelleController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(ImageParcelleController.class);

	@GET
	@Path("/getImageBordereau")
	@Produces("image/png")
	public Response createBordereauParcellaire(@QueryParam("parcelle") String parcelle) throws IOException {
        
		URL wfsUrl = new URL("http://gd-cms-crai-001.fasgfi.fr/geoserver/wfs?request=getFeature&typename=qgis:geo_parcelle&cql_filter='geo_parcelle="+parcelle+"'");

		URL url = new URL(baseMapUrl);
		BufferedImage image = ImageIO.read(url);

		File file = new File("/tmp/bordereau.png");
		ImageIO.write(image, "png", file);
		
		ResponseBuilder response = Response.ok((Object) file);
		
		return response.build();
	}

	
}
