package org.georchestra.cadastrapp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CadController {
	


	final static Logger logger = LoggerFactory.getLogger(CadController.class);
	
	private boolean isWhereAdded;
	
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
	 * 
	 * @return
	 */
	public int getUserCNILLevel() {

		logger.info(" Check user CNIL Level : ");
		
		return 0;
	}
	
	/**
	 * 
	 * @param clause
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
