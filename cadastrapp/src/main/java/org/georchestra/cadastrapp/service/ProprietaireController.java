package org.georchestra.cadastrapp.service;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class ProprietaireController extends CadController{

	final static Logger logger = LoggerFactory.getLogger(ProprietaireController.class);

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
	 * @param maritalsearch is a boolean to know when searching with ddenom you want to search as well in dnomlp, default value is false
	 * 
	 * @param details -> change list of fields in result 0 by default or if not present
	 * 					0 : dnomlp, dprnlp
	 * 					1 : dnomlp, dprnlp, epxnee, dnomcp, dprncp, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro_lib, comptecommunal will be return
	 * 					2 : comptecommunal
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
			@DefaultValue("false") @QueryParam("maritalsearch") boolean isMaritalSearch,
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
					queryBuilder.append("select distinct comptecommunal, ddenom ");   		    	   			    
				}
				else if(details == 1){
					queryBuilder.append("select dnomlp, dprnlp, epxnee, dnomcp, dprncp, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro_lib, comptecommunal ");   			    
				}
				else{
					queryBuilder.append("select distinct dnomlp, dprnlp, dnomcp, dprncp, ddenom");				    		    		
				}
				queryBuilder.append(" from ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".proprietaire");

				isWhereAdded = createEqualsClauseRequest(isWhereAdded, queryBuilder, "cgocommune", cgocommune, queryParams);

				// dnomlp can be null here
				if(dnomlp!=null){
					queryBuilder.append(" and UPPER(dnomlp) LIKE UPPER(?) ");
					queryParams.add("%"+dnomlp+"%");
				}

				// globalName can be null here
				if(globalName!=null){
					// replace all space by %
					globalName = globalName.replace(' ', '%');
					queryBuilder.append(" and (UPPER(dnomlp||' '||dprnlp) LIKE UPPER(?) or UPPER(dnomcp||' '||dprncp) LIKE UPPER(?) or UPPER(ddenom) LIKE UPPER(?)) ");
					queryParams.add(globalName+"%");
					queryParams.add(globalName+"%");
					queryParams.add(globalName+"%");		       
				}

				// search by ddenom
				if(ddenom!=null){
					// replace all space by %
					ddenom = ddenom.replace(' ', '%');
					if(isMaritalSearch){
						logger.debug("Search owners with marital informations ");
						queryBuilder.append("and (UPPER(dnomlp) LIKE UPPER(?) or UPPER(rtrim(ddenom)) LIKE UPPER(rtrim(?))) ");
						queryParams.add(ddenom+"%");
						queryParams.add(ddenom+"%");						
					}else{
						queryBuilder.append(" and UPPER(rtrim(ddenom)) LIKE UPPER(rtrim(?)) ");
						queryParams.add("%"+ddenom+"%");
					}	
				}

				isWhereAdded = createEqualsClauseRequest(isWhereAdded, queryBuilder, "dnupro", dnupro, queryParams);
				isWhereAdded = createEqualsClauseRequest(isWhereAdded, queryBuilder, "comptecommunal", compteCommunal, queryParams);

				queryBuilder.append(addAuthorizationFiltering(headers));

				if(details != 2){
					queryBuilder.append("order by dnomlp, dprnlp, dnomcp,  dprncp limit 25 ");
				}

				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				proprietaires = jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
			}
			else{
				//log empty request
				logger.info("Missing cgocommune and dnupro or less than 3 characters for dnomlp in request");
			}
		}else{
			logger.info("User does not have rights to see thoses informations");
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
				queryBuilder.append("select parcelle, comptecommunal, dnomlp, dprnlp, epxnee, dnomcp, dprncp, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro_lib");   			    
				queryBuilder.append("from ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".proprietaire prop, ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".proprietaire_parcelle proparc ");
				queryBuilder.append("where proparc.parcelle IN (?) and prop.comptecommunal = proparc.comptecommunal");
				queryBuilder.append(addAuthorizationFiltering(headers));

				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				proprietaires = jdbcTemplate.queryForList(queryBuilder.toString(), parcelleList.toArray());
			}
			else{
				//log empty request
				logger.info("Parcelle Id List is empty nothing to search");
			}
		}else{
			logger.info("User does not have rights to see thoses informations");
		}

		return proprietaires;
	}

	@GET
	@Path("/getProprietairesByInfoParcelles")
	@Produces("application/json")
	/**
	 * This will return information about owners in JSON format
	 *
	 * 
	 * @param headers headers from request used to filter search using LDAP Roles
	 * @param commune
	 * @param section
	 * @param numero
	 * 					 
	 * 
	 * @return list of information about all proprietaire of given parcelles 
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
				queryBuilder.append("ddenom ");
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
				queryBuilder.append(" and p.cgocommune = ? and p.ccosec = ? and p.dnupla = ? ");

				queryParams.add(commune);
				queryParams.add(section);
				queryParams.add(numero);
				if(ddenom!=null){
					queryBuilder.append(" and UPPER(rtrim(ddenom)) LIKE UPPER(rtrim(?)) ");
					queryParams.add("%"+ddenom+"%");
				}

				queryBuilder.append(";");

				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				proprietaires = jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
			}
			else{
				//log empty request
				logger.info("Parcelle Id List is empty nothing to search");
			}
		}else{
			logger.info("User does not have rights to see thoses informations");
		}

		return proprietaires;
	}
}

