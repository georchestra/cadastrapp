package org.georchestra.cadastrapp.model.pdf;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

public class Parcelle {

	private String parcelleId;
	
	private String image;
	
	private String libelleCommune;
	
	private String adresseCadastrale;
	
	private String section;
	
	private String parcelle;
	
	private String codeFantoir;
	
	private int surfaceCadastrale;
	
	private List<Proprietaire> proprietaires;
	
	private List<ProprieteBatie> proprieteBaties;
	
	private List<ProprieteNonBatie> proprieteNonBaties;

	/**
	 * @return the parcelleId
	 */
	public String getParcelleId() {
		return parcelleId;
	}

	@XmlAttribute
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

	@XmlAttribute
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

	@XmlAttribute
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

	@XmlAttribute
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

	@XmlAttribute
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

	@XmlAttribute
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

	@XmlAttribute
	/**
	 * @param codeFantoir the codeFantoir to set
	 */
	public void setCodeFantoir(String codeFantoir) {
		this.codeFantoir = codeFantoir;
	}

	/**
	 * @return the surfaceCadastrale
	 */
	public int getSurfaceCadastrale() {
		return surfaceCadastrale;
	}

	@XmlAttribute
	/**
	 * @param surfaceCadastrale the surfaceCadastrale to set
	 */
	public void setSurfaceCadastrale(int surfaceCadastrale) {
		this.surfaceCadastrale = surfaceCadastrale;
	}
	

	@XmlElementWrapper(name="proprietaires")
    @XmlElements({@XmlElement(name="proprietaire",   type=Proprietaire.class)})
	/**
	 * @param proprietaires the proprietaires List to set
	 */
	public void setProprietaires(List<Proprietaire> proprietaires) {
		this.proprietaires = proprietaires;
	}
	
	/**
	 * @param proprietaires the proprietaires List to set
	 */
	public List<Proprietaire> getProprietaires() {
		return proprietaires;
	}

	/**
	 * @return the proprieteBaties
	 */
	public List<ProprieteBatie> getProprieteBaties() {
		return proprieteBaties;
	}

	@XmlElementWrapper(name="proprieteBaties")
    @XmlElements({@XmlElement(name="proprieteBatie",   type=ProprieteBatie.class)})
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

	@XmlElementWrapper(name="proprieteNonBaties")
    @XmlElements({@XmlElement(name="proprieteNonBatie",   type=ProprieteNonBatie.class)})
	/**
	 * @param proprieteNonBaties the proprieteNonBaties to set
	 */
	public void setProprieteNonBaties(List<ProprieteNonBatie> proprieteNonBaties) {
		this.proprieteNonBaties = proprieteNonBaties;
	}
}
