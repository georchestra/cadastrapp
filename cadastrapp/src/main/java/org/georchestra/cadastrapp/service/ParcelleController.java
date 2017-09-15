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

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.georchestra.cadastrapp.model.pdf.ExtFormResult;
import org.georchestra.cadastrapp.service.export.ExportHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Parcelle controller expose all rest service for plots information
 */
public class ParcelleController extends CadController {

	static final Logger logger = LoggerFactory.getLogger(ParcelleController.class);
	
	@Autowired
	ExportHelper exportHelper;

	@GET
	@Path("/getParcelle")
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 *  Works like {@link #getParcelle} 
	 */
	public List<Map<String, Object>> getParcelleGET(@Context HttpHeaders headers, @QueryParam("parcelle") final List<String> parcelleList, @DefaultValue("0") @QueryParam("details") int details, @QueryParam("cgocommune") String cgoCommune, @QueryParam("ccopre") String ccopre, @QueryParam("ccosec") String ccosec, @QueryParam("dnupla") String dnupla,
			@QueryParam("dnvoiri") String dnvoiri, @QueryParam("dlindic") String dindic, @QueryParam("cconvo") String cconvo, @QueryParam("dvoilib") String dvoilib, @QueryParam("comptecommunal") final List<String> comptecommunalList, @DefaultValue("0") @QueryParam("unitefonciere") int uf) throws SQLException {

		return getParcelle(headers, parcelleList, details, cgoCommune, ccopre, ccosec, dnupla, dnvoiri, dindic, cconvo, dvoilib, comptecommunalList, uf);
	}
	
	@POST
	@Path("/getParcelle")
	@Produces(MediaType.APPLICATION_JSON)
	/**
	 * Works like {@link #getParcelle}  
	 */
	public List<Map<String, Object>> getParcellePOST(@Context HttpHeaders headers, 
			@FormParam("parcelle") final List<String> parcelleList, 
			@DefaultValue("0") @FormParam("details") int details, 
			@FormParam("cgocommune") String cgoCommune, 
			@FormParam("ccopre") String ccopre, 
			@FormParam("ccosec") String ccosec, 
			@FormParam("dnupla") String dnupla,
			@FormParam("dnvoiri") String dnvoiri, 
			@FormParam("dlindic") String dindic, 
			@FormParam("cconvo") String cconvo,
			@FormParam("dvoilib") String dvoilib, 
			@FormParam("comptecommunal") final List<String> comptecommunalList, 
			@DefaultValue("0") @FormParam("unitefonciere") int uf) throws SQLException {

		return getParcelle(headers, parcelleList, details, cgoCommune, ccopre, ccosec, dnupla, dnvoiri, dindic, cconvo, dvoilib, comptecommunalList, uf);
		
	}
	
	
	/**
	 * 
	 * getParcelle
	 * 
	 * @param headers http headers, used to get ldap role information about the user group
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
	 * @param comptecommunal
	 * @param unitefonciere
	 * 
	 * @return List of parcelle information in List
	 * 
	 * @throws SQLException
	 */
	private List<Map<String, Object>> getParcelle(HttpHeaders headers,final List<String> parcelleList, int details,String cgoCommune, 
			String ccopre, String ccosec, String dnupla, String dnvoiri, String dindic, String cconvo, String dvoilib, final List<String> comptecommunalList, int uf) throws SQLException 
	{
		List<Map<String, Object>> parcellesResult = new ArrayList<Map<String, Object>>();
		
		// Search by Id Parcelle
		if (parcelleList != null && !parcelleList.isEmpty()) {

			List<String> parsedParcelleList = prepareParcelleList(parcelleList);
			parcellesResult = getParcellesById(parsedParcelleList, details, getUserCNILLevel(headers));

			// Search by Proprietaire
		} else if (comptecommunalList != null && !comptecommunalList.isEmpty()) {

			parcellesResult = getParcellesByProprietaire(comptecommunalList, details, getUserCNILLevel(headers));

			// Search by unitefonciere
		} else if (uf != 0) {

			parcellesResult = getParcellesByUniteFonciere(uf, details, getUserCNILLevel(headers));

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
			queryBuilder.append(";");

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
	 * @throws SQLException
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
	 * @throws SQLException
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
	 * @param comptecommunal
	 * @param details
	 * @param userCNILLevel
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
	 * @param headers
	 * @param fileContent
	 *            parcelleId separated by space, ',' or ';'
	 * 
	 * @return Json data, with corresponding parcelleId information
	 * 
	 */
	@POST
	@Path("/fromParcellesFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response getFromParcellesFile(@Context HttpHeaders headers, @FormParam("filePath") String fileContent) {

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
				parcellesResult = getParcellesById(parcelleList, 0, getUserCNILLevel(headers));
			} else {
				logger.warn("No information given to create csv");
			}

			// les forms ExtJs attendent du JSON sous format TEXT/HTML... (avec
			// success=true)
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(new ExtFormResult(true, parcellesResult));
			return Response.ok(json, MediaType.TEXT_HTML).build();

		} catch (IOException e) {
			logger.error("Error while trying to read input data ", e);
			return Response.serverError().build();

		} catch (SQLException e) {
			logger.error("Error while trying to get information from database ", e);
			return Response.serverError().build();
		}
	}

	/**
	 * Get export of parcelle for given comptecommunal
	 * 
	 * @param headers
	 *            make sure user have CNIL level
	 * @param details
	 * @param city
	 * @param fileContent
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/fromProprietairesFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response getFromProprietairesFile(@Context HttpHeaders headers, @DefaultValue("0") @FormParam("details") int details, @FormParam("cgocommune") String city, @FormParam("filePath") String fileContent) {

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
						logger.debug("Parcelle from the csv file : " + proprietaireId);
					}

					if (proprietaireId != null && proprietaireId.length() >= 8 && !proprietaireList.contains(proprietaireId) && proprietaireId.matches("^[0-9]{5,}.*")) {
						if (logger.isDebugEnabled()) {
							logger.debug("Added to parcelle list : " + proprietaireId);
						}
						proprietaireList.add(proprietaireId.trim());
					}
				}
			}

			// Avoid call without parameter
			if (proprietaireList != null && !proprietaireList.isEmpty()) {
				ownersResult = getParcellesByProprietaire(proprietaireList, details, getUserCNILLevel(headers));
			} else {
				logger.warn("No information given to get Parcelle information");
			}

			// les forms ExtJs attendent du JSON sous format TEXT/HTML... (avec success=true)
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(new ExtFormResult(true, ownersResult));
			return Response.ok(json, MediaType.TEXT_HTML).build();

		} catch (IOException e) {
			logger.error("Error while trying to read input data ", e);
			return Response.serverError().build();
		}
	}

	@GET
	@Path("/getDnuplaList")
	@Produces("application/json")
	/**
	 *  Return only dnupla list from a section of a commune
	 *  
	 * @param headers http headers, used to get ldap role information about the user group
	 * @param cgocommune code geographique officiel commune  like 630103 (codep + codir + cocom)
	 * 					cgocommune should be on 6 char
	 * @param ccopre prefix de section
	 * @param ccosec code de section
	 * @return list de dnupla 
	 * 
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getDnuplaList(@Context HttpHeaders headers, @QueryParam("cgocommune") String cgoCommune, @QueryParam("ccopre") String ccopre, @QueryParam("ccosec") String ccosec) throws SQLException {

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
	
	@POST
	@Path("/exportParcellesAsCSV")
	@Produces("text/csv")
	/**
	 * Create a csv file from given parcelles id
	 * 
	 * @param headers Used to filter displayed information
	 * @param parcelles list of parcelle separated by a coma
	 * 
	 * @return csv containing list of owners
	 * 
	 * @throws SQLException
	 */
	public Response exportParcellesAsSCV(
			@Context HttpHeaders headers,
			@FormParam("parcelles") String parcelles) throws SQLException {
		
		// Create empty content
		ResponseBuilder response = Response.noContent();
		
			String entete = "Identifiant de parcelle;Commune;N° de voirie;Indice de répétition;Nature de voie;Libellé de voie;Préfixe de section;Section;N° de plan;Contenance DGFiP en m²";
			
			String[] parcelleArray = StringUtils.split(parcelles, ',');
			List<String> parcelleList = new ArrayList<String>();
			CollectionUtils.addAll(parcelleList, parcelleArray);
			
			if(parcelleList != null && !parcelleList.isEmpty()){
				
				logger.debug("Nb of parcelles to search in : " + parcelleList.size());

				// Get value from database
				List<Map<String,Object>> parcellesResult = getParcellesById(parcelleList, 0, getUserCNILLevel(headers));
				
				File file = null;
				try{
					file = exportHelper.createCSV(parcellesResult, entete);
					
					// build csv response
					response = Response.ok((Object) file);
					response.header("Content-Disposition", "attachment; filename=" + file.getName());
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
	
		return response.build();
	}

}
