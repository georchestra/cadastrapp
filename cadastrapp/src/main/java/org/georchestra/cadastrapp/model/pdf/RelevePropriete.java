package org.georchestra.cadastrapp.model.pdf;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RelevePropriete {

	
	private String service;
	
	private String anneMiseAJour;
	
	private List<CompteCommunal> comptesCommunaux;
		

	/**
	 * @return the dateDeValidité
	 */
	public String getAnneMiseAJour() {
		return anneMiseAJour;
	}


	@XmlElement
	/**
	 * @param dateDeValidité the dateDeValidité to set
	 */
	public void setAnneMiseAJour(String anneMiseAJour) {
		this.anneMiseAJour = anneMiseAJour;
	}
	
	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}

	@XmlElement
	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}


	/**
	 * @return the comptesCommunaux
	 */
	public List<CompteCommunal> getComptesCommunaux() {
		return comptesCommunaux;
	}


	@XmlElementWrapper(name="comptesCommunaux")
    @XmlElements({@XmlElement(name="compteCommunal",     type=CompteCommunal.class)})
	/**
	 * @param comptesCommunaux the comptesCommunaux to set
	 */
	public void setComptesCommunaux(List<CompteCommunal> comptesCommunaux) {
		this.comptesCommunaux = comptesCommunaux;
	}

	
}
