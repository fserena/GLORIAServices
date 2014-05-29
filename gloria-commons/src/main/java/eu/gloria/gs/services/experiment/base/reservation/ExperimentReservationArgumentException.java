package eu.gloria.gs.services.experiment.base.reservation;

import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.log.action.ActionException;

public class ExperimentReservationArgumentException extends ActionException {

	public ExperimentReservationArgumentException() {
		super();
	}
	
	public ExperimentReservationArgumentException(Action action) {
		super(action);
	}

	public ExperimentReservationArgumentException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 5848333110207976076L;

}
