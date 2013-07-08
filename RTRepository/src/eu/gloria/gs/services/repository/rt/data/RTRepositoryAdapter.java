package eu.gloria.gs.services.repository.rt.data;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebParam;

import org.apache.ibatis.exceptions.PersistenceException;

import eu.gloria.gs.services.repository.rt.data.dbservices.DeviceDBService;
import eu.gloria.gs.services.repository.rt.data.dbservices.DeviceEntry;
import eu.gloria.gs.services.repository.rt.data.dbservices.ObservatoryDBService;
import eu.gloria.gs.services.repository.rt.data.dbservices.ObservatoryEntry;
import eu.gloria.gs.services.repository.rt.data.dbservices.RTAvailabilityEntry;
import eu.gloria.gs.services.repository.rt.data.dbservices.RTDBService;
import eu.gloria.gs.services.repository.rt.data.dbservices.RTDevEntry;
import eu.gloria.gs.services.repository.rt.data.dbservices.RTDeviceDBService;
import eu.gloria.gs.services.repository.rt.data.dbservices.RTEntry;
import eu.gloria.gs.services.repository.rt.data.dbservices.RTRepositoryAdapterException;

public class RTRepositoryAdapter {

	private ObservatoryDBService obsService;
	private RTDBService rtService;
	private DeviceDBService devService;
	private RTDeviceDBService rtdevService;

	public RTRepositoryAdapter() {

	}

	public void setObservatoryDBService(ObservatoryDBService service) {
		this.obsService = service;
		obsService.create();
	}

	public void setRTDBService(RTDBService service) {
		this.rtService = service;
		rtService.create();
	}

	public void setDeviceDBService(DeviceDBService service) {
		this.devService = service;
		devService.create();
	}

	public void setRTDeviceDBService(RTDeviceDBService service) {
		this.rtdevService = service;
		rtdevService.create();
	}

	public void registerRT(String rt, String owner, String url, String user, String password)
			throws RTRepositoryAdapterException {
		if (rt == null)
			throw new RTRepositoryAdapterException("The RT name cannot be null");
		if (owner == null)
			throw new RTRepositoryAdapterException(
					"The owner name cannot be null");
		if (url == null)
			throw new RTRepositoryAdapterException("The URL cannot be null");
		if (user == null)
			throw new RTRepositoryAdapterException("The user cannot be null");
		if (password == null)
			throw new RTRepositoryAdapterException("The password cannot be null");

		boolean previouslyContained = false;

		previouslyContained = rtService.contains(rt);

		if (!previouslyContained) {
			RTEntry entry = new RTEntry();
			entry.setOwner(owner);
			entry.setUrl(url);
			entry.setName(rt);
			entry.setPassword(password);
			entry.setUser(user);

			rtService.save(entry);

		}

		if (previouslyContained)
			throw new RTRepositoryAdapterException("The robotic telescope '"
					+ rt + "' already exists");
	}

	public void unregisterRT(String rt) throws RTRepositoryAdapterException {

		boolean previouslyContained = false;

		previouslyContained = rtService.contains(rt);

		if (previouslyContained) {
			rtService.remove(rt);

		}

		if (!previouslyContained)
			throw new RTRepositoryAdapterException("The telescope '" + rt
					+ "' does not exist");
	}

	public boolean containsRT(String rt) throws RTRepositoryAdapterException {

		return rtService.contains(rt);
	}

	public void addDevice(DeviceType type, String rt, String name, String model)
			throws RTRepositoryAdapterException {

		boolean deviceFound = false;
		boolean duplicated = false;

		DeviceEntry devEntry = null;

		try {
			devEntry = devService.get(type.name(), model);
		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException("The device model '" + model
					+ "' and type '" + type.name() + "' does not exist");
		}

		if (devEntry != null) {
			deviceFound = true;
			int did = devEntry.getDid();

			RTDevEntry rtDevEntry = new RTDevEntry();
			rtDevEntry.setDid(did);
			rtDevEntry.setName(name);
			rtDevEntry.setRT(rt);

			try {
				rtdevService.save(rtDevEntry);

			} catch (PersistenceException e) {
				if (e.getMessage().contains("Duplicate"))
					duplicated = true;
				else {
					throw new RTRepositoryAdapterException(e.getMessage());
				}
			}
		}

		if (!deviceFound)
			throw new RTRepositoryAdapterException("The device model '" + model
					+ "' and type '" + type.name() + "' does not exist");

		if (duplicated)
			throw new RTRepositoryAdapterException("The device name '" + name
					+ "' is already included in the RT '" + rt + "'");
	}

