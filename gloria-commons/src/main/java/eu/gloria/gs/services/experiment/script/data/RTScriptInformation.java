package eu.gloria.gs.services.experiment.script.data;

import java.util.List;

import eu.gloria.gs.services.experiment.ScriptSlot;

public class RTScriptInformation {

	private ScriptType type;
	private String experiment;
	private String operation;
	private String rt;
	private ScriptSlot slot;
	private int id;
	private ScriptState state;
	private int rid;
	private String username;
	private Object init;
	private boolean notify;
	private List<Integer> ridHistory;

	public List<Integer> getRidHistory() {
		return ridHistory;
	}

	public void setRidHistory(List<Integer> ridHistory) {
		this.ridHistory = ridHistory;
	}

	public boolean isNotify() {
		return notify;
	}

	public void setNotify(boolean notify) {
		this.notify = notify;
	}

	public Object getInit() {
		return init;
	}

	public void setInit(Object init) {
		this.init = init;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public ScriptState getState() {
		return state;
	}

	public void setState(ScriptState state) {
		this.state = state;
	}

	public ScriptType getType() {
		return type;
	}

	public void setType(ScriptType type) {
		this.type = type;
	}

	public String getExperiment() {
		return experiment;
	}

	public void setExperiment(String experiment) {
		this.experiment = experiment;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getRt() {
		return rt;
	}

	public void setRt(String rt) {
		this.rt = rt;
	}
	
	public ScriptSlot getSlot() {
		return slot;
	}

	public void setSlot(ScriptSlot slot) {
		this.slot = slot;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
