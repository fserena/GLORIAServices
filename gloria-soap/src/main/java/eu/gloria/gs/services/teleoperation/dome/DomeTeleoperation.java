package eu.gloria.gs.services.teleoperation.dome;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.dome.operations.CloseOperation;
import eu.gloria.gs.services.teleoperation.dome.operations.GetAzimuthOperation;
import eu.gloria.gs.services.teleoperation.dome.operations.GetStateOperation;
import eu.gloria.gs.services.teleoperation.dome.operations.IsTrackingEnabledOperation;
import eu.gloria.gs.services.teleoperation.dome.operations.OpenOperation;
import eu.gloria.gs.services.teleoperation.dome.operations.ParkOperation;
import eu.gloria.gs.services.teleoperation.dome.operations.SetTrackingOperation;

public class DomeTeleoperation extends AbstractTeleoperation implements
		DomeTeleoperationInterface {

	public DomeTeleoperation() {
		super(DomeTeleoperation.class.getSimpleName());
	}

	@Override
	public void close(String rt, String dome)
			throws DeviceOperationFailedException, DomeTeleoperationException {
		try {
			this.invokeSetOperation(CloseOperation.class, rt, dome);
		} catch (DeviceOperationFailedException e) {
			throw e;
		} catch (ActionException e) {
			throw new DomeTeleoperationException(e.getAction());
		}
	}

	@Override
	public double getAzimuth(String rt, String dome)
			throws DeviceOperationFailedException, DomeTeleoperationException {
		try {
			return (Double) this.invokeGetOperation(GetAzimuthOperation.class,
					rt, dome);
		} catch (DeviceOperationFailedException e) {
			throw e;
		} catch (ActionException e) {
			throw new DomeTeleoperationException(e.getAction());
		}
	}

	@Override
	public DomeOpeningState getState(String rt, String dome)
			throws DeviceOperationFailedException, DomeTeleoperationException {
		try {
			return (DomeOpeningState) this.invokeGetOperation(
					GetStateOperation.class, rt, dome);
		} catch (DeviceOperationFailedException e) {
			throw e;
		} catch (ActionException e) {
			throw new DomeTeleoperationException(e.getAction());
		}
	}

	@Override
	public boolean isTrackingEnabled(String rt, String dome)
			throws DeviceOperationFailedException, DomeTeleoperationException {
		try {
			return (Boolean) this.invokeGetOperation(
					IsTrackingEnabledOperation.class, rt, dome);
		} catch (DeviceOperationFailedException e) {
			throw e;
		} catch (ActionException e) {
			throw new DomeTeleoperationException(e.getAction());
		}
	}

	@Override
	public void open(String rt, String dome)
			throws DeviceOperationFailedException, DomeTeleoperationException {
		try {
			this.invokeSetOperation(OpenOperation.class, rt, dome);
		} catch (DeviceOperationFailedException e) {
			throw e;
		} catch (ActionException e) {
			throw new DomeTeleoperationException(e.getAction());
		}
	}

	@Override
	public void park(String rt, String dome)
			throws DeviceOperationFailedException, DomeTeleoperationException {
		try {
			this.invokeSetOperation(ParkOperation.class, rt, dome);
		} catch (DeviceOperationFailedException e) {
			throw e;
		} catch (ActionException e) {
			throw new DomeTeleoperationException(e.getAction());
		}
	}

	@Override
	public void setTracking(String rt, String dome, boolean mode)
			throws DeviceOperationFailedException, DomeTeleoperationException {
		try {
			this.invokeSetOperation(SetTrackingOperation.class, rt, dome);
		} catch (DeviceOperationFailedException e) {
			throw e;
		} catch (ActionException e) {
			throw new DomeTeleoperationException(e.getAction());
		}
	}
}