	public void removeDevice(String rt, String device)
			throws RTRepositoryAdapterException {

		RTDevEntry entry = null;

		try {
			entry = rtdevService.get(rt, device);
		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException("There is no device named '"
					+ device + "' registered on the RT '" + rt + "'");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}

		boolean contained = false;

		try {
			if (entry != null) {
				rtdevService.remove(rt, entry.getDid(), device);

				contained = true;
			}

		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}

		if (!contained) {
			throw new RTRepositoryAdapterException("There is no device named '"
					+ device + "' registered on the RT '" + rt
					+ "' or the RT does not exist");
		}
	}

	public DeviceInformation getRTDeviceInformation(String rt, String device)
			throws RTRepositoryAdapterException {

		RTDevEntry rtDevEntry = null;

		try {
			rtDevEntry = rtdevService.get(rt, device);

			DeviceInformation devInfo = null;

			if (rtDevEntry != null) {

				DeviceEntry entry = devService.getById(rtDevEntry.getDid());

				if (entry != null) {
					devInfo = new DeviceInformation();
					devInfo.setDescription(entry.getDescription());
					devInfo.setModel(entry.getModel());
					devInfo.setDeviceId(entry.getDid());
					devInfo.setType(DeviceType.valueOf(entry.getType()));
				}
			}

			return devInfo;

		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException(
					"No device entries available.");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}
	}

	public List<String> getDeviceNames(String rt, DeviceType type)
			throws RTRepositoryAdapterException {

		List<RTDevEntry> entries = null;
		try {
			entries = rtdevService.getByRT(rt);

		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException(
					"There are not telescope-devices relation entries registered.");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}

		List<String> deviceNames = new ArrayList<String>();

		String typeStr = type.name();

		if (entries != null) {
			for (RTDevEntry entry : entries) {
				DeviceEntry devEntry = null;

				try {
					devEntry = devService.getById(entry.getDid());
					if (devEntry.getType().equals(typeStr)) {
						deviceNames.add(entry.getName());
					}
				} catch (PersistenceException e) {

					deviceNames.clear();
					throw new RTRepositoryAdapterException(e.getMessage());
				}
			}
		}

		return deviceNames;
	}

	public List<String> getAllDeviceNames(String rt)
			throws RTRepositoryAdapterException {

		List<RTDevEntry> entries = null;

		try {
			entries = rtdevService.getByRT(rt);
		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException(
					"There are not telescope-devices relation entries registered.");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}

		List<String> deviceNames = new ArrayList<String>();

		if (entries != null) {
			for (RTDevEntry entry : entries) {
				deviceNames.add(entry.getName());
			}
		}

		return deviceNames;
	}

	public void registerDevice(DeviceType type, String model)
			throws RTRepositoryAdapterException {

		if (type == null || model == null)
			throw new RTRepositoryAdapterException(
					"Neither the type nor the model can be null");

		boolean previouslyContained = false;

		try {
			previouslyContained = devService.contains(type.name(), model);

			if (!previouslyContained) {
				DeviceEntry entry = new DeviceEntry();
				entry.setType(type.name());
				entry.setModel(model);

				try {
					devService.save(entry);
				} catch (PersistenceException e) {
					throw new RTRepositoryAdapterException(e.getMessage());
				}

			}
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}

		if (previouslyContained)
			throw new RTRepositoryAdapterException("The '" + type.name()
					+ "' device model '" + model + "' already exists");
	}

	public void unregisterDevice(DeviceType type, String model)
			throws RTRepositoryAdapterException {

		boolean previouslyContained = false;

		try {
			previouslyContained = devService.contains(type.name(), model);

			if (previouslyContained) {
				devService.remove(type.name(), model);

			}

		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}

		if (!previouslyContained)
			throw new RTRepositoryAdapterException("The '" + type.name()
					+ "' device model '" + model + "' does not exist");
	}

	public DeviceInformation getDeviceInformation(DeviceType type, String model)
			throws RTRepositoryAdapterException {

		DeviceInformation devInfo = null;

		try {
			DeviceEntry entry = devService.get(type.name(), model);

			if (entry != null) {
				devInfo = new DeviceInformation();
				devInfo.setDescription(entry.getDescription());
				devInfo.setModel(entry.getModel());
				devInfo.setDeviceId(entry.getDid());
				devInfo.setType(DeviceType.valueOf(entry.getType()));

				return devInfo;
			} else {
				throw new NullPointerException();
			}

		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException(
					"The device entry does not exists.");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}
	}

