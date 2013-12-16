package eu.gloria.gs.services.repository.rt;

import java.util.List;

import eu.gloria.gs.services.core.GSLogProducerService;
import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.repository.rt.data.DeviceInformation;
import eu.gloria.gs.services.repository.rt.data.DeviceType;
import eu.gloria.gs.services.repository.rt.data.ObservatoryInformation;
import eu.gloria.gs.services.repository.rt.data.RTAvailability;
import eu.gloria.gs.services.repository.rt.data.RTCoordinates;
import eu.gloria.gs.services.repository.rt.data.RTCredentials;
import eu.gloria.gs.services.repository.rt.data.RTInformation;
import eu.gloria.gs.services.repository.rt.data.RTRepositoryAdapter;
import eu.gloria.gs.services.repository.rt.data.ServerKeyData;
import eu.gloria.gs.services.repository.rt.data.dbservices.RTRepositoryAdapterException;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rt.entity.device.Device;
import eu.gloria.rti.GloriaRti;
import eu.gloria.rti.RtiError;
import eu.gloria.rti.client.RTSManager;

public class RTRepository extends GSLogProducerService implements
		RTRepositoryInterface {

	private RTRepositoryAdapter adapter;

	public RTRepository() {
	}

	public void setAdapter(RTRepositoryAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public void registerBatchRT(String rt, String owner, String url,
			String port, String user, String password)
			throws RTRepositoryException {

		LogAction action = new LogAction();

		action.put("sender", this.getUsername());
		action.put("operation", "register batch rt");
		action.put("owner", this.getClientUsername());

		try {
			adapter.registerBatchRT(rt, owner, url, port, user, password);
		} catch (RTRepositoryAdapterException e) {

			action.put("cause", "internal error");			
			this.logError(getClientUsername(), action);
			action.put("more", action);
			throw new RTRepositoryException(action);
		}

		/*
		 * Gloria rti; try {
		 * 
		 * ServerKeyData keyData = new ServerKeyData(); keyData.setUrl(url);
		 * RTCredentials rtCredentials = new RTCredentials();
		 * rtCredentials.setUser(user); rtCredentials.setPassword(password);
		 * keyData.setCredentials(rtCredentials);
		 * 
		 * rti = RTSManager.getReference().getRTS(keyData).getPort();
		 * 
		 * try { List<Device> devices = rti.devGetDevices(null, false);
		 * 
		 * for (Device device : devices) { DeviceType type =
		 * DeviceType.valueOf(device.getType() .name());
		 * 
		 * try { this.registerDevice(type, device.getShortName()); } catch
		 * (RTRepositoryException e) {
		 * 
		 * }
		 * 
		 * this.addRTDevice(type, rt, device.getShortName(),
		 * device.getShortName()); }
		 * 
		 * } catch (RtiError e) { e.printStackTrace(); }
		 * 
		 * } catch (NullPointerException en) { throw new
		 * RTRepositoryException("The RTS on " + url + " is not available."); }
		 * catch (TeleoperationException e1) { throw new
		 * RTRepositoryException(e1.getAction()); }
		 */

		this.logInfo(this.getClientUsername(), action);
	}

	@Override
	public void registerInteractiveRT(String rt, String owner, String url,
			String port, String user, String password)
			throws RTRepositoryException {

		LogAction action = new LogAction();

		action.put("sender", this.getUsername());
		action.put("operation", "register interactive rt");
		action.put("owner", this.getClientUsername());
		action.put("rt", rt);

		try {
			adapter.registerInteractiveRT(rt, owner, url, port, user, password);
		} catch (RTRepositoryAdapterException e) {

			action.put("cause", e.getAction());
			this.logError(this.getClientUsername(), action);
			this.logError(getClientUsername(), action);
			action.put("more", action);
			throw new RTRepositoryException(action);
		}

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
				
				this.logInfo(this.getClientUsername(), action);

			} catch (RtiError e) {
				action.put("step", "retrieving devices");
				this.logError(this.getClientUsername(), action);
			}

		} catch (NullPointerException en) {
			
			action.put("cause", "internal error");
			this.logError(getClientUsername(), action);
			throw new RTRepositoryException(action);
		} catch (TeleoperationException e1) {
			action.put("cause", "rti access");
			this.logError(getClientUsername(), action);			
			throw new RTRepositoryException(action);
		}

	}

	@Override
	public void unregisterRT(String rt) throws RTRepositoryException {

		try {
			adapter.unregisterRT(rt);

		} catch (RTRepositoryAdapterException ce) {
			throw new RTRepositoryException(ce.getAction());
		}

	}

	@Override
	public boolean containsRT(String rt) throws RTRepositoryException {

		try {

			boolean contains = adapter.containsRT(rt);

			return contains;
		} catch (RTRepositoryAdapterException ce) {
			throw new RTRepositoryException(ce.getAction());
		}
	}

	@Override
	public void addRTDevice(DeviceType type, String rt, String name,
			String model) throws RTRepositoryException {

		try {
			adapter.addDevice(type, rt, name, model);

		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}

	}

	@Override
	public void removeRTDevice(String rt, String device)
			throws RTRepositoryException {

		try {
			adapter.removeDevice(rt, device);

		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public DeviceInformation getRTDeviceInformation(String rt, String device)
			throws RTRepositoryException {

		try {
			DeviceInformation devInfo = adapter.getRTDeviceInformation(rt,
					device);

			return devInfo;
		} catch (RTRepositoryAdapterException e) {

			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public List<String> getRTDeviceNames(String rt, DeviceType type)
			throws RTRepositoryException {

		try {
			List<String> deviceNames = adapter.getDeviceNames(rt, type);

			return deviceNames;
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public List<String> getDeviceNames(String rt) throws RTRepositoryException {

		try {
			List<String> deviceNames = adapter.getAllDeviceNames(rt);

			return deviceNames;
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void registerDevice(DeviceType type, String model)
			throws RTRepositoryException {

		try {
			adapter.registerDevice(type, model);

		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void unregisterDevice(DeviceType type, String model)
			throws RTRepositoryException {

		try {
			adapter.unregisterDevice(type, model);

		} catch (RTRepositoryAdapterException e) {
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
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void setDeviceInformation(DeviceType type, String model,
			DeviceInformation info) throws RTRepositoryException {

		try {
			adapter.setDeviceInformation(type, model, info);

		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}

	}

	@Override
	public List<String> getDeviceModels(DeviceType type)
			throws RTRepositoryException {

		try {
			List<String> models = adapter.getDeviceModels(type);

			return models;
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public List<DeviceType> getAllDeviceTypes() throws RTRepositoryException {

		try {
			List<DeviceType> types = adapter.getAllDeviceTypes();

			return types;
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void registerObservatory(String observatory, String city,
			String country) throws RTRepositoryException {

		try {
			adapter.registerObservatory(observatory, city, country);

		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void unregisterObservatory(String observatory)
			throws RTRepositoryException {

		try {
			adapter.unregisterObservatory(observatory);

		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void setObservatoryInformation(String observatory,
			ObservatoryInformation info) throws RTRepositoryException {

		try {
			adapter.setObservatoryInformation(observatory, info);

		} catch (RTRepositoryAdapterException e) {
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
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public List<String> getAllObservatoryNames() throws RTRepositoryException {

		try {
			List<String> obsNames = adapter.getAllObservatoryNames();

			return obsNames;
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public List<String> getAllRTInObservatory(String observatory)
			throws RTRepositoryException {

		try {
			List<String> rts = adapter.getAllRTInObservatory(observatory);

			if (rts == null) {
				LogAction action = new LogAction();
				action.put("cause", "no telescopes");
				throw new RTRepositoryException(action);
			}

			return rts;
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public String getRTUrl(String rt) throws Exception {

		try {
			String url = adapter.getRTUrl(rt);

			return url;
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void setRTDescription(String rt, String description)
			throws RTRepositoryException {
		try {
			adapter.setRTDescription(rt, description);
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public String getRTDescription(String rt) throws RTRepositoryException {
		try {
			String description = adapter.getRTDescription(rt);

			return description;
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void setRTPublicKey(String rt, String pk)
			throws RTRepositoryException {
		try {
			adapter.setRTPublicKey(rt, pk);
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public String getRTPublicKey(String rt) throws RTRepositoryException {
		try {
			String pk = adapter.getRTPublicKey(rt);

			return pk;
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public String getRTOwner(String rt) throws RTRepositoryException {
		try {
			String owner = adapter.getRTOwner(rt);

			return owner;
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public RTCoordinates getRTCoordinates(String rt)
			throws RTRepositoryException {
		try {
			RTCoordinates coords = adapter.getRTCoordinates(rt);

			return coords;
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void setRTCoordinates(String rt, RTCoordinates coords)
			throws RTRepositoryException {
		try {
			adapter.setRTCoordinates(rt, coords);

		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void setRTObservatory(String rt, String observatory)
			throws RTRepositoryException {
		try {
			adapter.setRTObservatory(rt, observatory);

		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public String getRTObservatory(String rt) throws RTRepositoryException {
		try {
			String observatory = adapter.getRTObservatory(rt);

			return observatory;
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public RTAvailability getRTAvailability(String rt)
			throws RTRepositoryException {
		try {
			RTAvailability availability = adapter.getRTAvailability(rt);

			return availability;
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	@Override
	public void setRTAvailability(String rt, RTAvailability availability)
			throws RTRepositoryException {
		try {
			adapter.setRTAvailability(rt, availability);

		} catch (RTRepositoryAdapterException e) {
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

			return rtInfo;

		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.rt.RTRepositoryInterface#
	 * getAllInteractiveRTs()
	 */
	@Override
	public List<String> getAllInteractiveRTs() throws RTRepositoryException {
		try {
			List<String> rts = adapter.getAllInterativeRTs();

			if (rts == null) {
				LogAction action = new LogAction();
				action.put("cause", "no telescopes");
				throw new RTRepositoryException(action);
			}

			return rts;
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.repository.rt.RTRepositoryInterface#getAllBatchRTs
	 * ()
	 */
	@Override
	public List<String> getAllBatchRTs() throws RTRepositoryException {
		try {
			List<String> rts = adapter.getAllBatchRTs();

			if (rts == null) {
				LogAction action = new LogAction();
				action.put("cause", "no telescopes");
				throw new RTRepositoryException(action);
			}

			return rts;
		} catch (RTRepositoryAdapterException e) {
			throw new RTRepositoryException(e.getAction());
		}
	}

}
