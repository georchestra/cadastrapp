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
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;


public class SectionController extends CadController {
	
	final static Logger logger = LoggerFactory.getLogger(SectionController.class);

	@GET
	@Path("/getSection")
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 * /getSection
	 * 
	 * return information about section from view section using parameter given.
	 *  results will be filtered with user group geographical limitation
	 *  
	 * @param headers headers from request used to filter search using LDAP Roles
	 * @param cgocommune code geographique officil commune  like 630103 (codep + codir + cocom)
     * 					cgocommune should be on 6 char, if only 5 we deduce that codir is not present and we replace it in the request
	 * @param ccopre partial code pre section exemple A for AP or AC, could be the full code pre
	 * @param ccosec partial code section for exemple 2 or 25
	 * 
	 * @return cgocommune, ccopre, ccosec
	 * 
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getSectionList(
			@Context HttpHeaders headers,
			@QueryParam("cgocommune") String cgoCommune,
			@QueryParam("ccopre") String ccopre,
			@QueryParam("ccosec") String ccosec) throws SQLException {

		List<Map<String, Object>> sections = new ArrayList<Map<String, Object>>();
	   	List<String> queryParams = new ArrayList<String>();
		
		// Create query
		StringBuilder queryBuilder = new StringBuilder();

		queryBuilder.append("select distinct cgocommune, ccopre, ccosec from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".section ");

		// Special case when code commune on 5 characters is given
		// Convert 350206 to 35%206 for query
		if(cgoCommune!= null && 5 == cgoCommune.length()){
			cgoCommune = cgoCommune.substring(0, 2) + "%" +cgoCommune.substring(2); 
			queryBuilder.append(createLikeClauseRequest("cgocommune", cgoCommune, queryParams));
		} 
		else{
			queryBuilder.append(createEqualsClauseRequest("cgocommune", cgoCommune, queryParams));
		}
			
		queryBuilder.append(createLikeClauseRequest("ccopre", ccopre, queryParams));
		queryBuilder.append(createLikeClauseRequest("ccosec", ccosec, queryParams));
		queryBuilder.append(" ORDER BY ccopre, ccosec ");
		queryBuilder.append(finalizeQuery());
					
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		sections = jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());

		return sections;
	}


}
