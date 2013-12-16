package eu.gloria.gs.services.experiment.base.reservation;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class ExperimentReservationArgumentException extends ActionException {

	/**
	 * 
	 */
	public ExperimentReservationArgumentException(LogAction action)
	{
		super(action);
	}
	
	public ExperimentReservationArgumentException(String cause)
	{
		super(new LogAction());
		
		this.getAction().put("cause", cause);
	}
	
	private static final long serialVersionUID = 5848333110207976076L;

}
