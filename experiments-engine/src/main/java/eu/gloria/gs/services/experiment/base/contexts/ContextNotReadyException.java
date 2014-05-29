package eu.gloria.gs.services.experiment.base.contexts;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class ContextNotReadyException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3312920179746575637L;

	public ContextNotReadyException(Action action) {
		super(action);
	}

	public ContextNotReadyException(String cause) {
		super(cause);
	}

	public ContextNotReadyException(int rid) {
		super(new Action());

		this.getAction().put("rid", rid);
	}

	public ContextNotReadyException() {
		super();
	}
}
