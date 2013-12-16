package eu.gloria.gs.services.experiment.base.data;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;


public class ExperimentDatabaseException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExperimentDatabaseException(String cause) {
		super(new LogAction());
		
		LogAction action = this.getAction();
		action.put("cause", cause);
	}
	
	public ExperimentDatabaseException() {
		super(new LogAction());
		
		LogAction action = this.getAction();
		action.put("cause", "db problem");
	}
	
}
