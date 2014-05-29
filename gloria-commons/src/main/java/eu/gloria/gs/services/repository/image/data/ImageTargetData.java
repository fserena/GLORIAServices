package eu.gloria.gs.services.repository.image.data;

import java.io.Serializable;

public class ImageTargetData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1054114642496522379L;

	private String object;
	private Double ra;
	private Double dec;

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public Double getRa() {
		return ra;
	}

	public void setRa(Double ra) {
		this.ra = ra;
	}

	public Double getDec() {
		return dec;
	}

	public void setDec(Double dec) {
		this.dec = dec;
	}

}
