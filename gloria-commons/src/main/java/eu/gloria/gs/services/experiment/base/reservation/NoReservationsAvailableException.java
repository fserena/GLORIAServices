package eu.gloria.gs.services.experiment.base.reservation;

import eu.gloria.gs.services.log.action.ActionException;

public class NoReservationsAvailableException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoReservationsAvailableException(String type) {
		super("reservations not available");
		this.getAction().put("type", type);
	}

	public NoReservationsAvailableException() {
		super();
	}

}
