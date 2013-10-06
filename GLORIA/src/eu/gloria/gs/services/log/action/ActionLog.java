package eu.gloria.gs.services.log.action;

import java.util.Date;

import eu.gloria.gs.services.core.GSWebService;
import eu.gloria.gs.services.log.action.ActionLogException;
import eu.gloria.gs.services.log.action.ActionLogInterface;
import eu.gloria.gs.services.log.action.data.ActionLogAdapter;
import eu.gloria.gs.services.log.action.data.dbservices.ActionLogAdapterException;

public class ActionLog extends GSWebService implements ActionLogInterface {

	private ActionLogAdapter adapter;

	public ActionLog() {		
	}

	public void setAdapter(ActionLogAdapter adapter) {
		this.adapter = adapter;
		adapter.init();
	}

	@Override
	public void registerAction(String username, Date when, String action)
			throws ActionLogException {

		try {
			adapter.registerAction(username, when, action);
		} catch (ActionLogAdapterException e) {

			throw new ActionLogException(e.getMessage());
		}
	}

}
