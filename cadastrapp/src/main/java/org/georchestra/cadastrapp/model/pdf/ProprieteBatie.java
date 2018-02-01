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
	
	// Nature du local professionel
	private String ccocac;
	
	// Nature de la dépendance
	private String cconad;
	
	// Catégorie
	private String dcapec;
	
	// Valeur locative
	private String revcad;
	
	// Zone de ramassage
	private String gtauom;
	
	// Liste de lots optional
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
	 * @return the cconlc
	 */
	public String getCcocac() {
		return ccocac;
	}

	@XmlAttribute
	/**
	 * @param cconlc the cconlc to set
	 */
	public void setCcocac(String ccocac) {
		this.ccocac = ccocac;
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
	 * @return the revcad
	 */
	public String getRevcad() {
		return revcad;
	}

	@XmlAttribute
	/**
	 * @param revcad the revcad to set
	 */
	public void setRevcad(String revcad) {
		this.revcad = revcad;
	}

	/**
	 * @return the gtauom
	 */
	public String getGtauom() {
		return gtauom;
	}

	@XmlAttribute
	/**
	 * @param gtauom the gtauom to set
	 */
	public void setGtauom(String gtauom) {
		this.gtauom = gtauom;
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

	/**
	 * @return the cconad
	 */
	public String getCconad() {
		return cconad;
	}

	@XmlAttribute
	/**
	 * @param cconad the cconad to set
	 */
	public void setCconad(String cconad) {
		this.cconad = cconad;
	}
}

