package eu.gloria.gs.services.teleoperation.generic;

import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.rti.client.RTSHandler;

public class GenericTeleoperation extends AbstractTeleoperation implements
		GenericTeleoperationInterface {

	@Override
	public void startTeleoperation(String rt)
			throws GenericTeleoperationException {

		try {

			RTSHandler rtHandler = (RTSHandler) this.getServerResolver()
					.getHandler(rt);

			rtHandler.startTeleoperation();
		} catch (GenericTeleoperationException e) {
			this.processException(e.getMessage(), rt);
		}

		this.processSuccess(rt, "generic", "StartTeleoperation", null, "ok");
	}

	@Override
	public void notifyTeleoperation(String rt, long seconds)
			throws GenericTeleoperationException {

		try {
			RTSHandler rtHandler = (RTSHandler) this.getServerResolver()
					.getHandler(rt);

			rtHandler.notifyTeleoperation(seconds);
		} catch (GenericTeleoperationException e) {
			this.processException(e.getMessage(), rt);
		}

		this.processSuccess(rt, "generic", "StopTeleoperation", null, "ok");
	}

	@Override
	public void stopTeleoperation(String rt)
			throws GenericTeleoperationException {
		
		try {
			RTSHandler rtHandler = (RTSHandler) this.getServerResolver()
					.getHandler(rt);

			rtHandler.stopTeleoperation();
		} catch (GenericTeleoperationException e) {
			this.processException(e.getMessage(), rt);
		}

		this.processSuccess(rt, "generic", "StopTeleoperation", null, "ok");
	}

}
