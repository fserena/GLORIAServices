package eu.gloria.gs.services.teleoperation.dome;

import javax.jws.WebService;

@WebService(name = "DomeTeleoperationInterface", targetNamespace = "http://dome.teleoperation.services.gs.gloria.eu/")
public interface DomeTeleoperationInterface {

	public DomeOpeningState getState(String rt, String dome)
			throws DomeTeleoperationException;

	public void setTracking(String rt, String dome, boolean mode)
			throws DomeTeleoperationException;

	public boolean isTrackingEnabled(String rt, String dome)
			throws DomeTeleoperationException;

	public double getAzimuth(String rt, String dome)
			throws DomeTeleoperationException;

	public void open(String rt, String dome) throws DomeTeleoperationException;

	public void close(String rt, String dome) throws DomeTeleoperationException;

	public void park(String rt, String dome) throws DomeTeleoperationException;

}
