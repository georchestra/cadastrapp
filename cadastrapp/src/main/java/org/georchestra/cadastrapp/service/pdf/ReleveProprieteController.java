package org.georchestra.cadastrapp.service.pdf;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
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
	 * 
	 * @param headers
	 * @param parcelleList
	 * @return
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
	 * @param parcelleList
	 * @param headers
	 * @return
	 */
	private RelevePropriete getReleveProprieteInformation(List<String> idComptesCommunaux, HttpHeaders headers) {

		RelevePropriete relevePropriete = new RelevePropriete();

		// Information d'entête
		relevePropriete.setAnneMiseAJour(dateValiditeDonnees);
		relevePropriete.setService(organisme);

		List<CompteCommunal> comptesCommunaux = new ArrayList<CompteCommunal>();

		for (String idCompteCommunal : idComptesCommunaux) {

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

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(queryBuilder.toString(), idCompteCommunal);

			for (Map row : rows) {
				compteCommunal.setLibelleCommune((String) row.get("libcom"));

				String cgoCommune = (String) row.get("cgocommune");
				if (cgoCommune != null && cgoCommune.length() > 4) {
					compteCommunal.setCodeCommune(cgoCommune.substring(3));
					compteCommunal.setCodeDepartement(cgoCommune.substring(0, 3));
				}
			}
			
			// Display information only if at least CNIL level 1 or 2
			if (getUserCNILLevel(headers) > 0) {
				// Inforamtion sur les proprietaires
				List<Proprietaire> proprietaires = new ArrayList<Proprietaire>();

				StringBuilder queryBuilderProprietaire = new StringBuilder();
				queryBuilderProprietaire.append("select prop.comptecommunal, ccodem_lib, dldnss, jdatnss,  ccodro_lib, prop.ccoqua_lib||' '||prop.ddenom as nom, prop.dlign3||' '||prop.dlign4||' '||prop.dlign5||' '||prop.dlign6 as adresse ");
				queryBuilderProprietaire.append("from ");
				queryBuilderProprietaire.append(databaseSchema);
				queryBuilderProprietaire.append(".proprietaire prop ");
				queryBuilderProprietaire.append("where prop.comptecommunal = ?");
				queryBuilderProprietaire.append(addAuthorizationFiltering(headers));

				List<Map<String, Object>> proprietairesResult = jdbcTemplate.queryForList(queryBuilderProprietaire.toString(), idCompteCommunal);

				for (Map<String, Object> prop : proprietairesResult) {
					Proprietaire proprietaire = new Proprietaire();
					proprietaire.setNom((String) prop.get("nom"));
					proprietaire.setAdresse((String) prop.get("adresse"));
					proprietaire.setCodeDeDemenbrement((String) prop.get("ccodem_lib"));
					proprietaire.setDateNaissance((Date) prop.get("jdatnss"));
					proprietaire.setLieuNaissance((String) prop.get("dldnss"));
					proprietaire.setDroitReel((String) prop.get("ccodro_lib"));

					proprietaires.add(proprietaire);
				}
				// Ajout la liste des proprietaires
				compteCommunal.setProprietaires(proprietaires);

				// Information sur les proprietés baties
				List<ProprieteBatie> proprietesBaties = new ArrayList<ProprieteBatie>();
				StringBuilder queryBuilderProprieteBatie = new StringBuilder();

				queryBuilderProprieteBatie.append("select jdatat, ccopre, ccosec, dnupla, dnvoiri, dindic, natvoi||' '||dvoilib as voie, ccoriv, dnubat, descr, dniv, dpor, invar, ccoaff, ccoeva, ccostn, ccolloc, gnextl, jandeb, janimp, fcexb, mvltieomx ");
				queryBuilderProprieteBatie.append("from ");
				queryBuilderProprieteBatie.append(databaseSchema);
				queryBuilderProprieteBatie.append(".proprietebatie pb ");
				queryBuilderProprieteBatie.append(" where pb.comptecommunal = ?");

				List<Map<String, Object>> proprietesBatiesResult = jdbcTemplate.queryForList(queryBuilderProprieteBatie.toString(), idCompteCommunal);

				for (Map<String, Object> propBat : proprietesBatiesResult) {
					ProprieteBatie proprieteBatie = new ProprieteBatie();
					proprieteBatie.setCcoaff((String) propBat.get("ccoaff"));
					proprieteBatie.setCcoeav((String) propBat.get("ccoeav"));
					proprieteBatie.setCcolloc((String) propBat.get("ccolloc"));
					proprieteBatie.setCconlc((String) propBat.get("cconlc"));
					proprieteBatie.setCcopre((String) propBat.get("ccopre"));
					proprieteBatie.setCcoriv((String) propBat.get("ccoriv"));
					proprieteBatie.setCcosec((String) propBat.get("ccosec"));
					proprieteBatie.setCcostn((String) propBat.get("ccostn"));
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
					proprieteBatie.setInvar((String) propBat.get("invar"));
					proprieteBatie.setJandeb((String) propBat.get("jandeb"));
					proprieteBatie.setJanimp((String) propBat.get("janimp"));
					proprieteBatie.setJdatat((Date) propBat.get("jdatat"));
					proprieteBatie.setMvltieomx((String) propBat.get("mvltieomx"));

					proprietesBaties.add(proprieteBatie);
				}
				// ajout la liste des propriete baties uniquement si il y en a au moins une
				if(!proprietesBaties.isEmpty()){
					compteCommunal.setProprieteBaties(proprietesBaties);
				}

				// Information sur les proprietés non baties
				List<ProprieteNonBatie> proprietesNonBaties = new ArrayList<ProprieteNonBatie>();

				StringBuilder queryBuilderProprieteNonBatie = new StringBuilder();

				queryBuilderProprieteNonBatie.append("select jdatat, ccopre, ccosec, dnupla, dnvoiri, dindic, natvoi||' '||dvoilib as voie, ccoriv, dparpi, gpafpd, ccostn, ccosub, cgrnum, dclssf, cnatsp, dcntsf, drcsuba, pdl, dnulot, ccolloc, jandeb, janimp, fcexb ");
				queryBuilderProprieteNonBatie.append("from ");
				queryBuilderProprieteNonBatie.append(databaseSchema);
				queryBuilderProprieteNonBatie.append(".proprietenonbatie pnb ");
				queryBuilderProprieteNonBatie.append(" where pnb.comptecommunal = ?");

				List<Map<String, Object>> proprietesNonBatiesResult = jdbcTemplate.queryForList(queryBuilderProprieteNonBatie.toString(), idCompteCommunal);

				for (Map<String, Object> propNonBat : proprietesNonBatiesResult) {

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
					proprieteNonBatie.setDcntsf((Integer) propNonBat.get("dcntsf"));
					proprieteNonBatie.setDindic((String) propNonBat.get("dindic"));
					proprieteNonBatie.setDnulot((String) propNonBat.get("dnulot"));
					proprieteNonBatie.setDnupla((String) propNonBat.get("dnupla"));
					proprieteNonBatie.setDnvoiri((String) propNonBat.get("dnvoiri"));
					proprieteNonBatie.setDparpi((String) propNonBat.get("dparpi"));
					proprieteNonBatie.setDrcsuba((BigDecimal) propNonBat.get("drcsuba"));
					proprieteNonBatie.setDreflf((String) propNonBat.get("dreflf"));
					proprieteNonBatie.setDsgrpf((String) propNonBat.get("dsgrpf"));
					proprieteNonBatie.setDvoilib((String) propNonBat.get("voie"));

					proprieteNonBatie.setFcexb((String) propNonBat.get("fcexb"));
					proprieteNonBatie.setGnextl((String) propNonBat.get("gnextl"));
					proprieteNonBatie.setGpafpd((String) propNonBat.get("gpafpd"));
					proprieteNonBatie.setJandeb((String) propNonBat.get("jandeb"));
					proprieteNonBatie.setJanimp((String) propNonBat.get("janimp"));
					proprieteNonBatie.setJdatat((Date) propNonBat.get("jdatat"));

					proprieteNonBatie.setPdl((String) propNonBat.get("pdl"));
					proprieteNonBatie.setPexb((String) propNonBat.get("pexb"));

					proprietesNonBaties.add(proprieteNonBatie);
				}
				
				// ajout la liste des propriete non baties uniquement si il y en a au moins une
				if(!proprietesNonBaties.isEmpty()){
					compteCommunal.setProprieteNonBaties(proprietesNonBaties);
				}

				// Ajout du compte communal à la liste
				comptesCommunaux.add(compteCommunal);
			}
			relevePropriete.setComptesCommunaux(comptesCommunaux);

		}

		return relevePropriete;
	}

}
