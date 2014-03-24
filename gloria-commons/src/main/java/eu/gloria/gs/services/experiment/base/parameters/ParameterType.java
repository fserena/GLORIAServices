package eu.gloria.gs.services.experiment.base.parameters;

import java.util.ArrayList;

public class ParameterType {

	private Class<?> valueType;
	private Class<?> elementType;
	private boolean operationDependent;
	private ArrayList<Class<?>> argumentTypes;
	private ArrayList<String> argumentNames;

	public ArrayList<String> getArgumentNames() {
		return argumentNames;
	}

	public void setArgumentNames(ArrayList<String> argumentNames) {
		this.argumentNames = argumentNames;
	}

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

	public Class<?> getElementType() {
		return elementType;
	}

	public void setElementType(Class<?> elementType) {
		this.elementType = elementType;
	}

	// RT_NAME, CCD_NAME, INTEGER, DOUBLE, STRING, DOME_NAME, MOUNT_NAME,
	// FOCUSER_NAME, SCAM_NAME, OPERATION_PTR, BRIGHTNESS, CONTRAST, EXPOSURE,
	// GAIN, STATIC_URL, DYNAMIC_URL, STREAM
}
