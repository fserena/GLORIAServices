package eu.gloria.gs.services.experiment.base.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.base.data.ExperimentInformation;
import eu.gloria.gs.services.experiment.base.data.FeatureCompliantInformation;
import eu.gloria.gs.services.experiment.base.data.FeatureInformation;
import eu.gloria.gs.services.experiment.base.data.JSONConverter;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.data.OperationInformation;
import eu.gloria.gs.services.experiment.base.data.ParameterInformation;
import eu.gloria.gs.services.experiment.base.models.CustomExperimentException;
import eu.gloria.gs.services.experiment.base.models.ExperimentFeature;
import eu.gloria.gs.services.experiment.base.models.OperationFeature;
import eu.gloria.gs.services.experiment.base.models.ParameterFeature;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationFactory;
import eu.gloria.gs.services.experiment.base.operations.OperationTypeNotAvailableException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterFactory;
import eu.gloria.gs.services.experiment.base.parameters.ParameterTypeNotAvailableException;

public class CustomExperimentModel extends ExperimentModel {

	private ExperimentDBAdapter adapter;
	private ExperimentParameterFactory parameterFactory;
	private ExperimentOperationFactory operationFactory;
	private ExperimentFeatureFactory featureFactory;

	public CustomExperimentModel() {
		super();
	}

	public void setAdapter(ExperimentDBAdapter adapter) {
		this.adapter = adapter;
	}

	public void setParameterFactory(ExperimentParameterFactory factory) {
		this.parameterFactory = factory;
	}

	public void setOperationFactory(ExperimentOperationFactory factory) {
		this.operationFactory = factory;
	}

	public void setFeatureFactory(ExperimentFeatureFactory factory) {
		this.featureFactory = factory;
	}

	public void buildOperation(OperationInformation operationInfo)
			throws OperationTypeNotAvailableException,
			CustomExperimentException, NoSuchExperimentException,
			ExperimentOperationException {

		ExperimentOperation operation = null;

		try {
			operation = operationFactory.createOperation(operationInfo
					.getType());
		} catch (OperationTypeNotAvailableException e) {
			throw e;
		}

		this.addOperation(operationInfo.getName(), operation,
				operationInfo.getArguments());

		try {
			adapter.addExperimentOperation(this.getName(), operationInfo);
		} catch (ExperimentDatabaseException e) {
			throw new CustomExperimentException(e.getMessage());
		} catch (NoSuchExperimentException e) {
			throw e;
		}

		try {
			adapter.setOperationArguments(this.getName(),
					operationInfo.getName(), operationInfo.getArguments());
		} catch (ExperimentDatabaseException e) {
			throw new CustomExperimentException(e.getMessage());
		}
	}

	public void buildParameter(ParameterInformation parameterInfo)
			throws CustomExperimentException,
			ParameterTypeNotAvailableException, NoSuchExperimentException,
			ExperimentParameterException {

		ExperimentParameter parameter = null;

		try {
			parameter = parameterFactory.createParameter(parameterInfo
					.getType());
		} catch (ParameterTypeNotAvailableException
				| ExperimentParameterException e) {
			throw new CustomExperimentException(e.getMessage());
		}

		parameterInfo.setParameter(parameter);

		this.addParameter(parameterInfo.getName(), parameter,
				parameterInfo.getArguments());

		try {
			adapter.addExperimentParameter(this.getName(), parameterInfo);
		} catch (ExperimentDatabaseException e) {
			throw new CustomExperimentException(e.getMessage());
		} catch (NoSuchExperimentException e) {
			throw e;
		}
	}

	private String processSuffixName(FeatureInformation featureInfo,
			String[] nameSuffix) {
		String suffixArgument = "";

		if (nameSuffix != null) {
			for (String suffixPart : nameSuffix) {
				if (suffixPart.startsWith("arg")) {
					suffixPart = suffixPart.replace("arg", "");
					int featureArgumentOrder = Integer.parseInt(suffixPart);

					suffixArgument += featureInfo.getArguments()[featureArgumentOrder];
				} else {
					suffixArgument += suffixPart;
				}
			}
		}

		return suffixArgument;
	}

