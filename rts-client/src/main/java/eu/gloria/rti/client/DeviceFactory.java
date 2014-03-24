package eu.gloria.rti.client;

import eu.gloria.gs.services.repository.rt.data.ServerKeyData;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rt.entity.device.DeviceType;
import eu.gloria.rti.client.devices.Barometer;
import eu.gloria.rti.client.devices.CCD;
import eu.gloria.rti.client.devices.FilterWheel;
import eu.gloria.rti.client.devices.Focuser;
import eu.gloria.rti.client.devices.Dome;
import eu.gloria.rti.client.devices.Mount;
import eu.gloria.rti.client.devices.RHSensor;
import eu.gloria.rti.client.devices.Scam;
import eu.gloria.rti.client.devices.TempSensor;
import eu.gloria.rti.client.devices.WindSensor;

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

	public CCD createCCD(ServerKeyData keyData, String name) throws TeleoperationException {
		RTSHandler rts = RTSManager.getReference().getRTS(keyData);

		return (CCD) rts.getDeviceHandler(name, DeviceType.CCD);
	}

	public Dome createDome(ServerKeyData keyData, String name) throws TeleoperationException {
		RTSHandler rts = RTSManager.getReference().getRTS(keyData);

		return (Dome) rts.getDeviceHandler(name, DeviceType.DOME);
	}

	public Mount createMount(ServerKeyData keyData, String name) throws TeleoperationException {
		RTSHandler rts = RTSManager.getReference().getRTS(keyData);

		return (Mount) rts.getDeviceHandler(name, DeviceType.MOUNT);
	}

	public Scam createSurveillanceCamera(ServerKeyData keyData, String name)
			throws TeleoperationException {
		RTSHandler rts = RTSManager.getReference().getRTS(keyData);

		return (Scam) rts
				.getDeviceHandler(name, DeviceType.SURVEILLANCE_CAMERA);
	}

	public Focuser createFocuser(ServerKeyData keyData, String name) throws TeleoperationException {
		RTSHandler rts = RTSManager.getReference().getRTS(keyData);

		return (Focuser) rts.getDeviceHandler(name, DeviceType.FOCUS);
	}
	
	public FilterWheel createFilterWheel(ServerKeyData keyData, String name) throws TeleoperationException {
		RTSHandler rts = RTSManager.getReference().getRTS(keyData);

		return (FilterWheel) rts.getDeviceHandler(name, DeviceType.FW);
	}
	
	public Barometer createBarometer(ServerKeyData keyData, String name) throws TeleoperationException {
		RTSHandler rts = RTSManager.getReference().getRTS(keyData);

		return (Barometer) rts.getDeviceHandler(name, DeviceType.BAROMETER);
	}
	
	public RHSensor createRHSensor(ServerKeyData keyData, String name) throws TeleoperationException {
		RTSHandler rts = RTSManager.getReference().getRTS(keyData);

		return (RHSensor) rts.getDeviceHandler(name, DeviceType.RH_SENSOR);
	}
	
	public WindSensor createWindSensor(ServerKeyData keyData, String name) throws TeleoperationException {
		RTSHandler rts = RTSManager.getReference().getRTS(keyData);

		return (WindSensor) rts.getDeviceHandler(name, DeviceType.WIND_SPEED_SENSOR);
	}
	
	public TempSensor createTempSensor(ServerKeyData keyData, String name) throws TeleoperationException {
		RTSHandler rts = RTSManager.getReference().getRTS(keyData);

		return (TempSensor) rts.getDeviceHandler(name, DeviceType.TEMPERATURE_SENSOR);
	}
}
