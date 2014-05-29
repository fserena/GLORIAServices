package eu.gloria.gs.services.repository.image;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;

public class ImageRepositoryException extends ActionException {

	/**
	 * 
	 */
	public ImageRepositoryException(Action action)
	{
		super(action);
	}
	
	public ImageRepositoryException() {
		super();
	}
	
	private static final long serialVersionUID = 5848333110207976076L;

}
