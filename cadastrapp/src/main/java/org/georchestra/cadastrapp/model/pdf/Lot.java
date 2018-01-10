package org.georchestra.cadastrapp.model.pdf;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;


public class Lot {
	
	// Id du lot
	private String lotId;
	
	// Numérateur dnumql
	private String numerateur;
	
	// Dénominateur ddenql
	private String denominateur;
	
	private ProprieteBatie proprieteDescription;
	
	// Optional
	private List<Proprietaire> proprietaires;


	@XmlElementWrapper(name = "proprietaires")
	@XmlElements({ @XmlElement(name = "Proprietaire", type = Proprietaire.class) })
	/**
	 * @param proprietaires
	 */
	public void setProprietaires(List<Proprietaire> proprietaires) {
		this.proprietaires = proprietaires;
	}

	/**
	 * @return the proprietaires
	 */
	public List<Proprietaire> getProprietaires() {
		return proprietaires;
	}

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
	public void setDenominateur(String denominateur) {
		this.denominateur = denominateur;
	}

	/**
	 * @return the proprieteDescription
	 */
	public ProprieteBatie getProprieteDescription() {
		return proprieteDescription;
	}

	@XmlElements({ @XmlElement(name = "Propriete", type = ProprieteBatie.class) })
	/**
	 * @param proprieteDescription the proprieteDescription to set
	 */
	public void setProprieteDescription(ProprieteBatie proprieteDescription) {
		this.proprieteDescription = proprieteDescription;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Lot [lotId=" + lotId + ", repartition : " + numerateur + "/" + denominateur + "]";
	}



}
