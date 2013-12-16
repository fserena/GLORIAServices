package eu.gloria.gs.services.teleoperation.base;

import java.util.ArrayList;

import eu.gloria.gs.services.core.GSLogProducerService;
import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogAction;

public abstract class AbstractTeleoperation extends GSLogProducerService
		implements Teleoperation {

	private ServerResolver resolver;

	public AbstractTeleoperation() {

	}

	@Override
	public OperationReturn executeOperation(Operation operation)
			throws TeleoperationException {

		return operation.execute(this.getServerResolver());
	}

	public void setServerResolver(ServerResolver resolver) {
		this.resolver = resolver;
	}

	protected ServerResolver getServerResolver() {
		return this.resolver;
	}

	protected void processBadArgs(String rt, String device,
			String op, ArrayList<Object> args) {
		this.processError(null, rt, device, op, args, "bad arguments");
	}

	protected void processDeviceFailure(ActionException e, String rt,
			String device, String op, ArrayList<Object> args) {
		this.processError(e, rt, device, op, args, "device failure");
	}

	protected void processInternalError(ActionException e, String rt,
			String device, String op, ArrayList<Object> args) {
		this.processError(e, rt, device, op, args, "internal error");
	}

	protected void processError(ActionException e, String rt, String device,
			String op, ArrayList<Object> args, String cause) {

		LogAction action = new LogAction();

		action.put("operation", op);
		action.put("rt", rt);
		action.put("device", device);
		action.put("args", args);
		action.put("cause", cause);

		this.logRtError(this.getClientUsername(), rt, action);

		if (e != null)
			action.put("more", e.getAction());
	}

	protected void processSuccess(String rt, String device,
			String op, ArrayList<Object> args, Object result) {

		LogAction action = new LogAction();

		action.put("operation", op);
		action.put("rt", rt);
		action.put("device", device);
		action.put("args", args);

		if (result != null) {
			action.put("result", result);
		}

		this.logRtInfo(this.getClientUsername(), rt, action);
	}

	protected void processWarning(ActionException e, String rt, String device,
			String op, ArrayList<Object> args, String cause) {

		LogAction action = new LogAction();

		action.put("operation", op);
		action.put("rt", rt);
		action.put("device", device);
		action.put("args", args);
		action.put("cause", cause);

		this.logRtWarning(this.getClientUsername(), rt, action);

		action.put("more", e.getAction());
	}

}
