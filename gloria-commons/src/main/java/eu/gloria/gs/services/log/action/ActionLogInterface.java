package eu.gloria.gs.services.log.action;

import java.util.Date;

import javax.jws.WebService;

import eu.gloria.gs.services.utils.ObjectResponse;

@WebService(name = "ActionLogInterface", targetNamespace = "http://action.log.services.gs.gloria.eu/")
public interface ActionLogInterface {

	public void registerUserAction(LogType type, String username, Date when,
			ObjectResponse action) throws ActionLogException;

	public void registerContextAction(LogType type, String username, Date when,
			int rid, ObjectResponse action) throws ActionLogException;

	public void registerRTAction(LogType type, String username, Date when,
			String rt, ObjectResponse action) throws ActionLogException;

	public ObjectResponse getLogs(Date from, Date to, LogType type)
			throws ActionLogException;

	public ObjectResponse getUserLogs(String username, Date from, Date to,
			LogType type) throws ActionLogException;

	public ObjectResponse getRTLogs(String rt, Date from, Date to, LogType type)
			throws ActionLogException;

	public ObjectResponse getContextLogs(int rid, Date from, Date to,
			LogType type) throws ActionLogException;

	public boolean containsUserLogs(String user, Date from, Date to)
			throws ActionLogException;

	public boolean containsRtLogs(String rt, Date from, Date to)
			throws ActionLogException;

	public boolean containsLogs(Date from, Date to) throws ActionLogException;

	public boolean containsContextLogs(int rid, Date from, Date to)
			throws ActionLogException;
}
