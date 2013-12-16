package eu.gloria.gs.services.experiment.base.reservation;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public class ExperimentNotInstantiatedException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExperimentNotInstantiatedException(int rid) {
		super(new LogAction());
		
		this.getAction().put("rid", rid);
	}
	
	public ExperimentNotInstantiatedException(LogAction action) {
		super(action);
	}
	
	
}
