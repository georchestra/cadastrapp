package org.georchestra.cadastrapp.model.pdf;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Imposition {
	
	private float revenuImposable;
	
	private float communeRevenuExonere;
	
	private float departementRevenuExonere;
	
	private float groupementCommuneRevenuExonere;
	
	private float communeRevenuImposable;
	
	private float departementRevenuImposable;
	
	private float groupementCommuneRevenuImposable;

	/**
	 * @return the revenuImposable
	 */
	public float getRevenuImposable() {
		return revenuImposable;
	}

	@XmlAttribute
	/**
	 * @param revenuImposable the revenuImposable to set
	 */
	public void setRevenuImposable(float revenuImposable) {
		this.revenuImposable = revenuImposable;
	}

	/**
	 * @return the communeRevenuExonere
	 */
	public float getCommuneRevenuExonere() {
		return communeRevenuExonere;
	}

	@XmlAttribute
	/**
	 * @param communeRevenuExonere the communeRevenuExonere to set
	 */
	public void setCommuneRevenuExonere(float communeRevenuExonere) {
		this.communeRevenuExonere = communeRevenuExonere;
	}

	/**
	 * @return the departementRevenuExonere
	 */
	public float getDepartementRevenuExonere() {
		return departementRevenuExonere;
	}

	@XmlAttribute
	/**
	 * @param departementRevenuExonere the departementRevenuExonere to set
	 */
	public void setDepartementRevenuExonere(float departementRevenuExonere) {
		this.departementRevenuExonere = departementRevenuExonere;
	}

	/**
	 * @return the groupementCommuneRevenuExonere
	 */
	public float getGroupementCommuneRevenuExonere() {
		return groupementCommuneRevenuExonere;
	}

	@XmlAttribute
	/**
	 * @param groupementCommuneRevenuExonere the groupementCommuneRevenuExonere to set
	 */
	public void setGroupementCommuneRevenuExonere(float groupementCommuneRevenuExonere) {
		this.groupementCommuneRevenuExonere = groupementCommuneRevenuExonere;
	}

	/**
	 * @return the communeRevenuImposable
	 */
	public float getCommuneRevenuImposable() {
		return communeRevenuImposable;
	}

	@XmlAttribute
	/**
	 * @param communeRevenuImposable the communeRevenuImposable to set
	 */
	public void setCommuneRevenuImposable(float communeRevenuImposable) {
		this.communeRevenuImposable = communeRevenuImposable;
	}

	/**
	 * @return the departementRevenuImposable
	 */
	public float getDepartementRevenuImposable() {
		return departementRevenuImposable;
	}

	@XmlAttribute
	/**
	 * @param departementRevenuImposable the departementRevenuImposable to set
	 */
	public void setDepartementRevenuImposable(float departementRevenuImposable) {
		this.departementRevenuImposable = departementRevenuImposable;
	}

	/**
	 * @return the groupementCommuneRevenuImposable
	 */
	public float getGroupementCommuneRevenuImposable() {
		return groupementCommuneRevenuImposable;
	}

	@XmlAttribute
	/**
	 * @param groupementCommuneRevenuImposable the groupementCommuneRevenuImposable to set
	 */
	public void setGroupementCommuneRevenuImposable(float groupementCommuneRevenuImposable) {
		this.groupementCommuneRevenuImposable = groupementCommuneRevenuImposable;
	}
	

}
