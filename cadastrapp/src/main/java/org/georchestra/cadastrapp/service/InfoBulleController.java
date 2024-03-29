package org.georchestra.cadastrapp.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class InfoBulleController extends CadController {
	
	final static Logger logger = LoggerFactory.getLogger(InfoBulleController.class);


	@RequestMapping(path = "/getInfoBulle", produces = {MediaType.APPLICATION_JSON_VALUE}, method= {RequestMethod.GET})
	/**
	 * 
	 * @param parcelle id parcelle
	 * @param infocadastrale 1 to get additional information, 0 to avoid getting additional information (default value 1 if not set)
	 * @param infouf 1 to get additional information, 0 to avoid getting additional information (default value 1 if not set)
	 * 
	 * @return Data from parcelle view to be display in popup in JSON format
	 * 
	 * @throws SQLException
	 */
	public @ResponseBody Map<String, Object> getInfoBulle(
			@RequestParam String parcelle,
			@RequestParam(defaultValue = "1", required = false) int infocadastrale,
			@RequestParam(defaultValue = "1", required = false) int infouf) throws SQLException {
 
		Map<String, Object> informations = null;
		
		if(infocadastrale == 0 && infouf == 1){
			informations = getInfoBulleUniteFonciere(parcelle);
		}else if (infocadastrale == 1 && infouf == 0){
			informations = getInfoBulleParcelle(parcelle);
		}else if (infocadastrale == 0 && infouf == 0){
			logger.warn("No information can be serve");
		}else{
			informations = getInfoBulleParcelle(parcelle);
			informations.putAll(getInfoBulleUniteFonciere(parcelle));
		}

		return informations;
	}

	
	@RequestMapping(path = "/getInfobulleParcelle", produces = {MediaType.APPLICATION_JSON_VALUE}, method= {RequestMethod.GET})
	/**
	 * 
	 * @param parcelle id parcelle
	 * @return Data from parcelle view to be display in popup in JSON format
	 * 
	 * @throws SQLException
	 */
	public 	@ResponseBody Map<String, Object> getInfoBulleParcelle(
			@RequestParam("parcelle") String parcelle) throws SQLException {
 
		Map<String, Object> informations = null;

		if (isMandatoryParameterValid(parcelle)){
		
			// Create query
			StringBuilder queryBuilder = new StringBuilder();
	
			queryBuilder.append("select p.parcelle, p.cgocommune, p.dnupla, p.ccopre, p.ccosec, surf.surfc, c.libcom, p.dcntpa, p.dnvoiri, p.dindic, p.cconvo, p.dvoilib from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".parcelle p,");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".v_parcelle_surfc surf,");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".commune c where p.parcelle = ? and p.parcelle = surf.parcelle and p.cgocommune = c.cgocommune");

						
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			informations = jdbcTemplate.queryForMap(queryBuilder.toString(), parcelle);
			
			if(getUserCNILLevel()>0){
				
				List<Map<String, Object>> proprietaires = null;
				
				// Create query
				StringBuilder queryProprietaireBuilder = new StringBuilder();
				queryProprietaireBuilder.append("select distinct prop.app_nom_usage from ");
				queryProprietaireBuilder.append(databaseSchema);
				queryProprietaireBuilder.append(".proprietaire_parcelle proparc,");
				queryProprietaireBuilder.append(databaseSchema);
				queryProprietaireBuilder.append(".proprietaire prop ");
				queryProprietaireBuilder.append(" where proparc.parcelle = ? and proparc.comptecommunal = prop.comptecommunal ");
				queryProprietaireBuilder.append(addAuthorizationFiltering("prop."));
				queryProprietaireBuilder.append(" LIMIT 9");
				
				JdbcTemplate jdbcTemplateProp = new JdbcTemplate(dataSource);
				proprietaires = jdbcTemplateProp.queryForList(queryProprietaireBuilder.toString(), parcelle);
				
				// Add a new node proprietaire, fill with ddenom List
				informations.put("proprietaires", proprietaires);
				
				logger.debug("List des informations avec proprietaire : "+ informations.toString());
			}
		}
		else{
			logger.warn("missing mandatory parameters");
		}

		return informations;
	}

	@RequestMapping(path = "/getInfoUniteFonciere", produces = {MediaType.APPLICATION_JSON_VALUE}, method= {RequestMethod.GET})
	/**
	 *  
	 * @param parcelle id parcelle
	 * @return Data from parcelle view to be display in popup in JSON format
	 * 
	 * @throws SQLException
	 */
	public @ResponseBody Map<String, Object> getInfoBulleUniteFonciere(
			@RequestParam("parcelle") String parcelle) throws SQLException {
 
		Map<String, Object> informations = new HashMap<String,Object>();

		if (isMandatoryParameterValid(parcelle)){
		
			// Create query
			StringBuilder queryBuilder = new StringBuilder();
		
			queryBuilder.append("select uf.uf, uf.comptecommunal, sum(p.dcntpa) as dcntpa_sum, sum(p.surfc) as sigcal_sum, sum(p.surfb) as sigcalb_sum from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".parcelleDetails p, ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".uf_parcelle uf where uf.uf IN (select uf2.uf from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".uf_parcelle uf2 where uf2.parcelle= ? ) ");
			queryBuilder.append(" and uf.parcelle = p.parcelle ");
			
			// filter on geographical limitation only if search is filtered
			if(isSearchFiltered){
				queryBuilder.append(addAuthorizationFiltering("p."));
			}
			queryBuilder.append("GROUP BY uf.uf, uf.comptecommunal;");
								
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			// try query and catch request error if result is empty
			try {
				informations = jdbcTemplate.queryForMap(queryBuilder.toString(), parcelle);
			}
			catch (EmptyResultDataAccessException e){
				logger.debug("User does not have enough right to see information about parcelle", e);
			}			
		}
		else{
			logger.warn("missing mandatory parameters");
		}

		return informations;
	}

}
