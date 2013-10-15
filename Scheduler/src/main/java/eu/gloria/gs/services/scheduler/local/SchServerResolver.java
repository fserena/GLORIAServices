package eu.gloria.gs.services.scheduler.local;

import eu.gloria.gs.services.repository.rt.data.ServerKeyData;

public interface SchServerResolver {

	public ServerKeyData resolve(String server) throws SchServerNotAvailableException;
	public SchHandler getHandler(String server);
	
}
