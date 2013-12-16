package eu.gloria.gs.services.log.action;

import java.util.Date;

import javax.jws.WebParam;
import javax.jws.WebService;

import eu.gloria.gs.services.utils.ObjectResponse;

@WebService(name = "ActionLogInterface", targetNamespace = "http://action.log.services.gs.gloria.eu/")
public interface ActionLogInterface {

	public void registerError(@WebParam(name = "username") String username,
			@WebParam(name = "when") Date when,
			@WebParam(name = "action") ObjectResponse action)
			throws ActionLogException;

	public void registerWarning(@WebParam(name = "username") String username,
			@WebParam(name = "when") Date when,
			@WebParam(name = "action") ObjectResponse action)
			throws ActionLogException;

	public void registerInfo(@WebParam(name = "username") String username,
			@WebParam(name = "when") Date when,
			@WebParam(name = "action") ObjectResponse action)
			throws ActionLogException;

	public void registerContextError(
			@WebParam(name = "username") String username,
			@WebParam(name = "when") Date when,
			@WebParam(name = "rid") int rid,
			@WebParam(name = "action") ObjectResponse action)
			throws ActionLogException;

	public void registerContextWarning(
			@WebParam(name = "username") String username,
			@WebParam(name = "when") Date when,
			@WebParam(name = "rid") int rid,
			@WebParam(name = "action") ObjectResponse action)
			throws ActionLogException;

	public void registerContextInfo(
			@WebParam(name = "username") String username,
			@WebParam(name = "when") Date when,
			@WebParam(name = "rid") int rid,
			@WebParam(name = "action") ObjectResponse action)
			throws ActionLogException;

	public void registerRtError(@WebParam(name = "username") String username,
			@WebParam(name = "when") Date when,
			@WebParam(name = "rt") String rt,
			@WebParam(name = "action") ObjectResponse action)
			throws ActionLogException;

	public void registerRtWarning(@WebParam(name = "username") String username,
			@WebParam(name = "when") Date when,
			@WebParam(name = "rt") String rt,
			@WebParam(name = "action") ObjectResponse action)
			throws ActionLogException;

	public void registerRtInfo(@WebParam(name = "username") String username,
			@WebParam(name = "when") Date when,
			@WebParam(name = "rt") String rt,
			@WebParam(name = "action") ObjectResponse action)
			throws ActionLogException;

	public ObjectResponse getAllUserLogs(
			@WebParam(name = "username") String username)
			throws ActionLogException;

	public ObjectResponse getAllUserLogsByDate(
			@WebParam(name = "username") String username,
			@WebParam(name = "from") Date from, @WebParam(name = "to") Date to)
			throws ActionLogException;

	public ObjectResponse getAllRtLogs(@WebParam(name = "rt") String rt)
			throws ActionLogException;

	public ObjectResponse getAllRtLogsByDate(@WebParam(name = "rt") String rt,
			@WebParam(name = "from") Date from, @WebParam(name = "to") Date to)
			throws ActionLogException;

	public ObjectResponse getAllRidLogs(@WebParam(name = "rid") int rid)
			throws ActionLogException;

	public ObjectResponse getAllRidLogsByDate(@WebParam(name = "rid") int rid,
			@WebParam(name = "from") Date from, @WebParam(name = "to") Date to)
			throws ActionLogException;

	public ObjectResponse getAllDateLogs(@WebParam(name = "from") Date from,
			@WebParam(name = "to") Date to) throws ActionLogException;

	public ObjectResponse getErrorLogsByDate(
			@WebParam(name = "from") Date from, @WebParam(name = "to") Date to)
			throws ActionLogException;

	public ObjectResponse getWarningLogsByDate(
			@WebParam(name = "from") Date from, @WebParam(name = "to") Date to)
			throws ActionLogException;

	public ObjectResponse getInfoLogsByDate(@WebParam(name = "from") Date from,
			@WebParam(name = "to") Date to) throws ActionLogException;

