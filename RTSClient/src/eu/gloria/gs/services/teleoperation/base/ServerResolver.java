package eu.gloria.gs.services.teleoperation.base;

public interface ServerResolver {

	public String resolve(String server) throws Exception;
	public ServerHandler getHandler(String server);
	
}
