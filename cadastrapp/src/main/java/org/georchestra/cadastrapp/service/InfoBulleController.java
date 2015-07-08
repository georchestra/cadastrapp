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


public class InfoBulleController extends CadController {
	
	final static Logger logger = LoggerFactory.getLogger(InfoBulleController.class);


	@Path("/getInfoBulle")
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
		
		if(infocadastrale.equals("0") && infouf.equals("1")){
			informations = getInfoBulleUniteFonciere(headers, parcelle);
		}else if (infocadastrale.equals("1") && infouf.equals("0")){
			informations = getInfoBulleParcelle(headers, parcelle);
		}else if (infocadastrale.equals("0") && infouf.equals("0")){
			logger.warn("No information can be serve");
		}else if (isMandatoryParameterValid(parcelle)){
		
			// Create query
			StringBuilder queryBuilder = new StringBuilder();
	
			// TODO Add surfacecalculee
			queryBuilder.append("select p.parcelle, c.libcom, p.dcntpa, p.dnvoiri, p.dindic, p.cconvo, p.dvoilib");
				
			//TODO check dcnptap_sum, sigcal_sum, batical
			queryBuilder.append(", p.comptecommunal");

			queryBuilder.append(" from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".parcelle p,");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".commune c ");
			
			if(getUserCNILLevel(headers)>0){
			
			}
				
			queryBuilder.append(createEqualsClauseRequest("p.parcelle", parcelle));
			queryBuilder.append(" and p.ccocom = c.ccocom and p.ccodep = c.ccodep");

			queryBuilder.append(finalizeQuery());
						
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			informations = jdbcTemplate.queryForList(queryBuilder.toString());
			
			
			if(getUserCNILLevel(headers)>0){
				
				List<Map<String, Object>> proprietaires = null;
				
				// Create query
				StringBuilder queryProprietaireBuilder = new StringBuilder();
				//TODO add proprietaires
				queryProprietaireBuilder.append("select prop.ddenom");
				queryProprietaireBuilder.append(" from ");
				queryProprietaireBuilder.append(databaseSchema);
				queryProprietaireBuilder.append(".parcelle p,");
				queryProprietaireBuilder.append(databaseSchema);
				queryProprietaireBuilder.append(".proprietaire prop ");
				queryProprietaireBuilder.append(createEqualsClauseRequest("p.parcelle", parcelle));
				queryProprietaireBuilder.append(" and p.dnupro = prop.dnupro LIMIT 5");	
				
				queryBuilder.append(finalizeQuery());
				
				JdbcTemplate jdbcTemplateProp = new JdbcTemplate(dataSource);
				proprietaires = jdbcTemplateProp.queryForList(queryProprietaireBuilder.toString());
				
				informations.get(0).put("proprietaires", proprietaires);
				
				logger.debug("List des informations avec proprietaire : "+ informations.toString());
			}
		
		}
		else{
			logger.warn("missing mandatory parameters");
		}

		return informations;
	}

	
	@Path("/getInfobulleParcelle")
	@GET
	@Produces("application/json")
	/**
	 * 
	 * @param headers / headers from request used to filter search using LDAP Roles to display only information about parcelle from available ccoinsee
	 * 
	 * @param parcelle id parcelle
	 * @return Data from parcelle view to be display in popup in JSON format
	 * 
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getInfoBulleParcelle(
			@Context HttpHeaders headers,
			@QueryParam("parcelle") String parcelle) throws SQLException {
 
		List<Map<String, Object>> informations = null;

		if (isMandatoryParameterValid(parcelle)){
		
			// Create query
			StringBuilder queryBuilder = new StringBuilder();
	
			queryBuilder.append("select ");
	
			// TODO Add surfacecalculee
			queryBuilder.append("p.parcelle, c.libcom, p.dcntpa, p.dnvoiri, p.dindic, p.cconvo, p.dvoilib");
			
			queryBuilder.append(" from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".parcelle p,");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".commune c ");

			queryBuilder.append(createEqualsClauseRequest("p.parcelle", parcelle));
			queryBuilder.append(" and p.ccocom = c.ccocom and p.ccodep = c.ccodep");
			
			queryBuilder.append(finalizeQuery());
						
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			informations = jdbcTemplate.queryForList(queryBuilder.toString());
			
			if(getUserCNILLevel(headers)>0){
				
				List<Map<String, Object>> proprietaires = null;
				
				// Create query
				StringBuilder queryProprietaireBuilder = new StringBuilder();
				queryProprietaireBuilder.append("select prop.ddenom");
				queryProprietaireBuilder.append(" from ");
				queryProprietaireBuilder.append(databaseSchema);
				queryProprietaireBuilder.append(".parcelle p,");
				queryProprietaireBuilder.append(databaseSchema);
				queryProprietaireBuilder.append(".proprietaire prop ");
				queryProprietaireBuilder.append(createEqualsClauseRequest("p.parcelle", parcelle));
				queryProprietaireBuilder.append(" and p.dnupro = prop.dnupro  LIMIT 5");	
				
				queryBuilder.append(finalizeQuery());
				
				JdbcTemplate jdbcTemplateProp = new JdbcTemplate(dataSource);
				proprietaires = jdbcTemplateProp.queryForList(queryProprietaireBuilder.toString());
				
				informations.get(0).put("proprietaires", proprietaires);
				
				logger.debug("List des informations avec proprietaire : "+ informations.toString());
			}
		}
		else{
			logger.warn("missing mandatory parameters");
		}

		return informations;
	}

	@Path("/getInfobulleUniteFonciere")
	@GET
	@Produces("application/json")
	/**
	 * 
	 * @param headers / headers from request used to filter search using LDAP Roles to display only information about parcelle from available ccoinsee
	 * 
	 * @param parcelle id parcelle
	 * @return Data from parcelle view to be display in popup in JSON format
	 * 
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getInfoBulleUniteFonciere(
			@Context HttpHeaders headers,
			@QueryParam("parcelle") String parcelle) throws SQLException {
 
		List<Map<String, Object>> informations = null;

		if (isMandatoryParameterValid(parcelle)){
		
			// Create query
			StringBuilder queryBuilder = new StringBuilder();
	
			queryBuilder.append("select ");
				
			//TODO check dcnptap_sum, sigcal_sum, batical
			queryBuilder.append(" p.comptecommunal, p.dcntpa");
			
			queryBuilder.append(" from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".parcelle p");
		
			queryBuilder.append(createEqualsClauseRequest("p.parcelle", parcelle));

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
