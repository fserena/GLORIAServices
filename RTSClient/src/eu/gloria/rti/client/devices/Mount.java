package eu.gloria.rti.client.devices;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.MountState;
import eu.gloria.gs.services.teleoperation.mount.TrackingRate;
import eu.gloria.rt.entity.device.ActivityStateMount;
import eu.gloria.rti.client.RTSHandler;

public class Mount extends DeviceHandler implements MountInterface {

	private String mount;

	public Mount(RTSHandler rts, String mount) throws TeleoperationException {

		super(rts);
		this.mount = mount;
	}

	@Override
	public void moveNorth() throws TeleoperationException {
		rts.moveNorth(mount);
	}

	@Override
	public void moveSouth() throws TeleoperationException {
		rts.moveSouth(mount);
	}

	@Override
	public void moveEast() throws TeleoperationException {
		rts.moveEast(mount);
	}

	@Override
	public void moveWest() throws TeleoperationException {
		rts.moveWest(mount);
	}

	@Override
	public void setSlewRate(String rate) throws TeleoperationException {
		rts.setSlewRate(mount, rate);
	}

	@Override
	public void setTrackingRate(TrackingRate rate)
			throws TeleoperationException {
		rts.setTrackingRate(mount, rate);
	}

	@Override
	public void setTracking(boolean mode) throws TeleoperationException {
		rts.setMountTracking(mount, mode);
	}

	@Override
	public void slewToObject(String object) throws TeleoperationException {
		rts.slewToObject(mount, object);
	}

	@Override
	public void slewToCoordinates(double ra, double dec)
			throws TeleoperationException {
		rts.slewToCoordinates(mount, ra, dec);
	}

	@Override
	public MountState getState() throws TeleoperationException {
		ActivityStateMount state = rts.getMountState(mount);

		if (state.equals(ActivityStateMount.MOVING)) {
			return MountState.MOVING;
		} else if (state.equals(ActivityStateMount.TRACKING)) {
			return MountState.TRACKING;
		} else if (state.equals(ActivityStateMount.NOT_DEFINED)) {
			return MountState.UNDEFINED;
		} else if (state.equals(ActivityStateMount.PARKED)) {
			return MountState.PARKED;
		} else if (state.equals(ActivityStateMount.PARKING)) {
			return MountState.PARKING;
		} else if (state.equals(ActivityStateMount.READY)) {
			return MountState.READY;
		}

		return MountState.UNDEFINED;

	}

	@Override
	public void park() throws TeleoperationException {
		rts.parkMount(mount);
	}

}
