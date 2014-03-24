package eu.gloria.gs.services.core.tasks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import eu.gloria.gs.services.utils.LoggerEntity;

public abstract class ServerTask extends LoggerEntity implements
		ServletContextListener {

	public ServerTask(String name) {
		super(name);
	}

	protected static ExecutorService executor = Executors.newCachedThreadPool();

	protected abstract ServerThread createServerThread(
			ApplicationContext context);

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		ApplicationContext cxt = WebApplicationContextUtils
				.getWebApplicationContext(sce.getServletContext());

		try {

			ServerThread myThread = this.createServerThread(cxt);
			log.debug("Server thread created");
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