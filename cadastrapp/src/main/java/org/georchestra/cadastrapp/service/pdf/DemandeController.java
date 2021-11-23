package org.georchestra.cadastrapp.service.pdf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.georchestra.cadastrapp.configuration.CadastrappPlaceHolder;
import org.georchestra.cadastrapp.model.pdf.BordereauParcellaire;
import org.georchestra.cadastrapp.model.pdf.RelevePropriete;
import org.georchestra.cadastrapp.model.request.InformationRequest;
import org.georchestra.cadastrapp.model.request.ObjectRequest;
import org.georchestra.cadastrapp.repository.RequestRepository;
import org.georchestra.cadastrapp.service.CadController;
import org.georchestra.cadastrapp.service.constants.CadastrappConstants;
import org.georchestra.cadastrapp.service.exception.CadastrappServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DemandeController extends CadController {

	static final Logger logger = LoggerFactory.getLogger(DemandeController.class);
	final static Logger docLogger = LoggerFactory.getLogger("org.georchestra.cadastrapp.loggers.documents");
	
	static final String xslTemplate = "xsl/relevePropriete.xsl";
	static final String xslTemplateMinimal = "xsl/releveProprieteMinimal.xsl";

	@Autowired
	RequestRepository requestRepository;
	@Autowired
	BordereauParcellaireHelper bordereauParcellaireHelper;
	@Autowired
	ReleveProprieteHelper  releveProprieteHelper;

	/**
	 * Create a PDF using a request id
	 * 
	 * @param requestId user request Id
	 * @return pdf demande resume
	 * @throws IOException if an input or output exception occured
	 */
	@RequestMapping(path = "/createDemandeFromObj", produces ={MediaType.APPLICATION_PDF_VALUE}, method= {RequestMethod.GET})
	public ResponseEntity<byte[]> createDemandeFromObj(
		@RequestParam(name="requestid") long requestId) throws IOException {

		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(HttpStatus.NO_CONTENT);

		List<File> listPdfPath = new ArrayList<File>();

		int maxRequest = Integer.parseInt(CadastrappPlaceHolder.getProperty("maxRequest"));

		// Check if requestId exist
		if (requestId != 0) {

			// Get bordereau parcellaire information
			InformationRequest requestInformation = requestRepository.findByRequestId(requestId);
			
			boolean isMinimal = false;
			// If demandeur tiers, pdf has less information
			if(requestInformation.getUser() != null && requestInformation.getUser().getType() != null){
				isMinimal = CadastrappConstants.CODE_DEMANDEUR_TIER.equals(requestInformation.getUser().getType());
			}
			
			MDC.put("demmandeId", Long.toString(requestId));
			MDC.put("isMinimal", (isMinimal?"true":"false"));
			docLogger.info("Demmande - "+requestId+" - "+requestInformation.getUser().getType() );

			if (requestInformation != null && requestInformation.getObjectsRequest().size() <= maxRequest) {

				for (ObjectRequest objReq : requestInformation.getObjectsRequest()){
					if(objReq.getType() == CadastrappConstants.CODE_DEMANDEUR_COMPTE_COMMUNAL){
						//if BP
						if("1".equals(objReq.getBp())){
							listPdfPath.add(createBordereauParcellaireByCC(objReq.getComptecommunal(),null, false));
						}
						//if RP
						if("1".equals(objReq.getRp())){
							listPdfPath.add(createReleveProprieteByCC(objReq.getComptecommunal(), isMinimal));

						}

					}else if(objReq.getType() == CadastrappConstants.CODE_DEMANDEUR_PARCELLE_ID){
						//if BP
						if("1".equals(objReq.getBp())){
							listPdfPath.add(createBordereauParcellaireById(objReq.getParcelle()));
						}
						//if RP
						if("1".equals(objReq.getRp())){
							listPdfPath.add(createReleveProprieteById(objReq.getParcelle(), isMinimal));

						}

					}else if(objReq.getType() == CadastrappConstants.CODE_DEMANDEUR_COPROPRIETE){

						//if BP
						if("1".equals(objReq.getBp())){
							listPdfPath.add(createBordereauParcellaireByCC(objReq.getComptecommunal(),objReq.getParcelle(), true));
						}
						//if RP
						if("1".equals(objReq.getRp())){
							listPdfPath.add(createReleveCoProprieteByCCandParcelle(objReq.getComptecommunal(),objReq.getParcelle(), isMinimal));

						} 


					}else if(objReq.getType() == CadastrappConstants.CODE_DEMANDEUR_PARCELLE){
						//if BP
						if("1".equals(objReq.getBp())){
							listPdfPath.add(createBordereauParcellaireByInfoParcelle(objReq.getCommune(),objReq.getSection(),objReq.getNumero()));
						}
						//if RP
						if("1".equals(objReq.getRp())){
							listPdfPath.add(createReleveProprieteByInfoParcelle(objReq.getCommune(),objReq.getSection(),objReq.getNumero(), isMinimal));

						}

					}else if(objReq.getType() == CadastrappConstants.CODE_DEMANDEUR_PROPRIETAIRE){

						//if BP
						if("1".equals(objReq.getBp())){
							listPdfPath.add(createBordereauParcellaireByInfoOwner(objReq.getCommune(),objReq.getProprietaire()));
						}
						//if RP
						if("1".equals(objReq.getRp())){
							listPdfPath.add(createReleveProprieteByInfoOwner(objReq.getCommune(),objReq.getProprietaire(), isMinimal));

						}
					}else if(objReq.getType() == CadastrappConstants.CODE_DEMANDEUR_LOT_COPROPRIETE){
						//if BP
						if("1".equals(objReq.getBp())){
							listPdfPath.add(createBordereauParcellaireLot(objReq.getCommune(),objReq.getSection(),objReq.getNumero(),objReq.getProprietaire()));
						}
						//if RP
						if("1".equals(objReq.getRp())){
							listPdfPath.add(createReleveProprieteByInfoLot(objReq.getCommune(),objReq.getSection(),objReq.getNumero(),objReq.getProprietaire(), isMinimal));

						}
					}
				}

				//merge pdf files
				PDFMergerUtility ut = new PDFMergerUtility();
				for (File file :listPdfPath){
					ut.addSource(file);
				}
				String tempFolder = CadastrappPlaceHolder.getProperty("tempFolder");

				// Pdf temporary filename using tmp folder and timestamp
				final String pdfTmpFileName = tempFolder+File.separator+"DEMANDE_"+new Date().getTime();

				ut.setDestinationFileName(pdfTmpFileName+".pdf");
				ut.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());

				File pdfResult = new File(ut.getDestinationFileName());

				pdfResult.deleteOnExit();

				ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
					.filename(pdfResult.getName())
					.build();

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_PDF);
				headers.setContentDisposition(contentDisposition);

				response = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(pdfResult), headers, HttpStatus.OK);	
			}

		}
		return response;
	}

	/**
	 *  Create RP using owner code CC
	 *  
	 * @param compteCommunal owner ident
	 * @param isMinimal if true then only some information will be displayed
	 * @return PDF File RP
	 */
	private File createReleveProprieteByCC(String compteCommunal, boolean isMinimal) {

		//Store field search if no data to display => inform on PDF file
		List<String> fields = new ArrayList<String>();

		List<String> compteCommIds = new ArrayList<String>();
		compteCommIds.add(compteCommunal);
		// Get Releve Propriete information
		RelevePropriete relevePropriete = releveProprieteHelper.getReleveProprieteInformation(compteCommIds, new String());

		File pdf = null;
		//generate PDF
		if(relevePropriete.isEmpty()){
			fields.add("Compte communal : "+ compteCommunal);
			relevePropriete.setFieldSearch(fields);     
			pdf = releveProprieteHelper.generatePDF(relevePropriete,isMinimal, true);
		}else {
			pdf = releveProprieteHelper.generatePDF(relevePropriete,isMinimal, false);
		}
		
		for (String cc : compteCommIds) {
			docLogger.info("Relevé de propriété - DemandeCCId - "+MDC.get("demmandeId")+" - "+cc+" - null - "+MDC.get("isMinimal")+" - PDF" );
		}

		return pdf;
	}

	/**
	 * Create RP using parcelle id
	 * 
	 * @param parcelle id parcelle 
	 * @param isMinimal if true then only some information will be displayed
	 * @return PDF File RP
	 */
	private File createReleveProprieteById(String parcelle, boolean isMinimal) {

		//Store field search if no data to display => inform on PDF file
		List<String> fields = new ArrayList<String>();

		List<String> compteCommunauxList = new ArrayList<String>();
		//get compte communal by parcelle 
		List<Map<String, Object>> compteCommunaux = releveProprieteHelper.getProprietaireByParcelles(parcelle);

		for (Map<?, ?> row : compteCommunaux) {
			compteCommunauxList.add((String) row.get("comptecommunal"));

		}

		// Get Releve Propriete information
		RelevePropriete relevePropriete = releveProprieteHelper.getReleveProprieteInformation(compteCommunauxList, parcelle);


		File pdf = null;
		//generate PDF
		if(relevePropriete.isEmpty()){
			fields.add("Parcelle : "+ parcelle);
			relevePropriete.setFieldSearch(fields);     
			pdf = releveProprieteHelper.generatePDF(relevePropriete,isMinimal, true);
		}else {
			pdf = releveProprieteHelper.generatePDF(relevePropriete,isMinimal, false);
		}
		
		for (String cc : compteCommunauxList) {
			docLogger.info("Relevé de propriété - DemandeParcelleId - "+MDC.get("demmandeId")+" - "+cc+" - "+parcelle+" - "+MDC.get("isMinimal")+" - PDF" );
		}

		return pdf;
	}


	/**
	 *  Create borderau parcellaire using parcelle id
	 *  
	 * @param parcelle id parcelle 
	 * @return PDF File BP
	 */
	private File createBordereauParcellaireById(String parcelle) {

		//Store field search if no data to display => inform on PDF file
		List<String> fields = new ArrayList<String>();
		List<String> parcellId = new ArrayList<String>();
		parcellId.add(parcelle);

		// Get bordereau parcellaire information
		BordereauParcellaire bordereauParcellaire = bordereauParcellaireHelper.getBordereauParcellaireInformation(parcellId, 1, false);

		File pdf = null;
		try {
			//generate PDF
			if(bordereauParcellaire.isEmpty()){

				fields.add("Parcelle : "+parcelle);
				
				bordereauParcellaire.setFieldSearch(fields);     

				pdf = bordereauParcellaireHelper.generatePDF(bordereauParcellaire, true);

			}else {
				pdf = bordereauParcellaireHelper.generatePDF(bordereauParcellaire,false);
			}
		} catch (CadastrappServiceException e) {
			fields.add(CadastrappConstants.GENERATING_PICTURE_ERROR);
			bordereauParcellaire.setFieldSearch(fields);
			logger.error(CadastrappConstants.GENERATING_PICTURE_ERROR, e);
			try {
				pdf = bordereauParcellaireHelper.generatePDF(bordereauParcellaire,true);
				
				docLogger.info("Bordereau Parcellaire - DemandeParcelleId - "+MDC.get("demmandeId")+" - "+parcelle+" - 1 - false" );
			} catch (CadastrappServiceException e1) {
				logger.error("Error", e1);
			}
		}

		return pdf;

	}

	/**
	 * createBordereauParcellaireByCC
	 * 
	 * @param comptecommunal 
	 * @param parcelleId
	 * @param isCoPro boolean to check is request is a copropriete
	 * 
	 * @return PDF file contening wanted Bordereau Parcellaire
	 */
	private File createBordereauParcellaireByCC(String comptecommunal,String parcelleId, boolean isCoPro) {

		// Store field search if no data to display => inform on PDF file
		List<String> fields = new ArrayList<String>();
		
		// Parcelle list
		List<String> parcellesId = new ArrayList<String>();
		
		// Init pdf
		File pdf = null;

		if(parcelleId == null){
			
			// Get parcelle by compte communal
			List<Map<String, Object>> parcelleIds = bordereauParcellaireHelper.getParcellesByProprietaire(comptecommunal, isCoPro, null);

			for (Map<?, ?> row : parcelleIds) {
				parcellesId.add((String) row.get("parcelle"));

			}
		}else {
			parcellesId.add(parcelleId);
		}

		// Get bordereau parcellaire information
		BordereauParcellaire bordereauParcellaire = bordereauParcellaireHelper.getBordereauParcellaireInformation(parcellesId, 1, isCoPro);
		try {
			//generate PDF
			if(bordereauParcellaire.isEmpty()){
				fields.add("Compte communal : "+ comptecommunal);
				fields.add("Parcelle : "+ parcelleId);
				bordereauParcellaire.setFieldSearch(fields);     
				pdf = bordereauParcellaireHelper.generatePDF(bordereauParcellaire, true);
			}else {
				pdf = bordereauParcellaireHelper.generatePDF(bordereauParcellaire,false);
			}
			
			for (String p : parcellesId) {
				docLogger.info("Bordereau Parcellaire - DemandeCCId - "+MDC.get("demmandeId")+" - "+p+" - 1 - "+isCoPro );
			}
		} catch (CadastrappServiceException e) {
			fields.add(CadastrappConstants.GENERATING_PICTURE_ERROR);
			bordereauParcellaire.setFieldSearch(fields);
			logger.error(CadastrappConstants.GENERATING_PICTURE_ERROR, e);
			try {
				pdf = bordereauParcellaireHelper.generatePDF(bordereauParcellaire,true);
			} catch (CadastrappServiceException e1) {
				logger.error("Error", e1);
			}
		}


		return pdf;
	}

	/**
	 * Create RP using owner ident and parcelle id
	 * 
	 * @param compteCommunal
	 * @param parcellaId
	 * @param isMinimal
	 * @return File PDF RP
	 */
	private File createReleveCoProprieteByCCandParcelle(String compteCommunal, String parcelleId, boolean isMinimal) {

		//Store field search if no data to display => inform on PDF file
		List<String> fields = new ArrayList<String>();
		List<String> compteCommIds = new ArrayList<String>();
		compteCommIds.add(compteCommunal);

		// Get Releve Propriete information
		RelevePropriete relevePropriete = releveProprieteHelper.getReleveProprieteInformation(compteCommIds, parcelleId);
		
		File pdf = null;
		//generate PDF
		if(relevePropriete.isEmpty()){
			fields.add("Compte communal : "+compteCommunal);
			fields.add("Parcelle : "+parcelleId);
			relevePropriete.setFieldSearch(fields);     
			pdf = releveProprieteHelper.generatePDF(relevePropriete,isMinimal, true);
		}else {
			pdf = releveProprieteHelper.generatePDF(relevePropriete,isMinimal, false);
		}
		
		docLogger.info("Relevé de propriété - DemandeCoProCCParcelleId - "+MDC.get("demmandeId")+" - "+compteCommunal+" - "+parcelleId+" - "+MDC.get("isMinimal")+" - PDF" );

		return pdf;
	}

	/**
	 * Create bordereau parcellaire using parcelle details
	 * 
	 * @param commune
	 * @param section
	 * @param numero
	 * @return File PDF BP
	 */
	private File createBordereauParcellaireByInfoParcelle(String commune, String section, String numero){

		//Store field search if no data to display => inform on PDF file
		List<String> fields = new ArrayList<String>();
		//get parcelle by compte communal
		List<Map<String, Object>> parcelleIds = bordereauParcellaireHelper.getParcellesByInfoParcelle(commune,section,numero);

		List<String> parcellId = new ArrayList<String>();

		for (Map<?, ?> row : parcelleIds) {
			parcellId.add((String) row.get("parcelle"));
		}
		// Get bordereau parcellaire information
		BordereauParcellaire bordereauParcellaire = bordereauParcellaireHelper.getBordereauParcellaireInformation(parcellId, 1, false);
		File pdf = null;
		try {
			//generate PDF
			pdf = bordereauParcellaireHelper.generatePDF(bordereauParcellaire,false);
			
			for (String p : parcellId) {
				docLogger.info("Bordereau Parcellaire - DemandeInfoParcelle - "+MDC.get("demmandeId")+" - "+p+" - 1 - false" );
			}
		} catch (CadastrappServiceException e) {
			fields.add(CadastrappConstants.GENERATING_PICTURE_ERROR);
			bordereauParcellaire.setFieldSearch(fields);
			logger.error(CadastrappConstants.GENERATING_PICTURE_ERROR, e);
			try {
				pdf = bordereauParcellaireHelper.generatePDF(bordereauParcellaire,true);
			} catch (CadastrappServiceException e1) {
				logger.error("Error", e1);
			}
		}

		return pdf;
	}

	/**
	 * Create RP unsint parcelle details
	 * 
	 * @param commune
	 * @param section
	 * @param numero
	 * @param isMinimal
	 * @return File PDF RP
	 */
	private File createReleveProprieteByInfoParcelle(String commune, String section,String numero, boolean isMinimal) {

		List<String> compteCommunauxList = new ArrayList<String>();
		//get compte communal by parcelle 
		List<Map<String, Object>> compteCommunaux = releveProprieteHelper.getProprietaireByInfoParcelle(commune,section,numero);
		String idParcelle = null;

		for (Map<?, ?> row : compteCommunaux) {
			compteCommunauxList.add((String) row.get("comptecommunal"));
			if( idParcelle == null ) idParcelle = (String) row.get("parcelle");
		}

		// Get Releve Propriete information
		RelevePropriete relevePropriete = releveProprieteHelper.getReleveProprieteInformation(compteCommunauxList, idParcelle);

		//generate PDF
		File pdf = releveProprieteHelper.generatePDF(relevePropriete,isMinimal, false);
		
		for (String cc : compteCommunauxList) {
			docLogger.info("Relevé de propriété - DemandeInfoParcelle - "+MDC.get("demmandeId")+" - "+cc+" - "+idParcelle+" - "+MDC.get("isMinimal")+" - PDF" );
		}

		return pdf;
	}


	/**
	 * Create BP by owner details
	 * 
	 * @param commune
	 * @param ownerName
	 * @return
	 */
	private File createBordereauParcellaireByInfoOwner(String commune, String ownerName){

		//Store field search if no data to display => inform on PDF file
		List<String> fields = new ArrayList<String>();
		//get parcelle by compte communal
		List<Map<String, Object>> parcelleIds = bordereauParcellaireHelper.getParcellesByInfoOwner(commune,ownerName);

		List<String> parcellId = new ArrayList<String>();

		for (Map<?, ?> row : parcelleIds) {
			parcellId.add((String) row.get("parcelle"));

		}
		// Get bordereau parcellaire information
		BordereauParcellaire bordereauParcellaire = bordereauParcellaireHelper.getBordereauParcellaireInformation(parcellId, 1, false);
		File pdf = null;
		try {
			//generate PDF
			pdf = bordereauParcellaireHelper.generatePDF(bordereauParcellaire,false);
		} catch (CadastrappServiceException e) {
			fields.add(CadastrappConstants.GENERATING_PICTURE_ERROR);
			bordereauParcellaire.setFieldSearch(fields);
			logger.error(CadastrappConstants.GENERATING_PICTURE_ERROR, e);
			try {
				pdf = bordereauParcellaireHelper.generatePDF(bordereauParcellaire,true);
				
				for (String p : parcellId) {
					docLogger.info("Bordereau Parcellaire - DemandeInfoProp - "+MDC.get("demmandeId")+" - "+p+" - 1 - false" );
				}
			} catch (CadastrappServiceException e1) {
				logger.error("Error", e1);
			}
		}

		return pdf;
	}

	/**
	 * Create RP by owners details
	 * 
	 * @param commune
	 * @param ownerName
	 * @param isMinimal
	 * @return File PDF RP
	 */
	private File createReleveProprieteByInfoOwner(String commune, String ownerName, boolean isMinimal) {

		List<String> compteCommunauxList = new ArrayList<String>();
		//get compte communal by commune and owner 
		List<Map<String, Object>> compteCommunaux = releveProprieteHelper.getProprietaireByInfoOwner(commune,ownerName);

		for (Map<?, ?> row : compteCommunaux) {
			compteCommunauxList.add((String) row.get("comptecommunal"));

		}

		// Get Releve Propriete information
		RelevePropriete relevePropriete = releveProprieteHelper.getReleveProprieteInformation(compteCommunauxList, new String());

		//generate PDF
		File pdf = releveProprieteHelper.generatePDF(relevePropriete,isMinimal, false);
		
		for (String cc : compteCommunauxList) {
			docLogger.info("Relevé de propriété - DemandeInfoProp - "+MDC.get("demmandeId")+" - "+cc+" - null - "+MDC.get("isMinimal")+" - PDF" );
		}

		return pdf;
	}


	/**
	 * Create BP using owners details
	 * 
	 * @param commune
	 * @param section
	 * @param numero
	 * @param proprietaire
	 * @return
	 */
	private File createBordereauParcellaireLot(String commune, String section, String numero, String proprietaire) {
		//Store field search if no data to display => inform on PDF file
		List<String> fields = new ArrayList<String>();
		//get parcelle by commune,section,numero and proprietaire
		List<Map<String, Object>> parcelleIds = bordereauParcellaireHelper.getParcellesByInfoLot(commune,section,numero,proprietaire);

		List<String> parcellId = new ArrayList<String>();

		for (Map<?, ?> row : parcelleIds) {
			parcellId.add((String) row.get("parcelle"));

		}
		// Get bordereau parcellaire information
		BordereauParcellaire bordereauParcellaire = bordereauParcellaireHelper.getBordereauParcellaireInformation(parcellId, 1, false);

		File pdf = null;
		try {
			//generate PDF
			pdf = bordereauParcellaireHelper.generatePDF(bordereauParcellaire,false);
		} catch (CadastrappServiceException e) {
			fields.add(CadastrappConstants.GENERATING_PICTURE_ERROR);
			bordereauParcellaire.setFieldSearch(fields);
			logger.error(CadastrappConstants.GENERATING_PICTURE_ERROR, e);
			try {
				pdf = bordereauParcellaireHelper.generatePDF(bordereauParcellaire,true);
				
				for (String p : parcellId) {
					docLogger.info("Bordereau Parcellaire - DemandeLot - "+MDC.get("demmandeId")+" - "+p+" - 1 - false" );
				}
			} catch (CadastrappServiceException e1) {
			}
		}
		return pdf;
	}

	/**
	 * Create RP using owners details
	 * 
	 * @param commune
	 * @param section
	 * @param numero
	 * @param proprietaire
	 * @param isMinimal
	 * @return File PDF RP 
	 */
	private File createReleveProprieteByInfoLot(String commune, String section, String numero, String proprietaire, boolean isMinimal) {
		List<String> compteCommunauxList = new ArrayList<String>();
		//get compte communal by parcelle commune,section,numero, and proprietaire
		List<Map<String, Object>> compteCommunaux = releveProprieteHelper.getProprietaireByInfoLot(commune,section,numero,proprietaire);
		String parcelleId = null;

		for (Map<?, ?> row : compteCommunaux) {
			compteCommunauxList.add((String) row.get("comptecommunal"));
			parcelleId = (String) row.get("parcelle");

		}

		// Get Releve Propriete information
		RelevePropriete relevePropriete = releveProprieteHelper.getReleveProprieteInformation(compteCommunauxList, parcelleId);

		//generate PDF
		File pdf = releveProprieteHelper.generatePDF(relevePropriete,isMinimal, false);
		
		for (String cc : compteCommunauxList) {
			docLogger.info("Relevé de propriété - DemandeLot - "+MDC.get("demmandeId")+" - "+cc+" - null - "+MDC.get("isMinimal")+" - PDF" );
		}

		return pdf;
	}


}

