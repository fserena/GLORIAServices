package eu.gloria.gs.services.experiment.online.operations;

import org.springframework.beans.factory.BeanNameAware;

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
