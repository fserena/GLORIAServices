package eu.gloria.gs.services.experiment.online.models;

import java.util.Map;

public class ExperimentFeature {

	private Map<String, ParameterFeature> parameters;
	private Map<String, OperationFeature> operations;

	public Map<String, ParameterFeature> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, ParameterFeature> parameters) {
		this.parameters = parameters;
	}

	public Map<String, OperationFeature> getOperations() {
		return operations;
	}

	public void setOperations(Map<String, OperationFeature> operations) {
		this.operations = operations;
	}
}
