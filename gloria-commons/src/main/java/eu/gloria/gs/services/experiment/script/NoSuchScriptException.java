package eu.gloria.gs.services.experiment.script;

import eu.gloria.gs.services.log.action.ActionException;

public class NoSuchScriptException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSuchScriptException(int rid) {
		super("script not known");
		this.getAction().put("sid", rid);
	}

	public NoSuchScriptException() {
		super();
	}

}
