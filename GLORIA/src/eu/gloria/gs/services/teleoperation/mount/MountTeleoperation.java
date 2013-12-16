package eu.gloria.gs.services.teleoperation.mount;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.MountTeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.MountTeleoperationInterface;
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

	@Override
	public MountState getState(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		
		String operationName = "get state";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		GetStateOperation operation = null;

		try {
			operation = new GetStateOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, mount, operationName, args.getArguments());

			throw new MountTeleoperationException(
					"bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			MountState state = (MountState) returns.getReturns().get(0);
			this.processSuccess(rt, mount, operationName, args.getArguments(),
					state);

			return state;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, mount, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, mount, operationName, args.getArguments());
			throw new MountTeleoperationException(e.getAction());
		}
	}

	@Override
	public void moveNorth(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		
		String operationName = "move north";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		MoveNorthOperation operation = null;

		try {
			operation = new MoveNorthOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, mount, operationName, args.getArguments());

			throw new MountTeleoperationException(
					"bad request");
		}

		try {

			this.executeOperation(operation);
			this.processSuccess(rt, mount, operationName, args.getArguments(),
					null);
		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, mount, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, mount, operationName, args.getArguments());
			throw new MountTeleoperationException(e.getAction());
		}
	}

	@Override
	public void moveSouth(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		
		String operationName = "move south";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		MoveSouthOperation operation = null;

		try {
			operation = new MoveSouthOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, mount, operationName, args.getArguments());

			throw new MountTeleoperationException(
					"bad request");
		}

		try {
			this.executeOperation(operation);
			this.processSuccess(rt, mount, operationName, args.getArguments(),
					null);
		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, mount, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, mount, operationName, args.getArguments());
			throw new MountTeleoperationException(e.getAction());
		}
	}

	@Override
	public void moveEast(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		
		String operationName = "move east";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		MoveEastOperation operation = null;

		try {
			operation = new MoveEastOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, mount, operationName, args.getArguments());

			throw new MountTeleoperationException(
					"bad request");
		}

		try {
			this.executeOperation(operation);
			this.processSuccess(rt, mount, operationName, args.getArguments(),
					null);
		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, mount, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, mount, operationName, args.getArguments());
			throw new MountTeleoperationException(e.getAction());
		}
	}

	@Override
	public void moveWest(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		
		String operationName = "move west";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		MoveWestOperation operation = null;

		try {
			operation = new MoveWestOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, mount, operationName, args.getArguments());

			throw new MountTeleoperationException(
					"bad request");
		}

		try {
			this.executeOperation(operation);
			this.processSuccess(rt, mount, operationName, args.getArguments(),
					null);
		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, mount, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, mount, operationName, args.getArguments());
			throw new MountTeleoperationException(e.getAction());
		}
	}

	@Override
	public void setSlewRate(String rt, String mount, String rate)
			throws DeviceOperationFailedException, MountTeleoperationException {
		
		String operationName = "set slew rate";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);
		args.getArguments().add(rate);

		SetSlewRateOperation operation = null;

		try {
			operation = new SetSlewRateOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, mount, operationName, args.getArguments());

			throw new MountTeleoperationException(
					"bad request");
		}

		try {

			this.executeOperation(operation);
			this.processSuccess(rt, mount, operationName, args.getArguments(),
					null);
		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, mount, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, mount, operationName, args.getArguments());
			throw new MountTeleoperationException(e.getAction());
		}
	}

	@Override
	public void slewToObject(String rt, String mount, String object)
			throws DeviceOperationFailedException, MountTeleoperationException {
		
		String operationName = "slew to object";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);
		args.getArguments().add(object);

		SlewToObjectOperation operation = null;

		try {
			operation = new SlewToObjectOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, mount, operationName, args.getArguments());

			throw new MountTeleoperationException(
					"bad request");
		}

		try {

			this.executeOperation(operation);

			this.processSuccess(rt, mount, operationName, args.getArguments(),
					null);
		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, mount, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, mount, operationName, args.getArguments());
			throw new MountTeleoperationException(e.getAction());
		}
	}
	
	@Override
	public void slewToCoordinates(String rt, String mount, double ra, double dec)
			throws DeviceOperationFailedException, MountTeleoperationException {
		
		String operationName = "slew to coordinates";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);
		args.getArguments().add(ra);
		args.getArguments().add(dec);

		SlewToCoordinatesOperation operation = null;

		try {
			operation = new SlewToCoordinatesOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, mount, operationName, args.getArguments());

			throw new MountTeleoperationException(
					"bad request");
		}

		try {

			this.executeOperation(operation);

			this.processSuccess(rt, mount, operationName, args.getArguments(),
					null);
		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, mount, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, mount, operationName, args.getArguments());
			throw new MountTeleoperationException(e.getAction());
		}
	}

	@Override
	public void park(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		
		String operationName = "park";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		ParkOperation operation = null;

		try {
			operation = new ParkOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, mount, operationName, args.getArguments());

			throw new MountTeleoperationException(
					"bad request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, mount, operationName, args.getArguments(),
					null);
		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, mount, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, mount, operationName, args.getArguments());
			throw new MountTeleoperationException(e.getAction());
		}
	}

	@Override
	public void setTrackingRate(String rt, String mount, TrackingRate rate)
			throws DeviceOperationFailedException, MountTeleoperationException {
		
		String operationName = "set tracking rate";
			
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);
		args.getArguments().add(rate);

		SetTrackingRateOperation operation = null;

		try {
			operation = new SetTrackingRateOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, mount, operationName, args.getArguments());

			throw new MountTeleoperationException(
					"bad request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, mount, operationName, args.getArguments(),
					null);
		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, mount, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, mount, operationName, args.getArguments());
			throw new MountTeleoperationException(e.getAction());
		}
	}

	@Override
	public void setTracking(String rt, String mount, boolean mode)
			throws DeviceOperationFailedException, MountTeleoperationException {
		
		String operationName = "set tracking";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);
		args.getArguments().add(mode);

		SetTrackingOperation operation = null;

		try {
			operation = new SetTrackingOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, mount, operationName, args.getArguments());

			throw new MountTeleoperationException(
					"bad request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, mount, operationName, args.getArguments(),
					null);
		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, mount, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, mount, operationName, args.getArguments());
			throw new MountTeleoperationException(e.getAction());
		}
	}

	/* (non-Javadoc)
	 * @see eu.gloria.gs.services.teleoperation.mount.MountTeleoperationInterface#getRA(java.lang.String, java.lang.String)
	 */
	@Override
	public double getRA(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		
		String operationName = "get ra";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		GetRAOperation operation = null;

		try {
			operation = new GetRAOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, mount, operationName, args.getArguments());

			throw new MountTeleoperationException(
					"bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			double ra = (Double) returns.getReturns().get(0);
			this.processSuccess(rt, mount, operationName, args.getArguments(),
					ra);

			return ra;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, mount, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, mount, operationName, args.getArguments());
			throw new MountTeleoperationException(e.getAction());
		}
	}

	/* (non-Javadoc)
	 * @see eu.gloria.gs.services.teleoperation.mount.MountTeleoperationInterface#getDEC(java.lang.String, java.lang.String)
	 */
	@Override
	public double getDEC(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		
		String operationName = "get dec";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		GetDECOperation operation = null;

		try {
			operation = new GetDECOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, mount, operationName, args.getArguments());

			throw new MountTeleoperationException(
					"bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			double dec = (Double) returns.getReturns().get(0);
			this.processSuccess(rt, mount, operationName, args.getArguments(),
					dec);

			return dec;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, mount, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, mount, operationName, args.getArguments());
			throw new MountTeleoperationException(e.getAction());
		}
	}
}
