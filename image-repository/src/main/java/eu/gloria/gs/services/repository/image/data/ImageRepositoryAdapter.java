package eu.gloria.gs.services.repository.image.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;

import eu.gloria.gs.services.log.action.ActionException;
import eu.gloria.gs.services.repository.image.data.dbservices.ImageDBService;
import eu.gloria.gs.services.repository.image.data.dbservices.ImageEntry;
import eu.gloria.gs.services.utils.JSONConverter;
import eu.gloria.gs.services.utils.LoggerEntity;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class ImageRepositoryAdapter extends LoggerEntity {

	private ImageDBService imageService;

	/**
	 * 
	 */
	public ImageRepositoryAdapter() {
		super(ImageRepositoryAdapter.class.getSimpleName());
	}

	/**
	 * 
	 */
	public void init() {
		imageService.create();
	}

	public void saveImage(String rt, String ccd, String user, Date when,
			String lid, ImageTargetData target, double exposure)
			throws ActionException {

		try {
			ImageEntry entry = new ImageEntry();
			entry.setUser(user);
			entry.setDate(when);
			entry.setLocal_id(lid);
			entry.setRt(rt);
			entry.setCcd(ccd);
			entry.setExposure(exposure);
			entry.setTarget(JSONConverter.toJSON(target));
			imageService.save(entry);
		} catch (PersistenceException | IOException e) {
			throw new ActionException(e.getMessage());
		}

	}

	public void removeImage(int id) throws ActionException {
		try {
			if (!imageService.contains(id)) {
				throw new ActionException("image does not exist");
			}

			imageService.remove(id);
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public void setExperimentReservation(int id, int rid)
			throws ActionException {
		try {
			if (!imageService.contains(id)) {
				throw new ActionException("image does not exist");
			}

			imageService.setReservation(id, rid);
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	private String extractHost(String url) {
		String host = null;

		if (url != null) {
			String[] parts = url.split("\\?");

			if (parts.length == 2) {
				host = parts[0];
			}
		}

		return host;
	}

	private String extractFilename(String url) {
		String fileName = null;

		if (url != null) {
			String[] parts = url.split("fileName=");

			if (parts.length == 2) {
				fileName = parts[0];
			}
		}

		return fileName;
	}

	public void setJpg(int id, String jpg) throws ActionException {
		try {
			if (!imageService.contains(id)) {
				throw new ActionException("image does not exist");
			}

			String host = this.extractHost(jpg);

			if (host != null) {
				String fileName = this.extractFilename(jpg);

				if (fileName != null) {
					imageService.setJpg(id, host);
				} else {
					imageService.setJpg(id, host);
				}
			}
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public void setFits(int id, String fits) throws ActionException {
		try {
			if (!imageService.contains(id)) {
				throw new ActionException("image does not exist");
			}

			String host = this.extractHost(fits);

			if (host != null) {
				String fileName = this.extractFilename(fits);

				if (fileName != null) {
					imageService.setFits(id, host);
				} else {
					imageService.setFits(id, host);
				}
			}
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public void setJpgByRT(String rt, String lid, String jpg)
			throws ActionException {
		try {
			if (!imageService.containsRTLocalId(rt, lid)) {
				throw new ActionException("image does not exist");
			}

			String host = this.extractHost(jpg);

			if (host != null) {
				String fileName = this.extractFilename(jpg);

				if (fileName != null) {
					imageService.setJpgByRTLocalId(rt, lid, host);
				} else {
					imageService.setJpgByRTLocalId(rt, lid, host);
				}
			}
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public void setFitsByRT(String rt, String lid, String fits)
			throws ActionException {
		if (!imageService.containsRTLocalId(rt, lid)) {
			throw new ActionException("image does not exist");
		}

		String host = this.extractHost(fits);

		if (host != null) {
			String fileName = this.extractFilename(fits);

			if (fileName != null) {
				imageService.setFitsByRTLocalId(rt, lid, host);
			} else {
				imageService.setFitsByRTLocalId(rt, lid, host);
			}
		}
	}

	public void setTargetByRT(String rt, String lid, ImageTargetData target)
			throws ActionException {
		try {
			if (!imageService.containsRTLocalId(rt, lid)) {
				throw new ActionException("image does not exist");
			}

			imageService.setTargetByRTLocalId(rt, lid,
					JSONConverter.toJSON(target));
		} catch (PersistenceException | IOException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public void setUser(int id, String user) throws ActionException {
		try {
			if (!imageService.contains(id)) {
				throw new ActionException("image does not exist");
			}

			imageService.setUser(id, user);
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}

	}

	private List<ImageInformation> buildImageInfoList(List<ImageEntry> entries)
			throws ActionException {
		List<ImageInformation> imageInfos = new ArrayList<ImageInformation>();

		if (entries != null) {
			for (ImageEntry entry : entries) {
				ImageInformation imageInfo = new ImageInformation();
				this.decorateImageInfo(imageInfo, entry);
				imageInfos.add(imageInfo);
			}
		}

		return imageInfos;
	}

	private void decorateImageInfo(ImageInformation info, ImageEntry entry)
			throws ActionException {
		info.setCreationDate(entry.getDate());
		info.setId(entry.getIdimage());
		info.setLocalid(entry.getLocal_id());
		info.setRt(entry.getRt());
		info.setExposure(entry.getExposure());
		info.setCcd(entry.getCcd());
		info.setRid(entry.getRid());

		if (entry.getHost() != null) {

			if (entry.getJpg_gen() != null) {
				String jpg = entry.getHost();
				if (jpg.contains("ServletImage")) {
					jpg += "?fileName=" + entry.getLocal_id();
					jpg += ".jpg";
				} else if (jpg.contains("FileServlet")) {
					jpg += "?format=JPG&uuid=" + entry.getLocal_id();
				}

				info.setJpg(jpg);
			}

			if (entry.getFits_gen() != null) {
				String fits = entry.getHost();
				if (fits.contains("ServletImage")) {
					fits += "?fileName=" + entry.getLocal_id();
					fits += ".fits";
				} else if (fits.contains("FileServlet")) {
					fits += "?format=FITS&uuid=" + entry.getLocal_id();
				}

				info.setFits(fits);
			}
		}

		try {
			info.setTarget((ImageTargetData) JSONConverter.fromJSON(
					entry.getTarget(), ImageTargetData.class, null));
		} catch (IOException e) {
			throw new ActionException(e.getMessage());
		}

	}

	public List<ImageInformation> getRandomImagesInformation(int count)
			throws ActionException {
		try {
			List<ImageEntry> entries = imageService.getRandom(count);
			return this.buildImageInfoList(entries);
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public List<ImageInformation> getRandomUserImagesInformation(String user,
			int count) throws ActionException {
		try {
			List<ImageEntry> entries = imageService.getUserRandom(user, count);
			return this.buildImageInfoList(entries);
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public ImageInformation getImageInformation(int id) throws ActionException {
		try {
			if (!imageService.contains(id)) {
				throw new ActionException("image does not exist");
			}

			ImageEntry entry = imageService.get(id);
			ImageInformation imageInfo = new ImageInformation();
			this.decorateImageInfo(imageInfo, entry);

			return imageInfo;
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public ImageInformation getImageInformationByRTLocalId(String rt, String lid)
			throws ActionException {
		try {

			if (!imageService.containsRTLocalId(rt, lid)) {
				throw new ActionException("image does not exist");
			}

			ImageEntry entry = imageService.getByRTLocalId(rt, lid);

			ImageInformation imageInfo = new ImageInformation();
			this.decorateImageInfo(imageInfo, entry);

			return imageInfo;
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public List<ImageInformation> getImagesByReservation(int rid, int limit)
			throws ActionException {
		try {
			List<ImageEntry> entries = imageService.getByReservation(rid);
			return this.buildImageInfoList(entries);
		} catch (PersistenceException e) {
			throw new ActionException(e.getMessage());
		}
	}

	public List<ImageInformation> getAllWithoutUrl(int limit)
			throws ActionException {

		List<ImageEntry> entries = imageService.getAllWithoutUrl();

		return this.buildImageInfoList(entries);
	}

	public List<Integer> getAllImagesBetween(Date from, Date to, int limit) {

		List<Integer> imageIds = imageService.getAllBetweenDates(from, to,
				limit);

		if (imageIds == null) {
			imageIds = new ArrayList<>();
		}

		return imageIds;
	}

	public List<Integer> getRandomImagesBetween(Date from, Date to, int limit) {

		List<Integer> imageIds = imageService.getRandomBetweenDates(from, to,
				limit);

		if (imageIds == null) {
			imageIds = new ArrayList<>();
		}

		return imageIds;
	}

	public List<Integer> getAllObjectImages(String object) {

		List<Integer> imageIds = imageService.getAllObjectImages("%" + object
				+ "%");

		if (imageIds == null) {
			imageIds = new ArrayList<>();
		}

		return imageIds;
	}

	public List<Integer> getAllObjectImagesByDate(String object, Date from,
			Date to) {

		List<Integer> imageIds = imageService.getAllObjectImagesBetweenDates(
				"%" + object + "%", from, to);

		if (imageIds == null) {
			imageIds = new ArrayList<>();
		}

		return imageIds;
	}

	/**
	 * @param service
	 */
	public void setImageDBService(ImageDBService service) {
		this.imageService = service;
	}
}
