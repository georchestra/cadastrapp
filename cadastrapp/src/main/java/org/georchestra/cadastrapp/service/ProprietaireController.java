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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;


@Path("/getProprietaire")
public class ProprietaireController {
	
	final static Logger logger = LoggerFactory.getLogger(ProprietaireController.class);
	
	@Resource(name="dbDataSource")
	private DataSource dataSource;

    @GET
    @Path("/dnomlp/{input}")
    @Produces("application/json")
    public List<Map<String,Object>> getProprietairesList(@PathParam("input") String dnomlp) throws SQLException {
    	
    	List<Map<String,Object>> proprietaires = null;
    	
    	if(dnomlp != null && !dnomlp.isEmpty() && 3<dnomlp.length()){
	    	String query = "select dnomlp, dpmlp from cadastreapp_qgis.proprietaire where dnomlp LIKE '%"+dnomlp+"%';";
	        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	        proprietaires = jdbcTemplate.queryForList(query);
    	}
    	//TODO add exception management
    	else{
		//log empty request
		logger.info("Null or Empty libcom in request");
	}
              
        return proprietaires;
    }

}

