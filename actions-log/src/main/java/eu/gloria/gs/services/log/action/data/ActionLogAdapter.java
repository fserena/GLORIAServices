package eu.gloria.gs.services.log.action.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.LogType;
import eu.gloria.gs.services.log.action.data.dbservices.ActionLogDBService;
import eu.gloria.gs.services.log.action.data.dbservices.ActionLogEntry;
import eu.gloria.gs.services.utils.JSONConverter;
import eu.gloria.gs.services.utils.LoggerEntity;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class ActionLogAdapter extends LoggerEntity {

	private ActionLogDBService service;

	/**
	 * 
	 */
	public ActionLogAdapter() {
		super(ActionLogAdapter.class.getSimpleName());
	}

	public void init() {

		try {
			service.create();
			log.info("Database ready");
		} catch (PersistenceException e) {
			log.error(e.getMessage());
		}
	}

	private void register(String user, Date when, LogType type, Integer rid,
			String rt, Object message) throws ActionException {

		ActionLogEntry entry = new ActionLogEntry();
		entry.setUser(user);
		entry.setDate(when);
		entry.setType(this.stringifyType(type));
		entry.setRid(rid);
		entry.setRt(rt);
		try {
			entry.setAction(JSONConverter.toJSON(message));
			service.save(entry);
			log.debug("New entry registered: " + entry.getAction());
		} catch (IOException | PersistenceException e) {
			log.error(e.getMessage());
			throw new ActionException(e.getMessage());
		}
	}

	public void registerUserAction(LogType type, String user, Date when,
			Object message) throws ActionException {
		this.register(user, when, type, null, null, message);
	}

	public void registerContextAction(LogType type, String user, Date when,
			int rid, Object message) throws ActionException {
		this.register(user, when, type, rid, null, message);
	}

	public void registerRTAction(LogType type, String user, Date when,
			String rt, Object message) throws ActionException {
		this.register(user, when, type, null, rt, message);
	}

	private LogType parseType(String type) {
		if (type.equals("E"))
			return LogType.ERROR;
		else if (type.equals("I"))
			return LogType.INFO;
		else
			return LogType.WARNING;
	}

	private String stringifyType(LogType type) {
		return type.name().substring(0, 1);
	}

	private List<ActionLogInformation> getActionInformations(
			List<ActionLogEntry> entries) {
		List<ActionLogInformation> actionLogs = new ArrayList<ActionLogInformation>();

		for (ActionLogEntry entry : entries) {
			ActionLogInformation actionInfo = new ActionLogInformation();

			actionInfo.setUser(entry.getUser());
			actionInfo.setDate(entry.getDate());
			actionInfo.setRid(entry.getRid());
			actionInfo.setRt(entry.getRt());
			actionInfo.setId(entry.getIdactions_log());
			actionInfo.setType(this.parseType(entry.getType()));
			try {
				actionInfo.setAction(JSONConverter.fromJSON(
						(String) entry.getAction(), Object.class, null));

			} catch (IOException e) {
				log.warn(e.getMessage());
				actionInfo.setAction("?");
			}

			actionLogs.add(actionInfo);
		}

		return actionLogs;
	}

	public List<ActionLogInformation> getLogs(Date from, Date to, LogType type)
			throws ActionException {

		try {
			List<ActionLogEntry> entries;

			if (type == null) {
				entries = service.getAllByDate(from, to);
			} else {
				entries = service.getByDateAndType(from, to, type.name());
			}

			return this.getActionInformations(entries);
		} catch (PersistenceException e) {
			log.error(e.getMessage());
			throw new ActionException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getUserLogs(String user, Date from,
			Date to, LogType type) throws ActionException {

		try {

			List<ActionLogEntry> entries = null;
			if (from == null || to == null) {
				if (type == null) {
					entries = service.getAllByUser(user);
				} else {
					entries = service.getByUserAndType(user,
							this.stringifyType(type));
				}
			} else {
				if (type == null) {
					entries = service.getAllByUserAndDate(user, from, to);
				} else {
					entries = service.getByUserTypeAndDate(user,
							this.stringifyType(type), from, to);
				}
			}

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			log.error(e.getMessage());
			throw new ActionException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getRTLogs(String rt, Date from, Date to,
			LogType type) throws ActionException {

		try {

			List<ActionLogEntry> entries = null;
			if (from == null || to == null) {
				if (type == null) {
					entries = service.getAllByRt(rt);
				} else {
					entries = service.getByRtAndType(rt,
							this.stringifyType(type));
				}
			} else {
				if (type == null) {
					entries = service.getAllByRtAndDate(rt, from, to);
				} else {
					entries = service.getByRtTypeAndDate(rt,
							this.stringifyType(type), from, to);
				}
			}

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			log.error(e.getMessage());
			throw new ActionException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getContextLogs(int rid, Date from,
			Date to, LogType type) throws ActionException {

		try {

			List<ActionLogEntry> entries = null;
			if (from == null || to == null) {
				if (type == null) {
					entries = service.getAllByRid(rid);
				} else {
					entries = service.getByRidAndType(rid,
							this.stringifyType(type));
				}
			} else {
				if (type == null) {
					entries = service.getAllByRidAndDate(rid, from, to);
				} else {
					entries = service.getByRidTypeAndDate(rid,
							this.stringifyType(type), from, to);
				}
			}

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			log.error(e.getMessage());
			throw new ActionException(e.getMessage());
		}
	}

	public boolean containsContextLogs(int rid, Date from, Date to)
			throws ActionException {

		try {
			if (from == null || to == null) {
				return service.containsRid(rid);
			} else {
				return service.containsRidByDate(rid, from, to);
			}
		} catch (PersistenceException e) {
			log.error(e.getMessage());
			throw new ActionException(e.getMessage());
		}
	}

	public boolean containsLogs(Date from, Date to) throws ActionException {

		try {
			return service.containsDate(from, to);
		} catch (PersistenceException e) {
			log.error(e.getMessage());
			throw new ActionException(e.getMessage());
		}
	}

	public boolean containsUserLogs(String user, Date from, Date to)
			throws ActionException {

		try {
			if (from == null || to == null) {
				return service.containsUser(user);
			} else {
				return service.containsUserByDate(user, from, to);
			}
		} catch (PersistenceException e) {
			log.error(e.getMessage());
			throw new ActionException(e.getMessage());
		}
	}

	public boolean containsRTLogs(String rt, Date from, Date to)
			throws ActionException {

		try {
			if (from == null || to == null) {
				return service.containsUser(rt);
			} else {
				return service.containsRtByDate(rt, from, to);
			}
		} catch (PersistenceException e) {
			log.error(e.getMessage());
			throw new ActionException(e.getMessage());
		}
	}

	public void setActionLogDBService(ActionLogDBService service) {
		this.service = (ActionLogDBService) service;
		log.info("Database service injected");
	}
}
