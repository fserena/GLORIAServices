package eu.gloria.gs.services.repository.image;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebParam;

import eu.gloria.gs.services.core.GSLogProducerService;
import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.repository.image.data.ImageDatabaseException;
import eu.gloria.gs.services.repository.image.data.ImageInformation;
import eu.gloria.gs.services.repository.image.data.ImageRepositoryAdapter;
import eu.gloria.gs.services.repository.image.data.ImageTargetData;

public class ImageRepository extends GSLogProducerService implements
		ImageRepositoryInterface {

	private ImageRepositoryAdapter adapter;

	public ImageRepository() {
		this.createLogger(ImageRepository.class);
	}

	public void setAdapter(ImageRepositoryAdapter adapter) {
		this.adapter = adapter;
		this.adapter.init();
	}

	@Override
	public void saveImage(@WebParam(name = "user") String user,
			@WebParam(name = "rt") String rt,
			@WebParam(name = "ccd") String ccd,
			@WebParam(name = "lid") String lid,
			@WebParam(name = "target") ImageTargetData target,
			@WebParam(name = "exposure") double exposure)
			throws ImageRepositoryException {

		LogAction action = new LogAction();

		try {

			action.put("sender", this.getUsername());
			action.put("operation", "save image");
			action.put("rt", rt);

			this.adapter.saveImage(rt, ccd, user, new Date(), lid, target,
					exposure);

			this.logInfo(this.getClientUsername(), action);

		} catch (ImageDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(this.getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ImageRepositoryException(action);
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
		LogAction action = new LogAction();

		try {

			action.put("sender", this.getUsername());
			action.put("operation", "set rid");

			this.adapter.setExperimentReservation(id, rid);
			this.logInfo(this.getClientUsername(), action);

		} catch (ImageDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(this.getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ImageRepositoryException(action);
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
		LogAction action = new LogAction();

		try {

			action.put("sender", this.getUsername());
			action.put("operation", "set user");

			this.adapter.setUser(id, user);

			this.logInfo(this.getClientUsername(), action);
		} catch (ImageDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(this.getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ImageRepositoryException(action);
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
		} catch (ImageDatabaseException e) {
			throw new ImageRepositoryException(e.getAction());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#
	 * getImageInformation(int)
	 */
	@Override
	public List<ImageInformation> getRandomImagesInformation(int count)
			throws ImageRepositoryException {
		try {
			return this.adapter.getRandomImagesInformation(count);
		} catch (ImageDatabaseException e) {
			throw new ImageRepositoryException(e.getAction());
		}
	}

	@Override
	public List<ImageInformation> getRandomUserImagesInformation(String user,
			int count) throws ImageRepositoryException {
		try {
			return this.adapter.getRandomUserImagesInformation(user, count);
		} catch (ImageDatabaseException e) {
			throw new ImageRepositoryException(e.getAction());
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
	public List<ImageInformation> getAllReservationImages(
			@WebParam(name = "rid") int rid) throws ImageRepositoryException {

		try {
			List<ImageInformation> images = this.adapter
					.getImagesByReservation(rid, 100);

			return images;
		} catch (ImageDatabaseException e) {
			throw new ImageRepositoryException(e.getAction());
		}
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
			@WebParam(name = "dateTo") Date to, int limit) {

		return this.adapter.getAllImagesBetween(from, to, limit);
	}

	@Override
	public List<Integer> getRandomImageIdentifiersByDate(
			@WebParam(name = "dateFrom") Date from,
			@WebParam(name = "dateTo") Date to, int limit) {

		return this.adapter.getRandomImagesBetween(from, to, limit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#
	 * setTargetByRTLocalId(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void setTargetByRTLocalId(@WebParam(name = "rt") String rt,
			@WebParam(name = "localid") String localid,
			@WebParam(name = "target") ImageTargetData target)
			throws ImageRepositoryException {
		try {
			this.adapter.setTargetByRT(rt, localid, target);
		} catch (ImageDatabaseException e) {
			throw new ImageRepositoryException(e.getAction());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#
	 * setUrlByRTLocalId(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setJpgByRTLocalId(@WebParam(name = "rt") String rt,
			@WebParam(name = "localid") String localid,
			@WebParam(name = "url") String url) throws ImageRepositoryException {
		try {
			this.adapter.setJpgByRT(rt, localid, url);
		} catch (ImageDatabaseException e) {
			throw new ImageRepositoryException(e.getAction());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#
	 * setUrlByRTLocalId(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setFitsByRTLocalId(@WebParam(name = "rt") String rt,
			@WebParam(name = "localid") String localid,
			@WebParam(name = "fits") String fits)
			throws ImageRepositoryException {
		try {
			this.adapter.setFitsByRT(rt, localid, fits);
		} catch (ImageDatabaseException e) {
			throw new ImageRepositoryException(e.getAction());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.repository.image.ImageRepositoryInterface#setJpg
	 * (int, java.lang.String)
	 */
	@Override
	public void setJpg(@WebParam(name = "id") int id,
			@WebParam(name = "jpg") String jpg) throws ImageRepositoryException {

		LogAction action = new LogAction();

		try {

			action.put("sender", this.getUsername());
			action.put("operation", "set jpg");

			this.adapter.setJpg(id, jpg);

			this.logInfo(this.getClientUsername(), action);

		} catch (ImageDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(this.getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ImageRepositoryException(action);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.repository.image.ImageRepositoryInterface#setFits
	 * (int, java.lang.String)
	 */
	@Override
	public void setFits(@WebParam(name = "id") int id,
			@WebParam(name = "fits") String fits)
			throws ImageRepositoryException {
		LogAction action = new LogAction();

		try {

			action.put("sender", this.getUsername());
			action.put("operation", "set fits");

			this.adapter.setFits(id, fits);
			this.logInfo(this.getClientUsername(), action);

		} catch (ImageDatabaseException e) {
			action.put("cause", "internal error");
			this.logError(this.getClientUsername(), action);
			action.put("more", e.getAction());
			throw new ImageRepositoryException(action);
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
		} catch (ImageDatabaseException e) {
			throw new ImageRepositoryException(e.getAction());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#
	 * getAllObjectImages(java.lang.String)
	 */
	@Override
	public List<Integer> getAllObjectImages(
			@WebParam(name = "object") String object)
			throws ImageRepositoryException {
		return this.adapter.getAllObjectImages(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#
	 * getAllObjectImagesByDate(java.lang.String)
	 */
	@Override
	public List<Integer> getAllObjectImagesByDate(
			@WebParam(name = "object") String object,
			@WebParam(name = "dateFrom") Date from,
			@WebParam(name = "dateTo") Date to) throws ImageRepositoryException {
		return this.adapter.getAllObjectImagesByDate(object, from, to);
	}

}
