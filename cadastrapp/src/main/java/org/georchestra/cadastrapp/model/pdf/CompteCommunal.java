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
	
	private List<ProprieteBatie> proprieteBaties;
	
	private List<ProprieteNonBatie> proprieteNonBaties;


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
	public List<ProprieteBatie> getProprieteBaties() {
		return proprieteBaties;
	}

	@XmlElementWrapper(name="proprietesBaties", nillable=true)
    @XmlElements({@XmlElement(name="proprieteBatie", type=ProprieteBatie.class, nillable = true)})
	/**
	 * @param proprieteBaties the proprieteBaties to set
	 */
	public void setProprieteBaties(List<ProprieteBatie> proprieteBaties) {
		this.proprieteBaties = proprieteBaties;
	}

	/**
	 * @return the proprieteNonBaties
	 */
	public List<ProprieteNonBatie> getProprieteNonBaties() {
		return proprieteNonBaties;
	}

	@XmlElementWrapper(name="proprietesNonBaties", nillable=true)
    @XmlElements({@XmlElement(name="proprieteNonBatie",     type=ProprieteNonBatie.class, nillable = true)})
	/**
	 * @param proprieteNonBaties the proprieteNonBaties to set
	 */
	public void setProprieteNonBaties(List<ProprieteNonBatie> proprieteNonBaties) {
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
