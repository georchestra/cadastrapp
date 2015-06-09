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
	 * @param parcelle
	 * @param details
	 * @param ccodep
	 * @param ccodir
	 * @param ccocom
	 * @param ccopre
	 * @param ccosec
	 * @param dnupla
	 * @param dnvoiri
	 * @param dlindic
	 * @param cconvo
	 * @param dvoilib
	 * @param dnomlp
	 * @param dprnlp
	 * @param dnomcp
	 * @param dprncp
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
			// Check mandatory params
			List<String> mandatoryParameters = new ArrayList<String>();
			mandatoryParameters.add(ccodep);
			mandatoryParameters.add(ccocom);
		
			// Avoid to do request if mandatory parameters are not set
			if (checkAreMandatoryParametersValid(mandatoryParameters)){
				
				StringBuilder queryBuilder = new StringBuilder();
				
				queryBuilder.append(createSelectParcelleQuery(details, getUserCNILLevel(headers)));

				queryBuilder.append(" from ");
				// TODO replace this by configuration
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
			else{
				logger.error("Missing Mandatory parameters");
			}
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
	 * @param parcelle
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
		queryBuilder.append(createListToStringQuery(parcelleList));
		queryBuilder.append(");");

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		parcelles = jdbcTemplate.queryForList(queryBuilder.toString());

		return parcelles;
	}
	
	
	/** 
	 * @param parcelle
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
