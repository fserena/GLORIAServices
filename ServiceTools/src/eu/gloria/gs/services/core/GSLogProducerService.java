/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.core;

import java.util.Date;

import eu.gloria.gs.services.core.client.GSClientProvider;
import eu.gloria.gs.services.log.action.ActionLogException;
import eu.gloria.gs.services.log.action.ActionLogInterface;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public abstract class GSLogProducerService extends GSWebService {

	private ActionLogInterface alog;

	public void setActionLog(ActionLogInterface alog) {
		this.alog = alog;

	}
	
	public ActionLogInterface getActionLog() {
		return this.alog;
	}

	protected void logAction(String username, String action)
			throws ActionLogException {

		try {
			GSClientProvider.setCredentials(this.getUsername(),
					this.getPassword());

			alog.registerAction(this.getClientUsername(), new Date(), action);
		} catch (ActionLogException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
