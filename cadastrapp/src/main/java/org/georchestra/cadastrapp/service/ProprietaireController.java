package org.georchestra.cadastrapp.service;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class ProprietaireController extends CadController{
	
	final static Logger logger = LoggerFactory.getLogger(ProprietaireController.class);

    @GET
    @Path("/getProprietaire")
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * This will return information about owners in JSON format
     * 
     * You can call this service with different input, but some are mandatory
     * 
     * User must be at least CNIL 1 level to get information from this service
     * 
     *   If you call with dnupro, cgocommune is mandatory
     *   If you call with dnomlp, cgocommune is mandatory and dnomlp must me at least n chars
     *   
     *   in addition results are filtered by the geographical limitation of the user group
     * 
     * @param headers headers from request used to filter search using LDAP Roles
     * @param dnomlp partial name to be search, must be have at least n char
     * @param cgocommun  code commune like 630103 (codep + codir + cocom)
     * 					cgocommun should be on 6 char
     * @param dnupro id to be search, a same dnupro can be found in several commune
     * @param comptecommunal id specific for a owner
     * 
     * @param details -> change list of fields in result 0 dy default or if not present
     * 					0 : dnomlp, dprnlp
     * 					1 : dnomlp, dprnlp, epxnee, dnomcp, dprncp, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro_lib, comptecommunal will be return
     * 					2 : comptecommunal
     * 					 
     * 
     * @return list of information about proprietaire depending on details level asked
     * 
     * @throws SQLException
     */
    public List<Map<String,Object>> getProprietairesList(
    			@Context HttpHeaders headers,
    			@QueryParam("dnomlp") String dnomlp,
    			@QueryParam("cgocommune") String cgocommune,
    			@QueryParam("dnupro") String dnupro,
    			@QueryParam("comptecommunal") String compteCommunal,
    			@QueryParam("globalname") String globalName,
    			@DefaultValue("0") @QueryParam("details") int details
    			) throws SQLException {
    	
    	// Init list to return response even if nothing in it.
    	List<Map<String,Object>> proprietaires = new ArrayList<Map<String,Object>>();;
    	List<String> queryParams = new ArrayList<String>();
    	
    	logger.info("details : " + details);
    	// User need to be at least CNIL1 level
    	if (getUserCNILLevel(headers)>0){
    	    
    		// No search if all parameters are null or dnomlpPariel less than n char
    		// when searching by dnupro, cgocommune is mandatory
    		// when searching bu dnomlp, cgocommune is mandatory
	    	if((dnomlp != null && !dnomlp.isEmpty() && minNbCharForSearch <= dnomlp.length() && cgocommune!=null && cgoCommuneLength == cgocommune.length()) 
	    			|| (globalName != null && !globalName.isEmpty() && minNbCharForSearch <= globalName.length() && cgocommune!=null && cgoCommuneLength == cgocommune.length()) 
	    			|| (cgocommune!=null &&  cgoCommuneLength == cgocommune.length() && dnupro!=null && dnupro.length()>0)
	    			|| (compteCommunal != null && compteCommunal.length()>0)){
	    		
	    		StringBuilder queryBuilder = new StringBuilder();
	    		
	    		logger.info("details : " + details);
	    		
	    		if(details == 2){
	    			queryBuilder.append("select distinct comptecommunal, ddenom ");   		    	   			    
	    		}
	    		else if(details == 1){
	    			queryBuilder.append("select dnomlp, dprnlp, epxnee, dnomcp, dprncp, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro_lib, comptecommunal ");   			    
	    		}
	    		else{
	    			queryBuilder.append("select distinct dnomlp, dprnlp, dnomcp, dprncp");				    		    		
	    		}
		        queryBuilder.append(" from ");
		        queryBuilder.append(databaseSchema);
		        queryBuilder.append(".proprietaire");

    		   queryBuilder.append(createEqualsClauseRequest("cgocommune", cgocommune, queryParams));
    		   
    		   // dnomlp can be null here
    		   if(dnomlp!=null){
			       queryBuilder.append(" and UPPER(dnomlp) LIKE UPPER(?) ");
			       queryParams.add("%"+dnomlp+"%");
    		   }
    		   
    		   // usual can be null here
    		   if(globalName!=null){
    			   // replace all space by %
    			   globalName = globalName.replace(' ', '%');
			       queryBuilder.append(" and (UPPER(dnomlp||' '||dprnlp) LIKE UPPER(?) or UPPER(dnomcp||' '||dprncp) LIKE UPPER(?) or UPPER(ddenom) LIKE UPPER(?))");
			       queryParams.add(globalName+"%");
			       queryParams.add(globalName+"%");
			       queryParams.add(globalName+"%");		       
    		   }
		       
		       queryBuilder.append(createEqualsClauseRequest("dnupro", dnupro, queryParams));
		       queryBuilder.append(createEqualsClauseRequest("comptecommunal", compteCommunal, queryParams));
		      
		       queryBuilder.append(addAuthorizationFiltering(headers));
		      
		       if(details != 2){
		    	   queryBuilder.append("order by dnomlp, dprnlp, dnomcp,  dprncp limit 25 ");
		       }
		       queryBuilder.append(finalizeQuery());
	 	       
		    	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		        proprietaires = jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
	    	}
	    	else{
			//log empty request
			logger.info("Missing cgocommune and dnupro or less than 3 characters for dnomlp in request");
		}
    	}else{
    		logger.info("User does not have rights to see thoses informations");
    	}
              
        return proprietaires;
    }
    
    
    @GET
    @Path("/getProprietairesByParcelles")
    @Produces("application/json")
    /**
     * This will return information about owners in JSON format
     *
     * 
     * @param headers headers from request used to filter search using LDAP Roles
     * @param parcelleList
     * 					 
     * 
     * @return list of information about all proprietaire of given parcelles 
     * 
     * @throws SQLException
     */
    public List<Map<String,Object>> getProprietairesByParcelle(
    			@Context HttpHeaders headers,
    			@QueryParam("parcelles") List<String> parcelleList
    			) throws SQLException {
    	
    	// Init list to return response even if nothing in it.
    	List<Map<String,Object>> proprietaires = new ArrayList<Map<String,Object>>();;
    	
    	// User need to be at least CNIL1 level
    	if (getUserCNILLevel(headers)>0){
    	    
    		if(parcelleList != null && !parcelleList.isEmpty()){
	    		
    			StringBuilder queryBuilder = new StringBuilder();
    			queryBuilder.append("select parcelle, comptecommunal, dnomlp, dprnlp, epxnee, dnomcp, dprncp, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro_lib");   			    
    			queryBuilder.append("from ");
    			queryBuilder.append(databaseSchema);
    			queryBuilder.append(".proprietaire prop, ");
    			queryBuilder.append(databaseSchema);
    			queryBuilder.append(".proprietaire_parcelle proparc ");
    			queryBuilder.append("where proparc.parcelle IN (?) and prop.comptecommunal = proparc.comptecommunal");
    			queryBuilder.append(addAuthorizationFiltering(headers));
    			queryBuilder.append(finalizeQuery());
	 	       
		    	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		        proprietaires = jdbcTemplate.queryForList(queryBuilder.toString(), parcelleList.toArray());
	    	}
	    	else{
			//log empty request
			logger.info("Parcelle Id List is empty nothing to search");
		}
    	}else{
    		logger.info("User does not have rights to see thoses informations");
    	}
              
        return proprietaires;
    }
}

