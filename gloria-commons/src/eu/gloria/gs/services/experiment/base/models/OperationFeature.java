package eu.gloria.gs.services.experiment.base.models;

import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;

public class OperationFeature {

	private ExperimentOperation operation;
	private String[] relations;
	private String[] suffix;

	public ExperimentOperation getOperation() {
		return operation;
	}

	public void setOperation(ExperimentOperation operation) {
		this.operation = operation;
	}

	public String[] getRelations() {
		return relations;
	}

	public void setRelations(String[] relations) {
		this.relations = relations;
	}

	public void setNameSuffix(String[] suffix) {
		this.suffix = suffix;
	}

	public String[] getNameSuffix() {
		return this.suffix;
	}
}
