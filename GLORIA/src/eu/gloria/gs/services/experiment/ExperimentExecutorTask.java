package eu.gloria.gs.services.experiment.online;

import org.springframework.context.ApplicationContext;

import eu.gloria.gs.services.core.tasks.ServerTask;
import eu.gloria.gs.services.core.tasks.ServerThread;

public class ExperimentExecutorTask extends ServerTask {

	@Override
	protected ServerThread createServerThread(ApplicationContext context) {

		System.out.println("Experiment context manager online!");
		return (ServerThread) context.getBean("experimentExecutor");
	}

}
