package eu.gloria.gs.services.scheduler;

import org.springframework.context.ApplicationContext;

import eu.gloria.gs.services.core.tasks.ServerTask;
import eu.gloria.gs.services.core.tasks.ServerThread;

public class SchedulerExecutorTask extends ServerTask {

	@Override
	protected ServerThread createServerThread(ApplicationContext context) {

		System.out.println("Scheduler Monitor online!");
		return (ServerThread) context.getBean("schedulerMonitorExecutor");
	}

}
