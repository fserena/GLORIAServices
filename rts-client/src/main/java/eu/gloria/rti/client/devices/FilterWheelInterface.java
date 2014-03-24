package eu.gloria.rti.client.devices;

import java.util.List;

import eu.gloria.gs.services.teleoperation.base.TeleoperationException;

public interface FilterWheelInterface {

	public List<String> getFilters() throws TeleoperationException;

	public void selectFilter(String filter) throws TeleoperationException;
}
