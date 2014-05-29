/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.scheduler.op;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import eu.gloria.gs.services.repository.rt.data.RTCredentials;
import eu.gloria.gs.services.repository.rt.data.ServerKeyData;
import eu.gloria.gs.services.scheduler.local.EmptySchFilterResultException;
import eu.gloria.gs.services.scheduler.local.GenericSchException;
import eu.gloria.gs.services.scheduler.local.SchHandler;
import eu.gloria.gs.services.scheduler.local.SchServerManager;
import eu.gloria.gs.services.scheduler.local.SchServerNotAvailableException;
import eu.gloria.rt.entity.db.UuidType;
import eu.gloria.rt.entity.scheduler.PlanInfo;
import eu.gloria.rt.entity.scheduler.PlanSearchFilter;
import eu.gloria.rt.entity.scheduler.PlanSearchFilterResult;
import eu.gloria.rt.entity.scheduler.PlanSearchPagination;
import eu.gloria.rt.entity.scheduler.PlanStateInfo;
import eu.gloria.rti_db.GloriaRtiDb;
import eu.gloria.rti_db.RtiDbError;
import eu.gloria.rti_db.tools.RTIDBProxyConnection;
import eu.gloria.rti_scheduler.GloriaRtiScheduler;
import eu.gloria.rti_scheduler.PlanSearchByFilter;
import eu.gloria.rti_scheduler.PlanSearchByUuid;
import eu.gloria.rti_scheduler.RtiSchError;
import eu.gloria.rti_scheduler.tools.RTISchProxyConnection;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class Test {

	/**
	 * @param args
	 * @throws SchServerNotAvailableException 
	 */
	public static void main(String[] args) throws SchServerNotAvailableException {

		
		/*RTISchProxyConnection rtiSchConnection = new RTISchProxyConnection(
				"88.198.125.131", "8080", "RTISch", "gloria_user", "1q2w3e4r",
				false, "");

		RTIDBProxyConnection rtiDBConnection = new RTIDBProxyConnection(
				"88.198.125.131", "8080", "RTIDB", "gloria_user", "1q2w3e4r",
				false, "");*/
		
	/*	RTISchProxyConnection rtiSchConnection = new RTISchProxyConnection(
				"139.229.12.76", "8080", "RTISch", "gloria_user", "12345",
				false, "");

		RTIDBProxyConnection rtiDBConnection = new RTIDBProxyConnection(
				"139.229.12.76", "8080", "RTIDB", "gloria_user", "12345",
				false, "");*/

		ServerKeyData keyData = new ServerKeyData();
		keyData.setUrl("88.198.125.131");
		RTCredentials credentials = new RTCredentials();
		credentials.setUser("gloria_user");
		credentials.setPassword("1q2w3e4r");
		
		keyData.setCredentials(credentials);
		
		SchHandler handler = SchServerManager.getReference().getSch(keyData);
		
		ObservingPlan op = new ObservingPlan();
		
		/*op.setUser("gloria");
		op.setMoonAltitude(0.0);
		op.setMoonDistance(20.0);
		op.setTargetAltitude(42.0);
		op.setPriority(100);
	
		op.setObject("m31");
		op.setDescription("FER - M31");
		op.setExposure(4.0);
		op.setFilter("OPEN");*/

		op.setHandler(handler);
		
		//op.build();
		//op.advertise();
		
		/*
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	*/
		
	/*	List<PlanInfo> plans;
		try {
			plans = handler.getOPInformationByUser("gloria");
			
			for (PlanInfo plan :plans) {
				System.out.println(plan.getDescription() + " " + plan.getUuid());
			}
			
		} catch (GenericSchException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (EmptySchFilterResultException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	*/
		
		try {
			PlanInfo plan = handler.getOPInformationByUuid("0000000f20131009000001419e4b6b54v001");
			
			System.out.println(plan.getStateInfo().getState());
			
		} catch (GenericSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EmptySchFilterResultException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
