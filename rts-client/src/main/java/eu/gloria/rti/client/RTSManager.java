package eu.gloria.rti.client;

import java.util.HashMap;

import eu.gloria.gs.services.repository.rt.data.ServerKeyData;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.RTSHandler;

public class RTSManager {

	private static RTSManager instance = null;
	private static Object sync = new Object();
	private static HashMap<String, RTSHandler> rtsTable = new HashMap<String, RTSHandler>();

	private RTSManager() {

	}

	static public RTSManager getReference() {
		synchronized (sync) {
			if (instance == null)
				instance = new RTSManager();
		}

		return instance;
	}

	public RTSHandler getRTS(ServerKeyData keyData)
			throws TeleoperationException {

		String requestedUrl = keyData.getUrl();
		String requestedPort = keyData.getPort();

		synchronized (rtsTable) {
			if (!rtsTable.containsKey(requestedUrl)) {
				RTSHandler rts = new RTSHandler(requestedUrl, requestedPort, keyData
						.getCredentials().getUser(), keyData.getCredentials()
						.getPassword());
				rtsTable.put(requestedUrl, rts);
			}

			return rtsTable.get(requestedUrl);
		}
	}

}
