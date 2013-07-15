package eu.gloria.gs.services.experiment.base.contexts;

import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;

public abstract class Context {

	public abstract void instantiate() throws ExperimentParameterException,
			NoSuchExperimentException;
}
