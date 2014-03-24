package eu.gloria.gs.services.teleoperation.scam;

import eu.gloria.gs.services.teleoperation.base.AbstractTeleoperation;
import eu.gloria.gs.services.teleoperation.base.DeviceOperationFailedException;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.scam.operations.GetBrightnessOperation;
import eu.gloria.gs.services.teleoperation.scam.operations.GetContrastOperation;
import eu.gloria.gs.services.teleoperation.scam.operations.GetExposureTimeOperation;
import eu.gloria.gs.services.teleoperation.scam.operations.GetGainOperation;
import eu.gloria.gs.services.teleoperation.scam.operations.GetImageURLOperation;
import eu.gloria.gs.services.teleoperation.scam.operations.GetStateOperation;
import eu.gloria.gs.services.teleoperation.scam.operations.SetBrightnessOperation;
import eu.gloria.gs.services.teleoperation.scam.operations.SetContrastOperation;
import eu.gloria.gs.services.teleoperation.scam.operations.SetExposureTimeOperation;
import eu.gloria.gs.services.teleoperation.scam.operations.SetGainOperation;

public class SCamTeleoperation extends AbstractTeleoperation implements
		SCamTeleoperationInterface {

	public SCamTeleoperation() {
		super(SCamTeleoperation.class.getSimpleName());
	}

	@Override
	public long getBrightness(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		try {
			return (Long) this.invokeGetOperation(GetBrightnessOperation.class,
					rt, scam);
		} catch (TeleoperationException e) {
			throw new SCamTeleoperationException(e.getAction());
		}
	}

	@Override
	public long getContrast(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		try {
			return (Long) this.invokeGetOperation(GetContrastOperation.class,
					rt, scam);
		} catch (TeleoperationException e) {
			throw new SCamTeleoperationException(e.getAction());
		}
	}

	@Override
	public double getExposureTime(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		try {
			return (Double) this.invokeGetOperation(
					GetExposureTimeOperation.class, rt, scam);
		} catch (TeleoperationException e) {
			throw new SCamTeleoperationException(e.getAction());
		}
	}

	@Override
	public long getGain(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		try {
			return (Long) this.invokeGetOperation(GetGainOperation.class, rt,
					scam);
		} catch (TeleoperationException e) {
			throw new SCamTeleoperationException(e.getAction());
		}
	}

	@Override
	public String getImageURL(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		try {
			return (String) this.invokeGetOperation(GetImageURLOperation.class,
					rt, scam);
		} catch (TeleoperationException e) {
			throw new SCamTeleoperationException(e.getAction());
		}
	}

	@Override
	public SCamState getState(String rt, String scam)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		try {
			return (SCamState) this.invokeGetOperation(GetStateOperation.class,
					rt, scam);
		} catch (TeleoperationException e) {
			throw new SCamTeleoperationException(e.getAction());
		}
	}

	@Override
	public void setBrightness(String rt, String scam, long value)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		try {
			this.invokeSetOperation(SetBrightnessOperation.class, rt, scam, value);
		} catch (TeleoperationException e) {
			throw new SCamTeleoperationException(e.getAction());
		}
	}

	@Override
	public void setContrast(String rt, String scam, long value)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		try {
			this.invokeSetOperation(SetContrastOperation.class, rt, scam, value);
		} catch (TeleoperationException e) {
			throw new SCamTeleoperationException(e.getAction());
		}		
	}

	@Override
	public void setExposureTime(String rt, String scam, double value)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		try {
			this.invokeSetOperation(SetExposureTimeOperation.class, rt, scam, value);
		} catch (TeleoperationException e) {
			throw new SCamTeleoperationException(e.getAction());
		}
	}

	@Override
	public void setGain(String rt, String scam, long value)
			throws DeviceOperationFailedException, SCamTeleoperationException {
		try {
			this.invokeSetOperation(SetGainOperation.class, rt, scam, value);
		} catch (TeleoperationException e) {
			throw new SCamTeleoperationException(e.getAction());
		}		
	}
}
