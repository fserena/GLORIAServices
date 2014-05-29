/**
 * Author: Fernando Serena (fserena@ciclope.info)
 * Organization: Ciclope Group (UPM)
 * Project: GLORIA
 */
package eu.gloria.gs.services.experiment.operations;

import eu.gloria.gs.services.experiment.base.data.ExperimentDBAdapter;
import eu.gloria.gs.services.experiment.base.operations.ExperimentOperationException;
import eu.gloria.gs.services.experiment.base.operations.OperationContext;

/**
 * @author Fernando Serena (fserena@ciclope.info)
 *
 */
public abstract class ServiceOperation {

	private OperationContext context;
	private Object[] arguments;
	private ExperimentDBAdapter adapter;
	private String username;
	private String password;
	
	public ServiceOperation() {
		
	}	
	
	public ExperimentDBAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(ExperimentDBAdapter adapter) {
		this.adapter = adapter;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setContext(OperationContext context) {
		this.context = context;
	}
	
	public void setArguments(Object[] args) {
		this.arguments = args;
	}

	public OperationContext getContext() {
		return context;
	}

	public Object[] getArguments() {
		return arguments;
	}
	
	public abstract void execute() throws ExperimentOperationException;
}
