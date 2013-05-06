package eu.gloria.gs.services.teleoperation.mount;

import java.util.ArrayList;
import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.MountTeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.MountTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.mount.operations.GetStateOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.MoveEastOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.MoveNorthOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.MoveSouthOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.MoveWestOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.ParkOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.SetSlewRateOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.SetTrackingOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.SetTrackingRateOperation;
import eu.gloria.gs.services.teleoperation.mount.operations.SlewToObjectOperation;

public class MountTeleoperation extends AbstractTeleoperation implements
		MountTeleoperationInterface {

	@Override
	public MountState getState(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		GetStateOperation operation = null;

		try {
			operation = new GetStateOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getMountState/Bad args", rt);

			throw new MountTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			MountState state = (MountState) returns.getReturns().get(0);
			this.processSuccess(rt, mount, "getState", null, state.name());

			return state;

		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new MountTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void moveNorth(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		MoveNorthOperation operation = null;

		try {
			operation = new MoveNorthOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/moveNorth/Bad args", rt);

			throw new MountTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {

			this.executeOperation(operation);
			this.processSuccess(rt, mount, "moveNorth", null, null);
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new MountTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void moveSouth(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		MoveSouthOperation operation = null;

		try {
			operation = new MoveSouthOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/moveSouth/Bad args", rt);

			throw new MountTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);
			this.processSuccess(rt, mount, "moveSouth", null, null);
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new MountTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void moveEast(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		MoveEastOperation operation = null;

		try {
			operation = new MoveEastOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/moveEast/Bad args", rt);

			throw new MountTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);
			this.processSuccess(rt, mount, "moveEast", null, null);
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new MountTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void moveWest(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		MoveWestOperation operation = null;

		try {
			operation = new MoveWestOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/moveWest/Bad args", rt);

			throw new MountTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);
			this.processSuccess(rt, mount, "moveWest", null, null);
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new MountTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setSlewRate(String rt, String mount, String rate)
			throws DeviceOperationFailedException, MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);
		args.getArguments().add(rate);

		SetSlewRateOperation operation = null;

		try {
			operation = new SetSlewRateOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/setSlewRate/Bad args", rt);

			throw new MountTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {

			this.executeOperation(operation);
			this.processSuccess(rt, mount, "setSlewRate",
					new Object[] { rate }, null);
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new MountTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void slewToObject(String rt, String mount, String object)
			throws DeviceOperationFailedException, MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);
		args.getArguments().add(object);

		SlewToObjectOperation operation = null;

		try {
			operation = new SlewToObjectOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/slewToObject/Bad args", rt);

			throw new MountTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {

			this.executeOperation(operation);

			this.processSuccess(rt, mount, "slewToObject",
					new Object[] { object }, null);
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new MountTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void park(String rt, String mount)
			throws DeviceOperationFailedException, MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);

		ParkOperation operation = null;

		try {
			operation = new ParkOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/parkMount/Bad args", rt);

			throw new MountTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, mount, "park", null, null);
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new MountTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setTrackingRate(String rt, String mount, TrackingRate rate)
			throws DeviceOperationFailedException, MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);
		args.getArguments().add(rate);

		SetTrackingRateOperation operation = null;

		try {
			operation = new SetTrackingRateOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/setTrackingRate/Bad args", rt);

			throw new MountTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, mount, "setTrackingRate",
					new Object[] { rate.name() }, null);
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new MountTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void setTracking(String rt, String mount, boolean mode)
			throws DeviceOperationFailedException, MountTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(mount);
		args.getArguments().add(mode);

		SetTrackingOperation operation = null;

		try {
			operation = new SetTrackingOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/setTracking/Bad args", rt);

			throw new MountTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, mount, "setTracking",
					new Object[] { mode }, null);
		} catch (DeviceOperationFailedException e) {
			this.processException(e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(e.getMessage(), rt);
			throw new MountTeleoperationException(e.getMessage());
		}
	}
}
