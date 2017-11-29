package org.georchestra.cadastrapp.model.pdf;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

public class ProprietesBaties {
	

	// Liste de lots
	private List<ProprieteBatie> proprietes;
	
	private Imposition imposition;
	
	/**
	 * @return the impositionBatie
	 */
	public Imposition getImposition() {
		return imposition;
	}

	@XmlElement(name="imposition",     type=Imposition.class, nillable = true)
	/**
	 * @param impositionBatie the impositionBatie to set
	 */
	public void setImposition(Imposition imposition) {
		this.imposition = imposition;
	}

	/**
	 * @return list of proprietes
	 */
	public List<ProprieteBatie> getProprietes() {
		return proprietes;
	}

	@XmlElementWrapper(name="proprietes")
    @XmlElements({@XmlElement(name="propriete",   type=ProprieteBatie.class)})
	/**
	 * @param proprietes
	 */
	public void setProprietes(List<ProprieteBatie> proprietes) {
		this.proprietes = proprietes;
	}
}

