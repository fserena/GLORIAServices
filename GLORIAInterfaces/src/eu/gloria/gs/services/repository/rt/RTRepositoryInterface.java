package eu.gloria.gs.services.repository.rt;

import eu.gloria.gs.services.repository.rt.data.DeviceInformation;
import eu.gloria.gs.services.repository.rt.data.DeviceType;
import eu.gloria.gs.services.repository.rt.data.ObservatoryInformation;
import eu.gloria.gs.services.repository.rt.data.RTAvailability;
import eu.gloria.gs.services.repository.rt.data.RTCoordinates;
import eu.gloria.gs.services.repository.rt.data.RTInformation;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "RTRepositoryInterface", targetNamespace = "http://rt.repository.services.gs.gloria.eu/")
public interface RTRepositoryInterface {

	public void registerRT(String rt, String owner, String url, String user, String password)
			throws RTRepositoryException;

	public void unregisterRT(String rt) throws RTRepositoryException;

	public boolean containsRT(String rt) throws RTRepositoryException;

	public void addRTDevice(DeviceType type, String rt, String name,
			String model) throws RTRepositoryException;

	public void removeRTDevice(String rt, String device)
			throws RTRepositoryException;

	public DeviceInformation getRTDeviceInformation(String rt, String device)
			throws RTRepositoryException;

	public List<String> getRTDeviceNames(String rt, DeviceType type)
			throws RTRepositoryException;

	public List<String> getDeviceNames(String rt) throws RTRepositoryException;

	// Generic device operations
	public void registerDevice(DeviceType type, String model)
			throws RTRepositoryException;

	public void unregisterDevice(DeviceType type, String model)
			throws RTRepositoryException;

	public DeviceInformation getDeviceInformation(DeviceType type, String model)
			throws RTRepositoryException;

	public void setDeviceInformation(DeviceType type, String model,
			DeviceInformation info) throws RTRepositoryException;

	public List<String> getDeviceModels(DeviceType type)
			throws RTRepositoryException;

	public List<DeviceType> getAllDeviceTypes() throws RTRepositoryException;

	public void registerObservatory(String observatory, String city,
			String country) throws RTRepositoryException;

	public void unregisterObservatory(String observatory)
			throws RTRepositoryException;

	public void setObservatoryInformation(String observatory,
			ObservatoryInformation info) throws RTRepositoryException;

	public ObservatoryInformation getObservatoryInformation(String observatory)
			throws RTRepositoryException;

	public List<String> getAllObservatoryNames() throws RTRepositoryException;

	public List<String> getAllRTInObservatory(
			@WebParam(name = "observatory") String observatory)
			throws RTRepositoryException;

	public RTInformation getRTInformation(String rt)
			throws RTRepositoryException;

	public String getRTUrl(String rt) throws Exception;

	public void setRTDescription(String rt, String description)
			throws RTRepositoryException;

	public String getRTDescription(String rt) throws RTRepositoryException;

	public void setRTPublicKey(String rt, String pk)
			throws RTRepositoryException;

	public String getRTPublicKey(String rt) throws RTRepositoryException;

	public String getRTOwner(String rt) throws RTRepositoryException;

	public RTCoordinates getRTCoordinates(String rt)
			throws RTRepositoryException;

	public void setRTCoordinates(String rt, RTCoordinates coords)
			throws RTRepositoryException;

	public RTAvailability getRTAvailability(String rt)
			throws RTRepositoryException;

	public void setRTAvailability(String rt, RTAvailability availability)
			throws RTRepositoryException;

	public void setRTObservatory(String rt, String observatory)
			throws RTRepositoryException;

	public String getRTObservatory(String rt) throws RTRepositoryException;

}
