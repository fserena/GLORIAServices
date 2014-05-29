package eu.gloria.gs.services.teleoperation.scam.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.Scam;

public class GetImageURLOperation extends SurveillanceCameraOperation {

	public GetImageURLOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateSCam(Scam scam, OperationReturn returns)
			throws TeleoperationException {
		String url = null;
		try {
			url = scam.getImageURL();
		} catch (DeviceOperationFailedException e) {
		}

		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(url);

	}
}
