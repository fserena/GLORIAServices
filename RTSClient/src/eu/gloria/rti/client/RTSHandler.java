package eu.gloria.rti.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.DeviceNotAvailableException;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.IncorrectDeviceTypeException;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.base.ServerNotAvailableException;
import eu.gloria.gs.services.teleoperation.base.ServerHandler;
import eu.gloria.gs.services.teleoperation.ccd.ImageExtensionFormat;
import eu.gloria.gs.services.teleoperation.ccd.ImageNotAvailableException;
import eu.gloria.gs.services.teleoperation.ccd.ImageTransferFailedException;
import eu.gloria.gs.services.teleoperation.focuser.NotAbsoluteFocuserException;
import eu.gloria.gs.services.teleoperation.mount.TrackingRate;
import eu.gloria.rt.entity.device.ActivityContinueStateCamera;
import eu.gloria.rt.entity.device.ActivityStateDomeOpening;
import eu.gloria.rt.entity.device.ActivityStateMount;
import eu.gloria.rt.entity.device.Device;
import eu.gloria.rt.entity.device.DeviceCamera;
import eu.gloria.rt.entity.device.DeviceDome;
import eu.gloria.rt.entity.device.DeviceMount;
import eu.gloria.rt.entity.device.DeviceType;
import eu.gloria.rt.entity.device.ImageFormat;
import eu.gloria.rt.entity.device.TrackingRateType;
import eu.gloria.rti.GloriaRti;
import eu.gloria.rti.RtiError;
import eu.gloria.rti.client.devices.CCD;
import eu.gloria.rti.client.devices.Focuser;
import eu.gloria.rti.client.devices.Dome;
import eu.gloria.rti.client.devices.Mount;
import eu.gloria.rti.client.devices.Scam;
import eu.gloria.rti.factory.ProxyFactory;

public class RTSHandler implements ServerHandler {

	private GloriaRti rtsPort;
	private static String port;
	private static String serviceName;
	private static String user;
	private static String password;

