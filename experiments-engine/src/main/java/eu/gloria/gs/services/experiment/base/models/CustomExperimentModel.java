package eu.gloria.gs.services.experiment.base.models;

import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.OperationInformation;
import eu.gloria.gs.services.experiment.base.data.ParameterInformation;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationFactory;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameter;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterFactory;
import eu.gloria.gs.services.log.action.ActionException;

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
			throws ActionException {

		ExperimentOperation operation = null;

		operation = operationFactory.createOperation(operationInfo.getType());

		this.addOperation(operationInfo.getName(), operation,
				operationInfo.getArguments());

		adapter.addExperimentOperation(this.getName(), operationInfo);

		adapter.setOperationArguments(this.getName(), operationInfo.getName(),
				operationInfo.getArguments());
	}

	public void buildParameter(ParameterInformation parameterInfo)
			throws ActionException {

		ExperimentParameter parameter = null;

		parameter = parameterFactory.createParameter(parameterInfo.getType());

		parameterInfo.setParameter(parameter);

		this.addParameter(parameterInfo.getName(), parameter,
				parameterInfo.getArguments());
		adapter.addExperimentParameter(this.getName(), parameterInfo);
	}
}
