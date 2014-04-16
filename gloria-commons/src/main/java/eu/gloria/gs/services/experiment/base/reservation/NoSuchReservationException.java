package eu.gloria.gs.services.experiment.base.reservation;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class NoSuchReservationException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSuchReservationException(int rid) {
		super();
		this.getAction().put("rid", rid);
	}

	public NoSuchReservationException(Action action) {
		super(action);
	}

	public NoSuchReservationException() {
		super();
	}

	public NoSuchReservationException(String message) {
		super(message);
	}

}
