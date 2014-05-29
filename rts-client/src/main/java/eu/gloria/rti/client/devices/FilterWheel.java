package eu.gloria.rti.client.devices;

import java.util.List;

import eu.gloria.gs.services.teleoperation.base.DeviceHandler;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.RTSHandler;

public class FilterWheel extends DeviceHandler implements FilterWheelInterface {

	private String filterWheel;

	public FilterWheel(RTSHandler rts, String focuser) throws TeleoperationException {

		super(rts);
		this.filterWheel = focuser;
	}

	@Override
	public List<String> getFilters() throws TeleoperationException {
		return rts.getAvailableFilters(this.filterWheel);
	}
	
	@Override
	public void selectFilter(String filter) throws TeleoperationException {
		rts.selectFilter(this.filterWheel, filter);
	}
}
