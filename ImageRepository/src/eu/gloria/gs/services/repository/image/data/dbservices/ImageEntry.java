package eu.gloria.gs.services.repository.image.data.dbservices;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class ImageEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8784408047690730988L;

	private int idimage;
	private String user;
	private Date date;
	private String url;
	private int rid;
	private String local_id;
	private String rt;
	private String ccd;

	public int getIdimage() {
		return idimage;
	}

	public void setIdimage(int idimage) {
		this.idimage = idimage;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public String getLocal_id() {
		return local_id;
	}

	public void setLocal_id(String local_id) {
		this.local_id = local_id;
	}

	public String getRt() {
		return rt;
	}

	public void setRt(String rt) {
		this.rt = rt;
	}

	public String getCcd() {
		return ccd;
	}

	public void setCcd(String ccd) {
		this.ccd = ccd;
	}
}
