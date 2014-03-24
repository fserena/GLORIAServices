package eu.gloria.gs.services.experiment.base.reservation;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class ExperimentReservationArgumentException extends ActionException {

	/**
	 * 
	 */
	public ExperimentReservationArgumentException(Action action) {
		super(action);
	}

	public ExperimentReservationArgumentException() {
		super();
	}

	public ExperimentReservationArgumentException(String cause) {
		super(new Action());

		this.getAction().put("cause", cause);
	}

	private static final long serialVersionUID = 5848333110207976076L;

}
