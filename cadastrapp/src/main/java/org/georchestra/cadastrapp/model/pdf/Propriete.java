package org.georchestra.cadastrapp.model.pdf;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;


@XmlTransient
public abstract class Propriete extends Imposition{

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
	
	// Liste d'exonerations
	private List<Exoneration> exonerations;

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
	 * @return the exonerations
	 */
	public List<Exoneration> getExonerations() {
		return exonerations;
	}
	
	@XmlElementWrapper(name="exonerations")
    @XmlElements({@XmlElement(name="exoneration",   type=Exoneration.class)})
	/**
	 * @param exonerations the exonerations to set
	 */
	public void setExonerations(List<Exoneration> exonerations) {
		this.exonerations = exonerations;
	}
}
