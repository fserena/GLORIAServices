package eu.gloria.gs.services.repository.rt.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.repository.rt.RTRepositoryException;
import eu.gloria.gs.services.repository.rt.data.dbservices.DeviceDBService;
import eu.gloria.gs.services.repository.rt.data.dbservices.DeviceEntry;
import eu.gloria.gs.services.repository.rt.data.dbservices.ObservatoryDBService;
import eu.gloria.gs.services.repository.rt.data.dbservices.ObservatoryEntry;
import eu.gloria.gs.services.repository.rt.data.dbservices.RTAvailabilityEntry;
import eu.gloria.gs.services.repository.rt.data.dbservices.RTDBService;
import eu.gloria.gs.services.repository.rt.data.dbservices.RTDevEntry;
import eu.gloria.gs.services.repository.rt.data.dbservices.RTDeviceDBService;
import eu.gloria.gs.services.repository.rt.data.dbservices.RTEntry;
import eu.gloria.gs.services.utils.LoggerEntity;

public class RTRepositoryAdapter extends LoggerEntity {

	private ObservatoryDBService obsService;
	private RTDBService rtService;
	private DeviceDBService devService;
	private RTDeviceDBService rtdevService;

	public RTRepositoryAdapter() {
		super(RTRepositoryAdapter.class.getSimpleName());
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

	private void registerRT(String rt, String owner, String url, String port,
			String user, String password, String type) throws ActionException {
		if (rt == null)
			throw new ActionException("rt name cannot be null");
		if (owner == null)
			throw new ActionException("owner name cannot be null");
		if (url == null)
			throw new ActionException("url cannot be null");
		if (user == null)
			throw new ActionException("user cannot be null");
		if (password == null)
			throw new ActionException("password cannot be null");

		boolean previouslyContained = false;

		previouslyContained = rtService.contains(rt);

		if (!previouslyContained) {
			RTEntry entry = new RTEntry();
			entry.setOwner(owner);
			entry.setUrl(url);
			entry.setPort(port);
			entry.setName(rt);
			entry.setPassword(password);
			entry.setUser(user);
			entry.setType(type);
			entry.setLatitude(0);
			entry.setLongitude(0);
			entry.setDate(new Date());

			rtService.save(entry);
			rtService.setStartingAvailability(rt, new Date());
			rtService.setEndingAvailability(rt, new Date());
		}

//		if (previouslyContained) {
//			throw new ActionException("rt already exists");
//		}
	}

	public void registerBatchRT(String rt, String owner, String url,
			String port, String user, String password) throws ActionException {
		this.registerRT(rt, owner, url, port, user, password, "BATCH");
	}

	public void registerInteractiveRT(String rt, String owner, String url,
			String port, String user, String password) throws ActionException {
		this.registerRT(rt, owner, url, port, user, password, "INTERACTIVE");
	}

	public void unregisterRT(String rt) throws ActionException {

		boolean previouslyContained = false;

		previouslyContained = rtService.contains(rt);

		if (previouslyContained) {
			rtService.remove(rt);

		}

		if (!previouslyContained) {
			throw new ActionException("rt does not exists");
		}
	}

	public boolean containsRT(String rt) throws ActionException {

		return rtService.contains(rt);
	}

	public void addDevice(DeviceType type, String rt, String name, String model)
			throws ActionException {

		boolean deviceFound = false;
		boolean duplicated = false;

		DeviceEntry devEntry = null;

		try {
			devEntry = devService.get(type.name(), model);
		} catch (NullPointerException e) {
			throw new ActionException("model does not exist");
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
				

			} catch (Exception e) {
				if (e.getMessage().contains("Duplicate"))
					duplicated = true;
				else {
					throw new ActionException(e.getMessage());
				}
			}
		}

		if (!deviceFound)
			throw new ActionException("device model does not exist");

		//if (duplicated)
		//	throw new ActionException("already included");
	}

	public void removeDevice(String rt, String device) throws ActionException {

		RTDevEntry entry = null;

		try {
			entry = rtdevService.get(rt, device);
		} catch (NullPointerException e) {
			throw new ActionException("device name does not exist");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}

		boolean contained = false;

		try {
			if (entry != null) {
				rtdevService.remove(rt, entry.getDid(), device);

				contained = true;
			}

		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}

		if (!contained) {
			throw new ActionException("device or rt does not exist");
		}
	}

