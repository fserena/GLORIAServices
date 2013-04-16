package eu.gloria.gs.services.teleoperation.ccd.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.rti.client.RTSException;
import eu.gloria.rti.client.devices.CCD;

public class StartContinueModeOperation extends CCDOperation {

	public StartContinueModeOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateCCD(CCD ccd, OperationReturn returns)
			throws RTSException {

		String imageId = ccd.startContinueMode();

		returns.setMessage("Start continue mode operation executed: " + imageId
				+ ", " + this.getServer() + ", " + this.getCCDName());

		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(imageId);
	}
}
