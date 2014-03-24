package eu.gloria.gs.services.teleoperation.mount;


import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.operations.GetDECOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.GetRAOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.GetStateOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.MoveEastOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.MoveNorthOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.MoveSouthOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.MoveWestOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.ParkOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.SetSlewRateOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.SetTrackingOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.SetTrackingRateOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.SlewToCoordinatesOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.SlewToObjectOperation;

public class MountTeleoperation extends AbstractTeleoperation implements
		MountTeleoperationInterface {

	public MountTeleoperation() {
		super(MountTeleoperation.class.getSimpleName());
	}
	
	@Override
	public MountState getState(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		try {
			return (MountState) this.invokeGetOperation(GetStateOperation.class, rt, mount);
		} catch (TeleoperationException e) {
			throw new MountTeleoperationException(e.getAction());
		}		
	}

	@Override
	public void moveNorth(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		try {
			this.invokeSetOperation(MoveNorthOperation.class, rt, mount);
		} catch (TeleoperationException e) {
			throw new MountTeleoperationException(e.getAction());
		}			
	}

	@Override
	public void moveSouth(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		try {
			this.invokeSetOperation(MoveSouthOperation.class, rt, mount);
		} catch (TeleoperationException e) {
			throw new MountTeleoperationException(e.getAction());
		}	
	}

	@Override
	public void moveEast(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		try {
			this.invokeSetOperation(MoveEastOperation.class, rt, mount);
		} catch (TeleoperationException e) {
			throw new MountTeleoperationException(e.getAction());
		}	
	}

	@Override
	public void moveWest(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		try {
			this.invokeSetOperation(MoveWestOperation.class, rt, mount);
		} catch (TeleoperationException e) {
			throw new MountTeleoperationException(e.getAction());
		}	
	}

	@Override
	public void setSlewRate(String rt, String mount, String rate)
			throws DeviceOperationFailedException, MountTeleoperationException {
		try {
			this.invokeSetOperation(SetSlewRateOperation.class, rt, mount, rate);
		} catch (TeleoperationException e) {
			throw new MountTeleoperationException(e.getAction());
		}			
	}

	@Override
	public void slewToObject(String rt, String mount, String object)
			throws DeviceOperationFailedException, MountTeleoperationException {
		try {
			this.invokeSetOperation(SlewToObjectOperation.class, rt, mount, object);
		} catch (TeleoperationException e) {
			throw new MountTeleoperationException(e.getAction());
		}		
	}
	
	@Override
	public void slewToCoordinates(String rt, String mount, double ra, double dec)
			throws DeviceOperationFailedException, MountTeleoperationException {
		try {
			this.invokeSetOperation(SlewToCoordinatesOperation.class, rt, mount, ra, dec);
		} catch (TeleoperationException e) {
			throw new MountTeleoperationException(e.getAction());
		}		
	}

	@Override
	public void park(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		try {
			this.invokeSetOperation(ParkOperation.class, rt, mount);
		} catch (TeleoperationException e) {
			throw new MountTeleoperationException(e.getAction());
		}		
	}

	@Override
	public void setTrackingRate(String rt, String mount, TrackingRate rate)
			throws DeviceOperationFailedException, MountTeleoperationException {
		try {
			this.invokeSetOperation(SetTrackingRateOperation.class, rt, mount, rate);
		} catch (TeleoperationException e) {
			throw new MountTeleoperationException(e.getAction());
		}		
	}

	@Override
	public void setTracking(String rt, String mount, boolean mode)
			throws DeviceOperationFailedException, MountTeleoperationException {
		try {
			this.invokeSetOperation(SetTrackingOperation.class, rt, mount, mode);
		} catch (TeleoperationException e) {
			throw new MountTeleoperationException(e.getAction());
		}		
	}

	@Override
	public double getRA(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		try {
			return (Double) this.invokeGetOperation(GetRAOperation.class, rt, mount);
		} catch (TeleoperationException e) {
			throw new MountTeleoperationException(e.getAction());
		}
	}

	@Override
	public double getDEC(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		try {
			return (Double) this.invokeGetOperation(GetDECOperation.class, rt, mount);
		} catch (TeleoperationException e) {
			throw new MountTeleoperationException(e.getAction());
		}		
	}
}
