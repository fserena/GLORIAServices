package eu.gloria.gs.services.teleoperation.scam.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.rti.client.RTSException;
import eu.gloria.rti.client.devices.Scam;

public class GetImageURLOperation extends SurveillanceCameraOperation {

	public GetImageURLOperation(OperationArgs args) throws Exception {
		super(args);
	}

	@Override
	protected void operateSCam(Scam scam, OperationReturn returns)
			throws RTSException {
		String url = scam.getImageURL();
		
		returns.setMessage("Get image URL operation executed: " + url + ", "
				+ this.getServer() + ", " + this.getSCamName());

		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(url);

	}
}
