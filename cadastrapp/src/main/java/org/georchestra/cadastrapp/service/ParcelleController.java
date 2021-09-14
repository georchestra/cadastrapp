package org.georchestra.cadastrapp.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.cadastrapp.model.pdf.ExtFormResult;
import org.georchestra.cadastrapp.service.export.ExportHelper;
import org.georchestra.cadastrapp.service.constants.CadastrappConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Parcelle controller expose all rest service for plots information
 */
@Api( description="Récupération des informations de parcelles")
@Controller
public class ParcelleController extends CadController {

	static final Logger logger = LoggerFactory.getLogger(ParcelleController.class);
	
	@Autowired
	ExportHelper exportHelper;

	@ApiOperation(value = "Récupère la liste des parcelles")
	@RequestMapping(path = "/getParcelle", produces = {MediaType.APPLICATION_JSON_VALUE},  method = { RequestMethod.GET, RequestMethod.POST })
	/**
	 *  Works like {@link #getParcelle} 
	 */
	public	@ResponseBody List<Map<String, Object>> getParcelleEntrypoint(
		@RequestParam(name = "parcelle", required = false) final List<String> parcelleList, 
		@RequestParam(defaultValue = "0", required = false) int details, 
		@RequestParam(name ="cgocommune", required = false) String cgoCommune,
		@RequestParam(required = false) String ccopre, 
		@RequestParam(required = false) String ccosec, 
		@RequestParam(required = false) String dnupla,
		@RequestParam(required = false) String dnvoiri, 
		@RequestParam(name = "dlindic", required = false) String dindic, 
		@RequestParam(required = false) String cconvo, 
		@RequestParam(required = false) String dvoilib, 
		@RequestParam(name = "comptecommunal", required = false) final List<String> comptecommunalList, 
		@RequestParam(name = "unitefonciere", defaultValue = "0", required = false) int uf) throws SQLException {

		return getParcelle(parcelleList, details, cgoCommune, ccopre, ccosec, dnupla, dnvoiri, dindic, cconvo, dvoilib, comptecommunalList, uf);
	}

	
	/**
	 * 
	 * getParcelle
	 * 
	 * @param parcelleList
	 *            could be LIST if one or more element, if only one in the list,
	 *            this element could contains list of parcelleids separated by
	 *            space, comma, etc.. 
	 *            exemple ( '2014630103000AP0026', '2014630103000AP0027'
	 *            or '2014630103000AP0026 2014630103000AP0026' 
	 *            or '2014630103000AP0026,2014630103000AP0026'
	 *            or '2014630103000AP0026;2014630103000AP0026'
	 *            or '2014630103000AP0026' )
	 * @param details int default value 0
	 * 			0 for short details, 1 for full information
	 * 			if details = 0 , params 
	 * 			parcelle, cgocommune, dnvoiri, dindic, cconvo, dnupla, dvoilib, ccopre, ccosec, dcntpa
	 * 			will be displayed in JSON information
	 * @param cgocommune code geographique officil commune  like 630103 (codep + codir + cocom)
	 * 			cgocommune should be on 6 char
	 * @param ccopre prefix de section
	 * @param ccosec code de section
	 * @param dnupla code de parcelle dans une section définit
	 * @param dnvoiri
	 * @param dindic
	 * @param cconvo
	 * @param dvoilib
	 * @param comptecommunal id specific for a owner
	 * @param unitefonciere
	 * 
	 * @return List of parcelle information in List
	 * 
	 * @throws SQLException
	 */
	private List<Map<String, Object>> getParcelle(final List<String> parcelleList, int details,String cgoCommune, 
			String ccopre, String ccosec, String dnupla, String dnvoiri, String dindic, String cconvo, String dvoilib, final List<String> comptecommunalList, int uf) throws SQLException 
	{
		List<Map<String, Object>> parcellesResult = new ArrayList<Map<String, Object>>();
		
		// Search by Id Parcelle
		if (parcelleList != null && !parcelleList.isEmpty()) {

			List<String> parsedParcelleList = prepareParcelleList(parcelleList);
			parcellesResult = getParcellesById(parsedParcelleList, details, getUserCNILLevel());

			// Search by Proprietaire
		} else if (comptecommunalList != null && !comptecommunalList.isEmpty()) {

			parcellesResult = getParcellesByProprietaire(comptecommunalList, details, getUserCNILLevel());

			// Search by unitefonciere
		} else if (uf != 0) {

			parcellesResult = getParcellesByUniteFonciere(uf, details, getUserCNILLevel());

			// Search by attributes
		} else {
			List<String> queryParams = new ArrayList<String>();

			StringBuilder queryBuilder = new StringBuilder();
			boolean isWhereAdded = false;

			queryBuilder.append(createSelectParcelleQuery(details));
			isWhereAdded = createEqualsClauseRequest(isWhereAdded, queryBuilder, "cgocommune", cgoCommune, queryParams);
			isWhereAdded = createEqualsClauseRequest(isWhereAdded, queryBuilder, "ccopre", ccopre, queryParams);
			isWhereAdded = createEqualsClauseRequest(isWhereAdded, queryBuilder, "ccosec", ccosec, queryParams);
			isWhereAdded = createEqualsClauseRequest(isWhereAdded, queryBuilder, "dnupla", dnupla, queryParams);
			isWhereAdded = createEqualsClauseRequest(isWhereAdded, queryBuilder, "dnvoiri", dnvoiri, queryParams);
			isWhereAdded = createEqualsClauseRequest(isWhereAdded, queryBuilder, "dindic", dindic, queryParams);
			isWhereAdded = createEqualsClauseRequest(isWhereAdded, queryBuilder, "cconvo", cconvo, queryParams);
			isWhereAdded = createEqualsClauseRequest(isWhereAdded, queryBuilder, "dvoilib", dvoilib, queryParams);

			if (queryParams.size() > 1) {
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				parcellesResult = jdbcTemplate.queryForList(queryBuilder.toString(), queryParams.toArray());
			} else {
				logger.info("At least two parameters are required to get information from parcelle if not by id or by owners");
			}

		}

		return parcellesResult;
	}
	
