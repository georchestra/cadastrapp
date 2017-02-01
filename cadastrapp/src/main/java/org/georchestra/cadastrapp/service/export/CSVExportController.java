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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.georchestra.cadastrapp.service.CadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVExportController extends CadController {

	static final Logger logger = LoggerFactory.getLogger(CSVExportController.class);

	static final char DELIMITER = '\n';
	static final char SEPARATOR = ';';

	/**

	 * @param values
	 *            List<String> contains all data to export, each list of elements are separated by |
	 * 
	 * @return Rest Response containing CSV file with name
	 *         export-"currentDateTime".csv When header exist, if value have not
	 *         same number of value, the corresponding line won't be added to
	 *         csv
	 */
	@GET
	@Path("/exportAsCsv")
	@Produces({ "text/csv" })
	public Response cSVExport(@QueryParam("data") List<String> values) {

		ResponseBuilder response = Response.serverError();

		if (values != null && !values.isEmpty()) {
			

			if (logger.isDebugEnabled()) {
				logger.debug("Export CSV,  given values: " + values);
			}
				
			String tempFolder = CadastrappPlaceHolder.getProperty("tempFolder");
			
			// File with current time
			final String csvFileName = tempFolder + File.separator + "export-" + new Date().getTime() + ".csv";
			File file = null;

			try {
				// Create file
				file = new File(csvFileName);
				FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
			
				// For each line
				for (String line : values) {

					// Debug information
					if (logger.isDebugEnabled()) {
						logger.debug("Export CSV - value : " + line);
					}
					
					line = line.replace(',', SEPARATOR);
					fileWriter.append(line);
					fileWriter.append(DELIMITER);
				}

				// release file
				fileWriter.flush();
				fileWriter.close();

				response = Response.ok((Object) file);
				response.header("Content-Disposition", "attachment; filename=" + file.getName());

			} catch (IOException e) {
				logger.error("Error while creating CSV files ",  e);
			} finally {
				if (file != null) {
					file.deleteOnExit();
				}
			}
		}

		return response.build();
	}

}
