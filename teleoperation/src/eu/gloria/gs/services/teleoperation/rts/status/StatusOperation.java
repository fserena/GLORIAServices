package eu.gloria.gs.services.teleoperation.rts.status;

import eu.gloria.gs.services.teleoperation.base.Operation;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;

public abstract class StatusOperation extends Operation {
	
	public StatusOperation(OperationArgs args)
	{
		super(args);
		
	//	if (args.getArguments().size() > 1)
	//		this.mount = (String) args.getArguments().get(1);
	}	
}
