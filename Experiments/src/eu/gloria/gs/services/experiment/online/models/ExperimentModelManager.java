package eu.gloria.gs.services.experiment.online.models;

import java.util.Map;

import eu.gloria.gs.services.experiment.online.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.online.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.online.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.online.operations.ExperimentOperation;
import eu.gloria.gs.services.experiment.online.parameters.ExperimentParameter;

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
			throws InvalidExperimentModelException, NoSuchExperimentException,
			ExperimentDatabaseException {
		CustomExperimentModel model = null;

		if (adapter.containsExperiment(experiment)) {
			model = factory.loadCustomExperiment(experiment);
		}

		return model;
	}

	public void createModel(String experiment, String author)
			throws DuplicateExperimentException, ExperimentDatabaseException {

		if (!adapter.containsExperiment(experiment)) {

			factory.createCustomExperiment(experiment, author);

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

	public void removeModel(String experiment)
			throws NoSuchExperimentException, ExperimentDatabaseException {
		adapter.deleteExperiment(experiment);
	}

	public Map<String, ExperimentParameter> getAllExperimentParameters() {
		return factory.getAllExperimentParameters();
	}

	public Map<String, ExperimentOperation> getAllExperimentOperations() {
		return factory.getAllExperimentOperations();
	}
	
	public Map<String, ExperimentFeature> getAllExperimentFeatures() {
		return factory.getAllExperimentFeatures();
	}

	public ExperimentParameter getExperimentParameter(String name) {
		return factory.getExperimentParameter(name);
	}
	
	public ExperimentOperation getExperimentOperation(String name) {
		return factory.getExperimentOperation(name);
	}
	
	public ExperimentFeature getExperimentFeature(String name) {
		return factory.getExperimentFeature(name);
	}
}
