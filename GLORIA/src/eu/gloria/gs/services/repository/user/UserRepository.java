package eu.gloria.gs.services.repository.user;

import eu.gloria.gs.services.core.GSLogProducerService;
import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.repository.user.data.UserInformation;
import eu.gloria.gs.services.repository.user.data.UserRepositoryAdapter;
import eu.gloria.gs.services.repository.user.data.dbservices.UserRepositoryAdapterException;

public class UserRepository extends GSLogProducerService implements
		UserRepositoryInterface {

	private UserRepositoryAdapter adapter;

	public UserRepository() {
	}

	public void setAdapter(UserRepositoryAdapter adapter) {
		this.adapter = adapter;
		this.adapter.init();
	}

	@Override
	public void createUser(String name) throws UserRepositoryException {

		LogAction action = new LogAction();

		action.put("sender", this.getUsername());
		action.put("operation", "new user");
		action.put("name", name);

		try {
			adapter.create(name);

			this.logInfo(this.getClientUsername(), action);
		} catch (UserRepositoryAdapterException e) {
			action.put("cause", e.getAction());
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new UserRepositoryException(action);
		}
	}

	@Override
	public void activateUser(String name, String password)
			throws UserRepositoryException {

		LogAction action = new LogAction();

		action.put("sender", this.getUsername());
		action.put("operation", "activate user");
		action.put("name", name);

		try {
			adapter.activate(name, password);
			this.logInfo(this.getClientUsername(), action);
		} catch (UserRepositoryAdapterException e) {
			action.put("cause", e.getAction());
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new UserRepositoryException(action);
		}
	}

	@Override
	public boolean containsUser(String name) throws UserRepositoryException {

		try {
			boolean contains = adapter.contains(name);
			return contains;
		} catch (UserRepositoryAdapterException e) {
			throw new UserRepositoryException(e.getAction());
		}
	}

	@Override
	public void deactivateUser(String name, String password)
			throws UserRepositoryException {

		try {
			adapter.deactivate(name, password);
		} catch (UserRepositoryAdapterException e) {
			throw new UserRepositoryException(e.getAction());
		}

	}

	@Override
	public void changePassword(String name, String password)
			throws UserRepositoryException {

		LogAction action = new LogAction();

		action.put("sender", this.getUsername());
		action.put("operation", "change user");
		action.put("name", name);
		
		try {
			adapter.setPassword(name, password);
			this.logInfo(getClientUsername(), action);
		} catch (UserRepositoryAdapterException e) {
			action.put("cause", e.getAction());
			this.logError(getClientUsername(), action);
			action.put("more", e.getAction());
			throw new UserRepositoryException(action);
		}

	}

	@Override
	public UserInformation getUserInformation(String name)
			throws UserRepositoryException {

		UserInformation userInfo = null;

		try {
			userInfo = adapter.getUserInformation(name);
		} catch (UserRepositoryAdapterException e) {
			throw new UserRepositoryException(e.getAction());
		}

		return userInfo;
	}

	@Override
	public boolean authenticateUser(String name, String password)
			throws UserRepositoryException {

		if (password == null)
			throw new UserRepositoryException("null password cannot be used");

		try {

			if (adapter.contains(name) && adapter.isActivated(name)) {
				String actualPassword = adapter.getPassword(name);

				boolean result = false;
				result = password.equals(actualPassword);

				// this.logAction(this.getClientUsername(),
				// "/repository/user/authenticate?" + name + "&" + result);

				return result;
			}
		} catch (UserRepositoryAdapterException e) {
			throw new UserRepositoryException(e.getAction());
		}

		return false;
	}
}
