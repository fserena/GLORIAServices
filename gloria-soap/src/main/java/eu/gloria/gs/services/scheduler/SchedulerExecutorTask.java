package eu.gloria.gs.services.scheduler;

import org.springframework.context.ApplicationContext;

import eu.gloria.gs.services.core.tasks.ServerTask;
import eu.gloria.gs.services.core.tasks.ServerThread;

public class SchedulerExecutorTask extends ServerTask {

	public SchedulerExecutorTask() {
		super(SchedulerExecutorTask.class.getSimpleName());
	}

	@Override
	protected ServerThread createServerThread(ApplicationContext context) {

		log.info("Online");
		return (ServerThread) context.getBean("schedulerMonitorExecutor");
	}

}
