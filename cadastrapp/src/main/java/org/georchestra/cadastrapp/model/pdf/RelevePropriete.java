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
	
	
	private List<Parcelle> parcelleList;


	/**
	 * @return the dateDeValidité
	 */
	public String getAnneMiseAJour() {
		return anneMiseAJour;
	}

	/**
	 * @return the parcelleList
	 */
	public List<Parcelle> getParcelleList() {
		return parcelleList;
	}


	@XmlElementWrapper(name="parcelles")
    @XmlElements({@XmlElement(name="parcelle",     type=Parcelle.class)})
	/**
	 * @param parcelleList the parcelleList to set
	 */
	public void setParcelleList(List<Parcelle> parcelleList) {
		this.parcelleList = parcelleList;
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

	
}
