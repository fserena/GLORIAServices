package eu.gloria.gs.services.teleoperation.scam.operations;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.DeviceOperation;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.gs.services.teleoperation.base.ServerResolver;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;
import eu.gloria.rti.client.DeviceFactory;
import eu.gloria.rti.client.devices.Scam;

public abstract class SurveillanceCameraOperation extends DeviceOperation {

	private String scam;

	public SurveillanceCameraOperation(OperationArgs args) {
		super(args);

		if (args.getArguments().size() > 1)
			this.scam = (String) args.getArguments().get(1);
	}

	public String getSCamName() {
		return this.scam;
	}

	protected abstract void operateSCam(Scam scam,
			OperationReturn returns) throws RTSException;

	@Override
	protected DeviceHandler getDeviceHandler(ServerResolver resolver) throws Exception {

		String url = resolver.resolve(this.getServer());
		return DeviceFactory.getReference().createSurveillanceCamera(url,
				this.getSCamName());
	}

	@Override
	protected void operateHandler(DeviceHandler handler, OperationReturn returns)
			throws TeleoperationException {

		try {
			this.operateSCam((Scam) handler, returns);
		} catch (RTSException e) {
			throw new RTSTeleoperationException(e.getMessage());
		}

	}

}
