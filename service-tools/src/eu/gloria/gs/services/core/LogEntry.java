package eu.gloria.gs.services.core;

import java.util.Date;

import eu.gloria.gs.services.log.action.LogAction;

public abstract class LogEntry {

	private Date date;
	private String username;
	private LogAction action;
	private Integer rid;
	private String rt;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LogAction getAction() {
		return action;
	}

	public void setAction(LogAction action) {
		this.action = action;
	}

	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	public String getRt() {
		return rt;
	}

	public void setRt(String rt) {
		this.rt = rt;
	}
	
	

}
