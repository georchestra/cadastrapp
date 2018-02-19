package org.georchestra.cadastrapp.service;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConfigurationController {

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
		
		configuration.put("minNbCharForSearch", CadastrappPlaceHolder.getProperty("minNbCharForSearch"));
		
		configuration.put("cadastreWFSURL", CadastrappPlaceHolder.getProperty("cadastre.wfs.url"));
		configuration.put("cadastreWMSURL", CadastrappPlaceHolder.getProperty("cadastre.wms.url"));
		
		configuration.put("cadastreWMSLayerName", CadastrappPlaceHolder.getProperty("cadastre.wms.layer.name")); 
		configuration.put("cadastreWFSLayerName", CadastrappPlaceHolder.getProperty("cadastre.wfs.layer.name"));
		
		configuration.put("cadastreLayerIdParcelle", CadastrappPlaceHolder.getProperty("cadastre.layer.idParcelle"));

		configuration.put("cnil1RoleName", CadastrappPlaceHolder.getProperty("cnil1RoleName"));
		configuration.put("cnil2RoleName", CadastrappPlaceHolder.getProperty("cnil2RoleName"));
		
		configuration.put("maxRequest", CadastrappPlaceHolder.getProperty("maxRequest"));
		
		// Configuration for unite foncier
		configuration.put("uFWFSURL", CadastrappPlaceHolder.getProperty("uf.wfs.url"));
		configuration.put("uFWFSLayerName", CadastrappPlaceHolder.getProperty("uf.wfs.layer.name"));
		
		return configuration;
	}

}
