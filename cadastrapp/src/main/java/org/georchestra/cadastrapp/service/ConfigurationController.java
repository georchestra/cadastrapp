package org.georchestra.cadastrapp.service;

import java.util.HashMap;
import java.util.Map;

import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ConfigurationController extends CadController{

	final static Logger logger = LoggerFactory.getLogger(ConfigurationController.class);
	
	@RequestMapping(path = "/getConfiguration", produces = {MediaType.APPLICATION_JSON_VALUE}, method= {RequestMethod.GET})
	/**
	 *  Returns server configuration witch is common to client side 
	 *   
	 * @return JSON list 
	 */
	public @ResponseBody Map<String, Object> getConfiguration(){
		
		Map<String, Object> configuration = new HashMap<String, Object>();
		
		configuration.put("minNbCharForSearch", CadastrappPlaceHolder.getProperty("minNbCharForSearch"));
		configuration.put("minParacelleIdLength", CadastrappPlaceHolder.getProperty("parcelleId.length"));
		
		configuration.put("cadastreWFSURL", CadastrappPlaceHolder.getProperty("cadastre.wfs.url"));
		configuration.put("cadastreWMSURL", CadastrappPlaceHolder.getProperty("cadastre.wms.url"));
		
		configuration.put("cadastreWMSLayerName", CadastrappPlaceHolder.getProperty("cadastre.wms.layer.name")); 
		configuration.put("cadastreWFSLayerName", CadastrappPlaceHolder.getProperty("cadastre.wfs.layer.name"));
		
		configuration.put("cadastreLayerIdParcelle", CadastrappPlaceHolder.getProperty("cadastre.layer.idParcelle"));

		configuration.put("cnil1RoleName", CadastrappPlaceHolder.getProperty("cnil1RoleName"));
		configuration.put("cnil2RoleName", CadastrappPlaceHolder.getProperty("cnil2RoleName"));
		
		configuration.put("maxRequest", CadastrappPlaceHolder.getProperty("maxRequest"));
		
		// Configuration for unite foncier
		configuration.put("uFWFSURL", CadastrappPlaceHolder.getProperty("uf.wfs.url"));
		configuration.put("uFWFSLayerName", CadastrappPlaceHolder.getProperty("uf.wfs.layer.name"));
		
		configuration.put("organisme", CadastrappPlaceHolder.getProperty("pdf.organisme"));
		configuration.put("dateValiditeMajic", CadastrappPlaceHolder.getProperty("pdf.dateValiditeDonneesMajic"));
		configuration.put("dateValiditeEDIGEO", CadastrappPlaceHolder.getProperty("pdf.dateValiditeDonneesEDIGEO"));
		
		// Add base map hashmap to let user choose for BP
		final String regexBaseMap = "^pdf\\.baseMap\\.[0-9]*\\.title$";
		final String regexBaseMapThumbnail = "^pdf\\.baseMap\\.[0-9]*\\.title\\.thumbnail$";
		configuration.put("pdfbasemaptitles", CadastrappPlaceHolder.getPropertiesLike(regexBaseMap));
		configuration.put("pdfbasemapthumbnails", CadastrappPlaceHolder.getPropertiesLike(regexBaseMapThumbnail));
		
		return configuration;
	}

}
