package eu.gloria.gs.services.experiment.base.operations;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;
import eu.gloria.gs.services.experiment.base.operations.OperationTypeNotAvailableException;

public class ExperimentOperationFactory implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	public ExperimentOperation createOperation(String operationName)
			throws OperationTypeNotAvailableException {
		ExperimentOperation operation = null;

		operation = (ExperimentOperation) applicationContext
				.getBean(operationName);

		if (operation == null) {
			throw new OperationTypeNotAvailableException(operationName);
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

	public ExperimentOperation getOperation(String name)
			throws NoSuchOperationException {
		try {
			return (ExperimentOperation) this.applicationContext.getBean(name);
		} catch (NoSuchBeanDefinitionException e) {
			throw new NoSuchOperationException(name);
		} catch (ClassCastException e) {
			throw new NoSuchOperationException(name);
		}
	}
}
