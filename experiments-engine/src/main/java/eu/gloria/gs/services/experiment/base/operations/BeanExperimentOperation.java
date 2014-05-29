package eu.gloria.gs.services.experiment.base.operations;

import org.springframework.beans.factory.BeanNameAware;

import eu.gloria.gs.services.experiment.base.operations.ExperimentOperation;

public class BeanExperimentOperation extends ExperimentOperation implements
		BeanNameAware {

	@Override
	public void setBeanName(String name) {
		this.setConcreteName(name);

		if (this.getName() == null) {
			this.setName(name);
		}
	}
}
