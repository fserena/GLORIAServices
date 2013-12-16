package eu.gloria.gs.services.scheduler.op;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Date tools.
 * 
 * @author jcabello
 *
 */
public class DateTools {
	
	private static GregorianCalendar calendar;
	private static HashMap<String, SimpleDateFormat> sdfs;
	
	static{
		calendar = new GregorianCalendar();
		sdfs = new HashMap<String, SimpleDateFormat>();
	}
	
	/**
	 * Builds a date using a pattern.
	 * @param inputDate Date in the string format.
	 * @param pattern Date pattern. Example: yyyyMMddHHmmss
	 * @return Date
	 * @throws ParseException In error case.
	 */
	public static Date getDate(String inputDate, String pattern) throws ParseException{
		
		SimpleDateFormat sdf = getSdf(pattern);
		
		synchronized (sdf) {
			return sdf.parse(inputDate);
		}
		
	}
	
	public static String getDate(Date date, String pattern) throws ParseException{
		
		SimpleDateFormat sdf = getSdf(pattern);
		
		synchronized (sdf) {
			return sdf.format(date);
		}
		
	}
	
	public static XMLGregorianCalendar getXmlGregorianCalendar(Date date) throws Exception{
		
		if (date == null) return null;
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(date);
		XMLGregorianCalendar result = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		return result;

	}
	
	public static Date getDate(XMLGregorianCalendar xgc){
		return xgc.toGregorianCalendar().getTime();
	}
	
	private static SimpleDateFormat getSdf(String pattern){
		
		SimpleDateFormat sdf;
		
		synchronized (sdfs) {
			sdf = sdfs.get(pattern);
			if (sdf == null){
				sdf = new SimpleDateFormat(pattern);
				sdfs.put(pattern, sdf);
			}
		}
		
		return sdf;
		
	}
	
	public static Date trunk(Date date, String pattern) throws ParseException{
		
		SimpleDateFormat sdf = getSdf(pattern);
		
		synchronized (sdf) {
			return sdf.parse(sdf.format(date));
		}
		
	}
	
	public static Date increment(Date date, int incType, int incAmount){
		synchronized (calendar) {
			calendar.setTime(date);
			calendar.add(incType, incAmount);
			return calendar.getTime();
		}
		
	}
	
	public static Date getGMT(Date date) throws ParseException{
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		 String utcTime = sdf.format(date);
		 
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date result = (Date)dateFormat.parse(utcTime);

		 return result;
	}
	
	/**
	 * Retieve the offset in hours.
	 * @return Seconds
	 * @throws ParseException 
	 */
	public static int getGMTOffsetHour() throws ParseException{
		Date now = new Date();
		Date utcNow = getGMT(now);
		
		long msecOffset = now.getTime() - utcNow.getTime();
		return (int) ((msecOffset /1000) /3600);
	}
	
}
