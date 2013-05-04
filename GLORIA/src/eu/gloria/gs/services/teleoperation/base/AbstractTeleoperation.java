package eu.gloria.gs.services.teleoperation.base;

import eu.gloria.gs.services.core.GSLogProducerService;
import eu.gloria.gs.services.log.action.ActionLogException;

public abstract class AbstractTeleoperation extends GSLogProducerService implements
		Teleoperation {

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
	
	protected void processException(String message, String rt) {
		try {
			this.logAction(this.getClientUsername(), "'" + rt + "' error: "
					+ message);
		} catch (ActionLogException e) {
			e.printStackTrace();
		}
	}

	protected void processSuccess(String rt, String device, String op,
			Object[] args, Object result) {
		try {

			String message = rt + "/" + device + "/" + op;

			boolean first = true;

			if (args != null) {

				message += "?";

				for (Object arg : args) {

					if (!first) {
						message += "&";
					}

					message += String.valueOf(arg);

					first = false;
				}
			}

			if (result != null) {
				message += "->" + String.valueOf(result);
			}

			this.logAction(this.getClientUsername(), message);

		} catch (ActionLogException e) {
			e.printStackTrace();
		}
	}

}
