package eu.gloria.gs.services.teleoperation.focuser.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.rti.client.devices.Focuser;

public class MoveRelativeOperation extends FocuserOperation {

	private long steps;

	public MoveRelativeOperation(OperationArgs args) throws Exception {
		super(args);

		this.steps = (Long) args.getArguments().get(2);
	}

	@Override
	protected void operateFocuser(Focuser focuser, OperationReturn returns)
			throws RTSException {
		
		focuser.moveRelative(this.steps);

		returns.setMessage("Move focuser relative operation executed: "
				+ this.steps + ", " + this.getServer() + ","
				+ this.getFocuserName());
	}
}
