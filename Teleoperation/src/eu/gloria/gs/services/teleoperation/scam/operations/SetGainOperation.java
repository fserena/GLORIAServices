package eu.gloria.gs.services.teleoperation.scam.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.rti.client.devices.Scam;

public class SetGainOperation extends SurveillanceCameraOperation {

	private double gain;

	public SetGainOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 2)
			this.gain = (Double) args.getArguments().get(2);
	}

	@Override
	protected void operateSCam(Scam scam, OperationReturn returns)
			throws RTSException {
		scam.setGain((long) this.gain);

		returns.setMessage("Set gain operation executed: " + this.gain + ", "
				+ this.getServer() + "," + this.getSCamName());

	}
}
