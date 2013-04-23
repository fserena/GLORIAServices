package eu.gloria.gs.services.teleoperation.scam.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.rti.client.devices.Scam;

public class SetExposureTimeOperation extends SurveillanceCameraOperation {

	private double exposure;

	public SetExposureTimeOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 2)
			this.exposure = (Double) args.getArguments().get(2);
	}

	@Override
	protected void operateSCam(Scam scam, OperationReturn returns)
			throws RTSException {

		scam.setExposureTime(this.exposure);

		returns.setMessage("Set exposure operation executed: " + this.exposure
				+ ", " + this.getServer() + "," + this.getSCamName());
	}
}
