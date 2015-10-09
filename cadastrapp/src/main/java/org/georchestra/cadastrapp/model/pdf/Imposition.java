package org.georchestra.cadastrapp.model.pdf;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Imposition {
	
	private float revenuImposable;
	
	private int communeRevenuExonere;
	
	private int departementRevenuExonere;
	
	private int regionRevenuExonere;
	
	private int communeRevenuImposable;
	
	private int departementRevenuImposable;
	
	private int regionRevenuImposable;

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
	public int getCommuneRevenuExonere() {
		return communeRevenuExonere;
	}

	@XmlAttribute
	/**
	 * @param communeRevenuExonere the communeRevenuExonere to set
	 */
	public void setCommuneRevenuExonere(int communeRevenuExonere) {
		this.communeRevenuExonere = communeRevenuExonere;
	}

	/**
	 * @return the departementRevenuExonere
	 */
	public int getDepartementRevenuExonere() {
		return departementRevenuExonere;
	}

	@XmlAttribute
	/**
	 * @param departementRevenuExonere the departementRevenuExonere to set
	 */
	public void setDepartementRevenuExonere(int departementRevenuExonere) {
		this.departementRevenuExonere = departementRevenuExonere;
	}

	/**
	 * @return the regionRevenuExonere
	 */
	public int getRegionRevenuExonere() {
		return regionRevenuExonere;
	}

	@XmlAttribute
	/**
	 * @param regionRevenuExonere the regionRevenuExonere to set
	 */
	public void setRegionRevenuExonere(int regionRevenuExonere) {
		this.regionRevenuExonere = regionRevenuExonere;
	}

	/**
	 * @return the communeRevenuImposable
	 */
	public int getCommuneRevenuImposable() {
		return communeRevenuImposable;
	}

	@XmlAttribute
	/**
	 * @param communeRevenuImposable the communeRevenuImposable to set
	 */
	public void setCommuneRevenuImposable(int communeRevenuImposable) {
		this.communeRevenuImposable = communeRevenuImposable;
	}

	/**
	 * @return the departementRevenuImposable
	 */
	public int getDepartementRevenuImposable() {
		return departementRevenuImposable;
	}

	@XmlAttribute
	/**
	 * @param departementRevenuImposable the departementRevenuImposable to set
	 */
	public void setDepartementRevenuImposable(int departementRevenuImposable) {
		this.departementRevenuImposable = departementRevenuImposable;
	}

	/**
	 * @return the regionRevenuImposable
	 */
	public int getRegionRevenuImposable() {
		return regionRevenuImposable;
	}

	@XmlAttribute
	/**
	 * @param regionRevenuImposable the regionRevenuImposable to set
	 */
	public void setRegionRevenuImposable(int regionRevenuImposable) {
		this.regionRevenuImposable = regionRevenuImposable;
	}
	

}
