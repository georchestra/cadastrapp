package org.georchestra.cadastrapp.model.pdf;

import javax.xml.bind.annotation.XmlAttribute;


public class Lot {
	
	// Id du lot
	private String lotId;
	
	// Numérateur dnumql
	private String numerateur;
	
	// Dénominateur ddenql
	private String denominateur;

	/**
	 * @return the lotId
	 */
	public String getLotId() {
		return lotId;
	}

	@XmlAttribute
	/**
	 * @param lotId the lotId to set
	 */
	public void setLotId(String lotId) {
		this.lotId = lotId;
	}

	/**
	 * @return the numerateur
	 */
	public String getNumerateur() {
		return numerateur;
	}

	@XmlAttribute
	/**
	 * @param numerateur the numerateur to set
	 */
	public void setNumerateur(String numerateur) {
		this.numerateur = numerateur;
	}

	/**
	 * @return the denomianteur
	 */
	public String getDenominateur() {
		return denominateur;
	}

	@XmlAttribute
	/**
	 * @param denomianteur the denomianteur to set
	 */
	public void setDenominateur(String denomianteur) {
		this.denominateur = denomianteur;
	}


}
