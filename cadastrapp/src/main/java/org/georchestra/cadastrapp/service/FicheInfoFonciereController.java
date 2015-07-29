package org.georchestra.cadastrapp.service;

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

@Path("/getFIUF")
public class FicheInfoFonciereController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(FicheInfoFonciereController.class);

	@GET
	@Produces("application/json")
	/**
	 * 
	 * @param headers
	 * @param parcelle
	 * @return
	 */
	public List<Map<String, Object>> getInformationFonciere(@Context HttpHeaders headers, @QueryParam("parcelle") String parcelle) {

		List<Map<String, Object>> information = null;

		StringBuilder queryBuilder = new StringBuilder();

		// CNIL niveau 0
		queryBuilder.append("select ccodep, ccodir, ccocom, ccoprem, ccosecm, comptecommunal ");
		queryBuilder.append(" from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".parcelledetails p where parcelle = ? ");
		queryBuilder.append(addAuthorizationFiltering(headers, 1));
		queryBuilder.append(finalizeQuery());

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		information = jdbcTemplate.queryForList(queryBuilder.toString(), parcelle);

		// Return value providers will convert to JSON
		return information;
	}

}
