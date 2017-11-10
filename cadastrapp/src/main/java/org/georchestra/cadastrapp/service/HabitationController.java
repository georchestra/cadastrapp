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
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * HabitationController
 * 
 * Used to get article 40, 50 and 60 informations from cadastrapp database
 * 
 * @author pierre
 *
 */
public class HabitationController extends CadController {

	static final Logger logger = LoggerFactory.getLogger(HabitationController.class);
	
	
	@GET
	@Path("/getHabitationDetails")
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 *  Returns information about habitation
	 *  
	 * @param headers HttpHeaders http headers used 
	 *         information will only be return if user is CNIL2
	 * @param annee String corresponding to year of wanted information (normally only current year available)
	 * @param invar String id habitation 
	 * 
	 * @return Map<String, Object> containing informations from article 40 50 and 60,
	 * 			empty list if missing input parameter or if user doesn't have privilege
	 */
	public Map<String, Object> getHabitationDetails(@Context HttpHeaders headers, 
			@QueryParam("annee") String annee,
			@QueryParam("invar") String invar){
		
		Map<String, Object> habitationDesc = new HashMap<String, Object>();
		
		// Check information are valid and that user have priviledge to see it
		if(annee != null && invar != null && getUserCNILLevel(headers) > 1) 
		{					
			List<String> queryParams = new ArrayList<String>();
			queryParams.add(annee);
			queryParams.add(invar);
			
			habitationDesc.put("article40", getArticle40Details(queryParams));
			habitationDesc.put("article50", getArticle50Details(queryParams));
			habitationDesc.put("article60", getArticle60Details(queryParams));	
		}
		else{
			logger.info(" Missing input parameter ");
		}
		return habitationDesc;
	}
	
	/**
	 * getArticle40Details
	 * 
	 * @param queryParams List composed with year and invar information
	 * @return List<Map<String, Object>>
	 */
	private List<Map<String, Object>> getArticle40Details(List<String> queryParams){
			
		logger.debug("getArticle40Details");
		StringBuilder queryBuilder = new StringBuilder();
	
		queryBuilder.append("select hab.dnudes, hab.detent, hab.dsupdc, hab.dnbniv, hab.dnbpdc, ");
		queryBuilder.append("hab.dnbppr, hab.dnbsam, hab.dnbcha, hab.dnbcu8, hab.dnbcu9, hab.dnbsea, hab.dnbann, hab.dnbbai, hab.dnbdou, hab.dnblav, ");
		queryBuilder.append("hab.dnbwc, hab.geaulc, hab.gelelc, hab.ggazlc, hab.gchclc, hab.gteglc, hab.gesclc, hab.gasclc, hab.gvorlc, ");
		queryBuilder.append("toit.description as dmattodesc, mur.description as dmatgmdesc");
		queryBuilder.append(" from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".deschabitation hab , ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".prop_dmatto toit, ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".prop_dmatgm mur");
		queryBuilder.append(" where hab.annee = ? and hab.invar = ? ");
		queryBuilder.append(" and hab.dmatgm = mur.code and hab.dmatto = toit.code ;");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
	}
	
	/**
	 * getArticle50Details
	 * 
	 * @param ueryParams List composed with year and invar information
	 * @return List<Map<String, Object>>
	 */
	private List<Map<String, Object>> getArticle50Details(List<String> queryParams){
		
		logger.debug("getArticle50Details");
		
		StringBuilder queryBuilder = new StringBuilder();
		
		// CNIL Niveau 2
		queryBuilder.append("select pro.dnudes, pro.vsurzt, pro.dsupot, pro.dsup1, pro.dsup2, pro.dsup3, pro.dsupk1, pro.dsupk2");
		queryBuilder.append(" from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".descproffessionnel pro ");
		queryBuilder.append(" where pro.annee = ? and pro.invar = ? ;");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		return jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
	}
	
	/**
	 * getArticle60Details
	 * 
	 * @param queryParams List composed with year and invar information
	 * @return List<Map<String, Object>>
	 */
	private List<Map<String, Object>> getArticle60Details(List<String> queryParams){

		logger.debug("getArticle60Details");
		
		StringBuilder queryBuilder = new StringBuilder();
		
		// CNIL Niveau 2
		queryBuilder.append("select dep.dnudes, dep.cconad_lib, dep.dsudep, dep.dnbbai, dep.dnbdou, dep.dnblav, dep.dnbwc, dep.geaulc, dep.gelelc, dep.gchclc, ");
		queryBuilder.append("toit.description as dmattodesc, mur.description as dmatgmdesc");
		queryBuilder.append(" from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".descdependance dep , ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".prop_dmatto toit, ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".prop_dmatgm mur");
		queryBuilder.append(" where dep.annee = ? and dep.invar = ? ");
		queryBuilder.append(" and dep.dmatgm = mur.code and dep.dmatto = toit.code ;");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		return jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
	}

}