	public DeviceInformation getRTDeviceInformation(String rt, String device)
			throws ActionException {

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
			throw new ActionException("no device entries available");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public List<String> getDeviceNames(String rt, DeviceType type)
			throws ActionException {

		List<RTDevEntry> entries = null;
		try {
			entries = rtdevService.getByRT(rt);

		} catch (NullPointerException e) {
			throw new ActionException("no telescope-devices relations");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
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
					throw new ActionException(e.getMessage());
				}
			}
		}

		return deviceNames;
	}

	public List<String> getAllDeviceNames(String rt) throws ActionException {

		List<RTDevEntry> entries = null;

		try {
			entries = rtdevService.getByRT(rt);
		} catch (NullPointerException e) {
			throw new ActionException("no telescope-devices relations");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
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
			throws ActionException {

		if (type == null || model == null)
			throw new ActionException("parameters are null");

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
					throw new ActionException(e.getMessage());
				}

			}
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}

		if (previouslyContained)
			throw new ActionException("device model already exists");
	}

	public void unregisterDevice(DeviceType type, String model)
			throws ActionException {

		boolean previouslyContained = false;

		try {
			previouslyContained = devService.contains(type.name(), model);

			if (previouslyContained) {
				devService.remove(type.name(), model);

			}

		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}

		if (!previouslyContained)
			throw new ActionException("device model does not exist");
	}

	public DeviceInformation getDeviceInformation(DeviceType type, String model)
			throws ActionException {

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
			throw new ActionException("device entry does not exist");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public void setDeviceInformation(DeviceType type, String model,
			DeviceInformation info) throws ActionException {

		try {
			devService
					.setDescription(type.name(), model, info.getDescription());

		} catch (NullPointerException e) {
			throw new ActionException("device entry does not exist");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public List<String> getDeviceModels(DeviceType type) throws ActionException {

		List<String> models = null;
		try {
			models = devService.getModels(type.name());
		} catch (NullPointerException e) {
			throw new ActionException("no telescope-devices relations");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}

		return models;
	}

	public List<DeviceType> getAllDeviceTypes() throws ActionException {

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
			throw new ActionException("no device entries available");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public void registerObservatory(String observatory, String city,
			String country) throws ActionException {

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
					throw new ActionException(e.getMessage());
				}

			}
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}

		if (previouslyContained)
			throw new ActionException("observatory already exists");
	}

	public void unregisterObservatory(String observatory)
			throws ActionException {

		boolean previouslyContained = false;

		try {
			previouslyContained = obsService.contains(observatory);

			if (!previouslyContained) {
				obsService.remove(observatory);
			}

		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}

		if (!previouslyContained)
			throw new ActionException("observatory does not exist");

	}

	public void setObservatoryInformation(String observatory,
			ObservatoryInformation info) throws ActionException {

		try {
			if (info.getCity() != null)
				obsService.setCity(observatory, info.getCity());
			if (info.getCountry() != null)
				obsService.setCountry(observatory, info.getCountry());
			obsService.setLightPollution(observatory, info.getLightPollution());
			obsService.setVisibilityRatio(observatory,
					info.getVisibilityRatio());

		} catch (NullPointerException e) {
			throw new ActionException("observatory does not exist");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}

	}

	public ObservatoryInformation getObservatoryInformation(String observatory)
			throws ActionException {

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
				throw new ActionException("observatory does not exist");
			}
		} catch (NullPointerException e) {
			throw new ActionException("observatory does not exist");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public List<String> getAllObservatoryNames() throws ActionException {

		try {
			List<String> names = obsService.getAllNames();
			names = obsService.getAllNames();

			return names;
		} catch (NullPointerException e) {
			throw new ActionException("no observatories available");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public List<String> getAllInterativeRTs() throws ActionException {
		try {
			List<String> rts = rtService.getAllInteractive();

			return rts;

		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public List<String> getAllBatchRTs() throws ActionException {

		try {
			List<String> rts = rtService.getAllBatch();

			return rts;

		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public List<String> getAllRTInObservatory(String observatory)
			throws ActionException {

		try {
			int oid = obsService.getByName(observatory).getOid();

			List<String> rt = rtService.getByObservatoryId(oid);

			return rt;

		} catch (NullPointerException e) {
			throw new ActionException("no telescope-observatory relations");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public String getRTUrl(String rt) throws ActionException {

		RTEntry entry = null;

		try {
			entry = rtService.get(rt);
		} catch (NullPointerException e) {
			throw new ActionException("rt does not exist");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}

		if (entry != null)
			return entry.getUrl();
		else
			throw new ActionException("rt does not exist");
	}

	public String getRTPort(String rt) throws ActionException {

		RTEntry entry = null;

		try {
			entry = rtService.get(rt);
		} catch (NullPointerException e) {
			throw new ActionException("rt does not exist");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}

		if (entry != null)
			return entry.getPort();
		else
			throw new ActionException("rt does not exist");
	}

	public void setRTDescription(String rt, String description)
			throws ActionException {

		try {
			rtService.setDescription(rt, description);

		} catch (NullPointerException e) {
			throw new ActionException("rt does not exist");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}

	}

	public String getRTDescription(String rt) throws ActionException {

		String description = null;

		try {
			description = rtService.getDescription(rt);

			return description;
		} catch (NullPointerException e) {
			throw new ActionException("rt does not exist");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public Date getRTDate(String rt) throws ActionException {

		Date date = null;

		try {
			date = rtService.getDate(rt);

			return date;
		} catch (NullPointerException e) {
			throw new ActionException("rt does not exist");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public void setRTImage(String rt, String image) throws ActionException {

		try {
			rtService.setImage(rt, image);

		} catch (NullPointerException e) {
			throw new ActionException("rt does not exist");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}

	}

	public String getRTImage(String rt) throws ActionException {

		String image = null;

		try {
			image = rtService.getImage(rt);

			return image;
		} catch (NullPointerException e) {
			throw new ActionException("rt does not exist");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public String getRTOwner(String rt) throws ActionException {

		String owner = null;

		try {
			owner = rtService.getOwner(rt);

			return owner;
		} catch (NullPointerException e) {
			throw new ActionException("rt does not exist");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public RTCoordinates getRTCoordinates(String rt) throws ActionException {

		double value;
		RTCoordinates coordinates = new RTCoordinates();

		try {
			value = rtService.getLatitude(rt);
			coordinates.setLatitude(value);
			value = rtService.getLongitude(rt);
			coordinates.setLongitude(value);

			return coordinates;
		} catch (NullPointerException e) {
			throw new ActionException("rt does not exist");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public RTCredentials getRTCredentials(String rt) throws ActionException {

		RTEntry entry = null;
		RTCredentials credentials = new RTCredentials();

		try {
			entry = rtService.get(rt);

			credentials.setUser(entry.getUser());
			credentials.setPassword(entry.getPassword());

			return credentials;

		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public RTAvailability getRTAvailability(String rt) throws ActionException {

		RTAvailabilityEntry entry = null;
		RTAvailability availability = new RTAvailability();

		try {

			if (this.containsRT(rt)) {

				entry = rtService.getAvailability(rt);

				availability.setEndingTime(entry.getEnding_avl());
				availability.setStartingTime(entry.getStarting_avl());

				return availability;
			} else {
				RTRepositoryException e = new RTRepositoryException(
						"rt unknown");
				e.getAction().put("rt", rt);
				throw e;
			}

		} catch (NullPointerException e) {
			throw new ActionException("no availability defined");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public void setRTAvailability(String rt, RTAvailability availability)
			throws ActionException {

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
			throw new ActionException(e.getMessage());
		}

		if (!alreadyContained) {
			throw new ActionException("rt does not exist");
		}
	}

	public void setRTCoordinates(String rt, RTCoordinates coords)
			throws ActionException {

		try {
			rtService.setCoordinates(rt, coords.getLatitude(),
					coords.getLongitude());

		} catch (NullPointerException e) {
			throw new ActionException("rt does not exist");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public void setRTObservatory(String rt, String observatory)
			throws ActionException {

		ObservatoryEntry obsEntry = null;

		try {
			obsEntry = obsService.getByName(observatory);
		} catch (NullPointerException e) {
			throw new ActionException("rt does not exist");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}

		int oid = obsEntry.getOid();

		try {
			rtService.setObservatory(rt, oid);

		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}

	}

	public String getRTObservatory(String rt) throws ActionException {

		int oid;

		try {
			oid = rtService.getObservatory(rt);
		} catch (NullPointerException e) {
			throw new ActionException(
					"observatory-telescope relation does not exist");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}

		try {
			ObservatoryEntry obsEntry = obsService.getById(oid);

			return obsEntry.getName();
		} catch (NullPointerException e) {
			throw new ActionException("no observatory defined");
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}
}
