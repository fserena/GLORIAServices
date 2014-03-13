package eu.gloria.gs.services.teleoperation.base;

public interface Teleoperation {

	public OperationReturn executeOperation(Operation operation) throws Exception;
	
}
