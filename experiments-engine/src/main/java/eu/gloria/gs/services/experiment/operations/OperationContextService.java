package eu.gloria.gs.services.experiment.operations;

import eu.gloria.gs.services.experiment.base.contexts.ExperimentContextService;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.operations.OperationContext;

public class OperationContextService extends ExperimentContextService {

	protected void makeUpService(ServiceOperation service) {
		service.setAdapter(this.getAdapter());
		service.setUsername(this.getUsername());
		service.setPassword(this.getPassword());
	}

	public void treatOperation(OperationContext operationContext,
			String operation, Object... operationArguments)
			throws ExperimentOperationException {

		ServiceOperation service = null;

		try {

			Class<?> serviceClass = Class.forName(operation);

			try {
				service = (ServiceOperation) serviceClass.newInstance();

				this.makeUpService(service);

				service.setContext(operationContext);
				service.setArguments(operationArguments);

				service.execute();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new ExperimentOperationException(
						operationContext.getName(), "operation class problem");
			}

		} catch (ClassNotFoundException e) {
			throw new ExperimentOperationException(operationContext.getName(),
					"operation class not found");
		}
	}
}
