package eu.gloria.gs.services.experiment.base.contexts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eu.gloria.gs.services.experiment.ExperimentException;
import eu.gloria.gs.services.experiment.base.models.InvalidUserContextException;
import eu.gloria.gs.services.experiment.base.reservation.NoSuchReservationException;

public class ExperimentContextManager {

	private HashMap<Integer, ExperimentContext> contexts = null;
	private ExperimentContextFactory factory;

	public ExperimentContextManager() {
		contexts = new HashMap<Integer, ExperimentContext>();
	}

	public void setContextFactory(ExperimentContextFactory factory) {
		this.factory = factory;
	}

	public ExperimentContext getContext(String username, int rid)
			throws InvalidUserContextException, ExperimentException,
			NoSuchReservationException {
		ExperimentContext context = null;

		synchronized (contexts) {
			if (!contexts.containsKey(rid)) {
				context = factory.createExperimentContext(username, rid);
				contexts.put(rid, context);
			}

			context = contexts.get(rid);
		}

		return context;
	}

	public void deleteExperimentContexts(String experiment) {

		List<Integer> ids = new ArrayList<Integer>();

		synchronized (contexts) {

			for (Integer rid : contexts.keySet()) {
				if (contexts.get(rid).getExperimentName().equals(experiment)) {
					ids.add(rid);
				}
			}

			for (Integer rid : ids) {
				contexts.remove(rid);
			}
		}
	}
}
