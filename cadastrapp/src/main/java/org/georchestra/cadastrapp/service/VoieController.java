package org.georchestra.cadastrapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;


public class VoieController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(VoieController.class);
	
	@GET
	@Path("/getVoie")
	@Produces("application/json")
	/**
	 *  /getVoie
	 *  
	 *  return code nature de voie and libelle voie from parcelle view
	 *  
	 *  cgocommune on 6 char and dvoilib on minimum n char are mandatory to launch request
	 *  
	 *   n char is defined in minNbCharForSearch from cadastrapp.properties
	 * 
	 * @param cgocommune code commune like 630103 (codep + codir + cocom)
     * 					cgocommune should be on 6 char
	 * @param dvoilib at least n chars
	 * 
	 * @return JSON with list of cconvo, dvoilib
	 * 
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getVoie(
			@QueryParam("cgocommune") String cgoCommune,
			@QueryParam("dvoilib") String dvoilib) throws SQLException {

		List<Map<String, Object>> voies = null;
	   	List<String> queryParams = new ArrayList<String>();
		
		// 
		if (cgoCommune != null && cgoCommuneLength!=cgoCommune.length() && 
				dvoilib != null && minNbCharForSearch <= dvoilib.length()){
						
			// Create request
			StringBuilder queryBuilder = new StringBuilder();				
			queryBuilder.append("select distinct cconvo, dvoilib from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".parcelle where cgocommune = ? and UPPER(dvoilib) LIKE UPPER(?) ;");
			
			// Add parameter to statement
			queryParams.add(cgoCommune);
			queryParams.add("%"+dvoilib+"%");
						
			// Launch request
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			voies = jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
		}
		else{
			logger.info("Missing mandatory parameter cgocommune and dvoilib to launch request");
		}

		return voies;
	}

}
