package org.georchestra.cadastrapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
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


public class RequestInformationController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(RequestInformationController.class);
	
	@GET
	@Path("/getRequestValidity")
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 *  /getRequestValidity
	 *  
	 *  return message to indicate if user can make a search or not
	 *  
	 * 
	 * @param nom
	 * @param prenom
	 * @param adresse
	 * @param cni
	 * @param parcelleids
	 * 
	 * @return JSON 
	 * 
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getRequestValidity(
			@QueryParam("nom") String cgoCommune,
			@QueryParam("prenom") String dvoilib,
			@QueryParam("adresse") String adresse,
			@QueryParam("cni") String cni,
			@QueryParam("parcelleids") List<String> parcelleIds,
			@Context HttpHeaders headers) throws SQLException {

		// Check information in database
		
		// Denied request
		// if User has made more than 5 requests in the last week
		// if User has made more than 10 requests in the last month
		// if parcelle requested are not in geographic group of connected user
		
		List<Map<String, Object>> request = new ArrayList<Map<String, Object>>();


		return request;
	}
	

}
