package eu.gloria.gs.services.teleoperation.scam;

import javax.jws.WebService;

import eu.gloria.gs.services.teleoperation.base.TeleoperationException;

@WebService(name = "SCamTeleoperationInterface", targetNamespace = "http://scam.teleoperation.services.gs.gloria.eu/")
public interface SCamTeleoperationInterface {

	public String getImageURL(String rt, String scam)
			throws TeleoperationException;

	public void setExposureTime(String rt, String scam, double value)
			throws TeleoperationException;

	public double getExposureTime(String rt, String scam)
			throws TeleoperationException;

	public void setBrightness(String rt, String scam, long value)
			throws TeleoperationException;

	public long getBrightness(String rt, String scam)
			throws TeleoperationException;

	public void setContrast(String rt, String scam, long value)
			throws TeleoperationException;

	public long getContrast(String rt, String scam)
			throws TeleoperationException;

	public void setGain(String rt, String scam, long value)
			throws TeleoperationException;

	public long getGain(String rt, String scam) throws TeleoperationException;

	public SCamState getState(String rt, String scam)
			throws TeleoperationException;
}
