package eu.gloria.gs.services.scheduler.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.scheduler.brain.InvalidObservingPlanException;
import eu.gloria.gs.services.scheduler.data.dbservices.SchedulerDBService;
import eu.gloria.gs.services.scheduler.data.dbservices.ScheduleEntry;
import eu.gloria.gs.services.utils.JSONConverter;
import eu.gloria.gs.services.utils.LoggerEntity;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class SchedulerAdapter extends LoggerEntity {

	private SchedulerDBService schedulerService;

	/**
	 * 
	 */
	public SchedulerAdapter() {
		super(SchedulerAdapter.class.getSimpleName());
	}

	public void init() {

		schedulerService.createSchedule();
	}

	public int prepareSchedule(String user, ObservingPlanInformation opInfo)
			throws ActionException, InvalidObservingPlanException {
		ScheduleEntry entry = new ScheduleEntry();
		entry.setStatus("PREPARED");
		entry.setUser(user);
		entry.setTelescope("NONE");
		entry.setUuid(null);
		entry.setPublish_date(new Date());
		try {
			entry.setPlan(JSONConverter.toJSON(opInfo));
		} catch (IOException e) {
			throw new InvalidObservingPlanException();
		}

		try {
			schedulerService.save(entry);
		} catch (Exception e) {
			throw new ActionException();
		}

		int id = -1;

		try {
			id = schedulerService.getLastUserScheduleId(user);
		} catch (Exception e) {
			throw new ActionException();
		}
		return id;
	}

	public void saveSchedule(String rt, String user, String uuid, Date date)
			throws ActionException {
		ScheduleEntry entry = new ScheduleEntry();
		entry.setStatus("ADVERTISED");
		entry.setUser(user);
		entry.setTelescope(rt);
		entry.setUuid(uuid);
		entry.setPublish_date(date);

		try {
			schedulerService.save(entry);
		} catch (Exception e) {
			throw new ActionException();
		}
	}

	public void removeSchedule(int id) throws ActionException {
		if (!schedulerService.contains(id)) {
			throw new ActionException();
		}

		schedulerService.remove(id);
	}

	public ScheduleInformation getScheduleInformation(int id)
			throws ActionException {
		if (!schedulerService.contains(id)) {
			throw new ActionException();
		}

		ScheduleEntry entry = null;

		try {
			entry = schedulerService.get(id);
		} catch (Exception e) {
			throw new ActionException();
		}

		ScheduleInformation schInfo = this.buildScheduleInfo(entry);

		return schInfo;
	}

	public ScheduleInformation getScheduleByRTUuid(String rt, String uuid)
			throws ActionException {

		ScheduleEntry entry = null;
		try {
			if (!schedulerService.containsRTLocalId(rt, uuid)) {
				throw new ActionException();
			}

			entry = schedulerService.getByRTLocalId(rt, uuid);
		} catch (Exception e) {
			throw new ActionException();
		}

		ScheduleInformation schInfo = this.buildScheduleInfo(entry);

		return schInfo;
	}

	public int getUserActiveSchedulesCount(String user) throws ActionException {

		int count = 0;
		try {
			count = schedulerService.getUserActiveSchedulesCount(user);
		} catch (Exception e) {
			throw new ActionException();
		}

		return count;
	}

	@SuppressWarnings("unchecked")
	private ScheduleInformation buildScheduleInfo(ScheduleEntry entry)
			throws ActionException {
		ScheduleInformation schInfo = new ScheduleInformation();
		schInfo.setId(entry.getIdschedule());
		schInfo.setLastDate(entry.getLast_date());
		schInfo.setRt(entry.getTelescope());
		schInfo.setState(entry.getStatus());
		schInfo.setUuid(entry.getUuid());
		schInfo.setUser(entry.getUser());
		schInfo.setPublishDate(entry.getPublish_date());

		try {
			schInfo.setOpInfo((ObservingPlanInformation) JSONConverter
					.fromJSON(entry.getPlan(), ObservingPlanInformation.class,
							null));

			schInfo.getOpInfo().setPriority(100);

		} catch (IOException e) {
			throw new ActionException();
		}

		try {
			schInfo.setCandidates((List<String>) JSONConverter.fromJSON(
					entry.getCandidates(), List.class, String.class));
		} catch (IOException e) {
			throw new ActionException();
		}

		try {
			schInfo.setResults((List<ImageResult>) JSONConverter.fromJSON(
					entry.getResults(), List.class, ImageResult.class));
		} catch (IOException e) {
			throw new ActionException();
		}

		return schInfo;
	}

	public List<ScheduleInformation> getAllSchedulesByUser(String user,
			int limit) throws ActionException {

		List<ScheduleInformation> schInfos = new ArrayList<ScheduleInformation>();
		List<ScheduleEntry> entries = null;

		try {
			entries = schedulerService.getByUser(user);
		} catch (Exception e) {
			throw new ActionException();
		}

		if (entries != null) {
			for (ScheduleEntry entry : entries) {
				ScheduleInformation schInfo = this.buildScheduleInfo(entry);
				schInfos.add(schInfo);
			}
		}

		return schInfos;
	}

	public List<ScheduleInformation> getInactiveSchedulesByUser(String user,
			int limit) throws ActionException, ScheduleNotFoundException {

		List<ScheduleInformation> schInfos = new ArrayList<ScheduleInformation>();
		List<ScheduleEntry> entries = null;

		try {
			entries = schedulerService.getUserInactiveSchedules(user);
		} catch (Exception e) {
			throw new ActionException();
		}

		if (entries != null) {
			for (ScheduleEntry entry : entries) {
				ScheduleInformation schInfo = this.buildScheduleInfo(entry);
				schInfos.add(schInfo);
			}
		}

		return schInfos;
	}

	public List<ScheduleInformation> getActiveSchedulesByUser(String user,
			int limit) throws ActionException {

		List<ScheduleInformation> schInfos = new ArrayList<ScheduleInformation>();
		List<ScheduleEntry> entries = null;

		try {
			entries = schedulerService.getUserActiveSchedules(user);
		} catch (Exception e) {
			throw new ActionException();
		}

		if (entries != null) {
			for (ScheduleEntry entry : entries) {
				ScheduleInformation schInfo = this.buildScheduleInfo(entry);
				schInfos.add(schInfo);
			}
		}

		return schInfos;
	}

	public List<ScheduleInformation> getSchedulesByRT(String rt, int limit)
			throws ActionException {

		List<ScheduleInformation> schInfos = new ArrayList<ScheduleInformation>();
		List<ScheduleEntry> entries = null;

		try {
			entries = schedulerService.getByRT(rt);
		} catch (Exception e) {
			throw new ActionException();
		}

		if (entries != null) {
			for (ScheduleEntry entry : entries) {
				ScheduleInformation schInfo = this.buildScheduleInfo(entry);
				schInfos.add(schInfo);
			}
		}

		return schInfos;
	}

	public List<ScheduleInformation> getActiveSchedulesByRT(String rt, int limit)
			throws ActionException {

		List<ScheduleInformation> schInfos = new ArrayList<ScheduleInformation>();
		List<ScheduleEntry> entries = null;

		try {
			entries = schedulerService.getActiveByRT(rt);
		} catch (Exception e) {
			throw new ActionException();
		}

		if (entries != null) {
			for (ScheduleEntry entry : entries) {
				ScheduleInformation schInfo = this.buildScheduleInfo(entry);
				schInfos.add(schInfo);
			}
		}

		return schInfos;
	}

	public void setRT(int id, String rt) throws ActionException {
		if (!schedulerService.contains(id)) {
			throw new ActionException();
		}

		schedulerService.setTelescope(id, rt);
	}

	public void setPlan(int id, ObservingPlanInformation plan)
			throws ActionException {
		if (!schedulerService.contains(id)) {
			throw new ActionException();
		}

		try {
			schedulerService.setPlan(id, JSONConverter.toJSON(plan));
		} catch (IOException e) {
			throw new ActionException();
		}
	}

	public void setUuid(int id, String uuid) throws ActionException {
		if (!schedulerService.contains(id)) {
			throw new ActionException();
		}

		schedulerService.setUuid(id, uuid);
	}

	public void setState(int id, String status) throws ActionException {
		if (!schedulerService.contains(id)) {
			throw new ActionException();
		}

		schedulerService.setStatus(id, status);
	}

	public void setCandidates(int id, List<String> cands)
			throws ActionException {
		if (!schedulerService.contains(id)) {
			throw new ActionException();
		}

		try {
			schedulerService.setCandidates(id, JSONConverter.toJSON(cands));
		} catch (IOException e) {
			throw new ActionException();
		}
	}

	public void setResults(int id, List<ImageResult> res)
			throws ActionException {
		if (!schedulerService.contains(id)) {
			throw new ActionException();
		}

		try {
			schedulerService.setResults(id, JSONConverter.toJSON(res));
		} catch (IOException e) {
			throw new ActionException();
		}
	}

	public void setLastDate(int id, Date ld) throws ActionException {
		if (!schedulerService.contains(id)) {
			throw new ActionException();
		}

		schedulerService.setLastDate(id, ld);
	}

	public List<ScheduleInformation> getAllActiveSchedules()
			throws ActionException {

		List<ScheduleInformation> schInfos = new ArrayList<ScheduleInformation>();
		List<ScheduleEntry> entries = null;

		try {
			entries = schedulerService.getAllActiveSchedules();
		} catch (Exception e) {
			throw new ActionException(e.getMessage());
		}

		if (entries != null) {
			for (ScheduleEntry entry : entries) {
				ScheduleInformation schInfo = this.buildScheduleInfo(entry);
				schInfos.add(schInfo);
			}
		}

		return schInfos;
	}

	public void setSchedulerDBService(SchedulerDBService service) {
		this.schedulerService = service;
	}
}
