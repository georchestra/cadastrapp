package org.georchestra.cadastrapp.model.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="request_information")
@XmlRootElement
public class InformationRequest implements Serializable{
	

	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = 5439786730972374577L;

	@Id
	@GeneratedValue
	private long requestId;
	
	@ManyToOne(optional=false, fetch = FetchType.EAGER) 
    @JoinColumn(name="cni", nullable=false, updatable=false)
	private UserRequest user;
	
	@Column(name="requestdate")
	private Date requestDate;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name ="request_parcelles_information")
	private List<String> parcellesId;
	
	@Column(name="comptecommunal")
	private String comptecommunal;
	
	public InformationRequest(){}

	/**
	 * @return the requestId
	 */
	public long getRequestId() {
		return requestId;
	}

	
	/**
	 * @param requestId the requestId to set
	 */
	@XmlAttribute
	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	/**
	 * @return the userId
	 */
	public UserRequest getUser() {
		return user;
	}

	/**
	 * @param userId the userId to set
	 */
	@XmlElement(name="userRequest",     type=UserRequest.class)
	public void setUser(UserRequest user) {
		this.user = user;
	}

	/**
	 * @return the requestDate
	 */
	public Date getRequestDate() {
		return requestDate;
	}

	/**
	 * @param requestDate the requestDate to set
	 */
	@XmlAttribute
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	/**
	 * @return the parcelleId
	 */
	public List<String> getParcellesId() {
		return parcellesId;
	}

	/**
	 * @param parcelleId the parcelleId to set
	 */
	@XmlElementWrapper(name="parcelles")
    @XmlElements({@XmlElement(name="parcelle",     type=String.class)})
	public void setParcellesId(List<String> parcellesId) {
		this.parcellesId = parcellesId;
	}

	/**
	 * @return the comptecommunal
	 */
	public String getComptecommunal() {
		return comptecommunal;
	}

	/**
	 * @param comptecommunal the comptecommunal to set
	 */
	@XmlAttribute
	public void setComptecommunal(String comptecommunal) {
		this.comptecommunal = comptecommunal;
	}	

}
