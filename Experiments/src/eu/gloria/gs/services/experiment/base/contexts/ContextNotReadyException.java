package eu.gloria.gs.services.experiment.base.contexts;

public class ContextNotReadyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3312920179746575637L;

	public ContextNotReadyException(String parameterName) {
		super(parameterName);
	}
	
}
