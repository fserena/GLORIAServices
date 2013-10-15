package eu.gloria.gs.services.scheduler.local;

import java.util.HashMap;

import eu.gloria.gs.services.repository.rt.data.ServerKeyData;

public class SchServerManager {

	private static SchServerManager instance = null;
	private static Object sync = new Object();
	private static HashMap<String, SchHandler> schTable = new HashMap<String, SchHandler>();

	private SchServerManager() {

	}

	static public SchServerManager getReference() {
		synchronized (sync) {
			if (instance == null)
				instance = new SchServerManager();
		}

		return instance;
	}

	public SchHandler getSch(ServerKeyData keyData)
			throws SchServerNotAvailableException {

		String requestedUrl = keyData.getUrl();
		String requestedPort = keyData.getPort();

		synchronized (schTable) {
			if (!schTable.containsKey(requestedUrl)) {
				SchHandler sch = new SchHandler(requestedUrl, requestedPort, keyData
						.getCredentials().getUser(), keyData.getCredentials()
						.getPassword());
				schTable.put(requestedUrl, sch);
			}

			return schTable.get(requestedUrl);
		}
	}

}
