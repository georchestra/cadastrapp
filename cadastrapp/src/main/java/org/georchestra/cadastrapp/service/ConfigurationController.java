package org.georchestra.cadastrapp.service;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConfigurationController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(ConfigurationController.class);
	
	
	@GET
	@Path("/getConfiguration")
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 *  Returns server configuration witch is common to client side 
	 *   
	 * @return JSON list 
	 */
	public Map<String, Object> getConfiguration(){
		
		Map<String, Object> configuration = new HashMap<String, Object>();
		
		configuration.put("minNbCharForSearch",minNbCharForSearch);
		
		configuration.put("cadastreWFSURL", wfsUrl );
		configuration.put("cadastreWMSURL", wmsUrl );
		
		configuration.put("cadastreWMSLayerName", cadastreWMSLayerName); 
		configuration.put("cadastreWFSLayerName", cadastreWFSLayerName);
		configuration.put("cadastreLayerIdParcelle", cadastreLayerIdParcelle);

		configuration.put("cnil1RoleName", cnil1RoleName);
		configuration.put("cnil2RoleName", cnil2RoleName);
		
		return configuration;
	}

}
