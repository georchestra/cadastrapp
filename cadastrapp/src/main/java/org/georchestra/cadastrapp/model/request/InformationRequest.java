package org.georchestra.cadastrapp.model.request;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
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
	@SequenceGenerator(name = "RequestIdHibernateSequence", sequenceName = "hibernate_sequence", initialValue = 0, allocationSize = 1)
	@GeneratedValue(generator = "RequestIdHibernateSequence")
	private long requestId;
	
	@ManyToOne(optional=false, fetch = FetchType.EAGER) 
    @JoinColumn(name="userid", nullable=false, updatable=false)
	private UserRequest user;
	
	@Column(name="requestdate")
	private Date requestDate;

	@OneToMany(mappedBy = "informationRequest")
	private Set<ObjectRequest> objectsRequest;
		
	@Column(name="askby")
	private int askby;
	
	@Column(name="reponseby")
	private int responseby;
	
	@Column(name="objectnumber")
	private int objectNumber;

	
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
	 * @return the user request
	 */
	public UserRequest getUser() {
		return user;
	}

	/**
	 * @param user the user request to set
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
	public Set<ObjectRequest> getObjectsRequest() {
		return this.objectsRequest;
	}

	/**
	 * @param objectsRequest the set of object request
	 */
	@XmlElementWrapper(name="objects")
    @XmlElements({@XmlElement(name="object",     type=ObjectRequest.class)})
	public void setObjectsRequest(Set<ObjectRequest> objectsRequest) {
		this.objectsRequest = objectsRequest;
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
		return "InformationRequest [requestId=" + requestId + ", user=" + user + ", requestDate=" + requestDate + ", objectRequest=" + objectsRequest + ", askby=" + askby + ", responseby=" + responseby + "]";
	}

	/**
	 * @return the objectNumber
	 */
	public int getObjectNumber() {
		return objectNumber;
	}

	/**
	 * @param objectNumber the objectNumber to set
	 */
	public void setObjectNumber(int objectNumber) {
		this.objectNumber = objectNumber;
	}


}
