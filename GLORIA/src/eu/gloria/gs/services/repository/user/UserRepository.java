package eu.gloria.gs.services.repository.user;

import eu.gloria.gs.services.core.GSLogProducerService;
import eu.gloria.gs.services.log.action.ActionLogException;
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

		try {
			adapter.create(name);
		} catch (UserRepositoryAdapterException e) {
			throw new UserRepositoryException(e.getMessage());
		}

		try {
			this.logAction(this.getClientUsername(), "/repository/user/new?"
					+ name);
		} catch (ActionLogException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void activateUser(String name, String password)
			throws UserRepositoryException {

		try {
			adapter.activate(name, password);
		} catch (UserRepositoryAdapterException e) {
			throw new UserRepositoryException(e.getMessage());
		}

		try {
			this.logAction(this.getClientUsername(), "/repository/user/activate?"
					+ name);
		} catch (ActionLogException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean containsUser(String name) throws UserRepositoryException {

		try {
			boolean contains = adapter.contains(name);
			return contains;
		} catch (UserRepositoryAdapterException e) {
			throw new UserRepositoryException(e.getMessage());
		}
	}

	@Override
	public void deactivateUser(String name, String password)
			throws UserRepositoryException {

		try {
			adapter.deactivate(name, password);
		} catch (UserRepositoryAdapterException e) {
			throw new UserRepositoryException(e.getMessage());
		}

		try {
			this.logAction(this.getClientUsername(), "/repository/user/deactivate?"
					+ name);
		} catch (ActionLogException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void changePassword(String name, String password)
			throws UserRepositoryException {

		try {
			adapter.setPassword(name, password);
		} catch (UserRepositoryAdapterException e) {
			throw new UserRepositoryException(e.getMessage());
		}

		try {
			this.logAction(this.getClientUsername(), "/repository/user/changePassword?"
					+ name + "&" + password);
		} catch (ActionLogException e) {
			e.printStackTrace();
		}

	}

	@Override
	public UserInformation getUserInformation(String name)
			throws UserRepositoryException {

		UserInformation userInfo = null;

		try {
			userInfo = adapter.getUserInformation(name);
		} catch (UserRepositoryAdapterException e) {
			throw new UserRepositoryException(e.getMessage());
		}

		return userInfo;
	}

	@Override
	public boolean authenticateUser(String name, String password)
			throws UserRepositoryException {

		if (password == null)
			throw new UserRepositoryException("A null password cannot be used");

		try {

			if (adapter.contains(name) && adapter.isActivated(name)) {
				String actualPassword = adapter.getPassword(name);

				boolean result = false;				
				result = password.equals(actualPassword);
				
				try {
					this.logAction(this.getClientUsername(), "/repository/user/authenticate?"
							+ name + "&" + result);
				} catch (ActionLogException e) {
					e.printStackTrace();
				}
				
				return result;
			}
		} catch (UserRepositoryAdapterException e) {
			throw new UserRepositoryException(e.getMessage());
		}

		return false;
	}
}
