package org.georchestra.cadastrapp.model.request;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
	@GeneratedValue
	private long objectRequestId;

	@Column(name = "type")
	// COMPTECOMMUNAL = 0;
	// PARCELLE = 1;
	// COPROPRIETE = 2;
	private int type;

	@Column(name = "value")
	private String value;

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

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	@XmlAttribute
	public void setValue(String value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ObjectRequest [objectRequestId=" + objectRequestId + ", type=" + type + ", value=" + value + "]";
	}

}
