package org.georchestra.cadastrapp.model.pdf;

import javax.xml.bind.annotation.XmlAttribute;

public class ImpositionNonBatie extends Imposition{
	
	private int surface;
	
	private int majorationTerraion;

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
	 * @return the majorationTerraion
	 */
	public int getMajorationTerraion() {
		return majorationTerraion;
	}

	@XmlAttribute
	/**
	 * @param majorationTerraion the majorationTerraion to set
	 */
	public void setMajorationTerraion(int majorationTerraion) {
		this.majorationTerraion = majorationTerraion;
	}

}
