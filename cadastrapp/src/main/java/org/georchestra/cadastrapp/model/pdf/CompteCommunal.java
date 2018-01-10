package org.georchestra.cadastrapp.model.pdf;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

public class CompteCommunal {
	
	private String compteCommunal; 
	
	private String codeDepartement;
	
	private String codeCommune;
	
	private String libelleCommune;
	
	private String numeroCommunal;
	
	private List<Proprietaire> proprietaires;
	
	private ProprietesBaties proprieteBaties;
	
	private ProprietesNonBaties proprieteNonBaties;


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
	 * @return the proprietaires
	 */
	public List<Proprietaire> getProprietaires() {
		return proprietaires;
	}

	@XmlElementWrapper(name="proprietaires")
    @XmlElements({@XmlElement(name="proprietaire",     type=Proprietaire.class)})
	/**
	 * @param proprietaires the proprietaires to set
	 */
	public void setProprietaires(List<Proprietaire> proprietaires) {
		this.proprietaires = proprietaires;
	}

	/**
	 * @return the proprieteBaties
	 */
	public ProprietesBaties getProprietesBaties() {
		return proprieteBaties;
	}

    @XmlElements({@XmlElement(name="proprietesBaties", type=ProprietesBaties.class, nillable = true)})
	/**
	 * @param proprieteBaties the proprieteBaties to set
	 */
	public void setProprietesBaties(ProprietesBaties proprieteBaties) {
		this.proprieteBaties = proprieteBaties;
	}

	/**
	 * @return the proprieteNonBaties
	 */
	public ProprietesNonBaties getProprietesNonBaties() {
		return proprieteNonBaties;
	}

    @XmlElements({@XmlElement(name="proprietesNonBaties",     type=ProprietesNonBaties.class, nillable = true)})
	/**
	 * @param proprieteNonBaties the proprieteNonBaties to set
	 */
	public void setProprietesNonBaties(ProprietesNonBaties proprieteNonBaties) {
		this.proprieteNonBaties = proprieteNonBaties;
	}

	/**
	 * @return the codeDepartement
	 */
	public String getCodeDepartement() {
		return codeDepartement;
	}

	@XmlAttribute
	/**
	 * @param codeDepartement the codeDepartement to set
	 */
	public void setCodeDepartement(String codeDepartement) {
		this.codeDepartement = codeDepartement;
	}

	/**
	 * @return the codeCommune
	 */
	public String getCodeCommune() {
		return codeCommune;
	}

	@XmlAttribute
	/**
	 * @param codeCommune the codeCommune to set
	 */
	public void setCodeCommune(String codeCommune) {
		this.codeCommune = codeCommune;
	}

	/**
	 * @return the libelleCommune
	 */
	public String getLibelleCommune() {
		return libelleCommune;
	}

	@XmlAttribute
	/**
	 * @param libelleCommune the libelleCommune to set
	 */
	public void setLibelleCommune(String libelleCommune) {
		this.libelleCommune = libelleCommune;
	}

	/**
	 * @return the numeroCommunal
	 */
	public String getNumeroCommunal() {
		return numeroCommunal;
	}

	@XmlAttribute
	/**
	 * @param numeroCommunal the numeroCommunal to set
	 */
	public void setNumeroCommunal(String numeroCommunal) {
		this.numeroCommunal = numeroCommunal;
	}

}
