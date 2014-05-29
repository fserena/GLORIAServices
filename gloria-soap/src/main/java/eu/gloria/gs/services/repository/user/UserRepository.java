package eu.gloria.gs.services.repository.user;

import eu.gloria.gs.services.core.GSLogProducerService;
import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.log.action.Action;
import eu.gloria.gs.services.log.action.ServiceOperation;
import eu.gloria.gs.services.log.action.Param;
import eu.gloria.gs.services.repository.user.data.UserInformation;
import eu.gloria.gs.services.repository.user.data.UserRepositoryAdapter;

public class UserRepository extends GSLogProducerService implements
		UserRepositoryInterface {

	private UserRepositoryAdapter adapter;

	public UserRepository() {
		super(UserRepository.class.getSimpleName());
	}

	public void setAdapter(UserRepositoryAdapter adapter) {
		this.adapter = adapter;
		this.adapter.init();
	}

	@Override
	@ServiceOperation(name = "create user")
	public void createUser(@Param(name = "name") String name,
			@Param(name = "alias") String alias) throws UserRepositoryException {

		Action action = new Action(UserRepository.class, "createUser", name,
				alias);

		try {
			adapter.create(name, alias);
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw new UserRepositoryException(action);
		}
	}

	@Override
	@ServiceOperation(name = "activate user")
	public void activateUser(@Param(name = "name") String name, String password)
			throws UserRepositoryException {

		Action action = new Action(UserRepository.class, "activateUser", name);

		try {
			adapter.activate(name, password);
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw new UserRepositoryException(action);
		}
	}

	@Override
	@ServiceOperation(name = "contains user")
	public boolean containsUser(@Param(name = "name") String name)
			throws UserRepositoryException {

		Action action = new Action(UserRepository.class, "containsUser", name);

		try {
			boolean contains = adapter.containsName(name);
			return contains;
		} catch (ActionException e) {
			this.logException(action, e);
			throw new UserRepositoryException(action);
		}
	}

	@Override
	@ServiceOperation(name = "contains user")
	public boolean containsAlias(@Param(name = "alias")String alias)
			throws UserRepositoryException {

		Action action = new Action(UserRepository.class, "contcontainsAlias", alias);

		try {
			boolean contains = adapter.containsAlias(alias);
			return contains;
		} catch (ActionException e) {
			this.logException(action, e);
			throw new UserRepositoryException(action);
		}
	}

	@Override
	@ServiceOperation(name = "deactivate user")
	public void deactivateUser(@Param(name = "name") String name,
			String password) throws UserRepositoryException {

		Action action = new Action(UserRepository.class, "deactivateUser", name);

		try {
			adapter.deactivate(name, password);
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw new UserRepositoryException(action);
		}
	}

	@Override
	@ServiceOperation(name = "change password")
	public void changePassword(@Param(name = "name") String name,
			String password) throws UserRepositoryException {

		Action action = new Action(UserRepository.class, "changePassword", name);

		try {
			adapter.setPassword(name, password);
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw new UserRepositoryException(action);
		}

	}

	@Override
	@ServiceOperation(name = "get user info")
	public UserInformation getUserInformation(@Param(name = "name") String name)
			throws UserRepositoryException {

		Action action = new Action(UserRepository.class, "getUserInformation",
				name);

		UserInformation userInfo = null;

		try {
			userInfo = adapter.getUserInformation(name);
		} catch (ActionException e) {
			this.logException(action, e);
			throw new UserRepositoryException(action);
		}

		return userInfo;
	}

	@Override
	@ServiceOperation(name = "get user credentials")
	public UserInformation getUserCredentials(@Param(name = "name") String name)
			throws UserRepositoryException {

		Action action = new Action(UserRepository.class, "getUserCredentials",
				name);

		UserInformation userInfo = null;

		try {
			userInfo = adapter.getUserCredentials(name);
		} catch (ActionException e) {
			this.logException(action, e);
			throw new UserRepositoryException(action);
		}

		return userInfo;
	}

	@Override
	@ServiceOperation(name = "authenticate user")
	public boolean authenticateUser(@Param(name = "name") String name,
			String password) throws UserRepositoryException {

		Action action = new Action(UserRepository.class, "authenticateUser",
				name);

		try {
			if (password == null) {
				throw new ActionException("null password cannot be used");
			}

			if (adapter.containsName(name) && adapter.isActivated(name)) {
				String actualPassword = adapter.getPassword(name);

				boolean result = false;
				result = password.equals(actualPassword);

				return result;
			}
		} catch (ActionException e) {
			this.logException(action, e);
			throw new UserRepositoryException(action);
		}

		return false;
	}

	@Override
	@ServiceOperation(name = "set language")
	public void setUserLanguage(@Param(name = "name") String name,
			@Param(name = "language") String language)
			throws UserRepositoryException {
		Action action = new Action(UserRepository.class, "setUserLanguage",
				name, language);

		try {
			adapter.setLanguage(name, language);
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw new UserRepositoryException(action);
		}

	}

	@Override
	@ServiceOperation(name = "set ocupation")
	public void setUserOcupation(@Param(name = "name") String name,
			@Param(name = "ocupation") String ocupation)
			throws UserRepositoryException {
		Action action = new Action(UserRepository.class, "setUserOcupation",
				name, ocupation);

		try {
			adapter.setOcupation(name, ocupation);
			this.logInfo(action);
		} catch (ActionException e) {
			this.logException(action, e);
			throw new UserRepositoryException(action);
		}

	}
}
