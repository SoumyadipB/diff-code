package com.ericsson.isf.model;

public class CreateSrResponse {
	private CreateWoResponse workOrderDetails;
	private int serviceRequestID;
	
	public int getServiceRequestID() {
		return serviceRequestID;
	}
	public void setServiceRequestID(int serviceRequestID) {
		this.serviceRequestID = serviceRequestID;
	}
	public CreateWoResponse getWorkOrderDetails() {
		return workOrderDetails;
	}
	public void setWorkOrderDetails(CreateWoResponse workOrderDetails) {
		this.workOrderDetails = workOrderDetails;
	}
}
