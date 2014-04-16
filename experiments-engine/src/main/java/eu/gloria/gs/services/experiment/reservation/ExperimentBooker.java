package eu.gloria.gs.services.experiment.reservation;

import eu.gloria.gs.services.experiment.ExperimentException;
import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.ExperimentType;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.data.ReservationInformation;
import eu.gloria.gs.services.experiment.base.data.TimeSlot;
import eu.gloria.gs.services.experiment.base.models.InvalidUserContextException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentReservationArgumentException;
import eu.gloria.gs.services.experiment.base.reservation.MaxReservationTimeException;
import eu.gloria.gs.services.experiment.base.reservation.NoReservationsAvailableException;
import eu.gloria.gs.services.experiment.base.reservation.NoSuchReservationException;
import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.repository.rt.RTRepositoryException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExperimentBooker {

	private final static int MINUTES_FRAME = 15;
	private final static long MILLISECONDS_PER_FRAME = 60 * MINUTES_FRAME * 1000;
	private final static long MILLISECONDS_PER_30MIN = 30 * 60 * 1000;
	private final static int RESERVATION_DAYS = 7;
	private ExperimentDBAdapter adapter;
	private RTBooker rtBooker;

	public ExperimentBooker() {
	}

	public void setRTBooker(RTBooker rtBooker) {
		this.rtBooker = rtBooker;
	}

	public void setAdapter(ExperimentDBAdapter adapter) {
		this.adapter = adapter;
	}

	private void testAndThrowIfNull(Object arg, String message)
			throws ExperimentReservationArgumentException {
		if (arg == null)
			throw new ExperimentReservationArgumentException(message);
	}

	public List<TimeSlot> getAvailableTimeSlots(String experiment,
			List<String> telescopes, boolean adminMode)
			throws ExperimentReservationArgumentException, ExperimentException {

		testAndThrowIfNull(experiment, "experiment name cannot be null");
		testAndThrowIfNull(telescopes, "telescope list name cannot be null");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		int currentMinutes = calendar.get(Calendar.MINUTE);
		int integerFactor = currentMinutes / MINUTES_FRAME;

		calendar.set(Calendar.MINUTE, integerFactor * MINUTES_FRAME);

		Date fromDate = calendar.getTime();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)
				+ RESERVATION_DAYS);
		Date toDate = calendar.getTime();

		List<TimeSlot> timeSlots = new ArrayList<TimeSlot>();

		while (fromDate.before(toDate)) {
			calendar.setTime(fromDate);
			Date endTimeSlotDate = calendar.getTime();
			endTimeSlotDate.setTime(endTimeSlotDate.getTime()
					+ MILLISECONDS_PER_FRAME);

			boolean available = true;

			TimeSlot timeSlot = new TimeSlot();
			timeSlot.setBegin(fromDate);
			timeSlot.setEnd(endTimeSlotDate);

			if (!adminMode) {
				try {
					available = rtBooker.available(telescopes, timeSlot);
				} catch (RTRepositoryException e) {
					throw new ExperimentReservationArgumentException(
							e.getAction());
				}
			}

			if (available) {
				available = !adapter.anyRTReservationBetween(telescopes,
						timeSlot);
			}

			if (available) {
				TimeSlot ts = new TimeSlot();

				ts.setBegin((Date) fromDate.clone());
				ts.setEnd((Date) endTimeSlotDate.clone());
				timeSlots.add(ts);
			}

			fromDate.setTime(fromDate.getTime() + MILLISECONDS_PER_FRAME);
		}

		return timeSlots;
	}

	public synchronized void reserve(String experiment, String username,
			List<String> telescopes, TimeSlot timeSlot, boolean adminMode)
			throws NoReservationsAvailableException,
			ExperimentReservationArgumentException,
			MaxReservationTimeException, ExperimentException {

		testAndThrowIfNull(experiment, "experiment name cannot be null");
		testAndThrowIfNull(telescopes, "telescope list name cannot be null");
		testAndThrowIfNull(username, "username cannot be null");
		testAndThrowIfNull(timeSlot, "reservation time slot cannot be null");

		if (timeSlot.getEnd().before(new Date())) {
			throw new ExperimentReservationArgumentException(
					"cannot make a reservation on the past");
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)
				+ RESERVATION_DAYS);

		if (timeSlot.getEnd().after(calendar.getTime())) {
			throw new ExperimentReservationArgumentException(
					"reservation is not possible beyond " + RESERVATION_DAYS
							+ " days");
		}

		try {
			if (!rtBooker.available(telescopes, timeSlot) && !adminMode)
				throw new ExperimentReservationArgumentException(
						"one or more telescopes are not available");
		} catch (ExperimentReservationArgumentException e) {
		} catch (RTRepositoryException e) {
			throw new ExperimentReservationArgumentException(e.getAction());
		}

		List<ReservationInformation> pendingReservations = null;

		try {
			pendingReservations = adapter.getUserPendingReservations(
					ExperimentType.ONLINE, username);
		} catch (NoReservationsAvailableException e) {
		}

		long msReserved = 0;
		long msReservation = timeSlot.getEnd().getTime()
				- timeSlot.getBegin().getTime();

		if (pendingReservations != null) {
			for (ReservationInformation reservation : pendingReservations) {

				if (reservation.getExperiment().equals(experiment)) {
					TimeSlot reservedTimeSlot = reservation.getTimeSlot();
					msReserved += reservedTimeSlot.getEnd().getTime()
							- reservedTimeSlot.getBegin().getTime();
				}
			}
		}

		long limitMs = MILLISECONDS_PER_30MIN;

		if (adminMode) {
			limitMs = limitMs * 4;
		}

		if (msReserved + msReservation <= limitMs) {

			if (!adapter.anyRTReservationBetween(telescopes, timeSlot)) {
				adapter.makeReservation(experiment, telescopes, username,
						timeSlot);
			} else {
				throw new NoReservationsAvailableException(
						"one or more telescopes are not available");
			}
		} else {
			MaxReservationTimeException ex = new MaxReservationTimeException(
					msReserved, MILLISECONDS_PER_30MIN, username);

			throw ex;
		}
	}

	public void applyFor(String experiment, String username)
			throws ExperimentException, NoReservationsAvailableException,
			NoSuchExperimentException {

		if (adapter.containsExperiment(experiment)) {
			ExperimentType type = adapter.getExperimentType(experiment);

			if (type.equals(ExperimentType.OFFLINE)) {

				List<ReservationInformation> reservations = null;
				try {
					reservations = adapter.getUserReservationsActiveNow(
							ExperimentType.OFFLINE, username);

				} catch (NoReservationsAvailableException e) {
				}

				if (reservations != null) {
					for (ReservationInformation resInfo : reservations) {
						if (resInfo.getExperiment().equals(experiment)) {
							NoReservationsAvailableException ex = new NoReservationsAvailableException(
									"offline");
							ex.getAction().put("cause", "already applied");
							ex.getAction().put("experiment", experiment);

							throw ex;
						}
					}
				}

				Calendar calendar = Calendar.getInstance();
				Date date = new Date();
				calendar.setTime(date);

				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);

				TimeSlot timeSlot = new TimeSlot();
				timeSlot.setBegin(calendar.getTime());

				calendar.set(Calendar.DAY_OF_YEAR,
						calendar.get(Calendar.DAY_OF_YEAR) + 30);
				timeSlot.setEnd(calendar.getTime());
				adapter.makeReservation(experiment, null, username, timeSlot);
			} else {
				throw new NoSuchExperimentException(experiment);
			}
		}
	}

	public void cancelReservation(String username, int reservationId)
			throws NoSuchReservationException, ExperimentException,
			InvalidUserContextException {

		boolean allowCancel = true;

		List<ReservationInformation> reservations;
		try {

			reservations = adapter.getUserReservationsActiveNow(
					ExperimentType.ONLINE, username);

			for (ReservationInformation reservation : reservations) {
				if (reservation.getReservationId() == reservationId) {
					allowCancel = false;
					break;
				}
			}
		} catch (NoReservationsAvailableException e) {
		}

		ReservationInformation reservation;
		reservation = adapter.getReservationInformation(reservationId);

		if (!reservation.getUser().equals(username)) {
			throw new InvalidUserContextException(username, reservationId);
		}

		if (allowCancel) {
			adapter.deleteReservation(reservationId);
		} else {
			Action action = new Action();
			action.put("user", username);
			action.put("cause", "context active");
			throw new NoSuchReservationException(action);
		}
	}
}
