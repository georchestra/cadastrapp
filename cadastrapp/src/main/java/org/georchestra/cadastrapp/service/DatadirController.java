package org.georchestra.cadastrapp.service;

import org.apache.commons.codec.binary.Base64;
import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class DatadirController extends CadController {
	
	static final Logger logger = LoggerFactory.getLogger(DatadirController.class);
	
	/**
	 * Get datadir path from environment variable
	 * @return entire path as String 
	 */
	@Value("#{systemProperties['georchestra.datadir']}")
	private String dataDirPath;
	
	/**
	 * Get file from datadir
	 * @param targetPath path for given file from datadir path as root 
	 * 
	 * @return file from file system if exist as File
	 */
	private File getFileFromDataDir(String targetPath){
		
		// Check if cadastrapp datadir value  is set
		if(dataDirPath != null){
			String imageUrl;		
			
			imageUrl = dataDirPath + File.separator + "cadastrapp" + File.separator + targetPath;
			File file = new File(imageUrl);
			if(!file.exists()){
				return null;			
			}
			return file;		
		}else{
			logger.error("Cadastrapp image datadir not set for basemap thumbnails");
			return null;
		}	
	}
	
	/**
	 * Encode file into base64 string
	 * @param File file to be encode as base64 data
	 * @return String as base64
	 */
	private String encodeAsBase64(File file) {
		try {
			FileInputStream fileInputStreamReader = new FileInputStream(file);
			
			byte[] bytes = new byte[(int)file.length()];
			
			fileInputStreamReader.read(bytes);
			fileInputStreamReader.close();
			// encod file as base64
			String encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");		
			return encodedfile;
		}
		catch (Exception e) {
			logger.error("Fail to encod file as base64");
			return null;
		}				
	}		
	
	/**
	 * Get image name to be encoded to base64 data
	 * @param String nameImage name file
	 * @return String encoded file as base64 
	 */
	private String imageToString(String nameImage){
		try{			
			File fileToEncode = getFileFromDataDir(CadastrappPlaceHolder.getProperty("pdf.baseMap.image.folder") + File.separator + nameImage);
			if(fileToEncode != null){
				return encodeAsBase64(fileToEncode);				
			} else {
				logger.error("Fail to encode file as base64 : file is null");
				return null;
			}			
		}
		catch (Exception e){
			logger.error("Fail to transform file to string");
			return null;
		}		
	}

	/**
	 * Get image from datadir for given image name
	 * 
	 * @param imageName to find this image into datadir
	 * 
	 * @return Response that contain File
	 */
	@POST	
	@Path("/getImageFromDataDir")		
	public Response getBaseMapPreview(@RequestParam("imageName") String imageName) {
		String encodedImage;
		String encodedErrorImage;
		// use to find mime type
		try{			
			// return image as data base64
			encodedImage = imageToString(imageName);
			if(encodedImage != null){
				return Response.ok(encodedImage,MediaType.TEXT_PLAIN).build();
			} else {
				// return image error as data base64
				encodedErrorImage = imageToString("error.png");
				if(encodedErrorImage != null){
					logger.error("File not exist or fail to encode file : display error image");
					return Response.ok(encodedErrorImage,MediaType.TEXT_PLAIN).build();
				} else {
					return Response.status(Response.Status.NOT_FOUND).entity("Image not found for : " + imageName).build();
				}				
			}						
		}
		catch (Exception e){
			// response with bad request error			
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build();  
		}		
	}
	
}
