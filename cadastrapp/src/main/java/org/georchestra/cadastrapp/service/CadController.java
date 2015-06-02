package org.georchestra.cadastrapp.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 
 * @author gfi
 *
 */
public class CadController {
	

	final static Logger logger = LoggerFactory.getLogger(CadController.class);
	
	private boolean isWhereAdded;
	
	@Resource(name="dbDataSource")
	protected DataSource dataSource;
	
	/**
	 * 
	 */
	public CadController() {
		super();
		this.isWhereAdded = false;
	}
	
	/**
	 * 
	 * @param mandatoryList
	 */
	public void checkMandatoryParameter(List<String> mandatoryList) {

		logger.info(" Mandatory parameters to check : " + mandatoryList);
	}
	
	
	/**
	 * Get Cnil level depending on CNIL groups in sec-roles
	 * 
	 * 
	 * @param headers
	 * @return
	 */
	public int getUserCNILLevel(HttpHeaders headers) {
		
		int cnilLevel=0;

		logger.debug(" Check user CNIL Level ");

		// Get CNIL Group information
		String rolesList = headers.getHeaderString("sec-roles");
		logger.debug(" Get user roles informations : " + rolesList);
		if(rolesList.contains("CNIL2")){
			cnilLevel=2;
		}
		else if(rolesList.contains("CNIL1")){
			cnilLevel=1;
		}
		
		logger.debug(" Check user CNIL Level : " + cnilLevel);
		return cnilLevel;
	}
	
	
	/**
	 * Filter information depending on groups information
	 * 
	 * 
	 * @param headers
	 * @return
	 */
	public String addAuthorizationFiltering(HttpHeaders headers) {
		
		StringBuilder queryFilter = new StringBuilder();
				
		// get roles list in header
		String rolesList = headers.getHeaderString("sec-roles");
		
		// get commune list in database corresponding to this header
		StringBuilder queryBuilder = new StringBuilder();
    	queryBuilder.append("select ccoinsee from ");
    	
    	//TODO get schema and table in properties
    	queryBuilder.append("cadastreapp_qgis.groupe_autorisation ");
    	queryBuilder.append(" where idgroup = '");
    	queryBuilder.append(rolesList);
    	queryBuilder.append("' ;");
 
    	
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<String> communes = jdbcTemplate.queryForList(queryBuilder.toString(), String.class);
        
        StringBuilder ccoinseeList = new StringBuilder();
        for (String ccoinsee : communes)
        {
        	ccoinseeList.append("'");
        	ccoinseeList.append(ccoinsee);
        	ccoinseeList.append("',");
        }
        // remove last coma
        ccoinseeList.deleteCharAt(ccoinseeList.length()-1);
              
        logger.debug("Liste des communes : " + ccoinseeList);
		// filter request	
        if (communes != null && !communes.isEmpty()){
        	queryFilter.append(" AND ccoinsee IN (");
        	queryFilter.append(ccoinseeList.toString());
        	queryFilter.append(" )");
        }
        
        return queryFilter.toString();
		
	}
	
	/**
	 *  Add a clause like in query
	 *  
	 * @param libelle 
	 * @param value
	 * @return
	 */
	public String createLikeClauseRequest(String libelle, String value){
		
		StringBuilder subQuery = new StringBuilder();
		
		if (value != null && !value.isEmpty()){
			if(!isWhereAdded){
				subQuery.append(" where ");
				isWhereAdded=true;
			}
			else{
				subQuery.append(" and ");
			}
			subQuery.append(libelle);
			subQuery.append(" LIKE '%");
			subQuery.append(value);
			subQuery.append("%'");
			
		}
		return subQuery.toString();	
	}
	
	/**
	 * Add an equals clause to query
	 * 
	 * @param libelle
	 * @param value
	 * @return
	 */
	public String createEqualsClauseRequest(String libelle, String value){
		
		StringBuilder subQuery = new StringBuilder();
		
		if (value != null && !value.isEmpty()){
			if(!isWhereAdded){
				subQuery.append(" where ");
				isWhereAdded=true;
			}
			else{
				subQuery.append(" and ");
			}

			subQuery.append(libelle);
			subQuery.append(" ='");
			subQuery.append(value);
			subQuery.append("'");	
		}
		return subQuery.toString();
	}

}
