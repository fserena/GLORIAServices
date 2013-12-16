package eu.gloria.gs.services.experiment.base.parameters;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.ParameterType;
import eu.gloria.gs.services.experiment.base.parameters.ParameterTypeNotAvailableException;

public class ExperimentParameterFactory implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	private ExperimentParameterFactory() {
	}

	public ParameterType getParameterType(String name) {

		return (ParameterType) applicationContext.getBean(name);
	}

	public ExperimentParameter createParameter(String parameterName)
			throws ParameterTypeNotAvailableException,
			ExperimentParameterException {

		ExperimentParameter parameter = null;

		parameter = (ExperimentParameter) applicationContext
				.getBean(parameterName);

		if (parameter == null) {
			throw new ParameterTypeNotAvailableException(parameterName);
		}

		return parameter;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {

		this.applicationContext = applicationContext;

	}

	public Map<String, ExperimentParameter> getAllParameters() {
		return this.applicationContext
				.getBeansOfType(ExperimentParameter.class);
	}

	public ExperimentParameter getParameter(String name) {
		return (ExperimentParameter) this.applicationContext
				.getBean(name);
	}
}
