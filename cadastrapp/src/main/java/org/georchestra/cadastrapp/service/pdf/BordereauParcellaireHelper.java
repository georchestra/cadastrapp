package org.georchestra.cadastrapp.service.pdf;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
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

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.MimeConstants;
import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.georchestra.cadastrapp.model.pdf.BordereauParcellaire;
import org.georchestra.cadastrapp.model.pdf.Parcelle;
import org.georchestra.cadastrapp.model.pdf.Proprietaire;
import org.georchestra.cadastrapp.model.pdf.Style;
import org.georchestra.cadastrapp.service.CadController;
import org.georchestra.cadastrapp.service.exception.CadastrappServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public final class BordereauParcellaireHelper extends CadController{

	final static Logger logger = LoggerFactory.getLogger(BordereauParcellaireHelper.class);

	final String xslTemplate = "xsl/bordereauParcellaire.xsl";
	
	final String xslTemplateError = "xsl/bordereauParcellaireError.xsl";
	
	final String tableProprietaire = "proprietaire_parcelle";
	final String tableCoProprietaire = "co_propriete_parcelle";
	
	/**
	 * Get all information from database for all parcelle list
	 * 
	 * @param isCoPro	get copro BP information if true
	 * @param parcelleList
	 *            List of parcelle id, like
	 * @param personalData
	 *            filter use to add owners information about parcelle, 1 to get
	 *            owner information
	 * 
	 * @return BordereauParcellaire witch contains list of parcelle
	 */
	public BordereauParcellaire getBordereauParcellaireInformation(List<String> parcelleList, int personalData, boolean isCoPro) {
		return getBordereauParcellaireInformation(parcelleList, personalData, isCoPro, null, 0);
	}

	/**
	 * Get all information from database for all parcelle list
	 * 
	 * @param isCoPro	get copro BP information if true
	 * @param parcelleList
	 *            List of parcelle id, like
	 * @param personalData
	 *            filter use to add owners information about parcelle, 1 to get
	 *            owner information
	 * @param plotStyle style to display plot on image
	 * @param baseMapIndex index number of wanted basemap
	 * 
	 * @return BordereauParcellaire witch contains list of parcelle
	 */
	public BordereauParcellaire getBordereauParcellaireInformation(List<String> parcelleList, int personalData, boolean isCoPro, Style plotStyle, int baseMapIndex) {

		
		BordereauParcellaire bordereauParcellaire = new BordereauParcellaire();
		
		if(parcelleList!=null && !parcelleList.isEmpty()){
			logger.debug("Parcelle List : " + parcelleList);
			
			final String dateValiditeDonneesMajic = CadastrappPlaceHolder.getProperty("pdf.dateValiditeDonneesMajic");
			final String organisme = CadastrappPlaceHolder.getProperty("pdf.organisme");
			final String dateValiditeDonneesEDIGEO = CadastrappPlaceHolder.getProperty("pdf.dateValiditeDonneesEDIGEO");
			final String webappUrl = CadastrappPlaceHolder.getProperty("webapp.url.services");


			bordereauParcellaire.setDateDeValiditeMajic(dateValiditeDonneesMajic);
			bordereauParcellaire.setDateDeValiditeEdigeo(dateValiditeDonneesEDIGEO);
			bordereauParcellaire.setService(organisme);
			bordereauParcellaire.setServiceUrl(webappUrl);		
			bordereauParcellaire.setStyle(plotStyle);
			bordereauParcellaire.setBaseMapIndex(baseMapIndex);

			List<Parcelle> parcellesInformation = new ArrayList<Parcelle>();

			// Create query
			StringBuilder queryBuilder = new StringBuilder();

			queryBuilder.append("select p.parcelle, c.libcom, p.dcntpa, p.ccopre || p.ccosec as ccosec, p.dnupla, p.dnvoiri||' '||p.dindic||' '||p.cconvo||' '||dvoilib as adresse, p.ccoriv from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".parcelleDetails p, ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".commune c ");
			queryBuilder.append(createWhereInQuery(parcelleList.size(), "parcelle"));
			queryBuilder.append("and p.cgocommune = c.cgocommune;");

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			List<Map<String, Object>> parcelles = jdbcTemplate.queryForList(queryBuilder.toString(), parcelleList.toArray());

			logger.debug("Parcelle size : " + parcelles.size());
			
			if(parcelles.size() > 0){
				for (Map<String, Object> row : parcelles) {
					logger.debug("Parcelle information : " + row);
					Parcelle parcelle = new Parcelle();
					parcelle.setParcelleId((String) row.get("parcelle"));
					parcelle.setLibelleCommune((String) row.get("libcom"));
					parcelle.setAdresseCadastrale((String) row.get("adresse"));
					parcelle.setCodeFantoir((String) row.get("ccoriv"));
					parcelle.setParcelle((String) row.get("dnupla"));
					parcelle.setSection((String) row.get("ccosec"));
					parcelle.setSurfaceCadastrale((Integer) row.get("dcntpa"));

					logger.debug("Parcelle information : " + parcelle);

					if (personalData > 0 && getUserCNILLevel()>0) {
						List<Proprietaire> proprietaires = new ArrayList<Proprietaire>();
						
						String tableName = (isCoPro)?tableCoProprietaire:tableProprietaire;
										
						StringBuilder queryBuilderProprietaire = new StringBuilder();
						queryBuilderProprietaire.append("select prop.comptecommunal, app_nom_usage as nom, prop.dlign3||' '||prop.dlign4||' '||prop.dlign5||' '||prop.dlign6 as adresse ");   			    
						queryBuilderProprietaire.append("from ");
						queryBuilderProprietaire.append(databaseSchema);
						queryBuilderProprietaire.append(".proprietaire prop, ");
						queryBuilderProprietaire.append(databaseSchema);
						queryBuilderProprietaire.append(".");
						queryBuilderProprietaire.append(tableName);
						queryBuilderProprietaire.append(" proparc ");
						queryBuilderProprietaire.append("where proparc.parcelle = ? and prop.comptecommunal = proparc.comptecommunal");
						queryBuilderProprietaire.append(addAuthorizationFiltering());
			 	       
						List<Map<String, Object>> proprietairesResult = jdbcTemplate.queryForList(queryBuilderProprietaire.toString(), row.get("parcelle"));

						for (Map<String, Object> prop : proprietairesResult) {
							Proprietaire proprietaire = new Proprietaire();
							proprietaire.setNom((String) prop.get("nom"));
							proprietaire.setAdresse((String) prop.get("adresse"));
							
							proprietaires.add(proprietaire);
						}			
						parcelle.setProprietaires(proprietaires);
					}
					parcellesInformation.add(parcelle);
				}
				
				bordereauParcellaire.setParcelleList(parcellesInformation);
				
			}else {
				bordereauParcellaire.setEmpty(true);
			}
			
		}else{
			bordereauParcellaire.setEmpty(true);
		}

		return bordereauParcellaire;
	}
	
	/**
	 * get parcelles by owner
	 * @param comptecommunal owner id
	 * @param isCoPro	boolean to get coowner information
	 * @param idParcelle plot id
	 * @return list of parcelle
	 */
	public List<Map<String, Object>> getParcellesByProprietaire(String comptecommunal, boolean isCoPro, String idParcelle){
		
		List<Map<String, Object>> parcelles = null;
		StringBuilder queryBuilder = new StringBuilder();
		
		String tableName = (isCoPro)?tableCoProprietaire:tableProprietaire;
		
		String whereParcelle = (isCoPro)?" and parcelle = ? ":"";
		
		// if search by dnuproList or comptecommunal
		// directly search in view parcelle
		if(comptecommunal != null){
			queryBuilder.append("select distinct ");
			queryBuilder.append("parcelle ");
			queryBuilder.append("from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".");
			queryBuilder.append(tableName);
			queryBuilder.append(" where comptecommunal = ? ");
			queryBuilder.append(whereParcelle);
			queryBuilder.append(";");
			
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			parcelles = (isCoPro)?jdbcTemplate.queryForList(queryBuilder.toString(), comptecommunal,idParcelle):jdbcTemplate.queryForList(queryBuilder.toString(), comptecommunal);
		}
		else{
			logger.info("Missing or empty input parameter");
		}
		return parcelles;
	}
	
	/**
	 * Generate pdf file using FOP 
	 * @param bp object that contain all information about Plots 
	 * @param noData  true if erros occures when getting data
	 * @return pdf file by FOP
	 * @throws CadastrappServiceException if an globalException occured
	 */
	public File generatePDF(BordereauParcellaire bp, boolean noData) throws CadastrappServiceException{
		
		String tempFolder = CadastrappPlaceHolder.getProperty("tempFolder");
		
		// Pdf temporary filename using tmp folder and timestamp
		final String pdfTmpFileName = tempFolder+File.separator+"BP"+new Date().getTime();

		InputStream xsl = null;
		if(noData){
			xsl = Thread.currentThread().getContextClassLoader().getResourceAsStream(xslTemplateError);
		}else{
			xsl = Thread.currentThread().getContextClassLoader().getResourceAsStream(xslTemplate);
		}
		
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

			FopFactoryBuilder builder = new FopFactoryBuilder(pdfResult.toURI());
			
			// get DPI from comfig file
			int dpi=Integer.parseInt(CadastrappPlaceHolder.getProperty("pdf.dpi"));
			
			builder.setSourceResolution(dpi);
			builder.setTargetResolution(dpi);
			
			FopFactory fopFactory = builder.build();
			fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);

			jaxbContext = JAXBContext.newInstance(BordereauParcellaire.class);
			jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			File xmlfile = null;
			File foFile = null;
			OutputStream foOutPutStream = null;
			
			try {
				// Xml file will be deleted on JVM exit
				xmlfile = new File(pdfTmpFileName+".xml");
				xmlfile.deleteOnExit();
				
				jaxbMarshaller.marshal(bp, xmlfile);

				// log on console marshaller only if debug log is one
				if (logger.isDebugEnabled()) {
					StringWriter stringWriter = new StringWriter();					
					jaxbMarshaller.marshal(bp, stringWriter);
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
				
			} catch (JAXBException jaxbException) {
				logger.warn("Error during converting object to xml : " + jaxbException);
			} catch (TransformerException transformerException) {
				logger.warn("Error when transform object : " + transformerException);
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
		}catch (Exception globalException) {
			throw new CadastrappServiceException(globalException);
		}finally{
			// Could not delete pdfResult here because it's still used by cxf
		}
		
		return pdfResult;

	}

	/**
	 * get parcelle by parcelle attribute
	 * @param commune commune id
	 * @param section	plot section id
	 * @param numero plot number
	 * @return list of parcelle
	 */
	public List<Map<String, Object>> getParcellesByInfoParcelle(String commune, String section, String numero) {
		List<Map<String, Object>> parcelles = null;
		StringBuilder queryBuilder = new StringBuilder();
		
		// if search by dnuproList or comptecommunal
		// directly search in view parcelle
		if(commune != null || section != null || numero != null){
			queryBuilder.append("select distinct ");
			queryBuilder.append("parcelle ");
			queryBuilder.append("from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".");
			queryBuilder.append("parcelle ");
			queryBuilder.append(" where cgocommune = ? and ccosec = ? and dnupla = ? ");
			queryBuilder.append(";");
			
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			parcelles = jdbcTemplate.queryForList(queryBuilder.toString(), commune,section,numero);
		}
		else{
			logger.info("Missing or empty input parameter");
		}
		return parcelles;
	}

	/**
	 * get parcelle by info owner
	 * @param commune town id
	 * @param ownerName owner name
	 * @return plots list corresponding to params
	 */
	public List<Map<String, Object>> getParcellesByInfoOwner(String commune, String ownerName) {
		List<Map<String, Object>> parcelles = null;
		StringBuilder queryBuilder = new StringBuilder();
		
		// if search by dnuproList or comptecommunal
		// directly search in view parcelle
		if(commune != null || ownerName != null){
			queryBuilder.append("(");
			queryBuilder.append("select distinct ");
			queryBuilder.append("propar.parcelle ");
			queryBuilder.append("from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".proprietaire p ,");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".proprietaire_parcelle propar");
			queryBuilder.append(" where p.comptecommunal = propar.comptecommunal ");
			queryBuilder.append(" and p.cgocommune = ? and p.app_nom_usage = ? ");
			queryBuilder.append(") UNION (");
			queryBuilder.append("select distinct ");
			queryBuilder.append("copropar.parcelle ");
			queryBuilder.append("from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".proprietaire p ,");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".co_propriete_parcelle copropar");
			queryBuilder.append(" where p.comptecommunal = copropar.comptecommunal ");
			queryBuilder.append(" and p.cgocommune = ? and p.app_nom_usage = ? ");
			queryBuilder.append(")");
			queryBuilder.append(";");
			
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			parcelles = jdbcTemplate.queryForList(queryBuilder.toString(),commune,ownerName,commune,ownerName);
		}
		else{
			logger.info("Missing or empty input parameter");
		}
		return parcelles;
	}

	/**
	 * get parcelle by co-propriete info
	 * @param commune town id
	 * @param section	plot section
	 * @param numero	plot number
	 * @param proprietaire	owner id
	 * @return plot list
	 */
	public List<Map<String, Object>> getParcellesByInfoLot(String commune, String section, String numero, String proprietaire) {
		List<Map<String, Object>> parcelles = null;
		StringBuilder queryBuilder = new StringBuilder();
		
		// if search by dnuproList or comptecommunal
		// directly search in view parcelle
		if(commune != null || section != null || numero != null){
			queryBuilder.append("select distinct ");
			queryBuilder.append("p.parcelle ");
			queryBuilder.append("from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".");
			queryBuilder.append("parcelle p,");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".");
			queryBuilder.append("co_propriete_parcelle copropar, ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".");
			queryBuilder.append("proprietaire pro ");
			queryBuilder.append("where p.parcelle = copropar.parcelle ");
			queryBuilder.append("and copropar.comptecommunal = pro.comptecommunal ");
			queryBuilder.append(" and p.cgocommune = ? and p.ccosec = ? and p.dnupla = ? ");
			queryBuilder.append(" and p.cgocommune = pro.cgocommune and pro.app_nom_usage = ? ");
			queryBuilder.append(";");
			
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			parcelles = jdbcTemplate.queryForList(queryBuilder.toString(), commune,section,numero,proprietaire);
		}
		else{
			logger.info("Missing or empty input parameter");
		}
		return parcelles;
	}
	
	

}
