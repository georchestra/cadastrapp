package org.georchestra.cadastrapp.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;

import org.georchestra.cadastrapp.service.CadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public final class ProprietaireHelper extends CadController {

	static final Logger logger = LoggerFactory.getLogger(ProprietaireHelper.class);

	/**
	 * 
	 * @param parcelleList plots list
	 * @param isAddressConcat true to display address in only on field address, false to display each dlign
	 * @param headers A leaste level CNIL1 form http headers
	 * @return information list of owner whith address field concat in one field (if isAddressConcat is true)
	 */
	public List<Map<String, Object>> getProprietairesByParcelles(HttpHeaders headers, List<String> parcelleList, boolean isAddressConcat) {
	
		// Init list to return response even if nothing in it.
		List<Map<String,Object>> proprietaires = new ArrayList<Map<String,Object>>();
		
		// User need to be at least CNIL1 level
		if (getUserCNILLevel(headers)>0){

			if(parcelleList != null && !parcelleList.isEmpty()){
						
				StringBuilder queryBuilder = new StringBuilder();
				queryBuilder.append("select distinct prop.dnulp, proparc.parcelle, prop.comptecommunal, prop.app_nom_usage, prop.app_nom_naissance, prop.dldnss, prop.jdatnss, prop.ccodro_lib , prop.ccodro, prop.dformjur, ");
				if(isAddressConcat){
					queryBuilder.append(" COALESCE(prop.dlign3, '')||' '||COALESCE(prop.dlign4,'')||' '||COALESCE(prop.dlign5,'')||' '||COALESCE(prop.dlign6,'') as adresse ");	
					
				}else{
					queryBuilder.append(" prop.dlign3, prop.dlign4, prop.dlign5, prop.dlign6 ");
				}
				queryBuilder.append(" from ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".proprietaire prop, ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".proprietaire_parcelle proparc ");
				queryBuilder.append(createWhereInQuery(parcelleList.size(), "proparc.parcelle"));
				queryBuilder.append(" and prop.comptecommunal = proparc.comptecommunal");
				queryBuilder.append(addAuthorizationFiltering(headers, "prop."));
				queryBuilder.append(" ORDER BY prop.dnulp, prop.app_nom_usage ");
			
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				proprietaires = jdbcTemplate.queryForList(queryBuilder.toString(), parcelleList.toArray());
	
			}
			else{
				//log empty request
				logger.info(EMPTY_REQUEST_LOG);
			}
		}else{
			logger.info(ACCES_ERROR_LOG);
		}
		return proprietaires;
	}
}
