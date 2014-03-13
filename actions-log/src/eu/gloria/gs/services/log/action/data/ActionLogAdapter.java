package eu.gloria.gs.services.log.action.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;

import eu.gloria.gs.services.log.action.data.dbservices.ActionLogDBService;
import eu.gloria.gs.services.log.action.data.dbservices.ActionLogEntry;
import eu.gloria.gs.services.log.action.data.dbservices.ActionLogAdapterException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class ActionLogAdapter {

	private ActionLogDBService service;

	/**
	 * 
	 */
	public ActionLogAdapter() {

	}

	/**
	 * 
	 */
	public void init() {

		service.create();
	}

	private void register(String user, Date when, String type, Integer rid,
			String rt, Object message) throws ActionLogAdapterException {

		ActionLogEntry entry = new ActionLogEntry();
		entry.setUser(user);
		entry.setDate(when);
		entry.setType(type);
		entry.setRid(rid);
		entry.setRt(rt);
		try {
			entry.setAction(JSONConverter.toJSON(message));
			service.save(entry);

		} catch (IOException | PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public void registerError(String user, Date when, Object message)
			throws ActionLogAdapterException {

		this.register(user, when, "E", null, null, message);
	}

	public void registerInfo(String user, Date when, Object message)
			throws ActionLogAdapterException {

		this.register(user, when, "I", null, null, message);
	}

	public void registerWarning(String user, Date when, Object message)
			throws ActionLogAdapterException {

		this.register(user, when, "W", null, null, message);
	}

	public void registerContextError(String user, Date when, int rid,
			Object message) throws ActionLogAdapterException {

		this.register(user, when, "E", rid, null, message);
	}

	public void registerContextInfo(String user, Date when, int rid,
			Object message) throws ActionLogAdapterException {

		this.register(user, when, "I", rid, null, message);
	}

	public void registerContextWarning(String user, Date when, int rid,
			Object message) throws ActionLogAdapterException {

		this.register(user, when, "W", rid, null, message);
	}

	public void registerRtWarning(String user, Date when, String rt,
			Object message) throws ActionLogAdapterException {

		this.register(user, when, "W", null, rt, message);
	}

	public void registerRtError(String user, Date when, String rt,
			Object message) throws ActionLogAdapterException {

		this.register(user, when, "E", null, rt, message);
	}

	public void registerRtInfo(String user, Date when, String rt, Object message)
			throws ActionLogAdapterException {

		this.register(user, when, "I", null, rt, message);
	}

	private LogType parseType(String type) {
		if (type.equals("E"))
			return LogType.ERROR;
		else if (type.equals("I"))
			return LogType.INFO;
		else
			return LogType.WARNING;
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
				actionInfo.setAction("?");
			}

			actionLogs.add(actionInfo);
		}

		return actionLogs;
	}

	public List<ActionLogInformation> getAllUserLogs(String user)
			throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getAllByUser(user);

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getAllUserLogsByDate(String user,
			Date from, Date to) throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getAllByUserAndDate(user,
					from, to);

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getAllRtLogs(String rt)
			throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getAllByRt(rt);

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getAllRtLogsByDate(String rt, Date from,
			Date to) throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getAllByRtAndDate(rt, from,
					to);

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getAllRidLogs(int rid)
			throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getAllByRid(rid);

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getAllRidLogsByDate(int rid, Date from,
			Date to) throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getAllByRidAndDate(rid,
					from, to);

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getAllDateLogs(Date from, Date to)
			throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getAllByDate(from, to);

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getErrorDateLogs(Date from, Date to)
			throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByDateAndType(from, to,
					"E");

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getErrorUserLogs(String user)
			throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByUserAndType(user, "E");

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getErrorUserLogsByDate(String user,
			Date from, Date to) throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByUserTypeAndDate(user,
					"E", from, to);

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getErrorRtLogs(String rt)
			throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByRtAndType(rt, "E");

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getErrorRtLogsByDate(String rt,
			Date from, Date to) throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByRtTypeAndDate(rt, "E",
					from, to);

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getErrorRidLogs(int rid)
			throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByRidAndType(rid, "E");

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getErrorRidLogsByDate(int rid, Date from,
			Date to) throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByRidTypeAndDate(rid,
					"E", from, to);

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getWarningDateLogs(Date from, Date to)
			throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByDateAndType(from, to,
					"W");

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getWarningUserLogs(String user)
			throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByUserAndType(user, "W");

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getWarningUserLogsByDate(String user,
			Date from, Date to) throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByUserTypeAndDate(user,
					"W", from, to);

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getWarningRidLogs(int rid)
			throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByRidAndType(rid, "W");

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getWarningRidLogsByDate(int rid,
			Date from, Date to) throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByRidTypeAndDate(rid,
					"W", from, to);

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getWarningRtLogs(String rt)
			throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByRtAndType(rt, "W");

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getWarningRtLogsByDate(String rt,
			Date from, Date to) throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByRtTypeAndDate(rt, "W",
					from, to);

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getInfoDateLogs(Date from, Date to)
			throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByDateAndType(from, to,
					"I");

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getInfoUserLogs(String user)
			throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByUserAndType(user, "I");

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getInfoUserLogsByDate(String user,
			Date from, Date to) throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByUserTypeAndDate(user,
					"I", from, to);

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getInfoRidLogs(int rid)
			throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByRidAndType(rid, "I");

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getInfoRidLogsByDate(int rid, Date from,
			Date to) throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByRidTypeAndDate(rid,
					"I", from, to);

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getInfoRtLogs(String rt)
			throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByRtAndType(rt, "I");

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public List<ActionLogInformation> getInfoRtLogsByDate(String rt, Date from,
			Date to) throws ActionLogAdapterException {

		try {
			List<ActionLogEntry> entries = service.getByRtTypeAndDate(rt, "I",
					from, to);

			return this.getActionInformations(entries);

		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public boolean containsRidLogs(int rid) throws ActionLogAdapterException {

		try {
			return service.containsRid(rid);
		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public boolean containsRidLogsByDate(int rid, Date from, Date to)
			throws ActionLogAdapterException {

		try {
			return service.containsRidByDate(rid, from, to);
		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public boolean containsDateLogs(Date from, Date to)
			throws ActionLogAdapterException {

		try {
			return service.containsDate(from, to);
		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public boolean containsUserLogs(String user)
			throws ActionLogAdapterException {

		try {
			return service.containsUser(user);
		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public boolean containsUserLogsByDate(String user, Date from, Date to)
			throws ActionLogAdapterException {

		try {
			return service.containsUserByDate(user, from, to);
		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public boolean containsRtLogs(String rt) throws ActionLogAdapterException {

		try {
			return service.containsRt(rt);
		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	public boolean containsRtLogsByDate(String rt, Date from, Date to)
			throws ActionLogAdapterException {

		try {
			return service.containsRtByDate(rt, from, to);
		} catch (PersistenceException e) {
			throw new ActionLogAdapterException(e.getMessage());
		}
	}

	/**
	 * @param service
	 */
	public void setActionLogDBService(ActionLogDBService service) {
		this.service = service;
	}
}
