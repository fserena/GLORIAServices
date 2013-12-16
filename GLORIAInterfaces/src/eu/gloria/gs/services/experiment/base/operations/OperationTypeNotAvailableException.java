package eu.gloria.gs.services.experiment.base.operations;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class OperationTypeNotAvailableException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2236163623108047653L;

	public OperationTypeNotAvailableException(LogAction action) {
		super(action);
	}
	
	public OperationTypeNotAvailableException(String name) {
		super(new LogAction());
		
		LogAction action = this.getAction();
		action.put("name", name);
		action.put("phase", "creation");
	}

}
