package eu.gloria.gs.services.experiment;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.slf4j.Logger;

import eu.gloria.gs.services.experiment.base.contexts.ExperimentContext;
import eu.gloria.gs.services.experiment.base.contexts.ExperimentContextManager;
import eu.gloria.gs.services.experiment.base.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.base.models.InvalidUserContextException;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.operations.NoSuchOperationException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;
import eu.gloria.gs.services.experiment.base.parameters.NoSuchParameterException;
import eu.gloria.gs.services.experiment.base.reservation.ExperimentNotInstantiatedException;
import eu.gloria.gs.services.experiment.base.reservation.NoSuchReservationException;
import eu.gloria.gs.services.experiment.script.data.RTScriptInformation;
import eu.gloria.gs.services.utils.JSONConverter;

public class ScriptTask implements Runnable {

	protected ExperimentContextManager manager;
	protected RTScriptInformation script;
	protected Logger log;

	public void setExperimentContextManager(ExperimentContextManager manager) {
		this.manager = manager;
	}

	public void setScript(RTScriptInformation script) {
		this.script = script;
	}

	public void setLogger(Logger log) {
		this.log = log;
	}

	@Override
	public void run() {
		ExperimentContext context;
		try {
			context = manager.getContext(script.getUsername(), script.getRid());

			@SuppressWarnings("unchecked")
			LinkedHashMap<String, Object> initMap = (LinkedHashMap<String, Object>) JSONConverter
					.fromJSON((String) script.getInit(), Object.class, null);

			for (String paramRoute : initMap.keySet()) {
				context.setParameterValue(paramRoute, initMap.get(paramRoute));
			}

			context.getOperation(script.getOperation()).execute();
		} catch (InvalidUserContextException e) {
			log.error(e.getMessage());
		} catch (ExperimentDatabaseException e) {
			log.error(e.getMessage());
		} catch (NoSuchReservationException e) {
			log.error(e.getMessage());
		} catch (ExperimentOperationException e) {
			log.error(e.getMessage());
		} catch (NoSuchOperationException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (ExperimentParameterException e) {
			log.error(e.getMessage());
		} catch (ExperimentNotInstantiatedException e) {
			log.error(e.getMessage());
		} catch (NoSuchParameterException e) {
			log.error(e.getMessage());
		}
	}

}
