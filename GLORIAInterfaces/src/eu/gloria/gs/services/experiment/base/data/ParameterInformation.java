package eu.gloria.gs.services.experiment.base.data;

import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;

public class ParameterInformation {

	private String modelName;
	private String parameterName;
	protected ExperimentParameter parameter;
	private String[] arguments;

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String name) {
		this.modelName = name;
	}

	public ExperimentParameter getParameter() {
		return parameter;
	}

	public void setParameter(ExperimentParameter parameter) {
		this.parameter = parameter;
	}

	public String[] getArguments() {
		return arguments;
	}

	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

}
