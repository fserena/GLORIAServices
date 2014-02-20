package eu.gloria.gs.services.teleoperation.focuser;

import java.util.ArrayList;

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

	public FocuserTeleoperation() {
		this.createLogger(FocuserTeleoperation.class);
	}
	
	@Override
	public long getPosition(String rt, String focuser)
			throws DeviceOperationFailedException,
			FocuserTeleoperationException {
		
		String operationName = "get position";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(focuser);

		GetPositionOperation operation = null;

		try {
			operation = new GetPositionOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, focuser, operationName, args.getArguments());

			throw new FocuserTeleoperationException(
					"bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			long position = (Long) returns.getReturns().get(0);

			this.processSuccess(rt, focuser, operationName, args.getArguments(),
					position);

			return position;

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, focuser, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, focuser, operationName, args.getArguments());
			throw new FocuserTeleoperationException(e.getAction());
		}
	}

	@Override
	public void moveAbsolute(String rt, String focuser, long position)
			throws DeviceOperationFailedException,
			FocuserTeleoperationException {
		
		String operationName = "move absolute";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(focuser);
		args.getArguments().add(position);

		MoveAbsoluteOperation operation = null;

		try {
			operation = new MoveAbsoluteOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, focuser, operationName, args.getArguments());

			throw new FocuserTeleoperationException(
					"bad request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, focuser, operationName, args.getArguments(),
					null);

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, focuser, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, focuser, operationName, args.getArguments());
			throw new FocuserTeleoperationException(e.getAction());
		}
	}

	@Override
	public void moveRelative(String rt, String focuser, long steps)
			throws DeviceOperationFailedException,
			FocuserTeleoperationException {
		
		String operationName = "move relative";
		
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(focuser);
		args.getArguments().add(steps);

		MoveRelativeOperation operation = null;

		try {
			operation = new MoveRelativeOperation(args);
		} catch (Exception e) {
			this.processBadArgs(rt, focuser, operationName, args.getArguments());

			throw new FocuserTeleoperationException(
					"bad request");
		}

		try {
			this.executeOperation(operation);

			this.processSuccess(rt, focuser, operationName, args.getArguments(),
					null);

		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(e, rt, focuser, operationName, args.getArguments());
			throw e;
		} catch (TeleoperationException e) {
			this.processInternalError(e, rt, focuser, operationName, args.getArguments());
			throw new FocuserTeleoperationException(e.getAction());
		}
	}
}
