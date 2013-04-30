package eu.gloria.gs.services.teleoperation.ccd.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.CCD;

public class SetExposureTimeOperation extends CCDOperation {

	private double exposure;

	public SetExposureTimeOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 2)
			this.exposure = (Double) args.getArguments().get(2);
	}

	@Override
	protected void operateCCD(CCD ccd, OperationReturn returns)
			throws TeleoperationException {

		ccd.setExposureTime(this.exposure);

		returns.setMessage("Set exposure operation executed: " + this.exposure
				+ ", " + this.getServer() + "," + this.getCCDName());
	}
}
