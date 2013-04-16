package eu.gloria.gs.services.teleoperation.scam;

import javax.jws.WebService;

@WebService(name = "SCamTeleoperationInterface", targetNamespace = "http://scam.teleoperation.services.gs.gloria.eu/")
public interface SCamTeleoperationInterface {

	public String getImageURL(String rt, String scam)
			throws SCamTeleoperationException;

	public void setExposureTime(String rt, String scam, double value)
			throws SCamTeleoperationException;

	public double getExposureTime(String rt, String scam)
			throws SCamTeleoperationException;

	public void setBrightness(String rt, String scam, long value)
			throws SCamTeleoperationException;

	public long getBrightness(String rt, String scam)
			throws SCamTeleoperationException;

	public void setContrast(String rt, String scam, long value)
			throws SCamTeleoperationException;

	public long getContrast(String rt, String scam)
			throws SCamTeleoperationException;

	public void setGain(String rt, String scam, long value)
			throws SCamTeleoperationException;

	public long getGain(String rt, String scam) throws SCamTeleoperationException;

	public SCamState getState(String rt, String scam)
			throws SCamTeleoperationException;
}
