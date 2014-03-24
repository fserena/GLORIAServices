package eu.gloria.gs.services.experiment.script;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class OverlapRTScriptException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OverlapRTScriptException(String rt) {
		super(new Action());
		Action action = this.getAction();
		action.put("rt", rt);
	}

	public OverlapRTScriptException(Action action) {
		super(action);
	}

}
