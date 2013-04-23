package eu.gloria.rti.client;

import eu.gloria.gs.services.teleoperation.base.RTSException;
import eu.gloria.rt.entity.device.DeviceType;
import eu.gloria.rti.client.devices.CCD;
import eu.gloria.rti.client.devices.Focuser;
import eu.gloria.rti.client.devices.Dome;
import eu.gloria.rti.client.devices.Mount;
import eu.gloria.rti.client.devices.Scam;

public class DeviceFactory {

	private static DeviceFactory instance = null;
	private static Object sync = new Object();

	private DeviceFactory() {

	}

	static public DeviceFactory getReference() {
		synchronized (sync) {
			if (instance == null)
				instance = new DeviceFactory();
		}

		return instance;
	}

	public CCD createCCD(String host, String name) throws RTSException {
		RTSHandler rts = RTSManager.getReference().getRTS(host);

		return (CCD) rts.getDeviceHandler(name, DeviceType.CCD);
	}

	public Dome createDome(String host, String name) throws RTSException {
		RTSHandler rts = RTSManager.getReference().getRTS(host);

		return (Dome) rts.getDeviceHandler(name, DeviceType.DOME);
	}

	public Mount createMount(String host, String name) throws RTSException {
		RTSHandler rts = RTSManager.getReference().getRTS(host);

		return (Mount) rts.getDeviceHandler(name, DeviceType.MOUNT);
	}

	public Scam createSurveillanceCamera(String host, String name)
			throws RTSException {
		RTSHandler rts = RTSManager.getReference().getRTS(host);

		return (Scam) rts
				.getDeviceHandler(name, DeviceType.SURVEILLANCE_CAMERA);
	}

	public Focuser createFocuser(String host, String name) throws RTSException {
		RTSHandler rts = RTSManager.getReference().getRTS(host);

		return (Focuser) rts.getDeviceHandler(name, DeviceType.FOCUS);
	}
}
