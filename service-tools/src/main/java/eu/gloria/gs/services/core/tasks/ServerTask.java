package eu.gloria.gs.services.core.tasks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

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
	private static Object sync = new Object();
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
			if (thread.isAlive()) {
				thread.join(5000);
			}

			synchronized (sync) {
				if (!destroyed) {
					destroyed = true;
					executor.shutdown();
					log.info("Server task thread pool shutdown");

				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}