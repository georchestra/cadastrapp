package org.georchestra.cadastrapp.model.pdf;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

public class ProprietesNonBaties {
	

	// Liste de lots
	private List<ProprieteNonBatie> proprietes;
	
	private Imposition imposition;
	
	/**
	 * @return the impositionBatie
	 */
	public Imposition getImposition() {
		return imposition;
	}

	@XmlElement(name="imposition",     type=ImpositionNonBatie.class, nillable = true)
	/**
	 * @param impositionBatie the impositionBatie to set
	 */
	public void setImposition(Imposition imposition) {
		this.imposition = imposition;
	}

	/**
	 * @return list of proprietes
	 */
	public List<ProprieteNonBatie> getProprietes() {
		return proprietes;
	}

	@XmlElementWrapper(name="proprietes")
    @XmlElements({@XmlElement(name="propriete",   type=ProprieteNonBatie.class)})
	/**
	 * @param proprietes
	 */
	public void setProprietes(List<ProprieteNonBatie> proprietes) {
		this.proprietes = proprietes;
	}
}

