package eu.gloria.gs.services.experiment.reservation;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import eu.gloria.gs.services.experiment.base.data.TimeSlot;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentReservationArgumentException;
import eu.gloria.gs.services.repository.rt.RTRepositoryException;
import eu.gloria.gs.services.repository.rt.RTRepositoryInterface;
import eu.gloria.gs.services.repository.rt.data.RTAvailability;

public class RTBooker {

	private final static long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000;
	private HashMap<String, RTAvailability> availabilities;
	private RTRepositoryInterface repository;

	public RTBooker() {

		availabilities = new HashMap<>();
	}

	public void setRTRepository(RTRepositoryInterface repository) {
		this.repository = repository;
	}

	private RTAvailability getRTAvailability(String rt)
			throws RTRepositoryException {

		// GSClientProvider.setCredentials("experiment", "3xp3r1m3nt");

		if (!availabilities.containsKey(rt)) {

			RTAvailability availability = null;
			availability = repository.getRTAvailability(rt);

			availabilities.put(rt, availability);
		}

		return availabilities.get(rt);
	}

	public boolean available(List<String> telescopes, TimeSlot timeSlot)
			throws ExperimentReservationArgumentException, RTRepositoryException {

		if (timeSlot != null) {

			if (timeSlot.getBegin().getTime() >= timeSlot.getEnd().getTime()) {
				throw new ExperimentReservationArgumentException(
						"incorrect time slot");
			}

		} else
			throw new ExperimentReservationArgumentException("null time slot");

		if (telescopes == null || telescopes.size() == 0) {
			throw new ExperimentReservationArgumentException(
					"no telescopes specified");
		}

		boolean available = true;

		for (String rt : telescopes) {
			RTAvailability availability = this.getRTAvailability(rt);

			available = available
					&& this.timeSlotFitsIn(timeSlot, availability);

			if (!available)
				break;
		}

		return available;
	}

	private boolean timeSlotFitsIn(TimeSlot timeSlot,
			RTAvailability availability) {

		Date startingTime = availability.getStartingTime();
		Date endingTime = availability.getEndingTime();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(timeSlot.getBegin());
		int beginYear = calendar.get(Calendar.YEAR);

		calendar.setTime(timeSlot.getEnd());
		int endYear = calendar.get(Calendar.YEAR);

		calendar.setTime(startingTime);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		calendar.set(Calendar.YEAR, beginYear);
		int startingHour = calendar.get(Calendar.HOUR_OF_DAY);

		startingTime = calendar.getTime();

		calendar.setTime(endingTime);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		calendar.set(Calendar.YEAR, endYear);
		int endingHour = calendar.get(Calendar.HOUR_OF_DAY);

		if (startingHour <= endingHour) {
			calendar.set(Calendar.DAY_OF_YEAR, 1);
		} else {
			calendar.set(Calendar.DAY_OF_YEAR, 2);
		}

		endingTime = calendar.getTime();

		long availabilitySlot = endingTime.getTime() - startingTime.getTime();

		// If the rt availability time slot is equal to "at any time",
		// no matter the configuration of the time slot, it always fits in.
		if (availabilitySlot >= MILLISECONDS_PER_DAY)
			return true;

		// If the length of the time slot is greater than the actual
		// availability, it does not fit in.
		if ((timeSlot.getEnd().getTime() - timeSlot.getBegin().getTime()) > availabilitySlot)
			return false;

		calendar.setTime(timeSlot.getBegin());
		int beginHour = calendar.get(Calendar.HOUR_OF_DAY);
		int beginDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		beginYear = calendar.get(Calendar.YEAR);
		calendar.setTime(timeSlot.getEnd());
		int endDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		endYear = calendar.get(Calendar.YEAR);

		calendar.setTime(startingTime);
		calendar.set(Calendar.YEAR, beginYear);
		if (beginHour < endingHour) {
			calendar.set(Calendar.DAY_OF_YEAR, beginDayOfYear);
		} else
			calendar.set(Calendar.DAY_OF_YEAR, endDayOfYear);
		startingTime = calendar.getTime();
		calendar.setTime(endingTime);
		calendar.set(Calendar.YEAR, endYear);
		calendar.set(Calendar.DAY_OF_YEAR, endDayOfYear);
		endingTime = calendar.getTime();

		return (timeSlot.getBegin().getTime() >= startingTime.getTime() && timeSlot
				.getEnd().getTime() <= endingTime.getTime());
	}
}
