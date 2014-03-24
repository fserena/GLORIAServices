package eu.gloria.gs.services.experiment;

import org.springframework.context.ApplicationContext;

import eu.gloria.gs.services.core.tasks.ServerTask;
import eu.gloria.gs.services.core.tasks.ServerThread;

public class RTScriptExecutorTask extends ServerTask {

	public RTScriptExecutorTask() {
		super(RTScriptExecutorTask.class.getSimpleName());
	}

	@Override
	protected ServerThread createServerThread(ApplicationContext context) {

		log.info("Online");
		return (ServerThread) context.getBean("rtScriptExecutor");
	}

}
