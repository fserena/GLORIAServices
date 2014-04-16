package eu.gloria.gs.services.experiment.base.parameters;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class ParameterTypeNotAvailableException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParameterTypeNotAvailableException() {
		super();
	}
	
	public ParameterTypeNotAvailableException(String experiment, String name, String type) {
		super("type not available");
		
		Action action = this.getAction();
		action.put("experiment", experiment);
		action.put("name", name);
		action.put("type", type);
	}
	
	public ParameterTypeNotAvailableException(String name) {
		super("type not available");
		
		Action action = this.getAction();
		action.put("name", name);
		action.put("phase", "creation");
	}
	
}
