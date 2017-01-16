package org.georchestra.cadastrapp.service.pdf;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import javax.ws.rs.core.HttpHeaders;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.georchestra.cadastrapp.model.pdf.CompteCommunal;
import org.georchestra.cadastrapp.model.pdf.Imposition;
import org.georchestra.cadastrapp.model.pdf.ImpositionNonBatie;
import org.georchestra.cadastrapp.model.pdf.Lot;
import org.georchestra.cadastrapp.model.pdf.Proprietaire;
import org.georchestra.cadastrapp.model.pdf.ProprieteBatie;
import org.georchestra.cadastrapp.model.pdf.ProprieteNonBatie;
import org.georchestra.cadastrapp.model.pdf.RelevePropriete;
import org.georchestra.cadastrapp.model.pdf.Exoneration;
import org.georchestra.cadastrapp.service.CadController;
import org.georchestra.cadastrapp.service.constants.CadastrappConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public final class ReleveProprieteHelper extends CadController{

	final static Logger logger = LoggerFactory.getLogger(ReleveProprieteHelper.class);

	final String xslTemplate = "xsl/relevePropriete.xsl";
	final String xslTemplateMinimal = "xsl/releveProprieteMinimal.xsl";
	final String xslTemplateError = "xsl/releveProprieteError.xsl";

	/**
	 * 
	 * @param idComptesCommunaux
	 * @param headers
	 * @return
	 */
	public RelevePropriete getReleveProprieteInformation(List<String> idComptesCommunaux, HttpHeaders headers, String idParcelle) {

		RelevePropriete relevePropriete = new RelevePropriete();

		logger.debug("Get information to fill releve propriete ");

		final String dateValiditeDonneesMajic = CadastrappPlaceHolder.getProperty("pdf.dateValiditeDonneesMajic");
		final String organisme = CadastrappPlaceHolder.getProperty("pdf.organisme");

		// Information d'entête
		relevePropriete.setAnneMiseAJour(dateValiditeDonneesMajic);
		relevePropriete.setService(organisme);

		List<CompteCommunal> comptesCommunaux = new ArrayList<CompteCommunal>();

		if(idComptesCommunaux!=null && !idComptesCommunaux.isEmpty()){
			// Pour chaque compte communal
			for (String idCompteCommunal : idComptesCommunaux) {

				List<Object> queryParams = new ArrayList<Object>();
				queryParams.add(idCompteCommunal);
				logger.debug("CompteCommunal  : " + idCompteCommunal);

				CompteCommunal compteCommunal = new CompteCommunal();
				compteCommunal.setCompteCommunal(idCompteCommunal);

				// Select parcelle information to get entete information
				StringBuilder queryBuilder = new StringBuilder();

				queryBuilder.append("select c.cgocommune, c.libcom from ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".parcelle p, ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".commune c, ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".proprietaire_parcelle proparc ");
				queryBuilder.append("where proparc.comptecommunal = ? ");
				if(idParcelle != null && !idParcelle.isEmpty()){
					queryBuilder.append("and p.parcelle = ? ");
					queryParams.add(idParcelle);
				}else{
					queryBuilder.append("and p.parcelle = proparc.parcelle ");
				}
				
				queryBuilder.append("and p.cgocommune = c.cgocommune");

				logger.debug("Get town information " );
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				List<Map<String, Object>> rows = jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
				
				// Can be a coproprietaire
				if(rows.isEmpty()){
					rows = new ArrayList<Map<String, Object>>();
					
					// Select parcelle information to get entete information
					queryBuilder = new StringBuilder();

					queryBuilder.append("select c.cgocommune, c.libcom from ");
					queryBuilder.append(databaseSchema);
					queryBuilder.append(".parcelle p, ");
					queryBuilder.append(databaseSchema);
					queryBuilder.append(".commune c, ");
					queryBuilder.append(databaseSchema);
					queryBuilder.append(".co_propriete_parcelle proparc ");
					queryBuilder.append("where proparc.comptecommunal = ? ");
					if(idParcelle != null && !idParcelle.isEmpty()){
						queryBuilder.append("and proparc.parcelle = ? ");
					}else{
						queryBuilder.append("and p.parcelle = proparc.parcelle ");
					}
					queryBuilder.append("and p.cgocommune = c.cgocommune");

					logger.debug("Get town information " );
					rows = jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
				}

				// If result found
				if (!rows.isEmpty()){
					for (Map<?, ?> row : rows) {
						compteCommunal.setLibelleCommune((String) row.get("libcom"));

						String cgoCommune = (String) row.get("cgocommune");
						if (cgoCommune != null && cgoCommune.length() > 4) {
							compteCommunal.setCodeCommune(cgoCommune.substring(3));
							compteCommunal.setCodeDepartement(cgoCommune.substring(0, 3));
						}
					}

					// Display information only if at least CNIL level 1 or 2
					if (getUserCNILLevel(headers) > 0) {

						// Information sur les proprietaires
						List<Proprietaire> proprietaires = new ArrayList<Proprietaire>();

						StringBuilder queryBuilderProprietaire = new StringBuilder();
						queryBuilderProprietaire.append("select prop.comptecommunal, prop.dnulp, prop.ccodem_lib, prop.dldnss, prop.jdatnss,  prop.ccodro_lib, app_nom_usage as nom, app_nom_naissance as nom_naissance, COALESCE(prop.dlign3, '')||' '||COALESCE(prop.dlign4,'')||' '||COALESCE(prop.dlign5,'')||' '||COALESCE(prop.dlign6,'') as adresse ");
						queryBuilderProprietaire.append("from ");
						queryBuilderProprietaire.append(databaseSchema);
						queryBuilderProprietaire.append(".proprietaire prop ");
						queryBuilderProprietaire.append("where prop.comptecommunal = ? ");
						queryBuilderProprietaire.append(addAuthorizationFiltering(headers));
						queryBuilderProprietaire.append("order by prop.dnulp ASC ");

						logger.debug("Get owners information " );
						List<Map<String, Object>> proprietairesResult = jdbcTemplate.queryForList(queryBuilderProprietaire.toString(), idCompteCommunal);

						for (Map<String, Object> prop : proprietairesResult) {

							if(logger.isDebugEnabled()){
								logger.debug("Get owner information name : " + (String) prop.get("nom") );
							}

							Proprietaire proprietaire = new Proprietaire();
							proprietaire.setNom((String) prop.get("nom"));
							proprietaire.setNomNaissance((String) prop.get("nom_naissance"));
							proprietaire.setAdresse((String) prop.get("adresse"));
							proprietaire.setCodeDeDemenbrement((String) prop.get("ccodem_lib"));
							proprietaire.setDateNaissance((String) prop.get("jdatnss"));
							proprietaire.setLieuNaissance((String) prop.get("dldnss"));
							proprietaire.setDroitReel((String) prop.get("ccodro_lib"));

							proprietaires.add(proprietaire);
						}
						// Ajout la liste des proprietaires
						compteCommunal.setProprietaires(proprietaires);

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

						queryBuilderProprieteBatie.append("select distinct id_local, ccopre, ccosec, dnupla, COALESCE(natvoi,'')||' '||COALESCE(dvoilib,'') as voie, ccoriv, dnubat, descr, dniv, dpor, invar, ccoaff, ccoeva, cconlc, dcapec, ccolloc, gnextl, jandeb, janimp, gtauom, pexb, rcexba2, rcbaia_com, rcbaia_dep, rcbaia_gp, revcad, jdatat, parcelle ");
						queryBuilderProprieteBatie.append("from ");
						queryBuilderProprieteBatie.append(databaseSchema);
						queryBuilderProprieteBatie.append(".proprietebatie pb ");
						queryBuilderProprieteBatie.append(" where pb.comptecommunal = ? ORDER BY ccosec, dnupla");
						
						// Add map of invar to maker sure not to add two times taxable income
						List<String> invarTICount = new ArrayList<String>();

						logger.debug("Get developed property information " );
						List<Map<String, Object>> proprietesBatiesResult = jdbcTemplate.queryForList(queryBuilderProprieteBatie.toString(), idCompteCommunal);

						for (Map<String, Object> propBat : proprietesBatiesResult) {
							if( idParcelle == null || idParcelle.isEmpty() || idParcelle.equals((String)propBat.get(CadastrappConstants.PARC_ID)) ) {
								ProprieteBatie proprieteBatie = new ProprieteBatie();

								if(logger.isDebugEnabled()){
									logger.debug("Get developed property invar : " + (String) propBat.get(CadastrappConstants.PB_NUM_INVARIANT) );
								}

								String proprieteId = (String) propBat.get(CadastrappConstants.PB_NUM_INVARIANT);//N° invar
								proprieteBatie.setInvar(proprieteId);
								
								// TODO try changing this, 
								// making a request on each loop might not the best solution, but it can have more than one lot for one invar
								// Make sure an index exist on id_local
								String idLocal = (String) propBat.get(CadastrappConstants.PB_ID_LOCAL); 
								
								StringBuilder queryBuilderLots = new StringBuilder();
								queryBuilderLots.append("select dnulot, dnumql, ddenql ");
								queryBuilderLots.append("from ");
								queryBuilderLots.append(databaseSchema);
								queryBuilderLots.append(".lot l ");
								queryBuilderLots.append(" where l.id_local = ? ");
								List<Map<String, Object>> proprietesBatiesLots = jdbcTemplate.queryForList(queryBuilderLots.toString(), idLocal);
								
								List<Lot> proprieteBatieLot = new ArrayList<Lot>();
								for (Map<String, Object> propBatLot : proprietesBatiesLots) {
									// Create lot and add information
									Lot lot = new Lot();
									lot.setLotId((String) propBatLot.get(CadastrappConstants.PB_LOT_ID));
									lot.setDenominateur((String) propBatLot.get(CadastrappConstants.PB_LOT_DENOMINATEUR)); 
									lot.setNumerateur((String) propBatLot.get(CadastrappConstants.PB_LOT_NUMERATEUR));
									
									if(logger.isDebugEnabled()){
										logger.debug("Lot : " + lot );
									}
									
									// add lot to list
									proprieteBatieLot.add(lot);
								}
								// Add lot to list
								proprieteBatie.setLots(proprieteBatieLot);
							
                proprieteBatie.setCcoaff((String) propBat.get(CadastrappConstants.PB_AFFECTATION_PEV)); 																	//AF
                proprieteBatie.setCcoeva((String) propBat.get(CadastrappConstants.PB_CODE_EVAL)); 																			//M EVAL
                proprieteBatie.setCconlc((String) propBat.get(CadastrappConstants.PB_NATURE_LOCAL)); 																		//NAT LOC
                proprieteBatie.setCcopre((String) propBat.get(CadastrappConstants.PREFIX_SECTION)); 																		//N°SETCION part1
                proprieteBatie.setCcoriv((String) propBat.get(CadastrappConstants.CODE_RIVOLI_VOIE)); 																	//code rivoli
                proprieteBatie.setCcosec((String) propBat.get(CadastrappConstants.LETTRE_SECTION)); 																		//N°SETCION part2
                proprieteBatie.setDcapec((String) propBat.get(CadastrappConstants.PB_CATEGORIE));																			//Cat (PAS DANS LE REQUETTE)
                proprieteBatie.setDescr((String) propBat.get(CadastrappConstants.PB_NUM_ENTREE)); 																			//Ent
                proprieteBatie.setDniv((String) propBat.get(CadastrappConstants.PB_NIV_ETAGE)); 																			//Niv
                proprieteBatie.setDnubat((String) propBat.get(CadastrappConstants.PB_LETTRE_BAT));																			//Bat
                proprieteBatie.setDnupla((String) propBat.get(CadastrappConstants.NUM_PLAN));																			//N° plan
                proprieteBatie.setDpor((String) propBat.get(CadastrappConstants.PB_NUM_PORTE_LOCAL));																		//N° porte
                proprieteBatie.setRevcad(propBat.get(CadastrappConstants.PB_VAL_LOCAT_TOTAL) == null ? "":propBat.get(CadastrappConstants.PB_VAL_LOCAT_TOTAL).toString());	//Revenu cadastral
                proprieteBatie.setDvoilib((String) propBat.get(CadastrappConstants.ADRESSE)); 																			//Adresse
                proprieteBatie.setGtauom(propBat.get(CadastrappConstants.PB_MONTANT_TIEOM) == null ? "":propBat.get(CadastrappConstants.PB_MONTANT_TIEOM).toString()); 	//tx OM

                if(CadastrappConstants.MUTATION  != null){
                  // Date is store in database like jj/mm/yyyy
                  String[] parts = propBat.get(CadastrappConstants.MUTATION).toString().split("/");
                  if(parts != null && parts.length>1){
                    proprieteBatie.setJdatat(parts[2]);
                  }
                }

                List<Exoneration> proprieteBatieExonerations = new ArrayList<Exoneration>();
                Exoneration proprieteBatieExoneration = new Exoneration();

                proprieteBatieExoneration.setCcolloc((String) propBat.get(CadastrappConstants.PB_CODE_COLL_EXO));																		//COLL
                proprieteBatieExoneration.setGnextl((String) propBat.get(CadastrappConstants.PB_NATURE_EXO));																			//Nat Exo
                proprieteBatieExoneration.setJandeb((String) propBat.get(CadastrappConstants.PB_ANNEE_DEB_EXO));																		//An Deb
                proprieteBatieExoneration.setJanimp((String) propBat.get(CadastrappConstants.PB_ANNEE_RETOUR_IMPOSITION)); 															//An Ret
                proprieteBatieExoneration.setFcexn(propBat.get(CadastrappConstants.PB_FRACTION_EXO) == null ? "":propBat.get(CadastrappConstants.PB_FRACTION_EXO).toString());		//Fraction Exo
                proprieteBatieExoneration.setPexb(propBat.get(CadastrappConstants.PB_TAUX_EXO_ACCORDEE) == null ? "":propBat.get(CadastrappConstants.PB_TAUX_EXO_ACCORDEE).toString());//Exo

                proprieteBatieExonerations.add(proprieteBatieExoneration);
                proprieteBatie.setExonerations(proprieteBatieExonerations);

                // Add exoneration depending on type
                String exoneration = (String) propBat.get(CadastrappConstants.PB_CODE_COLL_EXO);
                if (CadastrappConstants.CODE_COLL_EXO_TC.equals(exoneration)) {
                  pbCommuneRevenuExonere = pbCommuneRevenuExonere + ((BigDecimal)propBat.get(CadastrappConstants.PB_FRACTION_EXO)).floatValue();
                  pbDepartementRevenuExonere = pbDepartementRevenuExonere + ((BigDecimal)propBat.get(CadastrappConstants.PB_FRACTION_EXO)).floatValue();
                  pbGroupementCommuneRevenuExonere = pbGroupementCommuneRevenuExonere + ((BigDecimal)propBat.get(CadastrappConstants.PB_FRACTION_EXO)).floatValue();
                } else if ( CadastrappConstants.CODE_COLL_EXO_C.equals(exoneration)) {
                  pbCommuneRevenuExonere = pbCommuneRevenuExonere + ((BigDecimal)propBat.get(CadastrappConstants.PB_FRACTION_EXO)).floatValue();
                } else if ( CadastrappConstants.CODE_COLL_EXO_GC.equals(exoneration)) {
                  pbGroupementCommuneRevenuExonere = pbGroupementCommuneRevenuExonere + ((BigDecimal)propBat.get(CadastrappConstants.PB_FRACTION_EXO)).floatValue();
                } else if (CadastrappConstants.CODE_COLL_EXO_D.equals(exoneration)) {
                  pbDepartementRevenuExonere = pbDepartementRevenuExonere + ((BigDecimal)propBat.get(CadastrappConstants.PB_FRACTION_EXO)).floatValue();
                } else if (CadastrappConstants.CODE_COLL_EXO_A.equals(exoneration)) {
                  logger.debug("Exoneration on additional taxes");
                }

								// Count only one taxable income for one invar
								if (!invarTICount.contains(proprieteId)){
									invarTICount.add(proprieteId);
									pbRevenuImposable = pbRevenuImposable + (propBat.get(CadastrappConstants.PB_VAL_LOCAT_TOTAL) == null ? 0 :((BigDecimal)propBat.get(CadastrappConstants.PB_VAL_LOCAT_TOTAL)).floatValue());			
									pbCommuneRevenuImposable = pbCommuneRevenuImposable + (propBat.get("rcbaia_com") == null ? 0 :((BigDecimal)propBat.get("rcbaia_com")).floatValue());
									pbDepartementRevenuImposable = pbDepartementRevenuImposable + (propBat.get("rcbaia_dep") == null ? 0 :((BigDecimal)propBat.get("rcbaia_dep")).floatValue());
									pbGroupementCommuneRevenuImposable = pbGroupementCommuneRevenuImposable + (propBat.get("rcbaia_gp") == null ? 0 :((BigDecimal)propBat.get("rcbaia_gp")).floatValue());
								}
															
								proprietesBaties.add(proprieteBatie);
							}
						}
						// ajout la liste des propriete baties uniquement si il y en a
						// au moins une
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

							compteCommunal.setImpositionBatie(impositionBatie);

							compteCommunal.setProprieteBaties(proprietesBaties);
						}

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

						queryBuilderProprieteNonBatie.append("select distinct pnb.id_local, pnb.cgocommune, pnb.ccopre, pnb.ccosec, pnb.dnupla, COALESCE(pnb.natvoi,'')||' '||COALESCE(pnb.dvoilib,'') as voie, pnb.ccoriv, pnb.dparpi, pnb.ccostn, pnb.ccosub, pnb.cgrnum, pnb.dsgrpf, pnb.dclssf, pnb.cnatsp, pnb.dcntsf, pnb.drcsuba, pnb.dnulot, pnb.dreflf, pnb.majposa, pnb.bisufad_com, pnb.bisufad_dep, pnb.bisufad_gp, pnb.gparnf, pnb.jdatat, pnb.parcelle ");
						queryBuilderProprieteNonBatie.append("from ");
						queryBuilderProprieteNonBatie.append(databaseSchema);
						queryBuilderProprieteNonBatie.append(".proprietenonbatie pnb ");
						queryBuilderProprieteNonBatie.append(" where pnb.comptecommunal = ? ORDER BY ccosec, dnupla");

						logger.debug("Get undeveloped property information " );
						List<Map<String, Object>> proprietesNonBatiesResult = jdbcTemplate.queryForList(queryBuilderProprieteNonBatie.toString(), idCompteCommunal);

						for (Map<String, Object> propNonBat : proprietesNonBatiesResult) {
							if( idParcelle == null || idParcelle.isEmpty() || idParcelle.equals((String)propNonBat.get(CadastrappConstants.PARC_ID)) ) {
								String proprieteNbId = (String) propNonBat.get("id_local");

								if(logger.isDebugEnabled()){
									logger.debug("Get undeveloped property id : " + proprieteNbId );
								}
								
								// Get exonerations for this PNB
								StringBuilder queryBuilderProprieteNonBatieExo = new StringBuilder();
								queryBuilderProprieteNonBatieExo.append("SELECT pnbsufexo.id_local, pnbsufexo.ccolloc, pnbsufexo.gnexts, pnbsufexo.jfinex, pnbsufexo.rcexnba, pnbsufexo.pexn ");
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
									proprieteNonBatieExoneration.setFcexn(propNonBatExo.get(CadastrappConstants.PNB_FRACTION_RC_EXO) == null ?"":propNonBatExo.get(CadastrappConstants.PNB_FRACTION_RC_EXO).toString());
									proprieteNonBatieExoneration.setPexb(propNonBatExo.get(CadastrappConstants.PNB_POURCENTAGE_EXO) == null ?"":propNonBatExo.get(CadastrappConstants.PNB_POURCENTAGE_EXO).toString());//à voir si besoin de divider par 100
									
									proprieteNonBatieExonerations.add(proprieteNonBatieExoneration);
									
									// "TC";"toutes les collectivités"
									// "R";"Région => l'exonération porte sur la seule part régionale"
									// "GC";"Groupement de communes"
									// "D";"Département => l'exonération porte sur la seule part départementale"
									// "C";"Commune => l'exonération porte sur la seule part communale"
									// "A";"l'exonération porte sur la taxe additionnelle"
									String exonerationType = (String) propNonBatExo.get(CadastrappConstants.PNB_CODE_COLL_EXO);
									float exonerationValue = (propNonBatExo.get("rcexnba") == null ? 0 :((BigDecimal)propNonBatExo.get("rcexnba")).floatValue());
									if (CadastrappConstants.CODE_COLL_EXO_TC.equals(exonerationType)) {
										pnbCommuneRevenuExonere = pnbCommuneRevenuExonere + exonerationValue;
										pnbDepartementRevenuExonere = pnbDepartementRevenuExonere + exonerationValue;
										pnbGroupementCommuneRevenuExonere = pnbGroupementCommuneRevenuExonere + exonerationValue;
									} else if ( CadastrappConstants.CODE_COLL_EXO_C.equals(exonerationType)) {
										pnbCommuneRevenuExonere = pnbCommuneRevenuExonere + exonerationValue;
									} else if ( CadastrappConstants.CODE_COLL_EXO_GC.equals(exonerationType)) {
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

                if(CadastrappConstants.MUTATION  != null){
                  // Date is store in database like jj/mm/yyyy
                  String[] parts = propNonBat.get(CadastrappConstants.MUTATION).toString().split("/");
                  if(parts != null && parts.length>1){
                    proprieteNonBatie.setJdatat(parts[2]);
                  }
                }

                int surface = (Integer) propNonBat.get(CadastrappConstants.PNB_CONTENANCE_CA) == null ? 0 : (Integer) propNonBat.get(CadastrappConstants.PNB_CONTENANCE_CA);
                proprieteNonBatie.setDcntsf(surface);

                float revenu = (propNonBat.get(CadastrappConstants.PNB_REVENU_CADASTRAL) == null ? 0 :((BigDecimal) propNonBat.get(CadastrappConstants.PNB_REVENU_CADASTRAL)).floatValue());
                proprieteNonBatie.setDrcsuba(revenu);

                pnbRevenuImposable = pnbRevenuImposable + revenu;

                pnbCommuneRevenuImposable = pnbCommuneRevenuImposable + (propNonBat.get("bisufad_com") == null ? 0 :((BigDecimal)propNonBat.get("bisufad_com")).floatValue());
                pnbDepartementRevenuImposable = pnbDepartementRevenuImposable +  (propNonBat.get("bisufad_dep") == null ? 0 :((BigDecimal)propNonBat.get("bisufad_dep")).floatValue());
                pnbGroupementCommuneRevenuImposable = pnbGroupementCommuneRevenuImposable + (propNonBat.get("bisufad_gp") == null ? 0 :((BigDecimal)propNonBat.get("bisufad_gp")).floatValue());

                pnbMajorationTerrain = pnbMajorationTerrain + (propNonBat.get("majposa") == null ? 0 :((BigDecimal)propNonBat.get("majposa")).floatValue());

                pnbSurface = pnbSurface + surface; 

                proprietesNonBaties.add(proprieteNonBatie);
						  }
            }

						// ajout la liste des propriete non baties uniquement si il y en
						// a au moins une
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

							compteCommunal.setImpositionNonBatie(impositionNonBatie);

							compteCommunal.setProprieteNonBaties(proprietesNonBaties);
						}

						// Ajout du compte communal à la liste
						comptesCommunaux.add(compteCommunal);
					}

				}else{
					relevePropriete.setEmpty(true);
				}
			}
			relevePropriete.setComptesCommunaux(comptesCommunaux);

		}else {
			relevePropriete.setEmpty(true);
		}

		return relevePropriete;
	}


	/**
	 * Generate pdf file using FOP 
	 * @param isNoData 
	 * @param bp object that contain all information about Plots 
	 * 
	 * @return File pdfResult
	 */
	public File generatePDF(RelevePropriete rp, boolean isMinimal, boolean isNoData){

		String tempFolder = CadastrappPlaceHolder.getProperty("tempFolder");
		String template = null;

		if(isNoData){
			template = xslTemplateError;
		}else{
			template = (isMinimal)? xslTemplateMinimal:xslTemplate;
		}

		// Pdf temporary filename using tmp folder and timestamp
		final String pdfTmpFileName = tempFolder+File.separator+"RP"+new Date().getTime();

		// Construct a FopFactory (reuse if you plan to render multiple documents!)
		FopFactory fopFactory = FopFactory.newInstance();
		InputStream xsl = Thread.currentThread().getContextClassLoader().getResourceAsStream(template);

		// Setup XSLT
		TransformerFactory factory = TransformerFactory.newInstance();

		Transformer transformerXSLT;
		Transformer transformerPDF;
		JAXBContext jaxbContext;
		Marshaller jaxbMarshaller;
		File pdfResult;
		OutputStream out;
		Fop fop;

		// Create Empyt PDF File will be erase after
		pdfResult = new File(pdfTmpFileName+".pdf");
		pdfResult.deleteOnExit();

		try {
			transformerXSLT = factory.newTransformer(new StreamSource(xsl));
			transformerPDF = factory.newTransformer();

			out = new BufferedOutputStream(new FileOutputStream(pdfResult));

			fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

			jaxbContext = JAXBContext.newInstance(RelevePropriete.class);
			jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			File xmlfile = null;
			File foFile = null;
			OutputStream foOutPutStream = null;

			try {
				// Xml file will be deleted on JVM exit
				xmlfile = new File(pdfTmpFileName+".xml");
				xmlfile.deleteOnExit();

				jaxbMarshaller.marshal(rp, xmlfile);

				// log on console marshaller only if debug log is one
				if (logger.isDebugEnabled()) {
					jaxbMarshaller.marshal(rp, System.out);
				}

				// FO file will be deleted on JVM exit
				// XML TO FO
				foFile = new File(pdfTmpFileName+".fo");
				foFile.deleteOnExit();

				foOutPutStream = new java.io.FileOutputStream(foFile);

				// Setup input for XSLT transformation
				Source srcXml = new StreamSource(xmlfile);
				Result resFo = new StreamResult(foOutPutStream);

				// Start XSLT transformation and FOP processing
				transformerXSLT.transform(srcXml, resFo);
				foOutPutStream.close();

				// FO TO PDF
				Source src = new StreamSource(foFile);
				Result res = new SAXResult(fop.getDefaultHandler());

				// Start PDF transformation and FOP processing
				transformerPDF.transform(src, res);

				out.close();

			} catch (JAXBException jaxbException) {
				logger.warn("Error during converting object to xml : " + jaxbException);
			} catch (TransformerException transformerException) {

			} catch (FileNotFoundException fileNotFoundException) {
				logger.warn("Error when using temporary files : " + fileNotFoundException);
			} finally {
				if (out != null) {
					// Clean-up
					out.close();
				}
				if(xmlfile != null){
					xmlfile.delete();
				}
				if(foFile != null){
					foFile.delete();
				}
				if (foOutPutStream != null){
					foOutPutStream.close();
				}
			}

		} catch (TransformerConfigurationException e) {
			logger.warn("Error when initialize transformers : " + e);
		} catch (IOException ioException) {
			logger.warn("Error when creating output file : " + ioException);
		} catch (FOPException fopException) {
			logger.warn("Error when creationg FOP file type : " + fopException);
		} catch (JAXBException jaxbException) {
			logger.warn("Error creating Marsharller : " + jaxbException);
		}finally{
			// Could not delete pdfResult here because it's still used by cxf
		}

		return pdfResult;
	}

	/**
	 * get owners by id parcelle
	 * 
	 * @param parcelle
	 * @return list of owners
	 */
	public List<Map<String, Object>> getProprietaireByParcelles(String parcelle) {
		List<Map<String, Object>> cc = null;
		StringBuilder queryBuilder = new StringBuilder();

		if(parcelle != null){
			queryBuilder.append("select distinct ");
			queryBuilder.append("proparc.comptecommunal ");
			queryBuilder.append("from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".proprietaire_parcelle proparc ");
			queryBuilder.append("where proparc.parcelle = ?");

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			cc = jdbcTemplate.queryForList(queryBuilder.toString(), parcelle);
		}
		else{
			logger.info("Missing or empty input parameter");
		}
		return cc;
	}

	/**
	 * get parcelle by compte communal
	 * @param comptecommunal
	 * 
	 * @return list of parcelle
	 */
	public List<Map<String, Object>> getParcellesByProprietaire(String comptecommunal){

		List<Map<String, Object>> parcelles = null;
		StringBuilder queryBuilder = new StringBuilder();

		if(comptecommunal != null){
			queryBuilder.append("select distinct ");
			queryBuilder.append("proparc.parcelle ");
			queryBuilder.append("from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".proprietaire_parcelle proparc ");
			queryBuilder.append("where proparc.comptecommunal = ?");

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			parcelles = jdbcTemplate.queryForList(queryBuilder.toString(), comptecommunal);
		}
		else{
			logger.info("Missing or empty input parameter");
		}
		return parcelles;
	}


	/**
	 * get lots by parcelle
	 * 
	 * @param compteCommunal
	 * @param parcellaId
	 * @return list of lots
	 */
	public List<Map<String, Object>> getlotsByCcAndParcelle(String compteCommunal, String parcellaId) {
		List<Map<String, Object>> parcelles = null;
		StringBuilder queryBuilder = new StringBuilder();

		if(compteCommunal != null){
			queryBuilder.append("select distinct ");
			queryBuilder.append("proparc.lots ");
			queryBuilder.append("from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".co_propriete_parcelle proparc ");
			queryBuilder.append("where proparc.comptecommunal = ? and proparc.parcelle = ?");

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			parcelles = jdbcTemplate.queryForList(queryBuilder.toString(), compteCommunal, parcellaId);
		}
		else{
			logger.info("Missing or empty input parameter");
		}
		return parcelles;
	}

	/**
	 * get proprietaire by info parcelle
	 * 
	 * @param commune
	 * @param section TODO missing ccopre
	 * @param numero
	 * @return list of compte communal
	 */
	public List<Map<String, Object>> getProprietaireByInfoParcelle(String commune, String section, String numero) {

		List<Map<String, Object>> cc = null;
		StringBuilder queryBuilder = new StringBuilder();

		if(commune != null || section != null || numero != null){
			queryBuilder.append("select distinct ");
			queryBuilder.append("proparc.comptecommunal, p.parcelle ");
			queryBuilder.append("from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".proprietaire_parcelle proparc ");
			queryBuilder.append(", ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".parcelle p ");
			queryBuilder.append("where p.cgocommune = ? and p.ccosec = ? and p.dnupla = ? ");
			queryBuilder.append("and p.parcelle = proparc.parcelle");

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			cc = jdbcTemplate.queryForList(queryBuilder.toString(), commune,section,numero);
		}
		else{
			logger.info("Missing or empty input parameter");
		}
		return cc;

	}


	/**
	 * get owners by info owner
	 * 
	 * @param commune
	 * @param ownerName
	 * @return list of compte communal
	 */
	public List<Map<String, Object>> getProprietaireByInfoOwner(String commune, String ownerName) {
		List<Map<String, Object>> cc = null;
		StringBuilder queryBuilder = new StringBuilder();

		// if search by dnuproList or comptecommunal
		// directly search in view parcelle
		if(commune != null || ownerName !=null){
			queryBuilder.append("select distinct ");
			queryBuilder.append("p.comptecommunal ");
			queryBuilder.append("from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".proprietaire p");
			queryBuilder.append(" where p.cgocommune = ? and p.app_nom_usage = ? ");

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			cc = jdbcTemplate.queryForList(queryBuilder.toString(), commune,ownerName);
		}
		else{
			logger.info("Missing or empty input parameter");
		}
		return cc;

	}


	/**
	 *  get Owner and parcelle using input 
	 *   
	 * @param commune
	 * @param section
	 * @param numero
	 * @param proprietaire
	 * @return
	 */
	public List<Map<String, Object>> getProprietaireByInfoLot(String commune, String section, String numero, String proprietaire) {
		List<Map<String, Object>> cc = null;
		StringBuilder queryBuilder = new StringBuilder();

		// if search by dnuproList or comptecommunal
		// directly search in view parcelle
		if(commune != null || section != null || numero != null){
			queryBuilder.append("select distinct ");
			queryBuilder.append("proparc.comptecommunal, proparc.parcelle ");
			queryBuilder.append("from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".co_propriete_parcelle proparc ");
			queryBuilder.append(", ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".parcelle p ");
			queryBuilder.append(", ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".proprietaire pro ");
			queryBuilder.append("where p.cgocommune = ? and p.ccosec = ? and p.dnupla = ? ");
			queryBuilder.append("and p.parcelle = proparc.parcelle ");
			queryBuilder.append("and pro.comptecommunal = proparc.comptecommunal and pro.app_nom_usage = ? ");

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			cc = jdbcTemplate.queryForList(queryBuilder.toString(), commune,section,numero,proprietaire);
		}
		else{
			logger.info("Missing or empty input parameter");
		}
		return cc;

	}


	/**
	 * Create RP without parcelle id.
	 * 
	 * @param comptesCommunaux
	 * @param headers
	 * @return
	 */
	public RelevePropriete getReleveProprieteInformation(List<String> comptesCommunaux, HttpHeaders headers) {

		return getReleveProprieteInformation(comptesCommunaux,headers,null);
	}


}
