package eu.gloria.gs.services.teleoperation.fw.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.Focuser;

public class SelectFilterOperation extends FilterWheelOperation {

	private long position;

	public SelectFilterOperation(OperationArgs args) throws Exception {
		super(args);

		this.position = (Long) args.getArguments().get(2);
	}

	@Override
	protected void operateFilterWheel(Focuser focuser, OperationReturn returns)
			throws TeleoperationException {

		focuser.moveAbsolute(this.position);
		
		returns.setMessage("Move focuser absolute operation executed: "
				+ this.position + ", " + this.getServer() + ","
				+ this.getFilterWheelName());
	}
}