	public void buildFeature(FeatureInformation featureInfo)
			throws CustomExperimentException, NoSuchExperimentException {

		ExperimentFeature feature = null;

		feature = featureFactory.getFeature(featureInfo.getName());

		Map<String, ParameterFeature> parameters = feature.getParameters();
		Map<String, OperationFeature> operations = feature.getOperations();
		Map<String, String> concreteParamNames = new HashMap<>();
		Map<String, String> concreteOpNames = new HashMap<>();

		ArrayList<String> createdOperations = new ArrayList<>();
		ArrayList<String> createdParameters = new ArrayList<>();

		for (String parameterModelName : parameters.keySet()) {
			ParameterFeature parameterFeature = parameters
					.get(parameterModelName);

			String[] nameSuffix = parameterFeature.getNameSuffix();

			concreteParamNames.put(parameterModelName, parameterModelName
					+ this.processSuffixName(featureInfo, nameSuffix));
		}

		for (String operationModelName : operations.keySet()) {
			OperationFeature operationFeature = operations
					.get(operationModelName);

			String[] nameSuffix = operationFeature.getNameSuffix();

			concreteOpNames.put(
					operationModelName,
					operationModelName
							+ this.processSuffixName(featureInfo, nameSuffix));
		}

		boolean allParametersCreated = false;
		boolean allOperationsCreated = false;

		int addedOperations = 0;
		int totalOperations = operations.size();
		int addedParameters = 0;
		int totalParameters = parameters.size();

		int operationIndex = 0;
		int parameterIndex = 0;

		String[] operationNames = operations.keySet().toArray(new String[0]);
		String[] parameterNames = parameters.keySet().toArray(new String[0]);

		while (!allParametersCreated || !allOperationsCreated) {

			if (!allParametersCreated) {
				String paramName = parameterNames[parameterIndex];

				if (!createdParameters.contains(paramName)) {
					ParameterFeature parameterFeature = parameters
							.get(paramName);

					boolean allowCreate = true;

					if (parameterFeature.getParameter().getType()
							.isOperationDependent()) {

						String[] paramFeatureArgs = parameterFeature
								.getParameterArguments();

						int order = 0;

						for (String paramArg : paramFeatureArgs) {
							if (parameterFeature.getParameter()
									.argumentIsOperation(order)) {

								String concreteParamArg = null;
								try {
									concreteParamArg = (String) JSONConverter
											.fromJSON(paramArg, String.class,
													null);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								if (!createdOperations
										.contains(concreteParamArg)) {
									allowCreate = false;
									break;
								}
							}

							order++;
						}
					}

					if (allowCreate) {
						ParameterInformation paramInfo = new ParameterInformation();
						paramInfo.setName(concreteParamNames.get(paramName));
						paramInfo.setParameter(parameterFeature.getParameter());

						paramInfo.setType(parameterFeature.getParameter()
								.getConcreteName());

						String[] parameterArguments = parameterFeature
								.getParameterArguments();

						String[] processedArguments = null;

						if (parameterArguments == null) {
							processedArguments = new String[0];
						} else {

							processedArguments = new String[parameterArguments.length];
							int i = 0;

							for (String argument : parameterArguments) {
								if (argument.startsWith("arg")) {

									String filteredArgument = argument.replace(
											"arg", "");
									int featureArgumentOrder = Integer
											.parseInt(filteredArgument);

									processedArguments[i] = featureInfo
											.getArguments()[featureArgumentOrder]
											.trim();

								} else {
									ExperimentParameter expParameter = parameterFeature
											.getParameter();

									if (expParameter.getType()
											.isOperationDependent()) {
										if (expParameter.argumentIsOperation(i)) {
											if (concreteOpNames
													.containsKey(argument)) {
												argument = concreteOpNames
														.get(argument);
											}
										}
									} else {

										if (concreteParamNames
												.containsKey(argument)) {
											argument = concreteParamNames
													.get(argument);
										}
									}

									processedArguments[i] = argument.trim();
								}

								i++;
							}
						}
						Object[] argsArray = null;

						try {
							argsArray = (Object[]) JSONConverter.fromJSON(
									Arrays.toString(processedArguments),
									Object[].class, null);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						paramInfo.setArguments(argsArray);

						try {

							this.buildParameter(paramInfo);

							addedParameters++;
							createdParameters.add(paramName);

						} catch (ParameterTypeNotAvailableException e) {
							throw new CustomExperimentException(e.getMessage());
						} catch (ExperimentParameterException e) {
							if (!e.getMessage().contains("already")) {
								throw new CustomExperimentException(
										e.getMessage());
							}

							addedParameters++;
							createdParameters.add(paramName);
						}

						allParametersCreated = addedParameters == totalParameters;

					}
				}

				if (parameterIndex < totalParameters - 1) {
					if (Math.random() > 0.5) {
						parameterIndex++;
					}
				} else
					parameterIndex = 0;
			}

			if (!allOperationsCreated) {
				String opName = operationNames[operationIndex];

				if (!createdOperations.contains(opName)) {
					OperationFeature operationFeature = operations.get(opName);

					boolean allowCreate = true;

					String[] relations = operationFeature.getRelations();

					for (String relation : relations) {

						String dependencyName = relation.split("\\.")[0];
						if (!this.containsParameter(dependencyName)
								&& !createdParameters.contains(dependencyName)) {
							allowCreate = false;
							break;
						}
					}

					if (allowCreate) {
						OperationInformation operationInfo = new OperationInformation();
						operationInfo.setName(concreteOpNames.get(opName));
						operationInfo.setOperation(operationFeature
								.getOperation());
						operationInfo.setType(operationFeature.getOperation()
								.getConcreteName());

						String[] argumentRelations = new String[0];

						if (relations != null) {
							argumentRelations = new String[relations.length];
						}

						int i = 0;
						for (String relation : relations) {

							argumentRelations[i] = relation;// concreteParamNames
							// .get(relation.split("\\.")[0]);
							i++;
						}

						operationInfo.setArguments(argumentRelations);

						try {
							this.buildOperation(operationInfo);

							createdOperations.add(opName);
							addedOperations++;
						} catch (ExperimentOperationException e) {
							if (!e.getMessage().contains("already")) {
								throw new CustomExperimentException(
										e.getMessage());
							}

							createdOperations.add(opName);
							addedOperations++;
						} catch (OperationTypeNotAvailableException e) {
							throw new CustomExperimentException(e.getMessage());
						}

						allOperationsCreated = addedOperations == totalOperations;
					}
				}

				if (operationIndex < totalOperations - 1) {
					if (Math.random() > 0.5) {
						operationIndex++;
					}
				} else
					operationIndex = 0;
			}
		}

		createdOperations.clear();
		createdParameters.clear();
	}

	private List<String> getParameterDependentOperations(
			ExperimentFeature feature, String parameterName) {

		Map<String, OperationFeature> featureOperations = feature
				.getOperations();

		List<String> dependentOperations = new ArrayList<>();

		for (String operationName : featureOperations.keySet()) {
			OperationFeature opFeature = featureOperations.get(operationName);

			String[] relations = opFeature.getRelations();

			int order = 0;
			boolean include = false;
			String dependency = "";

			for (String relation : relations) {

				if (relation.equals(parameterName)) {
					include = true;
					dependency += opFeature.getOperation().getConcreteName()
							+ "/" + order;
				} else {
					dependency += feature.getParameters().get(relation)
							.getParameter().getConcreteName();
				}

				order++;
			}

			if (include) {
				dependentOperations.add(dependency);
			}
		}

		return dependentOperations;
	}

	private List<String> getParameterDependentOperations(
			ExperimentInformation expInfo, String parameterName) {

		List<OperationInformation> modelOperations = expInfo.getOperations();

		List<String> dependentOperations = new ArrayList<>();

		for (OperationInformation opInfo : modelOperations) {

			String[] arguments = opInfo.getArguments();

			int order = 0;
			boolean include = false;
			String dependency = "";

			for (String relation : arguments) {
				if (relation.equals(parameterName)) {
					include = true;
					dependency += opInfo.getOperation().getConcreteName() + "/"
							+ order;
				} else {
					dependency += expInfo.getParameter(relation).getParameter()
							.getConcreteName();
				}

				order++;
			}

			if (include) {
				dependentOperations.add(dependency);
			}
		}

		return dependentOperations;
	}

	public FeatureCompliantInformation getFeatureCompliantInformation(
			FeatureInformation featureInfo) throws CustomExperimentException {

		ExperimentFeature feature = null;

		FeatureCompliantInformation compliantInfo = new FeatureCompliantInformation();

		feature = featureFactory.getFeature(featureInfo.getName());

		Map<String, ParameterFeature> featureParameters = feature
				.getParameters();
		Map<String, OperationFeature> featureOperations = feature
				.getOperations();

		Map<String, String> returnFeatureParameters = new HashMap<>();
		Map<String, String> returnFeatureOperations = new HashMap<>();

		ExperimentInformation expInfo = null;
		try {
			expInfo = this.adapter.getExperimentInformation(this.getName());
		} catch (ExperimentDatabaseException | NoSuchExperimentException e) {
			throw new CustomExperimentException(e.getMessage());
		}

		compliantInfo.setExperiment(expInfo.getName());
		compliantInfo.setFeature(featureInfo.getName());

		List<ParameterInformation> paramInfos = expInfo.getParameters();

		for (String featureParamName : featureParameters.keySet()) {

			ParameterFeature parameterFeature = featureParameters
					.get(featureParamName);

			String[] parameterFeatureArguments = parameterFeature
					.getParameterArguments();

			ExperimentParameter testExpParameter = parameterFeature
					.getParameter();

			boolean parameterMatch = false;

			for (ParameterInformation paramInfo : paramInfos) {
				if (paramInfo.getParameter().getConcreteName()
						.equals(testExpParameter.getConcreteName())) {

					int i = 0;
					boolean allMatch = true;

					for (String parameterFeatureArg : parameterFeatureArguments) {
						if (parameterFeatureArg.startsWith("arg")) {
							int argumentOrder = Integer
									.parseInt(parameterFeatureArg.replace(
											"arg", ""));

							if (!featureInfo.getArguments()[argumentOrder]
									.equals("*")) {
								if (!paramInfo.getArguments()[i]
										.equals(featureInfo.getArguments()[argumentOrder])) {
									allMatch = false;
								}
							}
						}

						i++;
					}

					System.out.println("---> " + paramInfo.getName() + ":");

					List<String> featureParameterDepOps = this
							.getParameterDependentOperations(feature,
									featureParamName);

					List<String> modelParameterDepOps = this
							.getParameterDependentOperations(expInfo,
									paramInfo.getName());

					for (String featureParameterDepOp : featureParameterDepOps) {
						if (!modelParameterDepOps
								.contains(featureParameterDepOp)) {
							allMatch = false;
						}
					}

					if (allMatch) {
						System.out.println(featureParameterDepOps);
						System.out.println(modelParameterDepOps);
						parameterMatch = true;
						returnFeatureParameters.put(featureParamName,
								paramInfo.getName());
						break;
					}
				}
			}

			if (!parameterMatch) {
				throw new CustomExperimentException(
						"The model is not compatible with that feature");
			}
		}

		compliantInfo.setParameters(returnFeatureParameters);

		List<OperationInformation> opInfos = expInfo.getOperations();

		for (String featureOpName : featureOperations.keySet()) {

			OperationFeature operationFeature = featureOperations
					.get(featureOpName);

			ExperimentOperation testExpOperation = operationFeature
					.getOperation();

			String[] relations = operationFeature.getRelations();

			boolean operationMatch = false;

			for (OperationInformation opInfo : opInfos) {
				if (opInfo.getOperation().getConcreteName()
						.equals(testExpOperation.getConcreteName())) {

					String[] opInfoArgs = opInfo.getArguments();

					int i = 0;
					boolean allMatch = true;

					for (String parameterRelation : relations) {
						ParameterFeature paramFeature = feature.getParameters()
								.get(parameterRelation);

						if (!paramFeature
								.getParameter()
								.getConcreteName()
								.equals(expInfo.getParameter(opInfoArgs[i])
										.getParameter().getConcreteName())) {
							allMatch = false;
						}

						i++;
					}

					if (allMatch) {
						operationMatch = true;
						returnFeatureOperations.put(featureOpName,
								opInfo.getName());
						break;
					}
				}
			}

			if (!operationMatch) {
				throw new CustomExperimentException(
						"The model is not compatible with that feature");
			}
		}

		compliantInfo.setOperations(returnFeatureOperations);

		return compliantInfo;
	}

	public boolean testFeature(FeatureInformation featureInfo)
			throws CustomExperimentException {

		ExperimentFeature feature = null;

		feature = featureFactory.getFeature(featureInfo.getName());

		Map<String, ParameterFeature> featureParameters = feature
				.getParameters();
		Map<String, OperationFeature> featureOperations = feature
				.getOperations();

		ExperimentInformation expInfo = null;
		try {
			expInfo = this.adapter.getExperimentInformation(this.getName());
		} catch (ExperimentDatabaseException | NoSuchExperimentException e) {
			throw new CustomExperimentException(e.getMessage());
		}

		List<ParameterInformation> paramInfos = expInfo.getParameters();

		for (String featureParamName : featureParameters.keySet()) {

			ParameterFeature parameterFeature = featureParameters
					.get(featureParamName);

			String[] parameterFeatureArguments = parameterFeature
					.getParameterArguments();

			ExperimentParameter testExpParameter = parameterFeature
					.getParameter();

			boolean parameterMatch = false;

			for (ParameterInformation paramInfo : paramInfos) {
				if (paramInfo.getParameter().getConcreteName()
						.equals(testExpParameter.getConcreteName())) {

					int i = 0;
					boolean allMatch = true;

					for (String parameterFeatureArg : parameterFeatureArguments) {
						if (parameterFeatureArg.startsWith("arg")) {
							int argumentOrder = Integer
									.parseInt(parameterFeatureArg.replace(
											"arg", ""));

							if (!featureInfo.getArguments()[argumentOrder]
									.equals("*")) {
								if (!paramInfo.getArguments()[i]
										.equals(featureInfo.getArguments()[argumentOrder])) {
									allMatch = false;
								}
							}
						}

						i++;
					}

					if (allMatch) {
						parameterMatch = true;
						break;
					}
				}
			}

			if (!parameterMatch) {
				return false;
			}
		}

		List<OperationInformation> opInfos = expInfo.getOperations();

		for (String featureOpName : featureOperations.keySet()) {

			OperationFeature operationFeature = featureOperations
					.get(featureOpName);

			ExperimentOperation testExpOperation = operationFeature
					.getOperation();

			String[] relations = operationFeature.getRelations();

			boolean operationMatch = false;

			for (OperationInformation opInfo : opInfos) {
				if (opInfo.getOperation().getConcreteName()
						.equals(testExpOperation.getConcreteName())) {

					String[] opInfoArgs = opInfo.getArguments();

					int i = 0;
					boolean allMatch = true;

					for (String parameterRelation : relations) {
						ParameterFeature paramFeature = feature.getParameters()
								.get(parameterRelation);

						if (!paramFeature
								.getParameter()
								.getConcreteName()
								.equals(expInfo.getParameter(opInfoArgs[i])
										.getParameter().getConcreteName())) {
							allMatch = false;
						}

						i++;
					}

					if (allMatch) {
						operationMatch = true;
						break;
					}
				}
			}

			if (!operationMatch) {
				return false;
			}
		}

		return true;
	}
}
