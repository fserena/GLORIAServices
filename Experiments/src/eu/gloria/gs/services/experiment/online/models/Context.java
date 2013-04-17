package eu.gloria.gs.services.experiment.online.models;

import eu.gloria.gs.services.experiment.online.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.online.parameters.ExperimentParameterException;

public abstract class Context {

	public abstract void instantiate() throws ExperimentParameterException,
			NoSuchExperimentException;
}
