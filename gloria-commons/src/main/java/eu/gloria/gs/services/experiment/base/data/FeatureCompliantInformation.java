package eu.gloria.gs.services.experiment.base.data;

import java.util.Map;

public class FeatureCompliantInformation {

	private String feature;
	private String experiment;
	private Map<String, String> parameters;
	private Map<String, String> operations;

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public Map<String, String> getOperations() {
		return operations;
	}

	public void setOperations(Map<String, String> operations) {
		this.operations = operations;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getExperiment() {
		return experiment;
	}

	public void setExperiment(String experiment) {
		this.experiment = experiment;
	}

}
