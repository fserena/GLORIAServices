package eu.gloria.gs.services.experiment.base.data;

import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;

public class OperationInformation {

	private String modelName;
	private String operationName;
	private ExperimentOperation operation;
	private String[] arguments;

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String name) {
		this.modelName = name;
	}

	public ExperimentOperation getOperation() {
		return operation;
	}

	public void setOperation(ExperimentOperation operation) {
		this.operation = operation;
	}

	public String[] getArguments() {
		return arguments;
	}

	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
}
