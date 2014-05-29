package eu.gloria.gs.services.experiment.base.reservation;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class MaxUserTimeException extends ActionException {

	/**
	 * 
	 */
	public MaxUserTimeException(Action action)
	{
		super(action);
	}
	
	
	private static final long serialVersionUID = 5848333110207976076L;

}
