package org.georchestra.cadastrapp.model.pdf;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

public class Proprietaire {
	
	String compteCommunal;
	
	String nom;
		
	String adresse;
	
	Date dateNaissance;
	
	String lieuNaissance;
	
	String droitReel;
	
	String codeDeDemenbrement;


	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	@XmlAttribute
	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	
	/**
	 * @return the adresse
	 */
	public String getAdresse() {
		return adresse;
	}

	@XmlAttribute
	/**
	 * @param adresse the adresse to set
	 */
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	/**
	 * @return the compteCommunal
	 */
	public String getCompteCommunal() {
		return compteCommunal;
	}

	@XmlAttribute
	/**
	 * @param compteCommunal the compteCommunal to set
	 */
	public void setCompteCommunal(String compteCommunal) {
		this.compteCommunal = compteCommunal;
	}

	/**
	 * @return the dateNaissance
	 */
	public Date getDateNaissance() {
		return dateNaissance;
	}

	@XmlAttribute
	/**
	 * @param dateNaissance the dateNaissance to set
	 */
	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	/**
	 * @return the lieuNaissance
	 */
	public String getLieuNaissance() {
		return lieuNaissance;
	}

	@XmlAttribute
	/**
	 * @param lieuNaissance the lieuNaissance to set
	 */
	public void setLieuNaissance(String lieuNaissance) {
		this.lieuNaissance = lieuNaissance;
	}

	/**
	 * @return the droitReel
	 */
	public String getDroitReel() {
		return droitReel;
	}

	@XmlAttribute
	/**
	 * @param droitReel the droitReel to set
	 */
	public void setDroitReel(String droitReel) {
		this.droitReel = droitReel;
	}

	/**
	 * @return the codeDeDemenbrement
	 */
	public String getCodeDeDemenbrement() {
		return codeDeDemenbrement;
	}

	@XmlAttribute
	/**
	 * @param codeDeDemenbrement the codeDeDemenbrement to set
	 */
	public void setCodeDeDemenbrement(String codeDeDemenbrement) {
		this.codeDeDemenbrement = codeDeDemenbrement;
	}
}