	public void setDeviceInformation(DeviceType type, String model,
			DeviceInformation info) throws RTRepositoryAdapterException {

		try {
			devService
					.setDescription(type.name(), model, info.getDescription());

		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException(
					"The device entry does not exists");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}
	}

	public List<String> getDeviceModels(DeviceType type)
			throws RTRepositoryAdapterException {

		List<String> models = null;
		try {
			models = devService.getModels(type.name());
		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException(
					"There are not telescope-devices relation entries registered.");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}

		return models;
	}

	public List<DeviceType> getAllDeviceTypes()
			throws RTRepositoryAdapterException {

		List<String> typeStr = null;

		try {
			typeStr = devService.getTypes();

			List<DeviceType> types = new ArrayList<DeviceType>();
			if (typeStr != null) {
				for (String str : typeStr) {
					types.add(DeviceType.valueOf(str));
				}
			}

			return types;
		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException(
					"No device entries available.");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}
	}

	public void registerObservatory(String observatory, String city,
			String country) throws RTRepositoryAdapterException {

		boolean previouslyContained = false;

		try {
			previouslyContained = obsService.contains(observatory);

			if (!previouslyContained) {
				ObservatoryEntry entry = new ObservatoryEntry();
				entry.setName(observatory);
				entry.setCity(city);
				entry.setCountry(country);

				try {
					obsService.save(entry);
				} catch (PersistenceException e) {

					throw new RTRepositoryAdapterException(e.getMessage());
				}

			}
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}

		if (previouslyContained)
			throw new RTRepositoryAdapterException(
					"The observatory entry already exists");
	}

	public void unregisterObservatory(
			@WebParam(name = "observatory") String observatory)
			throws RTRepositoryAdapterException {

		boolean previouslyContained = false;

		try {
			previouslyContained = obsService.contains(observatory);

			if (!previouslyContained) {
				obsService.remove(observatory);
			}

		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}

		if (!previouslyContained)
			throw new RTRepositoryAdapterException(
					"The observatory entry does not exists");

	}

	public void setObservatoryInformation(String observatory,
			ObservatoryInformation info) throws RTRepositoryAdapterException {

		try {
			if (info.getCity() != null)
				obsService.setCity(observatory, info.getCity());
			if (info.getCountry() != null)
				obsService.setCountry(observatory, info.getCountry());
			obsService.setLightPollution(observatory, info.getLightPollution());
			obsService.setVisibilityRatio(observatory,
					info.getVisibilityRatio());

		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException(
					"The observatory entry does not exists");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}

	}

	public ObservatoryInformation getObservatoryInformation(String observatory)
			throws RTRepositoryAdapterException {

		ObservatoryInformation obsInfo = null;

		try {
			ObservatoryEntry entry = obsService.getByName(observatory);

			if (entry != null) {
				obsInfo = new ObservatoryInformation();
				obsInfo.setCity(entry.getCity());
				obsInfo.setCountry(entry.getCountry());
				obsInfo.setLightPollution(entry.getLight_pollution());
				obsInfo.setVisibilityRatio(entry.getRatio());

				return obsInfo;
			} else {
				throw new RTRepositoryAdapterException(
						"The observatory entry does not exists");
			}
		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException(
					"The observatory entry does not exists");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}
	}

	public List<String> getAllObservatoryNames()
			throws RTRepositoryAdapterException {

		try {
			List<String> names = obsService.getAllNames();

			names = obsService.getAllNames();

			return names;

		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException(
					"No observatory entries available.");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}
	}

	public List<String> getAllRTInObservatory(String observatory)
			throws RTRepositoryAdapterException {

		try {
			int oid = obsService.getByName(observatory).getOid();

			List<String> rt = rtService.getByObservatoryId(oid);

			return rt;

		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException(
					"No telescope-observatory relation entries available.");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}
	}

	public String getRTUrl(String rt) throws RTRepositoryAdapterException {

		RTEntry entry = null;

		try {
			entry = rtService.get(rt);
		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException("The telescope '" + rt
					+ "' does not exist");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}

		if (entry != null)
			return entry.getUrl();
		else
			throw new RTRepositoryAdapterException("The telescope '" + rt
					+ "' does not exist");
	}

