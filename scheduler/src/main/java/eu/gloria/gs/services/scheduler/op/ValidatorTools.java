package eu.gloria.gs.services.scheduler.op;

public class ValidatorTools {
	
	public static boolean isDouble(String value){
		try{
			Double.parseDouble(value);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	
	public static boolean isEmpty(String value){
		return value == null || value.trim().isEmpty();
	}
	
	public static boolean isInteger(String value){
		try{
			int x = Integer.parseInt(value);
			return true;
		}catch(Exception ex){
			return false;
		}
	}

}
