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
import org.georchestra.cadastrapp.helper.ProprieteHelper;
import org.georchestra.cadastrapp.model.pdf.CompteCommunal;
import org.georchestra.cadastrapp.model.pdf.Proprietaire;
import org.georchestra.cadastrapp.model.pdf.RelevePropriete;
import org.georchestra.cadastrapp.service.CadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public final class ReleveProprieteHelper extends CadController {

	static final Logger logger = LoggerFactory.getLogger(ReleveProprieteHelper.class);
	
	@Autowired
	ProprieteHelper proprieteHelper;

	static final String XSL_TEMPLATE = "xsl/relevePropriete.xsl";
	static final String XSL_TEMPLATE_MINIMAL = "xsl/releveProprieteMinimal.xsl";
	static final String XSL_TEMPLATE_ERROR = "xsl/releveProprieteError.xsl";

	/**
	 * Get propertie information using given imput from database
	 * 
	 * @param idComptesCommunaux List<String> composed with comptecommunalid
	 * @param headers HttpHeaders used to verify user privilege
	 * @param idParcelle String plot id
	 * 
	 * @return RelevePropriete fill with database information
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

		if (idComptesCommunaux != null && !idComptesCommunaux.isEmpty()) {
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
				if (idParcelle != null && !idParcelle.isEmpty()) {
					queryBuilder.append("and p.parcelle = ? ");
					queryParams.add(idParcelle);
				} else {
					queryBuilder.append("and p.parcelle = proparc.parcelle ");
				}

				queryBuilder.append("and p.cgocommune = c.cgocommune");

				logger.debug("Get town information ");
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				List<Map<String, Object>> rows = jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());

				// Can be a coproprietaire
				if (rows.isEmpty()) {
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
					if (idParcelle != null && !idParcelle.isEmpty()) {
						queryBuilder.append("and p.parcelle = ? ");
					} else {
						queryBuilder.append("and p.parcelle = proparc.parcelle ");
					}
					queryBuilder.append("and p.cgocommune = c.cgocommune");

					logger.debug("Get town information ");
					rows = jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
				}

				// If result found
				if (!rows.isEmpty()) {
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

						logger.debug("Get owners information ");
						List<Map<String, Object>> proprietairesResult = jdbcTemplate.queryForList(queryBuilderProprietaire.toString(), idCompteCommunal);

						for (Map<String, Object> prop : proprietairesResult) {

							if (logger.isDebugEnabled()) {
								logger.debug("Get owner information name : " + (String) prop.get("nom"));
							}

							Proprietaire proprietaire = new Proprietaire();
							proprietaire.setNom((String) prop.get("nom"));
							proprietaire.setNomNaissance((String) prop.get("nom_naissance"));
							proprietaire.setCompteCommunal((String) prop.get("comptecommunal"));
							proprietaire.setAdresse((String) prop.get("adresse"));
							proprietaire.setCodeDeDemembrement((String) prop.get("ccodem_lib"));
							proprietaire.setDateNaissance((String) prop.get("jdatnss"));
							proprietaire.setLieuNaissance((String) prop.get("dldnss"));
							proprietaire.setDroitReel((String) prop.get("ccodro_lib"));

							proprietaires.add(proprietaire);
						}
						// Ajout la liste des proprietaires
						compteCommunal.setProprietaires(proprietaires);
				
						compteCommunal.setProprietesBaties(proprieteHelper.getProprieteBatieInformation(idCompteCommunal, idParcelle));

						compteCommunal.setProprietesNonBaties(proprieteHelper.getProprieteNonBatieInformation(idCompteCommunal, idParcelle));

					
						// Ajout du compte communal à la liste
						comptesCommunaux.add(compteCommunal);
					}

				} else {
					relevePropriete.setEmpty(true);
				}
			}
			relevePropriete.setComptesCommunaux(comptesCommunaux);

		} else {
			relevePropriete.setEmpty(true);
		}

		return relevePropriete;
	}

	/**
	 * Generate pdf file using FOP
	 * 
	 * @param isNoData
	 * @param bp
	 *            object that contain all information about Plots
	 * 
	 * @return File pdfResult
	 */
	public File generatePDF(RelevePropriete rp, boolean isMinimal, boolean isNoData) {

		String tempFolder = CadastrappPlaceHolder.getProperty("tempFolder");
		String template = null;

		if (isNoData) {
			template = XSL_TEMPLATE_ERROR;
		} else {
			template = isMinimal ? XSL_TEMPLATE_MINIMAL : XSL_TEMPLATE;
		}

		// Pdf temporary filename using tmp folder and timestamp
		final String pdfTmpFileName = tempFolder + File.separator + "RP" + new Date().getTime();

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
		pdfResult = new File(pdfTmpFileName + ".pdf");
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
				xmlfile = new File(pdfTmpFileName + ".xml");
				xmlfile.deleteOnExit();

				jaxbMarshaller.marshal(rp, xmlfile);

				// log on console marshaller only if debug log is one
				if (logger.isDebugEnabled()) {
					jaxbMarshaller.marshal(rp, System.out);
				}

				// FO file will be deleted on JVM exit
				// XML TO FO
				foFile = new File(pdfTmpFileName + ".fo");
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
				logger.warn("Error during converting object to xml", jaxbException);
			} catch (TransformerException transformerException) {
				logger.warn("Error during converting xml to xsl", transformerException);

			} catch (FileNotFoundException fileNotFoundException) {
				logger.warn("Error when using temporary files", fileNotFoundException);
			} finally {
				if (out != null) {
					// Clean-up
					out.close();
				}
				if (xmlfile != null) {
					xmlfile.delete();
				}
				if (foFile != null) {
					foFile.delete();
				}
				if (foOutPutStream != null) {
					foOutPutStream.close();
				}
			}

		} catch (TransformerConfigurationException e) {
			logger.warn("Error when initialize transformers", e);
		} catch (IOException ioException) {
			logger.warn("Error when creating output file", ioException);
		} catch (FOPException fopException) {
			logger.warn("Error when creationg FOP file type", fopException);
		} catch (JAXBException jaxbException) {
			logger.warn("Error creating Marsharller", jaxbException);
		} finally {
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

		if (parcelle != null) {
			queryBuilder.append("select distinct ");
			queryBuilder.append("proparc.comptecommunal ");
			queryBuilder.append("from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".proprietaire_parcelle proparc ");
			queryBuilder.append("where proparc.parcelle = ?");

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			cc = jdbcTemplate.queryForList(queryBuilder.toString(), parcelle);
		} else {
			logger.info("Missing or empty input parameter");
		}
		return cc;
	}

	/**
	 * get parcelle by compte communal
	 * 
	 * @param comptecommunal
	 * 
	 * @return list of parcelle
	 */
	public List<Map<String, Object>> getParcellesByProprietaire(String comptecommunal) {

		List<Map<String, Object>> parcelles = null;
		StringBuilder queryBuilder = new StringBuilder();

		if (comptecommunal != null) {
			queryBuilder.append("select distinct ");
			queryBuilder.append("proparc.parcelle ");
			queryBuilder.append("from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".proprietaire_parcelle proparc ");
			queryBuilder.append("where proparc.comptecommunal = ?");

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			parcelles = jdbcTemplate.queryForList(queryBuilder.toString(), comptecommunal);
		} else {
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

		if (compteCommunal != null) {
			queryBuilder.append("select distinct ");
			queryBuilder.append("proparc.lots ");
			queryBuilder.append("from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".co_propriete_parcelle proparc ");
			queryBuilder.append("where proparc.comptecommunal = ? and proparc.parcelle = ?");

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			parcelles = jdbcTemplate.queryForList(queryBuilder.toString(), compteCommunal, parcellaId);
		} else {
			logger.info("Missing or empty input parameter");
		}
		return parcelles;
	}

	/**
	 * get proprietaire by info parcelle
	 * 
	 * @param commune
	 * @param section
	 *            Section is ccopre+ccosec
	 * @param numero
	 * @return list of compte communal
	 */
	public List<Map<String, Object>> getProprietaireByInfoParcelle(String commune, String section, String numero) {

		List<Map<String, Object>> cc = null;
		StringBuilder queryBuilder = new StringBuilder();

		if (commune != null || section != null || numero != null) {
			queryBuilder.append("select distinct ");
			queryBuilder.append("proparc.comptecommunal, p.parcelle ");
			queryBuilder.append("from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".proprietaire_parcelle proparc ");
			queryBuilder.append(", ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".parcelle p ");
			queryBuilder.append("where p.cgocommune = ? and p.ccopre||p.ccosec = ? and p.dnupla = ? ");
			queryBuilder.append("and p.parcelle = proparc.parcelle");

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			cc = jdbcTemplate.queryForList(queryBuilder.toString(), commune, section, numero);
		} else {
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
		if (commune != null || ownerName != null) {
			queryBuilder.append("select distinct ");
			queryBuilder.append("p.comptecommunal ");
			queryBuilder.append("from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".proprietaire p");
			queryBuilder.append(" where p.cgocommune = ? and p.app_nom_usage = ? ");

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			cc = jdbcTemplate.queryForList(queryBuilder.toString(), commune, ownerName);
		} else {
			logger.info("Missing or empty input parameter");
		}
		return cc;

	}

	/**
	 * get Owner and parcelle using input
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
		if (commune != null || section != null || numero != null) {
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
			cc = jdbcTemplate.queryForList(queryBuilder.toString(), commune, section, numero, proprietaire);
		} else {
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

		return getReleveProprieteInformation(comptesCommunaux, headers, null);
	}

}
