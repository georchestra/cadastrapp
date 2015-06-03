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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

@Path("/getParcelle")
public class ParcelleController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(ParcelleController.class);
	
	@GET
	@Produces("application/json")
	/**
	 * 
	 * @param parcelle
	 * @param details
	 * @param ccodep
	 * @param ccodir
	 * @param ccocom
	 * @param ccopre
	 * @param ccosec
	 * @param dnupla
	 * @param dnvoiri
	 * @param dlindic
	 * @param natvoiriv_lib
	 * @param dvoilib
	 * @param dnomlp
	 * @param dprnlp
	 * @param dnomcp
	 * @param dprncp
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getParcelleList(
			@Context HttpHeaders headers,
			@QueryParam("parcelle") final List<String> parcelleList,
			@DefaultValue("0") @QueryParam("details") String details,
			@QueryParam("ccodep") String ccodep,
			@QueryParam("ccodir") String ccodir,
			@QueryParam("ccocom") String ccocom,
			@QueryParam("ccopre") String ccopre,
			@QueryParam("ccosec") String ccosec,
			@QueryParam("dnupla") String dnupla,
			@QueryParam("dnvoiri") String dnvoiri,
			@QueryParam("dlindic") String dlindic,
			@QueryParam("natvoiriv_lib") String natvoiriv_lib,
			@QueryParam("dvoilib") String dvoilib,
			@QueryParam("dnomlp") String dnomlp,
			@QueryParam("dprnlp") String dprnlp,
			@QueryParam("dnomcp") String dnomcp,
			@QueryParam("dprncp") String dprncp) throws SQLException {

		List<Map<String, Object>> parcellesResult = null;
		

		// Search by Id Parcelle
		if (parcelleList != null && !parcelleList.isEmpty()) {
			
			parcellesResult = getParcellesById(parcelleList, details, getUserCNILLevel(headers));

		} // Search by attributes
		else {
			// Check mandatory params
			List<String> mandatoryParameters = new ArrayList<String>();
			mandatoryParameters.add(ccodep);
			mandatoryParameters.add(ccocom);
			mandatoryParameters.add(ccopre);
			mandatoryParameters.add(ccosec);
			// Avoid to do request if mandatory parameters are not set
			if (checkAreMandatoryParametersValid(mandatoryParameters)){
				
				StringBuilder queryBuilder = new StringBuilder();
				
				queryBuilder.append(createSelectParcelleQuery(details, getUserCNILLevel(headers)));

				queryBuilder.append(" from ");
				// TODO replace this by configuration
				queryBuilder.append("cadastreapp_qgis.parcelle");
	
				queryBuilder.append(createEqualsClauseRequest("ccodep", ccodep));
				queryBuilder.append(createEqualsClauseRequest("ccodir", ccodir));
				queryBuilder.append(createEqualsClauseRequest("ccocom", ccocom));
				queryBuilder.append(createEqualsClauseRequest("ccopre", ccopre));
				queryBuilder.append(createEqualsClauseRequest("ccosec", ccosec));
				queryBuilder.append(createEqualsClauseRequest("dnupla", dnupla));
				queryBuilder.append(createEqualsClauseRequest("dnvoiri", dnvoiri));
				queryBuilder.append(createEqualsClauseRequest("dlindic", dlindic));
				queryBuilder.append(createEqualsClauseRequest("natvoiriv_lib", natvoiriv_lib));
				queryBuilder.append(createEqualsClauseRequest("dvoilib", dvoilib));
				queryBuilder.append(createEqualsClauseRequest("dnomlp", dnomlp));
				queryBuilder.append(createEqualsClauseRequest("dprnlp", dprnlp));
				queryBuilder.append(createEqualsClauseRequest("dnomcp", dnomcp));
				queryBuilder.append(createEqualsClauseRequest("dprncp", dprncp));
				queryBuilder.append(finalizeQuery());
				
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				parcellesResult = jdbcTemplate.queryForList(queryBuilder.toString());
			}
			else{
				logger.error("Missing Mandatory parameters");
			}
		}

		

		return parcellesResult;
	}

	/**
	 * 
	 * @param parcelle
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getParcelleById(String parcelle, String details, int userCNILLevel) throws SQLException {

		List<Map<String, Object>> parcelles = null;

		StringBuilder queryBuilder = new StringBuilder();
		
		queryBuilder.append(createSelectParcelleQuery(details, userCNILLevel));
		
		queryBuilder.append(" from ");
		// TODO Change by properties
		queryBuilder.append("cadastreapp_qgis.parcelle");
		queryBuilder.append(" where parcelle =' ");
		queryBuilder.append(parcelle);
		queryBuilder.append(" ';");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		parcelles = jdbcTemplate.queryForList(queryBuilder.toString());

		return parcelles;
	}
	
	/**
	 * 
	 * @param parcelle
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getParcellesById(List<String> parcelleList, String details, int userCNILLevel) throws SQLException {

		List<Map<String, Object>> parcelles = null;

		StringBuilder queryBuilder = new StringBuilder();
		
		queryBuilder.append(createSelectParcelleQuery(details, userCNILLevel));
		
		queryBuilder.append(" from ");
		// TODO Change by properties
		queryBuilder.append("cadastreapp_qgis.parcelle");
		queryBuilder.append(" where parcelle IN (");
		queryBuilder.append(createListToStringQuery(parcelleList));
		queryBuilder.append(");");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		parcelles = jdbcTemplate.queryForList(queryBuilder.toString());

		return parcelles;
	}
	
	
	/**
	 * 
	 * @param details
	 * @param userCNILLevel
	 * @return
	 */
	private String createSelectParcelleQuery(String details, int userCNILLevel) {
		
		StringBuilder selectQueryBuilder = new StringBuilder();
		selectQueryBuilder.append("select ");

		if (details.equals("0")) {
			selectQueryBuilder.append("dnupla");
		} else {
			selectQueryBuilder.append("parcelle, ccodep, ccodir, ccocom, ccopre, ccosec, dnupla, dnvoiri, dindic, dvoilib");

			// TODO userlevel
			if (userCNILLevel > 1) {
				selectQueryBuilder.append(", dnupro ");
			}
		}
		
		return selectQueryBuilder.toString();
	}

}
