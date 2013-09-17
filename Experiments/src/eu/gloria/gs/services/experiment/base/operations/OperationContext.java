package eu.gloria.gs.services.experiment.base.operations;

import java.util.Map;

import eu.gloria.gs.services.experiment.base.contexts.Context;
import eu.gloria.gs.services.experiment.base.contexts.ExperimentContext;
import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.operations.OperationContextService;

public class OperationContext extends Context {

	private ExperimentOperation operation = null;
	private ExperimentDBAdapter adapter;
	private String name;
	private ExperimentContext context;
	private String[] contextArguments;
	private OperationContextService contextService;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setExperimentOperation(ExperimentOperation operation) {
		this.operation = operation;
	}

	public ExperimentOperation getExperimentOperation() {
		return this.operation;
	}

	public void setExperimentContext(ExperimentContext context) {
		this.context = context;
	}

	public ExperimentContext getExperimentContext() {
		return this.context;
	}

	public void setAdapter(ExperimentDBAdapter adapter) {
		this.adapter = adapter;
	}

	public ExperimentDBAdapter getAdapter() {
		return this.adapter;
	}

	public void execute() throws ExperimentOperationException {
		this.manageOperationsExecution(this.operation.getBehaviour());
		
	}

	@Override
	public void instantiate() {

	}

	public void setContextArguments(String[] arguments) {
		this.contextArguments = arguments;
	}

	public String[] getContextArguments() {
		return this.contextArguments;
	}

	private void manageOperationsExecution(Map<String, String[]> list)
			throws ExperimentOperationException {
		if (list != null) {

			for (String operation : list.keySet()) {

				String[] operationArguments = list.get(operation);

				int order = 0;

				Object[] serviceOperationArguments = new Object[operationArguments
						.length];

				for (String argument : operationArguments) {

					if (argument.startsWith("arg")) {

						int contextArgOrder = Integer
								.parseInt(argument.replaceFirst(
										"arg", ""));

						String contextArgument = contextArguments[contextArgOrder];
						Object serviceArgument = null;

						serviceArgument = contextArgument;

						serviceOperationArguments[order] = serviceArgument;
					}

					order++;
				}

				contextService.treatOperation(this, operation,
						serviceOperationArguments);
			}
		}
	}

	public OperationContextService getContextService() {
		return contextService;
	}

	public void setContextService(OperationContextService contextService) {
		this.contextService = contextService;
	}

}
