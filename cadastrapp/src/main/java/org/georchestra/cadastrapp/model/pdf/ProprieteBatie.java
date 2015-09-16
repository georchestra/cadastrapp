package org.georchestra.cadastrapp.model.pdf;

import javax.xml.bind.annotation.XmlAttribute;

public class ProprieteBatie extends Propriete{
	
	// Lettre de batiment
	String dnubat;
	
	// Numéro d'entrée
	String descr;
	
	// Niveau d'étage
	String dniv;
	
	// Numéro de local
	String dpor;
	
	// Numéro de local
	String invar;
	
	// Affectation de la pev
	String ccoaff;
	
	// Code évaluation 
	String ccoeav;
	
	// Nature du local
	String cconlc;
	
	// Catégorie
	String dcapec;
	
	// Valeur locative
	String dvltrl;
	
	// Montant TIEOM
	String mvltieomx;

	/**
	 * @return the dnubat
	 */
	public String getDnubat() {
		return dnubat;
	}

	@XmlAttribute
	/**
	 * @param dnubat the dnubat to set
	 */
	public void setDnubat(String dnubat) {
		this.dnubat = dnubat;
	}

	/**
	 * @return the descr
	 */
	public String getDescr() {
		return descr;
	}

	@XmlAttribute
	/**
	 * @param descr the descr to set
	 */
	public void setDescr(String descr) {
		this.descr = descr;
	}

	/**
	 * @return the dniv
	 */
	public String getDniv() {
		return dniv;
	}

	@XmlAttribute
	/**
	 * @param dniv the dniv to set
	 */
	public void setDniv(String dniv) {
		this.dniv = dniv;
	}

	/**
	 * @return the dpor
	 */
	public String getDpor() {
		return dpor;
	}

	@XmlAttribute
	/**
	 * @param dpor the dpor to set
	 */
	public void setDpor(String dpor) {
		this.dpor = dpor;
	}

	/**
	 * @return the invar
	 */
	public String getInvar() {
		return invar;
	}
	
	@XmlAttribute
	/**
	 * @param invar the invar to set
	 */
	public void setInvar(String invar) {
		this.invar = invar;
	}

	/**
	 * @return the ccoaff
	 */
	public String getCcoaff() {
		return ccoaff;
	}

	@XmlAttribute
	/**
	 * @param ccoaff the ccoaff to set
	 */
	public void setCcoaff(String ccoaff) {
		this.ccoaff = ccoaff;
	}

	/**
	 * @return the ccoeav
	 */
	public String getCcoeav() {
		return ccoeav;
	}

	@XmlAttribute
	/**
	 * @param ccoeav the ccoeav to set
	 */
	public void setCcoeav(String ccoeav) {
		this.ccoeav = ccoeav;
	}

	/**
	 * @return the cconlc
	 */
	public String getCconlc() {
		return cconlc;
	}

	@XmlAttribute
	/**
	 * @param cconlc the cconlc to set
	 */
	public void setCconlc(String cconlc) {
		this.cconlc = cconlc;
	}

	/**
	 * @return the dcapec
	 */
	public String getDcapec() {
		return dcapec;
	}

	@XmlAttribute
	/**
	 * @param dcapec the dcapec to set
	 */
	public void setDcapec(String dcapec) {
		this.dcapec = dcapec;
	}

	/**
	 * @return the dvltrl
	 */
	public String getDvltrl() {
		return dvltrl;
	}

	@XmlAttribute
	/**
	 * @param dvltrl the dvltrl to set
	 */
	public void setDvltrl(String dvltrl) {
		this.dvltrl = dvltrl;
	}

	/**
	 * @return the mvltieomx
	 */
	public String getMvltieomx() {
		return mvltieomx;
	}

	@XmlAttribute
	/**
	 * @param mvltieomx the mvltieomx to set
	 */
	public void setMvltieomx(String mvltieomx) {
		this.mvltieomx = mvltieomx;
	}
}

