package eu.gloria.gs.services.teleoperation.base;

public interface ServerResolver {

	public String resolve(String server) throws TeleoperationException;
	public ServerHandler getHandler(String server);
	
}
