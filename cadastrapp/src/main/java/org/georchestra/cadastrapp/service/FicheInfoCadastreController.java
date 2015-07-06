package org.georchestra.cadastrapp.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

@Path("/getFIC")
public class FicheInfoCadastreController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(FicheInfoCadastreController.class);

	@GET
	@Produces("application/json")
	/**
	 *  Return all information need to fill cadastre information panel
	 *  
	 * @param headers
	 * @param parcelle / Id Parcelle exemple : 2014630103000AP0025
	 * @param onglet (for 0 to 5)
	 * 
	 * @return Json object corresponding on the onglet to fullfill
	 */
	public List<Map<String, Object>> getInformationCadastrale(@Context HttpHeaders headers, 
			@QueryParam("parcelle") String parcelle,
			@QueryParam("onglet") int onglet) {

		logger.debug(" parcelle : " + parcelle + " onglet : " + onglet);
		
		List<Map<String, Object>> information = null;

		switch (onglet) {
			case 0:
				information = infoOngletParcelle(parcelle);
				break;
			case 1:
				information = infoOngletProprietaire(parcelle);
				break;
			case 2:
				information = infoOngletBatiment(parcelle);
				break;
			case 3:
				information = infoOngletSubdivision(parcelle);
				break;
			case 4:
				information = infoOngletHistorique(parcelle);
				break;
			default:
				logger.error(" No operations for onglet : " + onglet);
				break;
		}
	
		// Return value providers will convert to JSON
		return information;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<Map<String, Object>> infoOngletParcelle(String parcelle){
		
		logger.debug("infoOngletParcelle - parcelle : " + parcelle);
		
		StringBuilder queryBuilder = new StringBuilder();
		
		// CNIL niveau 0
		//TODO add p.supf
		queryBuilder.append("select p.ccodep, p.ccodir, p.ccocom, c.libcom, p.ccopre, p.ccosec, p.dnupla, p.dnvoiri, p.dindic, p.cconvo, p.dvoilib, p.gparbat, p.gurbpa");
		queryBuilder.append(" from ");

		queryBuilder.append(databaseSchema);
		queryBuilder.append(".parcelle p, ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".commune c ");
		
		queryBuilder.append(createEqualsClauseRequest("p.parcelle", parcelle));
		queryBuilder.append(" and p.ccocom = c.ccocom and p.ccodep = c.ccodep ORDER BY p.parcelle DESC LIMIT 25");
		queryBuilder.append(finalizeQuery());
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForList(queryBuilder.toString());
	}
	
	/**
	 * 
	 * @return
	 */
	private List<Map<String, Object>> infoOngletProprietaire(String parcelle){
		
		logger.debug("infoOngletProprietaire - parcelle : " + parcelle);
		
		StringBuilder queryBuilder = new StringBuilder();
		
		// CNIL Niveau 1
		queryBuilder.append("select p.dnupro, p.dnomlp, p.dprnlp, p.epxnee, p.dnomcp, p.dprncp, p.dlign3, p.dlign4, p.dlign5, p.dlign6, p.dldnss, p.jdatnss, p.ccodro, p.ccodro_lib");
		queryBuilder.append(" from ");

		queryBuilder.append(databaseSchema);
		queryBuilder.append(".parcelle parc,");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".proprietaire p ");
		
		queryBuilder.append(createEqualsClauseRequest("parc.parcelle", parcelle));
		queryBuilder.append(" and p.comptecommunal = parc.comptecommunal ORDER BY p.dnomlp DESC LIMIT 25");
		queryBuilder.append(finalizeQuery());
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForList(queryBuilder.toString());	
	}
	
	/**
	 * 
	 * @return
	 */
	private List<Map<String, Object>> infoOngletBatiment(String parcelle){
		
		logger.debug("infoOngletBatiment - parcelle : " + parcelle);
		
		StringBuilder queryBuilder = new StringBuilder();
		
		// CNIL Niveau 2
		queryBuilder.append("select pb.dnubat, ");
		
		// Jointure locaux
		// TODO  pb.cconlc_lib , pb.dvltrt, pb.jannat
		queryBuilder.append("p.annee, pb.invar, pb.descr, pb.dniv, pb.dpor, pb.jdatat, pb.janimp, ");
		queryBuilder.append("pb.dnupro, prop.dnomlp, prop.dprnlp, prop.epxnee, prop.dnomcp, prop.dprncp ");
		
		queryBuilder.append(" from ");
		
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".parcelle p,");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".proprietebatie pb, ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".proprietaire prop ");
		
		queryBuilder.append(createEqualsClauseRequest("p.parcelle", parcelle));
		queryBuilder.append(" and p.lot = pb.lot");
		queryBuilder.append(" and pb.comptecommunal = prop.comptecommunal ORDER BY pb.dnubat DESC LIMIT 25");
		queryBuilder.append(finalizeQuery());
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForList(queryBuilder.toString());		
	}
	
	/**
	 * 
	 * @return
	 */
	private List<Map<String, Object>> infoOngletSubdivision(String parcelle){
		
		logger.debug("infoOngletSubdivision - parcelle : " + parcelle);
		
		//TODO Change with subdivision
		StringBuilder queryBuilder = new StringBuilder();
		
		// CNIL Niveau 2
		// , pnb.drcsub
		queryBuilder.append("select pnb.ccosub, pnb.dcntsf, pnb.cgrnum ");
		
		queryBuilder.append(" from ");
		
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".parcelle parc,");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".proprietenonbatie pnb ");
		
		queryBuilder.append(createEqualsClauseRequest("parc.parcelle", parcelle));
		queryBuilder.append(" and pnb.dnupro = parc.dnupro ORDER BY pnb.dcntsf DESC LIMIT 25");
		queryBuilder.append(finalizeQuery());
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForList(queryBuilder.toString());	
	}
	
	/**
	 * 
	 * @return
	 */
	private List<Map<String, Object>> infoOngletHistorique(String parcelle){
		
		logger.debug("infoOngletHistorique - parcelle : " + parcelle);
		
		StringBuilder queryBuilder = new StringBuilder();
		
		// CNIL Niveau 2
		queryBuilder.append("select p.jdatat, p.ccocom, p.ccoprem, p.ccosecm, p.dnuplam, p.type_filiation ");
		
		queryBuilder.append(" from ");
		
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".parcelle p ");
		
		queryBuilder.append(createEqualsClauseRequest("p.parcelle", parcelle));
		queryBuilder.append(finalizeQuery());
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForList(queryBuilder.toString());	
	}

}
