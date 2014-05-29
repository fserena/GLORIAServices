package eu.gloria.gs.services.teleoperation.focuser.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.Range;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.Focuser;

public class GetRangeOperation extends FocuserOperation {

	public GetRangeOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateFocuser(Focuser focuser, OperationReturn returns)
			throws TeleoperationException {

		Range range = focuser.getRange();

		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(range);
	}
}
