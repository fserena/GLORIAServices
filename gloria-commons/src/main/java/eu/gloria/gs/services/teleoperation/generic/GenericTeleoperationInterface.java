package eu.gloria.gs.services.teleoperation.generic;

import javax.jws.WebService;

@WebService(name = "GenericTeleoperationInterface", targetNamespace = "http://generic.teleoperation.services.gs.gloria.eu/")
public interface GenericTeleoperationInterface {

	
	public void startTeleoperation(String rt) throws GenericTeleoperationException;
	public void notifyTeleoperation(String rt, long seconds) throws GenericTeleoperationException;
	public void stopTeleoperation(String rt) throws GenericTeleoperationException;
}