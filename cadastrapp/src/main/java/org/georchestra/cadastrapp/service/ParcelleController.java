package org.georchestra.cadastrapp.service;

import javax.annotation.Resource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

@Path("/getParcelle")
public class ParcelleController extends CadController {
	
	final static Logger logger = LoggerFactory.getLogger(ParcelleController.class);

	@Resource(name = "dbDataSource")
	private DataSource dataSource;

	@GET
	@Path("/id/{parcelle}")
	@Produces("application/json")
	/**
	 * 
	 * @param parcelle
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getParcelleById(
			@PathParam("parcelle") String parcelle) throws SQLException {

		List<Map<String, Object>> parcelles = null;

		String query = "select parcelle, ccodep, ccodir, ccocom, ccopre, ccosec, dnupla, dnvoiri, dindic, dvoilib "
				+ "from cadastreapp_qgis.parcelle where parcelle LIKE '%"
				+ parcelle + "%';";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		parcelles = jdbcTemplate.queryForList(query);

		return parcelles;
	}

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
			@QueryParam("parcelle") String parcelle,
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

		List<Map<String, Object>> parcelles = null;

		List<String> mandatoryParameters = new ArrayList<String>();
		mandatoryParameters.add(ccodep);
		mandatoryParameters.add(ccocom);
		mandatoryParameters.add(ccopre);
		mandatoryParameters.add(ccosec);

		// Avoid to do request if mandatory parameters are not set
		checkMandatoryParameter(mandatoryParameters);
		
		// Create query
		StringBuilder queryBuilder = new StringBuilder();

		queryBuilder.append("select ");
		
		if(details.equals("0")){
			queryBuilder.append("dnupla");
		}
		else{
			queryBuilder.append("parcelle, ccodep, ccodir, ccocom, ccopre, ccosec, dnupla, dnvoiri, dindic, dvoilib");
			
			//TODO userlevel
			/*if(user.isCNILLvl1 || user.isCNILlvl2){
				queryBuilder.append(", dnupro "); 
			}*/
		}
	
		queryBuilder.append(" from ");
		// TODO replace this by configuration
		queryBuilder.append("cadastreapp_qgis.parcelle");
		queryBuilder.append(" where ccodep ='");
		queryBuilder.append(ccodep);
		queryBuilder.append("'");
		queryBuilder.append(createEqualsClauseRequest("ccocom", ccocom));
		queryBuilder.append(createEqualsClauseRequest("ccopre", ccopre));
		queryBuilder.append(createEqualsClauseRequest("ccocom", ccocom));
		queryBuilder.append(createEqualsClauseRequest("ccocom", ccosec));
		queryBuilder.append(createEqualsClauseRequest("dnupla", dnupla));
		queryBuilder.append(createEqualsClauseRequest("dnvoiri", dnvoiri));
		queryBuilder.append(createEqualsClauseRequest("dlindic", dlindic));
		queryBuilder.append(createEqualsClauseRequest("natvoiriv_lib", natvoiriv_lib));
		queryBuilder.append(createEqualsClauseRequest("dvoilib", dvoilib));
		queryBuilder.append(createEqualsClauseRequest("dnomlp", dnomlp));
		queryBuilder.append(createEqualsClauseRequest("dprnlp", dprnlp));
		queryBuilder.append(createEqualsClauseRequest("dnomcp", dnomcp));
		queryBuilder.append(createEqualsClauseRequest("dprncp", dprncp));
					
			
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		parcelles = jdbcTemplate.queryForList(queryBuilder.toString());

		return parcelles;
	}

	

}
