package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.gs.services.teleoperation.ccd.CCDState;
import eu.gloria.gs.services.teleoperation.ccd.ImageExtensionFormat;
import eu.gloria.rt.entity.device.ActivityContinueStateCamera;
import eu.gloria.rti.client.RTSHandler;

public class CCD extends DeviceHandler implements CCDInterface {

	private RTSHandler rts;
	private String ccd;

	public CCD(RTSHandler rts, String ccd) throws RTSException {

		this.rts = rts;
		this.ccd = ccd;
	}

	@Override
	public void setExposureTime(double value) throws RTSException {
		rts.setExposureTime(this.ccd, value);
	}

	@Override
	public double getExposureTime() throws RTSException {
		return rts.getExposureTime(this.ccd);
	}

	@Override
	public void setContrast(long value) throws RTSException {
		rts.setContrast(this.ccd, value);
	}

	@Override
	public long getContrast() throws RTSException {
		return rts.getContrast(this.ccd);
	}

	@Override
	public void setAutoExposure(boolean mode) throws RTSException {
		rts.setAutoExposure(this.ccd, mode);
	}

	@Override
	public boolean getAutoExposure() throws RTSException {
		return rts.getAutoExposure(this.ccd);
	}

	@Override
	public void setBrightness(long value) throws RTSException {
		rts.setBrightness(this.ccd, value);
	}

	@Override
	public long getBrightness() throws RTSException {
		return rts.getBrightness(this.ccd);
	}

	@Override
	public void setGain(long value) throws RTSException {
		rts.setGain(this.ccd, value);
	}

	@Override
	public long getGain() throws RTSException {
		return rts.getGain(this.ccd);
	}

	@Override
	public void setAutoGain(boolean mode) throws RTSException {
		rts.setAutoGain(this.ccd, mode);
	}

	@Override
	public boolean getAutoGain() throws RTSException {
		return rts.getAutoGain(this.ccd);
	}

	@Override
	public String startExposure() throws RTSException {
		return rts.startExposure(this.ccd);
	}

	@Override
	public String startContinueMode() throws RTSException {
		return rts.startContinueMode(this.ccd);
	}

	@Override
	public void stopContinueMode() throws RTSException {
		rts.stopContinueMode(this.ccd);
	}

	@Override
	public String getImageURL(String id, ImageExtensionFormat format) throws RTSException {
		return rts.getImageURL(this.ccd, id, format);
	}

	@Override
	public CCDState getState() throws RTSException {

		ActivityContinueStateCamera state = rts.getCCDState(this.ccd);

		if (state.equals(ActivityContinueStateCamera.EXPOSING)) {
			return CCDState.EXPOSING;
		}

		return CCDState.UNDEFINED;
	}
}
