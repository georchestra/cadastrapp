package org.georchestra.cadastrapp.model.pdf;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;


@XmlTransient
public abstract class Propriete {

	// Date d'acte de mutation du local
	String jdatat;

	// Préfixe de section
	String ccopre;

	// Section
	String ccosec;

	// Numéro de plan de la parcelle
	String dnupla;

	// Numéro de la voirie
	String dnvoiri;

	// Indice de répétition
	String dindic;

	// Libellé de la voie
	String dvoilib;

	// Code Rivoli de la voie (code fantoir)
	String ccoriv;

	// Tarif de la commune
	String ccostn;

	// Code de collectivité locale accordant l'éxonération
	String ccolloc;

	// Nature d'éxonération
	String gnextl;

	// Année de début éxonération
	String jandeb;

	// Année de retour à imposition
	String janimp;

	// Fraction EC exonérée
	String fcexb;
	
	// Taux d'éxonération accordée
	String pexb;

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
	 * @return the ccostn
	 */
	public String getCcostn() {
		return ccostn;
	}

	@XmlAttribute
	/**
	 * @param ccostn the ccostn to set
	 */
	public void setCcostn(String ccostn) {
		this.ccostn = ccostn;
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
	public String getFcexb() {
		return fcexb;
	}

	@XmlAttribute
	/**
	 * @param fcexb the fcexb to set
	 */
	public void setFcexb(String fcexb) {
		this.fcexb = fcexb;
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
