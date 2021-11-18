package org.georchestra.cadastrapp.model.request;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "object_request")
@XmlRootElement
public class ObjectRequest implements Serializable {

	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = 5439786730972374577L;

	@Id
	@SequenceGenerator(name = "HibernateSequence", sequenceName = "hibernate_sequence", initialValue = 0, allocationSize = 1)
	@GeneratedValue(generator = "HibernateSequence")
	private long objectId;

	@Column(name = "type")
	// COMPTECOMMUNAL = 0;
	// PARCELLE_ID = 1;
	// COPROPRIETE = 2;
	// PARCELLE = 3;
	// PROPRIETAIRE = 4;
	// LOT PROPRIETE = 5;
	
	private int type;

	@Column(name = "comptecommunal")
	private String comptecommunal;
	
	@Column(name = "parcelle")
	private String parcelle;
	
	@Column(name = "commune")
	private String commune;
	
	@Column(name = "section")
	private String section;
	
	@Column(name = "numero")
	private String numero;
	
	@Column(name = "proprietaire")
	private String proprietaire;
	
	@Column(name = "bp")
	private String bp;
	
	@Column(name = "rp")
	private String rp;

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
	
	
	/**
	 * @return the parcelle
	 */
	public String getParcelle() {
		return parcelle;
	}

	/**
	 * @param parcelle the parcelle to set
	 */
	@XmlAttribute
	public void setParcelle(String parcelle) {
		this.parcelle = parcelle;
	}


	/**
	 * @return the commune
	 */
	public String getCommune() {
		return commune;
	}

	/**
	 * @param commune the commune to set
	 */
	@XmlAttribute
	public void setCommune(String commune) {
		this.commune = commune;
	}

	/**
	 * @return the section
	 */
	public String getSection() {
		return section;
	}

	/**
	 * @param section the section to set
	 */
	@XmlAttribute
	public void setSection(String section) {
		this.section = section;
	}

	/**
	 * @return the numero
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	@XmlAttribute
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	/**
	 * @return the proprietaire
	 */
	public String getProprietaire() {
		return proprietaire;
	}

	/**
	 * @param proprietaire the proprietaire to set
	 */
	@XmlAttribute
	public void setProprietaire(String proprietaire) {
		this.proprietaire = proprietaire;
	}


	/**
	 * @return the bp
	 */
	public String getBp() {
		return bp;
	}

	/**
	 * @param bp the bp to set
	 */
	@XmlAttribute
	public void setBp(String bp) {
		this.bp = bp;
	}

	/**
	 * @return the rp
	 */
	public String getRp() {
		return rp;
	}

	/**
	 * @param rp the rp to set
	 */
	@XmlAttribute
	public void setRp(String rp) {
		this.rp = rp;
	}

	public ObjectRequest() {
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set COMPTECOMMUNAL = 0; PARCELLE = 1; COPROPRIETE
	 *            = 2;
	 */
	@XmlAttribute
	public void setType(int type) {
		this.type = type;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ObjectRequest [objectRequestId=" + objectId + ", type=" + type + ", comptecommunal=" + comptecommunal + ", parcelle=" + parcelle+ ", commune=" + commune + ", section=" + section + ", numero=" + numero + ", proprietaire=" + proprietaire+ "]";
	}

}
