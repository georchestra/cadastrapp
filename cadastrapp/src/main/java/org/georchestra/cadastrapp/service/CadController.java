package org.georchestra.cadastrapp.service;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	protected final String ACCES_ERROR_LOG = "User does not have rights to see thoses informations";
	protected final String EMPTY_REQUEST_LOG = "Parcelle Id List is empty nothing to search";
	
	
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
	 * Check if mandatory parameter are valid
	 * 
	 * @param	parameter	mandatoryList
	 * @return	true if parameter is not null or empty
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
	 * @param	mandatoryList	list of string parameter
	 * @return	true if all parameter are valid
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
	 * @param	headers	httpheader information, here we need sec-roles information
	 * @return 0 if user doesnot have any specific right, 1 for CNILLEVEL 1 et 2 for CNILLEVEL 2
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
	 * @param	headers	to search groups filtering
	 * @return	an SQL condition to add to initial query
	 */
	protected String addAuthorizationFiltering(HttpHeaders headers) {
		return addAuthorizationFiltering(headers, "");
	}
	
	/**
	 * Filter information depending on groups information
	 * 
	 * 
	 * @param headers	to search groups filtering
	 * @param tableAlias	table alias for original request to add to the condition
	 *  
	 * @return query to complete user right
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
		// get roles in heade
		String roleListString = headers.getHeaderString("sec-roles");
		
		// merge org+roles to get groups list
		List<String> groupsList = new ArrayList<String>();
		if(orgString!=null && !orgString.isEmpty()){
			groupsList.add(orgString);
		}
		if(roleListString!=null && !roleListString.isEmpty()){
			// set separator by default if not set
			if(roleSeparator.isEmpty()){
				roleSeparator = ";";
			}
			groupsList.addAll(Arrays.asList(roleListString.split(roleSeparator)));
		}

		if(!groupsList.isEmpty()){
			// get commune list in database corresponding to those groups
			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append("select distinct cgocommune, ccodep from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".groupe_autorisation ");
			queryBuilder.append(createWhereInQuery(groupsList.size(), "idgroup"));
			queryBuilder.append(";");
			
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);	
			limitations = jdbcTemplate.queryForList(queryBuilder.toString(), groupsList.toArray(new String[groupsList.size()]));
					
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
				
	
				queryFilter.append(" AND ( ");
				// If table contains cgocommune
				if(!deps.isEmpty()){
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
				}
				if(!communes.isEmpty()){
					if(!deps.isEmpty()) {
						queryFilter.append(" OR ");
					}
					queryFilter.append(tableAlias);
					queryFilter.append("cgocommune IN (");
					queryFilter.append(createListToStringQuery(communes));					
					queryFilter.append(" ) ");
				}			
				queryFilter.append(" ) ");
				if(logger.isDebugEnabled()){
					logger.debug("Resulting geographical SQL filter : " + queryFilter.toString());
				}
			}			
		}
		else{
			logger.warn("User authenticated as '" + usernameString + "' but no sec-org header, maybe something is wrong.");
			logger.warn("No filters applied because no sec-roles or sec-org corresponding rules were founds.");
		}

		return queryFilter.toString();

	}

	/**
	 * Add a clause like in query
	 * 
	 * @param	isWhereAdded	boolean to know if an where clause has already been added to the SQL query
	 * @param	sb	SQL request to add where clause
	 * @param	libelle	key parameter
	 * @param	value	value of like parameter 
	 * @param	paramList	final paramList used to execute request after completion
	 * @return	true if where is added for the first time, and add SQL Clause Like to the given stringbuilder with %value%
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
	 * Add a clause like in query, with % only on right side of value
	 * 
	 * @param	isWhereAdded	boolean to know if an where clause has already been added to the SQL query
	 * @param	sb	SQL request to add where clause
	 * @param	libelle	key parameter
	 * @param	value	value of like parameter
	 * @param	paramList	final paramList used to execute request after completion
	 * @return	true if where is added for the first time, and add SQL Clause Like to the given stringbuilder with value%
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
	 * Add a clause equal in query
	 * 
	 * @param	isWhereAdded	boolean to know if an where clause has already been added to the SQL query
	 * @param	sb	SQL request to add where clause
	 * @param	libelle	key parameter
	 * @param	value	value of like parameter
	 * @param	paramList	final paramList used to execute request after completion
	 * @return	true if where is added for the first time, and add SQL Clause equal to the given stringbuilder with value
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
	 * 	exemple : List (1,2,3,4) will be transform in "'1','2','3'"
	 * 
	 * @param values String list wanted to be parsed
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
