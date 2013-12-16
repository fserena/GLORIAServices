/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.core;

import java.util.Date;

import eu.gloria.gs.services.log.action.LogAction;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public abstract class GSLogProducerService extends GSWebService {

	private LogStore logStore;

	public void setLogStore(LogStore logStore) {
		this.logStore = logStore;

	}

	public LogStore getLogStore() {
		return this.logStore;
	}
	
	private void fillAction(LogAction action) {
		action.put("sender", this.getUsername());
		action.put("owner", this.getClientUsername());				
	}

	protected void logInfo(String username, LogAction action) {

		LogEntry entry = new InfoLogEntry();
		entry.setAction(action);
		this.fillAction(action);
		entry.setUsername(username);
		entry.setDate(new Date());

		logStore.addEntry(entry);

	}

	protected void logError(String username, LogAction action) {

		LogEntry entry = new ErrorLogEntry();
		this.fillAction(action);		
		entry.setAction(action);
		entry.setUsername(username);
		entry.setDate(new Date());

		logStore.addEntry(entry);
	}

	protected void logWarning(String username, LogAction action) {

		LogEntry entry = new WarningLogEntry();
		this.fillAction(action);
		entry.setAction(action);
		entry.setUsername(username);
		entry.setDate(new Date());

		logStore.addEntry(entry);
	}
	
	protected void logContextInfo(String username, int rid, LogAction action) {

		LogEntry entry = new InfoLogEntry();
		this.fillAction(action);
		entry.setAction(action);
		entry.setUsername(username);
		entry.setDate(new Date());
		entry.setRid(rid);

		logStore.addEntry(entry);
	}

	protected void logContextError(String username, int rid, LogAction action) {

		LogEntry entry = new ErrorLogEntry();
		entry.setAction(action);
		this.fillAction(action);
		entry.setUsername(username);
		entry.setDate(new Date());
		entry.setRid(rid);

		logStore.addEntry(entry);
	}

	protected void logContextWarning(String username, int rid, LogAction action) {

		LogEntry entry = new WarningLogEntry();
		entry.setAction(action);
		this.fillAction(action);
		entry.setUsername(username);
		entry.setDate(new Date());
		entry.setRid(rid);

		logStore.addEntry(entry);
	}
	
	protected void logRtInfo(String username, String rt, LogAction action) {

		LogEntry entry = new InfoLogEntry();
		entry.setAction(action);
		this.fillAction(action);
		entry.setUsername(username);
		entry.setDate(new Date());
		entry.setRt(rt);

		logStore.addEntry(entry);
	}

	protected void logRtError(String username, String rt, LogAction action) {

		LogEntry entry = new ErrorLogEntry();
		entry.setAction(action);
		this.fillAction(action);
		entry.setUsername(username);
		entry.setDate(new Date());
		entry.setRt(rt);

		logStore.addEntry(entry);
	}

	protected void logRtWarning(String username, String rt, LogAction action) {

		LogEntry entry = new WarningLogEntry();
		entry.setAction(action);
		this.fillAction(action);
		entry.setUsername(username);
		entry.setDate(new Date());
		entry.setRt(rt);

		logStore.addEntry(entry);
	}

}
