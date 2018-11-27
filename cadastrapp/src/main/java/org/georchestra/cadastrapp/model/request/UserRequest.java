package org.georchestra.cadastrapp.model.request;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;

@Entity
@Table(name="request_user_information")
public class UserRequest implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9140660737315556020L;
	
	@Id
	@GeneratedValue
	@Column(name="userid")
	private long userId;
		
	// No constraint because of Adminstration which have no mandatory field
	@Column(name="type")
	private String type;
	
	@Column(name="cni")
	private String cni;
	
	@Column(name="firstname")
	private String firstName;
	
	@Column(name="lastname")
	private String lastName;
	
	@Column(name="codepostal")
	private String codePostal;
	
	@Column(name="commune")
	private String commune;
	
	@Column(name="adress")
	private String adress;
	
	@Column(name="mail")
	private String mail;
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	@XmlAttribute
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	@XmlAttribute
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the cgoCommune
	 */
	public String getCommune() {
		return commune;
	}
	/**
	 * @param commune the cgoCommune to set
	 */
	@XmlAttribute
	public void setCommune(String commune) {
		this.commune = commune;
	}
	/**
	 * @return the adress
	 */
	public String getAdress() {
		return adress;
	}
	/**
	 * @param adress the adress to set
	 */
	@XmlAttribute
	public void setAdress(String adress) {
		this.adress = adress;
	}
	/**
	 * @return the cni
	 */
	public String getCni() {
		return cni;
	}
	/**
	 * @param cni the cni to set
	 */
	@XmlAttribute
	public void setCni(String cni) {
		this.cni = cni;
	}
	/**
	 * @return the codepostal
	 */
	public String getCodePostal() {
		return codePostal;
	}
	/**
	 * @param codepostal the codepostal to set
	 */
	@XmlAttribute
	public void setCodePostal(String codepostal) {
		this.codePostal = codepostal;
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	@XmlAttribute
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}
	
	@XmlAttribute
	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserRequest [userId=" + userId + ", type=" + type + ", cni=" + cni + ", firstName=" + firstName + ", lastName=" + lastName + ", codePostal=" + codePostal + ", commune=" + commune + ", adress=" + adress + ", mail=" + mail + "]";
	}
	
	
}
