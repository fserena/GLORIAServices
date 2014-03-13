package eu.gloria.gs.services.experiment.script;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class NoSuchScriptException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSuchScriptException(int rid) {
		super(new LogAction());
		LogAction action = this.getAction();
		action.put("sid", rid);
	}

	public NoSuchScriptException(LogAction action) {
		super(action);
	}

	public NoSuchScriptException() {
		super();
	}

}
