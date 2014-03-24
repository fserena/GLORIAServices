package eu.gloria.gs.services.scheduler.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ScheduleInformation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1054114642496522379L;

	private String rt;
	private String uuid;
	private String user;
	private String state;
	private int id;
	private Date publishDate;
	private Date lastDate;
	private ObservingPlanInformation opInfo;
	private List<String> candidates;
	private List<ImageResult> results;

	public ObservingPlanInformation getOpInfo() {
		return opInfo;
	}

	public void setOpInfo(ObservingPlanInformation opInfo) {
		this.opInfo = opInfo;
	}

	public String getRt() {
		return rt;
	}

	public void setRt(String rt) {
		this.rt = rt;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public List<String> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<String> candidates) {
		this.candidates = candidates;
	}

	public List<ImageResult> getResults() {
		return results;
	}

	public void setResults(List<ImageResult> results) {
		this.results = results;
	}

}