	/**
	 * 
	 * @param uf
	 * @param details
	 * @param userCNILLevel
	 * @return
	 */
	private List<Map<String, Object>> getParcellesByUniteFonciere(int uf, int details, int userCNILLevel) {

		List<Map<String, Object>> parcelles = null;
		StringBuilder queryBuilder = new StringBuilder();

		// if search by dnuproList or comptecommunal
		// directly search in view parcelle
		if (uf != 0) {

			// force details 1 to get surfc
			queryBuilder.append(createSelectParcelleQuery(1));
			queryBuilder.append(", ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".uf_parcelle uf where uf.uf = ? ");
			queryBuilder.append(" and uf.parcelle = p.parcelle ");
			queryBuilder.append(" order by p.parcelle;");

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			parcelles = jdbcTemplate.queryForList(queryBuilder.toString(), uf);
		} else {
			logger.info("Missing or empty input parameter");
		}
		return parcelles;
	}
	
	/**
	 * 
	 * @param parcelleList
	 *            could be LIST if one or more element, if only one in the list,
	 *            this element could contains list of parcelleids separated by
	 *            space, comma, etc.. exemple ( '2014630103000AP0026',
	 *            '2014630103000AP0027' or '2014630103000AP0026
	 *            2014630103000AP0026' or
	 *            '2014630103000AP0026,2014630103000AP0026' or
	 *            '2014630103000AP0026;2014630103000AP0026' or
	 *            '2014630103000AP0026' )
	 * 
	 * @return List of parcelle id with only one parcelle by element
	 */
	private List<String> prepareParcelleList(List<String> parcelleList) {

		List<String> newParcelleList = new ArrayList<String>();

		// Try to split when only one element exist could be
		// '2014630103000AP0026 2014630103000AP0026'
		// or
		// '2014630103000AP0026;2014630103000AP0026'
		// or
		// '2014630103000AP0026,2014630103000AP0026'
		if (parcelleList != null && !parcelleList.isEmpty()) {
			if (parcelleList.size() == 1) {
				newParcelleList = Arrays.asList(parcelleList.get(0).split("\\s|;|,"));
			} else {
				newParcelleList = parcelleList;
			}
		}
		return newParcelleList;
	}

	/**
	 * 
	 * 
	 * @param parcelle
	 *            Id Parcelle unique in all country exemple :
	 *            2014630103000AP0025
	 * @param details
	 *            int default value 0 0 for short details, 1 for full
	 *            information
	 * @param userCNILLevel
	 *            (0,1 or 2) ie CNIL_0, CNIL_1, CNIL_2
	 * 
	 * @return List of parcelle information in List
	 * 
	 * @throws SQLException if an SQL exception occured
	 */
	public List<Map<String, Object>> getParcelleById(String parcelle, int details, int userCNILLevel) throws SQLException {

		StringBuilder queryBuilder = new StringBuilder();

		queryBuilder.append(createSelectParcelleQuery(details));
		queryBuilder.append(" where parcelle ='?';");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForList(queryBuilder.toString(), parcelle);

	}

	/**
	 * getParcellesById, given a list of parcelles ids and details level wanted,
	 * this method will return informations about parcelles from cadastrapp view
	 * 
	 * userCNILLevel will filter information than can be return or not
	 * 
	 * @param parcelleList
	 *            could be LIST if one or more element, if only one in the list,
	 *            this element could contains list of parcelleids separated by
	 *            space, comma, etc.. exemple ( '2014630103000AP0026',
	 *            '2014630103000AP0027' or '2014630103000AP0026
	 *            2014630103000AP0026' or
	 *            '2014630103000AP0026,2014630103000AP0026' or
	 *            '2014630103000AP0026;2014630103000AP0026' or
	 *            '2014630103000AP0026' )
	 * @param details
	 *            0 for short details, 1 for full information
	 * @param userCNILLevel
	 *            (0,1 or 2) ie CNIL_0, CNIL_1, CNIL_2
	 *            
	 * @return List of parcelle information in List
	 * 
	 * @throws SQLException if an SQL exception occured
	 */
	public List<Map<String, Object>> getParcellesById(List<String> parcelleList, int details, int userCNILLevel) throws SQLException {

		StringBuilder queryBuilder = new StringBuilder();

		queryBuilder.append(createSelectParcelleQuery(details));
		queryBuilder.append(createWhereInQuery(parcelleList.size(), "parcelle"));
		queryBuilder.append(";");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForList(queryBuilder.toString(), parcelleList.toArray());
	}

	/**
	 * 
	 * @param comptecommunal id specific for a owner
	 * @param details 1 to have detailed information
	 * @param userCNILLevel 0,1 or 2
	 * 
	 * @return List of parcelle information in List
	 */
	public List<Map<String, Object>> getParcellesByProprietaire(List<String> comptecommunal, int details, int userCNILLevel) {

		List<Map<String, Object>> parcelles = null;
		StringBuilder queryBuilder = new StringBuilder();

		// if search by dnuproList or comptecommunal
		// directly search in view parcelle
		if (comptecommunal != null && !comptecommunal.isEmpty()) {

			queryBuilder.append("(");
			queryBuilder.append(createSelectParcelleQuery(details));
			queryBuilder.append(", ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".proprietaire_parcelle proparc ");
			queryBuilder.append(createWhereInQuery(comptecommunal.size(), "proparc.comptecommunal"));
			queryBuilder.append(" and proparc.parcelle = p.parcelle ");
			queryBuilder.append(") UNION (");
			queryBuilder.append(createSelectParcelleQuery(details));
			queryBuilder.append(", ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".co_propriete_parcelle coproparc ");
			queryBuilder.append(createWhereInQuery(comptecommunal.size(), "coproparc.comptecommunal"));
			queryBuilder.append(" and coproparc.parcelle = p.parcelle ");
			queryBuilder.append(")");
			queryBuilder.append(";");

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			
			// Because we use query params, we need to duplicate comptecommunal value in array because we have 2 where in query
			Object[] ccArray = comptecommunal.toArray();
			Object[] queryParam = ArrayUtils.addAll(ccArray, ccArray);

			parcelles = jdbcTemplate.queryForList(queryBuilder.toString(), queryParam);

		} else {
			logger.info("Missing or empty input parameter");
		}
		return parcelles;
	}

	/**
	 * 
	 * @param int default value 0 0 for short details, 1 for full information if
	 *        details = 0 , params parcelle, cgocommune, dnvoiri, dindic,
	 *        cconvo, dnupla, dvoilib, ccopre, ccosec, dcntpa will be displayed
	 *        in JSON information
	 * 
	 * @return create the select String query with the list of parameter needed
	 *         depending on user rights and details neeeded This string will be
	 *         used in jdbc statement for final query
	 */
	private String createSelectParcelleQuery(int details) {

		StringBuilder selectQueryBuilder = new StringBuilder();
		selectQueryBuilder.append("select distinct ");
		selectQueryBuilder.append("p.parcelle, p.cgocommune, p.dnvoiri, p.dindic, p.cconvo, p.dvoilib, p.ccopre, p.ccosec, p.dnupla, p.dcntpa");

		if (details == 1) {
			selectQueryBuilder.append(" ,p.surfc");
			selectQueryBuilder.append(" from ");
			selectQueryBuilder.append(databaseSchema);
			selectQueryBuilder.append(".parcelleDetails p");
		} else {
			selectQueryBuilder.append(" from ");
			selectQueryBuilder.append(databaseSchema);
			selectQueryBuilder.append(".parcelle p");
		}

		return selectQueryBuilder.toString();
	}

	/**
	 * Service witch use csv file as input
	 * 
	 * @param roleList fromhttp headers information
	 * @param fileContent
	 *            parcelleId separated by space, ',' or ';'
	 * 
	 * @return Json data, with corresponding parcelleId information
	 * 
	 */
	@RequestMapping(path = "/fromParcellesFile", consumes = {"multipart/form-data" }, method = {RequestMethod.POST })
	public ResponseEntity getFromParcellesFile(
		@RequestParam(name = "filePath") String fileContent) {

		ResponseEntity response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		// space, , or ;
		String delimitersRegex = "[\\s\\;\\,\\n]";
		List<Map<String, Object>> parcellesResult = new ArrayList<Map<String, Object>>();

		BufferedReader br = new BufferedReader(new StringReader(fileContent));

		List<String> parcelleList = new ArrayList<String>();
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				// Empty lines
				if (!line.trim().isEmpty()) {
					// split line
					String[] parcelleIds = line.split(delimitersRegex);

					for (String parcelleId : parcelleIds) {

						if (logger.isDebugEnabled()) {
							logger.debug("Parcelle from the csv file : " + parcelleId);
						}

						if (parcelleId != null && parcelleId.length() >= parcelleLength && parcelleId.matches("^[0-9]{9,}.*")) {
							if (logger.isDebugEnabled()) {
								logger.debug("Added to parcelle list : " + parcelleId);
							}
							parcelleList.add(parcelleId.trim());
						}
					}
				}
			}

			// Avoid make request if no parcelle id is given
			if (parcelleList != null && !parcelleList.isEmpty()) {
				parcellesResult = getParcellesById(parcelleList, 0, getUserCNILLevel());
			} else {
				logger.warn("No information given to create csv");
			}

			// les forms ExtJs attendent du JSON sous format TEXT/HTML... (avec
			// success=true)
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(new ExtFormResult(true, parcellesResult));

			// Create response
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.TEXT_HTML);
			response = new ResponseEntity<>(json, headers, HttpStatus.OK);

		} catch (IOException e) {
			logger.error("Error while trying to read input data ", e);

		} catch (SQLException e) {
			logger.error("Error while trying to get information from database ", e);
		}
		return response;
	}

