package eu.gloria.gs.services.teleoperation.ccd.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.rti.client.devices.CCD;

public class GetBrightnessOperation extends CCDOperation {

	public GetBrightnessOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateCCD(CCD ccd, OperationReturn returns)
			throws RTSException {

		long brightness = ccd.getBrightness();

		returns.setMessage("Get brightness operation executed: " + brightness
				+ ", " + this.getServer() + "," + this.getCCDName());

		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(brightness);
	}
}
