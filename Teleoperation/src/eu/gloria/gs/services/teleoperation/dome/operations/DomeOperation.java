package eu.gloria.gs.services.teleoperation.dome.operations;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.DeviceOperation;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.gs.services.teleoperation.base.ServerResolver;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;
import eu.gloria.rti.client.DeviceFactory;
import eu.gloria.rti.client.devices.Dome;

public abstract class DomeOperation extends DeviceOperation {

	private String dome;

	public DomeOperation(OperationArgs args) {
		super(args);

		if (args.getArguments().size() > 1)
			this.dome = (String) args.getArguments().get(1);
	}

	public String getDomeName() {
		return this.dome;
	}

	protected abstract void operateDome(Dome dome, OperationReturn returns)
			throws RTSException;

	@Override
	protected DeviceHandler getDeviceHandler(ServerResolver resolver)
			throws Exception {

		String url = resolver.resolve(this.getServer());
		return DeviceFactory.getReference().createDome(url, this.getDomeName());
	}

	@Override
	protected void operateHandler(DeviceHandler handler, OperationReturn returns)
			throws TeleoperationException {

		try {
			this.operateDome((Dome) handler, returns);
		} catch (RTSException e) {
			throw new RTSTeleoperationException(e.getMessage());
		}

	}

}
