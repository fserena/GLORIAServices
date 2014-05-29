/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.repository.image;

import java.util.Date;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import eu.gloria.gs.services.repository.image.data.ImageInformation;
import eu.gloria.gs.services.repository.image.data.ImageTargetData;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
@WebService(name = "ImageRepositoryInterface", targetNamespace = "http://image.repository.services.gs.gloria.eu/")
public interface ImageRepositoryInterface {

	public void saveImage(@WebParam(name = "user") String user,
			@WebParam(name = "rt") String rt,
			@WebParam(name = "ccd") String ccd,
			@WebParam(name = "lid") String lid,
			@WebParam(name = "target") ImageTargetData target,
			@WebParam(name = "exposure") double exposure)
			throws ImageRepositoryException;

	public void setExperimentReservation(@WebParam(name = "id") int id,
			@WebParam(name = "rid") int rid) throws ImageRepositoryException;

	public void setJpg(@WebParam(name = "id") int id,
			@WebParam(name = "jpg") String jpg) throws ImageRepositoryException;

	public void setFits(@WebParam(name = "id") int id,
			@WebParam(name = "fits") String fits)
			throws ImageRepositoryException;

	public void setJpgByRTLocalId(@WebParam(name = "rt") String rt,
			@WebParam(name = "localid") String localid,
			@WebParam(name = "jpg") String jpg) throws ImageRepositoryException;

	public void setFitsByRTLocalId(@WebParam(name = "rt") String rt,
			@WebParam(name = "localid") String localid,
			@WebParam(name = "fits") String fits)
			throws ImageRepositoryException;

	public void setTargetByRTLocalId(@WebParam(name = "rt") String rt,
			@WebParam(name = "localid") String localid,
			@WebParam(name = "target") ImageTargetData target)
			throws ImageRepositoryException;

	public void setUser(@WebParam(name = "id") int id,
			@WebParam(name = "user") String user)
			throws ImageRepositoryException;

	public ImageInformation getImageInformation(@WebParam(name = "id") int id)
			throws ImageRepositoryException;

	public List<ImageInformation> getRandomImagesInformation(
			@WebParam(name = "count") int count)
			throws ImageRepositoryException;

	public List<ImageInformation> getRandomUserImagesInformation(
			@WebParam(name = "user") String user,
			@WebParam(name = "count") int count)
			throws ImageRepositoryException;

	public ImageInformation getImageInformationByRTLocaId(
			@WebParam(name = "rt") String rt, @WebParam(name = "lid") String lid)
			throws ImageRepositoryException;

	public List<Integer> getAllUserImageIdentifiers(
			@WebParam(name = "user") String user)
			throws ImageRepositoryException;

	public List<Integer> getAllExperimentImageIdentifiers(
			@WebParam(name = "experiment") String experiment)
			throws ImageRepositoryException;

	public List<ImageInformation> getAllReservationImages(
			@WebParam(name = "rid") int rid) throws ImageRepositoryException;

	public List<Integer> getAllImageIdentifiersByDate(
			@WebParam(name = "dateFrom") Date from,
			@WebParam(name = "dateTo") Date to, int limit)
			throws ImageRepositoryException;

	public List<Integer> getRandomImageIdentifiersByDate(
			@WebParam(name = "dateFrom") Date from,
			@WebParam(name = "dateTo") Date to, int limit)
			throws ImageRepositoryException;

	public List<Integer> getAllObjectImages(
			@WebParam(name = "object") String object)
			throws ImageRepositoryException;

	public List<Integer> getAllObjectImagesByDate(
			@WebParam(name = "object") String object,
			@WebParam(name = "dateFrom") Date from,
			@WebParam(name = "dateTo") Date to) throws ImageRepositoryException;
}
