package org.georchestra.cadastrapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;


public class UniteCadastraleController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(UniteCadastraleController.class);

	@Path("/getFIC")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 * TODO change this to 5 separated services
	 * 
	 *  Return all information need to fill cadastre information panel
	 *  
	 * @param headers used to verify user group to check CNIL level and geographic limitation
	 * @param parcelle Id Parcelle unique in all country exemple : 2014630103000AP0025
	 * @param part (for 0 to 5)
	 * 			0 -> Parcelle Information
	 * 			1 -> Proprietaire information
	 * 			2 -> Dnubat batiments lists
	 * 			3 - > Subdivision information
	 * 			4 - > Historical information
	 * @return Json object corresponding on wanted part
	 */
	public List<Map<String, Object>> getInformationCadastrale(@Context HttpHeaders headers, 
			@QueryParam("parcelle") String parcelle,
			@QueryParam("onglet") int onglet) {

		logger.debug(" parcelle : " + parcelle + " onglet : " + onglet);
		
		List<Map<String, Object>> information = new ArrayList<Map<String, Object>>();

		switch (onglet) {
			case 0:
				information = infoOngletParcelle(parcelle, headers);
				break;
			case 1:
				if (getUserCNILLevel(headers)>0){
					information = infoOngletProprietaire(parcelle, headers);
				}
				else{
					logger.info("User does not have enough right to see information about proprietaire");
				}
				break;
			case 2:
				if (getUserCNILLevel(headers)>1){
					information = infoOngletBatimentList(parcelle, headers);
				}
				else{
					logger.info("User does not have enough right to see information about batiment");
				}
				break;
			case 3:
				if (getUserCNILLevel(headers)>1){
					information = infoOngletSubdivision(parcelle, headers);
				}
				else{
					logger.info("User does not have enough right to see information about subdivision");
				}				
				break;
			case 4:
				if (getUserCNILLevel(headers)>1){
				information = infoOngletHistorique(parcelle, headers);
				}
				else{
					logger.info("User does not have enough right to see information about filiation");
				}		
				break;
			default:
				logger.error(" No values to return for onglet : " + onglet);
				break;
		}
	
		// Return value providers will convert to JSON
		return information;
	}
	
	 /**
	 * 
	 * infoOngletParcelle
	 * 
	 * @param String parcelle  / Id Parcelle exemple : 2014630103000AP0025
	 * @param HttpHeaders headers
	 * 
	 * @return List<Map<String, Object>> 
	 */
	private List<Map<String, Object>> infoOngletParcelle(String parcelle, HttpHeaders headers){
		
		logger.debug("infoOngletParcelle - parcelle : " + parcelle);
		
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("select p.cgocommune, c.libcom, p.ccopre, p.ccosec, p.dnupla, p.ccoriv, p.dnvoiri, p.dindic, p.cconvo, p.dvoilib, p.dcntpa, p.surfc, p.gparbat, p.gurbpa");
		queryBuilder.append(" from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".parcelledetails p, ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".commune c where p.parcelle = ? ");
		queryBuilder.append(" and p.cgocommune = c.cgocommune ");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForList(queryBuilder.toString(), parcelle);
	}
	
	/**
	 * 
	 * infoOngletProprietaire
	 * 
	 * @param String parcelle / Id Parcelle exemple : 2014630103000AP0025
	 * @param HttpHeaders headers
	 * 
	 * @return List<Map<String, Object>> 
	 */
	private List<Map<String, Object>> infoOngletProprietaire(String parcelle, HttpHeaders headers){
		
		logger.debug("infoOngletProprietaire - parcelle : " + parcelle);
		
		StringBuilder queryBuilder = new StringBuilder();
		
		// CNIL Niveau 1
		queryBuilder.append("select distinct p.dnulp, p.comptecommunal, p.app_nom_usage, p.dlign3, p.dlign4, p.dlign5, p.dlign6, p.dldnss, p.jdatnss, p.ccodro, p.ccodro_lib");
		queryBuilder.append(" from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".proprietaire_parcelle propar,");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".proprietaire p where propar.parcelle = ? ");
		queryBuilder.append(" and p.comptecommunal = propar.comptecommunal ");
		queryBuilder.append(addAuthorizationFiltering(headers, "p."));
		queryBuilder.append(" ORDER BY p.dnulp, p.app_nom_usage ;");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForList(queryBuilder.toString(), parcelle);	
	}
	
	/**
	 * 
	 * infoOngletBatimentList
	 * 
	 * @param String parcelle / Id Parcelle exemple : 2014630103000AP0025
	 * @param HttpHeaders headers
	 * 
	 * @return List<Map<String, Object>> 
	 */
	private List<Map<String, Object>> infoOngletBatimentList(String parcelle, HttpHeaders headers ){
		
		logger.debug("infoOngletBatiment - parcelle : " + parcelle);
		
		StringBuilder queryBuilder = new StringBuilder();
		
		// CNIL Niveau 2
		queryBuilder.append("select distinct pb.dnubat from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".proprietebatie pb ");
		queryBuilder.append(" where pb.parcelle = ? ");
		queryBuilder.append(addAuthorizationFiltering(headers, "pb."));
		queryBuilder.append(" ORDER BY pb.dnubat");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForList(queryBuilder.toString(), parcelle);		
	}
		
	/**
	 *  infoOngletSubdivision get information about subdivision
	 *  
	 *  This method is filtered using information contains in httpheader
	 *  
	 *  call to this method should  only be done by CNIL2 level user with geographical rights
	 *  
	 * @param String parcelle / Id Parcelle exemple : 2014630103000AP0025
	 * @param HttpHeaders headers
	 * 
	 * @return List<Map<String, Object>>  containing Lettre indicative, Contenance, Code Nature de culture et Revenu au 01/01
	 */
	private List<Map<String, Object>> infoOngletSubdivision(String parcelle, HttpHeaders headers ){
		
		logger.debug("infoOngletSubdivision - parcelle : " + parcelle);
		
		StringBuilder subDivisionqueryBuilder = new StringBuilder();
		
		// Select information from view proprietenonbatie
		subDivisionqueryBuilder.append("select pnb.ccosub, pnb.dcntsf, pnb.cgrnum, pnb.drcsuba as drcsub from ");
		subDivisionqueryBuilder.append(databaseSchema);
		subDivisionqueryBuilder.append(".proprietenonbatie pnb ");
		subDivisionqueryBuilder.append(" where pnb.parcelle = ? ");
		subDivisionqueryBuilder.append(addAuthorizationFiltering(headers, "pnb."));
		
		// init jdbc template
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		// return list to service 
		return jdbcTemplate.queryForList(subDivisionqueryBuilder.toString(), parcelle);	
	}
	
	/**
	 * infoOngletHistorique
	 * 
	 * @param String parcelle / Id Parcelle exemple : 2014630103000AP0025
	 * @param HttpHeaders headers
	 * @return  List<Map<String, Object>>
	 */
	private List<Map<String, Object>> infoOngletHistorique(String parcelle, HttpHeaders headers){
		
		logger.debug("infoOngletHistorique - parcelle : " + parcelle);
		
		StringBuilder queryBuilder = new StringBuilder();
		
		// CNIL Niveau 2
		queryBuilder.append("select p.jdatat, p.ccocomm, p.ccoprem, p.ccosecm, p.dnuplam, p.type_filiation from ");	
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".parcelledetails p where p.parcelle = ? ");
		queryBuilder.append(addAuthorizationFiltering(headers, "p."));
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForList(queryBuilder.toString(), parcelle);	
	}
	
}
