package eu.gloria.gs.services.experiment;

import org.springframework.context.ApplicationContext;

import eu.gloria.gs.services.core.tasks.ServerTask;
import eu.gloria.gs.services.core.tasks.ServerThread;

public class ExperimentExecutorTask extends ServerTask {

	
	public ExperimentExecutorTask() {
		super(ExperimentExecutorTask.class.getSimpleName());
	}

	@Override
	protected ServerThread createServerThread(ApplicationContext context) {

		log.info("Online");
		return (ServerThread) context.getBean("experimentExecutor");
	}

}
