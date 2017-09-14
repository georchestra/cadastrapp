package org.georchestra.cadastrapp.purge;

 
import java.io.File;

import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("purgeJob")
public class PurgeJob {
	
	final static Logger logger = LoggerFactory.getLogger(PurgeJob.class);
	
	private int hours;
 
	/**
	 * Purge all files older than given number of hours from temp folder
	 */
    public void purge() {
        logger.info("Launch purge");
        
        // Get temporary folder to purge
        String tempFolderName = CadastrappPlaceHolder.getProperty("tempFolder");
        
        // Set before time to purge
        final long purgeTime = System.currentTimeMillis() - (hours * 60 * 60 * 1000);
        int nbFilesDeleted = 0;
        
        File tempFolder = new File(tempFolderName);

        if(tempFolder.exists()){
        	
        	// Check all file from temp folder
	        for (File f : tempFolder.listFiles()) {
	        	// If file older than hours deleted it
	        	if(f.isFile() && f.lastModified() <  purgeTime ){
	        		logger.debug("Purge file : " + f.getName());
	        		f.delete();
	        		nbFilesDeleted++;
	        	}
	        }
        }  
        
        logger.info(nbFilesDeleted + " were deleted");
    }

	/**
	 * @return the hours
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * @param hours the hours to set
	 */
	public void setHours(int hours) {
		this.hours = hours;
	}
    
   
}
