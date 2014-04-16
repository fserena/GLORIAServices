package eu.gloria.rti.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.DeviceNotAvailableException;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.IncorrectDeviceTypeException;
import eu.gloria.gs.services.teleoperation.base.ServerHandler;
import eu.gloria.gs.services.teleoperation.base.ServerNotAvailableException;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.ccd.ImageExtensionFormat;
import eu.gloria.gs.services.teleoperation.ccd.ImageNotAvailableException;
import eu.gloria.gs.services.teleoperation.ccd.ImageTransferFailedException;
import eu.gloria.gs.services.teleoperation.focuser.NotAbsoluteFocuserException;
import eu.gloria.gs.services.teleoperation.generic.GenericTeleoperationException;
import eu.gloria.gs.services.teleoperation.mount.TrackingRate;
import eu.gloria.rt.entity.device.ActivityContinueStateCamera;
import eu.gloria.rt.entity.device.ActivityStateDomeOpening;
import eu.gloria.rt.entity.device.ActivityStateMount;
import eu.gloria.rt.entity.device.AlarmState;
import eu.gloria.rt.entity.device.Device;
import eu.gloria.rt.entity.device.DeviceCamera;
import eu.gloria.rt.entity.device.DeviceDome;
import eu.gloria.rt.entity.device.DeviceMount;
import eu.gloria.rt.entity.device.DeviceType;
import eu.gloria.rt.entity.device.ImageFormat;
import eu.gloria.rt.entity.device.TrackingRateType;
import eu.gloria.rti.GloriaRti;
import eu.gloria.rti.RtiError;
import eu.gloria.rti.client.devices.Barometer;
import eu.gloria.rti.client.devices.CCD;
import eu.gloria.rti.client.devices.Dome;
import eu.gloria.rti.client.devices.FilterWheel;
import eu.gloria.rti.client.devices.Focuser;
import eu.gloria.rti.client.devices.Mount;
import eu.gloria.rti.client.devices.RHSensor;
import eu.gloria.rti.client.devices.Scam;
import eu.gloria.rti.client.devices.TempSensor;
import eu.gloria.rti.client.devices.WindSensor;
import eu.gloria.rti.factory.ProxyFactory;

public class RTSHandler implements ServerHandler {

	private GloriaRti rtsPort;
	private static String serviceName;
	private static boolean teleoperationStarted = false;
	private String host;
	private String port;

	static {

		//Properties properties = new Properties();
		//try {
			//InputStream in = Thread.currentThread().getContextClassLoader()
			//		.getResourceAsStream("rtsclient.properties");

			//properties.load(in);
			//in.close();

			serviceName = "RTI";//(String) properties.get("service_name");

		//} catch (IOException e) {
	//	}
	}
	
	public RTSHandler() {
		
	}

