package eu.gloria.gs.services.experiment.base.contexts;

import java.util.HashMap;

import eu.gloria.gs.services.experiment.base.data.ExperimentDatabaseException;
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
			throws InvalidUserContextException, ExperimentDatabaseException,
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
}
