/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.scheduler.local;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import eu.gloria.rt.entity.db.File;
import eu.gloria.rt.entity.db.UuidType;
import eu.gloria.rt.entity.scheduler.PlanInfo;
import eu.gloria.rt.entity.scheduler.PlanSearchFilter;
import eu.gloria.rt.entity.scheduler.PlanSearchFilterResult;
import eu.gloria.rt.entity.scheduler.PlanSearchPagination;
import eu.gloria.rti_db.GloriaRtiDb;
import eu.gloria.rti_db.RtiDbError;
import eu.gloria.rti_scheduler.GloriaRtiScheduler;
import eu.gloria.rti_scheduler.RtiSchError;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class SchHandler {

	private GloriaRtiScheduler schProxy;
	private GloriaRtiDb dbProxy;
	//private static String port;
	private static String schServiceName;
	private static String dbServiceName;

	static {

		Properties properties = new Properties();
		try {
			InputStream in = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("scheduler.properties");

			properties.load(in);
			in.close();

			//port = (String) properties.get("port");
			schServiceName = (String) properties.get("sch_service_name");
			dbServiceName = (String) properties.get("db_service_name");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SchHandler(String host, String port, String user, String password)
			throws SchServerNotAvailableException {

		String actionMessage = "scheduler/" + host + "?" + "->";

		RTISchProxyConnection schConnection = new RTISchProxyConnection(host,
				port, schServiceName, user, password, false, null);

		RTIDBProxyConnection dbConnection = new RTIDBProxyConnection(host,
				port, dbServiceName, user, password, false, null);

		schProxy = schConnection.getProxy();
		dbProxy = dbConnection.getProxy();

		if (schProxy == null) {
			throw new SchServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		if (dbProxy == null) {
			throw new SchServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}
	}

	public void advertise(Date limit, List<String> xmlPlans)
			throws GenericSchException {

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(limit);
		calendar.add(Calendar.DATE, 1);

		XMLGregorianCalendar xgcal = null;
		try {
			xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
					calendar);
		} catch (DatatypeConfigurationException e) {
		}

		try {
			schProxy.planAdvertise(xgcal, xmlPlans);
		} catch (RtiSchError e) {
			throw new GenericSchException(e.getMessage());
		}
	}

	public String requestUuid() throws GenericSchException {
		try {
			return dbProxy.uuidCreate(UuidType.OP);
		} catch (RtiDbError e) {
			throw new GenericSchException(e.getMessage());
		}
	}

	public PlanInfo getOPInformationByUuid(String uuid)
			throws GenericSchException, EmptySchFilterResultException {
		List<String> uuids = new ArrayList<>();
		uuids.add(uuid);

		try {
			List<PlanInfo> plansInfo = schProxy.planSearchByUuid(uuids);
			
			if (plansInfo.size() > 0) {
				return plansInfo.get(0);
			}

			throw new EmptySchFilterResultException(uuid);
		} catch (RtiSchError e) {
			throw new GenericSchException(e.getMessage());
		}
	}

	public List<File> getOPResultFiles(String uuid)
			throws GenericSchException {

		try {
			return dbProxy.opGet(uuid).getFiles();
		} catch (RtiDbError e) {
			throw new GenericSchException(e.getMessage());
		}		
	}
	
	public List<PlanInfo> getOPInformationByUser(String user)
			throws GenericSchException, EmptySchFilterResultException {

		PlanSearchFilter filter = new PlanSearchFilter();
		filter.setUser(user);

		PlanSearchPagination pagination = new PlanSearchPagination();
		pagination.setPageSize(100);

		try {
			PlanSearchFilterResult filterResult = schProxy.planSearchByFilter(
					filter, pagination);
			
			List<PlanInfo> plansInfo = filterResult.getItems();

			return plansInfo;			
		} catch (RtiSchError e) {
			throw new GenericSchException(e.getMessage());
		}
	}

}
