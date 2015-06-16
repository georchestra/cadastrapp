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
import org.springframework.jdbc.core.JdbcTemplate;


@Path("/getProprietaire")
public class ProprietaireController extends CadController{
	
	final static Logger logger = LoggerFactory.getLogger(ProprietaireController.class);

    @GET
    @Produces("application/json")
    public List<Map<String,Object>> getProprietairesList(
    			@Context HttpHeaders headers,
    			@QueryParam("dnomlp_partiel") String dnomlpPartiel,
    			@QueryParam("ccoinsee") String ccoinsee,
    			@QueryParam("dnupro") String dnupro
    			) throws SQLException {
    	
    	List<Map<String,Object>> proprietaires = null;
    	
    	if (getUserCNILLevel(headers)>0){
    	    
    		// No search if all parameters are null or dnomlpPariel less than 3 char
	    	if((dnomlpPartiel != null && !dnomlpPartiel.isEmpty() && 3<dnomlpPartiel.length()) 
	    			||  ccoinsee!=null || dnupro!=null){
	    		StringBuilder queryBuilder = new StringBuilder();
		       queryBuilder.append("select dnomlp, dprnlp, epxnee, dnomcp, dprncp, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro_lib");
		       queryBuilder.append(" from ");
		       queryBuilder.append(databaseSchema);
		       queryBuilder.append(".proprietaire");
		       queryBuilder.append(createLikeClauseRequest("dnomlp", dnomlpPartiel));
		       if (ccoinsee!=null && !ccoinsee.isEmpty() && ccoinsee.length()>3){
		    	  
		    	   int size = ccoinsee.length();
		    	   
		    	   String ccodep = ccoinsee.substring(0, 2);
		    	   queryBuilder.append(createEqualsClauseRequest("ccodep", ccodep));
		    	   
		    	   String ccocom = ccoinsee.substring(size-3, size);
		    	   queryBuilder.append(createEqualsClauseRequest("ccocom", ccocom));
		    	       	   
		    		if(size==5){
		    		   String ccodir = ccoinsee.substring(2, 3);
		    		   queryBuilder.append(createEqualsClauseRequest("ccodir", ccodir));
		    	   }
		    	   
		       }
		      
		       queryBuilder.append(createEqualsClauseRequest("dnupro", dnupro));
		       queryBuilder.append(finalizeQuery());
	 	       
		    	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		        proprietaires = jdbcTemplate.queryForList(queryBuilder.toString());
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

