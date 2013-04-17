package eu.gloria.gs.services.experiment.online;

import java.util.Date;
import java.util.List;

import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.core.tasks.ServerThread;
import eu.gloria.gs.services.experiment.online.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.online.data.ExperimentRuntimeInformation;
import eu.gloria.gs.services.experiment.online.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.online.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.online.data.ReservationInformation;
import eu.gloria.gs.services.experiment.online.models.ExperimentContext;
import eu.gloria.gs.services.experiment.online.models.ExperimentContextManager;
import eu.gloria.gs.services.experiment.online.models.InvalidUserContextException;
import eu.gloria.gs.services.experiment.online.models.InvalidExperimentModelException;
import eu.gloria.gs.services.experiment.online.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.online.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.online.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.experiment.online.reservation.NoSuchReservationException;
import eu.gloria.gs.services.experiment.online.reservation.NoReservationsAvailableException;
import eu.gloria.gs.services.log.action.ActionLogException;
import eu.gloria.gs.services.log.action.ActionLogInterface;

public class ExperimentExecutor extends ServerThread {

	private ExperimentDBAdapter adapter;
	private ExperimentContextManager manager;
	private ActionLogInterface alog;
	private String username;
	private String password;

	public void setAdapter(ExperimentDBAdapter adapter) {
		this.adapter = adapter;
	}

	public void setContextManager(ExperimentContextManager manager) {
		this.manager = manager;
	}

	public void setActionLog(ActionLogInterface alog) {
		this.alog = alog;
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
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		GSClientProvider.setCredentials(this.username, this.password);

		try {

			List<ReservationInformation> reservations = adapter
					.getAllReservationsActiveNow();

			System.out.println("Active reservations: " + reservations.size());

			for (ReservationInformation reservation : reservations) {

				boolean instantiated = adapter
						.isReservationContextInstantiated(reservation
								.getReservationId());

				ExperimentContext context = manager.getContext(
						reservation.getUser(), reservation.getReservationId());

				if (!instantiated) {
					System.out.println("Trying to instantiate: "
							+ reservation.getReservationId());

					try {
						alog.registerAction(
								username,
								new Date(),
								"Trying to instantiate an online experiment reservation ("
										+ reservation.getReservationId()
										+ ") for user '"
										+ reservation.getUser() + "'");

					} catch (ActionLogException e1) {
						e1.printStackTrace();
					}

					adapter.deleteExperimentContext(reservation
							.getReservationId());

					context.instantiate();

					try {
						alog.registerAction(
								username,
								new Date(),
								"Online experiment reservation sucessfully instantiated ("
										+ reservation.getReservationId()
										+ ") for user '"
										+ reservation.getUser() + "'");

					} catch (ActionLogException e1) {
						e1.printStackTrace();
					}

					try {
						context.init();
					} catch (ExperimentOperationException e) {
						adapter.deleteExperimentContext(reservation
								.getReservationId());

						try {
							alog.registerAction(
									username,
									new Date(),
									"Online experiment reservation cannot be initialized ("
											+ reservation.getReservationId()
											+ ") for user '"
											+ reservation.getUser() + "': "
											+ e.getMessage());

						} catch (ActionLogException e1) {
							e1.printStackTrace();
						}
					}

					try {
						alog.registerAction(
								username,
								new Date(),
								"Online experiment reservation sucessfully initialized ("
										+ reservation.getReservationId()
										+ ") for user '"
										+ reservation.getUser() + "'");

					} catch (ActionLogException e1) {
						e1.printStackTrace();
					}
				} else {

					ExperimentRuntimeInformation runtimeInfo = adapter
							.getExperimentRuntimeContext(reservation
									.getReservationId());

					if (runtimeInfo.getRemainingTime() < 10) {

						System.out.println("Ending: "
								+ reservation.getReservationId()
								+ ", remaining time= "
								+ runtimeInfo.getRemainingTime()
								+ ", elapsed time= "
								+ runtimeInfo.getElapsedTime());
						try {
							context.end();

							try {
								alog.registerAction(
										username,
										new Date(),
										"Online experiment reservation sucessfully terminated ("
												+ reservation
														.getReservationId()
												+ ") for user '"
												+ reservation.getUser() + "'");

							} catch (ActionLogException e1) {
								e1.printStackTrace();
							}

						} catch (ExperimentOperationException e) {
							try {
								alog.registerAction(
										username,
										new Date(),
										"Online experiment reservation cannot finalize ("
												+ reservation
														.getReservationId()
												+ ") for user '"
												+ reservation.getUser() + "': "
												+ e.getMessage());

							} catch (ActionLogException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}

		} catch (InvalidExperimentModelException | ExperimentParameterException
				| ExperimentDatabaseException | InvalidUserContextException
				| NoSuchExperimentException | NoSuchReservationException
				| NullPointerException e) {

			try {

				alog.registerAction(username, new Date(),
						"Error ocurred: " + e.getMessage());
			} catch (ActionLogException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (NoReservationsAvailableException
				| ExperimentNotInstantiatedException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			try {

				alog.registerAction(username, new Date(),
						"Error ocurred: " + e.getMessage());
			} catch (ActionLogException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		try { // Warning: in the future, may be millions of reservations to
				// get // and clear its contexts!!!
			adapter.clearAllObsoleteContexts();
		} catch (ExperimentDatabaseException e) {
			try {
				alog.registerAction(username, new Date(),
						"Error ocurred: " + e.getMessage());
			} catch (ActionLogException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}
}
