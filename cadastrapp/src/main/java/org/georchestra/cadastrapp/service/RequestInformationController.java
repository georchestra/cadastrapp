package org.georchestra.cadastrapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	// Request type made by administration
	final String A = "A";
	
	// Request type made by particulier détendeur des droits
	final String P1 = "P1";
	
	// Request type made by particulier agissant en qualité de mandataire
	final String P2 = "P2";
	
	// Request type made by Particuliers Tierce
	final String P3 = "P3";

	@Autowired
	RequestRepository requestRepository;
	@Autowired
	UserRequestRepository userRepository;

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
	public Map<String, Object> checkRequestLimitation(@QueryParam("cni") String cni, @QueryParam("type") String type, @Context HttpHeaders headers) throws SQLException {

		Map<String, Object> result = new HashMap<String, Object>();

		if (type == P3) {
			int nbRequestAvailable = 0;

			Date currentDate = new Date();

			Calendar cal = Calendar.getInstance();

			cal.setTime(currentDate);
			cal.add(Calendar.DATE, -7);
			Date datePlusOneWeek = cal.getTime();

			cal.setTime(currentDate);
			cal.add(Calendar.MONTH, -1);
			Date datePlusOneMonth = cal.getTime();

			if (logger.isDebugEnabled()) {

				logger.debug("Current date : " + currentDate.toString());
				logger.debug("One week before : " + datePlusOneWeek.toString());
				logger.debug("One monthe before : " + datePlusOneMonth.toString());
			}

			int numberRequestInTheWeek = requestRepository.countByUserCniAndUserTypeAndRequestDateAfter(cni, type, datePlusOneWeek);
			int numberRequestInTheMonth = requestRepository.countByUserCniAndUserTypeAndRequestDateAfter(cni, type, datePlusOneMonth);

			// Denied request
			// if User has made more than 5 requests in the last week
			// if User has made more than 10 requests in the last month
			if (numberRequestInTheMonth < maxRequestByMonth) {
				if (numberRequestInTheWeek < maxRequestByWeek) {
					nbRequestAvailable = maxRequestByWeek - numberRequestInTheWeek;
				}
			}

			result.put("requestAvailable", nbRequestAvailable);
		}

		// Check if parameter are not null or empty
		if (cni != null && cni.length() > 0 && type != null && type.length() > 0) {
			final UserRequest existingUser = userRepository.findByCniAndType(cni, type);

			if (existingUser != null) {
				result.put("user", existingUser);
			}
		}

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
	public List<Map<String, Object>> checkRequestValidity(@QueryParam("cni") String cni, @QueryParam("type") String type, @QueryParam("comptecommunaux") List<String> compteCommunaux, @QueryParam("coproprietes") List<String> coproprietes, @QueryParam("parcelles") List<String> parcelleIds, @Context HttpHeaders headers) throws SQLException {

		// Check information in database
		//final UserRequest existingUser = userRepository.findByCniAndType(cni, type);

		// Check if parcelle, comptecommunaux and lot coproprietes exist and if user have rights

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
			@QueryParam("type") String type, 
			@QueryParam("adress") String adress, 
			@QueryParam("commune") String commune, 
			@QueryParam("codepostal") String codePostal, 
			@QueryParam("firstname") String firstname, 
			@QueryParam("lastname") String lastname, 
			@QueryParam("mail") String mail,
			@QueryParam("comptecommunal") List<String> compteCommunaux, 
			@QueryParam("parcelles") List<String> parcelleIds, 
			@QueryParam("copropriete") List<String> coProprietes, 
			@QueryParam("askby") int askby, 
			@QueryParam("responseby") int responseby, @Context HttpHeaders headers) throws SQLException {

		// todo recheck value

		Map<String, Object> resultInformation = new HashMap<String, Object>();
		String result = "KO";
		String message = "user updated";

		// Check if user exist
		UserRequest existingUser = userRepository.findByCniAndType(cni, type);

		if (existingUser == null) {
			message = "user created";
			existingUser = new UserRequest();
			existingUser.setCni(cni);
			existingUser.setType(type);
		}

		// update information if needed
		existingUser.setAdress(adress);
		existingUser.setCommune(commune);
		existingUser.setCodePostal(codePostal);
		existingUser.setFirstName(firstname);
		existingUser.setLastName(lastname);
		existingUser.setMail(mail);

		UserRequest existingUser2 = userRepository.save(existingUser);

		// Create informationRequest
		final InformationRequest informationRequest = new InformationRequest();

		informationRequest.setUser(existingUser2);
		informationRequest.setRequestDate(new Date());

		if (compteCommunaux != null && !compteCommunaux.isEmpty()) {

			if (compteCommunaux.size() == 1) {
				compteCommunaux = Arrays.asList(compteCommunaux.get(0).split("\\s|;|,"));
			}

			Set<String> compteCommunauxSet = new HashSet<String>(compteCommunaux);
			informationRequest.setCompteCommunaux(compteCommunauxSet);
		}

		if (coProprietes != null && !coProprietes.isEmpty()) {

			if (coProprietes.size() == 1) {
				coProprietes = Arrays.asList(coProprietes.get(0).split("\\s|;|,"));
			}

			Set<String> coproprietesSet = new HashSet<String>(coProprietes);
			informationRequest.setCoproprietes(coproprietesSet);
		}

		if (parcelleIds != null && !parcelleIds.isEmpty()) {

			if (parcelleIds.size() == 1) {
				parcelleIds = Arrays.asList(parcelleIds.get(0).split("\\s|;|,"));
			}

			Set<String> parcellesSet = new HashSet<String>(parcelleIds);
			informationRequest.setParcellesId(parcellesSet);
		}
		
		informationRequest.setAskby(askby);
		informationRequest.setResponseby(responseby);

		final InformationRequest informationRequestSaved = requestRepository.save(informationRequest);

		result = "OK";

		resultInformation.put("insertion", result);
		resultInformation.put("message", message);
		resultInformation.put("id", informationRequestSaved.getRequestId());

		return resultInformation;
	}

}
