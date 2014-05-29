package eu.gloria.gs.services.repository.user.data;

import java.util.Date;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.repository.user.data.dbservices.UserDBService;
import eu.gloria.gs.services.repository.user.data.dbservices.UserEntry;
import eu.gloria.gs.services.utils.LoggerEntity;

public class UserRepositoryAdapter extends LoggerEntity {

	private String ADMIN_ROLE = "ADM";
	private String REGULAR_ROLE = "REG";
	private String OWNER_ROLE = "OWN";
	private String WS_ROLE = "WS";
	private String IMAGE_REPOSITORY_NAME;
	private String IMAGE_REPOSITORY_PWD;
	private String ADMIN_NAME;
	private String ADMIN_PWD;
	private String USER_REPOSITORY_NAME;
	private String USER_REPOSITORY_PWD;
	private String RT_REPOSITORY_NAME;
	private String RT_REPOSITORY_PWD;
	private String ACTION_LOG_NAME;
	private String ACTION_LOG_PWD;
	private String TELEOPERATION_NAME;
	private String TELEOPERATION_PWD;
	private String EXPERIMENT_NAME;
	private String EXPERIMENT_PWD;
	private String SCHEDULER_NAME;
	private String SCHEDULER_PWD;
	private UserDBService userService;

	public UserRepositoryAdapter() {
		super(UserRepositoryAdapter.class.getSimpleName());
	}

	public void setImageRepositoryUsername(String user) {
		this.IMAGE_REPOSITORY_NAME = user;
	}

	public void setImageRepositoryPassword(String pass) {
		this.IMAGE_REPOSITORY_PWD = pass;
	}

	public void setRTRepositoryUsername(String user) {
		this.RT_REPOSITORY_NAME = user;
	}

	public void setRTRepositoryPassword(String pass) {
		this.RT_REPOSITORY_PWD = pass;
	}

	public void setUserRepositoryUsername(String user) {
		this.USER_REPOSITORY_NAME = user;
	}

	public void setUserRepositoryPassword(String pass) {
		this.USER_REPOSITORY_PWD = pass;
	}

	public void setExperimentUsername(String user) {
		this.EXPERIMENT_NAME = user;
	}

	public void setExperimentPassword(String pass) {
		this.EXPERIMENT_PWD = pass;
	}

	public void setTeleoperationUsername(String user) {
		this.TELEOPERATION_NAME = user;
	}

	public void setTeleoperationPassword(String pass) {
		this.TELEOPERATION_PWD = pass;
	}

	public void setActionLogUsername(String user) {
		this.ACTION_LOG_NAME = user;
	}

	public void setActionLogPassword(String pass) {
		this.ACTION_LOG_PWD = pass;
	}

	public void setAdminUsername(String user) {
		this.ADMIN_NAME = user;
	}

	public void setAdminPassword(String pass) {
		this.ADMIN_PWD = pass;
	}

	public void setSchedulerUsername(String user) {
		this.SCHEDULER_NAME = user;
	}

	public void setSchedulerPassword(String pass) {
		this.SCHEDULER_PWD = pass;
	}

	public void setUserDBService(UserDBService service) {
		this.userService = service;
	}

