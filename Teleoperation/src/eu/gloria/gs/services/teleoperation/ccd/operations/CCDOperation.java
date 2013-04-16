package eu.gloria.gs.services.teleoperation.ccd.operations;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.DeviceOperation;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.ServerResolver;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;
import eu.gloria.rti.client.DeviceFactory;
import eu.gloria.rti.client.RTSException;
import eu.gloria.rti.client.devices.CCD;

public abstract class CCDOperation extends DeviceOperation {

	private String ccd;

	public CCDOperation(OperationArgs args) {
		super(args);

		if (args.getArguments().size() > 1)
			this.ccd = (String) args.getArguments().get(1);
	}

	public String getCCDName() {
		return this.ccd;
	}

	protected abstract void operateCCD(CCD ccd, OperationReturn returns)
			throws RTSException;

	@Override
	protected DeviceHandler getDeviceHandler(ServerResolver resolver) throws Exception {

		String url = resolver.resolve(this.getServer());
		return DeviceFactory.getReference().createCCD(url, this.getCCDName());
	}

	@Override
	protected void operateHandler(DeviceHandler handler, OperationReturn returns)
			throws TeleoperationException {

		try {
			this.operateCCD((CCD) handler, returns);
		} catch (RTSException e) {
			throw new RTSTeleoperationException(e.getMessage());
		}

	}

}
