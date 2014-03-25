package eu.gloria.gs.services.teleoperation.base;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import eu.gloria.gs.services.core.GSLogProducerService;
import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationException;

public abstract class AbstractTeleoperation extends GSLogProducerService
		implements Teleoperation {

	private ServerResolver resolver;

	protected AbstractTeleoperation(String name) {
		super(name);
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

	protected void processDeviceFailure(String rt, ActionException e) {
		e.getAction().put("reason", "device failure");
		this.processError(rt, e);
	}

	protected void processInternalError(String rt, ActionException e) {
		e.getAction().put("reason", "internal error");
		this.processError(rt, e);
	}

	protected void processError(String rt, ActionException e) {
		this.logError(rt, this.getClientUsername(), e.getAction());
	}

	private void processSuccess(String rt, String operation,
			ArrayList<Object> args, ArrayList<Object> result) {

		Action action = new Action();
		action.put("rt", rt);
		action.put("name", operation);
		action.put("args", args);

		if (result != null) {
			action.put("result", result);
		}

		this.logInfo(rt, this.getClientUsername(), action);
	}

	protected void processWarning(String rt, ActionException e) {
		this.logWarning(rt, this.getClientUsername(), e.getAction());
	}

	protected void invokeSetOperation(Class<?> cl, String rt, String device,
			Object... args) throws ActionException {
		try {
			this.invokeOperation(cl, rt, device, args);
		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(rt, e);
			throw e;
		} catch (ActionException e) {
			this.processInternalError(rt, e);
			throw e;
		}
	}

	protected Object invokeGetOperation(Class<?> cl, String rt, String device,
			Object... args) throws ActionException {
		try {
			Object value = this.invokeOperation(cl, rt, device, args).get(0);
			return value;
		} catch (DeviceOperationFailedException e) {
			this.processDeviceFailure(rt, e);
			throw e;
		} catch (ActionException e) {
			this.processInternalError(rt, e);
			throw e;
		}
	}

	protected ArrayList<Object> invokeOperation(Class<?> cl, String rt,
			Object... args) throws ActionException {

		OperationArgs opArgs = new OperationArgs();
		opArgs.getArguments().add(rt);

		if (args != null) {
			for (Object arg : args) {
				opArgs.getArguments().add(arg);
			}
		}

		DeviceOperation operation = null;
		String opName = cl.getSimpleName();
		if (cl.isAnnotationPresent(DeviceOp.class)) {
			opName = cl.getAnnotation(DeviceOp.class).name();
		}

		try {
			Constructor<?> constructor = cl.getConstructor(OperationArgs.class);
			operation = (DeviceOperation) constructor.newInstance(opArgs);
		} catch (Exception e) {
			throw new ActionException("bad request");
		}

		try {
			OperationReturn returns = this.executeOperation(operation);
			this.processSuccess(rt, opName, opArgs.getArguments(),
					returns.getReturns());

			return returns.getReturns();
		} catch (TeleoperationException e) {
			e.getAction().put("rt", rt);
			e.getAction().put("name", opName);
			e.getAction().put("args", opArgs.getArguments());
			throw e;
		}
	}

}
