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
	@Path("/checkRequestLimitation")
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 *  /checkRequestLimitation
	 *  
	 *  return user information, and limitation
	 *  
	 * 
	 * @param cni
	 * 
	 * @return JSON 
	 * 
	 * @throws SQLException
	 */
	public List<Map<String, Object>> checkRequestLimitation(
			@QueryParam("cni") String cni,
			@Context HttpHeaders headers) throws SQLException {

		// Check information in database
		
		// Denied request
		// if User has made more than 5 requests in the last week
		// if User has made more than 10 requests in the last month
		// if parcelle requested are not in geographic group of connected user
		
		List<Map<String, Object>> request = new ArrayList<Map<String, Object>>();


		return request;
	}
	
	@GET
	@Path("/checkRequestValidity")
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 *  /checkRequestValidity
	 *  
	 *  check if user can make this search (CNIL level and geographic constraints)
	 *  
	 * 
	 * @param cni
	 * @param comptecommunal
	 * @param parcelleids
	 * 
	 * @return JSON 
	 * 
	 * @throws SQLException
	 */
	public List<Map<String, Object>> checkRequestValidity(
			@QueryParam("cni") String cni,
			@QueryParam("comptecommunal") String compteCommunal,
			@QueryParam("parcelle") List<String> parcelleIds,
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
