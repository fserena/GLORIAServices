package eu.gloria.gs.services.experiment.online.parameters;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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
			throw new ParameterTypeNotAvailableException(
					"The experiment parameter '" + parameterName
							+ "' does not exist");
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