	public void setRTDescription(String rt, String description)
			throws RTRepositoryAdapterException {

		try {
			rtService.setDescription(rt, description);

		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException("The telescope '" + rt
					+ "' does not exist");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}

	}

	public String getRTDescription(String rt)
			throws RTRepositoryAdapterException {

		String description = null;

		try {
			description = rtService.getDescription(rt);

			return description;
		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException("The telescope '" + rt
					+ "' does not exist");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}
	}

	public void setRTPublicKey(String rt, String pk)
			throws RTRepositoryAdapterException {

		try {
			rtService.setPublicKey(rt, pk);

		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException("The telescope '" + rt
					+ "' does not exist");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}
	}

	public String getRTPublicKey(String rt) throws RTRepositoryAdapterException {

		String pk = null;

		try {
			pk = rtService.getPublicKey(rt);

			return pk;
		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException("The telescope '" + rt
					+ "' does not exist");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}
	}

	public String getRTOwner(String rt) throws RTRepositoryAdapterException {

		String owner = null;

		try {
			owner = rtService.getOwner(rt);

			return owner;
		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException("The telescope '" + rt
					+ "' does not exist");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}
	}

	public RTCoordinates getRTCoordinates(String rt)
			throws RTRepositoryAdapterException {

		double value;
		RTCoordinates coordinates = new RTCoordinates();

		try {
			value = rtService.getLatitude(rt);
			coordinates.setLatitude(value);
			value = rtService.getLongitude(rt);
			coordinates.setLongitude(value);

			return coordinates;
		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException("The telescope '" + rt
					+ "' does not exist");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}
	}

	public RTCredentials getRTCredentials(String rt)
			throws RTRepositoryAdapterException {

		RTEntry entry = null;
		RTCredentials credentials = new RTCredentials();

		try {
			entry = rtService.get(rt);
			
			credentials.setUser(entry.getUser());
			credentials.setPassword(entry.getPassword());
			
			return credentials;
			
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}
	}
	
	public RTAvailability getRTAvailability(String rt)
			throws RTRepositoryAdapterException {

		RTAvailabilityEntry entry = null;
		RTAvailability availability = new RTAvailability();

		try {
			entry = rtService.getAvailability(rt);

			availability.setEndingTime(entry.getEnding_avl());
			availability.setStartingTime(entry.getStarting_avl());

			return availability;
		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException("The telescope '" + rt
					+ "' has no availability defined");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}
	}

	public void setRTAvailability(String rt, RTAvailability availability)
			throws RTRepositoryAdapterException {

		boolean alreadyContained = false;

		try {
			if (rtService.contains(rt)) {
				alreadyContained = true;

				rtService.setStartingAvailability(rt,
						availability.getStartingTime());
				rtService.setEndingAvailability(rt,
						availability.getEndingTime());

			}
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}

		if (!alreadyContained) {
			throw new RTRepositoryAdapterException("The telescope '" + rt
					+ "' does not exist");
		}
	}

	public void setRTCoordinates(String rt, RTCoordinates coords)
			throws RTRepositoryAdapterException {

		try {
			rtService.setCoordinates(rt, coords.getLatitude(),
					coords.getLongitude());

		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException("The telescope '" + rt
					+ "' does not exist");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}
	}

	public void setRTObservatory(String rt, String observatory)
			throws RTRepositoryAdapterException {

		ObservatoryEntry obsEntry = null;

		try {
			obsEntry = obsService.getByName(observatory);
		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException("The telescope '" + rt
					+ "' does not exist");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}

		int oid = obsEntry.getOid();

		try {
			rtService.setObservatory(rt, oid);

		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}

	}

	public String getRTObservatory(String rt)
			throws RTRepositoryAdapterException {

		int oid;

		try {
			oid = rtService.getObservatory(rt);
		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException(
					"The observatory-telescope relation entry does not exist.");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}

		try {
			ObservatoryEntry obsEntry = obsService.getById(oid);

			return obsEntry.getName();
		} catch (NullPointerException e) {
			throw new RTRepositoryAdapterException("The telescope '" + rt
					+ "' does not have an observatory defined");
		} catch (PersistenceException e) {
			throw new RTRepositoryAdapterException(e.getMessage());
		}
	}
}