	public RTSHandler(String host, String port, String user, String password)
			throws TeleoperationException {

		//super(host);

		this.host = host;
		this.port = port;

		URL urlWsdl = null;
		try {
			urlWsdl = new URL(host + ":" + port + "/" + serviceName
					+ "/gloria_rti.wsdl");
		} catch (MalformedURLException e) {
		}

		URL urlWs = null;
		try {
			urlWs = new URL(host + ":" + port + "/" + serviceName
					+ "/services/gloria_rtiSOAP?wsdl");
		} catch (MalformedURLException e) {
			throw new ServerNotAvailableException(host, port, e.getClass()
					.getSimpleName());
		}

		ProxyFactory proxyFactory = new ProxyFactory();

		try {
			rtsPort = proxyFactory.getProxy(urlWsdl, urlWs, false, user,
					password);
		} catch (Exception e) {
			throw new ServerNotAvailableException(host, port, e.getClass()
					.getSimpleName());
		}

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}
	}

	public void absoluteFocuserMove(String focuser, long position)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		long effectiveMove = position;

		try {
			if (!rtsPort.focIsAbsolute(null, focuser)) {
				long currentPosition = rtsPort.focGetPosition(null, focuser);
				effectiveMove = position - currentPosition;
			}

			rtsPort.focMove(null, focuser, effectiveMove);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(focuser,
					DeviceType.FOCUS.name(), "absolute move", e.getMessage());
		}
	}

	public void close(String dome) throws TeleoperationException {
		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.domClose(null, dome, 0);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(dome,
					DeviceType.DOME.name(), "close", e.getMessage());
		}
	}

	public boolean getAutoExposure(String camera) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			return rtsPort.camGetAutoExposureTime(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera,
					DeviceType.CCD.name(), "get auto exposure", e.getMessage());
		}
	}

	public boolean getAutoGain(String camera) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			return rtsPort.camGetAutoGain(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera,
					DeviceType.CCD.name(), "get auto gain", e.getMessage());
		}
	}

	public List<String> getAvailableFilters(String filterWheel)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		List<String> filters;
		try {
			filters = rtsPort.fwGetFilterList(null, filterWheel);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(filterWheel,
					DeviceType.FW.name(), "get filters", e.getMessage());
		}
		return filters;
	}

	public long getBiningX(String camera) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			return rtsPort.camGetBinX(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera,
					DeviceType.CCD.name(), "get bining x", e.getMessage());
		}
	}

	public long getBiningY(String camera) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			return rtsPort.camGetBinY(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera,
					DeviceType.CCD.name(), "get bining y", e.getMessage());
		}
	}

	public long getBrightness(String camera) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(camera, null, e.getMessage());
		}

		try {
			if (device.getType().equals(DeviceType.CCD)) {
				return rtsPort.camGetBrightness(null, camera);
			} else {
				return rtsPort.scamGetBrightness(null, camera);
			}
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera, device.getType()
					.name(), "get brigtness", e.getMessage());
		}
	}

	public ActivityContinueStateCamera getCCDState(String ccd)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			DeviceCamera deviceCamera = (DeviceCamera) rtsPort.devGetDevice(
					null, ccd, false);

			return deviceCamera.getActivityContinueState();
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(ccd,
					DeviceType.CCD.name(), "get state", e.getMessage());
		}
	}

	public String getContinuousImagePath(String camera)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			return rtsPort.camGetContinueModeImagePath(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera,
					DeviceType.CCD.name(), "get continue url", e.getMessage());
		}
	}

	public long getContrast(String camera) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(camera, null, e.getMessage());
		}

		try {
			if (device.getType().equals(DeviceType.CCD)) {
				return rtsPort.camGetContrast(null, camera);
			} else {
				return rtsPort.scamGetContrast(null, camera);
			}
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera, device.getType()
					.name(), "get contrast", e.getMessage());
		}
	}

	public double getDEC(String mount) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			double dec = rtsPort.mntGetPosAxis2(null, mount);
			return dec;
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(mount,
					DeviceType.MOUNT.name(), "get dec", e.getMessage());
		}
	}

	public DeviceHandler getDeviceHandler(String name, DeviceType type)
			throws TeleoperationException {
		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, name, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(name, type.name(),
					e.getMessage());
		} catch (Exception ne) {
			throw new ServerNotAvailableException(this.host, this.port,
					"server not responding");
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
			} else if (type.equals(DeviceType.FW)) {
				return new FilterWheel(this, name);
			} else if (type.equals(DeviceType.RH_SENSOR)) {
				return new RHSensor(this, name);
			} else if (type.equals(DeviceType.WIND_SPEED_SENSOR)) {
				return new WindSensor(this, name);
			} else if (type.equals(DeviceType.BAROMETER)) {
				return new Barometer(this, name);
			} else if (type.equals(DeviceType.TEMPERATURE_SENSOR)) {
				return new TempSensor(this, name);
			}
		}

		throw new IncorrectDeviceTypeException(name, type.name());
	}

	public double getDomeAzimuth(String dome) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {

			return rtsPort.domGetAzimuth(null, dome);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(dome,
					DeviceType.DOME.name(), "get azimuth", e.getMessage());
		}
	}

	public ActivityStateDomeOpening getDomeState(String dome)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {

			DeviceDome domeDevice = (DeviceDome) rtsPort.devGetDevice(null,
					dome, false);

			return domeDevice.getActivityStateOpening();
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(dome,
					DeviceType.DOME.name(), "get state", e.getMessage());
		}
	}

	public boolean getDomeTracking(String dome) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			return rtsPort.domGetTracking(null, dome);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(dome,
					DeviceType.DOME.name(), "get tracking", e.getMessage());
		}
	}

	public double getExposureTime(String camera) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(camera, null, e.getMessage());
		}

		try {
			if (device.getType().equals(DeviceType.CCD)) {
				return rtsPort.camGetExposureTime(null, camera);
			} else {
				return rtsPort.scamGetExposureTime(null, camera);
			}

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera, device.getType()
					.name(), "get exposure time", e.getMessage());
		}
	}

	public long getFocuserAbsolutePosition(String focuser)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			if (rtsPort.focIsAbsolute(null, focuser)) {
				return rtsPort.focGetPosition(null, focuser);
			}
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(focuser,
					DeviceType.FOCUS.name(), "get position", e.getMessage());
		}

		throw new NotAbsoluteFocuserException(focuser);
	}

	public long getGain(String camera) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			return rtsPort.camGetGain(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera,
					DeviceType.CCD.name(), "get gain", e.getMessage());
		}
	}

	public long getGamma(String camera) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			return rtsPort.camGetGamma(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera,
					DeviceType.CCD.name(), "get gamma", e.getMessage());
		}
	}

	public String getImageURL(String camera, String imageId,
			ImageExtensionFormat format) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(camera, null, e.getMessage());
		}

		try {
			if (device.getType().equals(DeviceType.CCD)) {
				ImageFormat imageFormat = ImageFormat.valueOf(format.name());

				return rtsPort.camGetImageURLProperFormat(null, camera,
						imageId, imageFormat);
			} else {
				return rtsPort.scamGetVideoStreamingURL(null, camera);
			}
		} catch (RtiError e) {
			if (e.getMessage().equals("NOT_AVAILABLE")) {
				ImageNotAvailableException ex = new ImageNotAvailableException(
						camera, device.getType().name());
				if (device.getType().equals(DeviceType.CCD)) {
					ex.getAction().put("id", imageId);
					ex.getAction().put("format", format.name());
				}

				throw ex;
			} else if (e.getMessage().equals("FAILED")) {
				throw new ImageTransferFailedException(camera, imageId);
			} else
				throw new DeviceOperationFailedException(camera, device
						.getType().name(), "get url", e.getMessage());
		}
	}

	public ActivityStateMount getMountState(String mount)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {

			DeviceMount mountDevice = (DeviceMount) rtsPort.devGetDevice(null,
					mount, false);

			ActivityStateMount state = mountDevice.getActivityState();
			
			boolean parked = rtsPort.mntIsParked(null, mount);
			boolean slewing = rtsPort.mntIsSlewing(null, mount);
			boolean tracking = rtsPort.mntGetTracking(null, mount);

			if (parked)
				return ActivityStateMount.PARKED;
			if (tracking)
				return ActivityStateMount.TRACKING;
			if (slewing)
				return ActivityStateMount.MOVING;
			
			return state;
			
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(mount,
					DeviceType.MOUNT.name(), "get state", e.getMessage());
		}
	}

	public GloriaRti getPort() {
		return this.rtsPort;
	}

	public double getPressure(String barometer) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			return rtsPort.barGetMeasure(null, barometer);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(barometer,
					DeviceType.BAROMETER.name(), "get pressure", e.getMessage());
		}
	}

	public double getRA(String mount) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			double ra = rtsPort.mntGetPosAxis1(null, mount);
			return ra;
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(mount,
					DeviceType.MOUNT.name(), "get ra", e.getMessage());
		}
	}

	public double getRelativeHumidity(String rhSensor)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			return rtsPort.rhsGetMeasure(null, rhSensor);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(rhSensor,
					DeviceType.RH_SENSOR.name(), "get rh", e.getMessage());
		}
	}

	public ActivityContinueStateCamera getSCamState(String scam)
			throws TeleoperationException {
		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			DeviceCamera deviceCamera = (DeviceCamera) rtsPort.devGetDevice(
					null, scam, false);

			return deviceCamera.getActivityContinueState();
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(scam,
					DeviceType.SURVEILLANCE_CAMERA.name(), "get state",
					e.getMessage());
		}
	}

	public double getTemperature(String tempSensor)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			return rtsPort.tempGetMeasure(null, tempSensor);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(tempSensor,
					DeviceType.TEMPERATURE_SENSOR.name(), "get temperature",
					e.getMessage());
		}
	}

	public double getWindSpeed(String wind) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			return rtsPort.wspGetMeasure(null, wind);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(wind,
					DeviceType.WIND_SPEED_SENSOR.name(), "get wind speed",
					e.getMessage());
		}
	}

	public boolean isConnected(String device) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			return rtsPort.devIsConnected(null, device);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(device, null, e.getMessage());
		} catch (Exception ne) {
			throw new ServerNotAvailableException(host, port, null);
		}
	}

	public boolean isPressureSensorInAlarm(String barometer)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			Device sensor = rtsPort.devGetDevice(null, barometer, false);
			return sensor.getAlarmState().equals(AlarmState.WEATHER);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(barometer,
					DeviceType.BAROMETER.name(), "get pressure alarm",
					e.getMessage());
		}
	}

	public boolean isRHSensorInAlarm(String rhSensor)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			Device sensor = rtsPort.devGetDevice(null, rhSensor, false);
			return sensor.getAlarmState().equals(AlarmState.WEATHER);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(rhSensor,
					DeviceType.RH_SENSOR.name(), "get rh alarm", e.getMessage());
		}
	}

	public boolean isTeleoperationStarted() {
		return teleoperationStarted;
	}

	public boolean isTemperatureSensorInAlarm(String tempSensor)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			Device sensor = rtsPort.devGetDevice(null, tempSensor, false);
			return sensor.getAlarmState().equals(AlarmState.WEATHER);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(tempSensor,
					DeviceType.TEMPERATURE_SENSOR.name(),
					"get temperature alarm", e.getMessage());
		}
	}

	public boolean isWindSensorInAlarm(String wind)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			Device sensor = rtsPort.devGetDevice(null, wind, false);
			return sensor.getAlarmState().equals(AlarmState.WEATHER);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(wind,
					DeviceType.RH_SENSOR.name(), "get temperature alarm",
					e.getMessage());
		}
	}

	public void moveEast(String mount) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.mntMoveEast(null, mount);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(mount,
					DeviceType.MOUNT.name(), "move east", e.getMessage());
		}
	}

	public void moveNorth(String mount) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.mntMoveNorth(null, mount);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(mount,
					DeviceType.MOUNT.name(), "move north", e.getMessage());
		}
	}

	public void moveSouth(String mount) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.mntMoveSouth(null, mount);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(mount,
					DeviceType.MOUNT.name(), "move south", e.getMessage());
		}
	}

	public void moveWest(String mount) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.mntMoveWest(null, mount);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(mount,
					DeviceType.MOUNT.name(), "move west", e.getMessage());
		}
	}

	public void notifyTeleoperation(long seconds)
			throws GenericTeleoperationException {
		try {
			rtsPort.execStopOp(null);
			teleoperationStarted = false;
		} catch (RtiError e) {
			throw new GenericTeleoperationException("start", "stop previous", e);
		}

		try {
			rtsPort.execStartOp(null, null, "GLORIA", seconds);
			teleoperationStarted = true;
		} catch (RtiError e) {
			GenericTeleoperationException ex = new GenericTeleoperationException(
					"start", e);
			ex.getAction().put("while", "after stop");

			throw ex;
		}
	}

	public void open(String dome) throws TeleoperationException {
		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.domOpen(null, dome, 0);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(dome,
					DeviceType.DOME.name(), "open", e.getMessage());
		}
	}

	public void parkDome(String dome) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.domPark(null, dome);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(dome,
					DeviceType.DOME.name(), "park", e.getMessage());
		}
	}

	public void parkMount(String mount) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.mntPark(null, mount);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(mount,
					DeviceType.MOUNT.name(), "park", e.getMessage());
		}
	}

	public void relativeFocuserMove(String focuser, long steps)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		long effectiveMove = steps;

		try {
			if (rtsPort.focIsAbsolute(null, focuser)) {
				long currentPosition = rtsPort.focGetPosition(null, focuser);
				effectiveMove = currentPosition + steps;
			}

			rtsPort.focMove(null, focuser, effectiveMove);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(focuser,
					DeviceType.FOCUS.name(), "relative move", e.getMessage());
		}
	}

	public void selectFilter(String filterWheel, String filter)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {

			rtsPort.fwSelectFilterKind(null, filterWheel, filter);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(filterWheel,
					DeviceType.FW.name(), "select filter", e.getMessage());
		}
	}

	public void setAutoExposure(String camera, boolean mode)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.camSetAutoExposureTime(null, camera, mode);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera,
					DeviceType.CCD.name(), "set auto exposure", e.getMessage());
		}
	}

	public void setAutoGain(String camera, boolean mode)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.camSetAutoGain(null, camera, mode);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera,
					DeviceType.CCD.name(), "set auto gain", e.getMessage());
		}
	}

	public void setBiningX(String camera, long value)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.camSetBinX(null, camera, (int) value);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera,
					DeviceType.CCD.name(), "set bining x", e.getMessage());
		}
	}

	public void setBiningY(String camera, long value)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.camSetBinY(null, camera, (int) value);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera,
					DeviceType.CCD.name(), "set bining y", e.getMessage());
		}
	}

	public void setBrightness(String camera, double value)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(camera, null, e.getMessage());
		}

		try {

			if (device.getType().equals(DeviceType.CCD)) {
				rtsPort.camSetBrightness(null, camera, (long) value);
			} else {
				rtsPort.scamSetBrightness(null, camera, (long) value);
			}
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera, device.getType()
					.name(), "set brigtness", e.getMessage());
		}
	}

	public void setContrast(String camera, long value)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(camera, null, e.getMessage());
		}

		try {
			if (device.getType().equals(DeviceType.CCD)) {
				rtsPort.camSetContrast(null, camera, value);
			} else {
				rtsPort.scamSetContrast(null, camera, value);
			}
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera, device.getType()
					.name(), "set contrast", e.getMessage());
		}
	}

	public void setDomeTracking(String dome, boolean mode)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.domSetTracking(null, dome, mode);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(dome,
					DeviceType.DOME.name(), "set tracking", e.getMessage());
		}
	}

	public void setExposureTime(String camera, double value)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		Device device = null;

		try {
			device = rtsPort.devGetDevice(null, camera, false);
		} catch (RtiError e) {
			throw new DeviceNotAvailableException(camera, null, e.getMessage());
		}

		try {
			if (device.getType().equals(DeviceType.CCD)) {
				rtsPort.camSetExposureTime(null, camera, value);
			} else {
				rtsPort.scamSetExposureTime(null, camera, (long) value);
			}
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera, device.getType()
					.name(), "set exposure time", e.getMessage());
		}
	}

	public void setGain(String camera, long value)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.camSetGain(null, camera, value);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera,
					DeviceType.CCD.name(), "set gain", e.getMessage());
		}
	}

	public void setGamma(String camera, long value)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.camSetGamma(null, camera, value);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera,
					DeviceType.CCD.name(), "set gamma", e.getMessage());
		}
	}

	public void setMountTracking(String mount, boolean mode)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.mntSetTracking(null, mount, mode);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(mount,
					DeviceType.MOUNT.name(), "set tracking", e.getMessage());
		}
	}

	public void setSlewRate(String mount, String rate)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.mntSetSlewRate(null, mount, rate);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(mount,
					DeviceType.MOUNT.name(), "set slew rate", e.getMessage());
		}
	}

	public void setTrackingRate(String mount, TrackingRate rate)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.mntSetTrackingRate(null, mount,
					TrackingRateType.valueOf(rate.name()));
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(mount,
					DeviceType.MOUNT.name(), "set tracking rate",
					e.getMessage());
		}
	}

	public void slewToCoordinates(String mount, double ra, double dec)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.mntSlewToCoordinates(null, mount, ra, dec);

		} catch (RtiError e) {
			throw new DeviceOperationFailedException(mount,
					DeviceType.MOUNT.name(), "slew to coordinates",
					e.getMessage());
		}
	}

	public void slewToObject(String mount, String object)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.mntSlewObject(null, mount, object);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(mount,
					DeviceType.MOUNT.name(), "slew to object", e.getMessage());
		}
	}

	public String startContinueMode(String camera)
			throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			return rtsPort.camStartContinueMode(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera,
					DeviceType.CCD.name(), "start continue", e.getMessage());
		}
	}

	public String startExposure(String camera) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			return rtsPort.camStartExposure(null, camera, true);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera,
					DeviceType.CCD.name(), "start exposure", e.getMessage());
		}
	}

	public void startTeleoperation() throws GenericTeleoperationException {
		this.notifyTeleoperation(0);
	}

	public void stopContinueMode(String camera) throws TeleoperationException {

		if (rtsPort == null) {
			throw new ServerNotAvailableException(host, port, null);
		}

		try {
			rtsPort.camStopContinueMode(null, camera);
		} catch (RtiError e) {
			throw new DeviceOperationFailedException(camera,
					DeviceType.CCD.name(), "stop continue", e.getMessage());
		}
	}

	public void stopTeleoperation() throws GenericTeleoperationException {
		try {
			rtsPort.execStopOp(null);
			teleoperationStarted = false;
		} catch (RtiError e) {
			throw new GenericTeleoperationException("stop", e);
		}
	}
}