	public ObjectResponse getErrorUserLogs(
			@WebParam(name = "username") String username)
			throws ActionLogException;

	public ObjectResponse getErrorUserLogsByDate(
			@WebParam(name = "username") String username,
			@WebParam(name = "from") Date from, @WebParam(name = "to") Date to)
			throws ActionLogException;

	public ObjectResponse getErrorRidLogs(@WebParam(name = "rid") int rid)
			throws ActionLogException;

	public ObjectResponse getErrorRidLogsByDate(
			@WebParam(name = "rid") int rid,
			@WebParam(name = "from") Date from, @WebParam(name = "to") Date to)
			throws ActionLogException;

	public ObjectResponse getErrorRtLogs(@WebParam(name = "rt") String rt)
			throws ActionLogException;

	public ObjectResponse getErrorRtLogsByDate(
			@WebParam(name = "rt") String rt,
			@WebParam(name = "from") Date from, @WebParam(name = "to") Date to)
			throws ActionLogException;

	public ObjectResponse getInfoUserLogs(
			@WebParam(name = "username") String username)
			throws ActionLogException;

	public ObjectResponse getInfoUserLogsByDate(
			@WebParam(name = "username") String username,
			@WebParam(name = "from") Date from, @WebParam(name = "to") Date to)
			throws ActionLogException;

	public ObjectResponse getInfoRidLogs(@WebParam(name = "rid") int rid)
			throws ActionLogException;

	public ObjectResponse getInfoRidLogsByDate(@WebParam(name = "rid") int rid,
			@WebParam(name = "from") Date from, @WebParam(name = "to") Date to)
			throws ActionLogException;

	public ObjectResponse getInfoRtLogs(@WebParam(name = "rt") String rt)
			throws ActionLogException;

	public ObjectResponse getInfoRtLogsByDate(@WebParam(name = "rt") String rt,
			@WebParam(name = "from") Date from, @WebParam(name = "to") Date to)
			throws ActionLogException;

	public ObjectResponse getWarningUserLogs(
			@WebParam(name = "username") String username)
			throws ActionLogException;

	public ObjectResponse getWarningUserLogsByDate(
			@WebParam(name = "username") String username,
			@WebParam(name = "from") Date from, @WebParam(name = "to") Date to)
			throws ActionLogException;

	public ObjectResponse getWarningRidLogs(@WebParam(name = "rid") int rid)
			throws ActionLogException;

	public ObjectResponse getWarningRidLogsByDate(
			@WebParam(name = "rid") int rid,
			@WebParam(name = "from") Date from, @WebParam(name = "to") Date to)
			throws ActionLogException;

	public ObjectResponse getWarningRtLogs(@WebParam(name = "rt") String rt)
			throws ActionLogException;

	public ObjectResponse getWarningRtLogsByDate(
			@WebParam(name = "rt") String rt,
			@WebParam(name = "from") Date from, @WebParam(name = "to") Date to)
			throws ActionLogException;

	public boolean containsUserLogs(@WebParam(name = "user") String user)
			throws ActionLogException;

	public boolean containsUserLogsByDate(@WebParam(name = "user") String user,
			@WebParam(name = "from") Date from, @WebParam(name = "to") Date to) throws ActionLogException;

	public boolean containsRtLogs(@WebParam(name = "rt") String rt)
			throws ActionLogException;

	public boolean containsRtLogsByDate(@WebParam(name = "rt") String rt,
			@WebParam(name = "from") Date from, @WebParam(name = "to") Date to) throws ActionLogException;

	public boolean containsDateLogs(@WebParam(name = "from") Date from,
			@WebParam(name = "to") Date to) throws ActionLogException;

	public boolean containsRidLogs(@WebParam(name = "rid") int rid)
			throws ActionLogException;

	public boolean containsRidLogsByDate(@WebParam(name = "rid") int rid,
			@WebParam(name = "from") Date from, @WebParam(name = "to") Date to) throws ActionLogException;
}
