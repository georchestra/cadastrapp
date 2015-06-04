package org.georchestra.cadastrapp.service;



import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.georchestra.cadastrapp.model.ExtFormResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;


@Path("/getProprietaire")
public class ProprietaireController extends CadController{
	
	final static Logger logger = LoggerFactory.getLogger(ProprietaireController.class);

    @GET
    @Produces("application/json")
    public List<Map<String,Object>> getProprietairesList(@Context HttpHeaders headers,@QueryParam("dnomlp") String dnomlp) throws SQLException {
    	
    	List<Map<String,Object>> proprietaires = null;
    	
    	if (getUserCNILLevel(headers)>0){
    	    	
	    	if(dnomlp != null && !dnomlp.isEmpty() && 3<dnomlp.length()){
	    		//TODO change request with dpmlp
		    	//String query = "select dnomlp, dpmlp, dprnlp, expnee, dnomcp, dprncp, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro_lib from cadastreapp_qgis.proprietaire where dnomlp LIKE '%"+dnomlp+"%';";
		       StringBuilder queryBuilder = new StringBuilder();
		       queryBuilder.append("select dnomlp, dprnlp, epxnee, dnomcp, dprncp, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro_lib");
		       queryBuilder.append(" from ");
		       queryBuilder.append(databaseSchema);
		       queryBuilder.append(".proprietaire");
		       queryBuilder.append(createLikeClauseRequest("dnomlp", dnomlp));
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

