package eu.gloria.gs.services.experiment.base.data;

import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;

public class OperationInformation {

	private String name;
	private String type;
	private ExperimentOperation operation;
	private String[] arguments;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
