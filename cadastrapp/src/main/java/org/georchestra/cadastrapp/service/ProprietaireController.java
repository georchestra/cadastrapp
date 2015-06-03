package org.georchestra.cadastrapp.service;



import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;


@Path("/getProprietaire")
public class ProprietaireController extends CadController{
	
	final static Logger logger = LoggerFactory.getLogger(ProprietaireController.class);

    @GET
    @Produces("application/json")
    public List<Map<String,Object>> getProprietairesList(@QueryParam("dnomlp") String dnomlp) throws SQLException {
    	
    	List<Map<String,Object>> proprietaires = null;
    	
    	    	
    	if(dnomlp != null && !dnomlp.isEmpty() && 3<dnomlp.length()){
    		//TODO change request with dpmlp
	    	//String query = "select dnomlp, dpmlp, dprnlp, expnee, dnomcp, dprncp, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro_lib from cadastreapp_qgis.proprietaire where dnomlp LIKE '%"+dnomlp+"%';";
	       StringBuilder queryBuilder = new StringBuilder();
	       queryBuilder.append("select dnomlp, dprnlp, epxnee, dnomcp, dprncp, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro_lib");
	       queryBuilder.append(" from ");
	       //TODO change for properties
	       queryBuilder.append("cadastreapp_qgis.proprietaire");
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
              
        return proprietaires;
    }
    
    
    @POST
    @Path("/fromFile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String getProprietairesListFromFile(@FormParam("city") String city, @FormParam("filePath") String fileContent, @FormParam("jsonData") String jsonData) throws Exception {
    	
    	return "OK";
    }
    
    @GET
    @Path("/toFile")
    public Response getProprietairesListToFile() {
    	//TODO : fichier de test
    	File file = new File("/home/gfi/test.pdf");
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=" + file.getName());
		return response.build();
    }
}

