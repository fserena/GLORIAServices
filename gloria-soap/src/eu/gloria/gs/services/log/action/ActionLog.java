package eu.gloria.gs.services.log.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import eu.gloria.gs.services.core.GSWebService;
import eu.gloria.gs.services.log.action.ActionLogException;
import eu.gloria.gs.services.log.action.ActionLogInterface;
import eu.gloria.gs.services.log.action.data.ActionLogAdapter;
import eu.gloria.gs.services.log.action.data.ActionLogInformation;
import eu.gloria.gs.services.log.action.data.JSONConverter;
import eu.gloria.gs.services.log.action.data.dbservices.ActionLogAdapterException;
import eu.gloria.gs.services.utils.ObjectResponse;

public class ActionLog extends GSWebService implements ActionLogInterface {

	private ActionLogAdapter adapter;

	public ActionLog() {
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
	public void registerError(String username, Date when, ObjectResponse action)
			throws ActionLogException {

		try {
			this.adapter.registerError(username, when,
					this.formatAction(action));
		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		}
	}

	@Override
	public void registerWarning(String username, Date when,
			ObjectResponse action) throws ActionLogException {
		try {
			this.adapter.registerWarning(username, when,
					this.formatAction(action));
		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		}

	}

	@Override
	public void registerInfo(String username, Date when, ObjectResponse action)
			throws ActionLogException {
		try {
			this.adapter
					.registerInfo(username, when, this.formatAction(action));
		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		}

	}

	@Override
	public void registerContextError(String username, Date when, int rid,
			ObjectResponse action) throws ActionLogException {
		try {
			this.adapter.registerContextError(username, when, rid,
					this.formatAction(action));
		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		}

	}

	@Override
	public void registerContextWarning(String username, Date when, int rid,
			ObjectResponse action) throws ActionLogException {
		try {
			this.adapter.registerContextWarning(username, when, rid,
					this.formatAction(action));
		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		}
	}

	@Override
	public void registerContextInfo(String username, Date when, int rid,
			ObjectResponse action) throws ActionLogException {
		try {
			this.adapter.registerContextInfo(username, when, rid,
					this.formatAction(action));
		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		}
	}

	@Override
	public void registerRtError(String username, Date when, String rt,
			ObjectResponse action) throws ActionLogException {
		try {
			this.adapter.registerRtError(username, when, rt,
					this.formatAction(action));
		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		}

	}

	@Override
	public void registerRtWarning(String username, Date when, String rt,
			ObjectResponse action) throws ActionLogException {
		try {
			this.adapter.registerRtWarning(username, when, rt,
					this.formatAction(action));
		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		}
	}

	@Override
	public void registerRtInfo(String username, Date when, String rt,
			ObjectResponse action) throws ActionLogException {
		try {
			this.adapter.registerRtInfo(username, when, rt,
					this.formatAction(action));
		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		}
	}

	@Override
	public ObjectResponse getAllUserLogs(String username)
			throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getAllUserLogs(username);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getAllUserLogsByDate(String username, Date from,
			Date to) throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getAllUserLogsByDate(username, from, to);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getAllRtLogs(String rt) throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getAllRtLogs(rt);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getAllRtLogsByDate(String rt, Date from, Date to)
			throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getAllRtLogsByDate(rt, from, to);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getAllRidLogs(int rid) throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getAllRidLogs(rid);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getAllRidLogsByDate(int rid, Date from, Date to)
			throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getAllRidLogsByDate(rid, from, to);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getAllDateLogs(Date from, Date to)
			throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getAllDateLogs(from, to);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getErrorLogsByDate(Date from, Date to)
			throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getErrorDateLogs(from, to);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getInfoLogsByDate(Date from, Date to)
			throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getInfoDateLogs(from, to);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getWarningLogsByDate(Date from, Date to)
			throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getWarningDateLogs(from, to);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getErrorUserLogs(String username)
			throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getErrorUserLogs(username);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getErrorUserLogsByDate(String username, Date from,
			Date to) throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getErrorUserLogsByDate(username, from, to);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getErrorRtLogs(String rt) throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getErrorRtLogs(rt);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getErrorRtLogsByDate(String rt, Date from, Date to)
			throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getErrorRtLogsByDate(rt, from, to);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getErrorRidLogs(int rid) throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getErrorRidLogs(rid);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getErrorRidLogsByDate(int rid, Date from, Date to)
			throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getErrorRidLogsByDate(rid, from, to);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getInfoUserLogs(String username)
			throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getInfoUserLogs(username);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getInfoUserLogsByDate(String username, Date from,
			Date to) throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getInfoUserLogsByDate(username, from, to);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getInfoRtLogs(String rt) throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getInfoRtLogs(rt);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getInfoRtLogsByDate(String rt, Date from, Date to)
			throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getInfoRtLogsByDate(rt, from, to);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getInfoRidLogs(int rid) throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getInfoRidLogs(rid);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getInfoRidLogsByDate(int rid, Date from, Date to)
			throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getInfoRidLogsByDate(rid, from, to);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getWarningUserLogs(String username)
			throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getWarningUserLogs(username);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getWarningUserLogsByDate(String username, Date from,
			Date to) throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getWarningUserLogsByDate(username, from, to);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getWarningRtLogs(String rt) throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getWarningRtLogs(rt);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getWarningRtLogsByDate(String rt, Date from, Date to)
			throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getWarningRtLogsByDate(rt, from, to);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getWarningRidLogs(int rid) throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getWarningRidLogs(rid);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public ObjectResponse getWarningRidLogsByDate(int rid, Date from, Date to)
			throws ActionLogException {
		try {
			List<ActionLogInformation> actionLogs = this.adapter
					.getWarningRidLogsByDate(rid, from, to);

			return new ObjectResponse(JSONConverter.toJSON(actionLogs));

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		} catch (IOException e) {
			throw new ActionLogException("json error");
		}
	}

	@Override
	public boolean containsUserLogs(String user) throws ActionLogException {
		try {
			return this.adapter.containsUserLogs(user);

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		}
	}

	@Override
	public boolean containsUserLogsByDate(String user, Date from, Date to)
			throws ActionLogException {
		try {
			return this.adapter.containsUserLogsByDate(user, from, to);

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		}
	}

	@Override
	public boolean containsRtLogs(String rt) throws ActionLogException {
		try {
			return this.adapter.containsRtLogs(rt);

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		}
	}

	@Override
	public boolean containsRtLogsByDate(String rt, Date from, Date to)
			throws ActionLogException {
		try {
			return this.adapter.containsRtLogsByDate(rt, from, to);

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		}
	}

	@Override
	public boolean containsDateLogs(Date from, Date to)
			throws ActionLogException {
		try {
			return this.adapter.containsDateLogs(from, to);

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		}
	}

	@Override
	public boolean containsRidLogs(int rid) throws ActionLogException {
		try {
			return this.adapter.containsRidLogs(rid);

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		}
	}

	@Override
	public boolean containsRidLogsByDate(int rid, Date from, Date to)
			throws ActionLogException {
		try {
			return this.adapter.containsRidLogsByDate(rid, from, to);

		} catch (ActionLogAdapterException e) {
			throw new ActionLogException(e.getAction());
		}
	}

}
