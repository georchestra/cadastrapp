package org.georchestra.cadastrapp.service;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 
 * @author gfi
 *
 */
@Configuration
@PropertySource("classpath:cadastrapp.properties")
public class CadController {
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	 }
	
	final static Logger logger = LoggerFactory.getLogger(CadController.class);

	private boolean isWhereAdded;
	
	@Value("${schema.name}")
	protected String databaseSchema;
	
	@Resource(name = "dbDataSource")
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
	public boolean isMandatoryParameterValid(String parameter) {

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
	public boolean checkAreMandatoryParametersValid(List<String> mandatoryList) {

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
	public int getUserCNILLevel(HttpHeaders headers) {

		int cnilLevel = 0;

		logger.debug(" Check user CNIL Level ");

		// Get CNIL Group information
		String rolesList = headers.getHeaderString("sec-roles");
		logger.debug(" Get user roles informations : " + rolesList);
		if (rolesList!=null && rolesList.contains("CNIL2")) {
			cnilLevel = 2;
		} else if (rolesList!=null && rolesList.contains("CNIL1")) {
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
	public String addAuthorizationFiltering(HttpHeaders headers) {

		List<String> communes = null;
		StringBuilder queryFilter = new StringBuilder();

		// get roles list in header
		// Example 'ROLE_MOD_LDAPADMIN,ROLE_EL_CMS,ROLE_SV_ADMIN,ROLE_ADMINISTRATOR,ROLE_MOD_ANALYTICS,ROLE_MOD_EXTRACTORAPP' 
		String rolesList = headers.getHeaderString("sec-roles");
		
		if(rolesList!=null && !rolesList.isEmpty()){
		
			// get commune list in database corresponding to this header
			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append("select ccoinsee from ");
	
			// TODO get schema and table in properties
			queryBuilder.append("cadastreapp_qgis.groupe_autorisation ");
			queryBuilder.append(" where idgroup = '");
			queryBuilder.append(rolesList);
			queryBuilder.append("' ;");
	
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			communes = jdbcTemplate.queryForList(
					queryBuilder.toString(), String.class);
		}
		else{
			logger.warn("Missing sec-roles");
		}

		// filter request
		if (communes != null && !communes.isEmpty()) {
	
			StringBuilder ccoinseeList = new StringBuilder();

			ccoinseeList.append(createListToStringQuery(communes));
			
			queryFilter.append(" AND ccoinsee IN (");
			queryFilter.append(ccoinseeList.toString());
			queryFilter.append(" )");
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
	public String createLikeClauseRequest(String libelle, String value) {

		StringBuilder subQuery = new StringBuilder();

		if (value != null && !value.isEmpty()) {
			if (!isWhereAdded) {
				subQuery.append(" where ");
				isWhereAdded = true;
			} else {
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
	 * Add a clause like in query
	 * 
	 * @param libelle
	 * @param value
	 * @return
	 */
	public String createRightLikeClauseRequest(String libelle, String value) {

		StringBuilder subQuery = new StringBuilder();

		if (value != null && !value.isEmpty()) {
			if (!isWhereAdded) {
				subQuery.append(" where ");
				isWhereAdded = true;
			} else {
				subQuery.append(" and ");
			}
			subQuery.append(libelle);
			subQuery.append(" LIKE '");
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
	public String createEqualsClauseRequest(String libelle, String value) {

		StringBuilder subQuery = new StringBuilder();

		if (value != null && !value.isEmpty()) {
			if (!isWhereAdded) {
				subQuery.append(" where ");
				isWhereAdded = true;
			} else {
				subQuery.append(" and ");
			}

			subQuery.append(libelle);
			subQuery.append(" ='");
			subQuery.append(value);
			subQuery.append("'");
		}
		return subQuery.toString();
	}

	/**
	 *
	 * @param values
	 * @return
	 */
	public String createListToStringQuery(List<String> values) {

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
	 * 
	 * @return
	 */
	public String finalizeQuery(){
		isWhereAdded =false;
		return (";");
	}
	

}
