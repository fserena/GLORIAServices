package eu.gloria.rti.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
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
import eu.gloria.gs.services.teleoperation.generic.GenericTeleoperationException;
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
import eu.gloria.rti.client.devices.FilterWheel;
import eu.gloria.rti.client.devices.Focuser;
import eu.gloria.rti.client.devices.Dome;
import eu.gloria.rti.client.devices.Mount;
import eu.gloria.rti.client.devices.Scam;
import eu.gloria.rti.factory.ProxyFactory;

public class RTSHandler implements ServerHandler {

	private GloriaRti rtsPort;
	private static String port;
	private static String serviceName;
	private static boolean teleoperationStarted = false;

	static {

		Properties properties = new Properties();
		try {
			InputStream in = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("rtsclient.properties");

			properties.load(in);
			in.close();

			port = (String) properties.get("port");
			serviceName = (String) properties.get("service_name");
			// user = (String) properties.get("user");
			// password = (String) properties.get("password");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public RTSHandler(String host, String user, String password)
			throws TeleoperationException {

		String actionMessage = "rts/" + host + "?" + host + "->";

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
			throw new ServerNotAvailableException(actionMessage
					+ "MALFORMED_SERVER_URL");
		}

		ProxyFactory proxyFactory = new ProxyFactory();

		try {
			rtsPort = proxyFactory.getProxy(urlWsdl, urlWs, false, user,
					password);
		} catch (Exception e) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}
	}

	public void startTeleoperation() throws GenericTeleoperationException {
		try {
			rtsPort.execStopOp(null);
			teleoperationStarted = false;
		} catch (RtiError e) {
			throw new GenericTeleoperationException("/start -> PRE_STOP_FAILED");
		}

		try {
			rtsPort.execStartOp(null, null, "GLORIA", 0);
			teleoperationStarted = true;
		} catch (RtiError e) {
			throw new GenericTeleoperationException(
					"/start?secs=0 -> START_TELEOP_FAILED");
		}
	}

	public void stopTeleoperation() throws GenericTeleoperationException {
		try {
			rtsPort.execStopOp(null);
			teleoperationStarted = false;
		} catch (RtiError e) {
			throw new GenericTeleoperationException(
					"/stop -> STOP_TELEOP_FAILED");
		}
	}

	public void notifyTeleoperation(long seconds)
			throws GenericTeleoperationException {
		try {
			rtsPort.execStopOp(null);
			teleoperationStarted = false;
		} catch (RtiError e) {
			throw new GenericTeleoperationException("/start -> PRE_STOP_FAILED");
		}

		try {
			rtsPort.execStartOp(null, null, "GLORIA", seconds);
			teleoperationStarted = true;
		} catch (RtiError e) {
			throw new GenericTeleoperationException("/start?secs=" + seconds
					+ " -> START_TELEOP_FAILED");
		}
	}

	public boolean isTeleoperationStarted() {
		return teleoperationStarted;
	}

	public DeviceHandler getDeviceHandler(String name, DeviceType type)
			throws TeleoperationException {
		Device device = null;

		String actionMessage = "devices/" + type.name() + "?" + name + "->";

		try {
			device = rtsPort.devGetDevice(null, name, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(actionMessage
					+ "DEVICE_NOT_AVAILABLE");
		} catch (NullPointerException ne) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
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
			}else if (type.equals(DeviceType.FW)) {
				return new FilterWheel(this, name);
			}
		}

