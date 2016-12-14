package org.georchestra.cadastrapp.service.pdf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

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
import org.springframework.beans.factory.annotation.Autowired;

public class DemandeController extends CadController {

	final static Logger logger = LoggerFactory.getLogger(DemandeController.class);

	final String xslTemplate = "xsl/relevePropriete.xsl";
	final String xslTemplateMinimal = "xsl/releveProprieteMinimal.xsl";

	@Autowired
	RequestRepository requestRepository;
	@Autowired
	BordereauParcellaireHelper bordereauParcellaireHelper;
	@Autowired
	ReleveProprieteHelper  releveProprieteHelper;

	/**
	 * Create a PDF using a request id
	 * 
	 * @param headers to verify CNIL level information
	 * @param compteCommunal List of ids proprietaires
	 * @return pdf
	 * @throws IOException 
	 */
	@GET
	@Path("/createDemandeFromObj")
	@Produces("application/pdf")
	public Response createDemandeFromObj(@Context HttpHeaders headers, @QueryParam("requestid") long requestId) throws IOException {

		ResponseBuilder response = Response.noContent();

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

			if (requestInformation != null && requestInformation.getObjectsRequest().size() <= maxRequest) {

				for (ObjectRequest objReq : requestInformation.getObjectsRequest()){
					if(objReq.getType() == CadastrappConstants.CODE_DEMANDEUR_COMPTE_COMMUNAL){
						//if BP
						if("1".equals(objReq.getBp())){
							listPdfPath.add(createBordereauParcellaireByCC(objReq.getComptecommunal(),null, headers, false));
						}
						//if RP
						if("1".equals(objReq.getRp())){
							listPdfPath.add(createReleveProprieteByCC(objReq.getComptecommunal(), headers,isMinimal));

						}

					}else if(objReq.getType() == CadastrappConstants.CODE_DEMANDEUR_PARCELLE_ID){
						//if BP
						if("1".equals(objReq.getBp())){
							listPdfPath.add(createBordereauParcellaireById(objReq.getParcelle(), headers));
						}
						//if RP
						if("1".equals(objReq.getRp())){
							listPdfPath.add(createReleveProprieteById(objReq.getParcelle(), headers, isMinimal));

						}

					}else if(objReq.getType() == CadastrappConstants.CODE_DEMANDEUR_COPROPRIETE){

						//if BP
						if("1".equals(objReq.getBp())){
							listPdfPath.add(createBordereauParcellaireByCC(objReq.getComptecommunal(),objReq.getParcelle(), headers, true));
						}
						//if RP
						if("1".equals(objReq.getRp())){
							listPdfPath.add(createReleveCoProprieteByCCandParcelle(objReq.getComptecommunal(),objReq.getParcelle(), headers,isMinimal));

						} 


					}else if(objReq.getType() == CadastrappConstants.CODE_DEMANDEUR_PARCELLE){
						//if BP
						if("1".equals(objReq.getBp())){
							listPdfPath.add(createBordereauParcellaireByInfoParcelle(objReq.getCommune(),objReq.getSection(),objReq.getNumero(), headers));
						}
						//if RP
						if("1".equals(objReq.getRp())){
							listPdfPath.add(createReleveProprieteByInfoParcelle(objReq.getCommune(),objReq.getSection(),objReq.getNumero(), headers,isMinimal));

						}

					}else if(objReq.getType() == CadastrappConstants.CODE_DEMANDEUR_PROPRIETAIRE){

						//if BP
						if("1".equals(objReq.getBp())){
							listPdfPath.add(createBordereauParcellaireByInfoOwner(objReq.getCommune(),objReq.getProprietaire(), headers));
						}
						//if RP
						if("1".equals(objReq.getRp())){
							listPdfPath.add(createReleveProprieteByInfoOwner(objReq.getCommune(),objReq.getProprietaire(), headers,isMinimal));

						}
					}else if(objReq.getType() == CadastrappConstants.CODE_DEMANDEUR_LOT_COPROPRIETE){
						//if BP
						if("1".equals(objReq.getBp())){
							listPdfPath.add(createBordereauParcellaireLot(objReq.getCommune(),objReq.getSection(),objReq.getNumero(),objReq.getProprietaire(), headers));
						}
						//if RP
						if("1".equals(objReq.getRp())){
							listPdfPath.add(createReleveProprieteByInfoLot(objReq.getCommune(),objReq.getSection(),objReq.getNumero(),objReq.getProprietaire(), headers,isMinimal));

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

				ut.setDestinationFileName(pdfTmpFileName);
				ut.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());

				File pdfResult = new File(ut.getDestinationFileName());

				pdfResult.deleteOnExit();

				response = Response.ok((Object) pdfResult);
				response.header("Content-Disposition", "attachment; filename=" + pdfResult.getName() + ".pdf");
			}

		}
		return response.build();
	}

	
	private File createReleveProprieteByCC(String compteCommunal, HttpHeaders headers, boolean isMinimal) {

		//Store field search if no data to display => inform on PDF file
		List<String> fields = new ArrayList<String>();

		List<String> compteCommIds = new ArrayList<String>();
		compteCommIds.add(compteCommunal);
		// Get Releve Propriete information
		RelevePropriete relevePropriete = releveProprieteHelper.getReleveProprieteInformation(compteCommIds, headers, new String());

		File pdf = null;
		//generate PDF
		if(relevePropriete.isEmpty()){
			fields.add("Compte communal : "+ compteCommunal);
			relevePropriete.setFieldSearch(fields);     
			pdf = releveProprieteHelper.generatePDF(relevePropriete,isMinimal, true);
		}else {
			pdf = releveProprieteHelper.generatePDF(relevePropriete,isMinimal, false);
		}

		return pdf;
	}

	private File createReleveProprieteById(String parcelle, HttpHeaders headers, boolean isMinimal) {

		//Store field search if no data to display => inform on PDF file
		List<String> fields = new ArrayList<String>();

		List<String> compteCommunauxList = new ArrayList<String>();
		//get compte communal by parcelle 
		List<Map<String, Object>> compteCommunaux = releveProprieteHelper.getProprietaireByParcelles(parcelle);

		for (Map<?, ?> row : compteCommunaux) {
			compteCommunauxList.add((String) row.get("comptecommunal"));

		}

		// Get Releve Propriete information
		RelevePropriete relevePropriete = releveProprieteHelper.getReleveProprieteInformation(compteCommunauxList, headers, parcelle);


		File pdf = null;
		//generate PDF
		if(relevePropriete.isEmpty()){
			fields.add("Parcelle : "+ parcelle);
			relevePropriete.setFieldSearch(fields);     
			pdf = releveProprieteHelper.generatePDF(relevePropriete,isMinimal, true);
		}else {
			pdf = releveProprieteHelper.generatePDF(relevePropriete,isMinimal, false);
		}

		return pdf;
	}



	private File createBordereauParcellaireById(String parcelle, HttpHeaders headers) {

		//Store field search if no data to display => inform on PDF file
		List<String> fields = new ArrayList<String>();
		List<String> parcellId = new ArrayList<String>();
		parcellId.add(parcelle);

		// Get bordereau parcellaire information
		BordereauParcellaire bordereauParcellaire = bordereauParcellaireHelper.getBordereauParcellaireInformation(parcellId, 1, headers,false);

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
			} catch (CadastrappServiceException e1) {
			}
		}

