package eu.gloria.gs.services.experiment.online.parameters;

import java.util.Map;

import eu.gloria.gs.services.experiment.online.operations.ExperimentOperation;
import eu.gloria.gs.services.experiment.online.operations.OperationType;

public class ExperimentParameter {

	private String name;
	private ParameterType type;
	private Map<Integer, ExperimentParameter> parameterDependencies;
	private Map<Integer, ExperimentOperation> operationDependencies;
	private Map<String, String[]> initOperations;
	private Map<String, Object> properties;
	private String concreteName;

	public ParameterType getType() {
		return this.type;
	}

	public void setType(ParameterType type) {
		this.type = type;
	}

	public ParameterType getParameterArgumentType(int order) {
		if (this.argumentIsParameter(order)) {
			return parameterDependencies.get(order).getType();
		}

		return null;
	}

	public OperationType getOperationArgumentType(int order) {
		if (this.argumentIsParameter(order)) {
			return operationDependencies.get(order).getType();
		}

		return null;
	}

	public Class<?> getValueArgumentType(int order) {
		return this.type.getArgumentTypes().get(order);
	}

	public int getArgumentsNumber() {
		return type.getArgumentTypes().size();
	}

	public boolean argumentIsParameter(int order) {
		if (parameterDependencies != null) {
			return parameterDependencies.containsKey(order);
		}

		return false;
	}

	public boolean argumentIsOperation(int order) {
		if (operationDependencies != null) {
			return operationDependencies.containsKey(order);
		}

		return false;
	}

	protected void parseArgument(int order, String arg)
			throws IllegalArgumentException {

	}

	public Map<Integer, ExperimentParameter> getParameterDependencies() {
		return parameterDependencies;
	}

	public void setParameterDependencies(
			Map<Integer, ExperimentParameter> parameterDependencies) {
		this.parameterDependencies = parameterDependencies;
	}

	public Map<Integer, ExperimentOperation> getOperationDependencies() {
		return operationDependencies;
	}

	public void setOperationDependencies(
			Map<Integer, ExperimentOperation> operationDependencies) {
		this.operationDependencies = operationDependencies;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
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

	public Map<String, String[]> getInitOperations() {
		return initOperations;
	}

	public void setInitOperations(Map<String, String[]> initOperations) {
		this.initOperations = initOperations;
	}

}
