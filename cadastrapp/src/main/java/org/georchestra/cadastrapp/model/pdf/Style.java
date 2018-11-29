package org.georchestra.cadastrapp.model.pdf;


import javax.xml.bind.annotation.XmlAttribute;


public class Style {
	
	public Style() {
	}
	
	// couleur de remplissage de la parcelle
	private String fillColor;
	
	// opacité de remplissage de la parcelle
	private float fillOpacity;
	
	// couleur du contour de la parcelle
	private String strokeColor;
	
	// épaisseur du contour de la parcelle
	private int strokeWidth;

	public String getFillColor() {
		return fillColor;
	}

	@XmlAttribute
	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}

	public float getFillOpacity() {
		return fillOpacity;
	}

	@XmlAttribute
	public void setFillOpacity(float fillOpacity) {
		this.fillOpacity = fillOpacity;
	}

	public String getStrokeColor() {
		return strokeColor;
	}

	@XmlAttribute
	public void setStrokeColor(String strokeColor) {
		this.strokeColor = strokeColor;
	}

	public int getStrokeWidth() {
		return strokeWidth;
	}

	@XmlAttribute
	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}
	
	

}
