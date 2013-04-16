package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.dome.DomeOpeningState;
import eu.gloria.rt.entity.device.ActivityStateDomeOpening;
import eu.gloria.rti.client.RTSException;
import eu.gloria.rti.client.RTSHandler;

public class Dome extends DeviceHandler implements DomeInterface {

	private RTSHandler rts;
	private String dome;

	public Dome(RTSHandler rts, String ccd) throws RTSException {

		this.rts = rts;
		this.dome = ccd;
	}

	@Override
	public void open() throws RTSException {
		rts.open(dome);
	}

	@Override
	public void close() throws RTSException {
		rts.close(dome);
	}

	@Override
	public void park() throws RTSException {
		rts.parkDome(dome);
	}

	@Override
	public double getAzimuth() throws RTSException {
		return rts.getDomeAzimuth(dome);
	}

	@Override
	public boolean isTrackingEnabled() throws RTSException {
		return rts.getDomeTracking(dome);
	}

	@Override
	public void setTracking(boolean mode) throws RTSException {
		rts.setDomeTracking(dome, mode);
	}

	@Override
	public DomeOpeningState getOpeningStatus() throws RTSException {
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
