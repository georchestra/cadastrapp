package org.georchestra.cadastrapp.model.pdf;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InformationLots {

	private String parcelle;

	private String adresse;
	
	private String batiment;

	private int nbLots;

	private int sumPart;
	
	private String service;
	
	private String anneMiseAJour;

	private List<Lot> lots;

	public InformationLots() {
	}

	@XmlElementWrapper(name = "lots")
	@XmlElements({ @XmlElement(name = "Lot", type = Lot.class) })
	/**
	 * @param lots
	 */
	public void setLots(List<Lot> lots) {
		this.lots = lots;
	}

	/**
	 *  @return lots
	 */
	public List<Lot> getLots() {
		return lots;
	}

	/**
	 * @return the plot id
	 */
	public String getParcelle() {
		return parcelle;
	}

	@XmlElement
	/**
	 * @param parcelle
	 *            the plot to set
	 */
	public void setParcelle(String parcelle) {
		this.parcelle = parcelle;
	}

	/**
	 * @return the adresse
	 */
	public String getAdresse() {
		return adresse;
	}

	@XmlElement
	/**
	 * @param adresse
	 *            the adresse to set
	 */
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	/**
	 * 
	 * @return the lots number
	 */
	public int getNbLots() {
		return nbLots;
	}

	@XmlElement
	/**
	 * 
	 * @param nbLots
	 * 			the nbLots to set
	 */
	public void setNbLots(int nbLots) {
		this.nbLots = nbLots;
	}

	/**
	 * 
	 * @return the sum of all bundle 
	 */
	public int getSumPart() {
		return sumPart;
	}

	@XmlElement
	/**
	 @param sumPart
	 * 			the sumPart to set
	 */
	public void setSumPart(int sumPart) {
		this.sumPart = sumPart;
	}

	/**
	 * @return the batiment
	 */
	public String getBatiment() {
		return batiment;
	}

	@XmlElement
	/**
	 * @param batiment the batiment to set
	 */
	public void setBatiment(String batiment) {
		this.batiment = batiment;
	}

	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}

	/**
	 * @return the anneMiseAJour
	 */
	public String getAnneMiseAJour() {
		return anneMiseAJour;
	}

	/**
	 * @param anneMiseAJour the anneMiseAJour to set
	 */
	public void setAnneMiseAJour(String anneMiseAJour) {
		this.anneMiseAJour = anneMiseAJour;
	}

}
