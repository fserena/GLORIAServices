package eu.gloria.gs.services.experiment.base.parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eu.gloria.gs.services.experiment.base.contexts.Context;
import eu.gloria.gs.services.experiment.base.contexts.ContextNotReadyException;
import eu.gloria.gs.services.experiment.base.contexts.ExperimentContext;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.parameters.NoSuchParameterException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.experiment.parameters.ParameterContextService;
import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.utils.JSONConverter;

public class ParameterContext extends Context {

	private ExperimentParameter parameter = null;
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

	public void instantiate() throws ExperimentParameterException,
			NoSuchExperimentException {

		try {
			this.getAdapter().addParameterContext(
					this.context.getExperimentName(), this.getName(),
					this.context.getReservation());
		} catch (ActionException e) {
			throw new ExperimentParameterException(e.getAction());
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

							Action action = new Action();
							ExperimentParameterException ex = new ExperimentParameterException(
									action);
							ex.getAction().put("phase", "operation execution");
							ex.getAction().put("cause", "argument json error");
							ex.getAction().put("argument", contextArgument);

							throw ex;
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

	public Object parseValue(String valueStr, String[] tree)
			throws ExperimentParameterException,
			ExperimentNotInstantiatedException, NoSuchParameterException {

		Class<?> valueType = this.getExperimentParameter().getType()
				.getValueType();
		Class<?> elementType = this.getExperimentParameter().getType()
				.getElementType();

		Object value;
		try {
			value = JSONConverter.fromJSON(valueStr, valueType, elementType);
		} catch (IOException e1) {
			throw new ExperimentParameterException(this.getName(),
					"value json error");
		}

		boolean listIndexing = false;
		String indexArg = "";

		if (tree != null && tree.length > 1) {
			for (int i = 1; i < tree.length; i++) {
				if (value instanceof Map) {
					// if (((Map<?, ?>) value).containsKey(tree[i])) {
					value = ((Map<?, ?>) value).get(tree[i]);
					/*
					 * } else { throw new
					 * ExperimentParameterException("The property does not exist."
					 * ); }
					 */
				} else if (value instanceof List) {
					if (!listIndexing && tree[i].startsWith("[")) {
						listIndexing = true;
						indexArg = "";
						String listOrder = tree[i].substring(1);

						if (listOrder.endsWith("]")) {
							indexArg += listOrder.substring(0,
									listOrder.length() - 1);
							listIndexing = false;
						} else {
							indexArg += listOrder;
						}
					} else if (listIndexing) {
						if (tree[i].endsWith("]")) {
							indexArg += "."
									+ tree[i]
											.substring(0, tree[i].length() - 1);
							listIndexing = false;
						} else {
							indexArg += "." + tree[i];
						}
					}

					if (!listIndexing && !indexArg.equals("")) {
						int index = 0;

						if (indexArg.equals("length")) {
							value = ((List<?>) value).size();
						} else {
							try {
								index = Integer.valueOf(indexArg);
							} catch (NumberFormatException e) {
								index = (Integer) this.getExperimentContext()
										.getParameterValue(indexArg);
							}

							value = ((List<?>) value).get(index);
						}
					}
				} else {
					throw new ExperimentParameterException(this.getName(),
							"cannot access object property");
				}
			}
		}

		return value;
	}

	public String serializeValue(Object value) throws IOException {

		return JSONConverter.toJSON(value);
	}

	public Object getValue(String[] tree) throws ExperimentParameterException,
			ExperimentNotInstantiatedException, NoSuchParameterException {

		try {
			Object value = (Object) this.getAdapter().getParameterContextValue(
					name, this.context.getReservation());

			value = this.parseValue((String) value, tree);

			if (value == null)
				return null;

			return value;
		} catch (ActionException e) {
			throw new ExperimentParameterException(e.getAction());
		}
	}

	@SuppressWarnings("unchecked")
	public void setValue(String[] tree, Object value)
			throws ExperimentParameterException,
			ExperimentNotInstantiatedException, NoSuchParameterException {

		Object actualValue = null;

		try {

			if (tree != null && tree.length > 1) {
				actualValue = this.getValue(null);
				Object currentNode = actualValue;

				boolean listIndexing = false;
				String indexArg = "";

				for (int i = 1; i < tree.length; i++) {
					if (currentNode instanceof Map) {
						if (i < tree.length - 1) {
							if (!((Map<?, ?>) currentNode).containsKey(tree[i])) {
								((Map<String, Object>) currentNode).put(
										tree[i], new LinkedHashMap<>());
							}
							currentNode = ((Map<?, ?>) currentNode)
									.get(tree[i]);
						} else {
							((Map<String, Object>) currentNode).put(tree[i],
									value);
						}
					} else if (currentNode instanceof List) {
						if (!listIndexing && tree[i].startsWith("[")) {
							listIndexing = true;
							indexArg = "";

							String listOrder = tree[i].substring(1);

							if (listOrder.endsWith("]")) {
								indexArg += listOrder.substring(0,
										listOrder.length() - 1);
								listIndexing = false;
							} else {
								indexArg += listOrder;
							}
						} else if (listIndexing) {
							if (tree[i].endsWith("]")) {
								indexArg += "."
										+ tree[i].substring(0,
												tree[i].length() - 1);
								listIndexing = false;
							} else {
								indexArg += "." + tree[i];
							}
						}

						if (!listIndexing && !indexArg.equals("")) {
							if (indexArg.equals("add")) {
								if (i == tree.length - 1) {
									((List<Object>) currentNode).add(value);
								} else {
									throw new ExperimentParameterException(
											this.getName(),
											"lists can only be modified at leaf level");
								}
							} else {
								int paramIndex = 0;

								try {
									paramIndex = Integer.valueOf(indexArg);
								} catch (NumberFormatException e) {
									paramIndex = (Integer) this
											.getExperimentContext()
											.getParameterValue(indexArg);
								}

								if (((List<Object>) currentNode).size() < paramIndex + 1) {
									int size = ((List<Object>) currentNode)
											.size();
									for (int j = size; j < paramIndex + 1; j++) {

										if (tree.length > i + 1) {
											if (tree[i + 1].startsWith("[")) {
												((List<Object>) currentNode)
														.add(new ArrayList<>());
											} else {
												((List<Object>) currentNode)
														.add(new LinkedHashMap<>());
											}
										} else {
											if (j == paramIndex) {
												((List<Object>) currentNode)
														.add(value);
											} else {
												((List<Object>) currentNode)
														.add(null);
											}
										}
									}
								}

								currentNode = ((List<Object>) currentNode)
										.get(paramIndex);
							}
						}
					} else {
						throw new ExperimentParameterException(this.getName(),
								"cannot access object property");
					}
				}
			} else {
				actualValue = value;
			}

			String valueStr = this.serializeValue(actualValue);

			this.getAdapter().setParameterContextValue(name,
					this.context.getReservation(), valueStr);
		} catch (ActionException e) {
			throw new ExperimentParameterException(e.getAction());
		} catch (IOException e) {
			throw new ExperimentParameterException(this.getName(),
					"value json error");
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
