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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;


@Path("/getProprietaire")
public class ProprietaireController extends CadController{
	
	final static Logger logger = LoggerFactory.getLogger(ProprietaireController.class);

    @GET
    @Produces("application/json")
    /**
     * This will return information about owners in JSON format
     * 
     * You can call this service with different input, but some are mandatory
     * 
     * User must be at least CNIL 1 level to get information from this service
     * 
     *   If you call with dnupro, ccoinsee is mandatory
     *   If you call with dnomlp, ccoinsee is mandatory and dnomlp must me at least 3 chars
     * 
     * @param headers headers from request used to filter search using LDAP Roles
     * @param dnomlp partial name to be search, must be have at least 3 char
     * @param ccoinsee  code commune like 630103 (codep + codir + cocom)
     * 					ccoinsee should be on 6 char, if only 5 we deduce that codir is not present
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
    			@QueryParam("ccoinsee") String ccoinsee,
    			@QueryParam("dnupro") String dnupro,
    			@QueryParam("compteCommunal") String compteCommunal,
    			@DefaultValue("0") @QueryParam("details") int details
    			) throws SQLException {
    	
    	// Init list to return response even if nothing in it.
    	List<Map<String,Object>> proprietaires = new ArrayList<Map<String,Object>>();;
    	List<String> queryParams = new ArrayList<String>();
    	
    	logger.info("details : " + details);
    	// User need to be at least CNIL1 level
    	if (getUserCNILLevel(headers)>0){
    	    
    		// No search if all parameters are null or dnomlpPariel less than 3 char
	    	if((dnomlp != null && !dnomlp.isEmpty() && 2<dnomlp.length() && ccoinsee!=null) 
	    			|| (ccoinsee!=null &&  ccoinsee.length()>0 &&dnupro!=null && dnupro.length()>0)
	    			|| (compteCommunal != null && compteCommunal.length()>0)){
	    		
	    		StringBuilder queryBuilder = new StringBuilder();
	    		
	    		logger.info("details : " + details);
	    		
	    		if(details == 2){
	    			queryBuilder.append("select distinct comptecommunal ");   		    	   			    
	    		}
	    		else if(details == 1){
	    			queryBuilder.append("select RTRIM(dnomlp) as dnomlp, RTRIM(dprnlp) as dprnlp, epxnee, dnomcp, RTRIM(dprncp) as dprncp, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro_lib, comptecommunal ");   			    
	    		}
	    		else{
	    			queryBuilder.append("select distinct RTRIM(dnomlp) as dnomlp, RTRIM(dprnlp) as dprnlp ");				    		    		
	    		}
		        queryBuilder.append(" from ");
		        queryBuilder.append(databaseSchema);
		        queryBuilder.append(".proprietaire");
		      
		       //TODO factorize this
		       // no ccoinsee present in view proprietaire, parse it to get ccodep, ccocom and ccodir
		       // exemple ccoinsee : 630103 -> ccodep 63, ccodir 0, ccocom 103
		       if (ccoinsee!=null && !ccoinsee.isEmpty() && ccoinsee.length()>3){
		    	  
		    	   int size = ccoinsee.length();
		    	   
		    	   String ccodep = ccoinsee.substring(0, 2);
		    	   queryBuilder.append(createEqualsClauseRequest("ccodep", ccodep, queryParams));
		    	   
		    	   String ccocom = ccoinsee.substring(size-3, size);
		    	   queryBuilder.append(createEqualsClauseRequest("ccocom", ccocom, queryParams));
		    	    
		    	   // cas when ccoinsee have 6 chars
		    		if(size==5){
		    		   String ccodir = ccoinsee.substring(2, 3);
		    		   queryBuilder.append(createEqualsClauseRequest("ccodir", ccodir, queryParams));
		    	   }  
		       }	      
		       queryBuilder.append(" and UPPER(dnomlp) LIKE UPPER(?) ");
		       queryParams.add("%"+dnomlp+"%");
		       
		       queryBuilder.append(createEqualsClauseRequest("dnupro", dnupro, queryParams));
		       queryBuilder.append(createEqualsClauseRequest("comptecommunal", compteCommunal, queryParams));
		       if(details != 2){
		    	   queryBuilder.append("order by dnomlp, dprnlp ");
		       }
		       queryBuilder.append(finalizeQuery());
	 	       
		    	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		        proprietaires = jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
	    	}
	    	//TODO add exception management
	    	else{
			//log empty request
			logger.info("Missing ccoinsee and dnupro or less than 3 characters for dnomlp in request");
		}
    	}else{
    		logger.info("User does not have rights to see thoses informations");
    	}
              
        return proprietaires;
    }
}