		throw new IncorrectDeviceTypeException(actionMessage
				+ "INCORRECT_DEVICE_TYPE");
	}

	public GloriaRti getPort() {
		return this.rtsPort;
	}

	public boolean isConnected(String device) throws TeleoperationException {
		String actionMessage = "devices/connected?" + device + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			return rtsPort.devIsConnected(null, device);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(actionMessage
					+ "DEVICE_NOT_AVAILABLE");
		} catch (NullPointerException ne) {
			throw new TeleoperationException("The RTS is not available.");
		}
	}

	public long getBrightness(String camera) throws TeleoperationException {

		String actionMessage = camera + "/brightness->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(actionMessage
					+ "DEVICE_NOT_AVAILABLE");
		}

		try {
			if (device.getType().equals(DeviceType.CCD)) {
				return rtsPort.camGetBrightness(null, camera);
			} else {
				return rtsPort.scamGetBrightness(null, camera);
			}
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void setBrightness(String camera, double value)
			throws TeleoperationException {

		String actionMessage = camera + "/brightness?" + value + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(actionMessage
					+ "DEVICE_NOT_AVAILABLE");
		}

		try {

			if (device.getType().equals(DeviceType.CCD)) {
				rtsPort.camSetBrightness(null, camera, (long) value);
			} else {
				rtsPort.scamSetBrightness(null, camera, (long) value);
			}
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public double getExposureTime(String camera) throws TeleoperationException {

		String actionMessage = camera + "/exposure->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(actionMessage
					+ "DEVICE_NOT_AVAILABLE");
		}

		try {
			if (device.getType().equals(DeviceType.CCD)) {
				return rtsPort.camGetExposureTime(null, camera);
			} else {
				return rtsPort.scamGetExposureTime(null, camera);
			}

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void setExposureTime(String camera, double value)
			throws TeleoperationException {

		String actionMessage = camera + "/exposure?" + value + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(actionMessage
					+ "DEVICE_NOT_AVAILABLE");
		}

		try {
			if (device.getType().equals(DeviceType.CCD)) {
				rtsPort.camSetExposureTime(null, camera, value);
			} else {
				rtsPort.scamSetExposureTime(null, camera, (long) value);
			}
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public boolean getAutoExposure(String camera) throws TeleoperationException {

		String actionMessage = camera + "/auto-exposure->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {

			return rtsPort.camGetAutoExposureTime(null, camera);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void setAutoExposure(String camera, boolean mode)
			throws TeleoperationException {

		String actionMessage = camera + "/auto-exposure?" + mode + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			rtsPort.camSetAutoExposureTime(null, camera, mode);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public long getContrast(String camera) throws TeleoperationException {

		String actionMessage = camera + "/contrast->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(actionMessage
					+ "DEVICE_NOT_AVAILABLE");
		}

		try {
			if (device.getType().equals(DeviceType.CCD)) {
				return rtsPort.camGetContrast(null, camera);
			} else {
				return rtsPort.scamGetContrast(null, camera);
			}
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void setContrast(String camera, long value)
			throws TeleoperationException {

		String actionMessage = camera + "/contrast?" + value + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(actionMessage
					+ "DEVICE_NOT_AVAILABLE");
		}

		try {
			if (device.getType().equals(DeviceType.CCD)) {
				rtsPort.camSetContrast(null, camera, value);
			} else {
				rtsPort.scamSetContrast(null, camera, value);
			}
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public long getGain(String camera) throws TeleoperationException {

		String actionMessage = camera + "/gain" + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			return rtsPort.camGetGain(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void setGain(String camera, long value)
			throws TeleoperationException {

		String actionMessage = camera + "/gain?" + value + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			rtsPort.camSetGain(null, camera, value);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public long getGamma(String camera) throws TeleoperationException {

		String actionMessage = camera + "/gamma" + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			return rtsPort.camGetGamma(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void setGamma(String camera, long value)
			throws TeleoperationException {

		String actionMessage = camera + "/gamma?" + value + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			rtsPort.camSetGamma(null, camera, value);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public boolean getAutoGain(String camera) throws TeleoperationException {
		String actionMessage = camera + "/auto-gain" + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			return rtsPort.camGetAutoGain(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void setAutoGain(String camera, boolean mode)
			throws TeleoperationException {
		String actionMessage = camera + "/auto-gain?" + mode + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			rtsPort.camSetAutoGain(null, camera, mode);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public String startExposure(String camera) throws TeleoperationException {
		String actionMessage = camera + "/startExposure->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			return rtsPort.camStartExposure(null, camera, true);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public String startContinueMode(String camera)
			throws TeleoperationException {

		String actionMessage = camera + "/startContinue->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			return rtsPort.camStartContinueMode(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void stopContinueMode(String camera) throws TeleoperationException {
		String actionMessage = camera + "/stopContinue->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			rtsPort.camStopContinueMode(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public String getImageURL(String camera, String imageId,
			ImageExtensionFormat format) throws TeleoperationException {

		String actionMessage = camera + "/images/getURL?" + imageId + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(actionMessage
					+ "DEVICE_NOT_AVAILABLE");
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
				throw new ImageNotAvailableException(actionMessage
						+ "IMAGE_NOT_AVAILABLE");
			else if (e.getMessage().equals("FAILED"))
				throw new ImageTransferFailedException(actionMessage
						+ "IMAGE_TRANSFER_FAILED");
			else
				throw new DeviceOperationFailedException(actionMessage
						+ "OPERATION_FAILED");
		}
	}

	public String getContinuousImagePath(String camera)
			throws TeleoperationException {
		String actionMessage = camera + "/continuePath->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			return rtsPort.camGetContinueModeImagePath(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public ActivityContinueStateCamera getCCDState(String ccd)
			throws TeleoperationException {

		String actionMessage = ccd + "/getState->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			DeviceCamera deviceCamera = (DeviceCamera) rtsPort.devGetDevice(
					null, ccd, false);

			return deviceCamera.getActivityContinueState();
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public ActivityContinueStateCamera getSCamState(String scam)
			throws TeleoperationException {
		String actionMessage = scam + "/getState->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			DeviceCamera deviceCamera = (DeviceCamera) rtsPort.devGetDevice(
					null, scam, false);

			return deviceCamera.getActivityContinueState();
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void open(String dome) throws TeleoperationException {
		String actionMessage = dome + "/open->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			rtsPort.domOpen(null, dome, 0);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void close(String dome) throws TeleoperationException {
		String actionMessage = dome + "/close->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			rtsPort.domClose(null, dome, 0);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void parkDome(String dome) throws TeleoperationException {
		String actionMessage = dome + "/park->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			rtsPort.domPark(null, dome);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void parkMount(String mount) throws TeleoperationException {
		String actionMessage = mount + "/park->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			rtsPort.mntPark(null, mount);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public double getDomeAzimuth(String dome) throws TeleoperationException {
		String actionMessage = dome + "/azimuth->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {

			return rtsPort.domGetAzimuth(null, dome);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public boolean getDomeTracking(String dome) throws TeleoperationException {
		String actionMessage = dome + "/tracking->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			return rtsPort.domGetTracking(null, dome);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void setDomeTracking(String dome, boolean mode)
			throws TeleoperationException {
		String actionMessage = dome + "/tracking?" + mode + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			rtsPort.domSetTracking(null, dome, mode);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public ActivityStateDomeOpening getDomeState(String dome)
			throws TeleoperationException {
		String actionMessage = dome + "/getState->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {

			DeviceDome domeDevice = (DeviceDome) rtsPort.devGetDevice(null,
					dome, false);

			return domeDevice.getActivityStateOpening();
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void moveNorth(String mount) throws TeleoperationException {
		String actionMessage = mount + "/moveNorth->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			rtsPort.mntMoveNorth(null, mount);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void moveSouth(String mount) throws TeleoperationException {
		String actionMessage = mount + "/moveSouth->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			rtsPort.mntMoveSouth(null, mount);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void moveEast(String mount) throws TeleoperationException {
		String actionMessage = mount + "/moveEast->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			rtsPort.mntMoveEast(null, mount);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void moveWest(String mount) throws TeleoperationException {
		String actionMessage = mount + "/moveWest->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			rtsPort.mntMoveWest(null, mount);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void setSlewRate(String mount, String rate)
			throws TeleoperationException {
		String actionMessage = mount + "/setSlewRate?" + rate + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			rtsPort.mntSetSlewRate(null, mount, rate);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void setTrackingRate(String mount, TrackingRate rate)
			throws TeleoperationException {
		String actionMessage = mount + "/setTrackingRate?" + rate.name() + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			rtsPort.mntSetTrackingRate(null, mount,
					TrackingRateType.valueOf(rate.name()));
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void setMountTracking(String mount, boolean mode)
			throws TeleoperationException {

		String actionMessage = mount + "/tracking?" + mode + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			rtsPort.mntSetTracking(null, mount, mode);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void slewToObject(String mount, String object)
			throws TeleoperationException {
		String actionMessage = mount + "/slew?" + object + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			rtsPort.mntSlewObject(null, mount, object);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public ActivityStateMount getMountState(String mount)
			throws TeleoperationException {
		String actionMessage = mount + "/getState->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {

			DeviceMount mountDevice = (DeviceMount) rtsPort.devGetDevice(null,
					mount, false);

			return mountDevice.getActivityState();
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void relativeFocuserMove(String focuser, long steps)
			throws TeleoperationException {

		String actionMessage = focuser + "/move?relative&" + steps + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		long effectiveMove = steps;

		try {
			if (rtsPort.focIsAbsolute(null, focuser)) {
				long currentPosition = rtsPort.focGetPosition(null, focuser);
				effectiveMove = currentPosition + steps;
			}

			rtsPort.focMove(null, focuser, effectiveMove);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public void absoluteFocuserMove(String focuser, long position)
			throws TeleoperationException {

		String actionMessage = focuser + "/move?absolute&" + position + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		long effectiveMove = position;

		try {
			if (!rtsPort.focIsAbsolute(null, focuser)) {
				long currentPosition = rtsPort.focGetPosition(null, focuser);
				effectiveMove = position - currentPosition;
			}

			rtsPort.focMove(null, focuser, effectiveMove);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}

	public long getFocuserAbsolutePosition(String focuser)
			throws TeleoperationException {

		String actionMessage = focuser + "/position?absolute->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			if (rtsPort.focIsAbsolute(null, focuser)) {
				return rtsPort.focGetPosition(null, focuser);
			}
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}

		throw new NotAbsoluteFocuserException(actionMessage
				+ "NOT_ABSOLUTE_FOCUSER");
	}

	public List<String> getAvailableFilters(String filterWheel) throws TeleoperationException {
		String actionMessage = filterWheel + "/filters->";
		
		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		
		List<String> filters;
		try {
			filters = rtsPort.fwGetFilterList(null, filterWheel);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
		return filters;
	}

	public void selectFilter(String filterWheel, String filter)
			throws TeleoperationException {

		String actionMessage = filterWheel + "/selectFilter?" + filter + "->";

		if (rtsPort == null) {
			throw new ServerNotAvailableException(actionMessage
					+ "SERVER_NOT_AVAILABLE");
		}

		try {
			
			rtsPort.fwSelectFilterKind(null, filterWheel, filter);			
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(actionMessage
					+ "OPERATION_FAILED");
		}
	}
}
