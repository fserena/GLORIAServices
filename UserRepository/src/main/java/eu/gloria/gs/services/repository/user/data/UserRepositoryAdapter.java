package eu.gloria.gs.services.repository.user.data;

import eu.gloria.gs.services.repository.user.data.dbservices.UserDBService;
import eu.gloria.gs.services.repository.user.data.dbservices.UserEntry;
import eu.gloria.gs.services.repository.user.data.dbservices.UserRepositoryAdapterException;

public class UserRepositoryAdapter {

	final private String ADMIN_ROLE = "ADM";
	final private String REGULAR_ROLE = "REG";
	final private String OWNER_ROLE = "OWN";
	final private String WS_ROLE = "WS";
	final String ADMIN_NAME = "dummy";
	final String ADMIN_PWD = "dummy";
	final String USER_REPOSITORY_NAME = "dummy";
	final String USER_REPOSITORY_PWD = "dummy";
	final String RT_REPOSITORY_NAME = "dummy";
	final String RT_REPOSITORY_PWD = "dummy";
	final String ACTION_LOG_NAME = "dummy";
	final String ACTION_LOG_PWD = "dummy";
	final String TELEOPERATION_NAME = "dummy";
	final String TELEOPERATION_PWD = "dummy";
	final String EXPERIMENT_NAME = "dummy";
	final String EXPERIMENT_PWD = "dummy";
	final String LIFERAY_NAME = "dummy";
	final String LIFERAY_PWD = "dummy";
	final String IMAGE_REPOSITORY_NAME = "dummy";
	final String IMAGE_REPOSITORY_PWD = "dummy";
	private UserDBService userService;

	public UserRepositoryAdapter() {

	}

	public void setUserDBService(UserDBService service) {
		this.userService = service;
	}

	private void createUser(String name, String password, UserRole role) {

		if (!userService.contains(name)) {
			UserEntry admin = new UserEntry();
			admin.setName(name);
			admin.setPassword(password);

			if (role.equals(UserRole.ADMIN))
				admin.setRoles(ADMIN_ROLE);
			else if (role.equals(UserRole.OWNER))
				admin.setRoles(OWNER_ROLE);
			else if (role.equals(UserRole.REGULAR))
				admin.setRoles(REGULAR_ROLE);
			else if (role.equals(UserRole.WEB_SERVICE))
				admin.setRoles(WS_ROLE);
			userService.save(admin);
		}
	}

	public void init() {

		userService.create();

		this.createUser(ADMIN_NAME, ADMIN_PWD, UserRole.ADMIN);
		this.createUser(USER_REPOSITORY_NAME, USER_REPOSITORY_PWD,
				UserRole.WEB_SERVICE);
		this.createUser(RT_REPOSITORY_NAME, RT_REPOSITORY_PWD,
				UserRole.WEB_SERVICE);
		this.createUser(ACTION_LOG_NAME, ACTION_LOG_PWD, UserRole.WEB_SERVICE);
		this.createUser(TELEOPERATION_NAME, TELEOPERATION_PWD,
				UserRole.WEB_SERVICE);
		this.createUser(EXPERIMENT_NAME, EXPERIMENT_PWD, UserRole.WEB_SERVICE);
		this.createUser(IMAGE_REPOSITORY_NAME, IMAGE_REPOSITORY_PWD,
				UserRole.WEB_SERVICE);
		this.createUser(LIFERAY_NAME, LIFERAY_PWD, UserRole.ADMIN);
	}

	public void create(String name) throws UserRepositoryAdapterException {
		if (name == null)
			throw new UserRepositoryAdapterException("The user cannot be null");

		boolean previouslyContained = false;

		previouslyContained = userService.contains(name);

		if (!previouslyContained) {
			UserEntry entry = new UserEntry();
			entry.setPassword(null);
			entry.setName(name);
			entry.setRoles(this.REGULAR_ROLE);

			userService.save(entry);
		}

		if (previouslyContained)
			throw new UserRepositoryAdapterException("The user '" + name
					+ "' already exists");
	}

