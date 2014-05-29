package eu.gloria.gs.services.core;

import java.util.Date;

import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.log.action.LogType;

public class LogEntry {

	private Date date;
	private String username;
	private Action action;
	private Integer rid;
	private String rt;
	private LogType type;
	
	public LogEntry(LogType type) {
		this.type = type;
		
	}

	public LogType getType() {
		return type;
	}

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

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
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
