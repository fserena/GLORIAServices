package eu.gloria.gs.services.scheduler;

import java.util.List;

import javax.jws.WebParam;

import eu.gloria.gs.services.core.GSLogProducerService;
import eu.gloria.gs.services.scheduler.brain.InvalidObservingPlanException;
import eu.gloria.gs.services.scheduler.brain.MaxUserSchedulesException;
import eu.gloria.gs.services.scheduler.brain.SchedulerBrain;
import eu.gloria.gs.services.scheduler.data.ObservingPlanInformation;
import eu.gloria.gs.services.scheduler.data.ScheduleInformation;
import eu.gloria.gs.services.scheduler.data.ScheduleNotFoundException;
import eu.gloria.gs.services.scheduler.data.SchedulerAdapter;
import eu.gloria.gs.services.scheduler.data.SchedulerDatabaseException;

public class Scheduler extends GSLogProducerService implements
		SchedulerInterface {

	private SchedulerAdapter adapter;
	private SchedulerBrain brain;

	public SchedulerBrain getBrain() {
		return brain;
	}

	public void setBrain(SchedulerBrain brain) {
		this.brain = brain;
	}

	public Scheduler() {
	}

	public void setAdapter(SchedulerAdapter adapter) {
		this.adapter = adapter;
		this.adapter.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.scheduler.SchedulerInterface#schedule(eu.gloria
	 * .gs.services.scheduler.data.ObservingPlanInformation)
	 */
	@Override
	public int schedule(@WebParam(name = "op") ObservingPlanInformation op)
			throws SchedulerException, MaxUserSchedulesException,
			InvalidObservingPlanException {

		try {
			return this.brain.prepare(op);
		} catch (SchedulerDatabaseException e) {
			throw new SchedulerException(e.getAction());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.scheduler.SchedulerInterface#advertisePlan(java
	 * .lang.String,
	 * eu.gloria.gs.services.scheduler.data.ObservingPlanInformation)
	 */
	@Override
	public int advertisePlan(@WebParam(name = "rt") String rt,
			@WebParam(name = "op") ObservingPlanInformation op) {
		// TODO Auto-generated method stub

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.scheduler.SchedulerInterface#offerPlan(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public void offerPlan(@WebParam(name = "rt") String rt,
			@WebParam(name = "id") String id) throws SchedulerException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.scheduler.SchedulerInterface#getScheduleInformation
	 * (int)
	 */
	@Override
	public ScheduleInformation getScheduleInformation(
			@WebParam(name = "id") int id) throws SchedulerException {

		try {
			return this.adapter.getScheduleInformation(id);
		} catch (SchedulerDatabaseException e) {
			throw new SchedulerException(e.getAction());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.scheduler.SchedulerInterface#getMyActivePlans()
	 */
	@Override
	public List<ScheduleInformation> getMyActivePlans()
			throws SchedulerException {
		try {
			List<ScheduleInformation> schInfos = adapter
					.getActiveSchedulesByUser(this.getClientUsername(), 100);
			return schInfos;
		} catch (SchedulerDatabaseException e) {
			throw new SchedulerException(e.getAction());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.scheduler.SchedulerInterface#getMyInactivePlans()
	 */
	@Override
	public List<ScheduleInformation> getMyInactivePlans()
			throws SchedulerException, ScheduleNotFoundException {

		try {
			List<ScheduleInformation> schInfos = adapter
					.getInactiveSchedulesByUser(this.getClientUsername(), 100);
			return schInfos;
		} catch (SchedulerDatabaseException e) {
			throw new SchedulerException(e.getAction());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.scheduler.SchedulerInterface#getAllMyPlans()
	 */
	@Override
	public List<ScheduleInformation> getAllMyPlans() throws SchedulerException,
			ScheduleNotFoundException {
		try {
			List<ScheduleInformation> schInfos = adapter.getAllSchedulesByUser(
					this.getClientUsername(), 100);
			return schInfos;
		} catch (SchedulerDatabaseException e) {
			throw new SchedulerException(e.getAction());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.scheduler.SchedulerInterface#getAllRTPlans()
	 */
	@Override
	public List<ScheduleInformation> getAllRTPlans(
			@WebParam(name = "rt") String rt) throws SchedulerException,
			ScheduleNotFoundException {
		try {
			List<ScheduleInformation> schInfos = adapter.getSchedulesByRT(rt, 100);
			return schInfos;
		} catch (SchedulerDatabaseException e) {
			throw new SchedulerException(e.getAction());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.scheduler.SchedulerInterface#getAllActivePlans()
	 */
	@Override
	public List<ScheduleInformation> getAllActivePlans()
			throws SchedulerException, ScheduleNotFoundException {
		try {
			List<ScheduleInformation> schInfos = adapter.getAllActiveSchedules();
			return schInfos;
		} catch (SchedulerDatabaseException e) {
			throw new SchedulerException(e.getAction());
		}
	}

	/* (non-Javadoc)
	 * @see eu.gloria.gs.services.scheduler.SchedulerInterface#getActiveRTPlans(java.lang.String)
	 */
	@Override
	public List<ScheduleInformation> getActiveRTPlans(
			@WebParam(name = "rt") String rt) throws SchedulerException,
			ScheduleNotFoundException {
		try {
			List<ScheduleInformation> schInfos = adapter.getActiveSchedulesByRT(rt, 100);
			return schInfos;
		} catch (SchedulerDatabaseException e) {
			throw new SchedulerException(e.getAction());
		}
	}
}
