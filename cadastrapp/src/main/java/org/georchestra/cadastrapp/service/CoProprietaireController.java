package org.georchestra.cadastrapp.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.georchestra.cadastrapp.helper.ProprieteHelper;
import org.georchestra.cadastrapp.model.pdf.InformationLots;
import org.georchestra.cadastrapp.service.export.ExportHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Service to get co owners information
 * 
 * @author pierre jego
 *
 */
@Controller
public class CoProprietaireController extends CadController {

	static final Logger logger = LoggerFactory.getLogger(CoProprietaireController.class);
	
	@Autowired
	ExportHelper exportHelper;
	
	@Autowired
	ProprieteHelper proprieteHelper;

	@RequestMapping(path ="/getCoProprietaireList", produces = {MediaType.APPLICATION_JSON_VALUE}, method= {RequestMethod.GET})
	/**
	 * 
	 * /getCoProprietaireList 
	 * This will return information about owners in JSON format
	 * 
	 * @param headers headers from request used to filter search using LDAP Roles
	 * @param parcelle
	 * 
	 * @return List<Map<String, Object>> Co owners information in key:value List
	 * 
	 * @throws SQLException
	 */
	public @ResponseBody List<Map<String, Object>> getCoProprietairesList(
			@RequestParam(required= false) String parcelle, 
			@RequestParam(required= false) String comptecommunal,
			@RequestParam(required= false) String cgocommune, 
			@RequestParam(required= false) String ddenom,
			@RequestParam(defaultValue= "0", required= false) int details) throws SQLException {

		List<Map<String, Object>> coProprietaires = new ArrayList<Map<String, Object>>();
		List<String> queryParams = new ArrayList<String>();

		// only for CNIL1 and CNIL2
		if (getUserCNILLevel() > 0 && cgocommune != null && cgocommune.length() >0) {

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
				queryCoProprietaireBuilder.append(addAuthorizationFiltering("prop."));
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				coProprietaires = jdbcTemplate.queryForList(queryCoProprietaireBuilder.toString(), queryParams.toArray());
			}
			
		}
		return coProprietaires;
	}

	@RequestMapping(path = "/getCoProprietaire", produces = {MediaType.APPLICATION_JSON_VALUE}, method= {RequestMethod.GET})
	/**
	 * getCoProprietaire
	 * 
	 * @param parcelle
	 * @return
	 */
	public 	@ResponseBody Map<String, Object> getCoProprietaire(@RequestParam String parcelle, 
			@RequestParam(defaultValue= "0", required= false) int start,
			@RequestParam(defaultValue= "25", required= false) int limit) {

		logger.debug("get Co Proprietaire - parcelle : " + parcelle);

		Map<String, Object> finalResult = new HashMap<String, Object>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		if (getUserCNILLevel() > 0) {
			
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			
			StringBuilder queryCount = new StringBuilder();
			
			queryCount.append("SELECT COUNT(*) FROM (select distinct p.comptecommunal, p.app_nom_usage from ");
			queryCount.append(databaseSchema);
			queryCount.append(".co_propriete_parcelle propar, ");
			queryCount.append(databaseSchema);
			queryCount.append(".proprietaire p where propar.parcelle = ?  and p.comptecommunal = propar.comptecommunal ");
			queryCount.append(addAuthorizationFiltering("p."));
			queryCount.append(" ) as temp;");
			
			int resultCount = jdbcTemplate.queryForObject(queryCount.toString(), new Object[] {parcelle}, Integer.class);
			
			logger.debug("get Co Proprietaire - number of co-proprietaire : " + resultCount);
			
			finalResult.put("results", resultCount);

			StringBuilder queryBuilder = new StringBuilder();

			// CNIL Niveau 1 or 2
			queryBuilder.append("select distinct p.comptecommunal, p.app_nom_usage, p.dldnss, p.jdatnss, p.ccodro, p.ccodro_lib, p.dformjur, ");
			queryBuilder.append(" COALESCE(p.dlign3, '')||' '||COALESCE(p.dlign4,'')||' '||COALESCE(p.dlign5,'')||' '||COALESCE(p.dlign6,'') as adresse ");	
			queryBuilder.append(" from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".co_propriete_parcelle propar,");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".proprietaire p where propar.parcelle = ? ");
			queryBuilder.append(" and p.comptecommunal = propar.comptecommunal ");
			queryBuilder.append(addAuthorizationFiltering("p."));
			queryBuilder.append(" ORDER BY p.app_nom_usage ");
			queryBuilder.append(" LIMIT ?");
			queryBuilder.append(" OFFSET ?");
			
			result = jdbcTemplate.queryForList(queryBuilder.toString(),  new Object[] {parcelle, limit, start});
			
			finalResult.put("rows", result);

		} else {
			logger.info("User does not have enough right to see information about proprietaire");
		}

		return finalResult;

	}
	
	@RequestMapping(path = "/exportCoProprietaireByParcelles", produces = {"text/csv;charset=utf-8"}, method= {RequestMethod.POST})
	/**
	 * Create a csv file from given parcelles id
	 * 
	 * @param headers used to filter displayed information
	 * @param parcelles list of parcelle separated by a coma
	 * 
	 * @return csv containing list of co-owners of given parcelle list
	 * 
	 * @throws SQLException
	 */
	public 	ResponseEntity<byte[]> exportProprietaireByParcelles(
			@RequestParam String parcelles) throws SQLException {
		
		// Create empty content
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(HttpStatus.NO_CONTENT);
		
		// User need to be at least CNIL1 level
		if (getUserCNILLevel()>0){
		
			String  entete = "proprio_id;droit_reel_libelle;denomination_usage;parcelles;civilite;nom_usage;prenom_usage;denomination_naissance;nom_naissance;prenom_naissance;adresse_ligne3;adresse_ligne4;adresse_ligne5;adresse_ligne6;forme_juridique";
			if(getUserCNILLevel()>1){
				entete = entete + ";lieu_naissance; date_naissance";
			}
			
			String[] parcelleList = StringUtils.split(parcelles, ',');
			
			if(parcelleList != null && parcelleList.length > 0){
				
				logger.debug("Nb of parcelles to search in : " + parcelleList.length);

				// Get value from database
				List<Map<String,Object>> coproprietaires = new ArrayList<Map<String,Object>>();
										
				StringBuilder queryBuilder = new StringBuilder();
				queryBuilder.append("select prop.comptecommunal, ccodro_lib, app_nom_usage, string_agg(parcelle, ','), ccoqua_lib, dnomus, dprnus, ddenom, dnomlp, dprnlp, dlign3, dlign4, dlign5, dlign6, dformjur ");
				
				// If user is CNIL2 add birth information
				if(getUserCNILLevel()>1){
					queryBuilder.append(", dldnss, jdatnss ");
				}
				queryBuilder.append("from ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".proprietaire prop, ");
				queryBuilder.append(databaseSchema);
				queryBuilder.append(".co_propriete_parcelle proparc ");
				queryBuilder.append(createWhereInQuery(parcelleList.length, "proparc.parcelle"));
				queryBuilder.append(" and prop.comptecommunal = proparc.comptecommunal ");
				queryBuilder.append(addAuthorizationFiltering());
				queryBuilder.append("GROUP BY prop.comptecommunal, ccodro_lib, app_nom_usage, ccoqua_lib, dnomus, dprnus, ddenom, dnomlp, dprnlp, dlign3, dlign4, dlign5, dlign6, dformjur");
				// If user is CNIL2 add birth information
				if(getUserCNILLevel()>1){
					queryBuilder.append(", dldnss, jdatnss ");
				}
				queryBuilder.append(" ORDER BY prop.comptecommunal");
				
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				coproprietaires = jdbcTemplate.queryForList(queryBuilder.toString(), parcelleList);
				
				File file = null;

				try {
					// Create file
					file = exportHelper.createCSV(coproprietaires, entete);
									
					// build csv response
					HttpHeaders headers = new HttpHeaders();
					headers.setContentDispositionFormData("filename", file.getName());
					response = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
				}catch (IOException e) {
					logger.error("Error while creating CSV files ", e);
				} finally {
					if (file != null) {
						file.deleteOnExit();
					}
				}
			}
			else{
				//log empty request
				logger.info("Parcelle Id List is empty nothing to search");
			}
		}else{
			logger.info("User does not have rights to see thoses informations");
		}
	
		return response;
	}
	
	@RequestMapping(path = "/exportLotsAsCSV", produces = {"text/csv;charset=utf-8"}, method= {RequestMethod.POST})
	/**
	 * Create a csv file from given plot and building id
	 * 
	 * @param headers Used to filter displayed information
	 * @param parcelle String
	 * @param dnubat String
	 * 
	 * @return csv containing bundle information
	 * 
	 * @throws SQLException
	 */
	public ResponseEntity<byte[]>  exportLotsAsSCV(
			@RequestParam String parcelle, 
			@RequestParam String dnubat) throws SQLException {
		
		// Create empty content
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(HttpStatus.NO_CONTENT);
		
		if(getUserCNILLevel()>0){
			logger.debug("Input parameters are : " + parcelle + " - " + dnubat);
			
			String entete = "parcelle_num;numero_local;batiment;numero_lot;part_lot;total_lot;logement;dependance;local_commercial;type_proprietaire;compte_communal;nom_proprietaire;adresse";
				
			// Get value from database with owner values
			List<Map<String,Object>> bundleResults = proprieteHelper.getLotsInformation(parcelle, dnubat, true);
					
			File file = null;
			try{
				file = exportHelper.createCSV(bundleResults, entete);
				
				// build csv response
				HttpHeaders headers = new HttpHeaders();
				headers.setContentDispositionFormData("filename", file.getName());
				response = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
			}catch (IOException e) {
				logger.error("Error while creating CSV files ", e);
			} finally {
				if (file != null) {
					file.deleteOnExit();
				}
			}
		}else{
			logger.info("User does not have rights to see thoses informations");
		}
	
		return response;
	}
	
	@RequestMapping(path = "/exportLotsAsPDF" , produces = {MediaType.APPLICATION_PDF_VALUE}, method= {RequestMethod.POST})
	/**
	 * Create a pdf file from given plot and building id
	 * 
	 * @param parcelle String
	 * @param dnubat String
	 * 
	 * @return pdf containing bundle information
	 * 
	 * @throws SQLException
	 */
	public ResponseEntity<byte[]> exportLotsAsPDF(
			@RequestParam String parcelle,
			@RequestParam String dnubat) throws SQLException {
		
		// Create empty content
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(HttpStatus.NO_CONTENT);
		
		if(getUserCNILLevel()>0){
			logger.debug("Input parameters are : " + parcelle + " - " + dnubat);
			final String xslTemplate = "xsl/lots.xsl";
			
			// Check if parcelle list is not empty
			if (parcelle != null && dnubat != null) {
				
				String tempFolder = CadastrappPlaceHolder.getProperty("tempFolder");
				
				// Pdf temporary filename using tmp folder and timestamp
				final String pdfTmpFileName = tempFolder+File.separator+"Lots"+new Date().getTime();
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
					pdfResult = new File(pdfTmpFileName+".pdf");
					pdfResult.deleteOnExit();
					
					out = new BufferedOutputStream(new FileOutputStream(pdfResult));
					
					FopFactory fopFactory = FopFactory.newInstance(pdfResult.toURI());
					fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

					jaxbContext = JAXBContext.newInstance(InformationLots.class);
					jaxbMarshaller = jaxbContext.createMarshaller();

					jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

					// Get bordereau parcellaire information
					InformationLots informationLots = proprieteHelper.getInformationLotsForPDF(parcelle, dnubat);
					File xmlfile = null;
					File foFile = null;
					OutputStream foOutPutStream = null;
					
					try {
						// Xml file will be deleted on JVM exit
						xmlfile = new File(pdfTmpFileName+".xml");
						xmlfile.deleteOnExit();
						
						jaxbMarshaller.marshal(informationLots, xmlfile);

						// log on console marshaller only if debug log is one
						if (logger.isDebugEnabled()) {
							StringWriter stringWriter = new StringWriter();					
							jaxbMarshaller.marshal(informationLots, stringWriter);
							logger.debug(stringWriter.toString());
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

						// Create response
						ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
						.filename(pdfResult.getName())
						.build();

						HttpHeaders headers = new HttpHeaders();
						headers.setContentType(MediaType.APPLICATION_PDF);
						headers.setContentDisposition(contentDisposition);

						response = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(pdfResult), headers, HttpStatus.OK);
					} catch (JAXBException jaxbException) {
						logger.warn("Error during converting object to xml : " + jaxbException);
					} catch (TransformerException transformerException) {
						logger.warn("Error during converting object to xml : " + transformerException);
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
			} else {
				logger.warn("Required parameter missing");
			}

		}else{
			logger.info("User does not have rights to see thoses informations");
		}
	
		return response;
	}
}
