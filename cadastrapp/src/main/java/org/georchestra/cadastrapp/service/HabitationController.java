package org.georchestra.cadastrapp.service;

import java.util.ArrayList;
import java.util.HashMap;
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


public class HabitationController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(HabitationController.class);
	
	
	@GET
	@Path("/getHabitationDetails")
	@Produces("application/json")
	/**
	 *  Returns information about habitation
	 *  
	 * @param headers http headers used 
	 * @param annee 
	 * @param invar id habitation 
	 * 
	 * @return JSON list 
	 */
	public Map<String, Object> getHabitationDetails(@Context HttpHeaders headers, 
			@QueryParam("annee") String annee,
			@QueryParam("invar") String invar){
		
		Map<String, Object> habitationDesc = new HashMap<String, Object>();
		
		if(annee != null && annee != null && getUserCNILLevel(headers) > 1) 
		{	
			logger.debug("HabitationDetails - annee : " + annee + " for invar : " + invar);
			
			habitationDesc.put("article40", getArticle40Details(annee, invar));
			habitationDesc.put("article50", getArticle50Details(annee, invar));
			habitationDesc.put("article60", getArticle60Details(annee, invar));	
		}
		else{
			logger.info(" Missing input parameter ");
		}
		return habitationDesc;
	}
	
	/**
	 * 
	 * @param annee
	 * @param invar
	 * @return
	 */
	private Map<String, Object> getArticle40Details(String annee, String invar){
		
		List<String> queryParams = new ArrayList<String>();
		queryParams.add(annee);
		queryParams.add(invar);
		
		StringBuilder queryBuilder = new StringBuilder();
		
		// CNIL Niveau 2
		queryBuilder.append("select hab.dnupev_lib, hab.dnudes, hab.detent, hab.dsupdc, hab.dnbniv, hab.dnbpdc, ");
		queryBuilder.append("hab.dnbppr, hab.dnbsam, hab.dnbcha, hab.dnbcu8, hab.dnbcu9, hab.dnbsea, hab.dnbann, hab.dnbbai, hab.dnbdou, hab.dnblav, ");
		queryBuilder.append("hab.dnbwc, hab.geaulc, hab.gelelc, hab.ggazlc, hab.gchclc, hab.gteglc, hab.gesclc, hab.gasclc, hab.gvorlc ");
		queryBuilder.append("from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".deschabitation hab ");
		queryBuilder.append(" where hab.annee = ? and hab.invar = ? ;");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForMap(queryBuilder.toString(), queryParams.toArray());
	}
	
	/**
	 * 
	 * @param annee
	 * @param invar
	 * @return
	 */
	private List<Map<String, Object>> getArticle50Details(String annee, String invar){
		
		List<String> queryParams = new ArrayList<String>();
		queryParams.add(annee);
		queryParams.add(invar);
		
		StringBuilder queryBuilder = new StringBuilder();
		
		// CNIL Niveau 2
		queryBuilder.append("select pro.dnudes, pro.vsurzt");
		queryBuilder.append(" from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".descproffessionnel pro ");
		queryBuilder.append(" where pro.annee = ? and pro.invar = ? ;");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		return jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
	}
	
	/**
	 * 
	 * @param annee
	 * @param invar
	 * @return
	 */
	private List<Map<String, Object>> getArticle60Details(String annee, String invar){
		
		List<String> queryParams = new ArrayList<String>();
		queryParams.add(annee);
		queryParams.add(invar);
		
		StringBuilder queryBuilder = new StringBuilder();
		
		// CNIL Niveau 2
		queryBuilder.append("select dep.dnudes, dep.cconad_lib, dep.dsudep, dep.dnbbai, dep.dnbdou, dep.dnblav, dep.dnbwc, dep.geaulc, dep.gelelc, dep.gchclc");
		queryBuilder.append(" from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".descdependance dep ");
		queryBuilder.append(" where dep.annee = ? and dep.invar = ? ;");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		return jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
	}

}
