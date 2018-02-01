package org.georchestra.cadastrapp.model.pdf;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Exoneration {
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String exoneration = "";
		if(ccolloc != null){
			exoneration=ccolloc+"|"+gnextl+"|"+jandeb+"|"+janimp+"|"+fcexn+"|"+pexb;
		}
		return exoneration;
	}
}