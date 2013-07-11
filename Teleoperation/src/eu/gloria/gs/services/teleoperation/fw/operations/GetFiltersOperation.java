package eu.gloria.gs.services.teleoperation.fw.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.Focuser;

public class GetFiltersOperation extends FilterWheelOperation {

	public GetFiltersOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateFilterWheel(Focuser focuser, OperationReturn returns)
			throws TeleoperationException {

		long position = focuser.getPosition();

		returns.setMessage("Get focuser position operation executed: "
				+ position + ", " + this.getServer() + ","
				+ this.getFilterWheelName());
	}
}
