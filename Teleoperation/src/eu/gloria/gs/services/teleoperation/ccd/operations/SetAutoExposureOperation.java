package eu.gloria.gs.services.teleoperation.ccd.operations;

import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.base.ServerResolver;
import eu.gloria.gs.services.teleoperation.rts.RTSTeleoperationException;
import eu.gloria.rti.client.RTSHandler;
import eu.gloria.rti.client.devices.CCD;

public class SetAutoExposureOperation extends CCDOperation {

	private boolean auto;

	public SetAutoExposureOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 2)
			this.auto = (Boolean) args.getArguments().get(2);
	}

	@Override
	public OperationReturn execute(ServerResolver resolver) throws Exception {

		RTSHandler rts;

		try {
			rts = (RTSHandler) resolver.getHandler(getServer());
		} catch (NullPointerException e) {
			throw new Exception("The RTS is not available.");
		}

		try {
			rts.setAutoExposure(this.getCCDName(), this.auto);

			OperationReturn returns = new OperationReturn();

			returns.setMessage("Set auto exposure operation executed: "
					+ this.auto + ", " + this.getServer() + ","
					+ this.getCCDName());

			return returns;
		} catch (TeleoperationException e) {

			throw new RTSTeleoperationException(e.getMessage());
		}
	}

	@Override
	protected void operateCCD(CCD ccd, OperationReturn returns)
			throws TeleoperationException {
		ccd.setAutoExposure(this.auto);

		returns.setMessage("Set auto exposure operation executed: " + this.auto
				+ ", " + this.getServer() + "," + this.getCCDName());

	}
}
