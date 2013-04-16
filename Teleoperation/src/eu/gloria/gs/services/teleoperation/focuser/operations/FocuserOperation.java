package eu.gloria.gs.services.teleoperation.focuser.operations;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.DeviceOperation;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.ServerResolver;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;
import eu.gloria.rti.client.DeviceFactory;
import eu.gloria.rti.client.RTSException;
import eu.gloria.rti.client.devices.Focuser;

public abstract class FocuserOperation extends DeviceOperation {

	private String focuser;

	public FocuserOperation(OperationArgs args) {
		super(args);

		if (args.getArguments().size() > 1)
			this.focuser = (String) args.getArguments().get(1);
	}

	public String getFocuserName() {
		return this.focuser;
	}

	protected abstract void operateFocuser(Focuser ccd, OperationReturn returns)
			throws RTSException;

	@Override
	protected DeviceHandler getDeviceHandler(ServerResolver resolver)
			throws Exception {

		String url = resolver.resolve(this.getServer());
		return DeviceFactory.getReference().createFocuser(url,
				this.getFocuserName());
	}

	@Override
	protected void operateHandler(DeviceHandler handler, OperationReturn returns)
			throws TeleoperationException {

		try {
			this.operateFocuser((Focuser) handler, returns);
		} catch (RTSException e) {
			throw new RTSTeleoperationException(e.getMessage());
		}

	}

}
