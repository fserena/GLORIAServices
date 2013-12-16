package eu.gloria.gs.services.log.action;

import org.springframework.context.ApplicationContext;

import eu.gloria.gs.services.core.tasks.ServerTask;
import eu.gloria.gs.services.core.tasks.ServerThread;

public class LogStoreExecutorTask extends ServerTask {

	@Override
	protected ServerThread createServerThread(ApplicationContext context) {

		System.out.println("Log store manager online!");
		return (ServerThread) context.getBean("logStoreExecutor");
	}

}
