package eu.gloria.gs.services.experiment.base.parameters;

import java.util.Map;

import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;

public class ExperimentParameter {

	private String name;
	private ParameterType type;
	private Map<Integer, ExperimentParameter> parameterDependencies;
	private Map<Integer, ExperimentOperation> operationDependencies;
	private Map<String, String[]> initOperations;
	private Map<String, Object> properties;
	private String concreteName;
	private String contextBeanName;
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContextBeanName() {
		return contextBeanName;
	}

	public void setContextBeanName(String contextBeanName) {
		this.contextBeanName = contextBeanName;
	}

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
