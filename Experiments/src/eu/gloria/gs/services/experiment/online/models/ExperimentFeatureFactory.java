package eu.gloria.gs.services.experiment.online.models;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ExperimentFeatureFactory implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {

		this.applicationContext = applicationContext;

	}

	public Map<String, ExperimentFeature> getAllFeatures() {
		return this.applicationContext.getBeansOfType(ExperimentFeature.class);
	}

	public ExperimentFeature getFeature(String name) {
		return (ExperimentFeature) this.applicationContext.getBean(name);
	}
}
