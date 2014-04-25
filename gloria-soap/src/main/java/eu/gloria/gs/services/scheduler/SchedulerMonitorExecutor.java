package eu.gloria.gs.services.scheduler;

import eu.gloria.gs.services.core.LogStore;
import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.core.tasks.ServerThread;
import eu.gloria.gs.services.repository.rt.RTRepositoryException;
import eu.gloria.gs.services.scheduler.brain.SchedulerBrain;

public class SchedulerMonitorExecutor extends ServerThread {

	private LogStore logStore;
	private String username;
	private String password;
	private SchedulerBrain brain;

	/**
	 * @param name
	 */
	public SchedulerMonitorExecutor() {
		super(SchedulerMonitorExecutor.class.getSimpleName());
	}

	public void setLogStore(LogStore logStore) {
		this.logStore = logStore;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setBrain(SchedulerBrain brain) {
		this.brain = brain;
	}

	@Override
	public void end() {
		super.end();
		// GSClientProvider.clearCredentials();
	}

	@Override
	protected void doWork() {

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			log.warn(e.getMessage());
		}

		GSClientProvider.setCredentials(this.username, this.password);

		try {
			this.brain.refreshPlans();
		} catch (SchedulerException e) {
			log.error(e.getMessage());
		} catch (RTRepositoryException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
