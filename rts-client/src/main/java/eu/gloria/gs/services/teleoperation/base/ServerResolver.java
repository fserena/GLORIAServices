package eu.gloria.gs.services.teleoperation.base;

import eu.gloria.gs.services.repository.rt.data.ServerKeyData;

public interface ServerResolver {

	public ServerKeyData resolve(String server) throws TeleoperationException;
	public ServerHandler getHandler(String server) throws ServerNotAvailableException;
	
}
