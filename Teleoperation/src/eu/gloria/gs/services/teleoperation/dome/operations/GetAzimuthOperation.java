package eu.gloria.gs.services.teleoperation.dome.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.rti.client.devices.Dome;

public class GetAzimuthOperation extends DomeOperation {

	public GetAzimuthOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateDome(Dome dome, OperationReturn returns)
			throws RTSException {
		double azimuth = dome.getAzimuth();

		returns.setMessage("Get dome azimuth operation executed: " + azimuth
				+ ", " + this.getServer() + "," + this.getDomeName());

		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(azimuth);

	}
}
