package org.georchestra.cadastrapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;


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
		
		configuration.put("cadastreWMSURL", "");
		configuration.put("cadastreLayerName", ""); 
		configuration.put("cadastreLayerFormat", "");

		configuration.put("cnil1RoleName", "");
		configuration.put("cnil2RoleName", "");
		
		return configuration;
	}

}
