package eu.gloria.gs.services.repository.image;

import java.util.Date;
import java.util.List;

import javax.jws.WebParam;

import eu.gloria.gs.services.core.GSLogProducerService;
import eu.gloria.gs.services.log.action.ActionLogException;
import eu.gloria.gs.services.repository.image.data.ImageInformation;
import eu.gloria.gs.services.repository.image.data.ImageRepositoryAdapter;
import eu.gloria.gs.services.repository.image.data.dbservices.ImageRepositoryAdapterException;

public class ImageRepository extends GSLogProducerService implements
		ImageRepositoryInterface {

	private ImageRepositoryAdapter adapter;

	public ImageRepository() {
	}

	public void setAdapter(ImageRepositoryAdapter adapter) {
		this.adapter = adapter;
		this.adapter.init();
	}

	@Override
	public void saveImage(@WebParam(name = "user") String user,
			@WebParam(name = "rt") String rt,
			@WebParam(name = "ccd") String ccd,
			@WebParam(name = "lid") String lid) throws ImageRepositoryException {

		try {
			this.adapter.saveImage(rt, ccd, user, new Date(), lid);

			try {
				this.logAction(this.getClientUsername(), "/images/new?" + user
						+ "&" + rt + "&" + ccd + "&" + lid);
			} catch (ActionLogException e) {
				e.printStackTrace();
			}

		} catch (ImageRepositoryAdapterException e) {
			throw new ImageRepositoryException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#
	 * setExperimentReservation(int)
	 */
	@Override
	public void setExperimentReservation(@WebParam(name = "id") int id,
			@WebParam(name = "rid") int rid) throws ImageRepositoryException {
		try {
			this.adapter.setExperimentReservation(id, rid);

			try {
				this.logAction(this.getClientUsername(), "/images/" + id
						+ "/setReservation?" + rid);
			} catch (ActionLogException e) {
				e.printStackTrace();
			}

		} catch (ImageRepositoryAdapterException e) {
			throw new ImageRepositoryException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.repository.image.ImageRepositoryInterface#setUser
	 * (int, java.lang.String)
	 */
	@Override
	public void setUser(@WebParam(name = "id") int id,
			@WebParam(name = "user") String user)
			throws ImageRepositoryException {
		try {
			this.adapter.setUser(id, user);

			try {
				this.logAction(this.getClientUsername(), "/images/" + id
						+ "/setUser?" + user);
			} catch (ActionLogException e) {
				e.printStackTrace();
			}

		} catch (ImageRepositoryAdapterException e) {
			throw new ImageRepositoryException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#
	 * getImageInformation(int)
	 */
	@Override
	public ImageInformation getImageInformation(@WebParam(name = "id") int id)
			throws ImageRepositoryException {
		try {
			return this.adapter.getImageInformation(id);
		} catch (ImageRepositoryAdapterException e) {
			throw new ImageRepositoryException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#
	 * getAllUserImageIdentifiers(java.lang.String)
	 */
	@Override
	public List<Integer> getAllUserImageIdentifiers(
			@WebParam(name = "user") String user) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#
	 * getAllExperimentImageIdentifiers(java.lang.String)
	 */
	@Override
	public List<Integer> getAllExperimentImageIdentifiers(
			@WebParam(name = "experiment") String experiment) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#
	 * getAllReservationImageIdentifiers(java.lang.String)
	 */
	@Override
	public List<Integer> getAllReservationImageIdentifiers(
			@WebParam(name = "rid") String rid) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#
	 * getAllImageIdentifiersByDate(java.util.Date, java.util.Date)
	 */
	@Override
	public List<Integer> getAllImageIdentifiersByDate(
			@WebParam(name = "dateFrom") Date from,
			@WebParam(name = "dateTo") Date to) {
	
		System.out.println("Getting images by date ----------------------------->>>>>>");

		return this.adapter.getAllImagesBetween(from, to);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#
	 * setExperimentReservationByUrl(java.lang.String, int)
	 */
	@Override
	public void setExperimentReservationByUrl(
			@WebParam(name = "url") String url, @WebParam(name = "rid") int rid)
			throws ImageRepositoryException {
		try {
			this.adapter.setExperimentReservationByUrl(url, rid);
		} catch (ImageRepositoryAdapterException e) {
			throw new ImageRepositoryException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.repository.image.ImageRepositoryInterface#setUserByUrl
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void setUserByUrl(@WebParam(name = "url") String url,
			@WebParam(name = "user") String user)
			throws ImageRepositoryException {
		try {
			this.adapter.setUserByUrl(url, user);
		} catch (ImageRepositoryAdapterException e) {
			throw new ImageRepositoryException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#
	 * setUrlByRTLocalId(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setUrlByRTLocalId(@WebParam(name = "rt") String rt,
			@WebParam(name = "localid") String localid,
			@WebParam(name = "url") String url) throws ImageRepositoryException {
		try {
			this.adapter.setUrlByRT(rt, localid, url);
		} catch (ImageRepositoryAdapterException e) {
			throw new ImageRepositoryException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.repository.image.ImageRepositoryInterface#setUrl
	 * (int, java.lang.String)
	 */
	@Override
	public void setUrl(@WebParam(name = "id") int id,
			@WebParam(name = "url") String url) throws ImageRepositoryException {
		try {
			this.adapter.setUrl(id, url);

			try {
				this.logAction(this.getClientUsername(), "/images/" + id
						+ "/setUrl?" + url);
			} catch (ActionLogException e) {
				e.printStackTrace();
			}

		} catch (ImageRepositoryAdapterException e) {
			throw new ImageRepositoryException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#
	 * getImageInformationByRTLocaId(java.lang.String, java.lang.String)
	 */
	@Override
	public ImageInformation getImageInformationByRTLocaId(
			@WebParam(name = "rt") String rt, @WebParam(name = "lid") String lid)
			throws ImageRepositoryException {
		try {
			return this.adapter.getImageInformationByRTLocalId(rt, lid);
		} catch (ImageRepositoryAdapterException e) {
			throw new ImageRepositoryException(e.getMessage());
		}
	}

}
