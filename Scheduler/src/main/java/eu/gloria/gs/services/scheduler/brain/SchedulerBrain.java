/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.scheduler.brain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.gloria.gs.services.repository.image.ImageRepositoryException;
import eu.gloria.gs.services.repository.image.ImageRepositoryInterface;
import eu.gloria.gs.services.repository.image.data.ImageTargetData;
import eu.gloria.gs.services.repository.rt.RTRepositoryException;
import eu.gloria.gs.services.repository.rt.RTRepositoryInterface;
import eu.gloria.gs.services.repository.rt.data.RTCredentials;
import eu.gloria.gs.services.repository.rt.data.RTInformation;
import eu.gloria.gs.services.repository.rt.data.ServerKeyData;
import eu.gloria.gs.services.scheduler.SchedulerException;
import eu.gloria.gs.services.scheduler.data.ImageResult;
import eu.gloria.gs.services.scheduler.data.OPImageData;
import eu.gloria.gs.services.scheduler.data.ObservingPlanInformation;
import eu.gloria.gs.services.scheduler.data.ScheduleInformation;
import eu.gloria.gs.services.scheduler.data.SchedulerAdapter;
import eu.gloria.gs.services.scheduler.data.SchedulerDatabaseException;
import eu.gloria.gs.services.scheduler.local.EmptySchFilterResultException;
import eu.gloria.gs.services.scheduler.local.GenericSchException;
import eu.gloria.gs.services.scheduler.local.SchHandler;
import eu.gloria.gs.services.scheduler.local.SchServerManager;
import eu.gloria.gs.services.scheduler.local.SchServerNotAvailableException;
import eu.gloria.gs.services.scheduler.op.ObservingPlan;
import eu.gloria.gs.services.utils.JSONConverter;
import eu.gloria.rt.entity.db.File;
import eu.gloria.rt.entity.db.FileFormat;
import eu.gloria.rt.entity.db.Format;
import eu.gloria.rt.entity.scheduler.PlanInfo;
import eu.gloria.rt.entity.scheduler.PlanState;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class SchedulerBrain {

	private SchedulerAdapter adapter;
	private RTRepositoryInterface rtRepository;
	private ImageRepositoryInterface imageRepository;
	private static Random rand = new Random();
	private Logger log = LoggerFactory.getLogger(SchedulerBrain.class
			.getSimpleName());

	public int prepare(ObservingPlanInformation op) throws SchedulerException,
			SchedulerDatabaseException, MaxUserSchedulesException,
			InvalidObservingPlanException {

		String user = op.getUser();

		if (this.adapter.getUserActiveSchedulesCount(user) >= 5) {
			log.error(user + " has reached its maximum active plans");
			throw new MaxUserSchedulesException();
		}

		int id = this.adapter.prepareSchedule(user, op);
		try {
			log.info("Plan prepared for " + user + ": "
					+ JSONConverter.toJSON(op));
		} catch (IOException e) {
		}
		return id;
	}

	private String selectRTCandidate(List<String> exclude)
			throws SchedulerException {
		List<String> rts = null;
		try {
			rts = this.rtRepository.getAllBatchRTs();
		} catch (RTRepositoryException e) {
			throw new SchedulerException();
		}

		String rt = null;

		if (rts != null) {
			if (exclude != null) {
				rts.removeAll(exclude);
			}

			if (rts.size() > 0) {
				rt = rts.get(rand.nextInt(rts.size()));

			}
		}

		return rt;
	}

	private void treatPreparedSchedule(ScheduleInformation schInfo)
			throws SchedulerException, RTRepositoryException {

		log.info("Advertising plan " + schInfo.getId() + "...");

		List<String> candidates = schInfo.getCandidates();
		if (candidates == null) {
			candidates = new ArrayList<>();
		}

		String candidate = this.selectRTCandidate(schInfo.getCandidates());
		if (candidate != null) {
			log.info(candidate + " selected as candidate for plan "
					+ schInfo.getId());
		} else {
			log.info("No candidate selected for plan " + schInfo.getId());
		}

		if (candidate != null) {

			RTInformation rtInfo = this.rtRepository
					.getRTInformation(candidate);
			String password = rtInfo.getPassword();
			String username = rtInfo.getUser();

			ServerKeyData keyData = new ServerKeyData();
			keyData.setUrl(rtInfo.getUrl());
			keyData.setPort(rtInfo.getPort());
			RTCredentials credentials = new RTCredentials();
			credentials.setPassword(password);
			credentials.setUser(username);
			keyData.setCredentials(credentials);

			boolean rejection = false;

			try {
				SchHandler schHandler = SchServerManager.getReference().getSch(
						keyData);

				candidates.add(candidate);
				this.adapter.setCandidates(schInfo.getId(), candidates);

				ObservingPlan plan = new ObservingPlan();
				plan.setOpInfo(schInfo.getOpInfo());
				plan.setHandler(schHandler);

				plan.build();
				plan.advertise();

				log.debug("uuid " + schInfo.getOpInfo().getUuid()
						+ " obtained for plan " + schInfo.getId());
				log.info("Plan " + schInfo.getId() + " ADVERTISED to "
						+ candidate);
				this.adapter.setUuid(schInfo.getId(), schInfo.getOpInfo()
						.getUuid());
				this.adapter.setRT(schInfo.getId(), candidate);
				this.adapter.setPlan(schInfo.getId(), schInfo.getOpInfo());
				this.adapter.setLastDate(schInfo.getId(), new Date());
				this.adapter.setState(schInfo.getId(), "ADVERTISED");
			} catch (SchedulerDatabaseException e) {
				throw new SchedulerException();
			} catch (SchServerNotAvailableException | GenericSchException e) {
				log.error(candidate + " returned an error: " + e.getMessage());
			} catch (Exception e) {
				if (!e.getMessage().contains("already")) {
					throw e;
				} else {
					log.warn(candidate
							+ " said it has this OP already registered");
					rejection = true;
				}
			}

			if (rejection) {
				log.error(candidate + " rejected the plan " + schInfo.getId());
				try {
					this.adapter.setState(schInfo.getId(), "REJECTED");
				} catch (SchedulerDatabaseException e) {
					throw new SchedulerException();
				}
			}
		} else {

			log.error("Plan " + schInfo.getId()
					+ " is impossible by now. There are no candidates.");

			try {
				this.adapter.setState(schInfo.getId(), "IMPOSSIBLE");
			} catch (SchedulerDatabaseException e) {
				throw new SchedulerException();
			}
		}
	}

	private void treatRejectedSchedule(ScheduleInformation schInfo)
			throws SchedulerException, RTRepositoryException {

		List<String> candidates = schInfo.getCandidates();
		if (candidates == null) {
			candidates = new ArrayList<>();
		}

		if (candidates.size() >= 5) {
			log.error("Max candidates reached. Plan " + schInfo.getId()
					+ " is impossible by now");
			try {
				this.adapter.setState(schInfo.getId(), "IMPOSSIBLE");
			} catch (SchedulerDatabaseException e) {
				throw new SchedulerException();
			}
		} else {
			log.info("Preparing to request the plan " + schInfo.getId()
					+ " to another telescope...");
			try {
				this.adapter.setState(schInfo.getId(), "PREPARED");
			} catch (SchedulerDatabaseException e) {
				throw new SchedulerException();
			}
		}
	}

	private void treatAdvertisedSchedule(ScheduleInformation schInfo)
			throws RTRepositoryException, SchedulerException {

		String uuid = schInfo.getUuid();
		String rt = schInfo.getRt();

		RTInformation rtInfo = this.rtRepository.getRTInformation(rt);
		String password = rtInfo.getPassword();
		String username = rtInfo.getUser();

		ServerKeyData keyData = new ServerKeyData();
		keyData.setUrl(rtInfo.getUrl());
		keyData.setPort(rtInfo.getPort());
		RTCredentials credentials = new RTCredentials();
		credentials.setPassword(password);
		credentials.setUser(username);
		keyData.setCredentials(credentials);

		try {
			SchHandler schHandler = SchServerManager.getReference().getSch(
					keyData);

			/*log.info("Asking " + schInfo.getRt() + " about ADVERTISED plan "
					+ schInfo.getId() + "...");*/
			
			PlanInfo planInfo = null;
			try {
				planInfo = schHandler.getOPInformationByUuid(uuid);

				PlanState planState = planInfo.getStateInfo().getState();

				if (planState.equals(PlanState.REJECTED)
						|| planState.equals(PlanState.QUEUED)
						|| planState.equals(PlanState.RUNNING)
						|| planState.equals(PlanState.ERROR)) {

					log.info("Previously ADVERTISED plan " + schInfo.getId()
							+ " is now " + planState.name());

					this.adapter.setState(schInfo.getId(), planState.name());
					this.adapter.setLastDate(schInfo.getId(), new Date());
				}

			} catch (EmptySchFilterResultException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SchedulerDatabaseException e) {
			throw new SchedulerException();
		} catch (SchServerNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GenericSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void treatQueuedSchedule(ScheduleInformation schInfo)
			throws RTRepositoryException, SchedulerException {
		String uuid = schInfo.getUuid();
		String rt = schInfo.getRt();

		RTInformation rtInfo = this.rtRepository.getRTInformation(rt);
		String password = rtInfo.getPassword();
		String username = rtInfo.getUser();

		ServerKeyData keyData = new ServerKeyData();
		keyData.setUrl(rtInfo.getUrl());
		keyData.setPort(rtInfo.getPort());
		RTCredentials credentials = new RTCredentials();
		credentials.setPassword(password);
		credentials.setUser(username);
		keyData.setCredentials(credentials);

		try {

			SchHandler schHandler = SchServerManager.getReference().getSch(
					keyData);

			/*log.info("Asking " + schInfo.getRt() + " about QUEUED plan "
					+ schInfo.getId() + "...");*/

			PlanInfo planInfo = null;
			try {
				planInfo = schHandler.getOPInformationByUuid(uuid);

				PlanState planState = planInfo.getStateInfo().getState();

				if (planState.equals(PlanState.ABORTED)
						|| planState.equals(PlanState.DONE)
						|| planState.equals(PlanState.ERROR)) {

					log.info("Previously QUEUED plan " + schInfo.getId()
							+ " is now " + planState.name());

					schInfo.setState(planState.name());
					this.adapter.setState(schInfo.getId(), planState.name());
					this.adapter.setLastDate(schInfo.getId(), new Date());
				}

				if (planState.equals(PlanState.DONE)) {

					log.info("Plan " + schInfo.getId() + " is DONE!");

					List<File> files = schHandler.getOPResultFiles(schInfo
							.getUuid());

					List<ImageResult> results = new ArrayList<>();

					if (files != null) {

						for (File file : files) {
							ImageResult result = new ImageResult();
							result.setDate(file.getDate().toGregorianCalendar()
									.getTime());
							result.setUuid(file.getUuid());
							List<OPImageData> images = new ArrayList<>();

							result.setImages(images);

							try {

								ImageTargetData target = new ImageTargetData();
								target.setDec(schInfo.getOpInfo().getDec());
								target.setRa(schInfo.getOpInfo().getRa());
								target.setObject(schInfo.getOpInfo()
										.getObject());

								imageRepository.saveImage(schInfo.getUser(),
										schInfo.getRt(), "unknown", file
												.getUuid(), target, schInfo
												.getOpInfo().getExposure());
							} catch (ImageRepositoryException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							for (Format format : file.getFormats()) {
								OPImageData data = new OPImageData();
								data.setFormat(format.getFileFormat().name());
								data.setUrl(format.getUrl());
								images.add(data);

								try {
									if (format.getFileFormat().equals(
											FileFormat.JPG)) {

										imageRepository
												.setJpgByRTLocalId(
														schInfo.getRt(),
														file.getUuid(),
														format.getUrl());
									} else {
										imageRepository
												.setFitsByRTLocalId(
														schInfo.getRt(),
														file.getUuid(),
														format.getUrl());
									}
								} catch (ImageRepositoryException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

							results.add(result);
						}

						this.adapter.setResults(schInfo.getId(), results);
					}
				}

			} catch (EmptySchFilterResultException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SchedulerDatabaseException e) {
			throw new SchedulerException();
		} catch (SchServerNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GenericSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void refreshPlans() throws SchedulerException,
			SchedulerDatabaseException, RTRepositoryException {

		List<ScheduleInformation> schedules = this.adapter
				.getAllActiveSchedules();

		for (ScheduleInformation schedule : schedules) {
			if (schedule.getState().equals("REJECTED")) {
				this.treatRejectedSchedule(schedule);
			} else if (schedule.getState().equals("PREPARED")) {
				this.treatPreparedSchedule(schedule);
			} else if (schedule.getState().equals("ADVERTISED")) {
				this.treatAdvertisedSchedule(schedule);
			} else if (schedule.getState().equals("RUNNING")
					|| schedule.getState().equals("QUEUED")) {
				this.treatQueuedSchedule(schedule);
			}
		}
	}

	public SchedulerAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(SchedulerAdapter adapter) {
		this.adapter = adapter;
	}

	public RTRepositoryInterface getRt() {
		return rtRepository;
	}

	public void setRt(RTRepositoryInterface repository) {
		this.rtRepository = repository;
	}

	public ImageRepositoryInterface getImages() {
		return imageRepository;
	}

	public void setImages(ImageRepositoryInterface images) {
		this.imageRepository = images;
	}
}
