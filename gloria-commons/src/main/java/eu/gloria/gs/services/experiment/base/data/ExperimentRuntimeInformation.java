package eu.gloria.gs.services.experiment.base.data;

public class ExperimentRuntimeInformation {

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
}
