package org.georchestra.cadastrapp.service;


import java.sql.SQLException;
import java.util.ArrayList;
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


@Path("/getProprietaire")
public class ProprietaireController extends CadController{
	
	final static Logger logger = LoggerFactory.getLogger(ProprietaireController.class);

    @GET
    @Produces("application/json")
    /**
     * This will return information about owners in JSON format
     * 
     * @param headers headers from request used to filter search using LDAP Roles
     * @param dnomlpPartiel partial name to be search, must be have at least 3 char
     * @param ccoinsee  code commune like 630103 (codep + codir + cocom)
     * 					ccoinsee should be on 6 char, if only 5 we deduce that codir is not present
     * @param dnupro id to be search, a same dnupro can be found in several commune
     * 
     * @return 
     * @throws SQLException
     */
    public List<Map<String,Object>> getProprietairesList(
    			@Context HttpHeaders headers,
    			@QueryParam("dnomlp_partiel") String dnomlpPartiel,
    			@QueryParam("ccoinsee") String ccoinsee,
    			@QueryParam("dnupro") String dnupro
    			) throws SQLException {
    	
    	List<Map<String,Object>> proprietaires = null;
    	List<String> queryParams = new ArrayList<String>();
    	
    	if (getUserCNILLevel(headers)>0){
    	    
    		// No search if all parameters are null or dnomlpPariel less than 3 char
	    	if((dnomlpPartiel != null && !dnomlpPartiel.isEmpty() && 3<dnomlpPartiel.length()) 
	    			||  ccoinsee!=null || dnupro!=null){
	    		
	    		StringBuilder queryBuilder = new StringBuilder();
		       queryBuilder.append("select dnomlp, dprnlp, epxnee, dnomcp, dprncp, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro_lib");
		       queryBuilder.append(" from ");
		       queryBuilder.append(databaseSchema);
		       queryBuilder.append(".proprietaire");
		      
		       queryBuilder.append(createLikeClauseRequest("dnomlp", dnomlpPartiel, queryParams));
		      
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
		      
		       queryBuilder.append(createEqualsClauseRequest("dnupro", dnupro, queryParams));
		       queryBuilder.append(finalizeQuery());
	 	       
		    	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		        proprietaires = jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
	    	}
	    	//TODO add exception management
	    	else{
			//log empty request
			logger.info("Null or less than 3 characters for dnomlp in request");
		}
    	}else{
    		logger.info("User does not have rights to see thoses informations");
    	}
              
        return proprietaires;
    }
}

