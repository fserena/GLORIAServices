package eu.gloria.gs.services.experiment;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.gloria.gs.services.core.ErrorLogEntry;
import eu.gloria.gs.services.core.InfoLogEntry;
import eu.gloria.gs.services.core.LogEntry;
import eu.gloria.gs.services.core.LogStore;
import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.core.tasks.ServerThread;
import eu.gloria.gs.services.experiment.base.contexts.ExperimentContext;
import eu.gloria.gs.services.experiment.base.contexts.ExperimentContextManager;
import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.base.data.ExperimentRuntimeInformation;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.data.ReservationInformation;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.NoReservationsAvailableException;
import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.teleoperation.generic.GenericTeleoperationInterface;
import eu.gloria.gs.services.utils.JSONConverter;

public class ExperimentExecutor extends ServerThread {

	private ExperimentDBAdapter adapter;
	private ExperimentContextManager manager;
	private LogStore logStore;
	private String username;
	private String password;
	private GenericTeleoperationInterface genericTeleoperation;
	protected Logger log = LoggerFactory.getLogger(ExperimentExecutor.class
			.getSimpleName());

	public void setAdapter(ExperimentDBAdapter adapter) {
		this.adapter = adapter;
	}

	public void setContextManager(ExperimentContextManager manager) {
		this.manager = manager;
	}

	public void setLogStore(LogStore logStore) {
		this.logStore = logStore;
	}

	public void setGenericTeleoperation(GenericTeleoperationInterface gt) {
		this.genericTeleoperation = gt;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	protected void doWork() {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		GSClientProvider.setCredentials(this.username, this.password);
		List<ReservationInformation> reservations = null;

		try {

			reservations = adapter.getAllReservationsActiveNow();
		} catch (ExperimentDatabaseException | NoReservationsAvailableException e) {
		} catch (Exception e) {
		}

		if (reservations != null) {
			for (ReservationInformation reservation : reservations) {

				int reservationId = reservation.getReservationId();

				LogAction action = new LogAction();
				action.put("sender", "experiments daemon");

				boolean errorState = false;
				boolean newInstance = false;

				try {
					boolean instantiated = adapter
							.isReservationContextReady(reservationId);

					action.put("rid", reservationId);
					action.put("client", reservation.getUser());

					if (!instantiated) {

						adapter.deleteExperimentContext(reservation
								.getReservationId());

						ExperimentContext context = manager.getContext(
								reservation.getUser(),
								reservation.getReservationId());

						boolean instantiationDone = false;

						// Notify all telescopes the teleoperation timeslot
						List<String> telescopes = reservation.getTelescopes();

						if (telescopes != null && telescopes.size() > 0) {

							long duration = (reservation.getTimeSlot().getEnd()
									.getTime() - reservation.getTimeSlot()
									.getBegin().getTime()) / 1000;

							for (String rt : telescopes) {
								genericTeleoperation.notifyTeleoperation(rt,
										duration);
							}
						}

						try {
							context.instantiate();
							instantiationDone = true;
						} catch (NoSuchExperimentException
								| ExperimentParameterException e) {

							errorState = true;
							action.put("init", "failed");
							action.put("details", e.getAction());
						}

						if (instantiationDone) {

							action.put("instance", "created");

							try {
								context.init();
								action.put("init", "done");
								newInstance = true;

								adapter.setContextReady(reservation
										.getReservationId());

							} catch (ExperimentOperationException e) {

								errorState = true;
								action.put("init", "failed");
								action.put("details", e.getAction());
							}
						}

					} else {

						ExperimentRuntimeInformation runtimeInfo = adapter
								.getExperimentRuntimeContext(reservation
										.getReservationId());

						if (runtimeInfo.getRemainingTime() <= 1) {
							ExperimentContext context = manager.getContext(
									reservation.getUser(),
									reservation.getReservationId());
							try {
								context.end();

								action.put("end", "success");

							} catch (ExperimentOperationException e) {

								errorState = true;
								action.put("end", "failed");
								action.put("details", e.getAction());
							}
						}
					}
				} catch (Exception e) {

					errorState = true;
					action.put("grave", e.getClass().getSimpleName());
				}

				try {
					if (errorState) {
						adapter.setContextError(reservation.getReservationId());

						adapter.deleteExperimentContext(reservation
								.getReservationId());

						this.logError(username, reservationId, action);
					} else {
						if (newInstance)
							this.logInfo(username, reservationId, action);
					}
				} catch (ExperimentDatabaseException e) {
					action.put("cause", "internal error");
					action.put("details", e.getAction());
					this.logError(username, reservationId, action);
				}
			}
		}

		try { // Warning: in the future, may be millions of reservations to
				// get // and clear its contexts!!!
			adapter.clearAllObsoleteContexts();
		} catch (ExperimentDatabaseException e) {
			LogAction action = new LogAction();
			action.put("operation", "clear all obsolete contexts");
			action.put("sender", "experiments daemon");
			action.put("cause", "internal error");
			this.logError(username, null, action);
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
