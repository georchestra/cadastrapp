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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
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
import org.georchestra.cadastrapp.model.pdf.CompteCommunal;
import org.georchestra.cadastrapp.model.pdf.Imposition;
import org.georchestra.cadastrapp.model.pdf.ImpositionNonBatie;
import org.georchestra.cadastrapp.model.pdf.Proprietaire;
import org.georchestra.cadastrapp.model.pdf.ProprieteBatie;
import org.georchestra.cadastrapp.model.pdf.ProprieteNonBatie;
import org.georchestra.cadastrapp.model.pdf.RelevePropriete;
import org.georchestra.cadastrapp.service.CadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class ReleveProprieteController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(ReleveProprieteController.class);

	final String xslTemplate = "xsl/relevePropriete.xsl";

	/**
	 * Create a PDF using a list of comptecommunal
	 * 
	 * @param headers to verify CNIL level information
	 * @param compteCommunal List of ids proprietaires
	 * @return pdf
	 */
	@GET
	@Path("/createRelevePropriete")
	@Produces("application/pdf")
	public Response createRelevePDFPropriete(@Context HttpHeaders headers, @QueryParam("compteCommunal") final List<String> comptesCommunaux) {

		// Check if parcelle list is not empty
		if (comptesCommunaux != null && !comptesCommunaux.isEmpty()) {

			// Pdf temporary filename using tmp folder and timestamp
			final String pdfTmpFileName = tempFolder + File.separator + "RP" + new Date().getTime();

			// Construct a FopFactory (reuse if you plan to render multiple
			// documents!)
			FopFactory fopFactory = FopFactory.newInstance();
			InputStream xsl = Thread.currentThread().getContextClassLoader().getResourceAsStream(xslTemplate);

			// Setup XSLT
			TransformerFactory factory = TransformerFactory.newInstance();

			Transformer transformerXSLT;
			Transformer transformerPDF;
			JAXBContext jaxbContext;
			Marshaller jaxbMarshaller;
			File pdfResult;
			OutputStream out;
			Fop fop;

			try {
				transformerXSLT = factory.newTransformer(new StreamSource(xsl));
				transformerPDF = factory.newTransformer();

				// Create Empyt PDF File will be erase after
				pdfResult = new File(pdfTmpFileName + ".pdf");
				pdfResult.deleteOnExit();
				out = new BufferedOutputStream(new FileOutputStream(pdfResult));

				fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

				jaxbContext = JAXBContext.newInstance(RelevePropriete.class);
				jaxbMarshaller = jaxbContext.createMarshaller();

				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				// Get information about releve de propriete
				RelevePropriete relevePropriete = getReleveProprieteInformation(comptesCommunaux, headers);

				try {
					// Xml file will be deleted on JVM exit
					File xmlfile = new File(pdfTmpFileName + ".xml");
					xmlfile.deleteOnExit();

					jaxbMarshaller.marshal(relevePropriete, xmlfile);

					// log on console marshaller only if debug log is one
					if (logger.isDebugEnabled()) {
						jaxbMarshaller.marshal(relevePropriete, System.out);
					}

					// FO file will be deleted on JVM exit
					// XML TO FO
					File foFile = new File(pdfTmpFileName + ".fo");
					foFile.deleteOnExit();

					OutputStream foOutPutStream = new java.io.FileOutputStream(foFile);

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

					// Create response
					ResponseBuilder response = Response.ok((Object) pdfResult);
					response.header("Content-Disposition", "attachment; filename=" + pdfResult.getName());
					return response.build();

				} catch (TransformerException transformerException) {
					logger.error("Error during transformation : " + transformerException);
				} catch (FileNotFoundException fileNotFoundException) {
					logger.error("Error when using temporary files : " + fileNotFoundException);
				} finally {
					if (out != null) {
						// Clean-up
						out.close();
					}
				}
			} catch (JAXBException jaxbException) {
				logger.error("Error during converting object to xml : " + jaxbException);
			} catch (TransformerConfigurationException transformerConfigurationException) {
				logger.error("Error when initialize transformers : " + transformerConfigurationException);
			} catch (IOException ioException) {
				logger.error("Error when creating output file : " + ioException);
			} catch (FOPException fopException) {
				logger.error("Error when creationg FOP file type : " + fopException);
			}
		} else {
			logger.warn("Required parameter missing");
		}
		return null;
	}

	/**
	 * 
	 * @param idComptesCommunaux
	 * @param headers
	 * @return
	 */
	private RelevePropriete getReleveProprieteInformation(List<String> idComptesCommunaux, HttpHeaders headers) {

		RelevePropriete relevePropriete = new RelevePropriete();
		
		logger.debug("Get information to fill releve propriete ");

		// Information d'entête
		relevePropriete.setAnneMiseAJour(dateValiditeDonnees);
		relevePropriete.setService(organisme);

		List<CompteCommunal> comptesCommunaux = new ArrayList<CompteCommunal>();

		// Pour chaque compte communal
		for (String idCompteCommunal : idComptesCommunaux) {
			
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
			queryBuilder.append("and p.parcelle = proparc.parcelle ");
			queryBuilder.append("and p.cgocommune = c.cgocommune;");

			logger.debug("Get town information " );
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(queryBuilder.toString(), idCompteCommunal);

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
				queryBuilderProprietaire.append("select prop.comptecommunal, ccodem_lib, dldnss, jdatnss,  ccodro_lib, prop.ccoqua_lib||' '||prop.ddenom as nom, prop.dlign3||' '||prop.dlign4||' '||prop.dlign5||' '||prop.dlign6 as adresse ");
				queryBuilderProprietaire.append("from ");
				queryBuilderProprietaire.append(databaseSchema);
				queryBuilderProprietaire.append(".proprietaire prop ");
				queryBuilderProprietaire.append("where prop.comptecommunal = ?");
				queryBuilderProprietaire.append(addAuthorizationFiltering(headers));

				logger.debug("Get owners information " );
				List<Map<String, Object>> proprietairesResult = jdbcTemplate.queryForList(queryBuilderProprietaire.toString(), idCompteCommunal);

				for (Map<String, Object> prop : proprietairesResult) {
					
					if(logger.isDebugEnabled()){
						logger.debug("Get town information name : " + (String) prop.get("nom") );
					}
					
					Proprietaire proprietaire = new Proprietaire();
					proprietaire.setNom((String) prop.get("nom"));
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
				List<String> proprietesBIds = new ArrayList<String>();

				int pbCommuneRevenuExonere = 0;
				int pbCommuneRevenuImposable = 0;
				int pbDepartementRevenuExonere = 0;
				int pbDepartementRevenuImposable = 0;
				int pbRegionRevenuExonere = 0;
				int pbRegionRevenuImposable = 0;
				int pbRevenuImposable = 0;

				StringBuilder queryBuilderProprieteBatie = new StringBuilder();

				queryBuilderProprieteBatie.append("select distinct jdatat, ccopre, ccosec, dnupla, dnvoiri, dindic, natvoi||' '||dvoilib as voie, ccoriv, dnubat, descr, dniv, dpor, invar, ccoaff, ccoeva, ccolloc, gnextl, jandeb, janimp, fcexb, mvltieomx, dvldif2a, vlbaia, vlbaia_com, vlbaia_dep, vlbaia_reg ");
				queryBuilderProprieteBatie.append("from ");
				queryBuilderProprieteBatie.append(databaseSchema);
				queryBuilderProprieteBatie.append(".proprietebatie pb ");
				queryBuilderProprieteBatie.append(" where pb.comptecommunal = ? ORDER BY ccosec, dnupla");

				logger.debug("Get developed property information " );
				List<Map<String, Object>> proprietesBatiesResult = jdbcTemplate.queryForList(queryBuilderProprieteBatie.toString(), idCompteCommunal);

				for (Map<String, Object> propBat : proprietesBatiesResult) {
					ProprieteBatie proprieteBatie = new ProprieteBatie();
					
					if(logger.isDebugEnabled()){
						logger.debug("Get developed property invar : " + (String) propBat.get("invar") );
					}

					String proprieteId = (String) propBat.get("invar");
					proprieteBatie.setInvar(proprieteId);

					proprieteBatie.setCcoaff((String) propBat.get("ccoaff"));
					proprieteBatie.setCcoeav((String) propBat.get("ccoeav"));
					proprieteBatie.setCcolloc((String) propBat.get("ccolloc"));
					proprieteBatie.setCconlc((String) propBat.get("cconlc"));
					proprieteBatie.setCcopre((String) propBat.get("ccopre"));
					proprieteBatie.setCcoriv((String) propBat.get("ccoriv"));
					proprieteBatie.setCcosec((String) propBat.get("ccosec"));
					proprieteBatie.setDcapec((String) propBat.get("dcapec"));
					proprieteBatie.setDescr((String) propBat.get("descr"));
					proprieteBatie.setDindic((String) propBat.get("dindic"));
					proprieteBatie.setDniv((String) propBat.get("dniv"));
					proprieteBatie.setDnubat((String) propBat.get("dnubat"));
					proprieteBatie.setDnupla((String) propBat.get("dnupla"));
					proprieteBatie.setDnvoiri((String) propBat.get("dnvoiri"));
					proprieteBatie.setDpor((String) propBat.get("dpor"));
					proprieteBatie.setDvltrl((String) propBat.get("dvltrl"));
					proprieteBatie.setDvoilib((String) propBat.get("voie"));
					proprieteBatie.setFcexb((String) propBat.get("fcexb"));
					proprieteBatie.setGnextl((String) propBat.get("gnextl"));
					proprieteBatie.setJandeb((String) propBat.get("jandeb"));
					proprieteBatie.setJanimp((String) propBat.get("janimp"));
					proprieteBatie.setJdatat((String) propBat.get("jdatat"));
					proprieteBatie.setMvltieomx((String) propBat.get("mvltieomx"));

					String exoneration = (String) propBat.get("ccolloc");
					if (exoneration == "TC") {
						pbDepartementRevenuExonere = (Integer) propBat.get("dvldif2a");
					} else if (exoneration == "R") {
						pbRegionRevenuExonere = (Integer) propBat.get("dvldif2a");
					} else if (exoneration == "D") {
						pbDepartementRevenuExonere = 0;
					} else if (exoneration == "GC" || exoneration == "C") {
						pbCommuneRevenuExonere = (Integer) propBat.get("dvldif2a");
					} else {
						logger.debug("Exoneration on additional taxes");
					}

					if (!proprietesBIds.contains(proprieteId)) {

						pbRevenuImposable = pbRevenuImposable + ((Integer) propBat.get("vlbaia") == null ? 0 : (Integer) propBat.get("vlbaia"));
						pbCommuneRevenuImposable = pbCommuneRevenuImposable + ((Integer) propBat.get("vlbaia_com") == null ? 0 : (Integer) propBat.get("vlbaia_com"));
						pbDepartementRevenuImposable = pbDepartementRevenuImposable + ((Integer) propBat.get("vlbaia_dep") == null ? 0 : (Integer) propBat.get("vlbaia_dep"));
						pbRegionRevenuImposable = pbRegionRevenuImposable + ((Integer) propBat.get("vlbaia_reg") == null ? 0 : (Integer) propBat.get("vlbaia_reg"));
						
						proprietesBIds.add(proprieteId);
					}

					proprietesBaties.add(proprieteBatie);
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
					impositionBatie.setRegionRevenuExonere(pbRegionRevenuExonere);
					impositionBatie.setRegionRevenuImposable(pbRegionRevenuImposable);
					impositionBatie.setRevenuImposable(pbRevenuImposable);

					compteCommunal.setImpositionBatie(impositionBatie);

					compteCommunal.setProprieteBaties(proprietesBaties);
				}

				// Information sur les proprietés non baties
				List<ProprieteNonBatie> proprietesNonBaties = new ArrayList<ProprieteNonBatie>();
				List<String> proprietesNbIds = new ArrayList<String>();

				int pnbCommuneRevenuExonere = 0;
				int pnbCommuneRevenuImposable = 0;
				int pnbDepartementRevenuExonere = 0;
				int pnbDepartementRevenuImposable = 0;
				int pnbRegionRevenuExonere = 0;
				int pnbRegionRevenuImposable = 0;
				float pnbRevenuImposable = 0;
				int pnbMajorationTerrain = 0;
				int pnbSurface = 0;

				StringBuilder queryBuilderProprieteNonBatie = new StringBuilder();

				queryBuilderProprieteNonBatie.append("select distinct id_local, jdatat, ccopre, ccosec, dnupla, dnvoiri, dindic, natvoi||' '||dvoilib as voie, ccoriv, dparpi, gpafpd, ccostn, ccosub, cgrnum, dclssf, cnatsp, dcntsf, drcsuba, pdl, dnulot, ccolloc, jandeb, janimp, fcexb, dreflf, majposa, bisufad, bisufad_dep, bisufad_reg ");
				queryBuilderProprieteNonBatie.append("from ");
				queryBuilderProprieteNonBatie.append(databaseSchema);
				queryBuilderProprieteNonBatie.append(".proprietenonbatie pnb ");
				queryBuilderProprieteNonBatie.append(" where pnb.comptecommunal = ? ORDER BY ccosec, dnupla");

				logger.debug("Get undeveloped property information " );
				List<Map<String, Object>> proprietesNonBatiesResult = jdbcTemplate.queryForList(queryBuilderProprieteNonBatie.toString(), idCompteCommunal);

				for (Map<String, Object> propNonBat : proprietesNonBatiesResult) {
					
					String proprieteNbId = (String) propNonBat.get("id_local");
					
					if(logger.isDebugEnabled()){
						logger.debug("Get undeveloped property id : " + proprieteNbId );
					}

					// "TC";"toutes les collectivités"
					// "R";"Région => l'exonération porte sur la seule part régionale"
					// "GC";"Groupement de communes"
					// "D";"Département => l'exonération porte sur la seule part départementale"
					// "C";"Commune => l'exonération porte sur la seule part communale"
					// "A";"l'exonération porte sur la taxe additionnelle"
					String exonerationType = (String) propNonBat.get("ccolloc");
					int exonerationValue = (Integer) propNonBat.get("rcexnba") == null ? 0 : (Integer) propNonBat.get("rcexnba");
					
					if (exonerationType == "TC") {
						pnbDepartementRevenuExonere = pnbDepartementRevenuExonere + exonerationValue;
					} else if (exonerationType == "R") {
						pnbRegionRevenuExonere = pnbRegionRevenuExonere + exonerationValue;
					} else if (exonerationType == "D") {
						pnbDepartementRevenuExonere = pnbDepartementRevenuExonere + exonerationValue;
					} else if (exonerationType == "GC" || exonerationType == "C") {
						pnbCommuneRevenuExonere = pnbCommuneRevenuExonere + exonerationValue;
					} else {
						logger.debug("Exoneration on additional taxes");
					}

					ProprieteNonBatie proprieteNonBatie = new ProprieteNonBatie();

					proprieteNonBatie.setCcolloc((String) propNonBat.get("ccolloc"));
					proprieteNonBatie.setCcopre((String) propNonBat.get("ccopre"));
					proprieteNonBatie.setCcoriv((String) propNonBat.get("ccoriv"));
					proprieteNonBatie.setCcosec((String) propNonBat.get("ccosec"));
					proprieteNonBatie.setCcostn((String) propNonBat.get("ccostn"));
					proprieteNonBatie.setCcosub((String) propNonBat.get("ccosub"));
					proprieteNonBatie.setCgrnum((String) propNonBat.get("cgrnum"));
					proprieteNonBatie.setCnatsp((String) propNonBat.get("cnatsp"));
					proprieteNonBatie.setDclssf((String) propNonBat.get("dclssf"));		
					proprieteNonBatie.setDindic((String) propNonBat.get("dindic"));
					proprieteNonBatie.setDnulot((String) propNonBat.get("dnulot"));
					proprieteNonBatie.setDnupla((String) propNonBat.get("dnupla"));
					proprieteNonBatie.setDnvoiri((String) propNonBat.get("dnvoiri"));
					proprieteNonBatie.setDparpi((String) propNonBat.get("dparpi"));
					proprieteNonBatie.setDreflf((String) propNonBat.get("dreflf"));
					proprieteNonBatie.setDsgrpf((String) propNonBat.get("dsgrpf"));
					proprieteNonBatie.setDvoilib((String) propNonBat.get("voie"));
					proprieteNonBatie.setFcexb((String) propNonBat.get("fcexb"));
					proprieteNonBatie.setGnextl((String) propNonBat.get("gnextl"));
					proprieteNonBatie.setGpafpd((String) propNonBat.get("gpafpd"));
					proprieteNonBatie.setJandeb((String) propNonBat.get("jandeb"));
					proprieteNonBatie.setJanimp((String) propNonBat.get("janimp"));
					proprieteNonBatie.setJdatat((String) propNonBat.get("jdatat"));
					proprieteNonBatie.setGnextl((String) propNonBat.get("gnextl"));
					proprieteNonBatie.setPdl((String) propNonBat.get("pdl"));
					proprieteNonBatie.setPexb((String) propNonBat.get("pexn"));
									
					int surface = (Integer) propNonBat.get("dcntsf") == null ? 0 : (Integer) propNonBat.get("dcntsf");
					proprieteNonBatie.setDcntsf(surface);
					
					int revenu = (Integer) propNonBat.get("drcsuba") == null ? 0 : (Integer) propNonBat.get("drcsuba");
					proprieteNonBatie.setDrcsuba(revenu);
					
					if (!proprietesNbIds.contains(proprieteNbId)){
						
						pnbRevenuImposable = pnbRevenuImposable + revenu;
						
						pnbCommuneRevenuImposable = pnbCommuneRevenuImposable + ((Integer) propNonBat.get("bisufad") == null ? 0 : (Integer) propNonBat.get("bisufad"));
						pnbDepartementRevenuImposable = pnbDepartementRevenuImposable +  ((Integer) propNonBat.get("bisufad_dep") == null ? 0 : (Integer) propNonBat.get("bisufad_dep"));
						pnbRegionRevenuImposable = pnbRegionRevenuImposable + ((Integer) propNonBat.get("bisufad_reg") == null ? 0 : (Integer) propNonBat.get("bisufad_reg"));
						
						pnbMajorationTerrain = pnbMajorationTerrain + ((Integer) propNonBat.get("majposa") == null ? 0 : (Integer) propNonBat.get("majposa"));
						
						pnbSurface = pnbSurface + surface; 
						
						proprietesNbIds.add(proprieteNbId);
					}
					
					proprietesNonBaties.add(proprieteNonBatie);

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
					impositionNonBatie.setRegionRevenuExonere(pnbRegionRevenuExonere);
					impositionNonBatie.setRegionRevenuImposable(pnbRegionRevenuImposable);
					impositionNonBatie.setRevenuImposable(pnbRevenuImposable);
					impositionNonBatie.setMajorationTerraion(pnbMajorationTerrain);
					impositionNonBatie.setSurface(pnbSurface);

					compteCommunal.setImpositionNonBatie(impositionNonBatie);

					compteCommunal.setProprieteNonBaties(proprietesNonBaties);
				}

				// Ajout du compte communal à la liste
				comptesCommunaux.add(compteCommunal);
			}
		}
		relevePropriete.setComptesCommunaux(comptesCommunaux);
		
		return relevePropriete;
	}

}
