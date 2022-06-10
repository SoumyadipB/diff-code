package com.ericsson.isf.model;

import java.util.List;

public class NetworkElementDataTable {
	private List<NetworkElemntViewModel> data;
	private int recordsTotal;
	private int recordsFiltered;
	private String draw;
	public List<NetworkElemntViewModel> getData() {
		return data;
	}
	public void setData(List<NetworkElemntViewModel> data) {
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
