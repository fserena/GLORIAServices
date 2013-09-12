package eu.gloria.gs.services.experiment.base.data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;

@XmlRootElement
@XmlSeeAlso(Object[].class)
public class ParameterInformation {

	private String modelName;
	private String parameterName;
	protected ExperimentParameter parameter;
	private Object[] arguments;

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

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

}
