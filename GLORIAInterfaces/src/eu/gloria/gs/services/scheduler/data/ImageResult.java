/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.scheduler.data;

import java.util.List;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public class ImageResult extends OPResult {

	private List<OPImageData> images;

	public List<OPImageData> getImages() {
		return images;
	}

	public void setImages(List<OPImageData> images) {
		this.images = images;
	}
	
	

	
}
