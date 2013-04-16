package eu.gloria.rti.client;

import java.util.HashMap;

import eu.gloria.rti.client.RTSHandler;

public class RTSManager {
	
	private static RTSManager instance = null;
	private static Object sync = new Object();
	private static HashMap<String,RTSHandler> rtsTable = new HashMap<String, RTSHandler>();
	
	private RTSManager()
	{
		
	}
	
	static public RTSManager getReference()
	{
		synchronized(sync)
		{
			if (instance == null)
				instance = new RTSManager();
		}
		
		return instance;
	}
	
	public RTSHandler getRTS(String url) throws RTSException
	{
		synchronized(rtsTable)
		{
			if (!rtsTable.containsKey(url))
			{			
				RTSHandler rts = new RTSHandler(url);
				rtsTable.put(url, rts);				
			}
			
			return rtsTable.get(url);			
		}
	}
	
	

}
