package eu.gloria.gs.services.teleoperation.fw;

import java.util.List;

import javax.jws.WebService;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;

@WebService(name = "FilterWheelTeleoperationInterface", targetNamespace = "http://fw.teleoperation.services.gs.gloria.eu/")
public interface FilterWheelTeleoperationInterface {

	public List<String> getFilters(String rt, String filterWheel)
			throws DeviceOperationFailedException, FilterWheelTeleoperationException;

	public void selectFilter(String rt, String filterWheel, String filter)
			throws DeviceOperationFailedException, FilterWheelTeleoperationException;
}
