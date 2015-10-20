package org.georchestra.cadastrapp.model.request;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
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
	
	@OneToOne 
	@JoinColumn(name="cni")
	private UserRequest user;
	
	@Column(name="requestdate")
	private Date requestDate;
	
	@Column(name="parcelleid")
	private String parcelleId;
	
	@Column(name="comptecommunal")
	private String comptecommunal;
	
	public InformationRequest(){}
	
	public InformationRequest(UserRequest user, String parcelleId, String compteCommunal){
		this.user = user;
		this.parcelleId = parcelleId;
	}
	
	public String toString(){
		return String.format("Information Request : id=%d, user=%d, parcelle='%s'", requestId, user, parcelleId);
	}

	/**
	 * @return the requestId
	 */
	public long getRequestId() {
		return requestId;
	}

	@XmlAttribute
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	/**
	 * @return the userId
	 */
	public UserRequest getUser() {
		return user;
	}

	@XmlAttribute
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(UserRequest user) {
		this.user = user;
	}

	/**
	 * @return the requestDate
	 */
	public Date getRequestDate() {
		return requestDate;
	}

	@XmlAttribute
	/**
	 * @param requestDate the requestDate to set
	 */
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	/**
	 * @return the parcelleId
	 */
	public String getParcelleId() {
		return parcelleId;
	}

	@XmlAttribute
	/**
	 * @param parcelleId the parcelleId to set
	 */
	public void setParcelleId(String parcelleId) {
		this.parcelleId = parcelleId;
	}

	/**
	 * @return the comptecommunal
	 */
	public String getComptecommunal() {
		return comptecommunal;
	}

	@XmlAttribute
	/**
	 * @param comptecommunal the comptecommunal to set
	 */
	public void setComptecommunal(String comptecommunal) {
		this.comptecommunal = comptecommunal;
	}	

}
