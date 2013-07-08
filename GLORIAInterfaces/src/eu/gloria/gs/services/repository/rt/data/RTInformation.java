package eu.gloria.gs.services.repository.rt.data;

import java.util.List;

public class RTInformation {

	private List<DeviceInformation> devices;
	private String url;
	private String description;
	private String owner;
	private String user;
	private String password;
	private String observatory;
	private RTCoordinates coordinates;
	private RTAvailability availability;

	public List<DeviceInformation> getDevices() {
		return devices;
	}

	public void setDevices(List<DeviceInformation> devices) {
		this.devices = devices;
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getObservatory() {
		return observatory;
	}

	public void setObservatory(String observatory) {
		this.observatory = observatory;
	}

	public RTCoordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(RTCoordinates coordinates) {
		this.coordinates = coordinates;
	}

	public RTAvailability getAvailability() {
		return availability;
	}

	public void setAvailability(RTAvailability availability) {
		this.availability = availability;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
