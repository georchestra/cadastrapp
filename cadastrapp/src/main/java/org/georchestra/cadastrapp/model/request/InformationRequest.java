package org.georchestra.cadastrapp.model.request;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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
    @JoinColumn(name="userid", nullable=false, updatable=false)
	private UserRequest user;
	
	@Column(name="requestdate")
	private Date requestDate;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name ="request_parcelles_information", joinColumns=@JoinColumn(name="informationrequest_requestid"))
	@Column(name="parcelles")
	private Set<String> parcellesId;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name ="request_comptecommunaux_information", joinColumns=@JoinColumn(name="informationrequest_requestid"))
	@Column(name="comptecommunaux")
	private Set<String> compteCommunaux;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name ="request_coproprietes_information", joinColumns=@JoinColumn(name="informationrequest_requestid"))
	@Column(name="coproprietes")
	private Set<String> coProprietes;
	
	@Column(name="askby")
	private int askby;
	
	@Column(name="reponseby")
	private int responseby;

	
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
	public Set<String> getParcellesId() {
		return parcellesId;
	}

	/**
	 * @param parcelleId the parcelleId to set
	 */
	@XmlElementWrapper(name="parcelles")
    @XmlElements({@XmlElement(name="parcelle",     type=String.class)})
	public void setParcellesId(Set<String> parcellesId) {
		this.parcellesId = parcellesId;
	}

	/**
	 * @return the compteCommunaux
	 */
	public Set<String> getCompteCommunaux() {
		return compteCommunaux;
	}

	/**
	 * @param compteCommunaux the compteCommunaux to set
	 */
	@XmlElementWrapper(name="compteCommunaux")
    @XmlElements({@XmlElement(name="compteCommunal",     type=String.class)})
	public void setCompteCommunaux(Set<String> compteCommunaux) {
		this.compteCommunaux = compteCommunaux;
	}

	/**
	 * @return the coproprietes
	 */
	public Set<String> getCoProprietes() {
		return coProprietes;
	}

	/**
	 * @param coproprietes the coproprietes to set
	 */
	@XmlElementWrapper(name="coProprietes")
    @XmlElements({@XmlElement(name="coPropriete",     type=String.class)})
	public void setCoProprietes(Set<String> coproprietes) {
		this.coProprietes = coproprietes;
	}

	/**
	 * @return the askby
	 */
	public int getAskby() {
		return askby;
	}

	/**
	 * @param askby the askby to set
	 */
	@XmlAttribute
	public void setAskby(int askby) {
		this.askby = askby;
	}

	/**
	 * @return the responseby
	 */
	public int getResponseby() {
		return responseby;
	}

	/**
	 * @param responseby the responseby to set
	 */
	@XmlAttribute
	public void setResponseby(int responseby) {
		this.responseby = responseby;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InformationRequest [requestId=" + requestId + ", user=" + user + ", requestDate=" + requestDate + ", parcellesId=" + parcellesId + ", compteCommunaux=" + compteCommunaux + ", coProprietes=" + coProprietes + ", askby=" + askby + ", responseby=" + responseby + "]";
	}


}
