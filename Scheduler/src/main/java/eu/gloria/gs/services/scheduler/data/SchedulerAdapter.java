package eu.gloria.gs.services.scheduler.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.gloria.gs.services.scheduler.brain.InvalidObservingPlanException;
import eu.gloria.gs.services.scheduler.data.dbservices.SchedulerDBService;
import eu.gloria.gs.services.scheduler.data.dbservices.ScheduleEntry;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class SchedulerAdapter {

	private SchedulerDBService schedulerService;

	/**
	 * 
	 */
	public SchedulerAdapter() {

	}

	public void init() {

		schedulerService.createSchedule();
	}

	public int prepareSchedule(String user, ObservingPlanInformation opInfo)
			throws SchedulerDatabaseException, InvalidObservingPlanException {
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
			throw new SchedulerDatabaseException();
		}

		int id = -1;

		try {
			id = schedulerService.getLastUserScheduleId(user);
		} catch (Exception e) {
			throw new SchedulerDatabaseException();
		}
		return id;
	}

	public void saveSchedule(String rt, String user, String uuid, Date date)
			throws SchedulerDatabaseException {
		ScheduleEntry entry = new ScheduleEntry();
		entry.setStatus("ADVERTISED");
		entry.setUser(user);
		entry.setTelescope(rt);
		entry.setUuid(uuid);
		entry.setPublish_date(date);

		try {
			schedulerService.save(entry);
		} catch (Exception e) {
			throw new SchedulerDatabaseException();
		}
	}

	public void removeSchedule(int id) throws SchedulerDatabaseException {
		if (!schedulerService.contains(id)) {
			throw new SchedulerDatabaseException();
		}

		schedulerService.remove(id);
	}

	public ScheduleInformation getScheduleInformation(int id)
			throws SchedulerDatabaseException {
		if (!schedulerService.contains(id)) {
			throw new SchedulerDatabaseException();
		}

		ScheduleEntry entry = null;

		try {
			entry = schedulerService.get(id);
		} catch (Exception e) {
			throw new SchedulerDatabaseException();
		}

		ScheduleInformation schInfo = this.buildScheduleInfo(entry);

		return schInfo;
	}

	public ScheduleInformation getScheduleByRTUuid(String rt, String uuid)
			throws SchedulerDatabaseException {

		ScheduleEntry entry = null;
		try {
			if (!schedulerService.containsRTLocalId(rt, uuid)) {
				throw new SchedulerDatabaseException();
			}

			entry = schedulerService.getByRTLocalId(rt, uuid);
		} catch (Exception e) {
			throw new SchedulerDatabaseException();
		}

		ScheduleInformation schInfo = this.buildScheduleInfo(entry);

		return schInfo;
	}

	public int getUserActiveSchedulesCount(String user)
			throws SchedulerDatabaseException {

		int count = 0;
		try {
			count = schedulerService.getUserActiveSchedulesCount(user);
		} catch (Exception e) {
			throw new SchedulerDatabaseException();
		}

		return count;
	}

	@SuppressWarnings("unchecked")
	private ScheduleInformation buildScheduleInfo(ScheduleEntry entry)
			throws SchedulerDatabaseException {
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
			throw new SchedulerDatabaseException();
		}

		try {
			schInfo.setCandidates((List<String>) JSONConverter.fromJSON(
					entry.getCandidates(), List.class, String.class));
		} catch (IOException e) {
			throw new SchedulerDatabaseException();
		}

		try {
			schInfo.setResults((List<ImageResult>) JSONConverter.fromJSON(
					entry.getResults(), List.class, ImageResult.class));
		} catch (IOException e) {
			throw new SchedulerDatabaseException();
		}

		return schInfo;
	}

	public List<ScheduleInformation> getAllSchedulesByUser(String user,
			int limit) throws SchedulerDatabaseException {

		List<ScheduleInformation> schInfos = new ArrayList<ScheduleInformation>();
		List<ScheduleEntry> entries = null;

		try {
			entries = schedulerService.getByUser(user);
		} catch (Exception e) {
			throw new SchedulerDatabaseException();
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
			int limit) throws SchedulerDatabaseException,
			ScheduleNotFoundException {

		List<ScheduleInformation> schInfos = new ArrayList<ScheduleInformation>();
		List<ScheduleEntry> entries = null;

		try {
			entries = schedulerService.getUserInactiveSchedules(user);
		} catch (Exception e) {
			throw new SchedulerDatabaseException();
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
			int limit) throws SchedulerDatabaseException {

		List<ScheduleInformation> schInfos = new ArrayList<ScheduleInformation>();
		List<ScheduleEntry> entries = null;

		try {
			entries = schedulerService.getUserActiveSchedules(user);
		} catch (Exception e) {
			throw new SchedulerDatabaseException();
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
			throws SchedulerDatabaseException {

		List<ScheduleInformation> schInfos = new ArrayList<ScheduleInformation>();
		List<ScheduleEntry> entries = null;

		try {
			entries = schedulerService.getByRT(rt);
		} catch (Exception e) {
			throw new SchedulerDatabaseException();
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
			throws SchedulerDatabaseException {

		List<ScheduleInformation> schInfos = new ArrayList<ScheduleInformation>();
		List<ScheduleEntry> entries = null;

		try {
			entries = schedulerService.getActiveByRT(rt);
		} catch (Exception e) {
			throw new SchedulerDatabaseException();
		}

		if (entries != null) {
			for (ScheduleEntry entry : entries) {
				ScheduleInformation schInfo = this.buildScheduleInfo(entry);
				schInfos.add(schInfo);
			}
		}

		return schInfos;
	}

	public void setRT(int id, String rt) throws SchedulerDatabaseException {
		if (!schedulerService.contains(id)) {
			throw new SchedulerDatabaseException();
		}

		schedulerService.setTelescope(id, rt);
	}

	public void setPlan(int id, ObservingPlanInformation plan)
			throws SchedulerDatabaseException {
		if (!schedulerService.contains(id)) {
			throw new SchedulerDatabaseException();
		}

		try {
			schedulerService.setPlan(id, JSONConverter.toJSON(plan));
		} catch (IOException e) {
			throw new SchedulerDatabaseException();
		}
	}

	public void setUuid(int id, String uuid) throws SchedulerDatabaseException {
		if (!schedulerService.contains(id)) {
			throw new SchedulerDatabaseException();
		}

		schedulerService.setUuid(id, uuid);
	}

	public void setState(int id, String status)
			throws SchedulerDatabaseException {
		if (!schedulerService.contains(id)) {
			throw new SchedulerDatabaseException();
		}

		schedulerService.setStatus(id, status);
	}

	public void setCandidates(int id, List<String> cands)
			throws SchedulerDatabaseException {
		if (!schedulerService.contains(id)) {
			throw new SchedulerDatabaseException();
		}

		try {
			schedulerService.setCandidates(id, JSONConverter.toJSON(cands));
		} catch (IOException e) {
			throw new SchedulerDatabaseException();
		}
	}

	public void setResults(int id, List<ImageResult> res)
			throws SchedulerDatabaseException {
		if (!schedulerService.contains(id)) {
			throw new SchedulerDatabaseException();
		}

		try {
			schedulerService.setResults(id, JSONConverter.toJSON(res));
		} catch (IOException e) {
			throw new SchedulerDatabaseException();
		}
	}

	public void setLastDate(int id, Date ld) throws SchedulerDatabaseException {
		if (!schedulerService.contains(id)) {
			throw new SchedulerDatabaseException();
		}

		schedulerService.setLastDate(id, ld);
	}

	public List<ScheduleInformation> getAllActiveSchedules()
			throws SchedulerDatabaseException {

		List<ScheduleInformation> schInfos = new ArrayList<ScheduleInformation>();
		List<ScheduleEntry> entries = null;

		try {
			entries = schedulerService.getAllActiveSchedules();
		} catch (Exception e) {
			throw new SchedulerDatabaseException();
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
