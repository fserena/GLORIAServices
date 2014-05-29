package eu.gloria.gs.services.experiment.operations;

import eu.gloria.gs.services.repository.image.ImageRepositoryInterface;

public class ImageOperationContextService extends OperationContextService {

	private ImageRepositoryInterface image;

	@Override
	protected void makeUpService(ServiceOperation service) {
		super.makeUpService(service);
		((ImageOperation) service).setImageRepository(image);
	}

	public ImageRepositoryInterface getImage() {
		return image;
	}

	public void setImage(ImageRepositoryInterface imageRepository) {
		this.image = imageRepository;
	}

}
