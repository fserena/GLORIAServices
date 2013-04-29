package eu.gloria.gs.services.teleoperation.dome;

import javax.jws.WebService;

import eu.gloria.gs.services.teleoperation.base.TeleoperationException;

@WebService(name = "DomeTeleoperationInterface", targetNamespace = "http://dome.teleoperation.services.gs.gloria.eu/")
public interface DomeTeleoperationInterface {

	public DomeOpeningState getState(String rt, String dome)
			throws TeleoperationException;

	public void setTracking(String rt, String dome, boolean mode)
			throws TeleoperationException;

	public boolean isTrackingEnabled(String rt, String dome)
			throws TeleoperationException;

	public double getAzimuth(String rt, String dome)
			throws TeleoperationException;

	public void open(String rt, String dome) throws TeleoperationException;

	public void close(String rt, String dome) throws TeleoperationException;

	public void park(String rt, String dome) throws TeleoperationException;

}
