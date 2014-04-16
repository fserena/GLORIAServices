package eu.gloria.gs.services.teleoperation.generic;

import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.RTSHandler;

public class GenericTeleoperation extends AbstractTeleoperation implements
		GenericTeleoperationInterface {

	public GenericTeleoperation() {
		super(GenericTeleoperation.class.getSimpleName());
	}

	@Override
	public void startTeleoperation(String rt)
			throws GenericTeleoperationException {
		String operationName = "start teleoperation";

		Action action = new Action();
		action.put("name", operationName);
		action.put("rt", rt);

		try {
			RTSHandler rtHandler = (RTSHandler) this.getServerResolver()
					.getHandler(rt);

			rtHandler.startTeleoperation();

			this.logInfo(rt, this.getClientUsername(), action);
		} catch (TeleoperationException e) {
			this.logException(action, e);
			throw new GenericTeleoperationException(action);
		}
	}

	@Override
	public void notifyTeleoperation(String rt, long seconds)
			throws GenericTeleoperationException {

		String operationName = "notify teleoperation";

		Action action = new Action();
		action.put("name", operationName);
		action.put("rt", rt);
		action.put("seconds", seconds);

		try {
			RTSHandler rtHandler = (RTSHandler) this.getServerResolver()
					.getHandler(rt);

			rtHandler.notifyTeleoperation(seconds);

			this.logInfo(rt, this.getClientUsername(), action);
		} catch (TeleoperationException e) {
			this.logException(action, e);
			throw new GenericTeleoperationException(action);
		}
	}

	@Override
	public void stopTeleoperation(String rt)
			throws GenericTeleoperationException {

		String operationName = "stop teleoperation";

		Action action = new Action();
		action.put("name", operationName);
		action.put("rt", rt);

		try {
			RTSHandler rtHandler = (RTSHandler) this.getServerResolver()
					.getHandler(rt);

			rtHandler.stopTeleoperation();

			this.logInfo(rt, this.getClientUsername(), action);
		} catch (TeleoperationException e) {
			this.logException(action, e);
			throw new GenericTeleoperationException(action);
		}
	}

}
