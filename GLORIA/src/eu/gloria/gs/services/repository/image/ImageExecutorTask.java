package eu.gloria.gs.services.repository.image;

import org.springframework.context.ApplicationContext;

import eu.gloria.gs.services.core.tasks.ServerTask;
import eu.gloria.gs.services.core.tasks.ServerThread;

public class ImageExecutorTask extends ServerTask {

	@Override
	protected ServerThread createServerThread(ApplicationContext context) {

		System.out.println("Image URL Retriever online!");
		return (ServerThread) context.getBean("imageURLRetrieveExecutor");
	}

}
