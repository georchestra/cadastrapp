package org.georchestra.cadastrapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.georchestra.cadastrapp.helper.BatimentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


public class BatimentController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(BatimentController.class);
	
	@Autowired
	BatimentHelper batimentHelper;

	@RequestMapping(path ="/getBatiments", produces = {MediaType.APPLICATION_JSON_VALUE}, method= {RequestMethod.GET})
	/**
	 * getBuildingsDetails
	 *  Returns information about batiment dnubat on given parcell 
	 *  
	 * @param headers http headers used 
	 * @param parcelle parcelle id
	 * @param dnubat batiment number 
	 * 
	 * @return JSON list 
	 */
	public @ResponseBody List<Map<String, Object>> getBuildingsDetails(
			@RequestParam("parcelle") String parcelle,
			@RequestParam("dnubat") String dnubat){
		
		List<Map<String, Object>> batiments = new ArrayList<Map<String, Object>>();
		if (getUserCNILLevel() == 0) {
			logger.info("User does not have enough rights to see information about batiment");
		}
		else if(parcelle != null && !parcelle.isEmpty()
				&& dnubat != null && !dnubat.isEmpty())
		{
			logger.debug("infoOngletBatiment - parcelle : " + parcelle + " for dnubat : " + dnubat);
			
			List<String> queryParams = new ArrayList<String>();
			queryParams.add(parcelle);
			queryParams.add(dnubat);
			
			StringBuilder queryBuilder = new StringBuilder();
			
			// CNIL Niveau 2
			queryBuilder.append("select distinct hab.annee, pb.jannat, pb.invar, pb.descr, pb.dniv, pb.dpor, pb.revcad, hab.ccoaff_lib, ");
			queryBuilder.append("prop.comptecommunal, prop.dnupro, prop.app_nom_usage, prop.app_nom_naissance ");
			queryBuilder.append("from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".proprietebatie pb, ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".proprietaire prop, ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".deschabitation hab ");
			queryBuilder.append(" where pb.parcelle = ? ");
			queryBuilder.append(" and pb.comptecommunal = prop.comptecommunal ");
			queryBuilder.append(" and pb.dnubat = ? and hab.invar = pb.invar ;");

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			batiments = jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());	
		}
		else{
			logger.info(" Missing input parameter ");
		}
		return batiments;
	}

	@RequestMapping(path = "/getBatimentsByParcelle" , produces = {MediaType.APPLICATION_JSON_VALUE}, method= {RequestMethod.GET})
	/**
	 *  Returns all building from given plot 
	 *  
	 * @param headers http headers used only available for CNIL 2
	 * @param parcelle on unique plot id
	 * 
	 * @return JSON list compose with all dnubat from this plot, list is empy if no data, or if user doesn't have rights
	 */
	public @ResponseBody List<Map<String, Object>> getBuildingsByParcelle(
		@RequestParam String parcelle){
		
		List<Map<String, Object>> batiments = new ArrayList<Map<String, Object>>();
		if (getUserCNILLevel() == 0) {
			logger.info("User does not have enough rights to see information about buildings");
		}
		else if(parcelle != null && !parcelle.isEmpty())
		{
			batiments = batimentHelper.getBuildings(parcelle);
		}
		else{
			logger.info(" Missing input parameter ");
		}
		return batiments;
	}

}
