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

import org.springframework.jdbc.core.JdbcTemplate;



@Path("/getCommune")
public class CommuneController {
	
	@Resource(name="dbDataSource")
	private DataSource dataSource;

    @GET
    @Path("/id/{input}")
    @Produces("application/json")
    public List<Map<String,Object>> getCommuneList(@PathParam("input") String input) throws SQLException {
    	
	
    	String query = "select ccoinsee, libcom, libcom_min from cadastreapp_qgis.commune";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String,Object>> communes = jdbcTemplate.queryForList(query);
            
              
        return communes;
    }

}

