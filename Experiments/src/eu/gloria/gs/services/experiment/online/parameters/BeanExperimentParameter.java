package eu.gloria.gs.services.experiment.online.parameters;

import org.springframework.beans.factory.BeanNameAware;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public class BeanExperimentParameter extends ExperimentParameter implements
		BeanNameAware {

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
	 */
	@Override
	public void setBeanName(String name) {
		this.setConcreteName(name);
		
		if (this.getName() == null) {
			this.setName(name);
		}
	}
}
