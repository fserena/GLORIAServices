package eu.gloria.gs.services.experiment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import eu.gloria.gs.services.core.LogEntry;
import eu.gloria.gs.services.core.LogStore;
import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.core.tasks.ServerThread;
import eu.gloria.gs.services.experiment.base.contexts.ExperimentContextManager;
import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.ReservationInformation;
import eu.gloria.gs.services.experiment.base.data.ResultInformation;
import eu.gloria.gs.services.experiment.base.data.TimeSlot;
import eu.gloria.gs.services.experiment.script.data.RTScriptDBAdapter;
import eu.gloria.gs.services.experiment.script.data.RTScriptInformation;
import eu.gloria.gs.services.experiment.script.data.ScriptState;
import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.log.action.LogType;
import eu.gloria.gs.services.repository.image.ImageRepositoryInterface;

public class RTScriptExecutor extends ServerThread {

	private ExperimentDBAdapter experimentAdapter;
	private RTScriptDBAdapter scriptAdapter;
	private LogStore logStore;
	private String username;
	private String password;
	private ExperimentContextManager manager;
	private SendMailSSL mailSender;
	private ImageRepositoryInterface image;

	/**
	 * @param name
	 */
	public RTScriptExecutor() {
		super(RTScriptExecutor.class.getSimpleName());
	}

	private static Map<Integer, Future<?>> scriptTasks = new HashMap<Integer, Future<?>>();
	private static ExecutorService pool = Executors.newCachedThreadPool();

	public void setMailSender(SendMailSSL mailSender) {
		this.mailSender = mailSender;
	}

	public void setExperimentAdapter(ExperimentDBAdapter adapter) {
		this.experimentAdapter = adapter;
	}

