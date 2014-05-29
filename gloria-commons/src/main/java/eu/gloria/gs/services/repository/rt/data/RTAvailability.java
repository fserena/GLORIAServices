package eu.gloria.gs.services.repository.rt.data;

import java.util.Date;

public class RTAvailability {

	private Date startingTime;
	private Date endingTime;

	public Date getStartingTime() {
		return this.startingTime;
	}

	public void setStartingTime(Date starting) {
		this.startingTime = starting;
	}
	
	public Date getEndingTime() {
		return this.endingTime;
	}

	public void setEndingTime(Date ending) {
		this.endingTime = ending;
	}

}
