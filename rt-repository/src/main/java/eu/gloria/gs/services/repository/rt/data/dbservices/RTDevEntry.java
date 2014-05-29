package eu.gloria.gs.services.repository.rt.data.dbservices;

import java.io.Serializable;

public class RTDevEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8784408047690730988L;

	private String rt;
	private int did;
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRT() {
		return rt;
	}

	public void setRT(String rt) {
		this.rt = rt;
	}

	public int getDid() {
		return did;
	}

	public void setDid(int device) {
		this.did = device;
	}
}
