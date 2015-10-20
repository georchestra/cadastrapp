package org.georchestra.cadastrapp.model.request;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
	
	@Column(name="cgocommune")
	private int cgoCommune;
	
	@Column(name="adress")
	private String adress;
	
	/**
	 * @return the cNI
	 */
	public String getCNI() {
		return cni;
	}
	/**
	 * @param cNI the cNI to set
	 */
	public void setCNI(String cni) {
		cni = cni;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
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
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the cgoCommune
	 */
	public int getCgoCommune() {
		return cgoCommune;
	}
	/**
	 * @param cgoCommune the cgoCommune to set
	 */
	public void setCgoCommune(int cgoCommune) {
		this.cgoCommune = cgoCommune;
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
	public void setAdress(String adress) {
		this.adress = adress;
	}
	
	
}
