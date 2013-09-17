package eu.gloria.gs.services.experiment.base.operations;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;
import eu.gloria.gs.services.experiment.base.operations.OperationType;
import eu.gloria.gs.services.experiment.base.operations.OperationTypeNotAvailableException;

public class ExperimentOperationFactory implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	/*public OperationType getParameterType(String name) {

		return (OperationType) applicationContext.getBean(name);
	}*/

	public ExperimentOperation createOperation(String operationName)
			throws OperationTypeNotAvailableException {
		ExperimentOperation operation = null;

		operation = (ExperimentOperation) applicationContext
				.getBean(operationName);

		if (operation == null) {
			throw new OperationTypeNotAvailableException("Operation: "
					+ operationName);
		}

		return operation;

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {

		this.applicationContext = applicationContext;

	}

	public Map<String, ExperimentOperation> getAllOperations() {
		return this.applicationContext
				.getBeansOfType(ExperimentOperation.class);
	}

	public ExperimentOperation getOperation(String name) {
		return (ExperimentOperation) this.applicationContext.getBean(name);
	}
}
