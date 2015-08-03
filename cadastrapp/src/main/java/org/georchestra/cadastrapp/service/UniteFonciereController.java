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
	 * @return Data from parcelle view to be display in popup in JSON format
	 * 
	 * @throws SQLException
	 */
	public Map<String, Object> getInfoBulleUniteFonciere(
			@Context HttpHeaders headers,
			@QueryParam("parcelle") String parcelle) throws SQLException {
 
		Map<String, Object> informations = null;

		if (isMandatoryParameterValid(parcelle)){
		
			// Create query
			StringBuilder queryBuilder = new StringBuilder();
		
			//TODO add batical
			queryBuilder.append("select proparc.comptecommunal, sum(p.dcntpa) as dcntpa_sum, sum(p.surfc) as sigcal_sum from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".parcelleDetails p, ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".proprietaire_parcelle proparc where proparc.parcelle = ? and proparc.parcelle = p.parcelle GROUP BY proparc.comptecommunal;");
						
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			informations = jdbcTemplate.queryForMap(queryBuilder.toString(), parcelle);
		}
		else{
			logger.warn("missing mandatory parameters");
		}

		return informations;
	}

}
