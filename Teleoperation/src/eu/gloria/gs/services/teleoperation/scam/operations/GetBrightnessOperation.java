package eu.gloria.gs.services.teleoperation.scam.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.rti.client.devices.Scam;

public class GetBrightnessOperation extends SurveillanceCameraOperation {

	public GetBrightnessOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateSCam(Scam scam, OperationReturn returns)
			throws RTSException {

		long brightness = scam.getBrightness();

		returns.setMessage("Get brightness operation executed: " + brightness
				+ ", " + this.getServer() + "," + this.getSCamName());

		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(brightness);
	}
}
