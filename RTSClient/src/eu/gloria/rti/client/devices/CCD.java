package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.ccd.CCDState;
import eu.gloria.gs.services.teleoperation.ccd.ImageExtensionFormat;
import eu.gloria.rt.entity.device.ActivityContinueStateCamera;
import eu.gloria.rti.client.RTSHandler;

public class CCD extends DeviceHandler implements CCDInterface {

	private String ccd;

	public CCD(RTSHandler rts, String ccd) throws TeleoperationException {

		super(rts);
		this.ccd = ccd;
	}

	@Override
	public void setExposureTime(double value) throws TeleoperationException {
		rts.setExposureTime(this.ccd, value);
	}

	@Override
	public double getExposureTime() throws TeleoperationException {
		return rts.getExposureTime(this.ccd);
	}

	@Override
	public void setContrast(long value) throws TeleoperationException {
		rts.setContrast(this.ccd, value);
	}

	@Override
	public long getContrast() throws TeleoperationException {
		return rts.getContrast(this.ccd);
	}

	@Override
	public void setAutoExposure(boolean mode) throws TeleoperationException {
		rts.setAutoExposure(this.ccd, mode);
	}

	@Override
	public boolean getAutoExposure() throws TeleoperationException {
		return rts.getAutoExposure(this.ccd);
	}

	@Override
	public void setBrightness(long value) throws TeleoperationException {
		rts.setBrightness(this.ccd, value);
	}

	@Override
	public long getBrightness() throws TeleoperationException {
		return rts.getBrightness(this.ccd);
	}

	@Override
	public void setGain(long value) throws TeleoperationException {
		rts.setGain(this.ccd, value);
	}

	@Override
	public long getGain() throws TeleoperationException {
		return rts.getGain(this.ccd);
	}

	@Override
	public void setAutoGain(boolean mode) throws TeleoperationException {
		rts.setAutoGain(this.ccd, mode);
	}

	@Override
	public boolean getAutoGain() throws TeleoperationException {
		return rts.getAutoGain(this.ccd);
	}

	@Override
	public String startExposure() throws TeleoperationException {
		rts.startTeleoperation();
		return rts.startExposure(this.ccd);
	}

	@Override
	public String startContinueMode() throws TeleoperationException {
		rts.startTeleoperation();
		return rts.startContinueMode(this.ccd);
	}

	@Override
	public void stopContinueMode() throws TeleoperationException {		
		rts.stopContinueMode(this.ccd);
	}

	@Override
	public String getImageURL(String id, ImageExtensionFormat format) throws TeleoperationException {
		return rts.getImageURL(this.ccd, id, format);
	}

	@Override
	public CCDState getState() throws TeleoperationException {

		ActivityContinueStateCamera state = rts.getCCDState(this.ccd);

		if (state.equals(ActivityContinueStateCamera.EXPOSING)) {
			return CCDState.EXPOSING;
		}

		return CCDState.UNDEFINED;
	}

	@Override
	public void setGamma(long value) throws TeleoperationException {
		rts.setGamma(this.ccd, value);
		
	}

	@Override
	public long getGamma() throws TeleoperationException {
		return rts.getGamma(this.ccd);
	}
}
