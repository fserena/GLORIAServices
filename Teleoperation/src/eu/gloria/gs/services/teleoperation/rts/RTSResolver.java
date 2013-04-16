package eu.gloria.gs.services.teleoperation.rts;

import java.util.Date;
import java.util.HashMap;

import eu.gloria.gs.services.repository.rt.RTRepositoryInterface;
import eu.gloria.gs.services.teleoperation.base.ServerHandler;
import eu.gloria.gs.services.teleoperation.base.ServerResolver;
import eu.gloria.rti.client.RTSManager;

public class RTSResolver implements ServerResolver {

	private RTRepositoryInterface repository = null;
	private HashMap<String, String> urlTable = new HashMap<String, String>();
	private HashMap<String, Date> dateTable = new HashMap<String, Date>();

	public void setRTRepository(RTRepositoryInterface repository) {
		this.repository = repository;
	}
	
	@Override
	public String resolve(String server) throws Exception {

		synchronized (urlTable) {
			if (!urlTable.containsKey(server)) {

				String url = repository.getRTUrl(server);
				urlTable.put(server, url);
				dateTable.put(server, new Date());

				return url;
			}

			Date savedDate = dateTable.get(server);
			Date currentDate = new Date();

			if (currentDate.getTime() - savedDate.getTime() > 10000) {

				String url = repository.getRTUrl(server);
				urlTable.put(server, url);
				dateTable.put(server, new Date());
			}

			return urlTable.get(server);
		}
	}

	@Override
	public ServerHandler getHandler(String server) {

		try {
			String url = this.resolve(server);
			return RTSManager.getReference().getRTS(url);
		} catch (Exception e) {
			return null;
		}
	}

}
