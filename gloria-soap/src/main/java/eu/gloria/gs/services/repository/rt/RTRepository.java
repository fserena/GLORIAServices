package eu.gloria.gs.services.repository.rt;

import java.util.List;

import eu.gloria.gs.services.core.GSLogProducerService;
import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.log.action.Param;
import eu.gloria.gs.services.log.action.ServiceOperation;
import eu.gloria.gs.services.repository.rt.data.DeviceInformation;
import eu.gloria.gs.services.repository.rt.data.DeviceType;
import eu.gloria.gs.services.repository.rt.data.ObservatoryInformation;
import eu.gloria.gs.services.repository.rt.data.RTAvailability;
import eu.gloria.gs.services.repository.rt.data.RTCoordinates;
import eu.gloria.gs.services.repository.rt.data.RTCredentials;
import eu.gloria.gs.services.repository.rt.data.RTInformation;
import eu.gloria.gs.services.repository.rt.data.RTRepositoryAdapter;
import eu.gloria.gs.services.repository.rt.data.ServerKeyData;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rt.entity.device.Device;
import eu.gloria.rti.GloriaRti;
import eu.gloria.rti.RtiError;
import eu.gloria.rti.client.RTSManager;

public class RTRepository extends GSLogProducerService implements
		RTRepositoryInterface {

	private RTRepositoryAdapter adapter;

	public RTRepository() {
		super(RTRepository.class.getSimpleName());
	}

	public void setAdapter(RTRepositoryAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	@ServiceOperation(name = "register batch rt")
	public void registerBatchRT(@Param(name = "rt") String rt,
			@Param(name = "owner") String owner, String url, String port,
			String user, String password) throws RTRepositoryException {

		Action action = new Action(RTRepository.class, "registerBatchRT", rt,
				owner);

		try {
			adapter.registerBatchRT(rt, owner, url, port, user, password);
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw new RTRepositoryException(action);
		}
	}

	@Override
	@ServiceOperation(name = "register interactive rt")
	public void registerInteractiveRT(@Param(name = "rt") String rt,
			@Param(name = "owner") String owner, String url, String port,
			String user, String password) throws RTRepositoryException {
		Action action = new Action(RTRepository.class, "registerInteractiveRT",
				rt, owner);

		GloriaRti rti;
		try {

			ServerKeyData keyData = new ServerKeyData();
			keyData.setUrl(url);
			keyData.setPort(port);
			RTCredentials rtCredentials = new RTCredentials();
			rtCredentials.setUser(user);
			rtCredentials.setPassword(password);
			keyData.setCredentials(rtCredentials);

			rti = RTSManager.getReference().getRTS(keyData).getPort();

			try {
				List<Device> devices = rti.devGetDevices(null, false);

				try {
					adapter.registerInteractiveRT(rt, owner, url, port, user,
							password);
				} catch (ActionException e) {
					this.logException(action, e);
					throw new RTRepositoryException(action);
				}

				for (Device device : devices) {
					DeviceType type = DeviceType.valueOf(device.getType()
							.name());

					try {
						this.registerDevice(type, device.getShortName());
					} catch (RTRepositoryException e) {

					}

					this.addRTDevice(type, rt, device.getShortName(),
							device.getShortName());
				}

				this.logInfo(action);

			} catch (RtiError e) {
				action.put("step", "retrieving devices");
				this.logException(action, e);
			}

		} catch (NullPointerException | TeleoperationException e) {
			this.logException(action, e);
			throw new RTRepositoryException(action);
		}

	}

	@Override
	@ServiceOperation(name = "unregister rt")
	public void unregisterRT(@Param(name = "rt") String rt)
			throws RTRepositoryException {
		Action action = new Action(RTRepository.class, "unregisterRT", rt);

		try {
			adapter.unregisterRT(rt);
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw new RTRepositoryException(action);
		}

	}

	@Override
	@ServiceOperation(name = "contains rt")
	public boolean containsRT(@Param(name = "rt") String rt)
			throws RTRepositoryException {
		Action action = new Action(RTRepository.class, "containsRT", rt);

		try {
			boolean contains = adapter.containsRT(rt);
			return contains;
		} catch (ActionException e) {
			this.logException(action, e);
			throw new RTRepositoryException(action);
		}
	}

	@Override
	@ServiceOperation(name = "add rt device")
	public void addRTDevice(@Param(name = "type") DeviceType type,
			@Param(name = "rt") String rt, String name, String model)
			throws RTRepositoryException {
		Action action = new Action(RTRepository.class, "addRTDevice", type, rt);

		try {
			adapter.addDevice(type, rt, name, model);
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw new RTRepositoryException(action);
		}

	}

	@Override
	@ServiceOperation(name = "remove rt device")
	public void removeRTDevice(@Param(name = "rt") String rt,
			@Param(name = "device") String device) throws RTRepositoryException {
		Action action = new Action(RTRepository.class, "removeRTDevice", rt,
				device);

		try {
			adapter.removeDevice(rt, device);
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw new RTRepositoryException(action);
		}
	}

	@Override
	@ServiceOperation(name = "get rt device info")
	public DeviceInformation getRTDeviceInformation(String rt, String device)
			throws RTRepositoryException {

		try {
			DeviceInformation devInfo = adapter.getRTDeviceInformation(rt,
					device);

			return devInfo;
		} catch (ActionException e) {

			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public List<String> getRTDeviceNames(String rt, DeviceType type)
			throws RTRepositoryException {

		try {
			List<String> deviceNames = adapter.getDeviceNames(rt, type);

			return deviceNames;
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public List<String> getDeviceNames(String rt) throws RTRepositoryException {

		try {
			List<String> deviceNames = adapter.getAllDeviceNames(rt);

			return deviceNames;
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void registerDevice(DeviceType type, String model)
			throws RTRepositoryException {

		try {
			adapter.registerDevice(type, model);

		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void unregisterDevice(DeviceType type, String model)
			throws RTRepositoryException {

		try {
			adapter.unregisterDevice(type, model);

		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public DeviceInformation getDeviceInformation(DeviceType type, String model)
			throws RTRepositoryException {

		try {
			DeviceInformation devInfo = adapter.getDeviceInformation(type,
					model);

			return devInfo;
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void setDeviceInformation(DeviceType type, String model,
			DeviceInformation info) throws RTRepositoryException {

		try {
			adapter.setDeviceInformation(type, model, info);

		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}

	}

	@Override
	public List<String> getDeviceModels(DeviceType type)
			throws RTRepositoryException {

		try {
			List<String> models = adapter.getDeviceModels(type);

			return models;
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public List<DeviceType> getAllDeviceTypes() throws RTRepositoryException {

		try {
			List<DeviceType> types = adapter.getAllDeviceTypes();

			return types;
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void registerObservatory(String observatory, String city,
			String country) throws RTRepositoryException {

		try {
			adapter.registerObservatory(observatory, city, country);

		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void unregisterObservatory(String observatory)
			throws RTRepositoryException {

		try {
			adapter.unregisterObservatory(observatory);

		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void setObservatoryInformation(String observatory,
			ObservatoryInformation info) throws RTRepositoryException {

		try {
			adapter.setObservatoryInformation(observatory, info);

		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public ObservatoryInformation getObservatoryInformation(String observatory)
			throws RTRepositoryException {

		try {

			ObservatoryInformation obsInfo = adapter
					.getObservatoryInformation(observatory);

			return obsInfo;
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public List<String> getAllObservatoryNames() throws RTRepositoryException {

		try {
			List<String> obsNames = adapter.getAllObservatoryNames();

			return obsNames;
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public List<String> getAllRTInObservatory(String observatory)
			throws RTRepositoryException {

		try {
			List<String> rts = adapter.getAllRTInObservatory(observatory);

			if (rts == null) {
				Action action = new Action();
				action.put("cause", "no telescopes");
				throw new RTRepositoryException(action);
			}

			return rts;
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public String getRTUrl(String rt) throws Exception {

		try {
			String url = adapter.getRTUrl(rt);

			return url;
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void setRTDescription(String rt, String description)
			throws RTRepositoryException {
		try {
			adapter.setRTDescription(rt, description);
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public String getRTDescription(String rt) throws RTRepositoryException {
		try {
			String description = adapter.getRTDescription(rt);

			return description;
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void setRTImage(String rt, String image)
			throws RTRepositoryException {
		try {
			adapter.setRTImage(rt, image);
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public String getRTImage(String rt) throws RTRepositoryException {
		try {
			String image = adapter.getRTImage(rt);

			return image;
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public String getRTOwner(String rt) throws RTRepositoryException {
		try {
			String owner = adapter.getRTOwner(rt);

			return owner;
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public RTCoordinates getRTCoordinates(String rt)
			throws RTRepositoryException {
		try {
			RTCoordinates coords = adapter.getRTCoordinates(rt);

			return coords;
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void setRTCoordinates(String rt, RTCoordinates coords)
			throws RTRepositoryException {
		try {
			adapter.setRTCoordinates(rt, coords);

		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void setRTObservatory(String rt, String observatory)
			throws RTRepositoryException {
		try {
			adapter.setRTObservatory(rt, observatory);

		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public String getRTObservatory(String rt) throws RTRepositoryException {
		try {
			String observatory = adapter.getRTObservatory(rt);

			return observatory;
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public RTAvailability getRTAvailability(String rt)
			throws RTRepositoryException {
		try {
			RTAvailability availability = adapter.getRTAvailability(rt);

			return availability;
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void setRTAvailability(String rt, RTAvailability availability)
			throws RTRepositoryException {
		try {
			adapter.setRTAvailability(rt, availability);

		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public RTInformation getRTInformation(String rt)
			throws RTRepositoryException {
		RTInformation rtInfo = new RTInformation();

		try {
			rtInfo.setDescription(adapter.getRTDescription(rt));
			rtInfo.setOwner(adapter.getRTOwner(rt));
			rtInfo.setCoordinates(adapter.getRTCoordinates(rt));
			rtInfo.setAvailability(adapter.getRTAvailability(rt));
			rtInfo.setObservatory(adapter.getRTObservatory(rt));
			rtInfo.setUrl(adapter.getRTUrl(rt));
			rtInfo.setPort(adapter.getRTPort(rt));
			rtInfo.setUser(adapter.getRTCredentials(rt).getUser());
			rtInfo.setPassword(adapter.getRTCredentials(rt).getPassword());
			rtInfo.setImage(adapter.getRTImage(rt));
			rtInfo.setRegistrationDate(adapter.getRTDate(rt));

			return rtInfo;

		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}

	}

	@Override
	public List<String> getAllInteractiveRTs() throws RTRepositoryException {
		try {
			List<String> rts = adapter.getAllInterativeRTs();

			if (rts == null) {
				Action action = new Action();
				action.put("cause", "no telescopes");
				throw new RTRepositoryException(action);
			}

			return rts;
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public List<String> getAllBatchRTs() throws RTRepositoryException {
		try {
			List<String> rts = adapter.getAllBatchRTs();

			if (rts == null) {
				Action action = new Action();
				action.put("cause", "no telescopes");
				throw new RTRepositoryException(action);
			}

			return rts;
		} catch (ActionException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

}
