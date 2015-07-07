package org.georchestra.cadastrapp.service;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/getCoProprietaire")
public class CoProprietaireController extends CadController{
	
	final static Logger logger = LoggerFactory.getLogger(CoProprietaireController.class);

    @GET
    @Produces("application/json")
    /**
     * This will return information about owners in JSON format
     * 
     * @param headers headers from request used to filter search using LDAP Roles
     * @param parcelle
     * 
     * @return 
     * @throws SQLException
     */
    
    
    public List<Map<String,Object>> getCoProprietairesList(
    			@Context HttpHeaders headers,
    			@QueryParam("parcelle") String parcelle
    			) throws SQLException {
    	
    	List<Map<String,Object>> coProprietaires = null;
    	
    	//TODO add lot information
    	    	
    	//TODO get proprietaire list
    	if (getUserCNILLevel(headers)>0){
    		
    	}
    	    
              
        return coProprietaires;
    }
}

