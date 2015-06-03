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

@Path("/getSection")
public class SectionController extends CadController {
	
	final static Logger logger = LoggerFactory.getLogger(SectionController.class);


	@GET
	@Produces("application/json")
	/**
	 * 
	 * @param ccoinsee
	 * @param ccopre_partiel
	 * @param ccosec_partiel
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getSectionList(
			@QueryParam("ccoinsee") String ccoinsee,
			@QueryParam("ccopre_partiel") String ccopre_partiel,
			@QueryParam("ccosec_partiel") String ccosec_partiel) throws SQLException {

		List<Map<String, Object>> sections = null;

		
		// Create query
		StringBuilder queryBuilder = new StringBuilder();

		queryBuilder.append("select ");

		queryBuilder.append("ccoinsee, ccopre, ccosec, geo_section");

		queryBuilder.append(" from ");
		// TODO replace this by configuration
		queryBuilder.append("cadastreapp_qgis.section");

		queryBuilder.append(createEqualsClauseRequest("ccoinsee", ccoinsee));
		queryBuilder.append(createLikeClauseRequest("ccopre_partiel", ccopre_partiel));
		queryBuilder.append(createLikeClauseRequest("ccosec_partiel", ccosec_partiel));
		queryBuilder.append(finalizeQuery());
					
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		sections = jdbcTemplate.queryForList(queryBuilder.toString());

		return sections;
	}


}
