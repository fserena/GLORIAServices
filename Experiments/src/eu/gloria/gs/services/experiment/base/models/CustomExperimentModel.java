package eu.gloria.gs.services.experiment.base.models;


import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.data.OperationInformation;
import eu.gloria.gs.services.experiment.base.data.ParameterInformation;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationFactory;
import eu.gloria.gs.services.experiment.base.operations.OperationTypeNotAvailableException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterFactory;
import eu.gloria.gs.services.experiment.base.parameters.NoSuchParameterException;
import eu.gloria.gs.services.experiment.base.parameters.ParameterTypeNotAvailableException;

public class CustomExperimentModel extends ExperimentModel {

	private ExperimentDBAdapter adapter;
	private ExperimentParameterFactory parameterFactory;
	private ExperimentOperationFactory operationFactory;

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

	public void buildOperation(OperationInformation operationInfo)
			throws OperationTypeNotAvailableException,
			ExperimentOperationException, ExperimentDatabaseException {

		ExperimentOperation operation = null;

		operation = operationFactory.createOperation(operationInfo.getType());

		this.addOperation(operationInfo.getName(), operation,
				operationInfo.getArguments());

		try {
			adapter.addExperimentOperation(this.getName(), operationInfo);
		} catch (NoSuchExperimentException e) {
			throw new ExperimentDatabaseException(e.getMessage());
		}

		try {
			adapter.setOperationArguments(this.getName(),
					operationInfo.getName(), operationInfo.getArguments());
		} catch (NoSuchExperimentException e) {
			ExperimentDatabaseException ex = new ExperimentDatabaseException();
			ex.getAction().put("experiment not found", e.getAction());
			throw ex;
		} catch (NoSuchParameterException e) {
			ExperimentDatabaseException ex = new ExperimentDatabaseException();
			ex.getAction().put("parameter not found", e.getAction());
			throw ex;
		}
	}

	public void buildParameter(ParameterInformation parameterInfo)
			throws ParameterTypeNotAvailableException,
			ExperimentParameterException, ExperimentDatabaseException {

		ExperimentParameter parameter = null;

		parameter = parameterFactory.createParameter(parameterInfo.getType());

		parameterInfo.setParameter(parameter);

		this.addParameter(parameterInfo.getName(), parameter,
				parameterInfo.getArguments());

		try {
			adapter.addExperimentParameter(this.getName(), parameterInfo);
		} catch (NoSuchExperimentException e) {
			ExperimentDatabaseException ex = new ExperimentDatabaseException();
			ex.getAction().put("experiment not found", e.getAction());
			throw ex;
		}
	}
}
