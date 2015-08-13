package org.georchestra.cadastrapp.model.pdf;

import javax.xml.bind.annotation.XmlAttribute;

public class Proprietaire {
	
	String comptecommunal;
	
	String nom;
	
	String nomMarital;
	
	String adresse;
	
	String codeDroit;

	/**
	 * @return the comptecommunal
	 */
	public String getComptecommunal() {
		return comptecommunal;
	}

	@XmlAttribute
	/**
	 * @param comptecommunal the comptecommunal to set
	 */
	public void setComptecommunal(String comptecommunal) {
		this.comptecommunal = comptecommunal;
	}

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
	 * @return the nomMarital
	 */
	public String getNomMarital() {
		return nomMarital;
	}

	@XmlAttribute
	/**
	 * @param nomMarital the nomMarital to set
	 */
	public void setNomMarital(String nomMarital) {
		this.nomMarital = nomMarital;
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
	 * @return the codeDroit
	 */
	public String getCodeDroit() {
		return codeDroit;
	}

	@XmlAttribute
	/**
	 * @param codeDroit the codeDroit to set
	 */
	public void setCodeDroit(String codeDroit) {
		this.codeDroit = codeDroit;
	}

}
