package org.georchestra.cadastrapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.core.HttpHeaders;

import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
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
	
	@Resource(name = "dbDataSource")
	protected DataSource dataSource;
	
	protected String databaseSchema;
	
	protected String cnil2RoleName;
	
	protected String cnil1RoleName;
	
	protected int minNbCharForSearch;
	
	
	/**
	 * 
	 */
	public CadController() {
		super();
		this.isWhereAdded = false;
		this.databaseSchema = CadastrappPlaceHolder.getProperty("schema.name");
		this.cnil1RoleName = CadastrappPlaceHolder.getProperty("cnil1RoleName");
		this.cnil2RoleName = CadastrappPlaceHolder.getProperty("cnil2RoleName");
		this.minNbCharForSearch = Integer.parseInt(CadastrappPlaceHolder.getProperty("minNbCharForSearch"));
	}
	
	/**
	 * 
	 * @param mandatoryList
	 */
	protected boolean isMandatoryParameterValid(String parameter) {

		boolean valid = false;
		logger.debug(" Mandatory parameters to check : " + parameter);
		
		// Valid if list not empty and if all parameter are present
		if (parameter != null && !parameter.isEmpty()){
			valid = true;
			logger.debug(" Mandatory parameter is ok");
		}
		
		return valid;
	}


	/**
	 * 
	 * @param mandatoryList
	 */
	protected boolean checkAreMandatoryParametersValid(List<String> mandatoryList) {

		boolean valid = false;
		logger.debug(" Mandatory parameters to check : " + mandatoryList);
		
		// Valid if list not empty and if all parameter are present
		if (mandatoryList != null && !mandatoryList.isEmpty() && !mandatoryList.contains(null)){
			valid = true;
			logger.debug(" Mandatory parameters are ok");
		}
		
		return valid;
	}

	/**
	 * Get Cnil level depending on CNIL groups in sec-roles
	 * 
	 * 
	 * @param headers
	 * @return
	 */
	protected int getUserCNILLevel(HttpHeaders headers) {

		int cnilLevel = 0;

		logger.debug(" Check user CNIL Level ");

		// Get CNIL Group information
		String rolesList = headers.getHeaderString("sec-roles");
		logger.debug(" Get user roles informations : " + rolesList);
		if (rolesList!=null && rolesList.contains(cnil2RoleName)) {
			cnilLevel = 2;
		} else if (rolesList!=null && rolesList.contains(cnil1RoleName)) {
			cnilLevel = 1;
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
	protected String addAuthorizationFiltering(HttpHeaders headers) {
		return addAuthorizationFiltering(headers, "");
	}
	
	/**
	 * Filter information depending on groups information
	 * 
	 * 
	 * @param headers
	 * @param tableAlias
	 *  
	 * @return string query to complet user right
	 */
	protected String addAuthorizationFiltering(HttpHeaders headers, String tableAlias) {

		List<Map<String, Object>> limitations;
		List<String> communes = new ArrayList<String>();
		List<String> deps = new ArrayList<String>();
		
 		StringBuilder queryFilter = new StringBuilder();

		// get roles list in header
		// Example 'ROLE_MOD_LDAPADMIN,ROLE_EL_CMS,ROLE_SV_ADMIN,ROLE_ADMINISTRATOR,ROLE_MOD_ANALYTICS,ROLE_MOD_EXTRACTORAPP' 
		String roleListString = headers.getHeaderString("sec-roles");
		
		logger.debug("RoleList : "+ roleListString);
		if(roleListString!=null && !roleListString.isEmpty()){
			
			// Force to add the array of value in first place of a new Array
			String[] roleList = roleListString.split(",");  
 	
			// get commune list in database corresponding to this header
			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append("select distinct cgocommune, ccodep from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".groupe_autorisation ");
			queryBuilder.append(createWhereInQuery(roleList.length, "idgroup"));
			queryBuilder.append(";");
	
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);	
			limitations = jdbcTemplate.queryForList(queryBuilder.toString(), roleList);
					
			// filter request on commune
			if (limitations != null && !limitations.isEmpty()) {
				
				for (Map<String, Object> limitation : limitations) {
					if(limitation.get("cgocommune") != null){
						communes.add((String)limitation.get("cgocommune"));
					}
					if(limitation.get("ccodep") != null){ 
						deps.add((String)limitation.get("ccodep"));
					}
				}
	
				// If table contains cgocommune
				if(!deps.isEmpty()){
					for (String dep : deps){
						queryFilter.append(" AND ");
						queryFilter.append(tableAlias);
						queryFilter.append("cgocommune LIKE ");
						queryFilter.append("'" +dep+"%'");					
					}			
				}
				if(!communes.isEmpty()){
					queryFilter.append(" AND ");
					queryFilter.append(tableAlias);
					queryFilter.append("cgocommune IN (");
					queryFilter.append(createListToStringQuery(communes));					
					queryFilter.append(" ) ");
				}			
			}			
		}
		else{
			logger.warn("No filter, no sec-roles was found");
		}

		return queryFilter.toString();

	}

	/**
	 * Add a clause like in query
	 * 
	 * @param libelle
	 * @param value
	 * @return
	 */
	protected String createLikeClauseRequest(String libelle, String value, List<String> paramList) {

		StringBuilder subQuery = new StringBuilder();

		if (value != null && !value.isEmpty()) {
			if (!isWhereAdded) {
				subQuery.append(" where ");
				isWhereAdded = true;
			} else {
				subQuery.append(" and ");
			}
			subQuery.append(libelle);
			subQuery.append(" LIKE ? ");
			paramList.add("%"+value+"%");

		}
		return subQuery.toString();
	}
	
	/**
	 * Add a clause like in query
	 * 
	 * @param libelle
	 * @param value
	 * @return
	 */
	protected String createRightLikeClauseRequest(String libelle, String value, List<String> paramList) {

		StringBuilder subQuery = new StringBuilder();

		if (value != null && !value.isEmpty()) {
			if (!isWhereAdded) {
				subQuery.append(" where ");
				isWhereAdded = true;
			} else {
				subQuery.append(" and ");
			}
			subQuery.append(libelle);
			subQuery.append(" LIKE ?");
			paramList.add(value+"%");
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
	protected String createEqualsClauseRequest(String libelle, String value, List<String> paramList) {

		StringBuilder subQuery = new StringBuilder();

		if (value != null && !value.isEmpty()) {
			if (!isWhereAdded) {
				subQuery.append(" where ");
				isWhereAdded = true;
			} else {
				subQuery.append(" and ");
			}

			subQuery.append(libelle);
			subQuery.append(" = ? ");
			paramList.add(value);
		}
		return subQuery.toString();
	}

	/**
	 * Create a string of value from a list
	 * 	exemple : List (1,2,3,4) -> "'1','2','3'"
	 * 
	 * @param values List<String> wanted to be parsed
	 * 
	 * @return String "'value1','value2'"
	 */
	protected String createListToStringQuery(List<String> values) {

		StringBuilder listToString = new StringBuilder();

		if (values != null && !values.isEmpty()) {
			for (String value : values) {
				listToString.append("'");
				listToString.append(value);
				listToString.append("',");
			}
			// remove last coma check not empty
			listToString.deleteCharAt(listToString.length() - 1);
			logger.debug("List to String : " + listToString);

		}

		return listToString.toString();
	}
	
	
	/**
	 * Create a String WHERE paramName IN (?,?,?) where number of ? depends on size given
	 * 
	 * @param size number of parameters wanted
	 * @param paramName name of the parameter
	 * @return String "WHERE 'paramName' IN (?,?,?)
	 */
	protected String createWhereInQuery(int size, String paramName) {

		StringBuilder listToString = new StringBuilder();

		for (int i=0; i<size ; i++) {
			if(i == 0){
				listToString.append(" WHERE ");
				listToString.append(paramName);
				listToString.append(" IN (");
			}
			listToString.append("?,");
			if(i == size -1){
				// remove last coma check not empty
				listToString.deleteCharAt(listToString.length() - 1);
				listToString.append(") ");
			}
		}

		logger.debug("List to String : " + listToString);
		
		return listToString.toString();
	}
	
	/**
	 * Set boolean isWhereAdded to false
	 * 
	 * @return ;
	 */
	protected String finalizeQuery(){
		isWhereAdded =false;
		return (";");
	}
	

}
