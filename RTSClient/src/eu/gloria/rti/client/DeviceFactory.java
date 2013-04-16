package eu.gloria.rti.client;

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

		return rts.getCCD(name);
	}

	public Dome createDome(String host, String name) throws RTSException {
		RTSHandler rts = RTSManager.getReference().getRTS(host);

		return rts.getDome(name);
	}

	public Mount createMount(String host, String name) throws RTSException {
		RTSHandler rts = RTSManager.getReference().getRTS(host);

		return rts.getMount(name);
	}

	public Scam createSurveillanceCamera(String host, String name)
			throws RTSException {
		RTSHandler rts = RTSManager.getReference().getRTS(host);

		return rts.getSurveillanceCamera(name);
	}

	public Focuser createFocuser(String host, String name) throws RTSException {
		RTSHandler rts = RTSManager.getReference().getRTS(host);

		return rts.getFocuser(name);
	}
}