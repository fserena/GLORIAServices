package eu.gloria.gs.services.teleoperation.ccd.operations;

import java.util.ArrayList;

import eu.gloria.gs.services.teleoperation.base.DeviceOp;
import eu.gloria.gs.services.teleoperation.base.OperationArgs;
import eu.gloria.gs.services.teleoperation.base.OperationReturn;
import eu.gloria.gs.services.teleoperation.base.TeleoperationException;
import eu.gloria.gs.services.teleoperation.ccd.ImageExtensionFormat;
import eu.gloria.rti.client.devices.CCD;

@DeviceOp(name = "get image url")
public class GetImageURLOperation extends CCDOperation {

	private String imageId;
	private ImageExtensionFormat format;

	public GetImageURLOperation(OperationArgs args) throws Exception {
		super(args);
		if (args.getArguments().size() > 3) {
			this.imageId = (String) args.getArguments().get(2);
			this.format = (ImageExtensionFormat) args.getArguments().get(3);
		}
	}

	@Override
	protected void operateCCD(CCD ccd, OperationReturn returns)
			throws TeleoperationException {
		String url = ccd.getImageURL(this.imageId, this.format);

		returns.setReturns(new ArrayList<Object>());
		returns.getReturns().add(url);

	}
}
