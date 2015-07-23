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

@Path("/getVoie")
public class VoieController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(VoieController.class);
	
	@GET
	@Produces("application/json")
	/**
	 *  return code nature de voie and libelle voie from parcelle view
	 * 
	 * @param ccoinsee code commune like 630103 (codep + codir + cocom)
     * 					ccoinsee should be on 6 char, if only 5 we deduce that codir is not present
	 * @param dvoilib at least 3 chars
	 * 
	 * @return JSON with list of cconvo, dvoilib
	 * 
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getVoie(
			@QueryParam("ccoinsee") String ccoinsee,
			@QueryParam("dvoilib_partiel") String dvoilib) throws SQLException {

		List<Map<String, Object>> voies = null;
	   	List<String> queryParams = new ArrayList<String>();
		
		
		if (ccoinsee != null && !ccoinsee.isEmpty() && 
				dvoilib != null && dvoilib.length() > 2){
						
			StringBuilder queryBuilder = new StringBuilder();
				
			queryBuilder.append("select distinct cconvo, dvoilib ");
			queryBuilder.append(" from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".parcelle");

			queryBuilder.append(createEqualsClauseRequest("ccoinse", ccoinsee, queryParams));
			queryBuilder.append(createLikeClauseRequest("dvoilib", dvoilib, queryParams));
			queryBuilder.append(finalizeQuery());
					
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			voies = jdbcTemplate.queryForList(queryBuilder.toString());
		}

		return voies;
	}

}
