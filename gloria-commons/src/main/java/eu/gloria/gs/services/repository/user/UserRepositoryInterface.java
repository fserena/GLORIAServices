package eu.gloria.gs.services.repository.user;

import javax.jws.WebService;

import eu.gloria.gs.services.repository.user.data.UserInformation;

@WebService(name = "UserRepositoryInterface", targetNamespace = "http://user.repository.services.gs.gloria.eu/")
public interface UserRepositoryInterface {

	public void createUser(String name, String alias)
			throws UserRepositoryException;

	public void activateUser(String name, String password)
			throws UserRepositoryException;

	public boolean containsAlias(String alias) throws UserRepositoryException;

	public boolean containsUser(String name) throws UserRepositoryException;

	public void deactivateUser(String name, String password)
			throws UserRepositoryException;

	public void changePassword(String name, String password)
			throws UserRepositoryException;

	public UserInformation getUserInformation(String name)
			throws UserRepositoryException;

	public UserInformation getUserCredentials(String name)
			throws UserRepositoryException;

	public boolean authenticateUser(String name, String password)
			throws UserRepositoryException;

	public void setUserLanguage(String name, String language)
			throws UserRepositoryException;

	public void setUserOcupation(String name, String ocupation)
			throws UserRepositoryException;
}
