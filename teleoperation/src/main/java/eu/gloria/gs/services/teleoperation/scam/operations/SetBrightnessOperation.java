package eu.gloria.gs.services.teleoperation.scam.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.Scam;

public class SetBrightnessOperation extends SurveillanceCameraOperation {

	private double brightness;

	public SetBrightnessOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 2)
			this.brightness = (Double) args.getArguments().get(2);
	}

	@Override
	protected void operateSCam(Scam scam, OperationReturn returns)
			throws TeleoperationException {
		scam.setBrightness((long)this.brightness);

	}
}
