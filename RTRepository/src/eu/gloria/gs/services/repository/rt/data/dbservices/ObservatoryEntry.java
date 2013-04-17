package eu.gloria.gs.services.repository.rt.data.dbservices;

import java.io.Serializable;

public class ObservatoryEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3228159340197308556L;
	private String name;
	private int oid;
	private String city;
	private String country;
	private double ratio;
	private double light_pollution;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOid() {
		return oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public double getLight_pollution() {
		return light_pollution;
	}

	public void setLight_pollution(double light_pollution) {
		this.light_pollution = light_pollution;
	}
}
