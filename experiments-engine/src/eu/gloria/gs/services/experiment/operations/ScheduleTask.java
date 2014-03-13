package eu.gloria.gs.services.experiment.operations;

import java.util.TimerTask;

import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.operations.OperationContext;

public class ScheduleTask extends TimerTask {

	private OperationContext context;

	public ScheduleTask(OperationContext context) {
		this.context = context;
	}

	@Override
	public void run() {
		try {
			this.context.execute();
		} catch (ExperimentOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
