package eu.gloria.gs.services.log.action.data;

import java.util.Date;

import eu.gloria.gs.services.log.action.data.dbservices.ActionLogDBService;
import eu.gloria.gs.services.log.action.data.dbservices.ActionLogEntry;
import eu.gloria.gs.services.log.action.data.dbservices.ActionLogAdapterException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class ActionLogAdapter {

	private ActionLogDBService actionLogService;

	/**
	 * 
	 */
	public ActionLogAdapter() {

	}

	/**
	 * 
	 */
	public void init() {

		actionLogService.create();
	}

	/**
	 * @param user
	 * @param when
	 * @param action
	 * @throws ActionLogAdapterException
	 */
	public void registerAction(String user, Date when, String action)
			throws ActionLogAdapterException {

		ActionLogEntry entry = new ActionLogEntry();
		entry.setUser(user);
		entry.setDate(when);
		entry.setAction(action);

		actionLogService.save(entry);
	}

	/**
	 * @param service
	 */
	public void setActionLogDBService(ActionLogDBService service) {
		this.actionLogService = service;
	}
}
