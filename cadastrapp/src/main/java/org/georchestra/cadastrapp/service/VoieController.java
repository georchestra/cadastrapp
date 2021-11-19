package org.georchestra.cadastrapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class VoieController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(VoieController.class);

	@RequestMapping(path ="/getVoie", produces = {MediaType.APPLICATION_JSON_VALUE},  method = { RequestMethod.GET})
	/**
	 *  /getVoie
	 *  
	 *  return code nature de voie and libelle voie from parcelle view
	 *  
	 *  cgocommune on 6 char and dvoilib on minimum n char are mandatory to launch request
	 *  
	 *   n char is defined in minNbCharForSearch from cadastrapp.properties
	 * 
	 * @param cgocommune code commune like 630103 (codep + codir + cocom)
     * 					cgocommune should be on 6 char
	 * @param dvoilib at least n chars
	 * 
	 * @return JSON with list of cconvo, dvoilib
	 * 
	 * @throws SQLException
	 */
	public 	@ResponseBody List<Map<String, Object>> getVoie(
			@RequestParam(name= "cgocommune") String cgoCommune,
			@RequestParam(name= "dvoilib") String dvoilib) throws SQLException {

		List<Map<String, Object>> voies = new ArrayList<Map<String, Object>>();
	   	List<String> queryParams = new ArrayList<String>();
		
	   	int cgoCommuneLength = Integer.parseInt(CadastrappPlaceHolder.getProperty("cgoCommune.length"));
	    
	   	
		if (cgoCommune != null && cgoCommuneLength!=cgoCommune.length()-1 && 
				dvoilib != null && minNbCharForSearch <= dvoilib.length()){
						
			// Create request
			StringBuilder queryBuilder = new StringBuilder();				
			queryBuilder.append("select distinct cconvo, dvoilib from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".parcelle where cgocommune = ? and UPPER(dvoilib) LIKE UPPER(?) ");
			queryBuilder.append(" ORDER BY dvoilib;");
			
			// Add parameter to statement
			queryParams.add(cgoCommune);
			queryParams.add("%"+dvoilib+"%");
						
			// Launch request
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			voies = jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
		}
		else{
			logger.info("Error in mandatory parameter cgocommune or dvoilib to launch request");
		}

		return voies;
	}

}
