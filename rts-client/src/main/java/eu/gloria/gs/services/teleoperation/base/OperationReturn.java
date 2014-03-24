package eu.gloria.gs.services.teleoperation.base;

import java.util.ArrayList;

public class OperationReturn {

	private ArrayList<Object> returns;
	private String message;
	
	public void setReturns(ArrayList<Object> returns)
	{
		this.returns = returns;
	}
	
	public ArrayList<Object> getReturns()
	{
		return this.returns;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public String getMessage()
	{
		return this.message;
	}
	
	
}
