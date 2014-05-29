package eu.gloria.gs.services.experiment;

import java.util.Date;

public class ScriptSlot {

	private Date begin;
	private long length;

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}
}