	/**
	 * Get export of parcelle for given comptecommunal
	 * 
	 * @param rolesList
	 *            make sure user have CNIL level
	 * @param details 1 to have detailed information
	 * @param city cgocommune information from form
	 * @param fileContent file content
	 * @return	form validation 
	 */
	@RequestMapping(path = "/fromProprietairesFile", consumes = {"multipart/form-data"}, method = {RequestMethod.POST })
	public ResponseEntity getFromProprietairesFile(
		@RequestParam(defaultValue = "0", required = false) int details, 
		@RequestParam(name = "filePath") String fileContent) {

		ResponseEntity response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		if (logger.isDebugEnabled()) {
			logger.debug("csv content : " + fileContent);
		}
		BufferedReader br = new BufferedReader(new StringReader(fileContent));

		// space, , or ;
		String delimitersRegex = "[\\s\\;\\,\\n]";

		List<String> proprietaireList = new ArrayList<String>();
		List<Map<String, Object>> ownersResult = new ArrayList<Map<String, Object>>();
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				String[] proprietaireIds = line.split(delimitersRegex);

				for (String proprietaireId : proprietaireIds) {

					if (logger.isDebugEnabled()) {
						logger.debug("CompteCommunal from the csv file : " + proprietaireId);
					}

					if (proprietaireId != null && proprietaireId.length() >= 8 && !proprietaireList.contains(proprietaireId) && proprietaireId.matches("^[0-9]{5,}.*")) {
						if (logger.isDebugEnabled()) {
							logger.debug("Added to CompteCommunal list : " + proprietaireId);
						}
						proprietaireList.add(proprietaireId.trim());
					}
				}
			}

