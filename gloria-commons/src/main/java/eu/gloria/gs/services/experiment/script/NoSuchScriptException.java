package eu.gloria.gs.services.experiment.script;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class NoSuchScriptException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSuchScriptException(int rid) {
		super(new Action());
		Action action = this.getAction();
		action.put("sid", rid);
	}

	public NoSuchScriptException(Action action) {
		super(action);
	}

	public NoSuchScriptException() {
		super();
	}

}
