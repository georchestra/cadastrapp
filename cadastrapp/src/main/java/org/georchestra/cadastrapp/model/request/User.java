package org.georchestra.cadastrapp.model.request;

public class User {
	
	private String CNI;
	private String firstName;
	private String lastName;
	private int cgoCommune;
	private String adress;
	
	/**
	 * @return the cNI
	 */
	public String getCNI() {
		return CNI;
	}
	/**
	 * @param cNI the cNI to set
	 */
	public void setCNI(String cNI) {
		CNI = cNI;
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
