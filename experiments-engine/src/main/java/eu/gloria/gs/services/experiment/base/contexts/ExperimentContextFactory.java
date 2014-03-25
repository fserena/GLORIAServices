package eu.gloria.gs.services.experiment.base.contexts;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import eu.gloria.gs.services.experiment.ExperimentException;
import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.ExperimentInformation;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.data.OperationInformation;
import eu.gloria.gs.services.experiment.base.data.ParameterInformation;
import eu.gloria.gs.services.experiment.base.data.ReservationInformation;
import eu.gloria.gs.services.experiment.base.models.InvalidUserContextException;
import eu.gloria.gs.services.experiment.base.operations.OperationContext;
import eu.gloria.gs.services.experiment.base.parameters.ParameterContext;
import eu.gloria.gs.services.experiment.base.reservation.NoSuchReservationException;
import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.utils.LoggerEntity;

public class ExperimentContextFactory extends LoggerEntity implements
		ApplicationContextAware {

	private ExperimentDBAdapter adapter;
	private ApplicationContext applicationContext;

	public ExperimentContextFactory() {
		super(ExperimentContextFactory.class.getSimpleName());
	}

	public void setAdapter(ExperimentDBAdapter adapter) {
		this.adapter = adapter;
	}

	public ExperimentContext createExperimentContext(String username, int rid)
			throws InvalidUserContextException, ExperimentException,
			NoSuchReservationException {

		ExperimentContext context = null;

		ReservationInformation resInfo = null;
		try {
			resInfo = adapter.getReservationInformation(rid);
		} catch (ActionException e) {
			throw e;
		}

		if (!resInfo.getUser().equals(username)) {
			throw new InvalidUserContextException(username, rid);
		}

		ExperimentInformation experimentInfo = null;
		try {
			experimentInfo = this.adapter.getExperimentInformation(resInfo
					.getExperiment());
		} catch (NoSuchExperimentException e) {
			throw new ExperimentException(e.getMessage());
		}

		context = new ExperimentContext();
		context.setExperimentName(resInfo.getExperiment());
		context.setReservation(rid);

		for (ParameterInformation paramInfo : experimentInfo.getParameters()) {
			ParameterContext parameterContext = null;

			parameterContext = this.createParameterContext(paramInfo, context);
			context.addParameter(paramInfo.getName(), parameterContext);
		}

		for (OperationInformation opInfo : experimentInfo.getOperations()) {
			OperationContext operationContext = null;

			operationContext = this.createOperationContext(opInfo, context);
			context.addOperation(opInfo.getName(), operationContext);

		}

		log.info("context created for reservation id " + rid + ", user "
				+ username + " and " + experimentInfo.getName() + " experiment");

		return context;
	}

	public ParameterContext createParameterContext(
			ParameterInformation paramInfo, ExperimentContext context) {
		ParameterContext parameterContext = null;

		parameterContext = (ParameterContext) applicationContext
				.getBean(paramInfo.getParameter().getContextBeanName());

		parameterContext.setExperimentParameter(paramInfo.getParameter());
		parameterContext.setExperimentContext(context);
		parameterContext.setName(paramInfo.getName());
		parameterContext.setContextArguments(paramInfo.getArguments());

		return parameterContext;
	}

	public OperationContext createOperationContext(OperationInformation opInfo,
			ExperimentContext context) {

		OperationContext operationContext = null;

		// operationContext = opInfo.getOperation().getContext();

		operationContext = (OperationContext) applicationContext.getBean(opInfo
				.getOperation().getContextBeanName());

		operationContext.setExperimentOperation(opInfo.getOperation());
		operationContext.setExperimentContext(context);
		operationContext.setName(opInfo.getName());
		operationContext.setContextArguments(opInfo.getArguments());

		return operationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {

		this.applicationContext = applicationContext;

	}
}
