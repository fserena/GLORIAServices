package eu.gloria.gs.services.repository.image.data;

import java.io.Serializable;
import java.util.Date;

public class ImageInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1054114642496522379L;
	
	private String rt;
	private String ccd;
	private String user;
	private String url;
	private int id;
	private int rid;
	private String localid;
	private Date creationDate;

	public String getRt() {
		return rt;
	}

	public void setRt(String rt) {
		this.rt = rt;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public String getLocalid() {
		return localid;
	}

	public void setLocalid(String localid) {
		this.localid = localid;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCcd() {
		return ccd;
	}

	public void setCcd(String ccd) {
		this.ccd = ccd;
	}

}
