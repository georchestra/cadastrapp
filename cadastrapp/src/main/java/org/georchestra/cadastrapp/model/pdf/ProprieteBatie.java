package org.georchestra.cadastrapp.model.pdf;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

public class ProprieteBatie extends Propriete{
	
	// Lettre de batiment
	private String dnubat;
	
	// Numéro d'entrée
	private String descr;
	
	// Niveau d'étage
	private String dniv;
	
	// Numéro de local
	private String dpor;
	
	// Numéro de local
	private String invar;
	
	// Affectation de la pev
	private String ccoaff;
	
	// Code évaluation 
	private String ccoeva;
	
	// Nature du local
	private String cconlc;
	
	// Catégorie
	private String dcapec;
	
	// Valeur locative
	private String dvltrt;
	
	// Montant TIEOM
	private String mvltieomx;
	
	// Liste de lots
	private List<Lot> lots;

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
	 * @return the ccoeva
	 */
	public String getCcoeva() {
		return ccoeva;
	}

	@XmlAttribute
	/**
	 * @param ccoeva the ccoeva to set
	 */
	public void setCcoeva(String ccoeva) {
		this.ccoeva = ccoeva;
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
	 * @return the dvltrt
	 */
	public String getDvltrt() {
		return dvltrt;
	}

	@XmlAttribute
	/**
	 * @param dvltrt the dvltrt to set
	 */
	public void setDvltrt(String dvltrt) {
		this.dvltrt = dvltrt;
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

	/**
	 * @return the lots
	 */
	public List<Lot> getLots() {
		return lots;
	}

	@XmlElementWrapper(name="lots")
    @XmlElements({@XmlElement(name="lot",   type=Lot.class)})
	/**
	 * @param lots the lots to set
	 */
	public void setLots(List<Lot> lots) {
		this.lots = lots;
	}
}

