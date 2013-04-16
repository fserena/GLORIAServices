package eu.gloria.gs.services.core.tasks;

public abstract class ServerThread extends Thread {

	private boolean finish = false;
	//private int count = 0;

	public ServerThread() {
		super();
	}	
	
	protected abstract void doWork();

	@Override
	public void run() {
		while (!finish) {
			this.doWork();
		}
	}

	public void doShutdown() {

		this.finish = true;
	}

}
