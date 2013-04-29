package eu.gloria.gs.services.teleoperation.mount;

import java.util.ArrayList;

import eu.gloria.gs.services.log.action.ActionLogException;
import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
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

	private void processException(String message, String rt) {
		try {
			this.logAction(this.getClientUsername(), "'" + rt + "' error: "
					+ message);
		} catch (ActionLogException e) {
			e.printStackTrace();
		}
	}

	@Override
	public MountState getState(String rt, String mount)
			throws TeleoperationException {
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
			return (MountState) returns.getReturns().get(0);

		} catch (TeleoperationException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw e;
		}
	}

	@Override
	public void moveNorth(String rt, String mount)
			throws TeleoperationException {
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
		} catch (TeleoperationException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw e;
		}
	}

	@Override
	public void moveSouth(String rt, String mount)
			throws TeleoperationException {
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
		} catch (TeleoperationException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw e;
		}
	}

	@Override
	public void moveEast(String rt, String mount) throws TeleoperationException {
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
		} catch (TeleoperationException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw e;
		}
	}

	@Override
	public void moveWest(String rt, String mount) throws TeleoperationException {
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
		} catch (TeleoperationException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw e;
		}
	}

	@Override
	public void setSlewRate(String rt, String mount, String rate)
			throws TeleoperationException {
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
		} catch (TeleoperationException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw e;
		}
	}

	@Override
	public void slewToObject(String rt, String mount, String object)
			throws TeleoperationException {
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
		} catch (TeleoperationException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw e;
		}
	}

	@Override
	public void park(String rt, String mount) throws TeleoperationException {
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
		} catch (TeleoperationException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw e;
		}

	}

	@Override
	public void setTrackingRate(String rt, String mount, TrackingRate rate)
			throws TeleoperationException {
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
		} catch (TeleoperationException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw e;
		}
	}

	@Override
	public void setTracking(String rt, String mount, boolean mode)
			throws TeleoperationException {
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
		} catch (TeleoperationException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw e;
		}
	}
}
