package eu.gloria.gs.services.experiment.script;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class NoScriptsAvailableException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoScriptsAvailableException(String type) {
		super(new Action());
		Action action = this.getAction();
		action.put("type", type);
	}
	
	public NoScriptsAvailableException() {
		super();
	}
	
}
