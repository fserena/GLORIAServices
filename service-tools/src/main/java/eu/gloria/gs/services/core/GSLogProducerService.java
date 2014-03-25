/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.core;

import java.io.IOException;
import java.util.Date;

import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogType;
import eu.gloria.gs.services.utils.JSONConverter;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * @param <T>
 * 
 */
public abstract class GSLogProducerService extends GSWebService {

	private LogStore logStore;

	protected GSLogProducerService(String name) {
		super(name);
	}

	public void setLogStore(LogStore logStore) {
		log.debug("Log store set up");
		this.logStore = logStore;

	}

	public LogStore getLogStore() {
		return this.logStore;
	}

	protected void logException(Action action, ActionException e) {
		action.child("exception", e.getAction());
		e.setAction(action);
		this.logError(getClientUsername(), action);
	}

	protected void logException(Action action, Exception e) {
		ActionException exception = new ActionException(e.getMessage());
		this.logException(action, exception);
	}

	private void fillAction(Action action) {
		action.put("sender", this.getUsername());
		action.put("client", this.getClientUsername());
	}

	protected void logError(Action action) {
		this.logUserAction(LogType.ERROR, this.getClientUsername(), action);
	}

	protected void logError(String username, Action action) {
		this.logUserAction(LogType.ERROR, username, action);
	}

	protected void logError(String rt, String username, Action action) {
		this.logRTAction(LogType.ERROR, username, rt, action);
	}

	protected void logError(int rid, String username, Action action) {
		this.logContextAction(LogType.ERROR, username, rid, action);
	}

	protected void logWarning(Action action) {
		this.logUserAction(LogType.WARNING, this.getClientUsername(), action);
	}

	protected void logWarning(String username, Action action) {
		this.logUserAction(LogType.WARNING, username, action);
	}

	protected void logWarning(String rt, String username, Action action) {
		this.logRTAction(LogType.WARNING, username, rt, action);
	}

	protected void logWarning(int rid, String username, Action action) {
		this.logContextAction(LogType.WARNING, username, rid, action);
	}

	protected void logInfo(Action action) {
		this.logUserAction(LogType.INFO, this.getClientUsername(), action);
	}

	protected void logInfo(String username, Action action) {
		this.logUserAction(LogType.INFO, username, action);
	}

	protected void logInfo(String rt, String username, Action action) {
		this.logRTAction(LogType.INFO, username, rt, action);
	}

	protected void logInfo(int rid, String username, Action action) {
		this.logContextAction(LogType.INFO, username, rid, action);
	}

	private void logEntry(LogEntry entry) {
		logStore.addEntry(entry);

		try {
			log.info(JSONConverter.toJSON(entry.getAction()));
		} catch (IOException e) {
		}
	}

	private void logUserAction(LogType type, String username, Action action) {

		LogEntry entry = new LogEntry(type);
		entry.setAction(action);
		this.fillAction(action);
		entry.setUsername(username);
		entry.setDate(new Date());

		this.logEntry(entry);
	}

	private void logContextAction(LogType type, String username, int rid,
			Action action) {

		LogEntry entry = new LogEntry(type);
		this.fillAction(action);
		entry.setAction(action);
		entry.setUsername(username);
		entry.setDate(new Date());
		entry.setRid(rid);

		this.logEntry(entry);
	}

	private void logRTAction(LogType type, String username, String rt,
			Action action) {

		LogEntry entry = new LogEntry(type);
		entry.setAction(action);
		this.fillAction(action);
		entry.setUsername(username);
		entry.setDate(new Date());
		entry.setRt(rt);

		this.logEntry(entry);
	}
}
