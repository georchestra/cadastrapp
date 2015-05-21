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


@Path("/getParcelle")
public class ParcelleController {
	
	@Resource(name="dbDataSource")
	private DataSource dataSource;

    @GET
    @Path("/parcelle/{parcelle}")
    @Produces("application/json")
    public List<Map<String,Object>> getParcelleById(@PathParam("parcelle") String parcelle) throws SQLException {
    	
    	List<Map<String,Object>> parcelles = null;
    	
    	String query = "select parcelle, ccodep, ccodir, ccocom, ccopre, ccosec, dnupla, dnvoiri, dindic, dvoilib "
    			+ "from cadastreapp_qgis.parcelle where parcelle LIKE '%" + parcelle + "%';";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        parcelles = jdbcTemplate.queryForList(query);
            
              
        return parcelles;
    }
    
    
    @GET
    @Path("/ccodpe/{ccodep}/ccocom/{ccocom}/ccopre/{ccopre}")
    @Produces("application/json")
    public List<Map<String,Object>> getParcelleListByCode(@PathParam("ccodep") String ccodep, @PathParam("ccocom") String ccocom, @PathParam("ccopre") String ccopre) throws SQLException {
    	
    	 List<Map<String,Object>> parcelles = null;
    	
    	String query = "select parcelle, ccodep, ccodir, ccocom, ccopre, ccosec, dnupla, dnvoiri, dindic, dvoilib "
    			+ "from cadastreapp_qgis.parcelle where ccodep ='"+ ccodep + "' and ccocom = '"+ ccocom + "' and ccopre = '" + ccopre + "';";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        parcelles = jdbcTemplate.queryForList(query);
            
              
        return parcelles;
    }

}

