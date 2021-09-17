package org.georchestra.cadastrapp.service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.georchestra.cadastrapp.model.request.InformationRequest;
import org.georchestra.cadastrapp.model.request.ObjectRequest;
import org.georchestra.cadastrapp.model.request.UserRequest;
import org.georchestra.cadastrapp.repository.RequestRepository;
import org.georchestra.cadastrapp.repository.UserRequestRepository;
import org.georchestra.cadastrapp.service.constants.CadastrappConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RequestInformationController extends CadController{

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

	@RequestMapping(path = "/checkRequestLimitation", produces = {MediaType.APPLICATION_JSON_VALUE}, method= {RequestMethod.GET})
	/**
	 *  /checkRequestLimitation
	 *  
	 *  return user information, and limitation
	 *  
	 * 
	 * @param cni
	 * @param type
	 * 
	 * @return JSON which contains
	 * 
	 * @throws SQLException
	 */
	public @ResponseBody Map<String, Object> checkRequestLimitation(
		@RequestParam String cni,
		@RequestParam String type) throws SQLException {

		Map<String, Object> result = new HashMap<String, Object>();

		if (P3.equals(type)) {

			logger.debug("Check limitation for user type P3");

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

			int numberRequestInTheWeek = requestRepository.sumObjectNumberByUserCniAndUserTypeAndRequestDateAfter(cni, type, datePlusOneWeek);
			int numberRequestInTheMonth = requestRepository.sumObjectNumberByUserCniAndUserTypeAndRequestDateAfter(cni, type, datePlusOneMonth);

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

	@RequestMapping(path = "/saveInformationRequest", produces = {MediaType.APPLICATION_JSON_VALUE}, method= { RequestMethod.GET})
	/**
	 * /**
	 *  /saveInformationRequest
	 *  
	 *  save information request in database
	 *  
	 * 
	 * @param cni
	 * @param type Can be A, P1, P2 or P3
	 * @param adress
	 * @param commune
	 * @param codePostal
	 * @param firstname
	 * @param lastname
	 * @param mail
	 * @param compteCommunaux - liste de comptes communaux et des documents souhaités pour chaque comptecommunal 0 non, 1 oui
	 * 							exemple pour un compte communal : 2015xxxxxA00300|0|1 <-> comptecommunal|borderauParcellaire|releveDePropriete
	 * 								Le format du compte communal dépend de la base en provenance du modèle Qgis
	 * 
	 * 							
	 * @param parcelleIds - liste d'information de parcelle et des documents souhaités pour chaque parcelle 0 non, 1 oui
	 * 							exemple pour une parcelle : 350047|000ZK|226|0|1 <-> cgocommune|ccopre+ccosec|borderauParcellaire|releveDePropriete
	 * 
	 * @param proprietaires - liste de couple nom proprétaire et commune et des documents souhaités 0 non, 1 oui
	 * 							exemple pour un proprietaire : 350047|MR JEGO PIERRE|0|1 <-> cgocommune|DDENOM|borderauParcellaire|releveDePropriete
	 * 
	 * @param parcelles- liste d'identifiant de parcelles et des documents souhaités pour chaque parcelle 0 non, 1 oui
	 * 							exemple pour un identifiant de parcelle : 2015xxxxxx000ZK0026|0|1 <-> parcelleid|borderauParcellaire|releveDePropriete
	 * 
	 * @param coProprietes  - liste de lot de coproprietés et des documents souhaités pour chaque parcelle 0 non, 1 oui
	 * 							exemple pour une copropriete : 2015xxxxA00300|2015xxxxxx000ZK0026|0|1 <-> comptecommunal|parcelleid|borderauParcellaire|releveDePropriete
	 * @param lotCoproprietes
	 * @param askby   - 1 Guichet, 2 Courrier, 3 Mail
	 * @param responseby - 1 Guichet, 2 Courrier, 3 Mail
	 * 
	 * @return JSON 
	 * 
	 * @throws SQLException
	 */
	public @ResponseBody Map<String, Object> saveInformationRequest(
			@RequestParam("cni") String cni, 
			@RequestParam("type") String type, 
			@RequestParam(name= "adress", required= false) String adress, 
			@RequestParam(name= "commune", required= false) String commune, 
			@RequestParam(name= "codepostal", required= false) String codePostal, 
			@RequestParam(name= "firstname", required= false) String firstname, 
			@RequestParam(name= "lastname", required= false) String lastname,
			@RequestParam(name= "mail", required= false) String mail,
			@RequestParam(name= "comptecommunaux", required= false) List<String> compteCommunaux, 
			@RequestParam(name= "parcelleIds", required= false) List<String> parcelleIds, 
			@RequestParam(name= "proprietaires", required= false) List<String> proprietaires, 
			@RequestParam(name= "parcelles", required= false) List<String> parcelles, 
			@RequestParam(name= "coproprietes", required= false) List<String> coProprietes, 
			@RequestParam(name= "proprietaireLots", required= false) List<String> lotCoproprietes, 
			@RequestParam(name= "askby", required= false, defaultValue = "0") int askby, 
			@RequestParam(name= "responseby", required= false, defaultValue = "0") int responseby) throws SQLException {

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
		informationRequest.setAskby(askby);
		informationRequest.setResponseby(responseby);

		Set<ObjectRequest> objectRequestSet = new HashSet<ObjectRequest>();

		// Add compteCommunaux to request information
		if (compteCommunaux != null && !compteCommunaux.isEmpty()) {

			if (compteCommunaux.size() == 1) {
				compteCommunaux = Arrays.asList(compteCommunaux.get(0).split("\\s|;|,"));
			}

			Set<String> compteCommunauxSet = new HashSet<String>(compteCommunaux);

			for (String compteCommunal : compteCommunauxSet) {
				if (compteCommunal != null && compteCommunal.length() > 0) {
					ObjectRequest objectRequest = new ObjectRequest();
					objectRequest.setType(CadastrappConstants.CODE_DEMANDEUR_COMPTE_COMMUNAL);
					objectRequest.setComptecommunal(compteCommunal.split("\\|")[0]);
					objectRequest.setBp(compteCommunal.split("\\|")[1]);
					objectRequest.setRp(compteCommunal.split("\\|")[2]);

					objectRequestSet.add(objectRequest);
				}
			}

		}

		// Add coProprietes to request information
		if (coProprietes != null && !coProprietes.isEmpty()) {

			if (coProprietes.size() == 1) {
				coProprietes = Arrays.asList(coProprietes.get(0).split("\\s|;|,"));
			}

			Set<String> coProprietesSet = new HashSet<String>(coProprietes);

			for (String comptePropriete : coProprietesSet) {
				if (comptePropriete != null && comptePropriete.length() > 0) {
					ObjectRequest objectRequest = new ObjectRequest();
					objectRequest.setType(CadastrappConstants.CODE_DEMANDEUR_COPROPRIETE);
					
					objectRequest.setComptecommunal(comptePropriete.split("\\|")[0]);
					objectRequest.setParcelle(comptePropriete.split("\\|")[1]);
					objectRequest.setBp(comptePropriete.split("\\|")[2]);
					objectRequest.setRp(comptePropriete.split("\\|")[3]);
					objectRequestSet.add(objectRequest);
				}
			}
		}

		// Add parcelleId to request information
		if (parcelleIds != null && !parcelleIds.isEmpty()) {

			if (parcelleIds.size() == 1) {
				parcelleIds = Arrays.asList(parcelleIds.get(0).split("\\s|;|,"));
			}

			Set<String> parcellesSet = new HashSet<String>(parcelleIds);

			for (String parcelle : parcellesSet) {
				if (parcelle != null && parcelle.length() > 0) {
					ObjectRequest objectRequest = new ObjectRequest();
					objectRequest.setType(CadastrappConstants.CODE_DEMANDEUR_PARCELLE_ID);
					objectRequest.setParcelle(parcelle.split("\\|")[0]);
					objectRequest.setBp(parcelle.split("\\|")[1]);
					objectRequest.setRp(parcelle.split("\\|")[2]);

					objectRequestSet.add(objectRequest);
				}
			}
		}
		
		// Add parcelles to request information
		if (parcelles != null && !parcelles.isEmpty()) {

			Set<String> parcellesSet = new HashSet<String>(parcelles);

			for (String parcelle : parcellesSet) {
				if (parcelle != null && parcelle.length() > 0) {
					ObjectRequest objectRequest = new ObjectRequest();
					objectRequest.setType(CadastrappConstants.CODE_DEMANDEUR_PARCELLE);
					objectRequest.setCommune(parcelle.split("\\|")[0]);
					objectRequest.setSection(parcelle.split("\\|")[1]);
					objectRequest.setNumero(parcelle.split("\\|")[2]);
					objectRequest.setBp(parcelle.split("\\|")[3]);
					objectRequest.setRp(parcelle.split("\\|")[4]);
					objectRequestSet.add(objectRequest);
				}
			}
		}
		// Add proprietaire to request information
		if (proprietaires != null && !proprietaires.isEmpty()) {

			Set<String> proprietairesSet = new HashSet<String>(proprietaires);
			for (String proprio : proprietairesSet) {
				if (proprio != null && proprio.length() > 0) {
					ObjectRequest objectRequest = new ObjectRequest();
					objectRequest.setType(CadastrappConstants.CODE_DEMANDEUR_PROPRIETAIRE);
					objectRequest.setCommune(proprio.split("\\|")[0]);
					objectRequest.setProprietaire(proprio.split("\\|")[1]);
					objectRequest.setBp(proprio.split("\\|")[2]);
					objectRequest.setRp(proprio.split("\\|")[3]);			
					objectRequestSet.add(objectRequest);
				}
			}
		}
				
		// Add proprietaire to request information
		if (lotCoproprietes != null && !lotCoproprietes.isEmpty()) {
			
			Set<String> lotCoproprietesSet = new HashSet<String>(lotCoproprietes);
			for (String lotProprio : lotCoproprietesSet) {
				if (lotProprio != null && lotProprio.length() > 0) {
					ObjectRequest objectRequest = new ObjectRequest();
					objectRequest.setType(CadastrappConstants.CODE_DEMANDEUR_LOT_COPROPRIETE);
					objectRequest.setCommune(lotProprio.split("\\|")[0]);
					objectRequest.setSection(lotProprio.split("\\|")[1]);
					objectRequest.setNumero(lotProprio.split("\\|")[2]);
					objectRequest.setProprietaire(lotProprio.split("\\|")[3]);
					objectRequest.setBp(lotProprio.split("\\|")[4]);
					objectRequest.setRp(lotProprio.split("\\|")[5]);
					objectRequestSet.add(objectRequest);
				}
			}
		}

		informationRequest.setObjectNumber(objectRequestSet.size());
		informationRequest.setObjectsRequest(objectRequestSet);

		if (logger.isDebugEnabled()) {
			logger.debug("Inforamtion request : " + informationRequest.toString());
		}

		final InformationRequest informationRequestSaved = requestRepository.save(informationRequest);

		result = "OK";

		resultInformation.put("insertion", result);
		resultInformation.put("message", message);
		resultInformation.put("id", informationRequestSaved.getRequestId());

		return resultInformation;
	}

}
