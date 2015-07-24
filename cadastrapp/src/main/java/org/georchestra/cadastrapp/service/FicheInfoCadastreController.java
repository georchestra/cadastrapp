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
	 * @param onglet (for 0 to 4)
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
				if (getUserCNILLevel(headers)>0){
					information = infoOngletProprietaire(parcelle);
				}
				else{
					logger.info("User does not have enough right to see information about proprietaire");
				}
				break;
			case 2:
				if (getUserCNILLevel(headers)>1){
					information = infoOngletBatiment(parcelle);
				}
				else{
					logger.info("User does not have enough right to see information about batiment");
				}
				break;
			case 3:
				if (getUserCNILLevel(headers)>1){
					information = infoOngletSubdivision(parcelle);
				}
				else{
					logger.info("User does not have enough right to see information about subdivision");
				}				
				break;
			case 4:
				if (getUserCNILLevel(headers)>1){
				information = infoOngletHistorique(parcelle);
				}
				else{
					logger.info("User does not have enough right to see information about filiation");
				}		
				break;
			default:
				logger.error(" No values to retunr for onglet : " + onglet);
				break;
		}
	
		// Return value providers will convert to JSON
		return information;
	}
	
	/**
	 * 
	 * @param parcelle
	 * @return
	 */
	private List<Map<String, Object>> infoOngletParcelle(String parcelle){
		
		logger.debug("infoOngletParcelle - parcelle : " + parcelle);
		
		StringBuilder queryBuilder = new StringBuilder();
		
		// CNIL niveau 0
		queryBuilder.append("select p.ccodep, p.ccodir, p.ccocom, c.libcom, p.ccopre, p.ccosec, p.dnupla, p.dnvoiri, p.dindic, p.cconvo, p.dvoilib, p.dcntpa, p.surfc, p.gparbat, p.gurbpa");
		queryBuilder.append(" from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".parcelledetails p, ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".commune c where p.parcelle = ? ");
		queryBuilder.append(" and p.ccodep = c.ccodep and p.ccocom = c.ccocom ");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForList(queryBuilder.toString(), parcelle);
	}
	
	/**
	 * 
	 * @param parcelle
	 * @return
	 */
	private List<Map<String, Object>> infoOngletProprietaire(String parcelle){
		
		logger.debug("infoOngletProprietaire - parcelle : " + parcelle);
		
		StringBuilder queryBuilder = new StringBuilder();
		
		// CNIL Niveau 1
		queryBuilder.append("select p.dnupro, p.ddenom, p.dnomlp, p.dprnlp, p.epxnee, p.dnomcp, p.dprncp, p.dlign3, p.dlign4, p.dlign5, p.dlign6, p.dldnss, p.jdatnss, p.ccodro, p.ccodro_lib");
		queryBuilder.append(" from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".proprietaire_parcelle propar,");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".proprietaire p where propar.parcelle = ?");
		queryBuilder.append(" and p.comptecommunal = propar.comptecommunal ORDER BY p.ddenom");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForList(queryBuilder.toString(), parcelle);	
	}
	
	/**
	 * 
	 * @param parcelle
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
		queryBuilder.append(".proprietaire_parcelle propar,");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".proprietebatie pb, ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".proprietaire prop where propar.parcelle = ?");
		queryBuilder.append(" and propar.dnulot = pb.lot");
		queryBuilder.append(" and pb.comptecommunal = prop.comptecommunal ORDER BY pb.dnubat DESC LIMIT 25");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForList(queryBuilder.toString(), parcelle);		
	}
	
	/**
	 * 
	 * @param parcelle
	 * @return
	 */
	private List<Map<String, Object>> infoOngletSubdivision(String parcelle){
		
		logger.debug("infoOngletSubdivision - parcelle : " + parcelle);
		
		//TODO Change with subdivision
		StringBuilder queryBuilder = new StringBuilder();
		
		// CNIL Niveau 2
		// , pnb.drcsub
		queryBuilder.append("select parc.ccodep, parc.ccocom, parc.ccopre, parc.ccosec, parc.dnupla, pnb.ccosub, pnb.dcntsf, pnb.cgrnum from ");	
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".parcelledetails parc,");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".proprietenonbatie pnb ,");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".propriete_parcelle propar ");
		queryBuilder.append(" where propar.parcelle = ?");
		queryBuilder.append(" and pnb.comptecommunal = propar.comptecommunal");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForList(queryBuilder.toString(), parcelle);	
	}
	
	/**
	 * 
	 * @param parcelle
	 * @return
	 */
	private List<Map<String, Object>> infoOngletHistorique(String parcelle){
		
		logger.debug("infoOngletHistorique - parcelle : " + parcelle);
		
		StringBuilder queryBuilder = new StringBuilder();
		
		// CNIL Niveau 2
		queryBuilder.append("select p.jdatat, p.ccocom, p.ccoprem, p.ccosecm, p.dnuplam, p.type_filiation from ");	
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".parcelledetails p where p.parcelle = ?");
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForList(queryBuilder.toString(), parcelle);	
	}

}
