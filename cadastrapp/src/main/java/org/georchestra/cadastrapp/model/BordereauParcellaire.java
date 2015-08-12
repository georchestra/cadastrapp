package org.georchestra.cadastrapp.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BordereauParcellaire {

	private String id;
	
	private String parcelleId;
	
	private String image;
	
	private String libelleCommune;
	
	private String adresseCadastrale;
	
	private String section;
	
	private String parcelle;
	
	private String codeFantoir;
	
	private String surfaceCadastrale;
	
	private String service;
	
	private String dateDeValidite;
	
	private Date dateDeCreation;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	@XmlElement
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the parcelleId
	 */
	public String getParcelleId() {
		return parcelleId;
	}

	@XmlElement
	/**
	 * @param parcelleId the parcelleId to set
	 */
	public void setParcelleId(String parcelleId) {
		this.parcelleId = parcelleId;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	@XmlElement
	/**
	 * @param image the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return the libelleCommune
	 */
	public String getLibelleCommune() {
		return libelleCommune;
	}

	@XmlElement
	/**
	 * @param libelleCommune the libelleCommune to set
	 */
	public void setLibelleCommune(String libelleCommune) {
		this.libelleCommune = libelleCommune;
	}

	/**
	 * @return the adresseCadastrale
	 */
	public String getAdresseCadastrale() {
		return adresseCadastrale;
	}

	@XmlElement
	/**
	 * @param adresseCadastrale the adresseCadastrale to set
	 */
	public void setAdresseCadastrale(String adresseCadastrale) {
		this.adresseCadastrale = adresseCadastrale;
	}

	/**
	 * @return the section
	 */
	public String getSection() {
		return section;
	}

	@XmlElement
	/**
	 * @param section the section to set
	 */
	public void setSection(String section) {
		this.section = section;
	}

	/**
	 * @return the parcelle
	 */
	public String getParcelle() {
		return parcelle;
	}

	@XmlElement
	/**
	 * @param parcelle the parcelle to set
	 */
	public void setParcelle(String parcelle) {
		this.parcelle = parcelle;
	}

	/**
	 * @return the codeFantoir
	 */
	public String getCodeFantoir() {
		return codeFantoir;
	}

	@XmlElement
	/**
	 * @param codeFantoir the codeFantoir to set
	 */
	public void setCodeFantoir(String codeFantoir) {
		this.codeFantoir = codeFantoir;
	}

	/**
	 * @return the surfaceCadastrale
	 */
	public String getSurfaceCadastrale() {
		return surfaceCadastrale;
	}

	@XmlElement
	/**
	 * @param surfaceCadastrale the surfaceCadastrale to set
	 */
	public void setSurfaceCadastrale(String surfaceCadastrale) {
		this.surfaceCadastrale = surfaceCadastrale;
	}

	/**
	 * @return the dateDeValidité
	 */
	public String getDateDeValidite() {
		return dateDeValidite;
	}

	@XmlElement
	/**
	 * @param dateDeValidité the dateDeValidité to set
	 */
	public void setDateDeValidite(String dateDeValidité) {
		this.dateDeValidite = dateDeValidité;
	}

	/**
	 * @return the dateDeCreation
	 */
	public Date getDateDeCreation() {
		return dateDeCreation;
	}

	@XmlElement
	/**
	 * @param dateDeCreation the dateDeCreation to set
	 */
	public void setDateDeCreation(Date dateDeCreation) {
		this.dateDeCreation = dateDeCreation;
	}

	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}

	@XmlElement
	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}
	
	
}
