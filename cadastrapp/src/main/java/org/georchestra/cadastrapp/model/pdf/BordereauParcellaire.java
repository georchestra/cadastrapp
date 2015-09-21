package org.georchestra.cadastrapp.model.pdf;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BordereauParcellaire {

	
	private String service;
	
	private String dateDeValidite;
	
	private String baseMapUrl;
	
	private String serviceUrl;
	
	private List<Parcelle> parcelleList;


	/**
	 * @return the dateDeValidité
	 */
	public String getDateDeValidite() {
		return dateDeValidite;
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
	public void setDateDeValidite(String dateDeValidité) {
		this.dateDeValidite = dateDeValidité;
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
	 * @return the baseMapUrl
	 */
	public String getBaseMapUrl() {
		return baseMapUrl;
	}

	@XmlElement
	/**
	 * @param baseMapUrl the baseMapUrl to set
	 */
	public void setBaseMapUrl(String baseMapUrl) {
		this.baseMapUrl = baseMapUrl;
	}

	/**
	 * @return the serviceUrl
	 */
	public String getServiceUrl() {
		return serviceUrl;
	}

	@XmlElement
	/**
	 * @param serviceUrl the serviceUrl to set
	 */
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	
}
