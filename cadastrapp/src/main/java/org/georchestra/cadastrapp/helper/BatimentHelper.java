package org.georchestra.cadastrapp.helper;

import java.util.List;
import java.util.Map;

import org.georchestra.cadastrapp.service.CadController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public final class BatimentHelper extends CadController {

	static final Logger logger = LoggerFactory.getLogger(BatimentHelper.class);

	/**
	 * 
	 * getBuildings
	 * 
	 * @param	parcelle	plot id example : 2014630103000AP0025
	 * @param	headers		httpheaders information to validate authorization
	 * 
	 * @return buildings information on this plot
	 */
	public List<Map<String, Object>> getBuildings(String parcelle){
		
		logger.debug("infoOngletBatiment - parcelle : " + parcelle);
		
		StringBuilder queryBuilder = new StringBuilder();
		
		queryBuilder.append("select distinct pb.dnubat from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".proprietebatie pb ");
		queryBuilder.append(" where pb.parcelle = ? ");
		queryBuilder.append(addAuthorizationFiltering("pb."));
		queryBuilder.append(" ORDER BY pb.dnubat");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForList(queryBuilder.toString(), parcelle);		
	}
}
