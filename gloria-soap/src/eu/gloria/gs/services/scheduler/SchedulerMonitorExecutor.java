package eu.gloria.gs.services.scheduler;

import java.util.Date;
import eu.gloria.gs.services.core.ErrorLogEntry;
import eu.gloria.gs.services.core.InfoLogEntry;
import eu.gloria.gs.services.core.LogEntry;
import eu.gloria.gs.services.core.LogStore;
import eu.gloria.gs.services.core.WarningLogEntry;
import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.core.tasks.ServerThread;
import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.repository.rt.RTRepositoryException;
import eu.gloria.gs.services.scheduler.brain.SchedulerBrain;
import eu.gloria.gs.services.scheduler.data.SchedulerDatabaseException;

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
		} catch (SchedulerDatabaseException e) {
			log.error(e.getMessage());
		} catch (RTRepositoryException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private void processLogEntry(LogEntry entry, LogAction action) {
		entry.setUsername(this.username);
		entry.setDate(new Date());

		entry.setAction(action);
		this.logStore.addEntry(entry);
	}

	private void logError(LogAction action) {

		LogEntry entry = new ErrorLogEntry();
		this.processLogEntry(entry, action);
	}

	private void logInfo(LogAction action) {

		LogEntry entry = new InfoLogEntry();
		this.processLogEntry(entry, action);
	}

	private void logWarning(LogAction action) {

		LogEntry entry = new WarningLogEntry();
		this.processLogEntry(entry, action);
	}
}
