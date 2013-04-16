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

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
@WebService(name = "OnlineExperimentInterface", targetNamespace = "http://online.experiment.services.gs.gloria.eu/")
public interface ImageRepositoryInterface {

	public void saveImage(@WebParam(name = "user") String user,
			@WebParam(name = "rt") String rt,
			@WebParam(name = "ccd") String ccd,
			@WebParam(name = "lid") String lid) throws ImageRepositoryException;

	public void setExperimentReservation(@WebParam(name = "id") int id,
			@WebParam(name = "rid") int rid) throws ImageRepositoryException;

	public void setExperimentReservationByUrl(
			@WebParam(name = "url") String url, @WebParam(name = "rid") int rid)
			throws ImageRepositoryException;

	public void setUrl(@WebParam(name = "id") int id,
			@WebParam(name = "url") String url) throws ImageRepositoryException;

	public void setUrlByRTLocalId(@WebParam(name = "rt") String rt,
			@WebParam(name = "localid") String localid,
			@WebParam(name = "url") String url) throws ImageRepositoryException;

	public void setUser(@WebParam(name = "id") int id,
			@WebParam(name = "user") String user)
			throws ImageRepositoryException;

	public void setUserByUrl(@WebParam(name = "url") String url,
			@WebParam(name = "user") String user)
			throws ImageRepositoryException;

	public ImageInformation getImageInformation(@WebParam(name = "id") int id)
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

	public List<Integer> getAllReservationImageIdentifiers(
			@WebParam(name = "rid") String rid) throws ImageRepositoryException;

	public List<Integer> getAllImageIdentifiersByDate(
			@WebParam(name = "dateFrom") Date from,
			@WebParam(name = "dateTo") Date to) throws ImageRepositoryException;
}
