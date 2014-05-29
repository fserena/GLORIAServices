package eu.gloria.gs.services.repository.image;

import org.springframework.context.ApplicationContext;

import eu.gloria.gs.services.core.tasks.ServerTask;
import eu.gloria.gs.services.core.tasks.ServerThread;

public class ImageExecutorTask extends ServerTask {

	public ImageExecutorTask() {
		super(ImageExecutorTask.class.getSimpleName());
	}

	@Override
	protected ServerThread createServerThread(ApplicationContext context) {

		log.info("Online");
		return (ServerThread) context.getBean("imageURLRetrieveExecutor");
	}

}
