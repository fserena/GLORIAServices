package eu.gloria.gs.services.teleoperation.scam.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.Scam;

public class SetContrastOperation extends SurveillanceCameraOperation {

	private double contrast;

	public SetContrastOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 2)
			this.contrast = (Double) args.getArguments().get(2);
	}

	@Override
	protected void operateSCam(Scam scam, OperationReturn returns)
			throws TeleoperationException {
		scam.setContrast((long)this.contrast);
	}
}
