package org.georchestra.cadastrapp.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class CoProprietaireController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(CoProprietaireController.class);

	@Path("/getCoProprietaireList")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 * 
	 * /getCoProprietaireList 
	 * This will return information about owners in JSON format
	 * 
	 * @param headers headers from request used to filter search using LDAP Roles
	 * @param parcelle
	 * 
	 * @return 
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getCoProprietairesList(@Context HttpHeaders headers, 
			@QueryParam("parcelle") String parcelle, 
			@QueryParam("comptecommunal") String comptecommunal,
			@QueryParam("cgocommune") String cgocommune, 
			@QueryParam("ddenom") String ddenom,
			@DefaultValue("0") @QueryParam("details") int details) throws SQLException {

		List<Map<String, Object>> coProprietaires = new ArrayList<Map<String, Object>>();
		List<String> queryParams = new ArrayList<String>();

		// only for CNIL1 and CNIL2
		if (getUserCNILLevel(headers) > 0 && cgocommune != null && cgocommune.length() >0) {

			boolean isParamValid = false;
			
			StringBuilder queryCoProprietaireBuilder = new StringBuilder();
			
			if(details == 1){
				queryCoProprietaireBuilder.append("select distinct proparc.comptecommunal, prop.app_nom_usage ");   		    	   			    
    		}
    		else{
    			queryCoProprietaireBuilder.append("select prop.app_nom_usage, prop.app_nom_naissance, prop.dlign3, prop.dlign4, prop.dlign5, prop.dlign6, prop.dldnss, prop.jdatnss,prop.ccodro_lib, proparc.comptecommunal ");   			    
    		}
			
			queryCoProprietaireBuilder.append(" from ");
			queryCoProprietaireBuilder.append(databaseSchema);
			queryCoProprietaireBuilder.append(".proprietaire prop, ");
			queryCoProprietaireBuilder.append(databaseSchema);
			queryCoProprietaireBuilder.append(".co_propriete_parcelle proparc ");
			queryCoProprietaireBuilder.append(" where prop.cgocommune = ? ");
			queryParams.add(cgocommune);

			if (parcelle != null && parcelle.length() >0) {
				queryCoProprietaireBuilder.append("and proparc.parcelle = ? ");
				queryParams.add(parcelle);
				isParamValid=true;
			} else if (ddenom != null  && ddenom.length() >0) {
				queryCoProprietaireBuilder.append(" and UPPER(rtrim(prop.app_nom_usage)) LIKE UPPER(rtrim(?)) ");
				queryParams.add("%" + ddenom + "%");
				isParamValid=true;
			} else if (comptecommunal != null  && comptecommunal.length() >0) {
				queryCoProprietaireBuilder.append(" and proparc.comptecommunal = ? ");
				queryParams.add(comptecommunal);
				isParamValid=true;
			} else {
				logger.warn(" Not enough parameters to make the sql call");
			}

			if(isParamValid){
				queryCoProprietaireBuilder.append("and prop.comptecommunal = proparc.comptecommunal ");
				queryCoProprietaireBuilder.append(addAuthorizationFiltering(headers, "prop."));
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				coProprietaires = jdbcTemplate.queryForList(queryCoProprietaireBuilder.toString(), queryParams.toArray());
			}
			
		}
		return coProprietaires;
	}

	@Path("/getCoProprietaire")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 * getCoProprietaire
	 * 
	 * @param parcelle
	 * @return
	 */
	public Map<String, Object> getCoProprietaire(@QueryParam("parcelle") String parcelle, @QueryParam("start") int start,@QueryParam("limit") int limit, @Context HttpHeaders headers) {

		logger.debug("get Co Proprietaire - parcelle : " + parcelle);

		Map<String, Object> finalResult = new HashMap<String, Object>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		if (getUserCNILLevel(headers) > 0) {
			
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			
			StringBuilder queryCount = new StringBuilder();
			
			queryCount.append("SELECT COUNT(*) FROM (select distinct p.comptecommunal, p.app_nom_usage from ");
			queryCount.append(databaseSchema);
			queryCount.append(".co_propriete_parcelle propar, ");
			queryCount.append(databaseSchema);
			queryCount.append(".proprietaire p where propar.parcelle = ?  and p.comptecommunal = propar.comptecommunal ");
			queryCount.append(addAuthorizationFiltering(headers, "p."));
			queryCount.append(" ) as temp;");
			
			int resultCount = jdbcTemplate.queryForInt(queryCount.toString(), parcelle);
			
			logger.debug("get Co Proprietaire - number of co-proprietaire : " + resultCount);
			
			finalResult.put("results", resultCount);

			StringBuilder queryBuilder = new StringBuilder();

			// CNIL Niveau 1 or 2
			queryBuilder.append("select distinct p.comptecommunal, p.app_nom_usage, p.dlign3, p.dlign4, p.dlign5, p.dlign6, p.dldnss, p.jdatnss, p.ccodro, p.ccodro_lib");
			queryBuilder.append(" from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".co_propriete_parcelle propar,");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".proprietaire p where propar.parcelle = ? ");
			queryBuilder.append(" and p.comptecommunal = propar.comptecommunal ");
			queryBuilder.append(addAuthorizationFiltering(headers, "p."));
			queryBuilder.append(" ORDER BY p.app_nom_usage ");
			queryBuilder.append(" LIMIT ").append(limit);
			queryBuilder.append(" OFFSET ").append(start);
			
			result = jdbcTemplate.queryForList(queryBuilder.toString(), parcelle);
			
			finalResult.put("rows", result);

		} else {
			logger.info("User does not have enough right to see information about proprietaire");
		}

		return finalResult;

	}
}
