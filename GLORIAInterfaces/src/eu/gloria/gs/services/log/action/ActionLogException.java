package eu.gloria.gs.services.log.action;

public class ActionLogException extends ActionException {

	/**
	 * 
	 */
	public ActionLogException(LogAction action)
	{
		super(action);
	}
	
	public ActionLogException(String cause) {
		super(new LogAction());
		
		this.getAction().put("cause", cause);
	}
	
	
	private static final long serialVersionUID = 5848333110207976076L;

}
