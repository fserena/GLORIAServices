package eu.gloria.gs.services.teleoperation.scam;

import javax.jws.WebService;

import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;

@WebService(name = "SCamTeleoperationInterface", targetNamespace = "http://scam.teleoperation.services.gs.gloria.eu/")
public interface SCamTeleoperationInterface {

	public String getImageURL(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException;

	public void setExposureTime(String rt, String scam, double value)
			throws DeviceOperationFailedException, SCamTeleoperationException;

	public double getExposureTime(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException;

	public void setBrightness(String rt, String scam, long value)
			throws DeviceOperationFailedException, SCamTeleoperationException;

	public long getBrightness(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException;

	public void setContrast(String rt, String scam, long value)
			throws DeviceOperationFailedException, SCamTeleoperationException;

	public long getContrast(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException;

	public void setGain(String rt, String scam, long value)
			throws DeviceOperationFailedException, SCamTeleoperationException;

	public long getGain(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException;

	public SCamState getState(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException;
}
