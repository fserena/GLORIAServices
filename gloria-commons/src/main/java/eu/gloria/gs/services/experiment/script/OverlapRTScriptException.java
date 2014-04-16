package eu.gloria.gs.services.experiment.script;

import eu.gloria.gs.services.log.action.ActionException;

public class OverlapRTScriptException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OverlapRTScriptException(String rt) {
		super("slot overlap");
		this.getAction().put("rt", rt);
	}
	
	public OverlapRTScriptException() {
		super();
	}
}
