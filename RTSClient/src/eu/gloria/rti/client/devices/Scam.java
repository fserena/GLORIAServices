package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.scam.SCamState;
import eu.gloria.rt.entity.device.ActivityContinueStateCamera;
import eu.gloria.rti.client.RTSException;
import eu.gloria.rti.client.RTSHandler;

public class Scam extends DeviceHandler implements SCamInteface {

	private RTSHandler rts;
	private String camera;

	public Scam(RTSHandler rts, String scam) throws RTSException {

		this.rts = rts;
		this.camera = scam;
	}

	@Override
	public void setExposureTime(double value) throws RTSException {
		rts.setExposureTime(camera, value);
	}

	@Override
	public double getExposureTime() throws RTSException {
		return rts.getExposureTime(camera);
	}

	@Override
	public void setContrast(long value) throws RTSException {
		rts.setContrast(camera, value);
	}

	@Override
	public long getContrast() throws RTSException {
		return rts.getContrast(camera);
	}

	@Override
	public void setBrightness(long value) throws RTSException {
		rts.setBrightness(camera, value);
	}

	@Override
	public long getBrightness() throws RTSException {
		return rts.getBrightness(camera);
	}

	@Override
	public void setGain(long value) throws RTSException {
		rts.setGain(camera, value);
	}

	@Override
	public long getGain() throws RTSException {
		return rts.getGain(camera);
	}

	@Override
	public String getImageURL() throws RTSException {
		return rts.getImageURL(camera, null, null);
	}

	@Override
	public SCamState getState() throws RTSException {
		ActivityContinueStateCamera state = rts.getCCDState(this.camera);

		if (state.equals(ActivityContinueStateCamera.EXPOSING)) {
			return SCamState.EXPOSING;
		}

		return SCamState.UNDEFINED;
	}
}
