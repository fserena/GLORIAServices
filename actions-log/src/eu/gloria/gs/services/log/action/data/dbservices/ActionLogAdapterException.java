package eu.gloria.gs.services.log.action.data.dbservices;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public class ActionLogAdapterException extends ActionException {

	private static final long serialVersionUID = 1L;

	public ActionLogAdapterException(String message) {		
		super(new LogAction());
		
		LogAction action = this.getAction();
		action.put("cause", "persistence error");
		action.put("message", message);
	}

	public ActionLogAdapterException() {		
		super();
	}
}
