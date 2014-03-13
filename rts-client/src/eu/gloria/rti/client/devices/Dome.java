package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.dome.DomeOpeningState;
import eu.gloria.rt.entity.device.ActivityStateDomeOpening;
import eu.gloria.rti.client.RTSHandler;

public class Dome extends DeviceHandler implements DomeInterface {

	private String dome;

	public Dome(RTSHandler rts, String ccd) throws TeleoperationException {

		super(rts);
		this.dome = ccd;
	}

	@Override
	public void open() throws TeleoperationException {
		rts.open(dome);
	}

	@Override
	public void close() throws TeleoperationException {
		rts.close(dome);
	}

	@Override
	public void park() throws TeleoperationException {
		rts.parkDome(dome);
	}

	@Override
	public double getAzimuth() throws TeleoperationException {
		return rts.getDomeAzimuth(dome);
	}

	@Override
	public boolean isTrackingEnabled() throws TeleoperationException {
		return rts.getDomeTracking(dome);
	}

	@Override
	public void setTracking(boolean mode) throws TeleoperationException {
		rts.setDomeTracking(dome, mode);
	}

	@Override
	public DomeOpeningState getOpeningStatus() throws TeleoperationException {
		ActivityStateDomeOpening state = rts.getDomeState(dome);

		if (state.equals(ActivityStateDomeOpening.OPEN)) {
			return DomeOpeningState.OPEN;
		} else if (state.equals(ActivityStateDomeOpening.CLOSE)) {
			return DomeOpeningState.CLOSED;
		} else if (state.equals(ActivityStateDomeOpening.OPENING)) {
			return DomeOpeningState.OPENING;
		} else if (state.equals(ActivityStateDomeOpening.CLOSING)) {
			return DomeOpeningState.CLOSING;
		}

		return DomeOpeningState.UNDEFINED;

	}

}
