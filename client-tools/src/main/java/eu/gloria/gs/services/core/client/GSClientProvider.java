package eu.gloria.gs.services.core.client;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import eu.gloria.gs.services.core.security.client.Credentials;
import eu.gloria.gs.services.core.security.client.ThreadCredentialsStore;
import eu.gloria.gs.services.experiment.ExperimentInterface;
import eu.gloria.gs.services.log.action.ActionLogInterface;
import eu.gloria.gs.services.repository.image.ImageRepositoryInterface;
import eu.gloria.gs.services.repository.rt.RTRepositoryInterface;
import eu.gloria.gs.services.repository.user.UserRepositoryInterface;
import eu.gloria.gs.services.scheduler.SchedulerInterface;
import eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.dome.DomeTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.focuser.FocuserTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.fw.FilterWheelTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.generic.GenericTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.mount.MountTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.scam.SCamTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.weather.WeatherTeleoperationInterface;

public class GSClientProvider {

	private final static String ACTIONLOG_BEAN_NAME = "actionLogClientFactory";
	private final static String RTREPOSITORY_BEAN_NAME = "rtRepositoryClientFactory";
	private final static String IMAGEREPOSITORY_BEAN_NAME = "imageRepositoryClientFactory";
	private final static String USERREPOSITORY_BEAN_NAME = "userRepositoryClientFactory";
	private final static String CCDTELEOPERATION_BEAN_NAME = "ccdTeleoperationClientFactory";
	private final static String DOMETELEOPERATION_BEAN_NAME = "domeTeleoperationClientFactory";
	private final static String MOUNTTELEOPERATION_BEAN_NAME = "mountTeleoperationClientFactory";
	private final static String FOCUSERTELEOPERATION_BEAN_NAME = "focuserTeleoperationClientFactory";
	private final static String WEATHERTELEOPERATION_BEAN_NAME = "weatherTeleoperationClientFactory";
	private final static String FWTELEOPERATION_BEAN_NAME = "fwTeleoperationClientFactory";
	private final static String GENERICTELEOPERATION_BEAN_NAME = "genericTeleoperationClientFactory";
	private final static String SCAMTELEOPERATION_BEAN_NAME = "scamTeleoperationClientFactory";
	private final static String ONLINEEXPERIMENT_BEAN_NAME = "experimentClientFactory";
	private final static String SCHEDULER_BEAN_NAME = "schedulerClientFactory";
	private static String HOSTNAME = "localhost";
	private static String PORT = "8443";
	private static ClassPathXmlApplicationContext context;

	static {
		context = new ClassPathXmlApplicationContext("gsbeans.xml");
	}

	public static void setCredentials(String username, String password) {
		Credentials credentials = new Credentials();
		credentials.setUsername(username);
		credentials.setPassword(password);
		ThreadCredentialsStore.storeCredentials(credentials);
	}

	public static void clearCredentials() {
		ThreadCredentialsStore.clear();
	}

	public static void setHost(String hostname) {
		HOSTNAME = hostname;
	}

	public static String getHost() {
		return HOSTNAME;
	}

	public static void setPort(String port) {
		PORT = port;
	}

	public static String getPort() {
		return PORT;
	}

	public static ActionLogInterface getActionLogClient() {
		ClientFactory factory = (ClientFactory) context
				.getBean(ACTIONLOG_BEAN_NAME);

		return (ActionLogInterface) factory.create();
	}

	public static RTRepositoryInterface getRTRepositoryClient() {
		ClientFactory factory = (ClientFactory) context
				.getBean(RTREPOSITORY_BEAN_NAME);

		return (RTRepositoryInterface) factory.create();
	}

	public static ImageRepositoryInterface getImageRepositoryClient() {
		ClientFactory factory = (ClientFactory) context
				.getBean(IMAGEREPOSITORY_BEAN_NAME);

		return (ImageRepositoryInterface) factory.create();
	}

	public static SchedulerInterface getSchedulerClient() {
		ClientFactory factory = (ClientFactory) context
				.getBean(SCHEDULER_BEAN_NAME);

		return (SchedulerInterface) factory.create();
	}

	public static UserRepositoryInterface getUserRepositoryClient() {
		ClientFactory factory = (ClientFactory) context
				.getBean(USERREPOSITORY_BEAN_NAME);
		return (UserRepositoryInterface) factory.create();
	}

	public static CCDTeleoperationInterface getCCDTeleoperationClient() {
		ClientFactory factory = (ClientFactory) context
				.getBean(CCDTELEOPERATION_BEAN_NAME);
		return (CCDTeleoperationInterface) factory.create();
	}

	public static DomeTeleoperationInterface getDomeTeleoperationClient() {
		ClientFactory factory = (ClientFactory) context
				.getBean(DOMETELEOPERATION_BEAN_NAME);
		return (DomeTeleoperationInterface) factory.create();
	}

	public static WeatherTeleoperationInterface getWeatherTeleoperationClient() {
		ClientFactory factory = (ClientFactory) context
				.getBean(WEATHERTELEOPERATION_BEAN_NAME);
		return (WeatherTeleoperationInterface) factory.create();
	}

	public static MountTeleoperationInterface getMountTeleoperationClient() {
		ClientFactory factory = (ClientFactory) context
				.getBean(MOUNTTELEOPERATION_BEAN_NAME);
		return (MountTeleoperationInterface) factory.create();
	}

	public static SCamTeleoperationInterface getSCamTeleoperationClient() {
		ClientFactory factory = (ClientFactory) context
				.getBean(SCAMTELEOPERATION_BEAN_NAME);
		return (SCamTeleoperationInterface) factory.create();
	}

	public static FilterWheelTeleoperationInterface getFilterWheelTeleoperationClient() {
		ClientFactory factory = (ClientFactory) context
				.getBean(FWTELEOPERATION_BEAN_NAME);
		return (FilterWheelTeleoperationInterface) factory.create();
	}

	public static ExperimentInterface getOnlineExperimentClient() {
		ClientFactory factory = (ClientFactory) context
				.getBean(ONLINEEXPERIMENT_BEAN_NAME);
		return (ExperimentInterface) factory.create();
	}

	public static FocuserTeleoperationInterface getFocuserTeleoperationClient() {
		ClientFactory factory = (ClientFactory) context
				.getBean(FOCUSERTELEOPERATION_BEAN_NAME);
		return (FocuserTeleoperationInterface) factory.create();
	}

	public static GenericTeleoperationInterface getGenericTeleoperationClient() {
		ClientFactory factory = (ClientFactory) context
				.getBean(GENERICTELEOPERATION_BEAN_NAME);
		return (GenericTeleoperationInterface) factory.create();
	}
}
