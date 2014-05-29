package eu.gloria.gs.services.experiment.base.parameters;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class UndefinedExperimentParameterException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2236163623108047653L;


	public UndefinedExperimentParameterException(Action action) {
		super(action);
	}
	
	public UndefinedExperimentParameterException(String name) {
		super(new Action());
		
		this.getAction().put("name", name);
		this.getAction().put("cause", "parameter does not exist");
	}

}
