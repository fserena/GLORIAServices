package eu.gloria.gs.services.repository.rt.data;

public class ObservatoryInformation {

	private String city;
	private String country;
	private double visibilityRatio;
	private double lightPollution;

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

	public double getVisibilityRatio() {
		return visibilityRatio;
	}

	public void setVisibilityRatio(double visibilityRatio) {
		this.visibilityRatio = visibilityRatio;
	}

	public double getLightPollution() {
		return lightPollution;
	}

	public void setLightPollution(double lightPollution) {
		this.lightPollution = lightPollution;
	}
}
