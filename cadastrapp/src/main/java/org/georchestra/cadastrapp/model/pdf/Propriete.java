package org.georchestra.cadastrapp.model.pdf;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;


@XmlTransient
public abstract class Propriete {

	// Date d'acte de mutation du local
	private String jdatat;

	// Préfixe de section
	private String ccopre;

	// Section
	private String ccosec;

	// Numéro de plan de la parcelle
	private String dnupla;

	// Numéro de la voirie
	private String dnvoiri;

	// Indice de répétition
	private String dindic;

	// Libellé de la voie
	private String dvoilib;

	// Code Rivoli de la voie (code fantoir)
	private String ccoriv;

	// Code de collectivité locale accordant l'éxonération
	private String ccolloc;

	// Nature d'éxonération
	private String gnextl;

	// Année de début éxonération
	private String jandeb;

	// Année de retour à imposition
	private String janimp;

	// Fraction EC exonérée
	private String fcexn;
	
	// Taux d'éxonération accordée
	private String pexb;

	/**
	 * @return the jdatat
	 */
	public String getJdatat() {
		return jdatat;
	}

	@XmlAttribute
	/**
	 * @param jdatat the jdatat to set
	 */
	public void setJdatat(String jdatat) {
		this.jdatat = jdatat;
	}

	/**
	 * @return the ccopre
	 */
	public String getCcopre() {
		return ccopre;
	}
	
	@XmlAttribute
	/**
	 * @param ccopre the ccopre to set
	 */
	public void setCcopre(String ccopre) {
		this.ccopre = ccopre;
	}

	/**
	 * @return the ccosec
	 */
	public String getCcosec() {
		return ccosec;
	}

	@XmlAttribute
	/**
	 * @param ccosec the ccosec to set
	 */
	public void setCcosec(String ccosec) {
		this.ccosec = ccosec;
	}

	/**
	 * @return the dnupla
	 */
	public String getDnupla() {
		return dnupla;
	}

	@XmlAttribute
	/**
	 * @param dnupla the dnupla to set
	 */
	public void setDnupla(String dnupla) {
		this.dnupla = dnupla;
	}

	/**
	 * @return the dnvoiri
	 */
	public String getDnvoiri() {
		return dnvoiri;
	}

	@XmlAttribute
	/**
	 * @param dnvoiri the dnvoiri to set
	 */
	public void setDnvoiri(String dnvoiri) {
		this.dnvoiri = dnvoiri;
	}

	/**
	 * @return the dindic
	 */
	public String getDindic() {
		return dindic;
	}

	@XmlAttribute
	/**
	 * @param dindic the dindic to set
	 */
	public void setDindic(String dindic) {
		this.dindic = dindic;
	}


	/**
	 * @return the dvoilib
	 */
	public String getDvoilib() {
		return dvoilib;
	}

	@XmlAttribute
	/**
	 * @param dvoilib the dvoilib to set
	 */
	public void setDvoilib(String dvoilib) {
		this.dvoilib = dvoilib;
	}

	/**
	 * @return the ccoriv
	 */
	public String getCcoriv() {
		return ccoriv;
	}

	@XmlAttribute
	/**
	 * @param ccoriv the ccoriv to set
	 */
	public void setCcoriv(String ccoriv) {
		this.ccoriv = ccoriv;
	}

	/**
	 * @return the ccolloc
	 */
	public String getCcolloc() {
		return ccolloc;
	}

	@XmlAttribute
	/**
	 * @param ccolloc the ccolloc to set
	 */
	public void setCcolloc(String ccolloc) {
		this.ccolloc = ccolloc;
	}

	/**
	 * @return the gnextl
	 */
	public String getGnextl() {
		return gnextl;
	}

	@XmlAttribute
	/**
	 * @param gnextl the gnextl to set
	 */
	public void setGnextl(String gnextl) {
		this.gnextl = gnextl;
	}

	/**
	 * @return the jandeb
	 */
	public String getJandeb() {
		return jandeb;
	}

	@XmlAttribute
	/**
	 * @param jandeb the jandeb to set
	 */
	public void setJandeb(String jandeb) {
		this.jandeb = jandeb;
	}

	/**
	 * @return the janimp
	 */
	public String getJanimp() {
		return janimp;
	}

	@XmlAttribute
	/**
	 * @param janimp the janimp to set
	 */
	public void setJanimp(String janimp) {
		this.janimp = janimp;
	}

	/**
	 * @return the fcexb
	 */
	public String getFcexn() {
		return fcexn;
	}

	@XmlAttribute
	/**
	 * @param fcexb the fcexb to set
	 */
	public void setFcexn(String fcexn) {
		this.fcexn = fcexn;
	}

	/**
	 * @return the pexb
	 */
	public String getPexb() {
		return pexb;
	}

	@XmlAttribute
	/**
	 * @param pexb the pexb to set
	 */
	public void setPexb(String pexb) {
		this.pexb = pexb;
	}
}
