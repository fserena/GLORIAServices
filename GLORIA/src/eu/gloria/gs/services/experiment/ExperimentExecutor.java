package eu.gloria.gs.services.experiment;

import java.util.Date;
import java.util.List;

import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.core.tasks.ServerThread;
import eu.gloria.gs.services.experiment.base.contexts.ExperimentContext;
import eu.gloria.gs.services.experiment.base.contexts.ExperimentContextManager;
import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.base.data.ExperimentRuntimeInformation;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.data.ReservationInformation;
import eu.gloria.gs.services.experiment.base.models.InvalidExperimentModelException;
import eu.gloria.gs.services.experiment.base.models.InvalidUserContextException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.experiment.base.reservation.NoReservationsAvailableException;
import eu.gloria.gs.services.experiment.base.reservation.NoSuchReservationException;
import eu.gloria.gs.services.log.action.ActionLogException;
import eu.gloria.gs.services.log.action.ActionLogInterface;
import eu.gloria.gs.services.teleoperation.generic.GenericTeleoperationInterface;

public class ExperimentExecutor extends ServerThread {

	private ExperimentDBAdapter adapter;
	private ExperimentContextManager manager;
	private ActionLogInterface alog;
	private String username;
	private String password;
	private GenericTeleoperationInterface genericTeleoperation;

	public void setAdapter(ExperimentDBAdapter adapter) {
		this.adapter = adapter;
	}

	public void setContextManager(ExperimentContextManager manager) {
		this.manager = manager;
	}

	public void setActionLog(ActionLogInterface alog) {
		this.alog = alog;
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

				boolean errorState = false;

				try {
					boolean instantiated = adapter
							.isReservationContextReady(reservation
									.getReservationId());

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

							try {
								alog.registerAction(
										username,
										new Date(),
										"experiments/contexts/instantiate?"
												+ reservation
														.getReservationId()
												+ "&" + reservation.getUser()
												+ "->ERROR");
							} catch (ActionLogException el) {
							}

						}

						if (instantiationDone) {

							try {
								alog.registerAction(
										username,
										new Date(),
										"experiments/contexts/instantiate?"
												+ reservation
														.getReservationId()
												+ "&" + reservation.getUser());
							} catch (ActionLogException e) {
							}

							try {
								context.init();

								adapter.setContextReady(reservation
										.getReservationId());

								try {
									alog.registerAction(
											username,
											new Date(),
											"experiments/contexts/init?"
													+ reservation
															.getReservationId()
													+ "&"
													+ reservation.getUser());
								} catch (ActionLogException e) {
								}
							} catch (ExperimentOperationException e) {

								errorState = true;

								try {
									alog.registerAction(
											username,
											new Date(),
											"experiments/contexts/init?"
													+ reservation
															.getReservationId()
													+ "&"
													+ reservation.getUser()
													+ "->ERROR");
								} catch (ActionLogException el) {
								}
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

								try {
									alog.registerAction(
											username,
											new Date(),
											"experiments/contexts/end?"
													+ reservation
															.getReservationId()
													+ "&"
													+ reservation.getUser());

								} catch (ActionLogException e1) {
								}

							} catch (ExperimentOperationException e) {

								errorState = true;

								try {
									alog.registerAction(
											username,
											new Date(),
											"experiments/contexts/end?"
													+ reservation
															.getReservationId()
													+ "&"
													+ reservation.getUser()
													+ "->ERROR");

								} catch (ActionLogException e1) {
								}
							}
						}
					}
				} catch (Exception e) {

					errorState = true;

					try {

						alog.registerAction(
								username,
								new Date(),
								"experiments/contexts/error?"
										+ reservation.getReservationId()
										+ "->"
										+ e.getMessage().substring(
												0,
												Math.min(e.getMessage()
														.length(), 40)));
					} catch (ActionLogException e1) {
					}
				}

				try {
					if (errorState) {
						adapter.setContextError(reservation.getReservationId());

						adapter.deleteExperimentContext(reservation
								.getReservationId());
					}
				} catch (ExperimentDatabaseException e) {

				}
			}
		}

		try { // Warning: in the future, may be millions of reservations to
				// get // and clear its contexts!!!
			adapter.clearAllObsoleteContexts();
		} catch (ExperimentDatabaseException e) {
			try {
				alog.registerAction(username, new Date(),
						"experiments/contexts/clearObsolete->ERROR");
			} catch (ActionLogException e1) {
			}
		}

	}
}
