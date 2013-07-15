package eu.gloria.gs.services.experiment.base.contexts;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.base.data.ExperimentInformation;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.data.OperationInformation;
import eu.gloria.gs.services.experiment.base.data.ParameterInformation;
import eu.gloria.gs.services.experiment.base.data.ReservationInformation;
import eu.gloria.gs.services.experiment.base.models.InvalidExperimentModelException;
import eu.gloria.gs.services.experiment.base.models.InvalidUserContextException;
import eu.gloria.gs.services.experiment.base.operations.OperationContext;
import eu.gloria.gs.services.experiment.base.parameters.ParameterContext;
import eu.gloria.gs.services.experiment.base.reservation.NoSuchReservationException;

public class ExperimentContextFactory implements ApplicationContextAware {

	private ExperimentDBAdapter adapter;
	private ApplicationContext applicationContext;

	public void setAdapter(ExperimentDBAdapter adapter) {
		this.adapter = adapter;
	}

	public ExperimentContext createExperimentContext(String username, int rid)
			throws InvalidUserContextException, ExperimentDatabaseException,
			NoSuchReservationException, InvalidExperimentModelException,
			NoSuchExperimentException {

		ExperimentContext context = null;

		ReservationInformation resInfo = null;
		try {
			resInfo = adapter.getReservationInformation(rid);
		} catch (ExperimentDatabaseException e) {
			throw e;
		} catch (NoSuchReservationException e) {
			throw e;
		}

		if (!resInfo.getUser().equals(username)) {
			throw new InvalidUserContextException("Username : " + username);
		}

		ExperimentInformation experimentInfo = this.adapter
				.getExperimentInformation(resInfo.getExperiment());

		context = new ExperimentContext();
		context.setExperimentName(resInfo.getExperiment());
		context.setReservation(rid);

		for (ParameterInformation paramInfo : experimentInfo.getParameters()) {
			ParameterContext parameterContext = null;

			parameterContext = this.createParameterContext(paramInfo, context);
			context.addParameter(paramInfo.getModelName(), parameterContext);
		}

		for (OperationInformation opInfo : experimentInfo.getOperations()) {
			OperationContext operationContext = null;

			operationContext = this.createOperationContext(opInfo, context);
			context.addOperation(opInfo.getModelName(), operationContext);

		}

		return context;
	}

	public ParameterContext createParameterContext(
			ParameterInformation paramInfo, ExperimentContext context) {
		ParameterContext parameterContext = null;

		parameterContext = (ParameterContext) applicationContext
				.getBean("PARAMETER_CONTEXT");

		parameterContext.setExperimentParameter(paramInfo.getParameter());
		parameterContext.setExperimentContext(context);
		parameterContext.setName(paramInfo.getModelName());
		parameterContext.setContextArguments(paramInfo.getArguments());

		return parameterContext;
	}

	public OperationContext createOperationContext(OperationInformation opInfo,
			ExperimentContext context) {

		OperationContext operationContext = null;

		operationContext = (OperationContext) applicationContext
				.getBean("OPERATION_CONTEXT");

		operationContext.setExperimentOperation(opInfo.getOperation());
		operationContext.setExperimentContext(context);
		operationContext.setName(opInfo.getModelName());
		operationContext.setContextArguments(opInfo.getArguments());

		return operationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {

		this.applicationContext = applicationContext;

	}
}