		return pdf;

	}

	/**
	 * createBordereauParcellaireByCC
	 * 
	 * @param comptecommunal 
	 * @param parcelleId
	 * @param headers http headers to test wrights
	 * @param isCoPro boolean to check is request is a copropriete
	 * 
	 * @return PDF file contening wanted Bordereau Parcellaire
	 */
	private File createBordereauParcellaireByCC(String comptecommunal,String parcelleId, HttpHeaders headers, boolean isCoPro) {

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
		BordereauParcellaire bordereauParcellaire = bordereauParcellaireHelper.getBordereauParcellaireInformation(parcellesId, 1, headers, isCoPro);
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
		} catch (CadastrappServiceException e) {
			fields.add(CadastrappConstants.GENERATING_PICTURE_ERROR);
			bordereauParcellaire.setFieldSearch(fields);
			logger.error(CadastrappConstants.GENERATING_PICTURE_ERROR, e);
			try {
				pdf = bordereauParcellaireHelper.generatePDF(bordereauParcellaire,true);
			} catch (CadastrappServiceException e1) {
			}
		}


		return pdf;
	}


	private File createReleveCoProprieteByCCandParcelle(String compteCommunal, String parcellaId, HttpHeaders headers, boolean isMinimal) {

		//Store field search if no data to display => inform on PDF file
		List<String> fields = new ArrayList<String>();
		List<String> compteCommIds = new ArrayList<String>();
		compteCommIds.add(compteCommunal);

		// Get Releve Propriete information
		RelevePropriete relevePropriete = releveProprieteHelper.getReleveCoProprieteInformation(compteCommIds, headers, parcellaId);

		File pdf = null;
		//generate PDF
		if(relevePropriete.isEmpty()){
			fields.add("Compte communal : "+compteCommunal);
			fields.add("Parcelle : "+parcellaId);
			relevePropriete.setFieldSearch(fields);     
			pdf = releveProprieteHelper.generatePDF(relevePropriete,isMinimal, true);
		}else {
			pdf = releveProprieteHelper.generatePDF(relevePropriete,isMinimal, false);
		}

		return pdf;
	}

	private File createBordereauParcellaireByInfoParcelle(String commune, String section, String numero, HttpHeaders headers){

		//Store field search if no data to display => inform on PDF file
		List<String> fields = new ArrayList<String>();
		//get parcelle by compte communal
		List<Map<String, Object>> parcelleIds = bordereauParcellaireHelper.getParcellesByInfoParcelle(commune,section,numero);

		List<String> parcellId = new ArrayList<String>();

		for (Map<?, ?> row : parcelleIds) {
			parcellId.add((String) row.get("parcelle"));

		}
		// Get bordereau parcellaire information
		BordereauParcellaire bordereauParcellaire = bordereauParcellaireHelper.getBordereauParcellaireInformation(parcellId, 1, headers,false);
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
			} catch (CadastrappServiceException e1) {
			}
		}

		return pdf;
	}

	private File createReleveProprieteByInfoParcelle(String commune, String section,String numero, HttpHeaders headers, boolean isMinimal) {

		List<String> compteCommunauxList = new ArrayList<String>();
		//get compte communal by parcelle 
		List<Map<String, Object>> compteCommunaux = releveProprieteHelper.getProprietaireByInfoParcelle(commune,section,numero);
		String idParcelle = null;

		for (Map<?, ?> row : compteCommunaux) {
			compteCommunauxList.add((String) row.get("comptecommunal"));
			if( idParcelle == null ) idParcelle = (String) row.get("parcelle");
		}

		// Get Releve Propriete information
		RelevePropriete relevePropriete = releveProprieteHelper.getReleveProprieteInformation(compteCommunauxList, headers, idParcelle);

		//generate PDF
		File pdf = releveProprieteHelper.generatePDF(relevePropriete,isMinimal, false);

		return pdf;
	}


	private File createBordereauParcellaireByInfoOwner(String commune, String ownerName, HttpHeaders headers){

		//Store field search if no data to display => inform on PDF file
		List<String> fields = new ArrayList<String>();
		//get parcelle by compte communal
		List<Map<String, Object>> parcelleIds = bordereauParcellaireHelper.getParcellesByInfoOwner(commune,ownerName);

		List<String> parcellId = new ArrayList<String>();

		for (Map<?, ?> row : parcelleIds) {
			parcellId.add((String) row.get("parcelle"));

		}
		// Get bordereau parcellaire information
		BordereauParcellaire bordereauParcellaire = bordereauParcellaireHelper.getBordereauParcellaireInformation(parcellId, 1, headers,false);
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
			} catch (CadastrappServiceException e1) {
			}
		}

		return pdf;
	}

	private File createReleveProprieteByInfoOwner(String commune, String ownerName, HttpHeaders headers, boolean isMinimal) {

		List<String> compteCommunauxList = new ArrayList<String>();
		//get compte communal by commune and owner 
		List<Map<String, Object>> compteCommunaux = releveProprieteHelper.getProprietaireByInfoOwner(commune,ownerName);

		for (Map<?, ?> row : compteCommunaux) {
			compteCommunauxList.add((String) row.get("comptecommunal"));

		}

		// Get Releve Propriete information
		RelevePropriete relevePropriete = releveProprieteHelper.getReleveProprieteInformation(compteCommunauxList, headers, new String());

		//generate PDF
		File pdf = releveProprieteHelper.generatePDF(relevePropriete,isMinimal, false);

		return pdf;
	}



	private File createBordereauParcellaireLot(String commune, String section, String numero, String proprietaire, HttpHeaders headers) {
		//Store field search if no data to display => inform on PDF file
		List<String> fields = new ArrayList<String>();
		//get parcelle by commune,section,numero and proprietaire
		List<Map<String, Object>> parcelleIds = bordereauParcellaireHelper.getParcellesByInfoLot(commune,section,numero,proprietaire);

		List<String> parcellId = new ArrayList<String>();

		for (Map<?, ?> row : parcelleIds) {
			parcellId.add((String) row.get("parcelle"));

		}
		// Get bordereau parcellaire information
		BordereauParcellaire bordereauParcellaire = bordereauParcellaireHelper.getBordereauParcellaireInformation(parcellId, 1, headers,false);

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
			} catch (CadastrappServiceException e1) {
			}
		}
		return pdf;
	}

	private File createReleveProprieteByInfoLot(String commune, String section, String numero, String proprietaire, HttpHeaders headers, boolean isMinimal) {
		List<String> compteCommunauxList = new ArrayList<String>();
		//get compte communal by parcelle commune,section,numero, and proprietaire
		List<Map<String, Object>> compteCommunaux = releveProprieteHelper.getProprietaireByInfoLot(commune,section,numero,proprietaire);
		String parcelleId = null;

		for (Map<?, ?> row : compteCommunaux) {
			compteCommunauxList.add((String) row.get("comptecommunal"));
			parcelleId = (String) row.get("parcelle");

		}

		// Get Releve Propriete information
		RelevePropriete relevePropriete = releveProprieteHelper.getReleveCoProprieteInformation(compteCommunauxList, headers, parcelleId);

		//generate PDF
		File pdf = releveProprieteHelper.generatePDF(relevePropriete,isMinimal, false);

		return pdf;
	}


}