	private void createUser(String name, String password, UserRole role) {
		if (!userService.containsName(name)) {
			UserEntry admin = new UserEntry();
			admin.setName(name);
			admin.setPassword(password);
			admin.setDate(new Date());
			admin.setAlias(name);

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
		this.createUser(SCHEDULER_NAME, SCHEDULER_PWD, UserRole.WEB_SERVICE);
	}

	public void create(String name, String alias) throws ActionException {

		if (name == null) {
			throw new ActionException("user is null");
		}

		if (alias == null) {
			throw new ActionException("alias is null");
		}

		boolean previouslyContained = false;

		previouslyContained = userService.containsName(name);

		if (!previouslyContained) {
			UserEntry entry = new UserEntry();
			entry.setPassword(null);
			entry.setName(name);
			entry.setRoles(this.REGULAR_ROLE);
			entry.setAlias(alias);

			userService.save(entry);
		}

		if (previouslyContained) {
			throw new ActionException("user already exists");
		}
	}

	public void delete(String name) throws ActionException {

		if (name == null) {
			throw new ActionException("user is null");
		}

		boolean previouslyContained = false;

		previouslyContained = userService.containsName(name);

		if (previouslyContained) {
			userService.remove(name);
		}

		if (previouslyContained) {
			throw new ActionException("user does not exist");
		}
	}

	public void activate(String name, String password) throws ActionException {

		boolean previouslyContained = false;
		boolean previouslyActivated = false;

		previouslyContained = userService.containsName(name);

		if (previouslyContained) {

			String oldPassword = userService.getPassword(name);
			if (oldPassword == null) {
				userService.setPassword(name, password);
			} else
				previouslyActivated = true;
		}

		if (!previouslyContained) {
			throw new ActionException("user does not exist");
		}

		if (previouslyActivated) {
			throw new ActionException("user already activated");
		}
	}

	public void deactivate(String name, String password) throws ActionException {

		boolean previouslyContained = false;
		boolean previouslyDeactivated = false;

		previouslyContained = userService.containsName(name);

		if (previouslyContained) {

			String oldPassword = userService.getPassword(name);
			if (oldPassword != null) {
				userService.setPassword(name, null);
			} else
				previouslyDeactivated = true;
		}

		if (!previouslyContained) {
			throw new ActionException("user does not exist");
		}

		if (previouslyDeactivated) {
			throw new ActionException("user already deactivated");
		}
	}

	public boolean isActivated(String name) throws ActionException {

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

	public UserRole[] getRoles(String name) throws ActionException {

		UserEntry entry = userService.get(name);

		if (entry != null) {
			return this.parseRolesString(entry.getRoles());
		}

		return null;
	}

	public boolean isAdministrator(String name) throws ActionException {

		if (this.isActivated(name)) {
			UserRole[] roles = this.getRoles(name);

			if (roles != null) {
				return roles[0].equals(UserRole.ADMIN)
						|| roles[0].equals(UserRole.WEB_SERVICE);
			}
		}

		return false;
	}

	public boolean containsName(String name) throws ActionException {

		return userService.containsName(name);
	}

	public boolean containsAlias(String alias) throws ActionException {

		return userService.containsAlias(alias);
	}

	public boolean contains(String name, String alias) throws ActionException {

		return userService.containsAlias(alias)
				|| userService.containsName(name);
	}

	public Date getCreationDate(String name) throws ActionException {

		UserEntry entry = userService.get(name);

		if (entry != null) {
			return entry.getDate();
		} else {
			throw new ActionException("user does not exist");
		}

	}

	public String getPassword(String name) throws ActionException {

		UserEntry entry = userService.get(name);

		if (entry != null) {
			String password = entry.getPassword();

			if (password == null) {
				throw new ActionException("user not activated");
			} else
				return password;
		} else {
			throw new ActionException("user does not exist");
		}

	}

	public void setPassword(String name, String password)
			throws ActionException {

		boolean previouslyContained = false;
		boolean previouslyActivated = true;

		previouslyContained = userService.containsName(name);

		if (previouslyContained) {

			String oldPassword = userService.getPassword(name);
			if (oldPassword != null) {
				userService.setPassword(name, password);
			} else
				previouslyActivated = false;
		}

		if (!previouslyContained) {
			throw new ActionException("user does not exist");
		}

		if (!previouslyActivated) {
			throw new ActionException("user not activated");
		}
	}

	public String getOcupation(String name) throws ActionException {

		UserEntry entry = userService.get(name);

		if (entry != null) {
			return entry.getOcupation();
		} else {
			throw new ActionException("user does not exist");
		}

	}

	public void setOcupation(String name, String ocupation)
			throws ActionException {

		boolean previouslyContained = false;

		previouslyContained = userService.containsName(name);

		if (previouslyContained) {
			userService.setOcupation(name, ocupation);
		}

		if (!previouslyContained) {
			throw new ActionException("user does not exist");
		}
	}

	public String getLanguage(String name) throws ActionException {

		UserEntry entry = userService.get(name);

		if (entry != null) {
			return entry.getLanguage();
		} else {
			throw new ActionException("user does not exist");
		}

	}

	public void setLanguage(String name, String language)
			throws ActionException {

		boolean previouslyContained = false;

		previouslyContained = userService.containsName(name);

		if (previouslyContained) {
			userService.setOcupation(name, language);
		}

		if (!previouslyContained) {
			throw new ActionException("user does not exist");
		}
	}

	public UserInformation getUserInformation(String name)
			throws ActionException {

		UserInformation userInfo = new UserInformation();

		if (this.containsName(name)) {

			UserEntry entry = userService.get(name);

			userInfo.setPassword(entry.getPassword());
			userInfo.setRoles(this.parseRolesString(entry.getRoles()));
			userInfo.setName(name);
			userInfo.setCreationDate(entry.getDate());
			userInfo.setLanguage(entry.getLanguage());
			userInfo.setOcupation(entry.getOcupation());
			userInfo.setAlias(entry.getAlias());

			return userInfo;
		}

		return null;
	}

	public UserInformation getUserCredentials(String name)
			throws ActionException {

		UserInformation userInfo = new UserInformation();

		if (this.containsName(name)) {

			String password = userService.getPassword(name);

			if (password == null) {
				throw new ActionException("user not activated");
			} else {
				userInfo.setPassword(password);
				String rolesStr = userService.getRoles(name);
				userInfo.setRoles(this.parseRolesString(rolesStr));
				userInfo.setName(name);

				return userInfo;
			}
		} else {
			throw new ActionException("user does not exist");
		}
	}
}
