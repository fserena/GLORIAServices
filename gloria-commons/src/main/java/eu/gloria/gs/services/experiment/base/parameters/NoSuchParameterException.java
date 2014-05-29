package eu.gloria.gs.services.experiment.base.parameters;

import eu.gloria.gs.services.log.action.ActionException;

public class NoSuchParameterException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2236163623108047653L;

	public NoSuchParameterException(String name) {
		super("parameter not known");
		this.getAction().put("parameter", name);
	}

	public NoSuchParameterException() {
		super();
	}
}
