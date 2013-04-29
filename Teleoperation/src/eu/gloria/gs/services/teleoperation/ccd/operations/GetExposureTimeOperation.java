package eu.gloria.gs.services.teleoperation.ccd.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.CCD;

public class GetExposureTimeOperation extends CCDOperation {

	public GetExposureTimeOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateCCD(CCD ccd, OperationReturn returns)
			throws TeleoperationException {
		
		double exposure = ccd.getExposureTime();
		returns.setMessage("Get exposure operation executed: " + exposure
				+ ", " + this.getServer() + "," + this.getCCDName());

		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(exposure);

	}
}
