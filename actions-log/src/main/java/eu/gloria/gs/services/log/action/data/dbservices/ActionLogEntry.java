package eu.gloria.gs.services.log.action.data.dbservices;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class ActionLogEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8784408047690730988L;

	private int idactions_log;
	private String user;
	private Date date;
	private String action;
	private String type;
	private Integer rid;
	private String rt;

	public int getIdactions_log() {
		return idactions_log;
	}

	public void setIdactions_log(int idactions_log) {
		this.idactions_log = idactions_log;
	}

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}
}
