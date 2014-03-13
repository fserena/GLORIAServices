package eu.gloria.gs.services.repository.user;

import javax.jws.WebParam;
import javax.jws.WebService;

import eu.gloria.gs.services.repository.user.data.UserInformation;

@WebService(name = "UserRepositoryInterface", targetNamespace = "http://user.repository.services.gs.gloria.eu/")
public interface UserRepositoryInterface {

	public void createUser(@WebParam(name = "name") String name,
			@WebParam(name = "alias") String alias)
			throws UserRepositoryException;

	public void activateUser(@WebParam(name = "name") String name,
			@WebParam(name = "password") String password)
			throws UserRepositoryException;

	public boolean containsAlias(@WebParam(name = "alias") String alias)
			throws UserRepositoryException;

	public boolean contains(@WebParam(name = "name") String name,
			@WebParam(name = "alias") String alias)
			throws UserRepositoryException;

	public boolean containsUser(@WebParam(name = "name") String name)
			throws UserRepositoryException;

	public void deactivateUser(@WebParam(name = "name") String name,
			@WebParam(name = "password") String password)
			throws UserRepositoryException;

	public void changePassword(@WebParam(name = "name") String name,
			@WebParam(name = "password") String password)
			throws UserRepositoryException;

	public UserInformation getUserInformation(
			@WebParam(name = "name") String name)
			throws UserRepositoryException;

	public UserInformation getUserCredentials(
			@WebParam(name = "name") String name)
			throws UserRepositoryException;

	public boolean authenticateUser(@WebParam(name = "name") String name,
			@WebParam(name = "password") String password)
			throws UserRepositoryException;

	public void setUserLanguage(@WebParam(name = "name") String name,
			@WebParam(name = "language") String language)
			throws UserRepositoryException;

	public void setUserOcupation(@WebParam(name = "name") String name,
			@WebParam(name = "ocupation") String ocupation)
			throws UserRepositoryException;
}
