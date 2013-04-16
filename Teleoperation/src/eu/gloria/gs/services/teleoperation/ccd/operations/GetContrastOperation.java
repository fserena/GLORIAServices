package eu.gloria.gs.services.teleoperation.ccd.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.rti.client.RTSException;
import eu.gloria.rti.client.devices.CCD;

public class GetContrastOperation extends CCDOperation {

	public GetContrastOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateCCD(CCD ccd, OperationReturn returns)
			throws RTSException {

		long contrast = ccd.getContrast();

		returns.setMessage("Get contrast operation executed: " + contrast
				+ ", " + this.getServer() + "," + this.getCCDName());

		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(contrast);
		
	}
}
