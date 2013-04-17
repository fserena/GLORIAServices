package eu.gloria.gs.services.repository.rt.data.dbservices;

import java.io.Serializable;

public class RTEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8784408047690730988L;

	private String name;
	private String url;
	private String description;
	private String publicKey;
	private String owner;
	private double longitude;
	private double latitude;
	private int observatory;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getObservatory() {
		return observatory;
	}

	public void setObservatory(int observatory) {
		this.observatory = observatory;
	}
}
