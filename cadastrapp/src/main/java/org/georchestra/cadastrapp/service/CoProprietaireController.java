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

public class CoProprietaireController extends CadController{
	
	final static Logger logger = LoggerFactory.getLogger(CoProprietaireController.class);

	@Path("/getCoProprietaire")
    @GET
    @Produces("application/json")
    /**
     * 
     * /getCoProprietaire 
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
    	List<Map<String,Object>> lotsInformation = new ArrayList<Map<String,Object>>();
    	
    	StringBuilder queryBuilder = new StringBuilder();
    	queryBuilder.append("select distinct dnulot from ");
    	queryBuilder.append(databaseSchema);
    	queryBuilder.append(".proprietaire_parcelle where parcelle = ? and dnulot != '0'");
  
	    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	    lots = jdbcTemplate.queryForList(queryBuilder.toString(), String.class, parcelle);
  	    
	    int currentIndex = 0;
	    // for each lot
	    for(String lot : lots){
	    	
	    	List<Map<String,Object>> coProprietaires = null;
	    	Map<String,Object> lotMap = new HashMap<String,Object>();
		    lotMap.put("dnulot", lot);
		    
	    	// only for CNIL1 and CNIL2
		    if (getUserCNILLevel(headers)>0){
		    	StringBuilder queryProprietaireBuilder = new StringBuilder();
		    	queryProprietaireBuilder.append("select ddenom, dnomlp, dprnlp, epxnee, dnomcp, dprncp, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro_lib from ");
		    	queryProprietaireBuilder.append(databaseSchema);
		    	queryProprietaireBuilder.append(".proprietaire prop, ");
		    	queryProprietaireBuilder.append(databaseSchema);
		    	queryProprietaireBuilder.append(".proprietaire_parcelle propar ");
		    	queryProprietaireBuilder.append("where propar.dnulot = ? ;");
		    	
		    	coProprietaires = jdbcTemplate.queryForList(queryProprietaireBuilder.toString(), lot);
			    lotMap.put("proprietaires", coProprietaires);
	    	}
		    lotsInformation.add(currentIndex, lotMap);
	    }    
              
        return lotsInformation;
    }
}

