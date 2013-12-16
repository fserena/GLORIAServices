package eu.gloria.gs.services.experiment.base.contexts;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class ContextNotReadyException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3312920179746575637L;

	public ContextNotReadyException(LogAction action) {
		super(action);
	}
	
	public ContextNotReadyException(int rid) {
		super(new LogAction());
		
		this.getAction().put("rid", rid);
	}
	
}
