package eu.gloria.gs.services.teleoperation.ccd.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.CCD;

public class SetBrightnessOperation extends CCDOperation {

	private double brightness;

	public SetBrightnessOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 2)
			this.brightness = (Long) args.getArguments().get(2);
	}

	@Override
	protected void operateCCD(CCD ccd, OperationReturn returns)
			throws TeleoperationException {
		ccd.setBrightness((long)this.brightness);

		returns.setMessage("Set brightness operation executed: "
				+ this.brightness + ", " + this.getServer() + ","
				+ this.getCCDName());

	}
}