			// Avoid call without parameter
			if (proprietaireList != null && !proprietaireList.isEmpty()) {
				ownersResult = getParcellesByProprietaire(proprietaireList, details, getUserCNILLevel());
			} else {
				logger.warn("No information given to get CompteCommunal information");
			}

			// les forms ExtJs attendent du JSON sous format TEXT/HTML... (avec success=true)
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(new ExtFormResult(true, ownersResult));

			// Create response
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.TEXT_HTML);
			response = new ResponseEntity<>(json, headers, HttpStatus.OK);


		} catch (IOException e) {
			logger.error("Error while trying to read input data ", e);
		}
		return response;
	}

	@ResponseBody
	@RequestMapping(path = "/getDnuplaList", produces = {MediaType.APPLICATION_JSON_VALUE}, method = { RequestMethod.GET})
	/**
	 *  Return only dnupla list from a section of a commune
	 *  
	 * @param cgocommune code commune INSEE
	 * @param ccopre prefix de section
	 * @param ccosec code de section
	 * @return list de dnupla 
	 * 
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getDnuplaList(
		@RequestParam(name= "cgocommune", required= false) String cgoCommune, 
		@RequestParam(required= false) String ccopre, 
		@RequestParam(required= false) String ccosec) throws SQLException {

		List<Map<String, Object>> dnuplaList = null;
		List<String> queryParams = new ArrayList<String>();
		boolean isWhereAdded = false;

		StringBuilder dnuplaQueryBuilder = new StringBuilder();
		dnuplaQueryBuilder.append("select distinct dnupla from ");
		dnuplaQueryBuilder.append(databaseSchema);
		dnuplaQueryBuilder.append(".parcelle");
		isWhereAdded = createEqualsClauseRequest(isWhereAdded, dnuplaQueryBuilder, "cgocommune", cgoCommune, queryParams);
		isWhereAdded = createEqualsClauseRequest(isWhereAdded, dnuplaQueryBuilder, "ccopre", ccopre, queryParams);
		createEqualsClauseRequest(isWhereAdded, dnuplaQueryBuilder, "ccosec", ccosec, queryParams);
		dnuplaQueryBuilder.append("ORDER BY dnupla ASC");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		dnuplaList = jdbcTemplate.queryForList(dnuplaQueryBuilder.toString(), queryParams.toArray());

		return dnuplaList;
	}

	@RequestMapping(path = "/exportParcellesAsCSV", produces = {"text/csv;charset=utf-8"},  method = {RequestMethod.POST })
	/**
	 * Create a csv file from given parcelles id
	 * 
	 * @param parcelles list of parcelle separated by a coma
	 * 
	 * @return csv containing list of owners
	 * 
	 * @throws SQLException
	 */
	public ResponseEntity exportParcellesAsSCV(
			@RequestParam String parcelles) throws SQLException {
		
		// Create empty content
		ResponseEntity response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
			String entete = "parcelle; commune;voie_adr;voie_adr_cplmt;voie_type;voie_nom;section_prefixe;section;parcelle_num;contenance";
			
			String[] parcelleArray = StringUtils.split(parcelles, ',');
			List<String> parcelleList = new ArrayList<String>();
			CollectionUtils.addAll(parcelleList, parcelleArray);
			
			if(parcelleList != null && !parcelleList.isEmpty()){
				
				logger.debug("Nb of parcelles to search in : " + parcelleList.size());

				// Get value from database
				List<Map<String,Object>> parcellesResult = getParcellesById(parcelleList, 0, getUserCNILLevel());
				
				File file = null;
				try{
					file = exportHelper.createCSV(parcellesResult, entete);
					
					// build csv response
					// build csv response
					HttpHeaders headers = new HttpHeaders();
					headers.setContentDispositionFormData("filename", file.getName());
					response = new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);

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
	
		return response;
	}

}
