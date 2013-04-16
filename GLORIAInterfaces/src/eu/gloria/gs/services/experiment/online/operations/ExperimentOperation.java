package eu.gloria.gs.services.experiment.online.operations;

import java.util.Map;

import eu.gloria.gs.services.experiment.online.parameters.ExperimentParameter;

public class ExperimentOperation {

	private OperationType type;
	private String name;
	private Map<String, String[]> behaviour;

	private ExperimentParameter[] parameterTypes;
	private String concreteName;

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

	public OperationType getType() {
		return this.type;
	}

	public void setType(OperationType type) {
		this.type = type;
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
