package eu.gloria.gs.services.experiment.base.reservation;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class NoSuchReservationException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSuchReservationException(int rid) {
		super(new LogAction());
		LogAction action = this.getAction();
		action.put("rid", rid);
	}

	public NoSuchReservationException(LogAction action) {
		super(action);
	}

	public NoSuchReservationException() {
		super();
	}

}
