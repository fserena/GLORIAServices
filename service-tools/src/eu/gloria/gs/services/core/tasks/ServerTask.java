package eu.gloria.gs.services.core.tasks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public abstract class ServerTask implements ServletContextListener {

	protected static ExecutorService executor = Executors.newCachedThreadPool();

	protected abstract ServerThread createServerThread(
			ApplicationContext context);

	private static Logger log = LoggerFactory.getLogger(ServerTask.class
			.getSimpleName());

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		ApplicationContext cxt = WebApplicationContextUtils
				.getWebApplicationContext(sce.getServletContext());

		try {

			ServerThread myThread = this.createServerThread(cxt);
			log.debug("Server thread created: "
					+ myThread.getClass().getSimpleName());
			executor.submit(myThread);
		} catch (RejectedExecutionException e) {
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			executor.shutdownNow();
			log.debug("Server task thread pool shutdown");
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}
}