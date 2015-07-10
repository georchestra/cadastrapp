package org.georchestra.cadastrapp.service;

import java.sql.SQLException;
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
	 *  return code nature de voie and libellé voie from parcelle view
	 * 
	 * @param ccoinsee code commune like 630103 (codep + codir + cocom)
     * 					ccoinsee should be on 6 char, if only 5 we deduce that codir is not present
	 * @param dvoilib at least 4 chars
	 * 
	 * @return JSON with list of cconvo, dvoilib
	 * 
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getVoie(
			@QueryParam("ccoinsee") String ccoinsee,
			@QueryParam("dvoilib_partiel") String dvoilib) throws SQLException {

		List<Map<String, Object>> voies = null;
		
		
		if (ccoinsee != null && !ccoinsee.isEmpty() && 
				dvoilib != null && dvoilib.length() > 2){
						
			StringBuilder queryBuilder = new StringBuilder();
				
			queryBuilder.append("select distinct cconvo, dvoilib ");
			queryBuilder.append(" from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".parcelle");
			
			//TODO factorize this methode used in several classes
			// no ccoinsee present in view proprietaire, parse it to get ccodep, ccocom and ccodir
			// exemple ccoinsee : 630103 -> ccodep 63, ccodir 0, ccocom 103
			if (ccoinsee!=null && !ccoinsee.isEmpty() && ccoinsee.length()>3){
			    	  
				int size = ccoinsee.length();
				
				String ccodep = ccoinsee.substring(0, 2);
				queryBuilder.append(createEqualsClauseRequest("ccodep", ccodep));
				
				String ccocom = ccoinsee.substring(size-3, size);
				queryBuilder.append(createEqualsClauseRequest("ccocom", ccocom));
			    	    
				// cas when ccoinsee have 6 chars
				if(size==6){
					String ccodir = ccoinsee.substring(2, 3);
					queryBuilder.append(createEqualsClauseRequest("ccodir", ccodir));
				}  
			}

			queryBuilder.append(createLikeClauseRequest("dvoilib", dvoilib));
			queryBuilder.append(finalizeQuery());
					
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			voies = jdbcTemplate.queryForList(queryBuilder.toString());
		}

		return voies;
	}

}
