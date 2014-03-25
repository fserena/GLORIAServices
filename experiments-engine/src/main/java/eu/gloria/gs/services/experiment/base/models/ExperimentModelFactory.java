package eu.gloria.gs.services.experiment.base.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.gloria.gs.services.experiment.ExperimentException;
import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.ExperimentInformation;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.data.OperationInformation;
import eu.gloria.gs.services.experiment.base.data.ParameterInformation;
import eu.gloria.gs.services.experiment.base.models.DuplicateExperimentException;
import eu.gloria.gs.services.experiment.base.models.InvalidExperimentModelException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationFactory;
import eu.gloria.gs.services.experiment.base.operations.NoSuchOperationException;
import eu.gloria.gs.services.experiment.base.operations.OperationTypeNotAvailableException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterFactory;
import eu.gloria.gs.services.experiment.base.parameters.NoSuchParameterException;
import eu.gloria.gs.services.experiment.base.parameters.ParameterTypeNotAvailableException;

public class ExperimentModelFactory {

	private ExperimentDBAdapter adapter;
	private ExperimentParameterFactory parameterFactory;
	private ExperimentOperationFactory operationFactory;

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

	public CustomExperimentModel loadCustomExperiment(String experiment)
			throws NoSuchExperimentException, InvalidExperimentModelException,
			ExperimentException {
		CustomExperimentModel model = null;

		ExperimentInformation expInfo = null;
		expInfo = adapter.getExperimentInformation(experiment);

		model = new CustomExperimentModel();
		model.setAdapter(this.adapter);
		model.setName(experiment);
		model.setParameterFactory(this.parameterFactory);
		model.setOperationFactory(this.operationFactory);

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
					throw new InvalidExperimentModelException(e.getAction());
				}

				try {
					model.addParameter(parameterInfo.getName(), expParameter,
							parameterInfo.getArguments());
				} catch (ExperimentParameterException e) {
					throw new InvalidExperimentModelException(e.getAction());
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
				throw new InvalidExperimentModelException(e.getAction());
			}

			Map<Integer, ExperimentOperation> operationDependencies = expParameter
					.getOperationDependencies();

			String[] paramArgs = (String[]) parameterInfo.getArguments();

			for (Integer order : operationDependencies.keySet()) {

				ExperimentOperation depOperation = operationDependencies
						.get(order);

				String opName = paramArgs[order];

				OperationInformation actualOperationInfo = expInfo
						.getOperation(opName);

				if (depOperation == null) {
					InvalidExperimentModelException ex = new InvalidExperimentModelException(
							parameterInfo.getName(), "not well defined");
					ex.getAction().put("op-dependent", true);
					throw ex;
				}

				try {
					model.addOperation(actualOperationInfo.getName(),
							actualOperationInfo.getOperation(),
							actualOperationInfo.getArguments());
					preCreatedOperations.add(actualOperationInfo);
				} catch (ExperimentOperationException e) {
					throw new InvalidExperimentModelException(e.getAction());
				}

			}

			try {
				model.addParameter(parameterInfo.getName(), expParameter,
						parameterInfo.getArguments());
			} catch (ExperimentParameterException e) {
				throw new InvalidExperimentModelException(e.getAction());
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
					throw new InvalidExperimentModelException(e.getAction());

				}

				try {
					model.addOperation(operation.getName(), expOperation,
							operation.getArguments());
				} catch (ExperimentOperationException e) {
					throw new InvalidExperimentModelException(e.getAction());
				}
			}
		}

		preCreatedOperations.clear();
		pointers.clear();

		return model;
	}

	public void createCustomExperiment(String experiment, String author,
			String type) throws DuplicateExperimentException,
			ExperimentException {
		if (adapter.containsExperiment(experiment)) {
			throw new DuplicateExperimentException(experiment);
		}
		adapter.createExperiment(experiment, author, type);
	}

	public Map<String, ExperimentParameter> getAllExperimentParameters() {
		return this.parameterFactory.getAllParameters();
	}

	public Map<String, ExperimentOperation> getAllExperimentOperations() {
		return this.operationFactory.getAllOperations();
	}

	public ExperimentParameter getExperimentParameter(String name)
			throws NoSuchParameterException {
		return this.parameterFactory.getParameter(name);

	}

	public ExperimentOperation getExperimentOperation(String name)
			throws NoSuchOperationException {
		return this.operationFactory.getOperation(name);

	}
}
