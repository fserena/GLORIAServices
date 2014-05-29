package eu.gloria.gs.services.experiment.base.data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;

@XmlRootElement
@XmlSeeAlso(Object[].class)
public class ParameterInformation {

	private String name;
	private String type;
	protected ExperimentParameter parameter;
	private Object[] arguments;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getType() {
		return type;
	}

	public void setType(String parameterName) {
		this.type = parameterName;
	}

}
