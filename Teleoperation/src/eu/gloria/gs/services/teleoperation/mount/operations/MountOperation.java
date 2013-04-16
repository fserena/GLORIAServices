package eu.gloria.gs.services.teleoperation.mount.operations;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.DeviceOperation;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.ServerResolver;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;
import eu.gloria.rti.client.DeviceFactory;
import eu.gloria.rti.client.RTSException;
import eu.gloria.rti.client.devices.Mount;

public abstract class MountOperation extends DeviceOperation {

	private String mount;

	public MountOperation(OperationArgs args) {
		super(args);

		if (args.getArguments().size() > 1)
			this.mount = (String) args.getArguments().get(1);
	}

	public String getMountName() {
		return this.mount;
	}

	protected abstract void operateMount(Mount mount, OperationReturn returns)
			throws RTSException;

	@Override
	protected DeviceHandler getDeviceHandler(ServerResolver resolver)
			throws Exception {

		String url = resolver.resolve(this.getServer());
		return DeviceFactory.getReference().createMount(url,
				this.getMountName());
	}

	@Override
	protected void operateHandler(DeviceHandler handler, OperationReturn returns)
			throws TeleoperationException {

		try {
			this.operateMount((Mount) handler, returns);
		} catch (RTSException e) {
			throw new RTSTeleoperationException(e.getMessage());
		}

	}

}
