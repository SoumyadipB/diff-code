package com.ericsson.isf.service;

import java.util.List;

import com.ericsson.isf.model.ProjectQueueWorkOrderBasicDetailsModel;
import com.ericsson.isf.model.SearchPlannedWOProjectModel;

/**@author edyudev
 * */
public class PlannedWOProject {
	
	private List<ProjectQueueWorkOrderBasicDetailsModel> data;
	private int recordsTotal;
	private int recordsFiltered;
	private String draw;
	
	
	public List<ProjectQueueWorkOrderBasicDetailsModel> getData() {
		return data;
	}
	public void setData(List<ProjectQueueWorkOrderBasicDetailsModel> data) {
		this.data = data;
	}
	public int getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public int getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	public String getDraw() {
		return draw;
	}
	public void setDraw(String draw) {
		this.draw = draw;
	}
	
	
	
	
}
