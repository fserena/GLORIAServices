package eu.gloria.gs.services.experiment.base.reservation;

import eu.gloria.gs.services.log.action.ActionException;

public class ExperimentNotInstantiatedException extends ActionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExperimentNotInstantiatedException(int rid) {
		super();
		this.getAction().put("rid", rid);
	}
	
	public ExperimentNotInstantiatedException() {
		super();
	}

	public ExperimentNotInstantiatedException(String message) {
		super(message);
	}
}
