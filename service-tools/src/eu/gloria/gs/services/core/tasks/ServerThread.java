package eu.gloria.gs.services.core.tasks;

public abstract class ServerThread extends Thread {

	private boolean finish = false;

	public ServerThread() {
		super();
	}

	protected abstract void doWork();

	@Override
	public void run() {
		while (!finish) {
			try {
				this.doWork();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void end() {
		this.finish = true;
	}

}