	static {

		Properties properties = new Properties();
		try {
			InputStream in = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("rtsclient.properties");

			properties.load(in);
			in.close();

			port = (String) properties.get("port");
			serviceName = (String) properties.get("service_name");
			user = (String) properties.get("user");
			password = (String) properties.get("password");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public RTSHandler(String host) throws TeleoperationException {

		URL urlWsdl = null;
		try {
			urlWsdl = new URL("https://" + host + ":" + port + "/"
					+ serviceName + "/gloria_rti.wsdl");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // may be file url-->
			// file:\\c:\\tmp\\gloria_rti.wsdl
		URL urlWs = null;
		try {
			urlWs = new URL("https://" + host + ":" + port + "/" + serviceName
					+ "/services/gloria_rtiSOAP?wsdl");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ProxyFactory proxyFactory = new ProxyFactory();
		try {
			rtsPort = proxyFactory.getProxy(urlWsdl, urlWs, false, user,
					password);
		} catch (Exception e) {
			throw new ServerNotAvailableException("Get rt handler for " + host);
		}

		if (rtsPort == null) {
			throw new ServerNotAvailableException("Get rt handler for " + host);
		}
	}

	public DeviceHandler getDeviceHandler(String name, DeviceType type)
			throws TeleoperationException {
		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, name, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(name);
		} catch (NullPointerException ne) {
			throw new ServerNotAvailableException("Get device handler for " + name
					+ " " + type.name());
		}

		if (device.getType().equals(type)) {
			if (type.equals(DeviceType.CCD)) {
				return new CCD(this, name);
			} else if (type.equals(DeviceType.MOUNT)) {
				return new Mount(this, name);
			} else if (type.equals(DeviceType.SURVEILLANCE_CAMERA)) {
				return new Scam(this, name);
			} else if (type.equals(DeviceType.FOCUS)) {
				return new Focuser(this, name);
			} else if (type.equals(DeviceType.DOME)) {
				return new Dome(this, name);
			}
		}

		throw new IncorrectDeviceTypeException(name);
	}

	public GloriaRti getPort() {
		return this.rtsPort;
	}

	public boolean isConnected(String device) throws TeleoperationException {
		try {
			return rtsPort.devIsConnected(null, device);
		} catch (RtiError e) {
			throw new TeleoperationException("The device " + device
					+ " does not exist or its information cannot be recovered");
		} catch (NullPointerException ne) {
			throw new TeleoperationException("The RTS is not available.");
		}
	}

	public long getBrightness(String camera) throws TeleoperationException {

		String actionMessage = "Get brightness of " + camera;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(actionMessage);
		}

		try {
			if (device.getType().equals(DeviceType.CCD)) {
				return rtsPort.camGetBrightness(null, camera);
			} else {
				return rtsPort.scamGetBrightness(null, camera);
			}
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void setBrightness(String camera, double value) throws TeleoperationException {

		String actionMessage = "Set brightness of " + camera + " to " + value;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(actionMessage);
		}

		try {

			if (device.getType().equals(DeviceType.CCD)) {
				rtsPort.camSetBrightness(null, camera, (long) value);
			} else {
				rtsPort.scamSetBrightness(null, camera, (long) value);
			}
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public double getExposureTime(String camera) throws TeleoperationException {

		String actionMessage = "Get exposure of " + camera;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(actionMessage);
		}

		try {
			if (device.getType().equals(DeviceType.CCD)) {
				return rtsPort.camGetExposureTime(null, camera);
			} else {
				return rtsPort.scamGetExposureTime(null, camera);
			}

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void setExposureTime(String camera, double value)
			throws TeleoperationException {

		String actionMessage = "Set exposure of " + camera + " to " + value;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(actionMessage);
		}

		try {
			if (device.getType().equals(DeviceType.CCD)) {
				rtsPort.camSetExposureTime(null, camera, value);
			} else {
				rtsPort.scamSetExposureTime(null, camera, (long) value);
			}
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public boolean getAutoExposure(String camera) throws TeleoperationException {

		String actionMessage = "Get auto-exposure of " + camera;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {

			return rtsPort.camGetAutoExposureTime(null, camera);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void setAutoExposure(String camera, boolean mode)
			throws TeleoperationException {

		String actionMessage = "Set auto-exposure of " + camera + " to " + mode;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			rtsPort.camSetAutoExposureTime(null, camera, mode);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public long getContrast(String camera) throws TeleoperationException {

		String actionMessage = "Get contrast of " + camera;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(actionMessage);
		}

		try {
			if (device.getType().equals(DeviceType.CCD)) {
				return rtsPort.camGetContrast(null, camera);
			} else {
				return rtsPort.scamGetContrast(null, camera);
			}
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void setContrast(String camera, long value) throws TeleoperationException {

		String actionMessage = "Set contrast of " + camera + " to " + value;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(actionMessage);
		}

		try {
			if (device.getType().equals(DeviceType.CCD)) {
				rtsPort.camSetContrast(null, camera, value);
			} else {
				rtsPort.scamSetContrast(null, camera, value);
			}
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public long getGain(String camera) throws TeleoperationException {

		String actionMessage = "Get gain of " + camera;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			return rtsPort.camGetGain(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void setGain(String camera, long value) throws TeleoperationException {

		String actionMessage = "Set gain of " + camera + " to " + value;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			rtsPort.camSetGain(null, camera, value);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public boolean getAutoGain(String camera) throws TeleoperationException {
		String actionMessage = "Get auto-gain of " + camera;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			return rtsPort.camGetAutoGain(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void setAutoGain(String camera, boolean mode) throws TeleoperationException {
		String actionMessage = "Set auto-gain of " + camera + " to " + mode;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			rtsPort.camSetAutoGain(null, camera, mode);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public String startExposure(String camera) throws TeleoperationException {
		String actionMessage = "Start exposure of " + camera;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			return rtsPort.camStartExposure(null, camera, true);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public String startContinueMode(String camera) throws TeleoperationException {

		String actionMessage = "Start continue mode of " + camera;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			return rtsPort.camStartContinueMode(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void stopContinueMode(String camera) throws TeleoperationException {
		String actionMessage = "Stop continue mode of " + camera;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			rtsPort.camStopContinueMode(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public String getImageURL(String camera, String imageId,
			ImageExtensionFormat format) throws TeleoperationException {

		String actionMessage = "Get URL of " + imageId + " image from camera "
				+ camera;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(actionMessage);
		}

		try {
			if (device.getType().equals(DeviceType.CCD)) {
				ImageFormat imageFormat = ImageFormat.valueOf(format.name());

				return rtsPort.camGetImageURLProperFormat(null, camera,
						imageId, imageFormat);
			} else {
				return rtsPort.scamGetImageURL(null, camera);
			}
		} catch (RtiError e) {
			if (e.getMessage().equals("NOT_AVAILABLE"))
				throw new ImageNotAvailableException(actionMessage);
			else if (e.getMessage().equals("FAILED"))
				throw new ImageTransferFailedException(actionMessage);
			else
				throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public String getContinuousImagePath(String camera) throws TeleoperationException {
		String actionMessage = "Get URL of the continuous image of camera "
				+ camera;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			return rtsPort.camGetContinueModeImagePath(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public ActivityContinueStateCamera getCCDState(String ccd)
			throws TeleoperationException {

		String actionMessage = "Get continue mode state of " + ccd;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			DeviceCamera deviceCamera = (DeviceCamera) rtsPort.devGetDevice(
					null, ccd, false);

			return deviceCamera.getActivityContinueState();
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public ActivityContinueStateCamera getSCamState(String scam)
			throws TeleoperationException {
		String actionMessage = "Get continue mode state of " + scam;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			DeviceCamera deviceCamera = (DeviceCamera) rtsPort.devGetDevice(
					null, scam, false);

			return deviceCamera.getActivityContinueState();
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void open(String dome) throws TeleoperationException {
		String actionMessage = "Open dome " + dome;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			rtsPort.domOpen(null, dome, 0);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void close(String dome) throws TeleoperationException {
		String actionMessage = "Close dome " + dome;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			rtsPort.domClose(null, dome, 0);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void parkDome(String dome) throws TeleoperationException {
		String actionMessage = "Park dome " + dome;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			rtsPort.domPark(null, dome);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void parkMount(String mount) throws TeleoperationException {
		String actionMessage = "Park mount " + mount;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			rtsPort.mntPark(null, mount);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public double getDomeAzimuth(String dome) throws TeleoperationException {
		String actionMessage = "Get dome azimuth of " + dome;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {

			return rtsPort.domGetAzimuth(null, dome);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public boolean getDomeTracking(String dome) throws TeleoperationException {
		String actionMessage = "Get dome tracking of " + dome;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			return rtsPort.domGetTracking(null, dome);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void setDomeTracking(String dome, boolean mode) throws TeleoperationException {
		String actionMessage = "Set dome tracking of " + dome + " to " + mode;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			rtsPort.domSetTracking(null, dome, mode);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public ActivityStateDomeOpening getDomeState(String dome)
			throws TeleoperationException {
		String actionMessage = "Get dome state of " + dome;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {

			DeviceDome domeDevice = (DeviceDome) rtsPort.devGetDevice(null,
					dome, false);

			return domeDevice.getActivityStateOpening();
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void moveNorth(String mount) throws TeleoperationException {
		String actionMessage = "Move " + mount + "  to north";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			rtsPort.mntMoveNorth(null, mount);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void moveSouth(String mount) throws TeleoperationException {
		String actionMessage = "Move " + mount + "  to south";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			rtsPort.mntMoveSouth(null, mount);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void moveEast(String mount) throws TeleoperationException {
		String actionMessage = "Move " + mount + "  to east";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			rtsPort.mntMoveEast(null, mount);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void moveWest(String mount) throws TeleoperationException {
		String actionMessage = "Move " + mount + "  to west";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			rtsPort.mntMoveWest(null, mount);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void setSlewRate(String mount, String rate) throws TeleoperationException {
		String actionMessage = "Set slew rate of " + mount + " to " + rate;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			rtsPort.mntSetSlewRate(null, mount, rate);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void setTrackingRate(String mount, TrackingRate rate)
			throws TeleoperationException {
		String actionMessage = "Set tracking rate of " + mount + " to "
				+ rate.name();

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			rtsPort.mntSetTrackingRate(null, mount,
					TrackingRateType.valueOf(rate.name()));
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void setMountTracking(String mount, boolean mode)
			throws TeleoperationException {

		String actionMessage = "Set tracking mode of " + mount + " to " + mode;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			rtsPort.mntSetTracking(null, mount, mode);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void slewToObject(String mount, String object) throws TeleoperationException {
		String actionMessage = "Slew " + mount + " to " + object;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			rtsPort.mntSlewObject(null, mount, object);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public ActivityStateMount getMountState(String mount) throws TeleoperationException {
		String actionMessage = "Get state of " + mount;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {

			DeviceMount mountDevice = (DeviceMount) rtsPort.devGetDevice(null,
					mount, false);

			return mountDevice.getActivityState();
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void relativeFocuserMove(String focuser, long steps)
			throws TeleoperationException {

		String actionMessage = "Relative " + focuser + " move of " + steps
				+ " steps";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		long effectiveMove = steps;

		try {
			if (rtsPort.focIsAbsolute(null, focuser)) {
				long currentPosition = rtsPort.focGetPosition(null, focuser);
				effectiveMove = currentPosition + steps;
			}

			rtsPort.focMove(null, focuser, effectiveMove);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public void absoluteFocuserMove(String focuser, long position)
			throws TeleoperationException {

		String actionMessage = "Absolute " + focuser + " move to " + position
				+ " position";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		long effectiveMove = position;

		try {
			if (!rtsPort.focIsAbsolute(null, focuser)) {
				long currentPosition = rtsPort.focGetPosition(null, focuser);
				effectiveMove = position - currentPosition;
			}

			rtsPort.focMove(null, focuser, effectiveMove);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}
	}

	public long getFocuserAbsolutePosition(String focuser) throws TeleoperationException {

		String actionMessage = "Get absolute position of" + focuser;

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage);
		}

		try {
			if (rtsPort.focIsAbsolute(null, focuser)) {
				return rtsPort.focGetPosition(null, focuser);
			}
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage);
		}

		throw new NotAbsoluteFocuserException(actionMessage);
	}
}
