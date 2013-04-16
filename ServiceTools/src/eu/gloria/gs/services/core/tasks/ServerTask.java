package eu.gloria.gs.services.core.tasks;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public abstract class ServerTask implements ServletContextListener {

	protected ServerThread myThread = null;

	protected abstract ServerThread createServerThread(ApplicationContext context);

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		ApplicationContext cxt = WebApplicationContextUtils
				.getWebApplicationContext(sce.getServletContext());
		
		if ((myThread == null) || (!myThread.isAlive())) {
			myThread = this.createServerThread(cxt);
			System.out.println("Initializing thread!");
			myThread.start();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			myThread.doShutdown();
			myThread.interrupt();
			System.out.println("Destroying thread!");
		} catch (Exception ex) {
		}
	}
}