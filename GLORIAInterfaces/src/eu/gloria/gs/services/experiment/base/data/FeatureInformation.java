package eu.gloria.gs.services.experiment.base.data;

import eu.gloria.gs.services.experiment.base.models.ExperimentFeature;

public class FeatureInformation {

	private String name;
	private ExperimentFeature feature;
	private String[] arguments;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ExperimentFeature getFeature() {
		return feature;
	}

	public void setFeature(ExperimentFeature segment) {
		this.feature = segment;
	}

	public String[] getArguments() {
		return arguments;
	}

	public void setArguments(String[] arguments) {
		this.arguments = arguments;
	}
}
