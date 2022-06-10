package com.ericsson.isf.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateWoResponse {
	
	private int woPlanId;
	private List<CreateWorkOrderModel2> WorkOrderID = new ArrayList<CreateWorkOrderModel2>();
	private String msg;

	public int getWoPlanId() {
		return woPlanId;
	}

	public void setWoPlanId(int woPlanId) {
		this.woPlanId = woPlanId;
	}

	@JsonProperty(value = "WorkOrderID")
	public List<CreateWorkOrderModel2> getWorkOrderID() {
		return WorkOrderID;
	}

	public void setWorkOrderID(List<CreateWorkOrderModel2> workOrderID) {
		this.WorkOrderID = workOrderID;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
