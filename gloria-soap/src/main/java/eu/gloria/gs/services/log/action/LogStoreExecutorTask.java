package eu.gloria.gs.services.log.action;

import org.springframework.context.ApplicationContext;

import eu.gloria.gs.services.core.tasks.ServerTask;
import eu.gloria.gs.services.core.tasks.ServerThread;

public class LogStoreExecutorTask extends ServerTask {

	public LogStoreExecutorTask() {
		super(LogStoreExecutorTask.class.getSimpleName());
	}

	@Override
	protected ServerThread createServerThread(ApplicationContext context) {

		log.info("Online");
		return (ServerThread) context.getBean("logStoreExecutor");
	}

}
