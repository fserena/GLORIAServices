package eu.gloria.gs.services.experiment.base.reservation;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class NoReservationsAvailableException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoReservationsAvailableException(String type) {
		super(new LogAction());
		LogAction action = this.getAction();
		action.put("type", type);
	}
	
	public NoReservationsAvailableException() {
		super();
	}
	
}