	public void activate(String name, String password)
			throws UserRepositoryAdapterException {

		boolean previouslyContained = false;
		boolean previouslyActivated = false;

		previouslyContained = userService.contains(name);

		if (previouslyContained) {

			String oldPassword = userService.getPassword(name);
			if (oldPassword == null) {
				userService.setPassword(name, password);
			} else
				previouslyActivated = true;
		}

		if (!previouslyContained)
			throw new UserRepositoryAdapterException("The user '" + name
					+ "' does not exist");

		if (previouslyActivated)
			throw new UserRepositoryAdapterException("The user '" + name
					+ "' was already activated");
	}

	public void deactivate(String name, String password)
			throws UserRepositoryAdapterException {

		boolean previouslyContained = false;
		boolean previouslyDeactivated = false;

		previouslyContained = userService.contains(name);

		if (previouslyContained) {

			String oldPassword = userService.getPassword(name);
			if (oldPassword != null) {
				userService.setPassword(name, null);
			} else
				previouslyDeactivated = true;
		}

		if (!previouslyContained)
			throw new UserRepositoryAdapterException("The user '" + name
					+ "' does not exist");

		if (previouslyDeactivated)
			throw new UserRepositoryAdapterException("The user '" + name
					+ "' was already deactivated");
	}

	public boolean isActivated(String name)
			throws UserRepositoryAdapterException {

		boolean previouslyActivated = false;

		UserEntry entry = userService.get(name);

		if (entry != null) {
			String entryPassword = entry.getPassword();

			if (entryPassword != null) {
				previouslyActivated = true;
			}
		}

		return previouslyActivated;
	}

	private UserRole[] parseRolesString(String roles) {
		UserRole[] rolesArray = new UserRole[1];

		if (roles.contains("ADM")) {
			rolesArray[0] = UserRole.ADMIN;
		} else if (roles.contains("REG")) {
			rolesArray[0] = UserRole.REGULAR;
		} else if (roles.contains("OWN")) {
			rolesArray[0] = UserRole.OWNER;
		} else if (roles.contains("WS")) {
			rolesArray[0] = UserRole.WEB_SERVICE;
		}

		return rolesArray;
	}

	public UserRole[] getRoles(String name)
			throws UserRepositoryAdapterException {

		UserEntry entry = userService.get(name);

		if (entry != null) {
			return this.parseRolesString(entry.getRoles());
		}

		return null;
	}

	public boolean isAdministrator(String name)
			throws UserRepositoryAdapterException {

		if (this.isActivated(name)) {
			UserRole[] roles = this.getRoles(name);

			if (roles != null) {
				return roles[0].equals(UserRole.ADMIN)
						|| roles[0].equals(UserRole.WEB_SERVICE);
			}
		}

		return false;
	}

	public boolean contains(String name) throws UserRepositoryAdapterException {

		boolean contained = false;

		UserEntry entry = userService.get(name);
		contained = entry != null;

		return contained;
	}

	public String getPassword(String name)
			throws UserRepositoryAdapterException {

		UserEntry entry = userService.get(name);

		if (entry != null) {
			String password = entry.getPassword();

			if (password == null)
				throw new UserRepositoryAdapterException("The user '" + name
						+ "' is not activated");
			else
				return password;
		} else
			throw new UserRepositoryAdapterException("The user '" + name
					+ "' does not exist");

	}

	public void setPassword(String name, String password)
			throws UserRepositoryAdapterException {

		boolean previouslyContained = false;
		boolean previouslyActivated = true;

		previouslyContained = userService.contains(name);

		if (previouslyContained) {

			String oldPassword = userService.getPassword(name);
			if (oldPassword != null) {
				userService.setPassword(name, password);
			} else
				previouslyActivated = false;
		}

		if (!previouslyContained)
			throw new UserRepositoryAdapterException("The user '" + name
					+ "' does not exist");

		if (!previouslyActivated)
			throw new UserRepositoryAdapterException("The user '" + name
					+ "' was not activated");
	}

	public UserInformation getUserInformation(String name)
			throws UserRepositoryAdapterException {

		UserInformation userInfo = new UserInformation();

		if (this.contains(name)) {
			userInfo.setPassword(this.getPassword(name));
			userInfo.setRoles(this.getRoles(name));
			userInfo.setName(name);

			return userInfo;
		}

		return null;
	}
}
