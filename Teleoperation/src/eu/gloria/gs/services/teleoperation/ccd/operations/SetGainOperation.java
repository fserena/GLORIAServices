package eu.gloria.gs.services.teleoperation.ccd.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.rti.client.devices.CCD;

public class SetGainOperation extends CCDOperation {

	private double gain;

	public SetGainOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 2)
			this.gain = (Long) args.getArguments().get(2);
	}

	@Override
	protected void operateCCD(CCD ccd, OperationReturn returns)
			throws RTSException {
		ccd.setGain((long) this.gain);

		returns.setMessage("Set gain operation executed: " + this.gain + ", "
				+ this.getServer() + "," + this.getCCDName());

	}
}
