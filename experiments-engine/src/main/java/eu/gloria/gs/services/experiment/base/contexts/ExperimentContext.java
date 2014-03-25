package eu.gloria.gs.services.experiment.base.contexts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import eu.gloria.gs.services.experiment.ExperimentException;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.operations.NoSuchOperationException;
import eu.gloria.gs.services.experiment.base.parameters.NoSuchParameterException;
import eu.gloria.gs.services.experiment.base.operations.OperationContext;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.ParameterContext;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;

public class ExperimentContext extends Context {

	private int rid = -1;
	private HashMap<String, ParameterContext> parameterContexts;
	private HashMap<String, OperationContext> operationContexts;
	private String experiment;

	public ExperimentContext() {
		parameterContexts = new HashMap<>();
		operationContexts = new HashMap<>();
	}

	public Set<String> getParameterNames() {
		return parameterContexts.keySet();
	}

	public Set<String> getOperationNames() {
		return operationContexts.keySet();
	}

	public void setExperimentName(String experiment) {
		this.experiment = experiment;
	}

	public String getExperimentName() {
		return this.experiment;
	}

	public void setReservation(int rid) {
		this.rid = rid;
	}

	public int getReservation() {
		return this.rid;
	}

	public void addParameter(String parameterName,
			ParameterContext parameterContext) {

		if (!parameterContexts.containsKey(parameterName)) {
			parameterContexts.put(parameterName, parameterContext);
		} else {

			// TODO: throw SOMETHING!
		}

	}

	public void addOperation(String operationName,
			OperationContext operationContext) {

		if (!operationContexts.containsKey(operationName)) {
			operationContexts.put(operationName, operationContext);
		} else {
			// TODO: throw SOMETHING!
		}

	}

	@Override
	public void instantiate() throws ExperimentParameterException,
			NoSuchExperimentException {
		int requiredInstances = this.parameterContexts.size();

		int instancesDone = 0;
		int currentIndex = 0;

		String[] parameterNames = this.parameterContexts.keySet().toArray(
				new String[0]);

		List<String> parametersInstantiated = new ArrayList<>();
		List<String> parametersInitialized = new ArrayList<>();

		while (instancesDone < requiredInstances) {

			ParameterContext parameterContext = this.parameterContexts
					.get(parameterNames[currentIndex]);
			try {

				if (!parametersInstantiated
						.contains(parameterNames[currentIndex])) {
					parameterContext.instantiate();
					parametersInstantiated.add(parameterNames[currentIndex]);
				}

				if (!parametersInitialized
						.contains(parameterNames[currentIndex])) {
					parameterContext.init();
					parametersInitialized.add(parameterNames[currentIndex]);

					instancesDone++;
				}

				if (currentIndex == requiredInstances - 1)
					currentIndex = 0;
				else
					currentIndex++;

			} catch (ContextNotReadyException e) {
				String requiredParameter = e.getMessage();

				int requiredIndex = 0;
				for (String parameterName : parameterNames) {
					if (parameterName.equals(requiredParameter)) {
						currentIndex = requiredIndex;
						break;
					}

					requiredIndex++;
				}
			}
		}
	}

	public void init() throws ExperimentOperationException {

		OperationContext initContext;
		try {
			initContext = this.getOperation("init");

			initContext.execute();
		} catch (NoSuchOperationException e) {
		}
	}

	public void end() throws ExperimentOperationException {

		OperationContext endContext;
		try {
			endContext = this.getOperation("end");

			endContext.execute();
		} catch (NoSuchOperationException e) {
		}
	}

	public Object getParameterValue(String parameterName)
			throws ExperimentParameterException,
			ExperimentNotInstantiatedException, NoSuchParameterException {

		String[] parameterNodes = parameterName.split("\\.");

		if (this.parameterContexts.containsKey(parameterNodes[0])) {

			if (parameterNodes.length > 0) {

				ParameterContext parameterContext = this.parameterContexts
						.get(parameterNodes[0]);

				return parameterContext.getValue(parameterNodes);
			}
		}

		throw new NoSuchParameterException(parameterName);
	}

	public void setParameterValue(String parameterName, Object value)
			throws ExperimentParameterException,
			ExperimentNotInstantiatedException, NoSuchParameterException {

		String[] parameterNodes = parameterName.split("\\.");

		if (this.parameterContexts.containsKey(parameterNodes[0])) {
			ParameterContext parameterContext = this.parameterContexts
					.get(parameterNodes[0]);

			parameterContext.setValue(parameterNodes, value);
		} else {
			throw new NoSuchParameterException(parameterName);
		}
	}

	public ParameterContext getParameterContext(String parameter)
			throws NoSuchParameterException {
		if (this.parameterContexts.containsKey(parameter)) {
			return this.parameterContexts.get(parameter);
		} else {
			throw new NoSuchParameterException(parameter);
		}
	}

	public OperationContext getOperation(String operation)
			throws NoSuchOperationException {
		if (this.operationContexts.containsKey(operation)) {
			return this.operationContexts.get(operation);
		} else {
			throw new NoSuchOperationException(operation);
		}
	}

	public void executeOperation(String name)
			throws ExperimentOperationException, ExperimentException,
			NoSuchOperationException, ExperimentNotInstantiatedException {

		OperationContext operationContext = this.getOperation(name);

		operationContext.execute();
	}
}
