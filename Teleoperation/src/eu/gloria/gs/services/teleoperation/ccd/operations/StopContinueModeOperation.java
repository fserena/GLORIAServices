package eu.gloria.gs.services.teleoperation.ccd.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.rti.client.devices.CCD;

public class StopContinueModeOperation extends CCDOperation {

	public StopContinueModeOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateCCD(CCD ccd, OperationReturn returns)
			throws RTSException {

		ccd.stopContinueMode();

		returns.setMessage("Stop continue mode operation executed: "
				+ this.getServer() + ", " + this.getCCDName());

		returns.setReturns(new ArrayList<Object>());

	}
}
