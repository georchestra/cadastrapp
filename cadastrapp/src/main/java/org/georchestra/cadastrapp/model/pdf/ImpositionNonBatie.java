package org.georchestra.cadastrapp.model.pdf;

import javax.xml.bind.annotation.XmlAttribute;

public class ImpositionNonBatie extends Imposition{
	
	private int surface;
	
	private float majorationTerrain;

	/**
	 * @return the surface
	 */
	public int getSurface() {
		return surface;
	}

	@XmlAttribute
	/**
	 * @param surface the surface to set
	 */
	public void setSurface(int surface) {
		this.surface = surface;
	}

	/**
	 * @return the majorationTerrain
	 */
	public float getMajorationTerrain() {
		return majorationTerrain;
	}

	@XmlAttribute
	/**
	 * @param majorationTerrain the majorationTerrain to set
	 */
	public void setMajorationTerrain(float majorationTerrain) {
		this.majorationTerrain = majorationTerrain;
	}

}
