package eu.gloria.gs.services.experiment.base.reservation;

import eu.gloria.gs.services.log.action.ActionException;

public class MaxReservationTimeException extends ActionException {

	public MaxReservationTimeException() {
		super();
	}

	public MaxReservationTimeException(long current, long max, String user) {
		super("max time");

		this.getAction().put("current", current);
		this.getAction().put("max", max);
		this.getAction().put("user", user);

	}

	private static final long serialVersionUID = 5848333110207976076L;

}
