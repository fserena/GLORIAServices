package eu.gloria.gs.services.experiment.base.contexts;

import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.NoSuchExperimentException;
import eu.gloria.gs.services.experiment.base.parameters.ExperimentParameterException;

public abstract class Context {

	private ExperimentDBAdapter adapter;
	
	public abstract void instantiate() throws ExperimentParameterException,
			NoSuchExperimentException;

	public ExperimentDBAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(ExperimentDBAdapter adapter) {
		this.adapter = adapter;
	}
}
