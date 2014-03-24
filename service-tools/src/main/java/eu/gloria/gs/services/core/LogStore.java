package eu.gloria.gs.services.core;

import java.util.ArrayList;
import java.util.List;

public class LogStore {

	private static ArrayList<LogEntry> entries = new ArrayList<LogEntry>();
	private static Object sync = new Object();

	public LogStore() {
	}

	public void addEntry(LogEntry entry) {
		synchronized (sync) {
			entries.add(entry);
		}
	}

	public void removeEntry(LogEntry entry) {
		synchronized (sync) {
			entries.remove(entry);
		}
	}

	public void clearEntries() {
		synchronized (sync) {
			entries.clear();
		}
	}

	public LogEntry getEntry(int index) {
		synchronized (sync) {
			if (entries.size() > index)
				return entries.get(index);
			else
				return null;
		}
	}

	public List<LogEntry> getEntries() {
		return entries;
	}

}
