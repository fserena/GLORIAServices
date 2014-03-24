package eu.gloria.gs.services.core.security.client;

public class ThreadCredentialsStore {

	public static ThreadLocal<Credentials> threadCredentialsStore = new ThreadLocal<Credentials>();

	public static void storeCredentials(Credentials credentials) {
		threadCredentialsStore.set(credentials);
	}

	public static Credentials getCredentials() {
		return threadCredentialsStore.get();
	}

	public static void clear() {
		threadCredentialsStore.remove();
	}
}
