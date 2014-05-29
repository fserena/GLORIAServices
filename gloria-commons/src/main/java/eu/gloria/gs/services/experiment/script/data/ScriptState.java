package eu.gloria.gs.services.experiment.script.data;

public enum ScriptState {
	SCHEDULED, PREPARED, READY, TRIGGERED, WAITING_INVOKE, WAITING_END, DONE, ERROR, NOTIFYING
}
