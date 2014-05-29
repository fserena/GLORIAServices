package eu.gloria.gs.services.experiment.script.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;

import eu.gloria.gs.services.experiment.ExperimentException;
import eu.gloria.gs.services.experiment.ScriptSlot;
import eu.gloria.gs.services.experiment.base.data.dbservices.ExperimentDBService;
import eu.gloria.gs.services.experiment.script.NoScriptsAvailableException;
import eu.gloria.gs.services.experiment.script.NoSuchScriptException;
import eu.gloria.gs.services.experiment.script.OverlapRTScriptException;
import eu.gloria.gs.services.experiment.script.data.dbservices.RTScriptDBService;
import eu.gloria.gs.services.experiment.script.data.dbservices.RTScriptEntry;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class RTScriptDBAdapter {

	private RTScriptDBService service;
	private ExperimentDBService experimentService;

	/**
	 * 
	 */
	public RTScriptDBAdapter() {

	}

	public void setExperimentDBService(ExperimentDBService service)
			throws ExperimentException {
		this.experimentService = service;
	}

	public void setRTScriptDBService(RTScriptDBService service)
			throws ExperimentException {
		this.service = service;

		try {
			service.createRTScriptTable();
			service.createRTScriptRidTable();
		} catch (PersistenceException e) {
			throw new ExperimentException();
		}
	}

	public synchronized int addRTScript(String user, String experiment,
			String rt, String operation, ScriptSlot scriptSlot, String init,
			String result, boolean notify) throws ExperimentException,
			OverlapRTScriptException {

		try {
			int experimentId = experimentService.getExperimentId(experiment);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(scriptSlot.getBegin());

			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			int second = calendar.get(Calendar.SECOND);
			int ms = calendar.get(Calendar.MILLISECOND);

			calendar.setTime(new Date(0));
			calendar.set(Calendar.HOUR_OF_DAY, hour);
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, second);
			calendar.set(Calendar.MILLISECOND, ms);

			scriptSlot.setBegin(calendar.getTime());

			Date end = new Date(scriptSlot.getBegin().getTime()
					+ scriptSlot.getLength());

			if (service.anyRTScriptBetween(rt, scriptSlot.getBegin(), end)) {
				throw new OverlapRTScriptException(rt);
			}

			RTScriptEntry scriptEntry = new RTScriptEntry();

			scriptEntry.setExperiment(experimentId);
			scriptEntry.setType("DAILY");
			scriptEntry.setRt(rt);
			scriptEntry.setBegin(scriptSlot.getBegin());
			scriptEntry.setEnd(end);
			scriptEntry.setOperation(operation);
			scriptEntry.setUser(user);
			scriptEntry.setInit(init);
			if (notify) {
				scriptEntry.setNotify(1);
			}

			service.saveRTScript(scriptEntry);

			return service.getScriptId(rt, scriptSlot.getBegin());

		} catch (PersistenceException e) {
			throw new ExperimentException();
		}
	}

	public void removeRTScript(int sid) throws ExperimentException {
		try {
			service.removeScript(sid);
		} catch (PersistenceException e) {
			throw new ExperimentException();
		}
	}

	public List<Integer> getAllRTScripts(String rt)
			throws ExperimentException {
		try {
			List<Integer> scripts = service.getAllRTScripts(rt);
			if (scripts == null) {
				scripts = new ArrayList<Integer>();
			}

			return scripts;
		} catch (PersistenceException e) {
			throw new ExperimentException();
		}
	}

	private void parseStatus(RTScriptInformation script, String status) {
		if ("S".equals(status)) {
			script.setState(ScriptState.SCHEDULED);
		} else if ("P".equals(status)) {
			script.setState(ScriptState.PREPARED);
		} else if ("R".equals(status)) {
			script.setState(ScriptState.READY);
		} else if ("D".equals(status)) {
			script.setState(ScriptState.DONE);
		} else if ("T".equals(status)) {
			script.setState(ScriptState.TRIGGERED);
		} else if ("I".equals(status)) {
			script.setState(ScriptState.WAITING_INVOKE);
		} else if ("W".equals(status)) {
			script.setState(ScriptState.WAITING_END);
		} else if ("E".equals(status)) {
			script.setState(ScriptState.ERROR);
		} else if ("N".equals(status)) {
			script.setState(ScriptState.NOTIFYING);
		}
	}

	public RTScriptInformation getRTScriptInformation(int sid)
			throws ExperimentException, NoSuchScriptException {

		RTScriptInformation rtScript = null;

		try {
			RTScriptEntry scriptEntry = service.getRTScript(sid);

			if (scriptEntry != null) {
				rtScript = this.processRTScriptEntry(scriptEntry);
			}
		} catch (PersistenceException e) {
			throw new ExperimentException();
		}

		if (rtScript == null)
			throw new NoSuchScriptException(sid);

		return rtScript;
	}

	/**
	 * @return
	 * @throws ExperimentException
	 * @throws NoScriptsAvailableException
	 */
	public List<RTScriptInformation> getAllScriptsActiveNow()
			throws ExperimentException, NoScriptsAvailableException {

		try {
			List<RTScriptEntry> scriptEntries = service
					.getAllDailyScriptsAt(new Date());

			if (scriptEntries == null || scriptEntries.size() == 0) {
				throw new NoScriptsAvailableException("active");
			}

			return this.processRTScriptEntries(scriptEntries);
		} catch (PersistenceException e) {
			throw new ExperimentException();
		}
	}

	public void prepareAllDailyNotActive() throws ExperimentException,
			NoScriptsAvailableException {

		try {
			service.prepareAllDailyNotActive();

		} catch (PersistenceException e) {
			throw new ExperimentException();
		}
	}

	private RTScriptInformation processRTScriptEntry(RTScriptEntry entry) {
		RTScriptInformation script = new RTScriptInformation();
		String experimentName = (experimentService.getExperimentById(entry
				.getExperiment())).getName();
		script.setExperiment(experimentName);
		script.setRt(entry.getRt());
		script.setOperation(entry.getOperation());

		ScriptSlot scriptSlot = new ScriptSlot();
		scriptSlot.setBegin(entry.getBegin());

		scriptSlot.setLength(entry.getEnd().getTime()
				- entry.getBegin().getTime());

		script.setSlot(scriptSlot);
		script.setId(entry.getIdrt_script());

		String status = entry.getStatus();
		this.parseStatus(script, status);

		String type = entry.getType();
		script.setType(ScriptType.valueOf(type));

		script.setRid(entry.getRid());
		script.setUsername(entry.getUser());
		script.setInit(entry.getInit());
		if (entry.getNotify() == 1) {
			script.setNotify(true);
		} else {
			script.setNotify(false);
		}

		List<Integer> rids = service.getAllScriptRids(script.getId());
		if (rids == null) {
			rids = new ArrayList<Integer>();
		}
		script.setRidHistory(rids);

		return script;
	}

	private List<RTScriptInformation> processRTScriptEntries(
			List<RTScriptEntry> entries) {

		List<RTScriptInformation> scripts = null;

		scripts = new ArrayList<RTScriptInformation>();

		for (RTScriptEntry scriptEntry : entries) {
			RTScriptInformation script = this.processRTScriptEntry(scriptEntry);
			scripts.add(script);
		}

		return scripts;
	}

	public void setScriptScheduled(int sid) throws ExperimentException {
		try {
			service.setScriptStatus(sid, "S");

		} catch (PersistenceException e) {
			throw new ExperimentException();
		}
	}

	public void setScriptReady(int sid) throws ExperimentException {
		try {
			service.setScriptStatus(sid, "R");

		} catch (PersistenceException e) {
			throw new ExperimentException();
		}
	}

	public void setScriptDone(int sid) throws ExperimentException {
		try {
			service.setScriptStatus(sid, "D");

		} catch (PersistenceException e) {
			throw new ExperimentException();
		}
	}

	public void setScriptNotifying(int sid) throws ExperimentException {
		try {
			service.setScriptStatus(sid, "N");

		} catch (PersistenceException e) {
			throw new ExperimentException();
		}
	}

	public void setScriptPrepared(int sid) throws ExperimentException {
		try {
			service.setScriptStatus(sid, "P");
			service.setReservation(sid, null);
		} catch (PersistenceException e) {
			throw new ExperimentException();
		}
	}

	public void setScriptTriggered(int sid) throws ExperimentException {
		try {
			service.setScriptStatus(sid, "T");

		} catch (PersistenceException e) {
			throw new ExperimentException();
		}
	}

	public void setScriptWaitingInvoke(int sid)
			throws ExperimentException {
		try {
			service.setScriptStatus(sid, "I");

		} catch (PersistenceException e) {
			throw new ExperimentException();
		}
	}

	public void setScriptWaitingEnd(int sid) throws ExperimentException {
		try {
			service.setScriptStatus(sid, "W");

		} catch (PersistenceException e) {
			throw new ExperimentException();
		}
	}

	public void setScriptError(int sid) throws ExperimentException {
		try {
			service.setScriptStatus(sid, "E");
		} catch (PersistenceException e) {
			throw new ExperimentException();
		}
	}

	public void setReservation(int sid, int rid)
			throws ExperimentException {
		try {
			service.setReservation(sid, rid);
			service.saveRTScriptRid(sid, rid);

		} catch (PersistenceException e) {
			throw new ExperimentException();
		}
	}

}
