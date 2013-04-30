package eu.gloria.gs.services.teleoperation.focuser;

import java.util.ArrayList;

import eu.gloria.gs.services.log.action.ActionLogException;
import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.focuser.operations.GetPositionOperation;
import eu.gloria.gs.services.teleoperation.focuser.operations.MoveAbsoluteOperation;
import eu.gloria.gs.services.teleoperation.focuser.operations.MoveRelativeOperation;

public class FocuserTeleoperation extends AbstractTeleoperation implements
		FocuserTeleoperationInterface {

	private void processException(String message, String rt) {
		try {
			this.logAction(this.getClientUsername(), "'" + rt + "' error: "
					+ message);
		} catch (ActionLogException e) {
			e.printStackTrace();
		}
	}

	@Override
	public long getPosition(String rt, String focuser)
			throws DeviceOperationFailedException,
			FocuserTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(focuser);

		GetPositionOperation operation = null;

		try {
			operation = new GetPositionOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/getPosition/Bad args", rt);

			throw new FocuserTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			return (Long) returns.getReturns().get(0);

		} catch (DeviceOperationFailedException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw new FocuserTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void moveAbsolute(String rt, String focuser, long position)
			throws DeviceOperationFailedException,
			FocuserTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(focuser);
		args.getArguments().add(position);

		MoveAbsoluteOperation operation = null;

		try {
			operation = new MoveAbsoluteOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/moveAbsolute/Bad args", rt);

			throw new FocuserTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);
		} catch (DeviceOperationFailedException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw new FocuserTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void moveRelative(String rt, String focuser, long steps)
			throws DeviceOperationFailedException,
			FocuserTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(focuser);
		args.getArguments().add(steps);

		MoveRelativeOperation operation = null;

		try {
			operation = new MoveRelativeOperation(args);
		} catch (Exception e) {
			this.processException(e.getClass().getSimpleName()
					+ "/moveRelative/Bad args", rt);

			throw new FocuserTeleoperationException(
					"DEBUG: Bad teleoperation request");
		}

		try {
			this.executeOperation(operation);
		} catch (DeviceOperationFailedException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw e;
		} catch (TeleoperationException e) {
			this.processException(
					e.getClass().getSimpleName() + "/" + e.getMessage(), rt);
			throw new FocuserTeleoperationException(e.getMessage());
		}
	}
}
