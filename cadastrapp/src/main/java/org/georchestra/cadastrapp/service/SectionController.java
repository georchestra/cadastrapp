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

@Path("/getSection")
public class SectionController extends CadController {
	
	final static Logger logger = LoggerFactory.getLogger(SectionController.class);


	@GET
	@Produces("application/json")
	/**
	 * Return information about section from view section using parameter given.
	 *  
	 * @param headers headers from request used to filter search using LDAP Roles
	 * @param ccoinsee code commune like 630103 (codep + codir + cocom)
     * 					ccoinsee should be on 6 char, if only 5 we deduce that codir is not present
	 * @param ccopre_partiel exemple AP
	 * @param ccosec_partiel exemple 0025
	 * @return complete information about search section
	 * 
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getSectionList(
			@Context HttpHeaders headers,
			@QueryParam("ccoinsee") String ccoinsee,
			@QueryParam("ccopre_partiel") String ccopre_partiel,
			@QueryParam("ccosec_partiel") String ccosec_partiel) throws SQLException {

		List<Map<String, Object>> sections = null;
	   	List<String> queryParams = new ArrayList<String>();
		
		// Create query
		StringBuilder queryBuilder = new StringBuilder();

		queryBuilder.append("select distinct ccoinsee, ccopre, ccosec from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".section");

		// Special case when code commune on 5 characters is given
		// Convert 350206 to 35%206 for query
		if(ccoinsee!= null && 5 == ccoinsee.length()){
			ccoinsee = ccoinsee.substring(0, 2) + "%" +ccoinsee.substring(2); 
			queryBuilder.append(createLikeClauseRequest("ccoinsee", ccoinsee, queryParams));
		} 
		else{
			queryBuilder.append(createEqualsClauseRequest("ccoinsee", ccoinsee, queryParams));
		}
			
		queryBuilder.append(createLikeClauseRequest("ccopre", ccopre_partiel, queryParams));
		queryBuilder.append(createLikeClauseRequest("ccosec", ccosec_partiel, queryParams));
		queryBuilder.append(" ORDER BY ccopre, ccosec ");
		queryBuilder.append(finalizeQuery());
					
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		sections = jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());

		return sections;
	}


}
