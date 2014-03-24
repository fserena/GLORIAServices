package eu.gloria.gs.services.teleoperation.ccd.operations;

import eu.gloria.gs.services.teleoperation.base.DeviceOp;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.CCD;

@DeviceOp(name = "set ccd exposure")
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

		if (this.exposure >= 0) {
			ccd.setExposureTime(this.exposure);
		}
	}
}
