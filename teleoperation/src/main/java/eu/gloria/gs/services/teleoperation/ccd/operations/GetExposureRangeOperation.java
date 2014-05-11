package eu.gloria.gs.services.teleoperation.ccd.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.DeviceOp;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.Range;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.rti.client.devices.CCD;

@DeviceOp(name = "get exposure range")
public class GetExposureRangeOperation extends CCDOperation {

	private String object;
	
	public GetExposureRangeOperation(OperationArgs args) throws Exception {
		super(args);
		
		if (args.getArguments().size() > 2)
			this.object = (String) args.getArguments().get(2);
	}

	@Override
	protected void operateCCD(CCD ccd, OperationReturn returns)
			throws TeleoperationException {
		
		Range range = ccd.getExposureRange(object);
		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(range);

	}
}
