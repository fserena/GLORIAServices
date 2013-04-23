package eu.gloria.gs.services.teleoperation.focuser.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.rti.client.devices.Focuser;

public class MoveAbsoluteOperation extends FocuserOperation {

	private long position;

	public MoveAbsoluteOperation(OperationArgs args) throws Exception {
		super(args);

		this.position = (Long) args.getArguments().get(2);
	}

	@Override
	protected void operateFocuser(Focuser focuser, OperationReturn returns)
			throws RTSException {

		focuser.moveAbsolute(this.position);
		
		returns.setMessage("Move focuser absolute operation executed: "
				+ this.position + ", " + this.getServer() + ","
				+ this.getFocuserName());
	}
}
