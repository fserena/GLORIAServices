package eu.gloria.gs.services.repository.image.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.gloria.gs.services.log.action.LogAction;
import eu.gloria.gs.services.repository.image.data.dbservices.ImageDBService;
import eu.gloria.gs.services.repository.image.data.dbservices.ImageEntry;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class ImageRepositoryAdapter {

	private ImageDBService imageService;

	/**
	 * 
	 */
	public ImageRepositoryAdapter() {

	}

	/**
	 * 
	 */
	public void init() {

		imageService.create();
	}

	public void saveImage(String rt, String ccd, String user, Date when,
			String lid, ImageTargetData target, double exposure)
			throws ImageDatabaseException {

		ImageEntry entry = new ImageEntry();
		entry.setUser(user);
		entry.setDate(when);
		entry.setLocal_id(lid);
		entry.setRt(rt);
		entry.setCcd(ccd);
		entry.setExposure(exposure);

		try {
			entry.setTarget(JSONConverter.toJSON(target));
		} catch (IOException e) {
			throw new ImageDatabaseException();
		}

		imageService.save(entry);
	}

	public void removeImage(int id) throws ImageDatabaseException {
		if (!imageService.contains(id)) {
			LogAction action = new LogAction();

			action.put("id", id);
			action.put("cause", "image does not exist");
			throw new ImageDatabaseException(action);
		}

		imageService.remove(id);
	}

	public void setExperimentReservation(int id, int rid)
			throws ImageDatabaseException {
		if (!imageService.contains(id)) {
			LogAction action = new LogAction();
			action.put("id", id);
			action.put("cause", "image does not exist");
			throw new ImageDatabaseException(action);
		}

		imageService.setReservation(id, rid);
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

	public void setJpg(int id, String jpg) throws ImageDatabaseException {
		if (!imageService.contains(id)) {
			LogAction action = new LogAction();
			action.put("id", id);
			action.put("cause", "image does not exist");
			throw new ImageDatabaseException(action);
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
	}

	public void setFits(int id, String fits) throws ImageDatabaseException {
		if (!imageService.contains(id)) {
			LogAction action = new LogAction();
			action.put("id", id);
			action.put("cause", "image does not exist");
			throw new ImageDatabaseException(action);
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
	}

	public void setJpgByRT(String rt, String lid, String jpg)
			throws ImageDatabaseException {
		if (!imageService.containsRTLocalId(rt, lid)) {
			LogAction action = new LogAction();
			action.put("rt", rt);
			action.put("lid", lid);
			action.put("cause", "image does not exist");
			throw new ImageDatabaseException(action);
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
	}

	public void setFitsByRT(String rt, String lid, String fits)
			throws ImageDatabaseException {
		if (!imageService.containsRTLocalId(rt, lid)) {
			LogAction action = new LogAction();
			action.put("rt", rt);
			action.put("lid", lid);
			action.put("cause", "image does not exist");
			throw new ImageDatabaseException(action);
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
			throws ImageDatabaseException {
		if (!imageService.containsRTLocalId(rt, lid)) {
			LogAction action = new LogAction();
			action.put("rt", rt);
			action.put("lid", lid);
			action.put("cause", "image does not exist");
			throw new ImageDatabaseException(action);
		}

		try {
			imageService.setTargetByRTLocalId(rt, lid,
					JSONConverter.toJSON(target));
		} catch (IOException e) {
			LogAction action = new LogAction();
			action.put("rt", rt);
			action.put("lid", lid);
			action.put("cause", "json error");
			throw new ImageDatabaseException(action);
		}
	}

	public void setUser(int id, String user) throws ImageDatabaseException {
		if (!imageService.contains(id)) {
			LogAction action = new LogAction();
			action.put("id", id);
			action.put("cause", "image does not exist");
			throw new ImageDatabaseException(action);
		}

		imageService.setUser(id, user);
	}

	private void decorateImageInfo(ImageInformation info, ImageEntry entry,
			LogAction action) throws ImageDatabaseException {
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
			action.put("cause", "json error");
			throw new ImageDatabaseException(action);
		}

	}

	public List<ImageInformation> getRandomImagesInformation(int count)
			throws ImageDatabaseException {

		LogAction action = new LogAction();
		action.put("cause", "no images available");

		List<ImageEntry> entries = imageService.getRandom(count);

		if (entries == null) {
			throw new ImageDatabaseException(action);
		}

		List<ImageInformation> imageInfos = new ArrayList<ImageInformation>();

		for (ImageEntry entry : entries) {
			ImageInformation imageInfo = new ImageInformation();

			this.decorateImageInfo(imageInfo, entry, action);

			// imageInfo.setUser(entry.getUser());

			imageInfos.add(imageInfo);

		}
		return imageInfos;
	}

	public List<ImageInformation> getRandomUserImagesInformation(String user,
			int count) throws ImageDatabaseException {

		LogAction action = new LogAction();
		action.put("cause", "no images available");

		List<ImageEntry> entries = imageService.getUserRandom(user, count);

		if (entries == null) {
			throw new ImageDatabaseException(action);
		}

		List<ImageInformation> imageInfos = new ArrayList<ImageInformation>();

		for (ImageEntry entry : entries) {
			ImageInformation imageInfo = new ImageInformation();

			this.decorateImageInfo(imageInfo, entry, action);

			imageInfos.add(imageInfo);

		}
		return imageInfos;
	}

	public ImageInformation getImageInformation(int id)
			throws ImageDatabaseException {
		if (!imageService.contains(id)) {
			LogAction action = new LogAction();
			action.put("id", id);
			action.put("cause", "image does not exist");
			throw new ImageDatabaseException(action);
		}

		ImageEntry entry = imageService.get(id);
		ImageInformation imageInfo = new ImageInformation();
		LogAction action = new LogAction();

		this.decorateImageInfo(imageInfo, entry, action);

		return imageInfo;
	}

	public ImageInformation getImageInformationByRTLocalId(String rt, String lid)
			throws ImageDatabaseException {

		LogAction action = new LogAction();

		if (!imageService.containsRTLocalId(rt, lid)) {
			action.put("rt", rt);
			action.put("lid", lid);
			action.put("cause", "image does not exist");
			throw new ImageDatabaseException(action);
		}

		ImageEntry entry = imageService.getByRTLocalId(rt, lid);

		ImageInformation imageInfo = new ImageInformation();

		this.decorateImageInfo(imageInfo, entry, action);

		return imageInfo;
	}

	public List<ImageInformation> getImagesByReservation(int rid, int limit)
			throws ImageDatabaseException {

		List<ImageInformation> imageInfos = new ArrayList<ImageInformation>();
		List<ImageEntry> entries = imageService.getByReservation(rid);
		LogAction action = new LogAction();

		if (entries != null) {
			for (ImageEntry entry : entries) {
				ImageInformation imageInfo = new ImageInformation();

				this.decorateImageInfo(imageInfo, entry, action);
				imageInfos.add(imageInfo);
			}
		}

		return imageInfos;
	}

	public List<ImageInformation> getAllWithoutUrl(int limit)
			throws ImageDatabaseException {

		List<ImageInformation> imageInfos = new ArrayList<ImageInformation>();
		List<ImageEntry> entries = imageService.getAllWithoutUrl();

		for (ImageEntry entry : entries) {
			ImageInformation imageInfo = new ImageInformation();

			imageInfo.setId(entry.getIdimage());
			imageInfo.setCreationDate(entry.getDate());
			imageInfo.setLocalid(entry.getLocal_id());
			imageInfo.setRid(entry.getRid());
			imageInfo.setRt(entry.getRt());
			imageInfo.setExposure(entry.getExposure());
			imageInfo.setCcd(entry.getCcd());
			try {
				imageInfo.setTarget((ImageTargetData) JSONConverter.fromJSON(
						entry.getTarget(), ImageTargetData.class, null));
			} catch (IOException e) {
				LogAction action = new LogAction();
				action.put("entry", entry.getIdimage());
				action.put("cause", "json error");
				throw new ImageDatabaseException(action);
			}
			imageInfos.add(imageInfo);
		}

		return imageInfos;
	}

	public List<Integer> getAllImagesBetween(Date from, Date to, int limit) {

		List<Integer> imageIds = imageService.getAllBetweenDates(from, to);

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
