package eu.gloria.gs.services.teleoperation.focuser;

import java.util.ArrayList;

import eu.gloria.gs.services.log.action.ActionLogException;
import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.focuser.operations.GetPositionOperation;
import eu.gloria.gs.services.teleoperation.focuser.operations.MoveAbsoluteOperation;
import eu.gloria.gs.services.teleoperation.focuser.operations.MoveRelativeOperation;

public class FocuserTeleoperation extends AbstractTeleoperation implements
		FocuserTeleoperationInterface {

	private void processOperationException(String message, String rt,
			String focus, String operation) {
		try {
			this.logAction(this.getClientUsername(), "Error while trying to "
					+ operation + " of '" + focus + "' " + " of '"
					+ rt + "': " + message);
		} catch (ActionLogException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public long getPosition(String rt, String focuser)
			throws FocuserTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(focuser);

		try {
			GetPositionOperation operation = new GetPositionOperation(args);

			OperationReturn returns = this.executeOperation(operation);
			return (Long) returns.getReturns().get(0);

		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, focuser,
					"get position");
			throw new FocuserTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void moveAbsolute(String rt, String focuser, long position)
			throws FocuserTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(focuser);
		args.getArguments().add(position);

		try {
			MoveAbsoluteOperation operation = new MoveAbsoluteOperation(args);
			this.executeOperation(operation);
		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, focuser,
					"move absolute");
			throw new FocuserTeleoperationException(e.getMessage());
		}
	}

	@Override
	public void moveRelative(String rt, String focuser, long steps)
			throws FocuserTeleoperationException {
		OperationArgs args = new OperationArgs();

		args.setArguments(new ArrayList<Object>());
		args.getArguments().add(rt);
		args.getArguments().add(focuser);
		args.getArguments().add(steps);

		try {
			MoveRelativeOperation operation = new MoveRelativeOperation(args);
			this.executeOperation(operation);
		} catch (Exception e) {
			this.processOperationException(e.getMessage(), rt, focuser,
					"move relative");
			throw new FocuserTeleoperationException(e.getMessage());
		}
	}
}
