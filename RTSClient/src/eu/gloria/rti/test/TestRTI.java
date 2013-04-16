package eu.gloria.rti.test;

import eu.gloria.rti.client.RTSHandler;
import eu.gloria.rti.client.RTSException;


public class TestRTI {

	/**
	 * @param args
	 * @throws RTSException 
	 */
	public static void main(String[] args) throws RTSException {

		RTSHandler rts = new RTSHandler("161.72.128.9");
				
		double ff = rts.getBrightness("DBx 41AU02.AS");
		System.out.println(ff);
		
		ff = rts.getExposureTime("DBx 41AU02.AS");
		
		//double ff = rts.getExposure("C0");
		System.out.println(ff);
		
	/*	ff = rts.getContrast("DBx 41AU02.AS");
		System.out.println(ff);*/
		
		ff = rts.getGain("DBx 41AU02.AS");
		System.out.println(ff);
		
	//	String id = rts.startExposure("DBx 41AU02.AS");
	//	System.out.println(id);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	//	String url = rts.getImageUrl("DBx 41AU02.AS", id);
	//	System.out.println(url);
	}

}
