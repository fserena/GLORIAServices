package eu.gloria.gs.services.experiment.online.parameters;

import java.util.ArrayList;

public class ParameterType {
	
	private Class<?> valueType;
	private boolean operationDependent;
	private ArrayList<Class<?>> argumentTypes;

	public Class<?> getValueType() {
		return valueType;
	}

	public void setValueType(Class<?> valueType) {
		this.valueType = valueType;
	}

	public boolean isOperationDependent() {
		return operationDependent;
	}

	public void setOperationDependent(boolean operationDependent) {
		this.operationDependent = operationDependent;
	}

	public ArrayList<Class<?>> getArgumentTypes() {
		return argumentTypes;
	}

	public void setArgumentTypes(ArrayList<Class<?>> argumentTypes) {
		this.argumentTypes = argumentTypes;
	}

	// RT_NAME, CCD_NAME, INTEGER, DOUBLE, STRING, DOME_NAME, MOUNT_NAME,
	// FOCUSER_NAME, SCAM_NAME, OPERATION_PTR, BRIGHTNESS, CONTRAST, EXPOSURE,
	// GAIN, STATIC_URL, DYNAMIC_URL, STREAM
}
