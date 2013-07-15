package eu.gloria.gs.services.experiment.base.parameters;

import java.util.ArrayList;
import java.util.Map;

import eu.gloria.gs.services.experiment.base.contexts.Context;
import eu.gloria.gs.services.experiment.base.contexts.ContextNotReadyException;
import eu.gloria.gs.services.experiment.base.contexts.ExperimentContext;
import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.UndefinedExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.experiment.online.parameters.ParameterContextService;

public class ParameterContext extends Context {

	private ExperimentParameter parameter = null;
	private ExperimentDBAdapter adapter;
	private String name;
	private ExperimentContext context;
	private ParameterContextService contextService;
	private String[] contextArguments;

	public ParameterContext() {
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setExperimentParameter(ExperimentParameter parameter) {
		this.parameter = parameter;
	}

	public ExperimentParameter getExperimentParameter() {
		return this.parameter;
	}

	public void setExperimentContext(ExperimentContext context) {
		this.context = context;
	}

	public ExperimentContext getExperimentContext() {
		return this.context;
	}

	public void setAdapter(ExperimentDBAdapter adapter) {
		this.adapter = adapter;
	}

	public ExperimentDBAdapter getAdapter() {
		return this.adapter;
	}

	public void instantiate() throws ExperimentParameterException,
			NoSuchExperimentException {

		try {
			adapter.addParameterContext(this.context.getExperimentName(),
					this.getName(), this.context.getReservation());
		} catch (ExperimentDatabaseException e) {
			throw new ExperimentParameterException(e.getMessage());
		}
	}

	private void manageOperationsExecution(Map<String, String[]> list)
			throws ExperimentParameterException, ContextNotReadyException {
		if (list != null) {

			Map<String, Object> properties = this.parameter.getProperties();

			for (String operation : list.keySet()) {

				String[] operationArguments = list.get(operation);

				ArrayList<Class<?>> argumentTypes = this.parameter.getType()
						.getArgumentTypes();

				int order = 0;

				Object[] serviceOperationArguments = new Object[operationArguments.length];

				for (Object argument : operationArguments) {

					if (((String) argument).startsWith("arg")) {

						int contextArgOrder = Integer
								.parseInt(((String) argument).replaceFirst(
										"arg", ""));

						Class<?> argumentType = argumentTypes
								.get(contextArgOrder);

						String contextArgument = contextArguments[contextArgOrder];
						Object serviceArgument = null;

						if (argumentType.equals(Integer.class)) {
							serviceArgument = Integer.parseInt(contextArgument);
						} else if (argumentType.equals(Double.class)) {
							serviceArgument = Double
									.parseDouble(contextArgument);
						} else {
							if (contextArgument.equals("null")
									|| contextArgument.equals("NULL")) {
								serviceArgument = null;
							} else {
								serviceArgument = contextArgument;
							}
						}

						serviceOperationArguments[order] = serviceArgument;
					} else {
						if (properties != null
								&& properties.containsKey((String) argument)) {
							serviceOperationArguments[order] = properties
									.get(argument);
						} else {
							serviceOperationArguments[order] = argument;
						}
					}

					order++;
				}

				contextService.treatOperation(this, operation,
						serviceOperationArguments);
			}
		}
	}

	public void init() throws ExperimentParameterException,
			ContextNotReadyException {

		this.manageOperationsExecution(this.parameter.getInitOperations());
	}

	public void end() throws ExperimentParameterException,
			ContextNotReadyException {
	}

	public Object parseValue(String valueStr) {

		Class<?> valueType = this.getExperimentParameter().getType()
				.getValueType();

		if (valueType.equals(Integer.class)) {
			return Integer.parseInt(valueStr);
		} else if (valueType.equals(Double.class)) {
			return Double.parseDouble(valueStr);
		} else {
			return valueStr;
		}
	}

	public String serializeValue(Object value) {
		Class<?> valueType = this.getExperimentParameter().getType()
				.getValueType();

		if (value == null)
			return null;

		return String.valueOf(valueType.cast(value));
	}

	public Object getValue() throws ExperimentParameterException,
			NoSuchExperimentException, ExperimentNotInstantiatedException {

		try {
			Object value = (Object) adapter.getParameterContextValue(
					this.context.getExperimentName(), name,
					this.context.getReservation());

			if (value == null)
				return null;

			return this.parseValue((String) value);

		} catch (ExperimentDatabaseException e) {
			throw new ExperimentParameterException(e.getMessage());
		}
	}

	public void setValue(Object value)
			throws UndefinedExperimentParameterException,
			NoSuchExperimentException, ExperimentParameterException,
			ExperimentNotInstantiatedException {

		String valueStr = this.serializeValue(value);

		try {
			adapter.setParameterContextValue(this.context.getExperimentName(),
					name, this.context.getReservation(), valueStr);
		} catch (ExperimentDatabaseException e) {
			throw new ExperimentParameterException(e.getMessage());
		}
	}

	public ParameterContextService getContextService() {
		return contextService;
	}

	public void setContextService(ParameterContextService contextService) {
		this.contextService = contextService;
	}

	public String[] getContextArguments() {
		return contextArguments;
	}

	public void setContextArguments(String[] contextArguments) {
		this.contextArguments = contextArguments;
	}
}
