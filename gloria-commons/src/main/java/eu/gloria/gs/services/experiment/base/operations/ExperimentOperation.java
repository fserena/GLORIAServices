package eu.gloria.gs.services.experiment.base.operations;

import java.util.Map;

import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;

public class ExperimentOperation {

	private String name;
	private Map<String, String[]> behaviour;

	private ExperimentParameter[] parameterTypes;
	private String[] parameterNames;
	private String concreteName;
	private String description;

	private String contextBeanName;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getParameterNames() {
		return parameterNames;
	}

	public void setParameterNames(String[] parameterNames) {
		this.parameterNames = parameterNames;
	}

	public String getContextBeanName() {
		return contextBeanName;
	}

	public void setContextBeanName(String contextBeanName) {
		this.contextBeanName = contextBeanName;
	}

	public ExperimentParameter[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(ExperimentParameter[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public int getParametersNumber() {
		if (this.parameterTypes == null)
			return 0;

		return this.parameterTypes.length;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConcreteName() {
		return concreteName;
	}

	public void setConcreteName(String name) {
		this.concreteName = name;
	}

	public Map<String, String[]> getBehaviour() {
		return behaviour;
	}

	public void setBehaviour(Map<String, String[]> behaviour) {
		this.behaviour = behaviour;
	}
}
