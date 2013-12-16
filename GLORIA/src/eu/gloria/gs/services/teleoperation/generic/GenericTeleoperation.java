package eu.gloria.gs.services.teleoperation.generic;

import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.ServerNotAvailableException;
import eu.gloria.rti.client.RTSHandler;

public class GenericTeleoperation extends AbstractTeleoperation implements
		GenericTeleoperationInterface {

	@Override
	public void startTeleoperation(String rt)
			throws GenericTeleoperationException {

		String operationName = "start teleoperation";

		LogAction action = new LogAction();
		action.put("sender", this.getUsername());
		action.put("operation", operationName);
		action.put("rt", rt);

		try {

			RTSHandler rtHandler = (RTSHandler) this.getServerResolver()
					.getHandler(rt);

			rtHandler.startTeleoperation();

			this.logRtInfo(this.getClientUsername(), rt, action);

		} catch (GenericTeleoperationException e) {
			action.put("cause", "internal error");
			this.logRtError(this.getClientUsername(), rt, action);
			action.put("more", e.getAction());
			throw new GenericTeleoperationException(action);
		} catch (ServerNotAvailableException e) {
			action.put("cause", "server not available");
			this.logRtError(this.getClientUsername(), rt, action);
			action.put("more", e.getAction());
			throw new GenericTeleoperationException(action);
		}

	}

	@Override
	public void notifyTeleoperation(String rt, long seconds)
			throws GenericTeleoperationException {

		String operationName = "notify teleoperation";

		LogAction action = new LogAction();
		action.put("sender", this.getUsername());
		action.put("operation", operationName);
		action.put("rt", rt);

		try {
			RTSHandler rtHandler = (RTSHandler) this.getServerResolver()
					.getHandler(rt);

			rtHandler.notifyTeleoperation(seconds);

			this.logRtInfo(this.getClientUsername(), rt, action);

		} catch (GenericTeleoperationException e) {
			action.put("cause", "internal error");
			this.logRtError(this.getClientUsername(), rt, action);
			action.put("more", e.getAction());
			throw new GenericTeleoperationException(action);
		} catch (ServerNotAvailableException e) {
			action.put("cause", "server not available");
			this.logRtError(this.getClientUsername(), rt, action);
			action.put("more", e.getAction());
			throw new GenericTeleoperationException(action);
		}

		this.processSuccess(rt, "generic", "StopTeleoperation", null, "ok");
	}

	@Override
	public void stopTeleoperation(String rt)
			throws GenericTeleoperationException {

		String operationName = "stop teleoperation";

		LogAction action = new LogAction();
		action.put("sender", this.getUsername());
		action.put("operation", operationName);
		action.put("rt", rt);

		try {
			RTSHandler rtHandler = (RTSHandler) this.getServerResolver()
					.getHandler(rt);

			rtHandler.stopTeleoperation();

			this.logRtInfo(this.getClientUsername(), rt, action);
		} catch (GenericTeleoperationException e) {
			action.put("cause", "internal error");
			this.logRtError(this.getClientUsername(), rt, action);
			action.put("more", e.getAction());
			throw new GenericTeleoperationException(action);
		} catch (ServerNotAvailableException e) {
			action.put("cause", "server not available");
			this.logRtError(this.getClientUsername(), rt, action);
			action.put("more", e.getAction());
			throw new GenericTeleoperationException(action);
		}
	}

}