	public void setRTScriptAdapter(RTScriptDBAdapter adapter) {
		this.scriptAdapter = adapter;
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

	public void setContextManager(ExperimentContextManager manager) {
		this.manager = manager;
	}

	public void setImageRepository(ImageRepositoryInterface image) {
		this.image = image;
	}

	@Override
	public void end() {		
		try {
			pool.shutdownNow();
		} catch (Exception e) {
			this.log(LogType.ERROR, e.getMessage());
		}

		GSClientProvider.clearCredentials();
		super.end();
	}

	private Action getLogRTAction(RTScriptInformation script) {
		Action action = new Action();
		action.put("sender", "script daemon");
		action.put("client", script.getUsername());
		action.put("sid", script.getId());
		action.put("state", script.getState());
		action.put("rt", script.getRt());
		action.put("rid", script.getRid());
		return action;
	}

	private void processPreparedScript(RTScriptInformation script, TimeSlot slot) {

		Action action = this.getLogRTAction(script);
		log(LogType.INFO, action);
		List<String> telescopes = new ArrayList<String>();
		telescopes.add(script.getRt());

		try {
			int rid = experimentAdapter.makeReservation(script.getExperiment(),
					telescopes, this.username, slot);
			scriptAdapter.setReservation(script.getId(), rid);
			scriptAdapter.setScriptScheduled(script.getId());
			action.put("state", ScriptState.SCHEDULED);
		} catch (ActionException e) {
			action.child("exception", e.getAction());
			log(LogType.ERROR, action);
		}
	}

	private void processScheduledScript(RTScriptInformation script) {
		Action action = this.getLogRTAction(script);
		try {
			ReservationInformation resInfo = experimentAdapter
					.getReservationInformation(script.getRid());

			String resStatus = resInfo.getStatus();
			if (resStatus.equals("READY")) {
				ScriptTask task = new ScriptTask();
				task.setExperimentContextManager(manager);
				task.setLogger(log);
				task.setScript(script);

				Future<?> future = pool.submit(task);
				scriptTasks.put(script.getId(), future);
				scriptAdapter.setScriptTriggered(script.getId());
				action.put("state", ScriptState.TRIGGERED);
				action.put("operation", script.getOperation());
				action.put("ready", true);
				log(LogType.INFO, script.getUsername(),
						resInfo.getReservationId(), action);
			} else {
				action.put("ready", false);
				log(LogType.WARNING, action);
			}

		} catch (ActionException e) {
			action.child("exception", e.getAction());
			log(LogType.ERROR, script.getUsername(), (Integer) script.getRid(),
					action);
		}
	}

	private void processTriggeredScript(RTScriptInformation script) {
		Action action = this.getLogRTAction(script);
		try {
			scriptAdapter.setScriptWaitingInvoke(script.getId());
			action.put("state", ScriptState.WAITING_INVOKE);
			log(LogType.INFO, script.getUsername(), script.getRid(), action);
		} catch (ActionException e) {
			action.child("exception", e.getAction());
			log(LogType.ERROR, script.getUsername(), (Integer) script.getRid(),
					action);
		}
	}

	private void processWaitingInvokeScript(RTScriptInformation script) {
		Action action = this.getLogRTAction(script);
		int sid = script.getId();
		if (scriptTasks.containsKey(sid)) {
			Future<?> future = scriptTasks.get(sid);

			if (future.isDone()) {
				try {
					scriptAdapter.setScriptWaitingEnd(sid);
					script.setState(ScriptState.WAITING_END);
					scriptTasks.remove(sid);
					action.put("state", ScriptState.WAITING_END);
					log(LogType.INFO, script.getUsername(), script.getRid(),
							action);
				} catch (ActionException e) {
					action.child("exception", e.getAction());
					log(LogType.ERROR, script.getUsername(),
							(Integer) script.getRid(), action);
				} catch (Exception e) {
					log(LogType.ERROR, e.getMessage());
				}
			}
		} else {
			try {
				scriptAdapter.setScriptError(sid);
				action.put("state", ScriptState.ERROR);
				log(LogType.ERROR, script.getUsername(), script.getRid(),
						action);
			} catch (ActionException e) {
				action.child("exception", e.getAction());
				log(LogType.ERROR, script.getUsername(),
						(Integer) script.getRid(), action);
			}

		}
	}

	private void processWaitingEndScript(RTScriptInformation script) {
		Action action = this.getLogRTAction(script);
		Date now = new Date();
		int sid = script.getId();

		try {
			ReservationInformation resInfo = experimentAdapter
					.getReservationInformation(script.getRid());

			if (resInfo.getTimeSlot().getBegin().getTime()
					+ script.getSlot().getLength() - now.getTime() < 5000) {

				action.put("notify", script.isNotify());

				if (script.isNotify()) {
					NotificationTask task = new NotificationTask();
					task.setExperimentContextManager(manager);
					task.setLogger(log);
					task.setScript(script);
					task.setMailSender(this.mailSender);

					List<ResultInformation> results = experimentAdapter
							.getExperimentResults(script.getExperiment());

					task.setResults(results);
					task.setImageRepository(image);
					task.setUsername(this.username);
					task.setPassword(this.password);

					Future<?> future = pool.submit(task);
					scriptTasks.put(sid, future);
					scriptAdapter.setScriptNotifying(sid);
					action.put("state", ScriptState.NOTIFYING);
				} else {
					scriptAdapter.setScriptDone(sid);
					action.put("state", ScriptState.DONE);
				}

				log(LogType.INFO, script.getUsername(), script.getRid(), action);
			}
		} catch (ActionException e) {
			action.child("exception", e.getAction());
			log(LogType.ERROR, script.getUsername(), (Integer) script.getRid(),
					action);
		}
	}

	private void processNotifyingScript(RTScriptInformation script) {
		Action action = this.getLogRTAction(script);
		int sid = script.getId();
		if (scriptTasks.containsKey(sid)) {
			Future<?> future = scriptTasks.get(sid);

			if (future.isDone()) {
				try {
					scriptAdapter.setScriptDone(sid);
					script.setState(ScriptState.DONE);
					action.put("state", script.getState());
					log(LogType.INFO, script.getUsername(), script.getRid(),
							action);
					scriptTasks.remove(sid);
				} catch (ActionException e) {
					action.child("exception", e.getAction());
					log(LogType.ERROR, script.getUsername(),
							(Integer) script.getRid(), action);
				} catch (Exception e) {
					log(LogType.ERROR, e.getMessage());
				}
			}
		} else {
			try {
				scriptAdapter.setScriptError(sid);
				action.put("state", ScriptState.ERROR);
				log(LogType.INFO, script.getUsername(), script.getRid(), action);
			} catch (ActionException e) {
				action.child("exception", e.getAction());
				log(LogType.ERROR, script.getUsername(),
						(Integer) script.getRid(), action);
			}

		}

	}

	@Override
	protected void doWork() {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			log.warn(e.getMessage());
		}

		GSClientProvider.setCredentials(this.username, this.password);
		List<RTScriptInformation> scripts = null;

		try {

			scripts = scriptAdapter.getAllScriptsActiveNow();
		} catch (ActionException e) {
		} catch (Exception e) {
			log(LogType.ERROR, e.getMessage());
		}

		if (scripts != null) {

			for (RTScriptInformation script : scripts) {

				ScriptSlot scriptSlot = script.getSlot();
				TimeSlot dailyTimeSlot = new TimeSlot();
				dailyTimeSlot.setBegin(scriptSlot.getBegin());
				dailyTimeSlot.setEnd(new Date(scriptSlot.getBegin().getTime()
						+ scriptSlot.getLength()));

				Calendar calendar = Calendar.getInstance();
				int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
				int year = calendar.get(Calendar.YEAR);

				calendar.setTime(dailyTimeSlot.getBegin());
				calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
				calendar.set(Calendar.YEAR, year);
				dailyTimeSlot.setBegin(calendar.getTime());
				dailyTimeSlot.setEnd(new Date(dailyTimeSlot.getBegin()
						.getTime() + scriptSlot.getLength()));

				if (script.getState().equals(ScriptState.PREPARED)) {
					this.processPreparedScript(script, dailyTimeSlot);
				} else if (script.getState().equals(ScriptState.SCHEDULED)) {
					this.processScheduledScript(script);
				} else if (script.getState().equals(ScriptState.TRIGGERED)) {
					this.processTriggeredScript(script);
				} else if (script.getState().equals(ScriptState.WAITING_INVOKE)) {
					this.processWaitingInvokeScript(script);
				} else if (script.getState().equals(ScriptState.WAITING_END)) {
					this.processWaitingEndScript(script);
				} else if (script.getState().equals(ScriptState.NOTIFYING)) {
					this.processNotifyingScript(script);
				}
			}
		}

		try {
			scriptAdapter.prepareAllDailyNotActive();
		} catch (Exception e) {
			log(LogType.ERROR, e.getMessage());
		}
	}

	private void processLogEntry(LogEntry entry, String username, Integer rid,
			Action action) {
		entry.setUsername(username);
		entry.setDate(new Date());
		if (rid != null)
			entry.setRid(rid);

		entry.setAction(action);
		this.logStore.addEntry(entry);
	}

	private void log(LogType type, String username, Integer rid, Action action) {
		LogEntry entry = new LogEntry(type);
		this.processLogEntry(entry, username, rid, action);

		super.log(type, action);
	}
}
