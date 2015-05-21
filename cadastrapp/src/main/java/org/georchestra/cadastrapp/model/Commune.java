package org.georchestra.cadastrapp.model;

import java.io.Serializable;

public class Commune implements Serializable{
 
     
    /**
	 * 
	 */
	private static final long serialVersionUID = -8479731499789289961L;
	

	private String ccoinsee;
    private String commune;
    private String annee;
    private String ccodep;
    private String ccodir;
    private String ccocom;
    private String clerivili;
    private String libcom;
    private String libcom_maj;
    private String libcom_min;
    private String typcom;
    
	public String getCcoinsee() {
		return ccoinsee;
	}
	public void setCcoinsee(String ccoinsee) {
		this.ccoinsee = ccoinsee;
	}
	
	public String getCommune() {
		return commune;
	}
	public void setCommune(String commune) {
		this.commune = commune;
	}
	
	public String getAnnee() {
		return annee;
	}
	public void setAnnee(String annee) {
		this.annee = annee;
	}
	
	public String getCcodep() {
		return ccodep;
	}
	public void setCcodep(String ccodep) {
		this.ccodep = ccodep;
	}
	
	public String getCcodir() {
		return ccodir;
	}
	public void setCcodir(String ccodir) {
		this.ccodir = ccodir;
	}
	
	public String getCcocom() {
		return ccocom;
	}
	public void setCcocom(String ccocom) {
		this.ccocom = ccocom;
	}
	
	public String getClerivili() {
		return clerivili;
	}
	public void setClerivili(String clerivili) {
		this.clerivili = clerivili;
	}
	
	public String getLibcom() {
		return libcom;
	}
	public void setLibcom(String libcom) {
		this.libcom = libcom;
	}
	
	public String getLibcom_maj() {
		return libcom_maj;
	}
	public void setLibcom_maj(String libcom_maj) {
		this.libcom_maj = libcom_maj;
	}
	
	public String getLibcom_min() {
		return libcom_min;
	}
	public void setLibcom_min(String libcom_min) {
		this.libcom_min = libcom_min;
	}
	
	public String getTypcom() {
		return typcom;
	}
	public void setTypcom(String typcom) {
		this.typcom = typcom;
	}
	
	@Override
	public String toString() {
		return "Commune [ccoinsee=" + ccoinsee + ", commune=" + commune
				+ ", annee=" + annee + ", ccodep=" + ccodep + ", ccodir="
				+ ccodir + ", ccocom=" + ccocom + ", clerivili=" + clerivili
				+ ", libcom=" + libcom + ", libcom_maj=" + libcom_maj
				+ ", libcom_min=" + libcom_min + ", typcom=" + typcom + "]";
	}   

}
