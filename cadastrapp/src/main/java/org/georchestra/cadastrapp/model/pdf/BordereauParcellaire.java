package org.georchestra.cadastrapp.model.pdf;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BordereauParcellaire {

	
	private String service;
	
	private String dateDeValiditeMajic;
	
	private String dateDeValiditeEdigeo;
	
	private String baseMapUrl;
	
	private String serviceUrl;
	
	private List<Parcelle> parcelleList;


	/**
	 * @return the dateDeValidité
	 */
	public String getDateDeValiditeMajic() {
		return dateDeValiditeMajic;
	}
	
	/**
	 * @return the dateDeValidité
	 */
	public String getDateDeValiditeEdigeo() {
		return dateDeValiditeEdigeo;
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
	 * @param dateDeValidite the dateDeValidite to set
	 */
	public void setDateDeValiditeMajic(String dateDeValidité) {
		this.dateDeValiditeMajic = dateDeValidité;
	}
	
	@XmlElement
	/**
	 * @param dateDeValidite the dateDeValidite to set
	 */
	public void setDateDeValiditeEdigeo(String dateDeValidité) {
		this.dateDeValiditeEdigeo = dateDeValidité;
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
