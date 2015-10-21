package org.georchestra.cadastrapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import org.georchestra.cadastrapp.model.request.InformationRequest;
import org.georchestra.cadastrapp.model.request.UserRequest;
import org.georchestra.cadastrapp.repository.RequestRepository;
import org.georchestra.cadastrapp.repository.UserRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class RequestInformationController {

	final static Logger logger = LoggerFactory.getLogger(RequestInformationController.class);
	
	final int maxRequestByMonth = 10;
	final int maxRequestByWeek = 5; 
	
	@Autowired RequestRepository requestRepository;
	@Autowired UserRequestRepository userRepository;
	
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
	public Map<String, Object> checkRequestLimitation(
			@QueryParam("cni") String cni,
			@Context HttpHeaders headers) throws SQLException {
		
		Map<String, Object> result = new HashMap<String, Object>();
		int nbRequestAvailable = 0;
		
		Date currentDate = new Date();
		
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(currentDate);
		cal.add(Calendar.DATE, -7);
		Date datePlusOneWeek = cal.getTime();
	
		
		cal.setTime(currentDate);
		cal.add(Calendar.MONTH, -1);
		Date datePlusOneMonth = cal.getTime();
		
		if (logger.isDebugEnabled()){
						
			logger.debug("Current date : " + currentDate.toString());
			logger.debug("One week before : " + datePlusOneWeek.toString());
			logger.debug("One monthe before : " + datePlusOneMonth.toString());
		}
	
		
		int numberRequestInTheWeek = requestRepository.countByUserCniAndRequestDateAfter(cni, datePlusOneWeek);	
		int numberRequestInTheMonth = requestRepository.countByUserCniAndRequestDateAfter(cni, datePlusOneMonth);
		
		// Denied request
		// if User has made more than 5 requests in the last week
		// if User has made more than 10 requests in the last month
		if(numberRequestInTheMonth < maxRequestByMonth){
			if(numberRequestInTheWeek < maxRequestByWeek){
				nbRequestAvailable = maxRequestByWeek - numberRequestInTheWeek;
				
				final UserRequest existingUser = userRepository.findByCni(cni);
								
				if(existingUser != null){
					result.put("user", existingUser);
				}
			}
		}
		result.put("requestAvailable", nbRequestAvailable);

		return result;
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
			@QueryParam("parcelles") List<String> parcelleIds,
			@Context HttpHeaders headers) throws SQLException {

		// Check information in database		
		final UserRequest existingUser = userRepository.findByCni(cni);
				
		// Denied request
		// if User has made more than 5 requests in the last week
		// if User has made more than 10 requests in the last month
		// if parcelle requested are not in geographic group of connected user
		
		List<Map<String, Object>> request = new ArrayList<Map<String, Object>>();


		return request;
	}
	
	
	@GET
	@Path("/saveInformationRequest")
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 *  /saveInformationRequest
	 *  
	 *  save information request in database
	 *  
	 * 
	 * @param cni
	 * @adress adress
	 * @cgocommue cgocommune
	 * @firstname firstname
	 * @lastename lastname
	 * @param comptecommunal - 1 compte comunal unique
	 * @param parcelleids - une liste de 5 parcelles maximums
	 * 
	 * @return JSON 
	 * 
	 * @throws SQLException
	 */
	public Map<String, Object> saveInformationRequest(
			@QueryParam("cni") String cni,
			@QueryParam("adress") String adress,
			@QueryParam("commune") String commune,
			@QueryParam("codepostal") String codePostal,
			@QueryParam("firstname") String firstname,
			@QueryParam("lastname") String lastname,
			@QueryParam("comptecommunal") String compteCommunal,
			@QueryParam("parcelles") List<String> parcelleIds,
			@Context HttpHeaders headers) throws SQLException {
		
		//todo recheck value
		
		Map<String, Object> resultInformation = new HashMap<String, Object>();
		String result = "KO";
		String message = "user updated";

		// Check if user exist
		UserRequest existingUser = userRepository.findByCni(cni);
		
		if(existingUser == null){	
			message = "user created";
			existingUser = new UserRequest();
			existingUser.setCni(cni);
		}
		
		// update information if needed
		existingUser.setAdress(adress);
		existingUser.setCommune(commune);
		existingUser.setCodepostal(codePostal);
		existingUser.setFirstName(firstname);
		existingUser.setLastName(lastname);
			
		UserRequest existingUser2 = userRepository.save(existingUser);
		
		// Create informationRequest
		final InformationRequest informationRequest = new InformationRequest();
		
		informationRequest.setUser(existingUser2);
		informationRequest.setRequestDate(new Date());
		informationRequest.setComptecommunal(compteCommunal);
		informationRequest.setParcellesId(parcelleIds);
		
		final InformationRequest informationRequestSaved = requestRepository.save(informationRequest);
		
		result = "OK";
		
		resultInformation.put("insertion", result);
		resultInformation.put("message", message);
		resultInformation.put("id", informationRequestSaved.getRequestId());
		
		return resultInformation;
}
		
	

}
