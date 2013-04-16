package eu.gloria.gs.services.log.action;

import java.util.Date;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "ActionLogInterface", targetNamespace = "http://action.log.services.gs.gloria.eu/")
public interface ActionLogInterface {

	public void registerAction(@WebParam(name = "username") String username,
			@WebParam(name = "when") Date when, @WebParam(name = "action") String action) throws ActionLogException;
}
