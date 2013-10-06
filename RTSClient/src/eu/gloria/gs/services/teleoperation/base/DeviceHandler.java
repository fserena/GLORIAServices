package eu.gloria.gs.services.teleoperation.base;

import eu.gloria.rti.client.RTSHandler;

public class DeviceHandler {
	
	protected RTSHandler rts;
	
	public DeviceHandler(RTSHandler rts) {
		this.rts = rts;
	}
	
	public boolean isTeleoperationStarted() {
		return rts.isTeleoperationStarted();
	}

}
