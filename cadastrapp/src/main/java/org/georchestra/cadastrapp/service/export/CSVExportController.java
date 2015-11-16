package org.georchestra.cadastrapp.service.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.georchestra.cadastrapp.service.CadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVExportController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(CSVExportController.class);

	/**
	 * 
	 * @param headers
	 * @param data contains all data to export
	 * @return Rest Response containing CSV file
	 */
	@GET
	@Path("/exportAsCsv")
	@Produces({ "text/csv" })
	public Response CSVExport(@Context HttpHeaders headers, @QueryParam("data") List<String> values) {

		ResponseBuilder response = Response.serverError();

		final String csvFileName = tempFolder + File.separator + "export-" + new Date().getTime() + ".csv";

		final String DELIMITER = "\n";
		
		try {
			File file = new File(csvFileName);
			
			FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());

			for (String value : values){
				fileWriter.append(value);
				fileWriter.append(DELIMITER);
			}

			fileWriter.flush();
			fileWriter.close();

			response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=" + file.getName());

		} catch (IOException e) {
			logger.error("Error while creating CSV files : " + e.getMessage());
		}

		return response.build();
	}

}
