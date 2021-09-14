package org.georchestra.cadastrapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class CommuneController extends CadController{
	
	final static Logger logger = LoggerFactory.getLogger(CommuneController.class);
	
	@RequestMapping(path = "/getCommune/", produces = {MediaType.APPLICATION_JSON_VALUE}, method= {RequestMethod.GET})
    /**
     *  /getCommune 
     *  
     *  Will retunr list of communes depending user group geographical authorization
     *  
     * @param libcom, should be the n first characters of a libcom
     * 			n first characters are defined with the minNbCharForSearch from cadastrapp.properties
     * @param cgocommune, from n to six characters
     * 			cgocommune for Code Geographique Officile Commune
     * 			cgocommune = code departement + code arrondissement + code commune
     * 
     * 	exemple : 
     * @return JSON export of commune view
     * 
     * @throws SQLException
     */
    public	@ResponseBody List<Map<String,Object>> getCommunesList(
    			@RequestParam(name= "libcom", required= false) String libCom,
    			@RequestParam(name= "cgocommune", required= false) String cgoCommune){
    	
    	List<Map<String,Object>> communes = new ArrayList<Map<String, Object>>();
    	List<String> queryParams = new ArrayList<String>();
   	
    	// If one of the parameter is present only one clause
    	 if((libCom == null || libCom.isEmpty() || libCom.length()<minNbCharForSearch) 
    			 && (cgoCommune == null || cgoCommune.isEmpty())){
    		 logger.warn("No parameter in request or not enough characters");
    	 }
    	 else{
    		 boolean isWhereAdded = false;
    		 StringBuilder queryBuilder = new StringBuilder();
    	    	
    		 queryBuilder.append("select cgocommune, libcom, libcom_min from ");
    		 queryBuilder.append(databaseSchema);
    		 queryBuilder.append(".commune");
    		 
	    	 // Check if libcom is not null
	    	if(libCom != null && !libCom.isEmpty() && libCom.length()>=minNbCharForSearch){
		    	
	    		// Remove all accent from url	    		    	 
	    		isWhereAdded = createRightLikeClauseRequest(isWhereAdded, queryBuilder, "libcom_maj", libCom.toUpperCase(), queryParams);      
	    	}
	    	else{
	    		logger.info("LibCom has not enough characters to launch research with libCom");
	    	}
	    	
	    	// Check if cgocommune is not null
	    	if(cgoCommune != null && !cgoCommune.isEmpty()){
	    		
	    		// Special case when code commune on 5 characters is given
	    		// Convert 350206 to 35%206 for query
	    		if(5 == cgoCommune.length()){
	    			cgoCommune = cgoCommune.substring(0, 2) + "%" +cgoCommune.substring(2);  
	    			logger.debug("Missing ccodir in cgoCommune parameters adding it");
	    		} 	
	    		// Like query because cgocommune can be only 1 or 2 digit
	    		createLikeClauseRequest(isWhereAdded, queryBuilder,"cgocommune", cgoCommune, queryParams);     
	  		
	    	}
	    	if(isSearchFiltered){
	    		queryBuilder.append(addAuthorizationFiltering());
	    	}
	    	queryBuilder.append(" order by libcom ");
	         
	    	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	        communes = jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
    	 }
        
    	// Return value providers will convert to JSON
        return communes;
    }
    
}
