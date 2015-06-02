package org.georchestra.cadastrapp.service;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

		logger.info(" Check user CNIL Level ");

		// Get CNIL Group information
		String rolesList = headers.getHeaderString("sec-roles");
		logger.info(" Get user roles informations : " + rolesList);
		if(rolesList.contains("CNIL2")){
			cnilLevel=2;
		}
		else if(rolesList.contains("CNIL1")){
			cnilLevel=1;
		}
		
		logger.info(" Check user CNIL Level : " + cnilLevel);
		return cnilLevel;
	}
	
	
	/**
	 * Filter information depending on groups information
	 * 
	 * 
	 * @param headers
	 * @return
	 */
	public void filterWithGroupsLimitation(HttpHeaders headers) {
		
		// get roles list in header
		
		// get commune list in database corresponding to this header
		
		// filter request		
		
	}
	
	/**
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
