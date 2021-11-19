package org.georchestra.cadastrapp.service;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.georchestra.cadastrapp.helper.ProprietaireHelper;
import org.georchestra.cadastrapp.service.export.ExportHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProprietaireController extends CadController{

	static final Logger logger = LoggerFactory.getLogger(ProprietaireController.class);
	
	
	@Autowired
	ExportHelper exportHelper;
	
	@Autowired
	ProprietaireHelper  proprietaireHelper;

	@RequestMapping(path = "/getProprietaire", produces = {MediaType.APPLICATION_JSON_VALUE}, method= {RequestMethod.GET})
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
	public 	@ResponseBody List<Map<String,Object>> getProprietairesList(
			@RequestParam(required = false) String dnomlp,
			@RequestParam(required = false) String cgocommune,
			@RequestParam(name= "dnupro", required = false) final List<String> dnuproList,
			@RequestParam(name= "comptecommunal", required = false) String compteCommunal,
			@RequestParam(name= "globalname", required = false) String globalName,
			@RequestParam(required = false) String ddenom,
			@RequestParam(defaultValue = "0", name ="birthsearch", required = false) boolean isBirthSearch,
			@RequestParam(defaultValue = "0", required = false) int details
			) throws SQLException {

		// Init list to return response even if nothing in it.
		List<Map<String,Object>> proprietaires = new ArrayList<Map<String,Object>>();;
		List<String> queryParams = new ArrayList<String>();

		logger.info("details : " + details);
		// User need to be at least CNIL1 level
		if (getUserCNILLevel()>0){

			int cgoCommuneLength = Integer.parseInt(CadastrappPlaceHolder.getProperty("cgoCommune.length"));

			// No search if all parameters are null or dnomlp ddenom less than n char
			// when searching by dnupro, cgocommune is mandatory
			// when searching by dnomlp, cgocommune is mandatory
			if((dnomlp != null && !dnomlp.isEmpty() && minNbCharForSearch <= dnomlp.length() && cgocommune!=null && cgoCommuneLength == cgocommune.length()) 
					|| (globalName != null && !globalName.isEmpty() && minNbCharForSearch <= globalName.length() && cgocommune!=null && cgoCommuneLength == cgocommune.length())
					|| (ddenom != null && !ddenom.isEmpty() && minNbCharForSearch <= ddenom.length() && cgocommune!=null && cgoCommuneLength == cgocommune.length()) 
					|| (cgocommune!=null &&  cgoCommuneLength == cgocommune.length() && dnuproList!=null && dnuproList.size()>0)
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
				
				if (dnuproList!=null && !dnuproList.isEmpty()) {
					queryBuilder.append(createWhereInQuery(dnuproList.size(), "dnupro"));
					queryParams.addAll(dnuproList);
					isWhereAdded = true;
				}

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
				
				isWhereAdded = createEqualsClauseRequest(isWhereAdded, queryBuilder, "comptecommunal", compteCommunal, queryParams);

				queryBuilder.append(addAuthorizationFiltering());

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

	@RequestMapping(path = "/getProprietairesByParcelles", produces = {MediaType.APPLICATION_JSON_VALUE}, method= {RequestMethod.GET})
	/**
	 * This will return information about owners in JSON format
	 *
	 * @param parcelleList
	 * 					 
	 * 
	 * @return list of information about all proprietaire of given parcelles 
	 * 
	 * @throws SQLException
	 */
	public @ResponseBody List<Map<String,Object>> getProprietairesByParcelle(
			@RequestParam("parcelles") List<String> parcelleList
			) throws SQLException {

		return proprietaireHelper.getProprietairesByParcelles(parcelleList, true);
	}

	@RequestMapping(path = "/getProprietairesByInfoParcelles", produces = {MediaType.APPLICATION_JSON_VALUE}, method= {RequestMethod.GET})
	/**
	 * This will return information about co-owners in JSON format
	 *
	 * @param commune
	 * @param section containing ccopre+ccosec
	 * @param numero
	 * 					 
	 * 
	 * @return list of information about owners (co-owners id not co-owners list) of given plot 
	 * 
	 * @throws SQLException
	 */
	public @ResponseBody List<Map<String,Object>> getProprietairesByInfoParcelle(
			@RequestParam(required = true) String commune,
			@RequestParam(required = true) String section,
			@RequestParam(required = true) String numero,
			@RequestParam(required = false) String ddenom
			) throws SQLException {

		// Init list to return response even if nothing in it.
		List<Map<String,Object>> proprietaires = new ArrayList<Map<String,Object>>();;

		// User need to be at least CNIL1 level
		if (getUserCNILLevel()>0){

			if(commune != null && section != null && numero != null){
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
	
	@RequestMapping(path = "/exportProprietaireByParcelles", produces = {"text/csv"}, method= {RequestMethod.POST})
	/**
	 * Create a csv file from given parcelles id
	 * 
	 * @param rolesList Used to filter displayed information
	 * @param parcelles list of parcelle separated by a coma
	 * 
	 * @return csv containing list of owners
	 * 
	 * @throws SQLException
	 */
	public ResponseEntity<byte[]> exportProprietaireByParcelles(
			@RequestParam String parcelles) throws SQLException {
		
		// Create empty content
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(HttpStatus.NO_CONTENT);
		
		// User need to be at least CNIL1 level
		if (getUserCNILLevel()>0){
			
			String  entete = "proprio_id;droit_reel_libelle;denomination_usage;parcelles;civilite;nom_usage;prenom_usage;denomination_naissance;nom_naissance;prenom_naissance;adresse_ligne3;adresse_ligne4;adresse_ligne5;adresse_ligne6;forme_juridique";
			if(getUserCNILLevel()>1){
				entete = entete + ";lieu_naissance; date_naissance";
			}
			
			String[] parcelleList = StringUtils.split(parcelles, ',');
			
			if(parcelleList != null && parcelleList.length > 0){
				
				logger.debug("Nb of parcelles to search in : " + parcelleList.length);

				// Get value from database
				List<Map<String,Object>> proprietaires = new ArrayList<Map<String,Object>>();
										
				StringBuilder queryBuilder = new StringBuilder();
				queryBuilder.append("select prop.comptecommunal, ccodro_lib, app_nom_usage, string_agg(parcelle, ','), ccoqua_lib, dnomus, dprnus, ddenom, dnomlp, dprnlp, dlign3, dlign4, dlign5, dlign6, dformjur ");
				
				// If user is CNIL2 add birth information
				if(getUserCNILLevel()>1){
					queryBuilder.append(", dldnss, jdatnss ");
				}
				queryBuilder.append("from ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".proprietaire prop, ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".proprietaire_parcelle proparc ");
				queryBuilder.append(createWhereInQuery(parcelleList.length, "proparc.parcelle"));
				queryBuilder.append(" and prop.comptecommunal = proparc.comptecommunal ");
				queryBuilder.append(addAuthorizationFiltering());
				queryBuilder.append("GROUP BY prop.comptecommunal, ccodro_lib, app_nom_usage, ccoqua_lib, dnomus, dprnus, ddenom, dnomlp, dprnlp, dlign3, dlign4, dlign5, dlign6, dformjur");
				// If user is CNIL2 add birth information
				if(getUserCNILLevel()>1){
					queryBuilder.append(", dldnss, jdatnss ");
				}
				queryBuilder.append(" ORDER BY prop.comptecommunal");
				
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				proprietaires = jdbcTemplate.queryForList(queryBuilder.toString(), parcelleList);
				
				File file = null;
				try{
					file = exportHelper.createCSV(proprietaires, entete);
					
					// build csv response
					// Create response
					ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
					.filename(file.getName())
					.build();

					HttpHeaders headers = new HttpHeaders();
					headers.setContentDisposition(contentDisposition);

					response = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);

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
	
		return response;
	}
}

