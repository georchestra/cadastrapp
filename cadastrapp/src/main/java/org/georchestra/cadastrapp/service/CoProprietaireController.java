package org.georchestra.cadastrapp.service;

import java.sql.SQLException;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

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
    	
    	List<String> lots = null;
    	List<Map<String,Object>> coProprietaires = null;
    	List<Map<String,Object>> lotsInformation = new ArrayList<Map<String,Object>>();
    	
    	StringBuilder queryBuilder = new StringBuilder();
    	queryBuilder.append("select distinct lot from ");
    	queryBuilder.append(databaseSchema);
    	queryBuilder.append(".parcelle where parcelle = ?");
  
	    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	    lots = jdbcTemplate.queryForList(queryBuilder.toString(), String.class, parcelle);
	    
	    // for each lot
	    // TODO search information in view to be done
	    Map<String,Object> lot1 = new HashMap<String,Object>();
	    lot1.put("dnulot", lots.get(0));
	    lotsInformation.add(0, lot1);
		
	    // only for CNIL1 and CNIL2
	    if (getUserCNILLevel(headers)>0){
	    	StringBuilder queryProprietaireBuilder = new StringBuilder();
	    	queryProprietaireBuilder.append("select ddenom, dnomlp, dprnlp, epxnee, dnomcp, dprncp, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro_lib from ");
	    	queryProprietaireBuilder.append(databaseSchema);
	    	queryProprietaireBuilder.append(".proprietaire where lot = ? LIMIT 25;");
	    	
	    	coProprietaires = jdbcTemplate.queryForList(queryProprietaireBuilder.toString(), lots.get(0));
	    	
	    	logger.debug("Liste de lot : " + coProprietaires);
    	}
   	    
	    lotsInformation.get(0).put("proprietaires", coProprietaires);
              
        return lotsInformation;
    }
}

