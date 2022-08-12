package org.georchestra.cadastrapp.service.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.georchestra.cadastrapp.service.CadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CSVExportController extends CadController {

	static final Logger logger = LoggerFactory.getLogger(CSVExportController.class);
	final static Logger docLogger = LoggerFactory.getLogger("org.georchestra.cadastrapp.loggers.documents");
	
	static final char DELIMITER = '\n';
	static final char SEPARATOR = ';';

	/**

	 * @param values
	 *             list containing all data to export, each list of elements are separated by |
	 * 
	 * @return Rest Response containing CSV file with name
	 *         export-"currentDateTime".csv When header exist, if value have not
	 *         same number of value, the corresponding line won't be added to
	 *         csv
	 */
	@RequestMapping(path = "/exportAsCsv", produces = "text/csv", method= {RequestMethod.GET})
	public ResponseEntity<byte[]> cSVExport(@RequestParam(name = "data") List<String> values) {

		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);

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

				ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
					.filename(file.getName())
					.build();

				HttpHeaders headers = new HttpHeaders();
				headers.setContentDisposition(contentDisposition);

				response = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);	
				
				docLogger.info("Export CSV - ComptesCommunaux - ["+values+"]");
			} catch (IOException e) {
				logger.error("Error while creating CSV files ",  e);
			} finally {
				if (file != null) {
					file.deleteOnExit();
				}
			}
		}

		return response;
	}

}
