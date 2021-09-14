package org.georchestra.cadastrapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.georchestra.cadastrapp.service.constants.CadastrappConstants;

/**
 * HabitationController
 * 
 * Used to get article 40, 50 and 60 informations from cadastrapp database
 * 
 * @author pierre
 *
 */
@Controller
public class HabitationController extends CadController {

	static final Logger logger = LoggerFactory.getLogger(HabitationController.class);
	
	@RequestMapping(path = "/getHabitationDetails", produces = {MediaType.APPLICATION_JSON_VALUE}, method= {RequestMethod.GET})
	/**
	 *  Returns information about habitation
	 *  
	 * @param annee String corresponding to year of wanted information (normally only current year available)
	 * @param invar String id habitation 
	 * 
	 * @return Map<String, Object> containing informations from article 40 50 and 60,
	 * 			empty list if missing input parameter or if user doesn't have privilege
	 */
	public @ResponseBody Map<String, Object> getHabitationDetails(
			@RequestParam String annee,
			@RequestParam String invar){
		
		Map<String, Object> habitationDesc = new HashMap<String, Object>();
		
		List<String> queryParams = new ArrayList<String>();
		queryParams.add(annee);
		queryParams.add(invar);
		if (getUserCNILLevel() == 0) {
			logger.info("User needs does not have enough rights to see habitation details");
		}
		else if(annee != null && invar != null)
		{		
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
	private @ResponseBody List<Map<String, Object>> getArticle40Details(List<String> queryParams){
			
		logger.debug("getArticle40Details");
		StringBuilder queryBuilder = new StringBuilder();
	
		queryBuilder.append("select hab.dnudes, det.detent_lib as detent, hab.dsupdc, hab.dnbniv, hab.dnbpdc, ");
		queryBuilder.append("hab.dnbppr, hab.dnbsam, hab.dnbcha, hab.dnbcu8, hab.dnbcu9, hab.dnbsea, hab.dnbann, hab.dnbbai, hab.dnbdou, hab.dnblav, ");
		queryBuilder.append("hab.dnbwc, hab.geaulc, hab.gelelc, hab.ggazlc, hab.gchclc, hab.gteglc, hab.gesclc, hab.gasclc, hab.gvorlc, ");
		queryBuilder.append("toit.description as dmattodesc, mur.description as dmatgmdesc");
		queryBuilder.append(" from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".deschabitation hab , ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".prop_bati_detent det , ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".prop_dmatto toit, ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".prop_dmatgm mur");
		queryBuilder.append(" where hab.annee = ? and hab.invar = ? ");
		queryBuilder.append(" and hab.dmatgm = mur.code and hab.dmatto = toit.code and hab.detent = det.detent;");
		
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
		queryBuilder.append("select pro.dnudes, pro.vsurzt, pro.dsupot, pro.dsup1, pro.dsup2, pro.dsup3, pro.dsupk1, pro.dsupk2, pro.ccocac, cco.ccocac_lib");
		queryBuilder.append(" from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".descproffessionnel pro , ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".prop_ccocac cco");
		queryBuilder.append(" where pro.annee = ? and pro.invar = ? and pro.ccocac=cco.ccocac;");
		
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
