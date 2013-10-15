package eu.gloria.gs.services.scheduler.op;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.gloria.gs.services.scheduler.local.SchManager;

public class OpCameraData {
	
	private Map<String, String> binnings;
	private String selectedBinning;
	
	public OpCameraData(){
		
		binnings = new HashMap<String, String>();
		SchManager manager = new SchManager();
		List<String> filterList = manager.getBinningList();
		if (filterList != null){
			for (String string : filterList) {
				binnings.put(string, string);
			}
		}
		
	}

	public String getSelectedBinning() {
		return selectedBinning;
	}

	public void setSelectedBinning(String selectedBinning) {
		this.selectedBinning = selectedBinning;
	}

	public Map<String, String> getBinnings() {
		return binnings;
	}

	public void setBinnings(Map<String, String> binnings) {
		this.binnings = binnings;
	}

}
