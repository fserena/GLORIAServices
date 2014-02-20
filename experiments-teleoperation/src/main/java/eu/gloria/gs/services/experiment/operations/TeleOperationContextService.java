package eu.gloria.gs.services.experiment.operations;

import eu.gloria.gs.services.repository.image.ImageRepositoryInterface;
import eu.gloria.gs.services.repository.rt.RTRepositoryInterface;
import eu.gloria.gs.services.teleoperation.ccd.CCDTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.dome.DomeTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.focuser.FocuserTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.fw.FilterWheelTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.mount.MountTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.scam.SCamTeleoperationInterface;
import eu.gloria.gs.services.teleoperation.weather.WeatherTeleoperationInterface;

public class TeleOperationContextService extends OperationContextService {

	private RTRepositoryInterface rt;
	private CCDTeleoperationInterface ccd;
	private DomeTeleoperationInterface dome;
	private SCamTeleoperationInterface scam;
	private MountTeleoperationInterface mount;
	private FocuserTeleoperationInterface focus;
	private FilterWheelTeleoperationInterface fw;
	private ImageRepositoryInterface image;
	private WeatherTeleoperationInterface weather;

	@Override
	protected void makeUpService(ServiceOperation service) {
		super.makeUpService(service);
		((TeleOperation) service).setImageRepository(image);
		((TeleOperation) service).setRTRepository(rt);
		((TeleOperation) service).setCCDTeleoperation(ccd);
		((TeleOperation) service).setMountTeleoperation(mount);
		((TeleOperation) service).setFilterWheelTeleoperation(fw);
		((TeleOperation) service).setFocuserTeleoperation(focus);
		((TeleOperation) service).setSCamTeleoperation(scam);
		((TeleOperation) service).setWeatherTeleoperation(weather);
		((TeleOperation) service).setDomeTeleoperation(dome);
		
	}

	public RTRepositoryInterface getRt() {
		return rt;
	}

	public void setRt(RTRepositoryInterface rt) {
		this.rt = rt;
	}

	public CCDTeleoperationInterface getCcd() {
		return ccd;
	}

	public void setCcd(CCDTeleoperationInterface ccd) {
		this.ccd = ccd;
	}

	public DomeTeleoperationInterface getDome() {
		return dome;
	}

	public void setDome(DomeTeleoperationInterface dome) {
		this.dome = dome;
	}

	public SCamTeleoperationInterface getScam() {
		return scam;
	}

	public void setScam(SCamTeleoperationInterface scam) {
		this.scam = scam;
	}

	public MountTeleoperationInterface getMount() {
		return mount;
	}

	public void setMount(MountTeleoperationInterface mount) {
		this.mount = mount;
	}

	public FocuserTeleoperationInterface getFocus() {
		return focus;
	}

	public void setFocus(FocuserTeleoperationInterface focus) {
		this.focus = focus;
	}

	public FilterWheelTeleoperationInterface getFw() {
		return fw;
	}

	public void setFw(FilterWheelTeleoperationInterface fw) {
		this.fw = fw;
	}

	public ImageRepositoryInterface getImage() {
		return image;
	}

	public void setImage(ImageRepositoryInterface image) {
		this.image = image;
	}

	public WeatherTeleoperationInterface getWeather() {
		return weather;
	}

	public void setWeather(WeatherTeleoperationInterface weather) {
		this.weather = weather;
	}

}
