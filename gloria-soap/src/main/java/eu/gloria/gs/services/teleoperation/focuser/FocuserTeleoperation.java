package eu.gloria.gs.services.teleoperation.focuser;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.focuser.operations.GetPositionOperation;
import eu.gloria.gs.services.teleoperation.focuser.operations.MoveAbsoluteOperation;
import eu.gloria.gs.services.teleoperation.focuser.operations.MoveRelativeOperation;

public class FocuserTeleoperation extends AbstractTeleoperation implements
		FocuserTeleoperationInterface {

	public FocuserTeleoperation() {
		super(FocuserTeleoperation.class.getSimpleName());
	}

	@Override
	public long getPosition(String rt, String focuser)
			throws DeviceOperationFailedException,
			FocuserTeleoperationException {
		try {
			return (Long) this.invokeGetOperation(GetPositionOperation.class,
					rt, focuser);
		} catch (ActionException e) {
			throw new FocuserTeleoperationException(e.getAction());
		}
	}

	@Override
	public void moveAbsolute(String rt, String focuser, long position)
			throws DeviceOperationFailedException,
			FocuserTeleoperationException {
		try {
			this.invokeSetOperation(MoveAbsoluteOperation.class, rt, focuser);
		} catch (ActionException e) {
			throw new FocuserTeleoperationException(e.getAction());
		}
	}

	@Override
	public void moveRelative(String rt, String focuser, long steps)
			throws DeviceOperationFailedException,
			FocuserTeleoperationException {
		try {
			this.invokeSetOperation(MoveRelativeOperation.class, rt, focuser);
		} catch (ActionException e) {
			throw new FocuserTeleoperationException(e.getAction());
		}
	}
}
