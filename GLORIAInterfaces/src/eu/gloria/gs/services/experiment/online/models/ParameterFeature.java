package eu.gloria.gs.services.experiment.online.models;

import eu.gloria.gs.services.experiment.online.parameters.ExperimentParameter;

public class ParameterFeature {

	private String parameterName;
	private ExperimentParameter parameter;
	private String[] parameterArguments;
	private String[] suffix;

	public ExperimentParameter getParameter() {
		return parameter;
	}

	public void setParameter(ExperimentParameter parameter) {
		this.parameter = parameter;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String[] getParameterArguments() {
		return parameterArguments;
	}

	public void setParameterArguments(String[] parameterArguments) {
		this.parameterArguments = parameterArguments;
	}
	
	public void setNameSuffix(String[] suffix) {
		this.suffix = suffix;
	}
	
	public String[] getNameSuffix() {
		return this.suffix;
	}
}
