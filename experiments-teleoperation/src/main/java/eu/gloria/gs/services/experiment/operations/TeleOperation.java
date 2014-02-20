/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
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

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public abstract class TeleOperation extends ServiceOperation {

	private RTRepositoryInterface rt;	
	private CCDTeleoperationInterface ccd;
	private DomeTeleoperationInterface dome;
	private SCamTeleoperationInterface scam;
	private MountTeleoperationInterface mount;
	private FocuserTeleoperationInterface focus;
	private FilterWheelTeleoperationInterface fw;
	private ImageRepositoryInterface image;
	private WeatherTeleoperationInterface weather;
	
	public TeleOperation() {
		
	}	
	
	public ImageRepositoryInterface getImageRepository() {
		return image;
	}

	public void setImageRepository(ImageRepositoryInterface image) {
		this.image = image;
	}



	public void setRTRepository(RTRepositoryInterface rtRepository) {
		this.rt = rtRepository;
	}

	public RTRepositoryInterface getRTRepository() {
		return this.rt;
	}

	public void setCCDTeleoperation(CCDTeleoperationInterface ccd) {
		this.ccd = ccd;
	}

	public CCDTeleoperationInterface getCCDTeleoperation() {
		return this.ccd;
	}

	public void setDomeTeleoperation(DomeTeleoperationInterface dome) {
		this.dome = dome;
	}

	public DomeTeleoperationInterface getDomeTeleoperation() {
		return this.dome;
	}

	public void setSCamTeleoperation(SCamTeleoperationInterface scam) {
		this.scam = scam;
	}

	public SCamTeleoperationInterface getSCamTeleoperation() {
		return this.scam;
	}

	public void setMountTeleoperation(MountTeleoperationInterface mount) {
		this.mount = mount;
	}

	public MountTeleoperationInterface getMountTeleoperation() {
		return this.mount;
	}

	public void setFocuserTeleoperation(FocuserTeleoperationInterface focus) {
		this.focus = focus;
	}

	public FocuserTeleoperationInterface getFocuserTeleoperation() {
		return this.focus;
	}

	public void setFilterWheelTeleoperation(FilterWheelTeleoperationInterface fw) {
		this.fw = fw;
	}

	public FilterWheelTeleoperationInterface getFilterWheelTeleoperation() {
		return this.fw;
	}
	
	public WeatherTeleoperationInterface getWeatherTeleoperation() {
		return weather;
	}

	public void setWeatherTeleoperation(WeatherTeleoperationInterface weather) {
		this.weather = weather;
	}
}
