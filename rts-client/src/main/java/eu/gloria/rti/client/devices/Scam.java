package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.scam.SCamState;
import eu.gloria.rt.entity.device.ActivityContinueStateCamera;
import eu.gloria.rti.client.RTSHandler;

public class Scam extends DeviceHandler implements SCamInteface {

	private String camera;

	public Scam(RTSHandler rts, String scam) throws TeleoperationException {

		super(rts);
		this.camera = scam;
	}

	@Override
	public void setExposureTime(double value) throws TeleoperationException {
		rts.setExposureTime(camera, value);
	}

	@Override
	public double getExposureTime() throws TeleoperationException {
		return rts.getExposureTime(camera);
	}

	@Override
	public void setContrast(long value) throws TeleoperationException {
		rts.setContrast(camera, value);
	}

	@Override
	public long getContrast() throws TeleoperationException {
		return rts.getContrast(camera);
	}

	@Override
	public void setBrightness(long value) throws TeleoperationException {
		rts.setBrightness(camera, value);
	}

	@Override
	public long getBrightness() throws TeleoperationException {
		return rts.getBrightness(camera);
	}

	@Override
	public void setGain(long value) throws TeleoperationException {
		rts.setGain(camera, value);
	}

	@Override
	public long getGain() throws TeleoperationException {
		return rts.getGain(camera);
	}

	@Override
	public String getImageURL() throws TeleoperationException {
		return rts.getImageURL(camera, null, null);
	}

	@Override
	public SCamState getState() throws TeleoperationException {
		ActivityContinueStateCamera state = rts.getCCDState(this.camera);

		if (state.equals(ActivityContinueStateCamera.EXPOSING)) {
			return SCamState.EXPOSING;
		}

		return SCamState.UNDEFINED;
	}
}
