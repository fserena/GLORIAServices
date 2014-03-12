package eu.gloria.gs.services.experiment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.gloria.gs.services.core.ErrorLogEntry;
import eu.gloria.gs.services.core.InfoLogEntry;
import eu.gloria.gs.services.core.LogEntry;
import eu.gloria.gs.services.core.LogStore;
import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.core.tasks.ServerThread;
import eu.gloria.gs.services.experiment.base.contexts.ExperimentContextManager;
import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.base.data.ReservationInformation;
import eu.gloria.gs.services.experiment.base.data.ResultInformation;
import eu.gloria.gs.services.experiment.base.data.TimeSlot;
import eu.gloria.gs.services.experiment.base.reservation.NoSuchReservationException;
import eu.gloria.gs.services.experiment.script.NoScriptsAvailableException;
import eu.gloria.gs.services.experiment.script.data.RTScriptDBAdapter;
import eu.gloria.gs.services.experiment.script.data.RTScriptInformation;
import eu.gloria.gs.services.experiment.script.data.ScriptState;
import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.repository.image.ImageRepositoryInterface;
import eu.gloria.gs.services.utils.JSONConverter;

public class RTScriptExecutor extends ServerThread {

	private ExperimentDBAdapter experimentAdapter;
	private RTScriptDBAdapter scriptAdapter;
	private LogStore logStore;
	private String username;
	private String password;
	protected Logger log = LoggerFactory.getLogger(RTScriptExecutor.class
			.getSimpleName());
	private ExperimentContextManager manager;
	private SendMailSSL mailSender;
	private ImageRepositoryInterface image;

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
		super.end();

		try {
			pool.shutdownNow();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private void processPreparedScript(RTScriptInformation script, TimeSlot slot) {
		log.info("Script for " + script.getRt() + ": PREPARED");
		List<String> telescopes = new ArrayList<String>();
		telescopes.add(script.getRt());

		try {
			int rid = experimentAdapter.makeReservation(script.getExperiment(),
					telescopes, this.username, slot);
			scriptAdapter.setReservation(script.getId(), rid);
			scriptAdapter.setScriptScheduled(script.getId());
		} catch (ExperimentDatabaseException e) {
			log.error(e.getMessage());
		}
	}

	private void processScheduledScript(RTScriptInformation script) {
		log.info("Script for " + script.getRt() + ": SCHEDULED");
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
				log.info("Script for " + script.getRt() + ": TRIGGERED");
			}

		} catch (ExperimentDatabaseException | NoSuchReservationException e) {
			log.error(e.getMessage());
		}
	}

	private void processTriggeredScript(RTScriptInformation script) {
		log.info("Script for " + script.getRt() + ": WAITING INVOKE");
		try {
			scriptAdapter.setScriptWaitingInvoke(script.getId());
		} catch (ExperimentDatabaseException e) {
			log.error(e.getMessage());
		}
	}

	private void processWaitingInvokeScript(RTScriptInformation script) {
		int sid = script.getId();
		if (scriptTasks.containsKey(sid)) {
			Future<?> future = scriptTasks.get(sid);

			if (future.isDone()) {
				try {
					scriptAdapter.setScriptWaitingEnd(sid);
					script.setState(ScriptState.WAITING_END);
					log.info("Script for " + script.getRt() + ": WAITING END");
					scriptTasks.remove(sid);
				} catch (ExperimentDatabaseException e) {
					log.error(e.getMessage());
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		} else {
			try {
				scriptAdapter.setScriptError(sid);
				log.info("Script for " + script.getRt() + ": ERROR");
			} catch (ExperimentDatabaseException e) {
				log.error(e.getMessage());
			}

		}
	}

	private void processWaitingEndScript(RTScriptInformation script) {
		Date now = new Date();
		int sid = script.getId();

		try {
			if (script.getSlot().getBegin().getTime()
					+ script.getSlot().getLength() - now.getTime() < 5000) {

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
					log.info("Script for " + script.getRt() + ": NOTIFYING");

				} else {
					scriptAdapter.setScriptDone(sid);
					log.info("Script for " + script.getRt() + ": DONE");
				}
			}
		} catch (ExperimentDatabaseException e) {
			log.error(e.getMessage());
		}
	}

	private void processNotifyingScript(RTScriptInformation script) {
		int sid = script.getId();
		if (scriptTasks.containsKey(sid)) {
			Future<?> future = scriptTasks.get(sid);

			if (future.isDone()) {
				try {
					scriptAdapter.setScriptDone(sid);
					script.setState(ScriptState.DONE);
					log.info("Script for " + script.getRt() + ": DONE");
					scriptTasks.remove(sid);
				} catch (ExperimentDatabaseException e) {
					log.error(e.getMessage());
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		} else {
			try {
				scriptAdapter.setScriptError(sid);
				log.info("Script for " + script.getRt() + ": ERROR");
			} catch (ExperimentDatabaseException e) {
				log.error(e.getMessage());
			}

		}

	}

	@Override
	protected void doWork() {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		GSClientProvider.setCredentials(this.username, this.password);
		List<RTScriptInformation> scripts = null;

		try {

			scripts = scriptAdapter.getAllScriptsActiveNow();
		} catch (ExperimentDatabaseException | NoScriptsAvailableException e) {
		} catch (Exception e) {
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
			log.error(e.getMessage());
		}
	}

	private void processLogEntry(LogEntry entry, String username, Integer rid,
			LogAction action) {
		entry.setUsername(username);
		entry.setDate(new Date());
		if (rid != null)
			entry.setRid(rid);

		entry.setAction(action);
		this.logStore.addEntry(entry);
	}

	private void logError(String username, Integer rid, LogAction action) {

		LogEntry entry = new ErrorLogEntry();
		this.processLogEntry(entry, username, rid, action);

		try {
			log.error(JSONConverter.toJSON(action));
		} catch (IOException e) {
		}
	}

	private void logInfo(String username, Integer rid, LogAction action) {

		LogEntry entry = new InfoLogEntry();
		this.processLogEntry(entry, username, rid, action);

		try {
			log.info(JSONConverter.toJSON(action));
		} catch (IOException e) {
		}
	}
}
