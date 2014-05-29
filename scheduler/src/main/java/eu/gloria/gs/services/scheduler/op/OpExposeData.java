package eu.gloria.gs.services.scheduler.op;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.gloria.gs.services.scheduler.local.SchManager;

public class OpExposeData {
	
	
	
	private String time;
	private String repeat;
	private boolean repetitionCount;
	private boolean selected;
	private Map<String, String> filters;
	private String selectedFilter;
	
	public OpExposeData(){
		/*filters = new HashMap<String, String>();
		filters.put("H_ALPHA", "H_ALPHA");
		filters.put("r", "r");*/
		
		filters = new HashMap<String, String>();
		SchManager manager = new SchManager();
		List<String> filterList = manager.getFilterList();
		if (filterList != null){
			for (String string : filterList) {
				filters.put(string, string);
			}
		}
	}
	
	public String toString(){
		return "ExposeData::[time=" + time + ", repeat=" + repeat + ", repetitionCount=" + repetitionCount + "]";
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public boolean isRepetitionCount() {
		return repetitionCount;
	}
	public void setRepetitionCount(boolean repetitionCount) {
		this.repetitionCount = repetitionCount;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Map<String, String> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, String> filters) {
		this.filters = filters;
	}

	public String getSelectedFilter() {
		return selectedFilter;
	}

	public void setSelectedFilter(String selectedFilter) {
		this.selectedFilter = selectedFilter;
	}

}
