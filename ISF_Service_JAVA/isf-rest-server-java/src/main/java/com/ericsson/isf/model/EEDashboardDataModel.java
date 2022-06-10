package com.ericsson.isf.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EEDashboardDataModel {
	
	private int closedWOCounts;
	private boolean isOnLeave;
	
	
	public EEDashboardDataModel() {
		super();
	}
	public EEDashboardDataModel(int closedWOCounts) {
		super();
		this.closedWOCounts = closedWOCounts;
	}
	public boolean isOnLeave() {
		return isOnLeave;
	}
	public void setOnLeave(boolean isOnLeave) {
		this.isOnLeave = isOnLeave;
	}
	public int getClosedWOCounts() {
		return closedWOCounts;
	}
	public void setClosedWOCounts(int closedWOCounts) {
		this.closedWOCounts = closedWOCounts;
	}
	@JsonIgnore
	private Set<Integer> uniqueWorkOrders=new HashSet<>();
	
	public void addToUniqueWorkOrders(Integer woid){
		uniqueWorkOrders.add(woid);
	}
	public Set<Integer> getUniqueWorkOrders() {
		return uniqueWorkOrders;
	}

}
