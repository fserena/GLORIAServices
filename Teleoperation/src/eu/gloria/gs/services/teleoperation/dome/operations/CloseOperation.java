package eu.gloria.gs.services.teleoperation.dome.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.rti.client.devices.Dome;

public class CloseOperation extends DomeOperation {

	public CloseOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateDome(Dome dome, OperationReturn returns)
			throws RTSException {
		dome.close();

		returns.setMessage("Close dome operation executed: " + this.getServer()
				+ "," + this.getDomeName());

	}
}
