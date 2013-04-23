package eu.gloria.gs.services.teleoperation.ccd.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.rti.client.devices.CCD;

public class SetContrastOperation extends CCDOperation {

	private double contrast;

	public SetContrastOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 2)
			this.contrast = (Long) args.getArguments().get(2);
	}

	@Override
	protected void operateCCD(CCD ccd, OperationReturn returns)
			throws RTSException {
		ccd.setContrast((long)this.contrast);

		returns.setMessage("Set contrast operation executed: " + this.contrast
				+ ", " + this.getServer() + "," + this.getCCDName());

	}
}
