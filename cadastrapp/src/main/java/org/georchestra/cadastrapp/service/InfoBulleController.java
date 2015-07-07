package org.georchestra.cadastrapp.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

@Path("/getInfoBulle")
public class InfoBulleController extends CadController {
	
	final static Logger logger = LoggerFactory.getLogger(InfoBulleController.class);


	@GET
	@Produces("application/json")
	/**
	 * 
	 * @param headers / headers from request used to filter search using LDAP Roles to display only information about parcelle from available ccoinsee
	 * 
	 * @param parcelle id parcelle
	 * @param infocadastrale 1 to get additional information, 0 to avoid getting additional information (default value 1 if not set)
	 * @param infouf 1 to get additional information, 0 to avoid getting additional information (default value 1 if not set)
	 * 
	 * @return Data from parcelle view to be display in popup in JSON format
	 * 
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getInfoBulle(
			@Context HttpHeaders headers,
			@QueryParam("parcelle") String parcelle,
			@DefaultValue("1") @QueryParam("infocadastrale") String infocadastrale,
			@DefaultValue("1") @QueryParam("infouf") String infouf) throws SQLException {
 
		List<Map<String, Object>> informations = null;

		if (isMandatoryParameterValid(parcelle)){
		
			// Create query
			StringBuilder queryBuilder = new StringBuilder();
	
			queryBuilder.append("select ");
	
			// TODO Add libcom and surfacecalculee
			// queryBuilder.append("parcelle, libcom, dcntpa, surfacecalculee");
			queryBuilder.append("p.parcelle, c.libcom, p.dcntpa");
	
			if(getUserCNILLevel(headers)>0){
				//TODO add proprietaires
				queryBuilder.append(", p.dnupro");
			}
			if(infouf.equals("1")){
				//TODO check dcnptap_sum, sigcal_sum, batical
				queryBuilder.append(", p.comptecommunal, p.dcntpa");
			}
			queryBuilder.append(" from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".parcelle p,");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".commune c ");
		
			queryBuilder.append(createEqualsClauseRequest("p.parcelle", parcelle));
			queryBuilder.append(" and p.ccocom = c.ccocom and p.ccodep = c.ccodep");
			
			//TODO add ccodep 
			//queryBuilder.append(" and p.ccodep = c.ccodep");
			queryBuilder.append(finalizeQuery());
						
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			informations = jdbcTemplate.queryForList(queryBuilder.toString());
		}
		else{
			logger.warn("missing mandatory parameters");
		}

		return informations;
	}


}
