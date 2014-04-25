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
	private static boolean destroyed = false;
	private ServerThread thread = null;

	protected abstract ServerThread createServerThread(
			ApplicationContext context);

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		ApplicationContext cxt = WebApplicationContextUtils
				.getWebApplicationContext(sce.getServletContext());

		try {

			this.thread = this.createServerThread(cxt);
			log.debug("Server thread created");
			executor.submit(this.thread);
		} catch (RejectedExecutionException e) {
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

		if (thread != null)
			this.thread.end();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}

		try {
			synchronized (executor) {
				if (!destroyed) {
					executor.shutdown();
					log.info("Server task thread pool shutdown");
					destroyed = true;
				}
			}			
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}