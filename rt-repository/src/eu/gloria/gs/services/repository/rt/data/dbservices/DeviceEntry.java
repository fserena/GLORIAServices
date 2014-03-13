package eu.gloria.gs.services.repository.rt.data.dbservices;

import java.io.Serializable;

public class DeviceEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3228159340197308556L;
	private int did;
	private String type;
	private String model;
	private String description;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
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
}
