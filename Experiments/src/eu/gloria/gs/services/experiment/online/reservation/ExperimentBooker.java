package eu.gloria.gs.services.experiment.online.reservation;

import eu.gloria.gs.services.experiment.online.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.online.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.online.data.ReservationInformation;
import eu.gloria.gs.services.experiment.online.data.TimeSlot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExperimentBooker {

	private final static long MILLISECONDS_PER_30M = 60 * 30 * 1000;
	private final static long MILLISECONDS_PER_2H = 60 * 60 * 2 * 1000;
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
			List<String> telescopes)
			throws ExperimentReservationArgumentException,
			ExperimentDatabaseException {

		testAndThrowIfNull(experiment, "Experiment name cannot be null");
		testAndThrowIfNull(telescopes, "Telescope list name cannot be null");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		if (calendar.get(Calendar.MINUTE) < 30) {
			calendar.set(Calendar.MINUTE, 0);
		} else {
			calendar.set(Calendar.MINUTE, 30);
		}

		Date fromDate = calendar.getTime();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)
				+ RESERVATION_DAYS);
		Date toDate = calendar.getTime();

		List<TimeSlot> timeSlots = new ArrayList<TimeSlot>();

		while (fromDate.before(toDate)) {
			calendar.setTime(fromDate);
			Date endTimeSlotDate = calendar.getTime();
			endTimeSlotDate.setTime(endTimeSlotDate.getTime()
					+ MILLISECONDS_PER_30M);

			boolean available = true;

			TimeSlot timeSlot = new TimeSlot();
			timeSlot.setBegin(fromDate);
			timeSlot.setEnd(endTimeSlotDate);

			available = rtBooker.available(telescopes, timeSlot);

			if (available) {
				try {
					available = !adapter.anyRTReservationBetween(telescopes,
							timeSlot);
				} catch (ExperimentDatabaseException e) {
					throw e;
				}
			}

			if (available) {
				TimeSlot ts = new TimeSlot();

				ts.setBegin((Date) fromDate.clone());
				ts.setEnd((Date) endTimeSlotDate.clone());
				timeSlots.add(ts);
			}

			fromDate.setTime(fromDate.getTime() + MILLISECONDS_PER_30M);
		}

		return timeSlots;
	}

	public void reserve(String experiment, String username,
			List<String> telescopes, TimeSlot timeSlot)
			throws NoReservationsAvailableException,
			ExperimentReservationArgumentException, MaxReservationTimeException,
			ExperimentDatabaseException {

		testAndThrowIfNull(experiment, "Experiment name cannot be null");
		testAndThrowIfNull(telescopes, "Telescope list name cannot be null");
		testAndThrowIfNull(username, "Username cannot be null");
		testAndThrowIfNull(timeSlot, "Reservation time slot cannot be null");

		if (timeSlot.getEnd().before(new Date())) {
			throw new ExperimentReservationArgumentException(
					"Cannot make a reservation on the past");
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)
				+ RESERVATION_DAYS);

		if (timeSlot.getEnd().after(calendar.getTime())) {
			throw new ExperimentReservationArgumentException(
					"Reservation is not possible beyond " + RESERVATION_DAYS
							+ " days");
		}

		if (!rtBooker.available(telescopes, timeSlot))
			throw new ExperimentReservationArgumentException(
					"The reservation is incompatible with the availability "
							+ "of at least one telescope");

		List<ReservationInformation> pendingReservations = null;

		try {
			pendingReservations = adapter.getUserPendingReservations(username);
		} catch (ExperimentDatabaseException e) {
			throw e;
		} catch (NoReservationsAvailableException e) {
		}

		long msReserved = 0;
		long msReservation = timeSlot.getEnd().getTime()
				- timeSlot.getBegin().getTime();

		if (pendingReservations != null) {
			for (ReservationInformation reservation : pendingReservations) {
				TimeSlot reservedTimeSlot = reservation.getTimeSlot();
				msReserved += reservedTimeSlot.getEnd().getTime()
						- reservedTimeSlot.getBegin().getTime();
			}
		}

		try {
			if (msReserved + msReservation <= MILLISECONDS_PER_2H) {

				if (!adapter.anyRTReservationBetween(telescopes, timeSlot)) {
					adapter.makeReservation(experiment, telescopes, username,
							timeSlot);
				} else {
					throw new NoReservationsAvailableException(
							"No time available for reservation on at least one "
									+ "telescope");
				}
			} else {
				throw new MaxReservationTimeException(
						"The reservation exceeds the amount of time available "
								+ "to the user '" + username + "'");
			}
		} catch (ExperimentDatabaseException e) {
			throw e;
		}
	}

	public void cancelReservation(String username, int reservationId)
			throws NoSuchReservationException,
			ExperimentDatabaseException {

		boolean allowCancel = true;

		List<ReservationInformation> reservations;
		try {

			reservations = adapter.getUserReservationActiveNow(username);

			for (ReservationInformation reservation : reservations) {
				if (reservation.getReservationId() == reservationId) {
					allowCancel = false;
					break;
				}
			}
		} catch (ExperimentDatabaseException e) {
			throw e;
		} catch (NoReservationsAvailableException e) {
		}

		ReservationInformation reservation;
		try {
			reservation = adapter.getReservationInformation(reservationId);

			if (!reservation.getUser().equals(username)) {
				throw new NoSuchReservationException(
						"The reservation that you are trying to cancel is not yours");
			}
		} catch (ExperimentDatabaseException e) {
			throw e;
		} catch (NoSuchReservationException e) {
			throw e;
		}

		if (allowCancel) {
			try {
				adapter.deleteReservation(reservationId);
			} catch (ExperimentDatabaseException e) {
				throw e;
			}
		} else {
			throw new NoSuchReservationException(
					"Cannot cancel a reservation while it is active");
		}
	}
}
