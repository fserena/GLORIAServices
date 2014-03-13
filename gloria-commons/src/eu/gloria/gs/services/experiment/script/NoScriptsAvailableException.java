package eu.gloria.gs.services.experiment.script;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class NoScriptsAvailableException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoScriptsAvailableException(String type) {
		super(new LogAction());
		LogAction action = this.getAction();
		action.put("type", type);
	}
	
	public NoScriptsAvailableException() {
		super();
	}
	
}
