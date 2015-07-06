package org.georchestra.cadastrapp.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
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

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.georchestra.cadastrapp.model.ExtFormResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

@Path("/getParcelle")
public class ParcelleController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(ParcelleController.class);
	
	@GET
	@Produces("application/json")
	/**
	 * 
	 * @param headers
	 * @param parcelleList
	 * @param details
	 * @param ccodep
	 * @param ccodir
	 * @param ccocom
	 * @param ccopre
	 * @param ccosec
	 * @param dnupla
	 * @param dnvoiri
	 * @param dindic
	 * @param cconvo
	 * @param dvoilib
	 * @param dnomlp
	 * @param dprnlp
	 * @param dnomcp
	 * @param dprncp
	 * @param dnuproList
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getParcelleList(
			@Context HttpHeaders headers,
			@QueryParam("parcelle") final List<String> parcelleList,
			@DefaultValue("0") @QueryParam("details") String details,
			@QueryParam("ccodep") String ccodep,
			@QueryParam("ccodir") String ccodir,
			@QueryParam("ccocom") String ccocom,
			@QueryParam("ccopre") String ccopre,
			@QueryParam("ccosec") String ccosec,
			@QueryParam("dnupla") String dnupla,
			@QueryParam("dnvoiri") String dnvoiri,
			@QueryParam("dlindic") String dindic,
			@QueryParam("cconvo") String cconvo,
			@QueryParam("dvoilib") String dvoilib,
			@QueryParam("dnomlp") String dnomlp,
			@QueryParam("dprnlp") String dprnlp,
			@QueryParam("dnomcp") String dnomcp,
			@QueryParam("dprncp") String dprncp,
			@QueryParam("dnupro") final List<String> dnuproList) throws SQLException {

		List<Map<String, Object>> parcellesResult = null;
		
		// Search by Id Parcelle
		if (parcelleList != null && !parcelleList.isEmpty()) {
			
			parcellesResult = getParcellesById(parcelleList, details, getUserCNILLevel(headers));

		// Search by Id Proprietaire
		} else if (dnuproList != null && !dnuproList.isEmpty()) {
					
			parcellesResult = getParcellesByProprietaireId(dnuproList, details, getUserCNILLevel(headers));

		// Search by attributes
		} else {
			StringBuilder queryBuilder = new StringBuilder();
			
			queryBuilder.append(createSelectParcelleQuery(details, getUserCNILLevel(headers)));
	
			queryBuilder.append(" from ");
			queryBuilder.append(databaseSchema);
			queryBuilder.append(".parcelle");
			
			queryBuilder.append(createEqualsClauseRequest("ccodep", ccodep));
			queryBuilder.append(createEqualsClauseRequest("ccodir", ccodir));
			queryBuilder.append(createEqualsClauseRequest("ccocom", ccocom));
			queryBuilder.append(createEqualsClauseRequest("ccopre", ccopre));
			queryBuilder.append(createEqualsClauseRequest("ccosec", ccosec));
			queryBuilder.append(createEqualsClauseRequest("dnupla", dnupla));
			queryBuilder.append(createEqualsClauseRequest("dnvoiri", dnvoiri));
			queryBuilder.append(createEqualsClauseRequest("dindic", dindic));
			queryBuilder.append(createEqualsClauseRequest("cconvo", cconvo));
			queryBuilder.append(createEqualsClauseRequest("dvoilib", dvoilib));
			queryBuilder.append(createEqualsClauseRequest("dnomlp", dnomlp));
			queryBuilder.append(createEqualsClauseRequest("dprnlp", dprnlp));
			queryBuilder.append(createEqualsClauseRequest("dnomcp", dnomcp));
			queryBuilder.append(createEqualsClauseRequest("dprncp", dprncp));
			queryBuilder.append(finalizeQuery());
					
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			parcellesResult = jdbcTemplate.queryForList(queryBuilder.toString());
		}

		return parcellesResult;
	}

	/**
	 * @param parcelle
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getParcelleById(String parcelle, String details, int userCNILLevel) throws SQLException {

		List<Map<String, Object>> parcelles = null;

		StringBuilder queryBuilder = new StringBuilder();
		
		queryBuilder.append(createSelectParcelleQuery(details, userCNILLevel));
		
		queryBuilder.append(" from ");
		queryBuilder.append(databaseSchema);
		queryBuilder.append(".parcelle");
		queryBuilder.append(" where parcelle =' ");
		queryBuilder.append(parcelle);
		queryBuilder.append(" ';");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		parcelles = jdbcTemplate.queryForList(queryBuilder.toString());

		return parcelles;
	}
	
	
	/**
	 * getParcellesById, given a list of parcelles ids and details level wanted, 
	 * 	this method will return informations about parcelles from cadastrapp view
	 * 
	 * userCNILLevel will filter information than can be return or not 
	 * 
	 * @param parcelleList could be LIST if one or more element, if only one in the list, this element could contains list of parcelleids separated by space
	 * 			 			exemple ( '2014630103000AP0026', '2014630103000AP0027' or '2014630103000AP0026 2014630103000AP0026' or '2014630103000AP0026'
	 * @param details 0 for short details, 1 for full information
	 * @param userCNILLevel (0,1 or 2) ie CNIL_0, CNIL_1, CNIL_2
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getParcellesById(List<String> parcelleList, String details, int userCNILLevel) throws SQLException {

		List<Map<String, Object>> parcelles = null;
		
		StringBuilder queryBuilder = new StringBuilder();
		
		queryBuilder.append(createSelectParcelleQuery(details, userCNILLevel));
		
		queryBuilder.append(" from ");

		queryBuilder.append(databaseSchema);
		queryBuilder.append(".parcelle");
		queryBuilder.append(" where parcelle IN (");
		queryBuilder.append(createInQueryParcelles(parcelleList));
		queryBuilder.append(");");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		parcelles = jdbcTemplate.queryForList(queryBuilder.toString());

		return parcelles;
	}
	
	
	/**
	 * Given a list of String, this method will gives you a string that can be use in a IN SQL statement
	 * 
	 * 	exemple: 
	 * 			{2014630103000AP0026,2014630103000AP0027,2014630103000AP0028,2014630103000AP0029}
	 * 			will give you 
	 * 			'2014630103000AP0026','2014630103000AP0027','2014630103000AP0028','2014630103000AP0029'
	 * 
	 * Same for specific needs when copy paste from eclipe :
	 * 
	 * 			{'2014630103000AP0026 2014630103000AP0027 2014630103000AP0028 2014630103000AP0029',''}
	 * 			will give you 
	 * 			'2014630103000AP0026','2014630103000AP0027','2014630103000AP0028','2014630103000AP0029'
	 * 
	 * @param values List<String>
	 * 
	 * @return String that can be add to IN SQL query
	 */
	public String createInQueryParcelles(List<String> values) {

		StringBuilder listToString = new StringBuilder();

		if (values != null && !values.isEmpty()) {
			
			// Case of copy paste for excel 
			// only one element in list but several parcelle id separated by special char
			if (values.size() ==1){
				listToString.append("'");
				listToString.append(values.get(0).replaceAll("\\s","','"));
				listToString.append("'");
			}
			else{
				// Normal case	
				for (String value : values) {
					listToString.append("'");
					listToString.append(value);
					listToString.append("',");
				}
				// remove last coma check not empty
				listToString.deleteCharAt(listToString.length() - 1);
			}
				
			logger.debug("List to String : " + listToString);

		}

		return listToString.toString();
	}
	
	
	/**
	 * 
	 * 
	 * @param proprietaireList
	 * @param details
	 * @param userCNILLevel
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getParcellesByProprietaireId(List<String> proprietaireList, String details, int userCNILLevel) throws SQLException {

		List<Map<String, Object>> parcelles = null;

		StringBuilder queryBuilder = new StringBuilder();
		
		queryBuilder.append(createSelectParcelleQuery(details, userCNILLevel));
		
		queryBuilder.append(" from ");

		queryBuilder.append(databaseSchema);
		queryBuilder.append(".parcelle");
		queryBuilder.append(" where dnupro IN (");
		queryBuilder.append(createListToStringQuery(proprietaireList));
		queryBuilder.append(");");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		parcelles = jdbcTemplate.queryForList(queryBuilder.toString());

		return parcelles;
	}
	
	
	
	/**
	 * 
	 * @param details
	 * @param userCNILLevel
	 * @return
	 */
	private String createSelectParcelleQuery(String details, int userCNILLevel) {
		
		StringBuilder selectQueryBuilder = new StringBuilder();
		selectQueryBuilder.append("select ");

		if (details.equals("0")) {
			selectQueryBuilder.append("dnupla");
		} else {
			selectQueryBuilder.append("parcelle, ccodep, ccodir, ccocom, ccopre, ccosec, dnupla, dnvoiri, dindic, cconvo, dvoilib");

			// TODO userlevel
			if (userCNILLevel > 1) {
				selectQueryBuilder.append(", dnupro ");
			}
		}
		
		return selectQueryBuilder.toString();
	}
	
    
    @POST
    @Path("/fromParcellesFile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response getFromParcellesFile(
			@Context HttpHeaders headers,
			@DefaultValue("0") @FormParam("details") String details,
    		@FormParam("ccoinsee") String city,
    		@FormParam("filePath") String fileContent) throws Exception {
    	
    	BufferedReader br = new BufferedReader(new StringReader(fileContent));
    	
    	List<String> parcelleList = new ArrayList<String>();
    	String parcelleId = null;
    	while ((parcelleId = br.readLine()) != null) {
    		if (!parcelleId.trim().isEmpty()) {
    			parcelleList.add(parcelleId.trim());
    		}
		}
    	
    	List<Map<String, Object>> parcellesResult = getParcellesById(parcelleList, details, getUserCNILLevel(headers));
	    	
    	//les forms ExtJs attendent du JSON sous format TEXT/HTML... (avec success=true)
    	ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    	String json = ow.writeValueAsString( new ExtFormResult(true, parcellesResult));
    	return Response.ok(json, MediaType.TEXT_HTML).build();
    }
    
    @POST
    @Path("/fromProprietairesFile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response getFromProprietairesFile(
			@Context HttpHeaders headers,
			@DefaultValue("0") @FormParam("details") String details,
    		@FormParam("ccoinsee") String city,
    		@FormParam("filePath") String fileContent) throws Exception {
    	
    	BufferedReader br = new BufferedReader(new StringReader(fileContent));
    	
    	List<String> proprietaireList = new ArrayList<String>();
    	String proprietaireId = null;
    	while ((proprietaireId = br.readLine()) != null) {
    		if (!proprietaireId.trim().isEmpty()) {
    			proprietaireList.add(proprietaireId.trim());
    		}
		}
    	
    	List<Map<String, Object>> parcellesResult = getParcellesByProprietaireId(proprietaireList, details, getUserCNILLevel(headers));
	    	
    	//les forms ExtJs attendent du JSON sous format TEXT/HTML... (avec success=true)
    	ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    	String json = ow.writeValueAsString( new ExtFormResult(true, parcellesResult));
    	return Response.ok(json, MediaType.TEXT_HTML).build();
    }
    
    
    @GET
    @Path("/toFile")
    public Response getProprietairesListToFile(
			@Context HttpHeaders headers,
    		@FormParam("parcelle") String parcelle,
    		@FormParam("data") String withData) {
    	
    	//TODO : fichier de test
    	File file = new File("/home/gfi/test.pdf");
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=BP-" + parcelle + "-" + withData + file.getName().substring(file.getName().lastIndexOf(".")) );
		return response.build();
    }
}
