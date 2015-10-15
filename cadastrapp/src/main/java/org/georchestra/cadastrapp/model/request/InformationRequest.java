package org.georchestra.cadastrapp.model.request;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InformationRequest {
	

	private long requestId;
	private long userId;
	private Date requestDate;
	private String parcelleId;
	private String comptecommunal;
	
	public InformationRequest(){}
	
	public InformationRequest(int userId, String parcelleId, String compteCommunal){
		this.userId = userId;
		this.parcelleId = parcelleId;
	}
	
	public String toString(){
		return String.format("Information Request : id=%d, user=%d, parcelle='%s'", requestId, userId, parcelleId);
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
	public long getUserId() {
		return userId;
	}

	@XmlAttribute
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
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
