/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.scheduler;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import eu.gloria.gs.services.scheduler.brain.InvalidObservingPlanException;
import eu.gloria.gs.services.scheduler.brain.MaxUserSchedulesException;
import eu.gloria.gs.services.scheduler.data.ObservingPlanInformation;
import eu.gloria.gs.services.scheduler.data.ScheduleInformation;
import eu.gloria.gs.services.scheduler.data.ScheduleNotFoundException;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
@WebService(name = "SchedulerInterface", targetNamespace = "http://scheduler.services.gs.gloria.eu/")
public interface SchedulerInterface {

	public int schedule(@WebParam(name = "op") ObservingPlanInformation op)
			throws SchedulerException,
			MaxUserSchedulesException, InvalidObservingPlanException;

	public int advertisePlan(@WebParam(name = "rt") String rt,
			@WebParam(name = "op") ObservingPlanInformation op);

	public void offerPlan(@WebParam(name = "rt") String rt,
			@WebParam(name = "id") String id) throws SchedulerException;

	public ScheduleInformation getScheduleInformation(
			@WebParam(name = "id") int id) throws SchedulerException;

	public List<ScheduleInformation> getMyActivePlans()
			throws SchedulerException, ScheduleNotFoundException;
	
	public List<ScheduleInformation> getMyInactivePlans()
			throws SchedulerException, ScheduleNotFoundException;
	
	public List<ScheduleInformation> getAllMyPlans()
			throws SchedulerException, ScheduleNotFoundException;
	
	public List<ScheduleInformation> getAllRTPlans(@WebParam(name = "rt") String rt)
			throws SchedulerException, ScheduleNotFoundException;
	
	public List<ScheduleInformation> getActiveRTPlans(@WebParam(name = "rt") String rt)
			throws SchedulerException, ScheduleNotFoundException;
	
	public List<ScheduleInformation> getAllActivePlans()
			throws SchedulerException, ScheduleNotFoundException;
	
	
}
