package eu.gloria.gs.services.core.tasks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public abstract class ServerTask implements ServletContextListener {

	protected ServerThread myThread = null;
	protected static ExecutorService executor = Executors
			.newFixedThreadPool(10);

	protected abstract ServerThread createServerThread(
			ApplicationContext context);

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		ApplicationContext cxt = WebApplicationContextUtils
				.getWebApplicationContext(sce.getServletContext());

		try {

			myThread = this.createServerThread(cxt);

			executor.submit(myThread);
		} catch (RejectedExecutionException e) {
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			myThread.end();
		} catch (Exception ex) {
		}
	}
}