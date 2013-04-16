package eu.gloria.gs.services.experiment.online.data;

import java.util.HashMap;

public class ExperimentRuntimeInformation {

	private HashMap<String,Object> parameterValues;
	private long remainingTime;
	private long elapsedTime;
	
	public long getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(long remainingTime) {
		this.remainingTime = remainingTime;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public void setParameterValues(HashMap<String,Object> parameterValues) {
		this.parameterValues = parameterValues;
	}
	
	public HashMap<String,Object> getParameterValues() {
		return this.parameterValues;
	}
}
