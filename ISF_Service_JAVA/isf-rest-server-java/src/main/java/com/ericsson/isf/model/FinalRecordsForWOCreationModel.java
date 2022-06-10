package com.ericsson.isf.model;

import java.util.List;

public class FinalRecordsForWOCreationModel {

	private int woHistoryID;
	private List<CreateWorkOrderModel> createWorkOrderModels;

	public int getWoHistoryID() {
		return woHistoryID;
	}
	public void setWoHistoryID(int woHistoryID) {
		this.woHistoryID = woHistoryID;
	}
	public List<CreateWorkOrderModel> getCreateWorkOrderModels() {
		return createWorkOrderModels;
	}
	public void setCreateWorkOrderModels(List<CreateWorkOrderModel> createWorkOrderModels) {
		this.createWorkOrderModels = createWorkOrderModels;
	}
	
	
}
