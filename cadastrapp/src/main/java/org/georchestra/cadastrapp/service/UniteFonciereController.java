package org.georchestra.cadastrapp.service;

import java.sql.SQLException;
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


public class UniteFonciereController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(UniteFonciereController.class);

	@Path("/getInfoUniteFonciere")
	@GET
	@Produces("application/json")
	/**
	 * 
	 * @param headers / headers from request used to filter search using LDAP Roles to display only information about parcelle from available cgocommune
	 * 
	 * @param parcelle id parcelle
	 * @return Data from unite foncier and parcelle view to be display, retunr in JSON format
	 * 
	 * @throws SQLException
	 */
	public Map<String, Object> getInfoUniteFonciere(
			@Context HttpHeaders headers,
			@QueryParam("parcelle") String parcelle) throws SQLException {
 
		Map<String, Object> informations = null;

		if (isMandatoryParameterValid(parcelle)){
		
			// Create query
			StringBuilder queryBuilder = new StringBuilder();
			
			queryBuilder.append("select uf.uf, uf.comptecommunal, sum(p.dcntpa) as dcntpa_sum, sum(p.surfc) as sigcal_sum from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".parcelleDetails p, ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".uf_parcelle uf where uf.uf IN (select uf2.uf from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".uf_parcelle uf2 where uf2.parcelle= ? ) ");
			queryBuilder.append(" and uf.parcelle = p.parcelle GROUP BY uf.uf, uf.comptecommunal;");

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			informations = jdbcTemplate.queryForMap(queryBuilder.toString(), parcelle);		
		}
		else{
			logger.warn("missing mandatory parameters");
		}

		return informations;
	}

}
