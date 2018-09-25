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
	
	
	static final Logger logger = LoggerFactory.getLogger(CadController.class);
	
	@Resource(name = "dbDataSource")
	protected DataSource dataSource;
	
	protected String databaseSchema;
	
	protected String cnil2RoleName;
	
	protected String cnil1RoleName;
	
	protected String roleSeparator;
	
	protected int minNbCharForSearch;
	
	protected int parcelleLength;
	
	protected boolean isSearchFiltered;
	
	
	/**
	 * 
	 */
	public CadController() {
		super();
		this.databaseSchema = CadastrappPlaceHolder.getProperty("schema.name");
		this.cnil1RoleName = CadastrappPlaceHolder.getProperty("cnil1RoleName");
		this.cnil2RoleName = CadastrappPlaceHolder.getProperty("cnil2RoleName");
		this.roleSeparator = CadastrappPlaceHolder.getProperty("roleSeparator");
		this.minNbCharForSearch = Integer.parseInt(CadastrappPlaceHolder.getProperty("minNbCharForSearch"));
		this.isSearchFiltered =  "1".equals(CadastrappPlaceHolder.getProperty("user.search.are.filtered")) ? true : false;
		this.parcelleLength = Integer.parseInt(CadastrappPlaceHolder.getProperty("parcelleId.length"));
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

		String usernameString = headers.getHeaderString("sec-username");
		if (usernameString == null){
			logger.debug("Not checking geographical limitation, anonymous user");
			return queryFilter.toString();
		}
		// get org in header
		String orgString = headers.getHeaderString("sec-org");
		
		logger.debug("Check user " + usernameString + " with org : "+ orgString + "geographical limitation ");
		if(orgString!=null){
			// get commune list in database corresponding to this org
			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append("select distinct cgocommune, ccodep from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".groupe_autorisation ");
			queryBuilder.append(createWhereInQuery(1, "idgroup"));
			queryBuilder.append(";");
			
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);	
			limitations = jdbcTemplate.queryForList(queryBuilder.toString(), "ROLE_EL_"+orgString);
					
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
				
				if(logger.isDebugEnabled()){
					logger.debug("User have geographical limitation on zip code : " + communes.toString());
					logger.debug("User have geographical limitation on dep : " + deps.toString());
				}
				
	
				// If table contains cgocommune
				if(!deps.isEmpty()){
					queryFilter.append(" AND ( ");
					boolean isFirstDep = true;
					for (String dep : deps){
						if(!isFirstDep){
							queryFilter.append(" OR ");
						}
						queryFilter.append(tableAlias);
						queryFilter.append("cgocommune LIKE ");
						queryFilter.append("'" +dep+"%' ");	
						if(isFirstDep){
							isFirstDep = false;
						}
					}		
					queryFilter.append(" ) ");
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
			logger.error("Connected user but no sec-org header, something is wrong");
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
	protected boolean createLikeClauseRequest(boolean isWhereAdded, StringBuilder sb, String libelle, String value, List<String> paramList) {

		if (value != null && !value.isEmpty()) {
			if (!isWhereAdded) {
				sb.append(" where ");
				isWhereAdded = true;
			} else {
				sb.append(" and ");
			}
			sb.append(libelle);
			sb.append(" LIKE ? ");
			paramList.add("%"+value+"%");

		}
		return isWhereAdded;
	}
	
	/**
	 * Add a clause like in query
	 * 
	 * @param libelle
	 * @param value
	 * @return
	 */
	protected boolean createRightLikeClauseRequest(boolean isWhereAdded, StringBuilder sb, String libelle, String value, List<String> paramList) {

		if (value != null && !value.isEmpty()) {
			if (!isWhereAdded) {
				sb.append(" where ");
				isWhereAdded = true;
			} else {
				sb.append(" and ");
			}
			sb.append(libelle);
			sb.append(" LIKE ?");
			paramList.add(value+"%");
		}
		return isWhereAdded;
	}

	/**
	 * Add an equals clause to query
	 * 
	 * @param libelle
	 * @param value
	 * @return
	 */
	protected boolean createEqualsClauseRequest(boolean isWhereAdded, StringBuilder sb, String libelle, String value, List<String> paramList) {

		if (value != null && !value.isEmpty()) {
			if (!isWhereAdded) {
				sb.append(" where ");
				isWhereAdded = true;
			} else {
				sb.append(" and ");
			}

			sb.append(libelle);
			sb.append(" = ? ");
			paramList.add(value);
		}
		return isWhereAdded;
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
	

}
