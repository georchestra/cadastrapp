package org.georchestra.cadastrapp.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import org.georchestra.cadastrapp.model.pdf.Imposition;
import org.georchestra.cadastrapp.model.pdf.ImpositionNonBatie;
import org.georchestra.cadastrapp.model.pdf.Lot;
import org.georchestra.cadastrapp.model.pdf.ProprieteBatie;
import org.georchestra.cadastrapp.model.pdf.ProprieteNonBatie;
import org.georchestra.cadastrapp.model.pdf.ProprietesBaties;
import org.georchestra.cadastrapp.model.pdf.ProprietesNonBaties;
import org.georchestra.cadastrapp.model.pdf.Exoneration;
import org.georchestra.cadastrapp.service.CadController;
import org.georchestra.cadastrapp.service.constants.CadastrappConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public final class ProprieteHelper extends CadController {

	static final Logger logger = LoggerFactory.getLogger(ProprieteHelper.class);

	/**
	 * getProprieteBatieInformation
	 * 
	 * @param idCompteCommunal  String owner id
	 * @param idParcelle String plots id
	 * @return ProprietesBaties object with information coming from database
	 * 			
	 */
	public ProprietesBaties getProprieteBatieInformation(String idCompteCommunal, String idParcelle) {

		logger.debug("Get developed property information ");
		
		ProprietesBaties pbs = new ProprietesBaties();

		// Information sur les proprietés baties
		List<ProprieteBatie> proprietesBaties = new ArrayList<ProprieteBatie>();

		float pbCommuneRevenuExonere = 0;
		float pbCommuneRevenuImposable = 0;
		float pbDepartementRevenuExonere = 0;
		float pbDepartementRevenuImposable = 0;
		float pbGroupementCommuneRevenuExonere = 0;
		float pbGroupementCommuneRevenuImposable = 0;
		float pbRevenuImposable = 0;

		StringBuilder queryBuilderProprieteBatie = new StringBuilder();

		queryBuilderProprieteBatie.append("select distinct id_local, ccopre, ccosec, dnupla, COALESCE(natvoi,'')||' '||COALESCE(dvoilib,'') as voie, ccoriv, dnubat, descr, dniv, dpor, invar, ccoaff, ccoeva, cconlc, dcapec, ccolloc, gnextl, jandeb, janimp, gtauom, pexb, rcexba2, rcbaia_com, rcbaia_dep, rcbaia_gp, rcbaia_tse, revcad, jdatat, parcelle, ccocac ");
		queryBuilderProprieteBatie.append("from ");
		queryBuilderProprieteBatie.append(databaseSchema);
		queryBuilderProprieteBatie.append(".proprietebatie pb ");
		queryBuilderProprieteBatie.append(" where pb.comptecommunal = ? ORDER BY ccosec, dnupla");

		// Add map of invar to maker sure not to add two times taxable income
		List<String> invarTICount = new ArrayList<String>();

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> proprietesBatiesResult = jdbcTemplate.queryForList(queryBuilderProprieteBatie.toString(), idCompteCommunal);

		for (Map<String, Object> propBat : proprietesBatiesResult) {
			if (idParcelle == null || idParcelle.isEmpty() || idParcelle.equals((String) propBat.get(CadastrappConstants.PARC_ID))) {
				ProprieteBatie proprieteBatie = new ProprieteBatie();

				if (logger.isDebugEnabled()) {
					logger.debug("Get developed property invar : " + (String) propBat.get(CadastrappConstants.PB_NUM_INVARIANT));
				}

				String proprieteId = (String) propBat.get(CadastrappConstants.PB_NUM_INVARIANT);// N° invar
				proprieteBatie.setInvar(proprieteId);
				String idLocal = (String) propBat.get(CadastrappConstants.PB_ID_LOCAL);

				// Add lot to list
				proprieteBatie.setLots(getLotInformation(idLocal));

				proprieteBatie.setCcoaff((String) propBat.get(CadastrappConstants.PB_AFFECTATION_PEV)); // AF
				proprieteBatie.setCcoeva((String) propBat.get(CadastrappConstants.PB_CODE_EVAL)); // M EVAL
				proprieteBatie.setCconlc((String) propBat.get(CadastrappConstants.PB_NATURE_LOCAL)); // NAT LOC
				proprieteBatie.setCcocac((String) propBat.get(CadastrappConstants.PB_NATURE_LOCAL_PRO)); // NAT LOC PRO
				proprieteBatie.setCcopre((String) propBat.get(CadastrappConstants.PREFIX_SECTION)); // N°Section part1
				proprieteBatie.setCcoriv((String) propBat.get(CadastrappConstants.CODE_RIVOLI_VOIE)); // code rivoli
				proprieteBatie.setCcosec((String) propBat.get(CadastrappConstants.LETTRE_SECTION)); // N°Section partt2
				proprieteBatie.setDcapec((String) propBat.get(CadastrappConstants.PB_CATEGORIE)); // Cat
				proprieteBatie.setDescr((String) propBat.get(CadastrappConstants.PB_NUM_ENTREE)); // Ent
				proprieteBatie.setDniv((String) propBat.get(CadastrappConstants.PB_NIV_ETAGE)); // Niv
				proprieteBatie.setDnubat((String) propBat.get(CadastrappConstants.PB_LETTRE_BAT)); // Bat
				proprieteBatie.setDnupla((String) propBat.get(CadastrappConstants.NUM_PLAN)); // N° plan
				proprieteBatie.setDpor((String) propBat.get(CadastrappConstants.PB_NUM_PORTE_LOCAL)); // N° porte
				proprieteBatie.setRevcad(propBat.get(CadastrappConstants.PB_VAL_LOCAT_TOTAL) == null ? "" : propBat.get(CadastrappConstants.PB_VAL_LOCAT_TOTAL).toString()); // Revenu cadastral
				proprieteBatie.setDvoilib((String) propBat.get(CadastrappConstants.ADRESSE)); // Adresse
				proprieteBatie.setGtauom(propBat.get(CadastrappConstants.PB_MONTANT_TIEOM) == null ? "" : propBat.get(CadastrappConstants.PB_MONTANT_TIEOM).toString()); // tx OM
				if (CadastrappConstants.MUTATION != null) {
					// Date is store in database like jj/mm/yyyy
					String[] parts = propBat.get(CadastrappConstants.MUTATION).toString().split("/");
					if (parts != null && parts.length > 1) {
						proprieteBatie.setJdatat(parts[2]);
					}
				}

				List<Exoneration> proprieteBatieExonerations = new ArrayList<Exoneration>();
				Exoneration proprieteBatieExoneration = new Exoneration();

				proprieteBatieExoneration.setCcolloc((String) propBat.get(CadastrappConstants.PB_CODE_COLL_EXO)); // COLL
				proprieteBatieExoneration.setGnextl((String) propBat.get(CadastrappConstants.PB_NATURE_EXO)); // Nat Exo
				proprieteBatieExoneration.setJandeb((String) propBat.get(CadastrappConstants.PB_ANNEE_DEB_EXO)); // An Deb
				proprieteBatieExoneration.setJanimp((String) propBat.get(CadastrappConstants.PB_ANNEE_RETOUR_IMPOSITION)); // An Ret
				proprieteBatieExoneration.setFcexn(propBat.get(CadastrappConstants.PB_FRACTION_EXO) == null ? "" : propBat.get(CadastrappConstants.PB_FRACTION_EXO).toString()); // Fraction Exo
				proprieteBatieExoneration.setPexb(propBat.get(CadastrappConstants.PB_TAUX_EXO_ACCORDEE) == null ? "" : propBat.get(CadastrappConstants.PB_TAUX_EXO_ACCORDEE).toString());// Exo

				proprieteBatieExonerations.add(proprieteBatieExoneration);
				proprieteBatie.setExonerations(proprieteBatieExonerations);

				// Add exoneration depending on type
				String exoneration = (String) propBat.get(CadastrappConstants.PB_CODE_COLL_EXO);
				if (CadastrappConstants.CODE_COLL_EXO_TC.equals(exoneration)) {
					pbCommuneRevenuExonere = pbCommuneRevenuExonere + ((BigDecimal) propBat.get(CadastrappConstants.PB_FRACTION_EXO)).floatValue();
					pbDepartementRevenuExonere = pbDepartementRevenuExonere + ((BigDecimal) propBat.get(CadastrappConstants.PB_FRACTION_EXO)).floatValue();
					pbGroupementCommuneRevenuExonere = pbGroupementCommuneRevenuExonere + ((BigDecimal) propBat.get(CadastrappConstants.PB_FRACTION_EXO)).floatValue();
				} else if (CadastrappConstants.CODE_COLL_EXO_C.equals(exoneration)) {
					pbCommuneRevenuExonere = pbCommuneRevenuExonere + ((BigDecimal) propBat.get(CadastrappConstants.PB_FRACTION_EXO)).floatValue();
				} else if (CadastrappConstants.CODE_COLL_EXO_GC.equals(exoneration)) {
					pbGroupementCommuneRevenuExonere = pbGroupementCommuneRevenuExonere + ((BigDecimal) propBat.get(CadastrappConstants.PB_FRACTION_EXO)).floatValue();
				} else if (CadastrappConstants.CODE_COLL_EXO_D.equals(exoneration)) {
					pbDepartementRevenuExonere = pbDepartementRevenuExonere + ((BigDecimal) propBat.get(CadastrappConstants.PB_FRACTION_EXO)).floatValue();
				} else if (CadastrappConstants.CODE_COLL_EXO_A.equals(exoneration)) {
					logger.debug("Exoneration on additional taxes");
				}

				// Count only one taxable income for one invar
				if (!invarTICount.contains(proprieteId)) {
					invarTICount.add(proprieteId);

					float communeRevenuImposable = propBat.get("rcbaia_com") == null ? 0 : ((BigDecimal) propBat.get("rcbaia_com")).floatValue();
					float groupementCommuneRevenuImposable = propBat.get("rcbaia_gp") == null ? 0 : ((BigDecimal) propBat.get("rcbaia_gp")).floatValue();
					float departementRevenuImposable = propBat.get("rcbaia_dep") == null ? 0 : ((BigDecimal) propBat.get("rcbaia_dep")).floatValue();
					float tseRevenuImposable = propBat.get("rcbaia_tse") == null ? 0 : ((BigDecimal) propBat.get("rcbaia_tse")).floatValue();

					Imposition pbImposition = new Imposition();

					pbImposition.setCommuneRevenuImposable(communeRevenuImposable);
					pbImposition.setGroupementCommuneRevenuImposable(groupementCommuneRevenuImposable);
					pbImposition.setDepartementRevenuImposable(departementRevenuImposable);
					pbImposition.setTseRevenuImposable(tseRevenuImposable);

					proprieteBatie.setImposition(pbImposition);

					pbRevenuImposable = pbRevenuImposable + (propBat.get(CadastrappConstants.PB_VAL_LOCAT_TOTAL) == null ? 0 : ((BigDecimal) propBat.get(CadastrappConstants.PB_VAL_LOCAT_TOTAL)).floatValue());
					pbCommuneRevenuImposable = pbCommuneRevenuImposable + communeRevenuImposable;
					pbDepartementRevenuImposable = pbDepartementRevenuImposable + departementRevenuImposable;
					pbGroupementCommuneRevenuImposable = pbGroupementCommuneRevenuImposable + groupementCommuneRevenuImposable;
				}

				proprietesBaties.add(proprieteBatie);
			}
		}
		// ajout la liste des propriete baties uniquement si il y en a au moins une
		if (!proprietesBaties.isEmpty()) {

			// Init imposition batie
			Imposition impositionBatie = new Imposition();
			impositionBatie.setCommuneRevenuExonere(pbCommuneRevenuExonere);
			impositionBatie.setCommuneRevenuImposable(pbCommuneRevenuImposable);
			impositionBatie.setDepartementRevenuExonere(pbDepartementRevenuExonere);
			impositionBatie.setDepartementRevenuImposable(pbDepartementRevenuImposable);
			impositionBatie.setGroupementCommuneRevenuExonere(pbGroupementCommuneRevenuExonere);
			impositionBatie.setGroupementCommuneRevenuImposable(pbGroupementCommuneRevenuImposable);
			impositionBatie.setRevenuImposable(pbRevenuImposable);

			pbs.setImposition(impositionBatie);
			pbs.setProprietes(proprietesBaties);
		}
		return pbs;
	}

	/**
	 * getProprieteNonBatieInformation
	 * 
	 * @param idCompteCommunal  String owner id
	 * @param idParcelle String plots id
	 * @return ProprietesBaties object with information coming from database
	 * 			
	 */
	public ProprietesNonBaties getProprieteNonBatieInformation(String idCompteCommunal, String idParcelle) {

		logger.debug("Get undeveloped property information ");
		
		ProprietesNonBaties pnbs = new ProprietesNonBaties();

		// Information sur les proprietés non baties
		List<ProprieteNonBatie> proprietesNonBaties = new ArrayList<ProprieteNonBatie>();

		float pnbCommuneRevenuExonere = 0;
		float pnbCommuneRevenuImposable = 0;
		float pnbDepartementRevenuExonere = 0;
		float pnbDepartementRevenuImposable = 0;
		float pnbGroupementCommuneRevenuExonere = 0;
		float pnbGroupementCommuneRevenuImposable = 0;
		float pnbRevenuImposable = 0;
		float pnbMajorationTerrain = 0;
		int pnbSurface = 0;

		StringBuilder queryBuilderProprieteNonBatie = new StringBuilder();

		queryBuilderProprieteNonBatie.append("select distinct pnb.id_local, pnb.cgocommune, pnb.ccopre, pnb.ccosec, pnb.dnupla, ");
		queryBuilderProprieteNonBatie.append("COALESCE(pnb.natvoi,'')||' '||COALESCE(pnb.dvoilib,'') as voie, pnb.ccoriv, pnb.dparpi, ");
		queryBuilderProprieteNonBatie.append("pnb.ccostn, pnb.ccosub, pnb.cgrnum, pnb.dsgrpf, pnb.dclssf, pnb.cnatsp, pnb.dcntsf, pnb.drcsuba, ");
		queryBuilderProprieteNonBatie.append("pnb.dnulot, pnb.dreflf, pnb.majposa, pnb.bisufad_com, pnb.bisufad_dep, pnb.bisufad_gp, pnb.gparnf, ");
		queryBuilderProprieteNonBatie.append("pnb.jdatat, pnb.parcelle ");
		queryBuilderProprieteNonBatie.append("from ");
		queryBuilderProprieteNonBatie.append(databaseSchema);
		queryBuilderProprieteNonBatie.append(".proprietenonbatie pnb ");
		queryBuilderProprieteNonBatie.append(" where pnb.comptecommunal = ? ORDER BY ccosec, dnupla");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> proprietesNonBatiesResult = jdbcTemplate.queryForList(queryBuilderProprieteNonBatie.toString(), idCompteCommunal);

		for (Map<String, Object> propNonBat : proprietesNonBatiesResult) {
			if (idParcelle == null || idParcelle.isEmpty() || idParcelle.equals((String) propNonBat.get(CadastrappConstants.PARC_ID))) {
				String proprieteNbId = (String) propNonBat.get("id_local");

				if (logger.isDebugEnabled()) {
					logger.debug("Get undeveloped property id : " + proprieteNbId);
				}

				// Get exonerations for this PNB
				StringBuilder queryBuilderProprieteNonBatieExo = new StringBuilder();
				queryBuilderProprieteNonBatieExo.append("SELECT pnbsufexo.id_local, pnbsufexo.ccolloc, pnbsufexo.gnexts, pnbsufexo.jfinex, ");
				queryBuilderProprieteNonBatieExo.append("pnbsufexo.rcexnba, pnbsufexo.pexn ");
				queryBuilderProprieteNonBatieExo.append("from ");
				queryBuilderProprieteNonBatieExo.append(databaseSchema);
				queryBuilderProprieteNonBatieExo.append(".proprietenonbatiesufexo pnbsufexo ");
				queryBuilderProprieteNonBatieExo.append(" where pnbsufexo.cgocommune = ? and pnbsufexo.id_local = ? ORDER BY ccolloc");
				List<Map<String, Object>> proprieteNonBatieExonerationsResult = jdbcTemplate.queryForList(queryBuilderProprieteNonBatieExo.toString(), (String) propNonBat.get("cgocommune"), proprieteNbId);

				List<Exoneration> proprieteNonBatieExonerations = new ArrayList<Exoneration>();
				for (Map<String, Object> propNonBatExo : proprieteNonBatieExonerationsResult) {
					Exoneration proprieteNonBatieExoneration = new Exoneration();

					proprieteNonBatieExoneration.setCcolloc((String) propNonBatExo.get(CadastrappConstants.PNB_CODE_COLL_EXO));
					proprieteNonBatieExoneration.setGnextl((String) propNonBatExo.get(CadastrappConstants.PNB_NATURE_EXO));
					proprieteNonBatieExoneration.setJanimp((String) propNonBatExo.get(CadastrappConstants.PNB_ANNEE_RETOUR_IMPOSITION));
					proprieteNonBatieExoneration.setFcexn(propNonBatExo.get(CadastrappConstants.PNB_FRACTION_RC_EXO) == null ? "" : propNonBatExo.get(CadastrappConstants.PNB_FRACTION_RC_EXO).toString());
					proprieteNonBatieExoneration.setPexb(propNonBatExo.get(CadastrappConstants.PNB_POURCENTAGE_EXO) == null ? "" : propNonBatExo.get(CadastrappConstants.PNB_POURCENTAGE_EXO).toString());// à

					proprieteNonBatieExonerations.add(proprieteNonBatieExoneration);

					// "TC";"toutes les collectivités"
					// "R";"Région => l'exonération porte sur la seule part régionale"
					// "GC";"Groupement de communes"
					// "D";"Département => l'exonération porte sur la seule part départementale"
					// "C";"Commune => l'exonération porte sur la seule part communale"
					// "A";"l'exonération porte sur la taxe additionnelle"
					String exonerationType = (String) propNonBatExo.get(CadastrappConstants.PNB_CODE_COLL_EXO);
					float exonerationValue = (propNonBatExo.get("rcexnba") == null ? 0 : ((BigDecimal) propNonBatExo.get("rcexnba")).floatValue());
					if (CadastrappConstants.CODE_COLL_EXO_TC.equals(exonerationType)) {
						pnbCommuneRevenuExonere = pnbCommuneRevenuExonere + exonerationValue;
						pnbDepartementRevenuExonere = pnbDepartementRevenuExonere + exonerationValue;
						pnbGroupementCommuneRevenuExonere = pnbGroupementCommuneRevenuExonere + exonerationValue;
					} else if (CadastrappConstants.CODE_COLL_EXO_C.equals(exonerationType)) {
						pnbCommuneRevenuExonere = pnbCommuneRevenuExonere + exonerationValue;
					} else if (CadastrappConstants.CODE_COLL_EXO_GC.equals(exonerationType)) {
						pnbGroupementCommuneRevenuExonere = pnbGroupementCommuneRevenuExonere + exonerationValue;
					} else if (CadastrappConstants.CODE_COLL_EXO_D.equals(exonerationType)) {
						pnbDepartementRevenuExonere = pnbDepartementRevenuExonere + exonerationValue;
					} else if (CadastrappConstants.CODE_COLL_EXO_A.equals(exonerationType)) {
						logger.debug("Exoneration on additional taxes");
					}
				}

				ProprieteNonBatie proprieteNonBatie = new ProprieteNonBatie();

				proprieteNonBatie.setCcopre((String) propNonBat.get(CadastrappConstants.PREFIX_SECTION));
				proprieteNonBatie.setCcoriv((String) propNonBat.get(CadastrappConstants.CODE_RIVOLI_VOIE));
				proprieteNonBatie.setCcosec((String) propNonBat.get(CadastrappConstants.LETTRE_SECTION));
				proprieteNonBatie.setCcostn((String) propNonBat.get(CadastrappConstants.PNB_SERIE_TARIF));
				proprieteNonBatie.setCcosub((String) propNonBat.get(CadastrappConstants.PNB_LETTRE_SUF));
				proprieteNonBatie.setCgrnum((String) propNonBat.get(CadastrappConstants.PNB_GROUP_NATURE_CULTURE));
				proprieteNonBatie.setCnatsp((String) propNonBat.get(CadastrappConstants.PNB_CODE_NAT_CULT));
				proprieteNonBatie.setDclssf((String) propNonBat.get(CadastrappConstants.PNB_CLASSE));
				proprieteNonBatie.setDnulot((String) propNonBat.get(CadastrappConstants.PNB_REF_LOT));
				proprieteNonBatie.setDnupla((String) propNonBat.get(CadastrappConstants.NUM_PLAN));
				proprieteNonBatie.setDparpi((String) propNonBat.get(CadastrappConstants.PNB_NUM_PARC_PRIM));
				proprieteNonBatie.setDreflf((String) propNonBat.get("dreflf"));
				proprieteNonBatie.setDsgrpf((String) propNonBat.get(CadastrappConstants.PNB_SOUS_GROUP));
				proprieteNonBatie.setDvoilib((String) propNonBat.get(CadastrappConstants.ADRESSE));
				proprieteNonBatie.setGparnf((String) propNonBat.get(CadastrappConstants.PNB_FPDP));
				proprieteNonBatie.setExonerations(proprieteNonBatieExonerations);

				if (CadastrappConstants.MUTATION != null) {
					// Date is store in database like jj/mm/yyyy
					String[] parts = propNonBat.get(CadastrappConstants.MUTATION).toString().split("/");
					if (parts != null && parts.length > 1) {
						proprieteNonBatie.setJdatat(parts[2]);
					}
				}

				int surface = (Integer) propNonBat.get(CadastrappConstants.PNB_CONTENANCE_CA) == null ? 0 : (Integer) propNonBat.get(CadastrappConstants.PNB_CONTENANCE_CA);
				proprieteNonBatie.setDcntsf(surface);

				float revenu = propNonBat.get(CadastrappConstants.PNB_REVENU_CADASTRAL) == null ? 0 : ((BigDecimal) propNonBat.get(CadastrappConstants.PNB_REVENU_CADASTRAL)).floatValue();
				proprieteNonBatie.setDrcsuba(revenu);

				pnbRevenuImposable = pnbRevenuImposable + revenu;

				pnbCommuneRevenuImposable = pnbCommuneRevenuImposable + (propNonBat.get("bisufad_com") == null ? 0 : ((BigDecimal) propNonBat.get("bisufad_com")).floatValue());
				pnbDepartementRevenuImposable = pnbDepartementRevenuImposable + (propNonBat.get("bisufad_dep") == null ? 0 : ((BigDecimal) propNonBat.get("bisufad_dep")).floatValue());
				pnbGroupementCommuneRevenuImposable = pnbGroupementCommuneRevenuImposable + (propNonBat.get("bisufad_gp") == null ? 0 : ((BigDecimal) propNonBat.get("bisufad_gp")).floatValue());

				pnbMajorationTerrain = pnbMajorationTerrain + (propNonBat.get("majposa") == null ? 0 : ((BigDecimal) propNonBat.get("majposa")).floatValue());

				pnbSurface = pnbSurface + surface;

				proprietesNonBaties.add(proprieteNonBatie);
			}
		}

		// ajout la liste des propriete non baties uniquement si il y en a au moins une
		if (!proprietesNonBaties.isEmpty()) {

			// Init imposition no batie
			ImpositionNonBatie impositionNonBatie = new ImpositionNonBatie();
			impositionNonBatie.setCommuneRevenuExonere(pnbCommuneRevenuExonere);
			impositionNonBatie.setCommuneRevenuImposable(pnbCommuneRevenuImposable);
			impositionNonBatie.setDepartementRevenuExonere(pnbDepartementRevenuExonere);
			impositionNonBatie.setDepartementRevenuImposable(pnbDepartementRevenuImposable);
			impositionNonBatie.setGroupementCommuneRevenuExonere(pnbGroupementCommuneRevenuExonere);
			impositionNonBatie.setGroupementCommuneRevenuImposable(pnbGroupementCommuneRevenuImposable);
			impositionNonBatie.setRevenuImposable(pnbRevenuImposable);
			impositionNonBatie.setMajorationTerraion(pnbMajorationTerrain);
			impositionNonBatie.setSurface(pnbSurface);

			pnbs.setImposition(impositionNonBatie);
			pnbs.setProprietes(proprietesNonBaties);
		}

		return pnbs;
	}


	/**
	 *  getLotInformation 
	 * @param idLocal String local id from invar
	 * @return List<Lot> bundle number and distribution in this bundle
	 */
	public List<Lot> getLotInformation(String idLocal) {

		StringBuilder queryBuilderLots = new StringBuilder();
		queryBuilderLots.append("select dnulot, dnumql, ddenql ");
		queryBuilderLots.append("from ");
		queryBuilderLots.append(databaseSchema);
		queryBuilderLots.append(".lot l ");
		queryBuilderLots.append(" where l.id_local = ? ");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> proprietesBatiesLots = jdbcTemplate.queryForList(queryBuilderLots.toString(), idLocal);

		List<Lot> proprieteBatieLot = new ArrayList<Lot>();
		for (Map<String, Object> propBatLot : proprietesBatiesLots) {
			// Create bundle and add information
			Lot lot = new Lot();
			lot.setLotId((String) propBatLot.get(CadastrappConstants.PB_LOT_ID));
			lot.setDenominateur((String) propBatLot.get(CadastrappConstants.PB_LOT_DENOMINATEUR));
			lot.setNumerateur((String) propBatLot.get(CadastrappConstants.PB_LOT_NUMERATEUR));

			if (logger.isDebugEnabled()) {
				logger.debug("Lot : " + lot);
			}

			// add bundle to list
			proprieteBatieLot.add(lot);
		}
		return proprieteBatieLot;

	}
}
