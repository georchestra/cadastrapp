package org.georchestra.cadastrapp.model.pdf;

import javax.xml.bind.annotation.XmlAttribute;

public class Proprietaire {
	
	
	String nom;
		
	String adresse;


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
}
