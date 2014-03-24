package eu.gloria.gs.services.teleoperation.base;

import java.util.ArrayList;

public class OperationArgs {

	private ArrayList<Object> arguments;
	
	public ArrayList<Object> getArguments()
	{
		if (arguments == null) {
			this.arguments = new ArrayList<Object>();
		}
		
		return this.arguments;
	}	
}
