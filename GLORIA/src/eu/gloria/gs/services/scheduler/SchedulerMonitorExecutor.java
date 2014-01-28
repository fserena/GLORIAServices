package eu.gloria.gs.services.scheduler;

import java.util.Date;
import java.util.Map;

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
import eu.gloria.gs.services.scheduler.data.SchedulerAdapter;
import eu.gloria.gs.services.scheduler.data.SchedulerDatabaseException;

public class SchedulerMonitorExecutor extends ServerThread {

	private SchedulerAdapter adapter;
	private LogStore logStore;
	private String username;
	private String password;
	private SchedulerBrain brain;
	private boolean thereArePending;
	private Map<Integer, Integer> recoverRetries = null;

	public void setAdapter(SchedulerAdapter adapter) {
		this.adapter = adapter;
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

		GSClientProvider.setCredentials(this.username, this.password);

		try {
			this.brain.refreshPlans();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SchedulerDatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RTRepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
