package org.georchestra.cadastrapp.model.request;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	@Column(name="cni")
	private String cni;
	
	@Column(name="firstname")
	private String firstName;
	
	@Column(name="lastname")
	private String lastName;
	
	@Column(name="codepostal")
	private String codepostal;
	
	@Column(name="commune")
	private String commune;
	
	@Column(name="adress")
	private String adress;
	
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
	 * @param cgoCommune the cgoCommune to set
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
	public String getCodepostal() {
		return codepostal;
	}
	/**
	 * @param codepostal the codepostal to set
	 */
	@XmlAttribute
	public void setCodepostal(String codepostal) {
		this.codepostal = codepostal;
	}
	
	
}
