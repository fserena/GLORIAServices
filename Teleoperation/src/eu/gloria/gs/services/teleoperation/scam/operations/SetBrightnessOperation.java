package eu.gloria.gs.services.teleoperation.scam.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.gs.services.teleoperation.base.ServerResolver;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;
import eu.gloria.rti.client.RTSHandler;
import eu.gloria.rti.client.devices.Scam;

public class SetBrightnessOperation extends SurveillanceCameraOperation {

	private double brightness;

	public SetBrightnessOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 2)
			this.brightness = (Double) args.getArguments().get(2);
	}

	@Override
	public OperationReturn execute(ServerResolver resolver) throws Exception {

		RTSHandler rts;

		try {
			rts = (RTSHandler) resolver.getHandler(getServer());
		} catch (NullPointerException e) {
			throw new Exception("The RTS is not available.");
		}

		try {
			rts.setBrightness(this.getSCamName(), this.brightness);

			OperationReturn returns = new OperationReturn();

			returns.setMessage("Set brightness operation executed: "
					+ this.brightness + ", " + this.getServer() + ","
					+ this.getSCamName());

			return returns;
		} catch (RTSException e) {

			throw new RTSTeleoperationException(e.getMessage());
		}
	}

	@Override
	protected void operateSCam(Scam scam, OperationReturn returns)
			throws RTSException {
		scam.setBrightness((long)this.brightness);

		returns.setMessage("Set brightness operation executed: "
				+ this.brightness + ", " + this.getServer() + ","
				+ this.getSCamName());

	}
}
