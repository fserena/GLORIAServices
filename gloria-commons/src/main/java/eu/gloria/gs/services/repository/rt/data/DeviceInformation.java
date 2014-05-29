package eu.gloria.gs.services.repository.rt.data;

import java.util.HashMap;

public class DeviceInformation {

	private DeviceType type;
	private String model;
	private String description;
	private HashMap<String, String> features;
	private int deviceId;

	public DeviceType getType() {
		return type;
	}

	public void setType(DeviceType type) {
		this.type = type;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public HashMap<String, String> getFeatures() {
		return features;
	}

	public void setFeatures(HashMap<String, String> features) {
		this.features = features;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
}
