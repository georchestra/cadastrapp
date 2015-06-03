package org.georchestra.cadastrapp.service;



import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;


@Path("/getCommune")
public class CommuneController extends CadController{
	
	final static Logger logger = LoggerFactory.getLogger(CommuneController.class);
	
    @GET
    @Path("/all")
    @Produces("application/json")
    public List<Map<String,Object>> getCommunesList(@Context HttpHeaders headers){
    	
	
    	StringBuilder queryBuilder = new StringBuilder();
    	queryBuilder.append("select ccoinsee, libcom, libcom_min from ");
    	
    	//TODO get schema and table in properties
    	queryBuilder.append("cadastreapp_qgis.commune ");
    	
    	queryBuilder.append("where ccocom is not null ");
    	queryBuilder.append(addAuthorizationFiltering(headers));
    	queryBuilder.append(";");
    	
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String,Object>> communes = jdbcTemplate.queryForList(queryBuilder.toString());
            
              
        return communes;
    }
    
    @GET
    @Produces("application/json")
    /**
     *  GetCommune returns list of communes depending user group authorization
     *  
     * @param libcom_partiel
     * @param ccoinsee_partiel
     * @return JSON export of commune view
     * 
     * @throws SQLException
     */
    public List<Map<String,Object>> getCommunesListByLibcom(
    			@QueryParam("libcom_partiel") String libCom,
    			@QueryParam("ccoinsee_partiel") String ccoinsee){
    	
    	List<Map<String,Object>> communes = null;
    	
    	StringBuilder queryBuilder = new StringBuilder();
    	
    	queryBuilder.append("select ccoinsee, libcom, libcom_min from ");
    	
    	//TODO check in configuration
    	queryBuilder.append("cadastreapp_qgis.commune");
    	
    	// If one of the parameter is present only one clause
    	 if((libCom == null || libCom.isEmpty()) && (ccoinsee == null || ccoinsee.isEmpty())){
    		 logger.warn("No parameter in request");
    	 }
    	
    	 // Check if libcom is not null
    	 // TODO See if we limit search with n characters
    	if(libCom != null && !libCom.isEmpty()){
	    	
    		// Remove all accent from url
    		String newLibCom = StringUtils.stripAccents(libCom);
	    		    	 
    		queryBuilder.append(createLikeClauseRequest("libcom", newLibCom.toUpperCase()));      
    	}
    	
    	// Check if ccoinsee is nont null
    	if(ccoinsee != null && !ccoinsee.isEmpty()){
    		
    		// Special case when code commune on 5 characters is given
    		// Convert 350206 to 35%206 for query
    		if(5 == ccoinsee.length()){
    			ccoinsee = ccoinsee.substring(0, 2) + "%" +ccoinsee.substring(2);    			
    		} 	
    		queryBuilder.append(createLikeClauseRequest("ccoinsee", ccoinsee));     
  		
    	}
    	queryBuilder.append(" and ccocom is not null;"); 
         
    	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        communes = jdbcTemplate.queryForList(queryBuilder.toString());
        
    	// Return value providers will convert to JSON
        return communes;
    }
    
}
