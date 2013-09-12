package eu.gloria.gs.services.experiment.base.parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eu.gloria.gs.services.experiment.base.contexts.Context;
import eu.gloria.gs.services.experiment.base.contexts.ContextNotReadyException;
import eu.gloria.gs.services.experiment.base.contexts.ExperimentContext;
import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.base.data.JSONConverter;
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
	private Object[] contextArguments;

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

						Object contextArgument = contextArguments[contextArgOrder];
						Object serviceArgument;
						try {
							if (argumentType.equals(String.class)) {
								serviceArgument = (String) contextArgument;
							} else {
								serviceArgument = JSONConverter.fromJSON(
										(String) contextArgument, argumentType,
										null);
							}
						} catch (IOException e) {
							throw new ExperimentParameterException(
									e.getMessage());
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

	public Object parseValue(String valueStr, String[] tree) throws IOException {

		Class<?> valueType = this.getExperimentParameter().getType()
				.getValueType();
		Class<?> elementType = this.getExperimentParameter().getType()
				.getElementType();

		Object value = JSONConverter.fromJSON(valueStr, valueType, elementType);

		if (tree != null && tree.length > 1) {
			for (int i = 1; i < tree.length; i++) {
				if (value instanceof Map) {
					value = ((Map<?, ?>) value).get(tree[i]);
				} else {
					throw new IOException("Cannot access object property");
				}
			}
		}

		return value;
	}

	public String serializeValue(Object value) throws IOException {

		return JSONConverter.toJSON(value);
	}

	public Object getValue(String[] tree) throws ExperimentParameterException,
			NoSuchExperimentException, ExperimentNotInstantiatedException {

		try {
			Object value = (Object) adapter.getParameterContextValue(
					this.context.getExperimentName(), name,
					this.context.getReservation());

			value = this.parseValue((String) value, tree);

			if (value == null)
				return null;

			return value;
		} catch (ExperimentDatabaseException | IOException e) {
			throw new ExperimentParameterException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void setValue(String[] tree, Object value)
			throws UndefinedExperimentParameterException,
			NoSuchExperimentException, ExperimentParameterException,
			ExperimentNotInstantiatedException {

		Object actualValue = null;

		try {

			if (tree != null && tree.length > 1) {
				actualValue = this.getValue(null);
				Object currentNode = actualValue;
				for (int i = 1; i < tree.length; i++) {
					if (currentNode instanceof Map) {
						if (i < tree.length - 1) {
							if (!((Map<?, ?>) currentNode).containsKey(tree[i])) {
								((Map<String, Object>) currentNode).put(
										tree[i], new LinkedHashMap<>());
							} else {
								currentNode = ((Map<?, ?>) currentNode)
										.get(tree[i]);
							}
						} else {
							((Map<String, Object>) currentNode).put(tree[i],
									value);
						}
					} else if (currentNode instanceof List) {
						// if (i == tree.length - 1) {
						if (tree[i].startsWith("$")) {
							String listOrder = tree[i].substring(1);
							if (listOrder.equals("add")) {
								if (i == tree.length - 1) {
									((List<Object>) currentNode).add(value);
								} else {
									throw new IOException(
											"A list can only be modified at leaf level");
								}
							} else {
								int paramIndex = (Integer) this
										.getExperimentContext()
										.getParameterValue(listOrder);
								
								if (((List<Object>) currentNode).size() < paramIndex + 1) {
									for (int j = ((List<Object>) currentNode).size(); j < paramIndex + 1; j++) {
										((List<Object>) currentNode).add(null);
									}
								}
								
								currentNode = ((List<Object>) currentNode).get(paramIndex);
							}
						}
					} else {
						throw new IOException("Cannot access object property");
					}
				}
			} else {
				actualValue = value;
			}

			String valueStr = this.serializeValue(actualValue);

			adapter.setParameterContextValue(this.context.getExperimentName(),
					name, this.context.getReservation(), valueStr);
		} catch (ExperimentDatabaseException | IOException e) {
			throw new ExperimentParameterException(e.getMessage());
		}
	}

	public ParameterContextService getContextService() {
		return contextService;
	}

	public void setContextService(ParameterContextService contextService) {
		this.contextService = contextService;
	}

	public Object[] getContextArguments() {
		return contextArguments;
	}

	public void setContextArguments(Object[] contextArguments) {
		this.contextArguments = contextArguments;
	}
}
