package eu.gloria.gs.services.experiment.base.models;

import java.util.HashMap;
import java.util.Set;

import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationArgumentException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.operations.NoSuchOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterArgumentException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.UndefinedExperimentParameterException;

public abstract class ExperimentModel {

	private String name;
	private HashMap<String, ExperimentOperation> operations;
	private HashMap<String, ExperimentParameter> parameters;
	private HashMap<String, String[]> arguments;

	public ExperimentModel() {
		this.operations = new HashMap<String, ExperimentOperation>();
		this.parameters = new HashMap<String, ExperimentParameter>();
		this.arguments = new HashMap<String, String[]>();
	}

	public void setName(String experiment) {
		this.name = experiment;
	}

	public String getName() {
		return this.name;
	}

	public void addOperation(String name, ExperimentOperation operation,
			String[] arguments) throws ExperimentOperationException {

		if (operations.containsKey(name)) {
			throw new ExperimentOperationException(name, "already exists");
		}

		this.setOperationArguments(operation, arguments);

		this.operations.put(name, operation);
		this.arguments.put(name, arguments);
	}

	public void addParameter(String name, ExperimentParameter parameter,
			Object[] arguments) throws ExperimentParameterException {

		if (parameters.containsKey(name)) {
			throw new ExperimentParameterException(name, "already exists");
		}

		int order = 0;

		if (parameter.getArgumentsNumber() == arguments.length) {

			for (Object argument : arguments) {
				try {

					if (parameter.argumentIsOperation(order)) {
						if (!this.containsOperation((String) argument)) {
							ExperimentParameterArgumentException ex = new ExperimentParameterArgumentException(
									name, (String) argument,
									"operation does not exist");
							ex.getAction().put("op-argument", true);
							throw ex;
						}
					} else {
						if (parameter.argumentIsParameter(order)) {
							if (!this.containsParameter((String) argument)) {
								ExperimentParameterArgumentException ex = new ExperimentParameterArgumentException(
										name, (String) argument,
										"parameter does not exist");
								throw ex;
							}

							ExperimentParameter experimentParameter = this
									.getParameter((String) argument);

							if (!parameter.getParameterDependencies()
									.get(order).getName()
									.equals(experimentParameter.getName())) {

								throw new ExperimentParameterArgumentException(
										name, (String) argument,
										"type is invalid");
							}

						}
					}

				} catch (UndefinedExperimentParameterException e) {
					throw new ExperimentParameterArgumentException(
							e.getAction());
				}

				order++;
			}

		} else
			throw new ExperimentParameterArgumentException(name, null,
					"bad arguments");

		this.parameters.put(name, parameter);
	}

	public void setOperationArguments(ExperimentOperation operation,
			String[] arguments) throws ExperimentOperationArgumentException {

		int order = 0;

		ExperimentParameter[] parameterTypes = operation.getParameterTypes();

		for (String argument : arguments) {

			String actualArgument = argument;

			if (argument.contains(".")) {
				actualArgument = argument.substring(0, argument.indexOf("."));
			}

			try {
				ExperimentParameter experimentParameter = this
						.getParameter(actualArgument);

				if (!(parameterTypes[order].getType().getValueType() == Object.class)
						&& (!experimentParameter.getName().equals(
								parameterTypes[order].getName()) || !argument
								.equals(actualArgument))) {

					throw new ExperimentOperationArgumentException(
							operation.getName(), actualArgument,
							"incorrect type");
				}

			} catch (UndefinedExperimentParameterException e) {

				if (this.containsOperation(actualArgument)) {
					if (!parameterTypes[order].getType().isOperationDependent()) {
						throw new ExperimentOperationArgumentException(
								operation.getName(), actualArgument,
								"incorrect type");
					}
				} else {
					throw new ExperimentOperationArgumentException(
							operation.getName(), actualArgument,
							"operation arg does not exist");
				}
			}

			order++;
		}
	}

	public String[] getOperationArguments(String operation) {

		return arguments.get(operation);
	}

	public Set<String> getOperationNames() {
		return operations.keySet();
	}

	public Set<String> getParameterNames() {
		return parameters.keySet();
	}

	public ExperimentOperation getOperation(String name)
			throws NoSuchOperationException {
		if (operations.containsKey(name)) {
			return operations.get(name);
		}

		throw new NoSuchOperationException(name);
	}

	public boolean containsOperation(String name) {
		return operations.containsKey(name);
	}

	public boolean containsParameter(String name) {
		return parameters.containsKey(name);
	}

	public ExperimentParameter getParameter(String name)
			throws UndefinedExperimentParameterException {
		if (parameters.containsKey(name)) {
			return parameters.get(name);
		}

		throw new UndefinedExperimentParameterException(name);
	}
}
