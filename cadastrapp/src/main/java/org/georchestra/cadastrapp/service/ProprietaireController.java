package org.georchestra.cadastrapp.service;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.lang3.StringUtils;
import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.georchestra.cadastrapp.service.export.ExportHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class ProprietaireController extends CadController{

	static final Logger logger = LoggerFactory.getLogger(ProprietaireController.class);
	
	private final String ACCES_ERROR_LOG = "User does not have rights to see thoses informations";
	private final String EMPTY_REQUEST_LOG = "Parcelle Id List is empty nothing to search";
	
	@Autowired
	ExportHelper exportHelper;

	@GET
	@Path("/getProprietaire")
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 * This will return information about owners in JSON format
	 * 
	 * You can call this service with different input, but some are mandatory
	 * 
	 * User must be at least CNIL 1 level to get information from this service
	 * 
	 *   If you call with dnupro, cgocommune is mandatory
	 *   If you call with dnomlp, cgocommune is mandatory and dnomlp must me at least n chars
	 *   
	 *   in addition results are filtered by the geographical limitation of the user group
	 * 
	 * @param headers headers from request used to filter search using LDAP Roles
	 * @param dnomlp partial name to be search, must be have at least n char
	 * @param cgocommun  code commune like 630103 (codep + codir + cocom)
	 * 					cgocommun should be on 6 char
	 * @param dnupro id to be search, a same dnupro can be found in several commune
	 * @param comptecommunal id specific for a owner
	 * @param birthsearch is a boolean to know when searching with ddenom you want to search as well in app_nom_naissance, default value is false
	 * 
	 * @param details -> change list of fields in result 0 by default or if not present
	 * 					0 : app_nom_naissance, app_nom_usage
	 * 					1 : app_nom_usage, app_nom_naissance, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro_lib, comptecommunal will be return
	 * 					2 : comptecommunal, app_nom_usage
	 * 					 
	 * 
	 * @return list of information about proprietaire depending on details level asked
	 * 
	 * @throws SQLException
	 */
	public List<Map<String,Object>> getProprietairesList(
			@Context HttpHeaders headers,
			@QueryParam("dnomlp") String dnomlp,
			@QueryParam("cgocommune") String cgocommune,
			@QueryParam("dnupro") String dnupro,
			@QueryParam("comptecommunal") String compteCommunal,
			@QueryParam("globalname") String globalName,
			@QueryParam("ddenom") String ddenom,
			@DefaultValue("false") @QueryParam("birthsearch") boolean isBirthSearch,
			@DefaultValue("0") @QueryParam("details") int details
			) throws SQLException {

		// Init list to return response even if nothing in it.
		List<Map<String,Object>> proprietaires = new ArrayList<Map<String,Object>>();;
		List<String> queryParams = new ArrayList<String>();

		logger.info("details : " + details);
		// User need to be at least CNIL1 level
		if (getUserCNILLevel(headers)>0){

			int cgoCommuneLength = Integer.parseInt(CadastrappPlaceHolder.getProperty("cgoCommune.length"));

			// No search if all parameters are null or dnomlpPariel less than n char
			// when searching by dnupro, cgocommune is mandatory
			// when searching bu dnomlp, cgocommune is mandatory
			if((dnomlp != null && !dnomlp.isEmpty() && minNbCharForSearch <= dnomlp.length() && cgocommune!=null && cgoCommuneLength == cgocommune.length()) 
					|| (globalName != null && !globalName.isEmpty() && minNbCharForSearch <= globalName.length() && cgocommune!=null && cgoCommuneLength == cgocommune.length())
					|| (ddenom != null && !ddenom.isEmpty() && minNbCharForSearch <= ddenom.length() && cgocommune!=null && cgoCommuneLength == cgocommune.length()) 
					|| (cgocommune!=null &&  cgoCommuneLength == cgocommune.length() && dnupro!=null && dnupro.length()>0)
					|| (compteCommunal != null && compteCommunal.length()>0)){

				StringBuilder queryBuilder = new StringBuilder();
				boolean isWhereAdded = false;

				logger.info("details : " + details);

				if(details == 2){
					queryBuilder.append("select distinct comptecommunal, app_nom_usage ");   		    	   			    
				}
				else if(details == 1){
					queryBuilder.append("select app_nom_usage, app_nom_naissance, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro_lib, comptecommunal ");   			    
				}
				else{
					queryBuilder.append("select distinct app_nom_usage, app_nom_naissance ");				    		    		
				}
				queryBuilder.append(" from ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".proprietaire");

				isWhereAdded = createEqualsClauseRequest(isWhereAdded, queryBuilder, "cgocommune", cgocommune, queryParams);

				// dnomlp can be null here
				if(dnomlp!=null){
					queryBuilder.append(" and UPPER(app_nom_usage) LIKE UPPER(?) ");
					queryParams.add("%"+dnomlp.replace(' ', '%')+"%");
				}

				// globalName can be null here
				if(globalName!=null){
					// replace all space by %
					globalName = globalName.replace(' ', '%');
					queryBuilder.append(" and (UPPER(app_nom_usage) LIKE UPPER(?) or UPPER(app_nom_naissance) LIKE UPPER(?)) ");
					queryParams.add("%"+globalName+"%");
					queryParams.add("%"+globalName+"%");
				}

				// search by ddenom
				if(ddenom!=null){
					// replace all space by %
					ddenom = ddenom.replace(' ', '%');
					if(isBirthSearch){
						logger.debug("Search owners with birth informations ");
						queryBuilder.append("and UPPER(rtrim(app_nom_naissance)) LIKE UPPER(rtrim(?)) ");
						queryParams.add("%"+ddenom+"%");
					}else{
						queryBuilder.append(" and UPPER(rtrim(app_nom_usage)) LIKE UPPER(rtrim(?)) ");
						queryParams.add("%"+ddenom+"%");
					}
				}

				isWhereAdded = createEqualsClauseRequest(isWhereAdded, queryBuilder, "dnupro", dnupro, queryParams);
				isWhereAdded = createEqualsClauseRequest(isWhereAdded, queryBuilder, "comptecommunal", compteCommunal, queryParams);

				queryBuilder.append(addAuthorizationFiltering(headers));

				if(details != 2){
					queryBuilder.append("order by app_nom_usage, app_nom_naissance limit 25 ");
				}

				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				proprietaires = jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
			}
			else{
				//log empty request
				logger.info("Missing cgocommune and dnupro or less than 3 characters for dnomlp in request");
			}
		}else{
			logger.info(ACCES_ERROR_LOG);
		}

		return proprietaires;
	}


	@GET
	@Path("/getProprietairesByParcelles")
	@Produces("application/json")
	/**
	 * This will return information about owners in JSON format
	 *
	 * 
	 * @param headers headers from request used to filter search using LDAP Roles
	 * @param parcelleList
	 * 					 
	 * 
	 * @return list of information about all proprietaire of given parcelles 
	 * 
	 * @throws SQLException
	 */
	public List<Map<String,Object>> getProprietairesByParcelle(
			@Context HttpHeaders headers,
			@QueryParam("parcelles") List<String> parcelleList
			) throws SQLException {

		// Init list to return response even if nothing in it.
		List<Map<String,Object>> proprietaires = new ArrayList<Map<String,Object>>();;

		// User need to be at least CNIL1 level
		if (getUserCNILLevel(headers)>0){

			if(parcelleList != null && !parcelleList.isEmpty()){

				StringBuilder queryBuilder = new StringBuilder();
				queryBuilder.append("select parcelle, comptecommunal, app_nom_usage, app_nom_naissance, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro_lib");
				queryBuilder.append("from ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".proprietaire prop, ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".proprietaire_parcelle proparc ");
				queryBuilder.append(createWhereInQuery(parcelleList.size(), "proparc.parcelle"));
				queryBuilder.append(" and prop.comptecommunal = proparc.comptecommunal");
				queryBuilder.append(addAuthorizationFiltering(headers));

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

	@GET
	@Path("/getProprietairesByInfoParcelles")
	@Produces("application/json")
	/**
	 * This will return information about co-owners in JSON format
	 *
	 * 
	 * @param headers headers from request used to filter search using LDAP Roles
	 * @param commune
	 * @param section containing ccopre+ccosec
	 * @param numero
	 * 					 
	 * 
	 * @return list of information about owners (co-owners id not co-owners list) of given plot 
	 * 
	 * @throws SQLException
	 */
	public List<Map<String,Object>> getProprietairesByInfoParcelle(
			@Context HttpHeaders headers,
			@QueryParam("commune") String commune,
			@QueryParam("section") String section,
			@QueryParam("numero") String numero,
			@QueryParam("ddenom") String ddenom
			) throws SQLException {

		// Init list to return response even if nothing in it.
		List<Map<String,Object>> proprietaires = new ArrayList<Map<String,Object>>();;

		// User need to be at least CNIL1 level
		if (getUserCNILLevel(headers)>0){

			// if search by dnuproList or comptecommunal
			// directly search in view parcelle
			if(commune != null || section != null || numero != null){
				StringBuilder queryBuilder = new StringBuilder();
				List<String> queryParams = new ArrayList<String>();
				queryBuilder.append("select distinct ");
				queryBuilder.append("app_nom_usage ");
				queryBuilder.append("from ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".");
				queryBuilder.append("parcelle p,");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".");
				queryBuilder.append("co_propriete_parcelle copropar, ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".");
				queryBuilder.append("proprietaire pro ");
				queryBuilder.append(" where p.parcelle = copropar.parcelle ");
				queryBuilder.append(" and pro.comptecommunal = copropar.comptecommunal ");
				queryBuilder.append(" and p.cgocommune = ? and p.ccopre||p.ccosec = ? and p.dnupla = ? ");

				queryParams.add(commune);
				queryParams.add(section);
				queryParams.add(numero);
				if(ddenom!=null){
					queryBuilder.append(" and UPPER(rtrim(app_nom_usage)) LIKE UPPER(rtrim(?)) ");
					queryParams.add("%"+ddenom.replace(' ', '%')+"%");
				}

				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				proprietaires = jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
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
	
	
	@POST
	@Path("/exportProprietaireByParcelles")
	@Produces("text/csv")
	/**
	 * Create a csv file from given parcelles id
	 * 
	 * @param headers Used to filter displayed information
	 * @param parcelles list of parcelle separated by a coma
	 * 
	 * @return csv containing list of owners
	 * 
	 * @throws SQLException
	 */
	public Response exportProprietaireByParcelles(
			@Context HttpHeaders headers,
			@FormParam("parcelles") String parcelles) throws SQLException {
		
		// Create empty content
		ResponseBuilder response = Response.noContent();
		
		// User need to be at least CNIL1 level
		if (getUserCNILLevel(headers)>0){
			
			// TODO externalize
			// final String  entete = "comptecommunal;ccoqua_lib;dnomus;dprnus;dnomlp;dprnlp;ddenom;app_nom_usage;app_nom_naissance;dlign3;dling4;dling5;dling6;identitifiantsparcelles;ccodro_lib";	
			String  entete = "Compte communal;Civilité;Nom;Prénom;Nom d'usage;Prénom d'usage;Dénominiation;Nom d'usage;Adresse ligne 3;Adresse ligne 4;Adresse ligne 5;Adresse ligne 6;Identitifiants de parcelles;ccodro_lib";
			if(getUserCNILLevel(headers)>1){
				entete = entete + ";Lieu de naissance; Date de naissance";
			}
			
			String[] parcelleList = StringUtils.split(parcelles, ',');
			
			if(parcelleList != null && parcelleList.length > 0){
				
				logger.debug("Nb of parcelles to search in : " + parcelleList.length);

				// Get value from database
				List<Map<String,Object>> proprietaires = new ArrayList<Map<String,Object>>();
										
				StringBuilder queryBuilder = new StringBuilder();
				queryBuilder.append("select prop.comptecommunal, ccoqua_lib, dnomus, dprnus, dnomlp, dprnlp, ddenom, app_nom_usage, dlign3, dlign4, dlign5, dlign6, ");
				queryBuilder.append("string_agg(parcelle, ','), ccodro_lib ");
				// If user is CNIL2 add birth information
				if(getUserCNILLevel(headers)>1){
					queryBuilder.append(", dldnss, jdatnss ");
				}
				queryBuilder.append("from ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".proprietaire prop, ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".proprietaire_parcelle proparc ");
				queryBuilder.append(createWhereInQuery(parcelleList.length, "proparc.parcelle"));
				queryBuilder.append(" and prop.comptecommunal = proparc.comptecommunal ");
				queryBuilder.append(addAuthorizationFiltering(headers));
				queryBuilder.append("GROUP BY prop.comptecommunal, ccoqua_lib, dnomus, dprnus, dnomlp, dprnlp, ddenom, app_nom_usage, dlign3, dlign4, dlign5, dlign6, ccodro_lib ");
				// If user is CNIL2 add birth information
				if(getUserCNILLevel(headers)>1){
					queryBuilder.append(", dldnss, jdatnss ");
				}
				queryBuilder.append(" ORDER BY prop.comptecommunal");
				
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				proprietaires = jdbcTemplate.queryForList(queryBuilder.toString(), parcelleList);
				
				File file = null;
				try{
					file = exportHelper.createCSV(proprietaires, entete);
					
					// build csv response
					response = Response.ok((Object) file);
					response.header("Content-Disposition", "attachment; filename=" + file.getName());
				}catch (IOException e) {
					logger.error("Error while creating CSV files ", e);
				} finally {
					if (file != null) {
						file.deleteOnExit();
					}
				}
			}
			else{
				//log empty request
				logger.info(EMPTY_REQUEST_LOG);
			}
		}else{
			logger.info(ACCES_ERROR_LOG);
		}
	
		return response.build();
	}
}

