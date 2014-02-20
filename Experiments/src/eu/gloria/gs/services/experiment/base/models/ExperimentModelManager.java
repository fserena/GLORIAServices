package eu.gloria.gs.services.experiment.base.models;

import java.util.Map;

import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.models.DuplicateExperimentException;
import eu.gloria.gs.services.experiment.base.models.ExperimentFeature;
import eu.gloria.gs.services.experiment.base.models.InvalidExperimentModelException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;
import eu.gloria.gs.services.experiment.base.operations.NoSuchOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;
import eu.gloria.gs.services.experiment.base.parameters.NoSuchParameterException;

public class ExperimentModelManager {

	private ExperimentModelFactory factory;
	private ExperimentDBAdapter adapter;

	public ExperimentModelManager() {
	}

	public void setModelFactory(ExperimentModelFactory factory) {
		this.factory = factory;
	}

	public void setAdapter(ExperimentDBAdapter adapter) {
		this.adapter = adapter;
	}

	public CustomExperimentModel getModel(String experiment)
			throws NoSuchExperimentException, ExperimentDatabaseException {
		CustomExperimentModel model = null;

		if (adapter.containsExperiment(experiment)) {
			try {
				model = factory.loadCustomExperiment(experiment);
			} catch (InvalidExperimentModelException e) {
				throw new ExperimentDatabaseException(e.getMessage());
			}
		}

		return model;
	}

	public void createModel(String experiment, String author, String type)
			throws DuplicateExperimentException, ExperimentDatabaseException {

		if (!adapter.containsExperiment(experiment)) {

			factory.createCustomExperiment(experiment, author, type);

			try {
				factory.loadCustomExperiment(experiment); // Verification load
			} catch (InvalidExperimentModelException e) {
				throw new DuplicateExperimentException(e.getMessage());
			} catch (NoSuchExperimentException e) {
				throw new ExperimentDatabaseException(e.getMessage());
			}

		} else {
			throw new DuplicateExperimentException("Experiment: " + experiment);
		}
	}

	public void deleteModel(String experiment)
			throws NoSuchExperimentException, ExperimentDatabaseException {
		adapter.deleteExperiment(experiment);
	}

	public void deleteParameter(String experiment, String parameter)
			throws NoSuchExperimentException, ExperimentDatabaseException {
		adapter.removeExperimentParameter(experiment, parameter);
	}

	public void deleteOperation(String experiment, String operation)
			throws NoSuchExperimentException, ExperimentDatabaseException {
		adapter.removeExperimentOperation(experiment, operation);
	}

	public Map<String, ExperimentParameter> getAllExperimentParameters() {
		return factory.getAllExperimentParameters();
	}

	public Map<String, ExperimentOperation> getAllExperimentOperations() {
		return factory.getAllExperimentOperations();
	}

	public ExperimentParameter getExperimentParameter(String name)
			throws NoSuchParameterException {
		return factory.getExperimentParameter(name);
	}

	public ExperimentOperation getExperimentOperation(String name)
			throws NoSuchOperationException {
		return factory.getExperimentOperation(name);
	}
}
