package eu.gloria.gs.services.experiment.base.data;

import java.util.List;

public class ExperimentInformation {

	private ExperimentType type;
	private String name;
	private String description;
	private String author;
	private List<OperationInformation> operations;
	private List<ParameterInformation> parameters;

	public ExperimentType getType() {
		return type;
	}

	public void setType(ExperimentType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<OperationInformation> getOperations() {
		return operations;
	}

	public void setOperations(List<OperationInformation> operations) {
		this.operations = operations;
	}

	public List<ParameterInformation> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParameterInformation> parameters) {
		this.parameters = parameters;
	}

	public boolean containsOperation(String name) {
		for (OperationInformation opInfo : operations) {
			if (opInfo.getName().equals(name)) {
				return true;
			}
		}

		return false;
	}

	public OperationInformation getOperation(String name) {
		for (OperationInformation opInfo : operations) {
			if (opInfo.getName().equals(name)) {
				return opInfo;
			}
		}

		return null;
	}

	public boolean containsParameter(String name) {
		for (ParameterInformation paramInfo : parameters) {
			if (paramInfo.getName().equals(name)) {
				return true;
			}
		}

		return false;
	}

	public ParameterInformation getParameter(String name) {
		for (ParameterInformation paramInfo : parameters) {
			if (paramInfo.getName().equals(name)) {
				return paramInfo;
			}
		}

		return null;
	}
}
