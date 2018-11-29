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
	
	private Style style; 
	
	private List<Parcelle> parcelleList;
	
	private List<String> fieldSearch;
	
	private boolean isEmpty;



	public BordereauParcellaire(List<String> fields) {
		setFieldSearch(fields);
	}

	public BordereauParcellaire() {
	}

	/**
	 * @return the MAJIC information date
	 */
	public String getDateDeValiditeMajic() {
		return dateDeValiditeMajic;
	}
	
	/**
	 * @return the Edigeo informataion date
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
	 * @param dateDeValidite the information date of Majic data to be set
	 */
	public void setDateDeValiditeMajic(String dateDeValidite) {
		this.dateDeValiditeMajic = dateDeValidite;
	}
	
	@XmlElement
	/**
	 * @param dateDeValidite the information date of Edigeo to be set
	 */
	public void setDateDeValiditeEdigeo(String dateDeValidite) {
		this.dateDeValiditeEdigeo = dateDeValidite;
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

	/**
	 * @return the fieldSearch
	 */
	public List<String> getFieldSearch() {
		return fieldSearch;
	}

	/**
	 * @param fieldSearch the fieldSearch to set
	 */
	@XmlElementWrapper(name="fields")
    @XmlElements({@XmlElement(name="field",     type=String.class)})
	public void setFieldSearch(List<String> fieldSearch) {
		this.fieldSearch = fieldSearch;
	}
	

	/**
	 * @return the isEmpty
	 */
	public boolean isEmpty() {
		return isEmpty;
	}

	/**
	 * @param isEmpty the isEmpty to set
	 */
	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	/**
	 * @return the style
	 */
	public Style getStyle() {
		return style;
	}

	@XmlElement
	/**
	 * @param style the style to set
	 */
	public void setStyle(Style style) {
		this.style = style;
	}
	
}
