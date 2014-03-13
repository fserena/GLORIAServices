package eu.gloria.gs.services.teleoperation.dome.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.Dome;

public class ParkOperation extends DomeOperation {

	public ParkOperation(OperationArgs args) throws Exception {
		super(args);

	}

	@Override
	protected void operateDome(Dome dome, OperationReturn returns)
			throws TeleoperationException {
		dome.park();

		returns.setMessage("Park dome operation executed: " + this.getServer()
				+ "," + this.getDomeName());

	}
}
