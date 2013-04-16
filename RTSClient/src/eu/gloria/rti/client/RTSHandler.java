package eu.gloria.rti.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import eu.gloria.gs.services.teleoperation.base.ServerHandler;
import eu.gloria.gs.services.teleoperation.ccd.ImageExtensionFormat;
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
	private static String port = "8443";
	private static String serviceName = "RTI";
	private static String user = "gloria_user";
	private static String password = "12345";

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

	public RTSHandler(String host) throws RTSException {
		/*
		 * RTIProxyConnection proxyConnection = new RTIProxyConnection(host,
		 * port, serviceName, user, password, pk);
		 * 
		 * rtsPort = proxyConnection.getProxy();
		 */

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (rtsPort == null) {
			throw new RTSException(
					"The RTS on "
							+ host
							+ " is not available or does not exist (or maybe, the public key is not correct)");
		}
	}

	public CCD getCCD(String name) throws RTSException {
		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, name, false);
		} catch (RtiError e) {
			throw new RTSException("The device " + name + " does not exist: "
					+ e.getMessage());
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}

		if (device.getType().equals(DeviceType.CCD)) {
			return new CCD(this, name);
		}

		throw new RTSException("The device " + name + " is not a CCD.");
	}

	public Dome getDome(String name) throws RTSException {
		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, name, false);
		} catch (RtiError e) {
			throw new RTSException("The device " + name + " does not exist: "
					+ e.getMessage());
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}

		if (device.getType().equals(DeviceType.DOME)) {
			return new Dome(this, name);
		}

		throw new RTSException("The device " + name + " is not a Dome.");
	}

	public Mount getMount(String name) throws RTSException {
		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, name, false);
		} catch (RtiError e) {
			throw new RTSException("The device " + name + " does not exist: "
					+ e.getMessage());
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}

		if (device.getType().equals(DeviceType.MOUNT)) {
			return new Mount(this, name);
		}

		throw new RTSException("The device " + name + " is not a Mount.");
	}

	public Scam getSurveillanceCamera(String name) throws RTSException {

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, name, false);
		} catch (RtiError e) {
			throw new RTSException("The device " + name + " does not exist: "
					+ e.getMessage());
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}

		if (device.getType().equals(DeviceType.SURVEILLANCE_CAMERA)) {
			return new Scam(this, name);
		}

		throw new RTSException("The device " + name
				+ " is not a Surveillance Camera.");

	}

	public Focuser getFocuser(String name) throws RTSException {

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, name, false);
		} catch (RtiError e) {
			throw new RTSException("The device " + name + " does not exist: "
					+ e.getMessage());
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}

		if (device.getType().equals(DeviceType.FOCUS)) {
			return new Focuser(this, name);
		}

		throw new RTSException("The device " + name + " is not a Focuser.");

	}

	public GloriaRti getPort() {
		return this.rtsPort;
	}

	public boolean isConnected(String device) throws RTSException {
		try {
			return rtsPort.devIsConnected(null, device);
		} catch (RtiError e) {
			throw new RTSException("The device " + device
					+ " does not exist or its information cannot be recovered.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public long getBrightness(String camera) throws RTSException {
		try {
			Device device = rtsPort.devGetDevice(null, camera, false);

			if (device.getType().equals(DeviceType.CCD)) {
				return rtsPort.camGetBrightness(null, camera);
			} else {
				return rtsPort.scamGetBrightness(null, camera);
			}
		} catch (RtiError e) {
			throw new RTSException("The camera " + camera
					+ " does not exist or its brightness cannot be recovered.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public void setBrightness(String camera, double value) throws RTSException {
		try {

			Device device = rtsPort.devGetDevice(null, camera, false);

			if (device.getType().equals(DeviceType.CCD)) {
				rtsPort.camSetBrightness(null, camera, (long) value);
			} else {
				rtsPort.scamSetBrightness(null, camera, (long) value);
			}

		} catch (RtiError e) {
			throw new RTSException("The camera " + camera
					+ " does not exist or its brightness cannot be assigned.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public double getExposureTime(String camera) throws RTSException {
		try {

			Device device = rtsPort.devGetDevice(null, camera, false);

			if (device.getType().equals(DeviceType.CCD)) {
				return rtsPort.camGetExposureTime(null, camera);
			} else {
				return rtsPort.scamGetExposureTime(null, camera);
			}

		} catch (RtiError e) {
			throw new RTSException("The camera " + camera
					+ " does not exist or its exposure cannot be recovered.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public void setExposureTime(String camera, double value)
			throws RTSException {
		try {
			Device device = rtsPort.devGetDevice(null, camera, false);

			if (device.getType().equals(DeviceType.CCD)) {
				rtsPort.camSetExposureTime(null, camera, value);
			} else {
				rtsPort.scamSetExposureTime(null, camera, (long) value);
			}
		} catch (RtiError e) {
			throw new RTSException("The camera " + camera
					+ " does not exist or its exposure cannot be assigned.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public boolean getAutoExposure(String camera) throws RTSException {
		try {

			return rtsPort.camGetAutoExposureTime(null, camera);

		} catch (RtiError e) {
			throw new RTSException("The camera " + camera
					+ " does not exist or does not have auto exposure mode.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public void setAutoExposure(String camera, boolean mode)
			throws RTSException {
		try {
			rtsPort.camSetAutoExposureTime(null, camera, mode);

		} catch (RtiError e) {
			throw new RTSException(
					"The camera "
							+ camera
							+ " does not exist or its auto exposure cannot be assigned.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public long getContrast(String camera) throws RTSException {
		try {
			Device device = rtsPort.devGetDevice(null, camera, false);

			if (device.getType().equals(DeviceType.CCD)) {
				return rtsPort.camGetContrast(null, camera);
			} else {
				return rtsPort.scamGetContrast(null, camera);
			}
		} catch (RtiError e) {
			throw new RTSException("The camera " + camera
					+ " does not exist or its contrast cannot be recovered.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public void setContrast(String camera, long value) throws RTSException {
		try {
			Device device = rtsPort.devGetDevice(null, camera, false);

			if (device.getType().equals(DeviceType.CCD)) {
				rtsPort.camSetContrast(null, camera, value);
			} else {
				rtsPort.scamSetContrast(null, camera, value);
			}
		} catch (RtiError e) {
			throw new RTSException("The camera " + camera
					+ " does not exist or its contrast cannot be assigned.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public long getGain(String camera) throws RTSException {
		try {
			return rtsPort.camGetGain(null, camera);

		} catch (RtiError e) {
			throw new RTSException("The camera " + camera
					+ " does not exist or its gain cannot be recovered.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public void setGain(String camera, long value) throws RTSException {
		try {
			rtsPort.camSetGain(null, camera, value);

		} catch (RtiError e) {
			throw new RTSException("The camera " + camera
					+ " does not exist or its gain cannot be assigned.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public boolean getAutoGain(String camera) throws RTSException {
		try {

			return rtsPort.camGetAutoGain(null, camera);

		} catch (RtiError e) {
			throw new RTSException("The camera " + camera
					+ " does not exist or does not have auto gain mode.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public void setAutoGain(String camera, boolean mode) throws RTSException {
		try {
			rtsPort.camSetAutoGain(null, camera, mode);

		} catch (RtiError e) {
			throw new RTSException("The camera " + camera
					+ " does not exist or its auto gain cannot be assigned.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public String startExposure(String camera) throws RTSException {
		try {
			return rtsPort.camStartExposure(null, camera, true);
		} catch (RtiError e) {
			throw new RTSException(
					"The camera "
							+ camera
							+ " does not exist or an error has occured while trying to start exposure.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}

	}

	public String startContinueMode(String camera) throws RTSException {
		try {

			return rtsPort.camStartContinueMode(null, camera);

		} catch (RtiError e) {
			throw new RTSException(
					"The camera "
							+ camera
							+ " does not exist or an error has occured while trying to start continuous mode: "
							+ e.getMessage());
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public void stopContinueMode(String camera) throws RTSException {
		try {

			rtsPort.camStopContinueMode(null, camera);

		} catch (RtiError e) {
			throw new RTSException(
					"The camera "
							+ camera
							+ " does not exist or an error has occured while trying to stop continuous mode: "
							+ e.getMessage());
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public String getImageURL(String camera, String imageId,
			ImageExtensionFormat format) throws RTSException {
		try {

			Device device = rtsPort.devGetDevice(null, camera, false);

			if (device.getType().equals(DeviceType.CCD)) {

				ImageFormat imageFormat = ImageFormat.valueOf(format.name());

				return rtsPort.camGetImageURLProperFormat(null, camera,
						imageId, imageFormat);
			} else {
				return rtsPort.scamGetImageURL(null, camera);
			}
		} catch (RtiError e) {
			if (e.getMessage().equals("NOT_AVAILABLE"))
				throw new RTSException("The image is not yet available.");
			else if (e.getMessage().equals("FAILED"))
				throw new RTSException(
						"An error has occured while saving the image. It will not be available.");
			else
				throw new RTSException(
						"The camera "
								+ camera
								+ " does not exist or an error has occured while trying to recover the image url.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public String getContinuousImagePath(String camera) throws RTSException {
		try {

			return rtsPort.camGetContinueModeImagePath(null, camera);

		} catch (RtiError e) {
			throw new RTSException(
					"The camera "
							+ camera
							+ " does not exist or an error has occured while trying to recover the continue mode path.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public ActivityContinueStateCamera getCCDState(String ccd)
			throws RTSException {
		try {

			DeviceCamera deviceCamera = (DeviceCamera) rtsPort.devGetDevice(
					null, ccd, false);

			return deviceCamera.getActivityContinueState();
		} catch (RtiError e) {
			throw new RTSException(
					"The camera "
							+ ccd
							+ " does not exist or an error has occured while trying to recover the image url.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public ActivityContinueStateCamera getSCamState(String scam)
			throws RTSException {
		try {

			DeviceCamera deviceCamera = (DeviceCamera) rtsPort.devGetDevice(
					null, scam, false);

			return deviceCamera.getActivityContinueState();
		} catch (RtiError e) {
			throw new RTSException(
					"The camera "
							+ scam
							+ " does not exist or an error has occured while trying to recover the image url.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public void open(String dome) throws RTSException {
		try {
			rtsPort.domOpen(null, dome, 0);

		} catch (RtiError e) {
			throw new RTSException(
					"The dome "
							+ dome
							+ " does not exist or an error has occured while trying to open (may be it is already opened).");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public void close(String dome) throws RTSException {
		try {

			rtsPort.domClose(null, dome, 0);

		} catch (RtiError e) {
			throw new RTSException(
					"The dome "
							+ dome
							+ " does not exist or an error has occured while trying to close (may be it is already closed).");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public void parkDome(String dome) throws RTSException {
		try {
			rtsPort.domPark(null, dome);

		} catch (RtiError e) {
			throw new RTSException(
					"The dome "
							+ dome
							+ " does not exist or an error has occured while trying to park.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public void parkMount(String mount) throws RTSException {
		try {
			rtsPort.mntPark(null, mount);

		} catch (RtiError e) {
			throw new RTSException(
					"The mount "
							+ mount
							+ " does not exist or an error has occured while trying to park.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public double getDomeAzimuth(String dome) throws RTSException {
		try {

			return rtsPort.domGetAzimuth(null, dome);

		} catch (RtiError e) {
			throw new RTSException(
					"The dome "
							+ dome
							+ " does not exist or an error has occured while trying to recover its azimuth value.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public boolean getDomeTracking(String dome) throws RTSException {
		try {
			return rtsPort.domGetTracking(null, dome);
		} catch (RtiError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public void setDomeTracking(String dome, boolean mode) throws RTSException {
		try {
			rtsPort.domSetTracking(null, dome, mode);
		} catch (RtiError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ActivityStateDomeOpening getDomeState(String dome)
			throws RTSException {
		try {

			DeviceDome domeDevice = (DeviceDome) rtsPort.devGetDevice(null,
					dome, false);

			return domeDevice.getActivityStateOpening();
		} catch (RtiError e) {
			throw new RTSException(
					"The dome "
							+ dome
							+ " does not exist or an error has occured while trying to recover its status.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public void moveNorth(String mount) throws RTSException {
		try {
			rtsPort.mntMoveNorth(null, mount);
			// rtsPort.mntSetTracking(null, mount, false);
		} catch (RtiError e) {
			throw new RTSException("The mount has a problem: " + e.getMessage());
		}
	}

	public void moveSouth(String mount) throws RTSException {
		try {
			rtsPort.mntMoveSouth(null, mount);
			// rtsPort.mntSetTracking(null, mount, false);
		} catch (RtiError e) {
			throw new RTSException("The mount has a problem: " + e.getMessage());
		}
	}

	public void moveEast(String mount) throws RTSException {
		try {
			rtsPort.mntMoveEast(null, mount);
			// rtsPort.mntSetTracking(null, mount, false);
		} catch (RtiError e) {
			throw new RTSException("The mount has a problem: " + e.getMessage());
		}
	}

	public void moveWest(String mount) throws RTSException {
		try {
			rtsPort.mntMoveWest(null, mount);
			// rtsPort.mntSetTracking(null, mount, false);
		} catch (RtiError e) {
			throw new RTSException("The mount has a problem: " + e.getMessage());
		}
	}

	public void setSlewRate(String mount, String rate) throws RTSException {
		try {
			rtsPort.mntSetSlewRate(null, mount, rate);
		} catch (RtiError e) {
			throw new RTSException("The mount has a problem: " + e.getMessage());
		}
	}

	public void setTrackingRate(String mount, TrackingRate rate)
			throws RTSException {
		try {
			rtsPort.mntSetTrackingRate(null, mount,
					TrackingRateType.valueOf(rate.name()));
		} catch (RtiError e) {
			throw new RTSException("The mount has a problem: " + e.getMessage());
		}
	}

	public void setMountTracking(String mount, boolean mode)
			throws RTSException {
		try {
			rtsPort.mntSetTracking(null, mount, mode);
		} catch (RtiError e) {
			throw new RTSException("The mount has a problem: " + e.getMessage());
		}
	}

	public void slewToObject(String mount, String object) throws RTSException {
		try {
			rtsPort.mntSlewObject(null, mount, object);
		} catch (RtiError e) {
			throw new RTSException(e.getMessage());
		}
	}

	public ActivityStateMount getMountState(String mount) throws RTSException {
		try {

			DeviceMount mountDevice = (DeviceMount) rtsPort.devGetDevice(null,
					mount, false);

			return mountDevice.getActivityState();
		} catch (RtiError e) {
			throw new RTSException(
					"The mount "
							+ mount
							+ " does not exist or an error has occured while trying to recover its status.");
		} catch (NullPointerException ne) {
			throw new RTSException("The RTS is not available.");
		}
	}

	public void relativeFocuserMove(String focuser, long steps)
			throws RTSException {

		long effectiveMove = steps;

		try {
			if (rtsPort.focIsAbsolute(null, focuser)) {
				long currentPosition = rtsPort.focGetPosition(null, focuser);
				effectiveMove = currentPosition + steps;
			}

			rtsPort.focMove(null, focuser, effectiveMove);
		} catch (RtiError e) {
			throw new RTSException(e.getMessage());
		}
	}

	public void absoluteFocuserMove(String focuser, long position)
			throws RTSException {

		long effectiveMove = position;

		try {
			if (!rtsPort.focIsAbsolute(null, focuser)) {
				long currentPosition = rtsPort.focGetPosition(null, focuser);
				effectiveMove = position - currentPosition;
			}

			rtsPort.focMove(null, focuser, effectiveMove);
		} catch (RtiError e) {
			throw new RTSException(e.getMessage());
		}
	}

	public long getFocuserAbsolutePosition(String focuser) throws RTSException {

		try {
			if (rtsPort.focIsAbsolute(null, focuser)) {
				return rtsPort.focGetPosition(null, focuser);
			}
		} catch (RtiError e) {
			throw new RTSException(e.getMessage());
		}

		throw new RTSException("The focuser '" + focuser
				+ "' does not work in absolute mode");
	}
}
