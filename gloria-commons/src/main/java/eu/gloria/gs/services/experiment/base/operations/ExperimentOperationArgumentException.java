package eu.gloria.gs.services.experiment.base.operations;

public class ExperimentOperationArgumentException extends
		ExperimentOperationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2236163623108047653L;

	public ExperimentOperationArgumentException(String name, String arg,
			String message) {
		super(name, message);
		this.getAction().put("arg", arg);
	}
	
	public ExperimentOperationArgumentException() {
		super();
	}

}
