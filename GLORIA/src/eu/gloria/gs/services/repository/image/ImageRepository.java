package eu.gloria.gs.services.repository.image;

import java.util.Date;
import java.util.List;

import javax.jws.WebParam;

import eu.gloria.gs.services.core.GSLogProducerService;
import eu.gloria.gs.services.log.action.ActionLogException;
import eu.gloria.gs.services.repository.image.data.ImageDatabaseException;
import eu.gloria.gs.services.repository.image.data.ImageInformation;
import eu.gloria.gs.services.repository.image.data.ImageRepositoryAdapter;
import eu.gloria.gs.services.repository.image.data.ImageTargetData;

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
			@WebParam(name = "lid") String lid,
			@WebParam(name = "target") ImageTargetData target)
			throws ImageRepositoryException {

		try {
			this.adapter.saveImage(rt, ccd, user, new Date(), lid, target);

			try {
				this.logAction(this.getClientUsername(), "/images/new?" + user
						+ "&" + rt + "&" + ccd + "&" + lid);
			} catch (ActionLogException e) {
				e.printStackTrace();
			}

		} catch (ImageDatabaseException e) {
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

		} catch (ImageDatabaseException e) {
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

		} catch (ImageDatabaseException e) {
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
		} catch (ImageDatabaseException e) {
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
	public List<ImageInformation> getAllReservationImages(
			@WebParam(name = "rid") int rid) throws ImageRepositoryException {

		try {
			return this.adapter.getImagesByReservation(rid, 100);
		} catch (ImageDatabaseException e) {
			throw new ImageRepositoryException(e.getMessage());
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
			@WebParam(name = "dateTo") Date to) {

		return this.adapter.getAllImagesBetween(from, to, 100);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#
	 * setExperimentReservationByJpg(java.lang.String, int)
	 */
	@Override
	public void setExperimentReservationByJpg(
			@WebParam(name = "jpg") String jpg, @WebParam(name = "rid") int rid)
			throws ImageRepositoryException {
		try {
			this.adapter.setExperimentReservationByJpg(jpg, rid);
		} catch (ImageDatabaseException e) {
			throw new ImageRepositoryException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#
	 * setExperimentReservationByFits(java.lang.String, int)
	 */
	@Override
	public void setExperimentReservationByFits(
			@WebParam(name = "fits") String fits,
			@WebParam(name = "rid") int rid) throws ImageRepositoryException {
		try {
			this.adapter.setExperimentReservationByFits(fits, rid);
		} catch (ImageDatabaseException e) {
			throw new ImageRepositoryException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.repository.image.ImageRepositoryInterface#setUserByJpg
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void setUserByJpg(@WebParam(name = "jpg") String jpg,
			@WebParam(name = "user") String user)
			throws ImageRepositoryException {
		try {
			this.adapter.setUserByJpg(jpg, user);
		} catch (ImageDatabaseException e) {
			throw new ImageRepositoryException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.gloria.gs.services.repository.image.ImageRepositoryInterface#setUserByFits
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void setUserByFits(@WebParam(name = "fits") String fits,
			@WebParam(name = "user") String user)
			throws ImageRepositoryException {
		try {
			this.adapter.setUserByFits(fits, user);
		} catch (ImageDatabaseException e) {
			throw new ImageRepositoryException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#
	 * setTargetByRTLocalId(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setTargetByRTLocalId(@WebParam(name = "rt") String rt,
			@WebParam(name = "localid") String localid,
			@WebParam(name = "target") ImageTargetData target) throws ImageRepositoryException {
		try {
			this.adapter.setTargetByRT(rt, localid, target);
		} catch (ImageDatabaseException e) {
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
	public void setJpgByRTLocalId(@WebParam(name = "rt") String rt,
			@WebParam(name = "localid") String localid,
			@WebParam(name = "url") String url) throws ImageRepositoryException {
		try {
			this.adapter.setJpgByRT(rt, localid, url);
		} catch (ImageDatabaseException e) {
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
	public void setFitsByRTLocalId(@WebParam(name = "rt") String rt,
			@WebParam(name = "localid") String localid,
			@WebParam(name = "fits") String fits)
			throws ImageRepositoryException {
		try {
			this.adapter.setFitsByRT(rt, localid, fits);
		} catch (ImageDatabaseException e) {
			throw new ImageRepositoryException(e.getMessage());
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
		try {
			this.adapter.setJpg(id, jpg);

			try {
				this.logAction(this.getClientUsername(), "/images/" + id
						+ "/setJpg?" + jpg);
			} catch (ActionLogException e) {
				e.printStackTrace();
			}

		} catch (ImageDatabaseException e) {
			throw new ImageRepositoryException(e.getMessage());
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
		try {
			this.adapter.setFits(id, fits);

			try {
				this.logAction(this.getClientUsername(), "/images/" + id
						+ "/setFits?" + fits);
			} catch (ActionLogException e) {
				e.printStackTrace();
			}

		} catch (ImageDatabaseException e) {
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
		} catch (ImageDatabaseException e) {
			throw new ImageRepositoryException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#getAllObjectImages(java.lang.String)
	 */
	@Override
	public List<Integer> getAllObjectImages(
			@WebParam(name = "object") String object)
			throws ImageRepositoryException {
		return this.adapter.getAllObjectImages(object);
	}
	
	/* (non-Javadoc)
	 * @see eu.gloria.gs.services.repository.image.ImageRepositoryInterface#getAllObjectImagesByDate(java.lang.String)
	 */
	@Override
	public List<Integer> getAllObjectImagesByDate(
			@WebParam(name = "object") String object,
			@WebParam(name = "dateFrom") Date from,
			@WebParam(name = "dateTo") Date to)
			throws ImageRepositoryException {
		return this.adapter.getAllObjectImagesByDate(object, from, to);
	}

}
