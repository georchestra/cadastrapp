package org.georchestra.cadastrapp.service;


import javax.annotation.Resource;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;



@Path("/getCommune")
public class CommuneController {
	
	final static Logger logger = LoggerFactory.getLogger(CommuneController.class);
	
	@Resource(name="dbDataSource")
	private DataSource dataSource;

    @GET
    @Path("/all")
    @Produces("application/json")
    public List<Map<String,Object>> getCommunesList() throws SQLException {
    	
	
    	String query = "select ccoinsee, libcom, libcom_min from cadastreapp_qgis.commune where ccocom is not null;";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String,Object>> communes = jdbcTemplate.queryForList(query);
            
              
        return communes;
    }
    
    @GET
    @Path("/libcom_partiel/{input}")
    @Produces("application/json")
    public List<Map<String,Object>> getCommunesListByLibcom(@PathParam("input") String libCom) throws SQLException {
    	
    	 List<Map<String,Object>> communes = null;
    	
    	 // Check if libcom is not null
    	 // TODO See if we limit search with n characters
    	if(libCom != null && !libCom.isEmpty()){
	    	
    		// Remove all accent from url
    		String newLibCom = StringUtils.stripAccents(libCom);
	    		    	 
    		// Create and execute request
	    	String query = "select ccoinsee, libcom, libcom_min from cadastreapp_qgis.commune where libcom LIKE '" + newLibCom.toUpperCase() +"%' and ccocom is not null; ;";
	        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	        communes = jdbcTemplate.queryForList(query);
            
    	}
    	//TODO add exception management
    	else{
    		//log empty request
    		logger.info("Null or Empty libcom in request");
    	}
         
    	// Return value providers will convert to JSON
        return communes;
    }
    
    @GET
    @Path("/ccoinsee_partiel/{input}")
    @Produces("application/json")
    public List<Map<String,Object>> getCommunesListByCCOINSEE(@PathParam("input") String ccoinsee) throws SQLException {
    	
    	 List<Map<String,Object>> communes = null;
    	
    	 // Check if libcom is not null
    	 // TODO See if we limit search with n characters
    	if(ccoinsee != null && !ccoinsee.isEmpty()){
    		
    		// Special case when code commune on 5 characters is given
    		// Convert 350206 to 35%206 for query
    		if(5 == ccoinsee.length()){
    			ccoinsee = ccoinsee.substring(0, 2) + "%" +ccoinsee.substring(2);    			
    		}
	    	    		    		    	 
    		// Create and execute request
	    	String query = "select ccoinsee, libcom, libcom_min from cadastreapp_qgis.commune where ccoinsee LIKE '%" + ccoinsee +"%' and ccocom is not null;;";
	        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	        communes = jdbcTemplate.queryForList(query);
            
    	}else{
    		//log empty request
    		logger.info("Null or Empty ccoinsee in request");
    	}
         
    	// Return value providers will convert to JSON
        return communes;
    }
    
    
    @GET
    @Path("libcom_partiel/{libcom}/ccoinsee_partiel/{ccoinsee}")
    @Produces("application/json")
    public List<Map<String,Object>> getCommunesListByCCOINSEE(@PathParam("libcom") String libCom, @PathParam("ccoinsee") String ccoinsee) throws SQLException {
    	
    	 List<Map<String,Object>> communes = null;
    	
    	 // If no libcomm call CCOInsee
    	 if(libCom == null || libCom.isEmpty()){
    		 communes = getCommunesListByCCOINSEE(ccoinsee);
    	 }
    	 // If non ccoinse call libcom
    	 else if(ccoinsee == null || ccoinsee.isEmpty()){
    		 communes = getCommunesListByLibcom(libCom);
    	}
    	else{
    	
    		// Special case when code commune on 5 characters is given
    		// Convert 350206 to 35%206 for query
    		if(5 == ccoinsee.length()){
    			ccoinsee = ccoinsee.substring(0, 2) + "%" +ccoinsee.substring(2);    			
    		}
    		// request in without accents
    		String newLibCom = StringUtils.stripAccents(libCom);
	    	    		    		    	 
    		// Create and execute request
	    	String query = "select ccoinsee, libcom, libcom_min from cadastreapp_qgis.commune where ccoinsee LIKE '%" + ccoinsee +"%' and libcom LIKE '" + newLibCom.toUpperCase() +"%' ;";
	        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	        communes = jdbcTemplate.queryForList(query);
            
    	}
         // TODO exception management
    	 
    	// Return value providers will convert to JSON
        return communes;
    }


}

