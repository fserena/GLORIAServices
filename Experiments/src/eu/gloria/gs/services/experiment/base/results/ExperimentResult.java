/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.base.results;

import java.io.IOException;
import java.util.Date;

import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.data.ExperimentDatabaseException;
import eu.gloria.gs.services.experiment.base.data.JSONConverter;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 * 
 */
public class ExperimentResult {

	private String user;
	private Date date;
	private Object value;
	private String tag;
	private int context;
	private ExperimentDBAdapter adapter;

	public ExperimentResult() {

	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getContext() {
		return context;
	}

	public void setContext(int context) {
		this.context = context;
	}

	public void save() throws ExperimentDatabaseException, IOException {
		this.adapter
				.saveResult(context, tag, user, JSONConverter.toJSON(value));
	}

	public void load() {

	}

	public ExperimentDBAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(ExperimentDBAdapter adapter) {
		this.adapter = adapter;
	}
}
