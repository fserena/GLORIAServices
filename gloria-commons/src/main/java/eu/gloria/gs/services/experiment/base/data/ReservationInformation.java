package eu.gloria.gs.services.experiment.base.data;

import java.util.List;

public class ReservationInformation {

	private String experiment;
	private int reservationId;
	private String user;
	private TimeSlot timeSlot;
	private String status;
	private List<String> telescopes;

	public int getReservationId() {
		return reservationId;
	}

	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}

	public String getExperiment() {
		return experiment;
	}

	public void setExperiment(String experiment) {
		this.experiment = experiment;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public TimeSlot getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(TimeSlot timeSlot) {
		this.timeSlot = timeSlot;
	}

	public List<String> getTelescopes() {
		return telescopes;
	}

	public void setTelescopes(List<String> telescopes) {
		this.telescopes = telescopes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
