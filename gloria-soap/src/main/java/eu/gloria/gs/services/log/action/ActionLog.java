package eu.gloria.gs.services.log.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import eu.gloria.gs.services.core.GSWebService;
import eu.gloria.gs.services.log.action.ActionLogException;
import eu.gloria.gs.services.log.action.ActionLogInterface;
import eu.gloria.gs.services.log.action.data.ActionLogAdapter;
import eu.gloria.gs.services.log.action.data.ActionLogInformation;
import eu.gloria.gs.services.utils.JSONConverter;
import eu.gloria.gs.services.utils.ObjectResponse;

public class ActionLog extends GSWebService implements ActionLogInterface {

	private ActionLogAdapter adapter;

	public ActionLog() {
		super(ActionLog.class.getSimpleName());
	}

	public void setAdapter(ActionLogAdapter adapter) {
		this.adapter = adapter;
		adapter.init();
	}

	private Object formatAction(ObjectResponse action)
			throws ActionLogException {
		Object formattedAction = null;
		try {
			formattedAction = JSONConverter.fromJSON((String) action.content,
					Object.class, null);
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}

		return formattedAction;
	}

	@Override
	public void registerUserAction(LogType type, String username, Date when,
			ObjectResponse action) throws ActionLogException {
		try {
			this.adapter.registerUserAction(type, username, when,
					this.formatAction(action));
		} catch (ActionException e) {
			throw new ActionLogException(e.getAction());
		}
	}

	@Override
	public void registerContextAction(LogType type, String username, Date when,
			int rid, ObjectResponse action) throws ActionLogException {
		try {
			this.adapter.registerContextAction(type, username, when, rid,
					this.formatAction(action));
		} catch (ActionException e) {
			throw new ActionLogException(e.getAction());
		}
	}

	@Override
	public void registerRTAction(LogType type, String username, Date when,
			String rt, ObjectResponse action) throws ActionLogException {
		try {
			this.adapter.registerRTAction(type, username, when, rt,
					this.formatAction(action));
		} catch (ActionException e) {
			throw new ActionLogException(e.getAction());
		}
	}

	@Override
	public ObjectResponse getLogs(Date from, Date to, LogType type) throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter.getLogs(from,
					to, type);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getUserLogs(String username, Date from, Date to,
			LogType type) throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter.getUserLogs(
					username, from, to, type);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getRTLogs(String rt, Date from, Date to, LogType type)
			throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter.getRTLogs(rt,
					from, to, type);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getContextLogs(int rid, Date from, Date to,
			LogType type) throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getContextLogs(rid, from, to, type);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public boolean containsUserLogs(String user, Date from, Date to)
			throws ActionLogException {
		try {
			return this.adapter.containsUserLogs(user, from, to);
		} catch (ActionException e) {
			throw new ActionLogException(e.getAction());
		}
	}

	@Override
	public boolean containsRtLogs(String rt, Date from, Date to)
			throws ActionLogException {
		try {
			return this.adapter.containsRTLogs(rt, from, to);
		} catch (ActionException e) {
			throw new ActionLogException(e.getAction());
		}
	}

	@Override
	public boolean containsLogs(Date from, Date to) throws ActionLogException {
		try {
			return this.adapter.containsLogs(from, to);
		} catch (ActionException e) {
			throw new ActionLogException(e.getAction());
		}
	}

	@Override
	public boolean containsContextLogs(int rid, Date from, Date to)
			throws ActionLogException {
		try {
			return this.adapter.containsContextLogs(rid, from, to);
		} catch (ActionException e) {
			throw new ActionLogException(e.getAction());
		}
	}

}
