package eu.gloria.gs.services.experiment.base.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.base.data.ExperimentInformation;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.data.OperationInformation;
import eu.gloria.gs.services.experiment.base.data.ParameterInformation;
import eu.gloria.gs.services.experiment.base.models.DuplicateExperimentException;
import eu.gloria.gs.services.experiment.base.models.ExperimentFeature;
import eu.gloria.gs.services.experiment.base.models.InvalidExperimentModelException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationFactory;
import eu.gloria.gs.services.experiment.base.operations.OperationTypeNotAvailableException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterFactory;
import eu.gloria.gs.services.experiment.base.parameters.ParameterTypeNotAvailableException;

public class ExperimentModelFactory {

	private ExperimentDBAdapter adapter;
	private ExperimentParameterFactory parameterFactory;
	private ExperimentOperationFactory operationFactory;
	private ExperimentFeatureFactory featureFactory;

	protected ExperimentModelFactory() {
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

	public CustomExperimentModel loadCustomExperiment(String experiment)
			throws NoSuchExperimentException, InvalidExperimentModelException {
		CustomExperimentModel model = null;

		ExperimentInformation expInfo = null;

		try {
			expInfo = adapter.getExperimentInformation(experiment);
		} catch (ExperimentDatabaseException e) {
			throw new NoSuchExperimentException("Experiment: '" + experiment);
		} catch (NoSuchExperimentException e) {
			throw new NoSuchExperimentException("Experiment: '" + experiment);
		}

		model = new CustomExperimentModel();
		model.setAdapter(this.adapter);
		model.setName(experiment);
		model.setParameterFactory(this.parameterFactory);
		model.setOperationFactory(this.operationFactory);
		model.setFeatureFactory(this.featureFactory);

		List<ParameterInformation> parameters = expInfo.getParameters();
		List<ParameterInformation> pointers = new ArrayList<>();

		for (ParameterInformation parameterInfo : parameters) {

			if (!parameterInfo.getParameter().getType().isOperationDependent()) {

				ExperimentParameter expParameter = null;

				try {
					expParameter = this.parameterFactory
							.createParameter(parameterInfo.getType());
				} catch (ParameterTypeNotAvailableException
						| ExperimentParameterException e) {
					throw new InvalidExperimentModelException(e.getMessage());
				}

				try {
					model.addParameter(parameterInfo.getName(),
							expParameter, parameterInfo.getArguments());
				} catch (ExperimentParameterException e) {
					throw new InvalidExperimentModelException(e.getMessage());
				}

			} else {
				pointers.add(parameterInfo);
			}
		}

		List<OperationInformation> preCreatedOperations = new ArrayList<>();

		for (ParameterInformation parameterInfo : pointers) {
			ExperimentParameter expParameter = null;

			try {
				expParameter = (ExperimentParameter) this.parameterFactory
						.createParameter(parameterInfo.getType());
			} catch (ParameterTypeNotAvailableException
					| ExperimentParameterException e) {
				throw new InvalidExperimentModelException(e.getMessage());
			}

			Map<Integer, ExperimentOperation> operationDependencies = expParameter
					.getOperationDependencies();

			String[] paramArgs = (String[])parameterInfo.getArguments();

			for (Integer order : operationDependencies.keySet()) {

				ExperimentOperation depOperation = operationDependencies
						.get(order);

				String opName = paramArgs[order];

				OperationInformation actualOperationInfo = expInfo
						.getOperation(opName);

				if (depOperation == null
				// || !depOperation.getName().equals(
				// actualOperationInfo.getOperation().getName())
				) {
					throw new InvalidExperimentModelException(
							"The operation dependent parameter '"
									+ parameterInfo.getName()
									+ "' is not well-defined");
				}

				try {
					model.addOperation(actualOperationInfo.getName(),
							actualOperationInfo.getOperation(),
							actualOperationInfo.getArguments());
					preCreatedOperations.add(actualOperationInfo);
				} catch (ExperimentOperationException e) {
					throw new InvalidExperimentModelException(e.getMessage());
				}

			}

			try {
				model.addParameter(parameterInfo.getName(), expParameter,
						parameterInfo.getArguments());
			} catch (ExperimentParameterException e) {
				throw new InvalidExperimentModelException(e.getMessage());
			}
		}

		List<OperationInformation> operations = expInfo.getOperations();

		for (OperationInformation operation : operations) {

			if (!preCreatedOperations.contains(operation)) {
				ExperimentOperation expOperation = null;
				try {
					expOperation = operationFactory.createOperation(operation
							.getType());
				} catch (OperationTypeNotAvailableException e) {
					throw new InvalidExperimentModelException(e.getMessage());

				}

				try {
					model.addOperation(operation.getName(), expOperation,
							operation.getArguments());
				} catch (ExperimentOperationException e) {
					throw new InvalidExperimentModelException(e.getMessage());
				}
			}
		}

		preCreatedOperations.clear();
		pointers.clear();

		return model;
	}

	public void createCustomExperiment(String experiment, String author, String type)
			throws DuplicateExperimentException, ExperimentDatabaseException {
		try {
			if (adapter.containsExperiment(experiment)) {
				throw new DuplicateExperimentException("Experiment: "
						+ experiment);
			}
		} catch (ExperimentDatabaseException e) {
			throw e;
		}

		try {
			adapter.createExperiment(experiment, author, type);
		} catch (ExperimentDatabaseException e) {
			throw e;
		}

	}

	public Map<String, ExperimentParameter> getAllExperimentParameters() {
		return this.parameterFactory.getAllParameters();
	}

	public Map<String, ExperimentOperation> getAllExperimentOperations() {
		return this.operationFactory.getAllOperations();
	}

	public Map<String, ExperimentFeature> getAllExperimentFeatures() {
		return this.featureFactory.getAllFeatures();
	}

	public ExperimentParameter getExperimentParameter(String name) {
		return this.parameterFactory.getParameter(name);

	}

	public ExperimentOperation getExperimentOperation(String name) {
		return this.operationFactory.getOperation(name);

	}

	public ExperimentFeature getExperimentFeature(String name) {
		return this.featureFactory.getFeature(name);

	}
}
