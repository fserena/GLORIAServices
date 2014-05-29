/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import eu.gloria.gs.services.repository.image.ImageRepositoryInterface;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public abstract class ImageOperation extends ServiceOperation {

	private ImageRepositoryInterface image;
	
	public ImageOperation() {
		
	}	
		
	public ImageRepositoryInterface getImageRepository() {
		return image;
	}

	public void setImageRepository(ImageRepositoryInterface image) {
		this.image = image;
	}	
}
