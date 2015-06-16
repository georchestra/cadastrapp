package org.georchestra.cadastrapp.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

@Path("/getFicheInfoUniteFonciere")
public class FicheInfoCadastreController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(FicheInfoCadastreController.class);

	@GET
	@Produces("application/json")
	/**
	 * 
	 * @param headers
	 * @param parcelle
	 * @return
	 */
	public List<Map<String, Object>> getInformationCadastrale(@Context HttpHeaders headers, 
			@QueryParam("parcelle") String parcelle,
			@QueryParam("onglet") int onglet) {

		List<Map<String, Object>> information = null;

		StringBuilder queryBuilder = new StringBuilder();

		// CNIL niveau 0
		queryBuilder.append("select ccodep, ccodir, ccocom, libcom, ccopre, ccosec, dnupla, dnvoiri, dindic, cconvo, dvoilib, supf, gparbat, gurbpa");

		// CNIL Niveau 1
		queryBuilder.append(", dnupro, dnomlp, dprnlp, expnee, dnomcp, dpmcp, dlign3, dlign4, dlign5, dlign6, dldnss, jdatnss, ccodro, ccodro_lib");
		queryBuilder.append(" from ");

		queryBuilder.append(databaseSchema);
		queryBuilder.append(".parcelle");

		queryBuilder.append(addAuthorizationFiltering(headers));
		queryBuilder.append(finalizeQuery());

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		information = jdbcTemplate.queryForList(queryBuilder.toString());

		// Return value providers will convert to JSON
		return information;
	}

}
