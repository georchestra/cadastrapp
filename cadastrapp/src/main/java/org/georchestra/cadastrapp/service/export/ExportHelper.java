package org.georchestra.cadastrapp.service.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExportHelper {

	final static Logger logger = LoggerFactory.getLogger(ExportHelper.class);

	final String tempFolder = CadastrappPlaceHolder.getProperty("tempFolder");

	// Used to create CSV
	final static char DELIMITER = '\n';
	final static char SEPARATOR = ';';

	/**
	 *  Create a csv file with given data and entete
	 *  
	 * @param data List<Map<String, Object>>
	 * @param entete String, corresponding to the first line of the csv
	 * @return File
	 * @throws IOException
	 */
	public File createCSV(List<Map<String, Object>> data, String entete) throws IOException {

		// Parse value to have only once a comptecommunal, but with several
		// parcelle
		// at this time there is one comptecommunal for each parcelle
		String tempFolder = CadastrappPlaceHolder.getProperty("tempFolder");

		// File with current time
		final String csvFileName = tempFolder + File.separator + "export-" + new Date().getTime() + ".csv";
		File file = null;

		// Create file
		file = new File(csvFileName);
		FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());

		fileWriter.append(entete);
		fileWriter.append(DELIMITER);

		logger.debug("Nb of entries : " + data.size());
		// For each line
		for (Map<String, Object> proprietaire : data) {

			StringBuffer lineBuffer = new StringBuffer();
			for (Map.Entry<String, Object> entry : proprietaire.entrySet()) {
				lineBuffer.append(entry.getValue());
				lineBuffer.append(SEPARATOR);
			}

			// Debug information
			if (logger.isDebugEnabled()) {
				logger.debug("Export CSV - value : " + lineBuffer.toString());
			}

			fileWriter.append(lineBuffer.toString());
			fileWriter.append(DELIMITER);
		}

		// release file
		fileWriter.flush();
		fileWriter.close();
		
		return file;

	}

}
