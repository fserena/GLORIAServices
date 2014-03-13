package eu.gloria.gs.services.experiment;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class ExperimentException extends ActionException {

	/**
	 * 
	 */
	public ExperimentException(LogAction action)
	{		
		super(action);
	}
	
	public ExperimentException()
	{		
		super();
	}
	
	
	private static final long serialVersionUID = 5848333110207976076L;

}
