package eu.gloria.gs.services.teleoperation.base;

import eu.gloria.gs.services.core.GSLogProducerService;

public abstract class AbstractTeleoperation extends GSLogProducerService implements
		Teleoperation {

	private ServerResolver resolver;
	
	public AbstractTeleoperation() {

	}

	@Override
	public OperationReturn executeOperation(Operation operation)
			throws Exception {

		return operation.execute(this.getServerResolver());
	}

	public void setServerResolver(ServerResolver resolver) {
		this.resolver = resolver;
	}
	
	protected ServerResolver getServerResolver() {
		return this.resolver;
	}

}
