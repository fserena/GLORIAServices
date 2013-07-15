package eu.gloria.gs.services.experiment.base.models;

import java.util.ArrayList;
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
			throw new ExperimentOperationException("The operation '" + name
					+ " already exists in the current model");
		}

		this.setOperationArguments(operation, arguments);

		this.operations.put(name, operation);
		this.arguments.put(name, arguments);
	}

	public void addParameter(String name, ExperimentParameter parameter,
			String[] arguments) throws ExperimentParameterException {

		if (parameters.containsKey(name)) {
			throw new ExperimentParameterException("The parameter '" + name
					+ " already exists in the current model");
		}

		int order = 0;

		if (parameter.getArgumentsNumber() == arguments.length) {

			ArrayList<Class<?>> parameterValueTypes = parameter.getType()
					.getArgumentTypes();

			for (String argument : arguments) {
				try {

					if (parameter.argumentIsOperation(order)) {
						if (!this.containsOperation(argument)) {
							throw new ExperimentParameterArgumentException(
									"The operation referenced is not contained on the current model");
						}
					} else {
						if (parameter.argumentIsParameter(order)) {
							if (!this.containsParameter(argument)) {
								throw new ExperimentParameterArgumentException(
										"The parameter referenced is not contained on the current model");
							}

							ExperimentParameter experimentParameter = this
									.getParameter(argument);

							if (!parameter.getParameterDependencies()
									.get(order).getName()
									.equals(experimentParameter.getName())) {

								throw new ExperimentParameterArgumentException(
										"The type of the referenced parameter is not valid");
							}

						} else {

							if (parameterValueTypes.get(order).equals(
									Double.class)) {
								try {
									Double.parseDouble(argument);
								} catch (NumberFormatException e) {
									throw new ExperimentParameterArgumentException(
											e.getMessage());
								}
							} else if (parameterValueTypes.get(order).equals(
									Integer.class)) {
								try {
									Integer.parseInt(argument);
								} catch (NumberFormatException e) {
									throw new ExperimentParameterArgumentException(
											e.getMessage());
								}
							}
						}
					}

				} catch (UndefinedExperimentParameterException e) {
					throw new ExperimentParameterArgumentException(
							e.getMessage());
				}

				order++;
			}

		} else
			throw new ExperimentParameterArgumentException(
					"Bad arguments number for parameter '" + name + "'");

		this.parameters.put(name, parameter);
	}

	public void setOperationArguments(ExperimentOperation operation,
			String[] arguments) throws ExperimentOperationArgumentException {

		int order = 0;

		ExperimentParameter[] parameterTypes = operation.getParameterTypes();

		for (String argument : arguments) {
			try {
				ExperimentParameter experimentParameter = this
						.getParameter(argument);

				if (!experimentParameter.getName().equals(
						parameterTypes[order].getName())) {
					throw new ExperimentOperationArgumentException("Argument "
							+ order + " (" + argument + ") of operation '"
							+ operation.getName() + "' is incorrect");
				}

			} catch (UndefinedExperimentParameterException e) {
				throw new ExperimentOperationArgumentException(e.getMessage());
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

		throw new NoSuchOperationException("The operation '" + name
				+ "' is not included on the experiment model");
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

		throw new UndefinedExperimentParameterException("The parameter '"
				+ name + "' is not included on the experiment model");
	}
}
