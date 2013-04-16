package eu.gloria.gs.services.teleoperation.base;

public abstract class Operation {

	private String server;
	
	public Operation(OperationArgs args)
	{		
		if (args.getArguments().size() > 0)
			this.server = (String) args.getArguments().get(0);
	}
	
	public String getServer()
	{
		return this.server;
	}
		
	public abstract OperationReturn execute(ServerResolver resolver) throws Exception;
	
}
