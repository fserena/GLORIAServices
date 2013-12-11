package eu.gloria.gs.services.teleoperation.rts;

import java.util.Date;
import java.util.HashMap;

import eu.gloria.gs.services.repository.rt.RTRepositoryInterface;
import eu.gloria.gs.services.repository.rt.data.RTCredentials;
import eu.gloria.gs.services.repository.rt.data.RTInformation;
import eu.gloria.gs.services.repository.rt.data.ServerKeyData;
import eu.gloria.gs.services.teleoperation.base.ServerNotAvailableException;
import eu.gloria.gs.services.teleoperation.base.ServerHandler;
import eu.gloria.gs.services.teleoperation.base.ServerResolver;
import eu.gloria.rti.client.RTSManager;

public class RTSResolver implements ServerResolver {

	private RTRepositoryInterface repository = null;
	private HashMap<String, ServerKeyData> urlTable = new HashMap<String, ServerKeyData>();
	private HashMap<String, Date> dateTable = new HashMap<String, Date>();

	public void setRTRepository(RTRepositoryInterface repository) {
		this.repository = repository;
	}

	@Override
	public ServerKeyData resolve(String server)
			throws ServerNotAvailableException {

		synchronized (urlTable) {
			if (!urlTable.containsKey(server)) {

				ServerKeyData keyData = new ServerKeyData();
				try {
					RTInformation rtInfo = repository.getRTInformation(server);

					keyData.setUrl(rtInfo.getUrl());
					keyData.setPort(rtInfo.getPort());
					RTCredentials rtCredentials = new RTCredentials();
					rtCredentials.setUser(rtInfo.getUser());
					rtCredentials.setPassword(rtInfo.getPassword());
					keyData.setCredentials(rtCredentials);
				} catch (Exception e) {
					throw new ServerNotAvailableException(server);
				}
				urlTable.put(server, keyData);
				dateTable.put(server, new Date());

				return keyData;
			}

			Date savedDate = dateTable.get(server);
			Date currentDate = new Date();

			if (currentDate.getTime() - savedDate.getTime() > 10000) {

				RTInformation rtInfo = null;
				try {
					rtInfo = repository.getRTInformation(server);
				} catch (Exception e) {
					throw new ServerNotAvailableException(server);
				}

				ServerKeyData keyData = new ServerKeyData();

				keyData.setUrl(rtInfo.getUrl());
				keyData.setPort(rtInfo.getPort());
				RTCredentials rtCredentials = new RTCredentials();
				rtCredentials.setUser(rtInfo.getUser());
				rtCredentials.setPassword(rtInfo.getPassword());
				keyData.setCredentials(rtCredentials);

				urlTable.put(server, keyData);
				dateTable.put(server, new Date());
			}

			return urlTable.get(server);
		}
	}

	@Override
	public ServerHandler getHandler(String server) throws ServerNotAvailableException {

		try {
			ServerKeyData keyData = this.resolve(server);
			return RTSManager.getReference().getRTS(keyData);
		} catch (Exception e) {
			throw new ServerNotAvailableException(e.getMessage());
		}
	}

}
