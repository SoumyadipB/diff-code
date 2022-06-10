package com.ericsson.isf.service;

import java.util.List;

import com.ericsson.isf.model.NetworkElementModel;

public class NetworkElements {
	
	private List<NetworkElementModel> data;
	private int recordsTotal;
	private int recordsFiltered;
	private String draw;
	
//	public List<NetworkElementModel> getNetworkElements() {
//		return networkElements;
//	}
//	public void setNetworkElements(List<NetworkElementModel> networkElements) {
//		this.networkElements = networkElements;
//	}
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
	public List<NetworkElementModel> getData() {
		return data;
	}
	public void setData(List<NetworkElementModel> data) {
		this.data = data;
	}
	public String getDraw() {
		return draw;
	}
	public void setDraw(String draw) {
		this.draw = draw;
	}
	
	
	
	
//	@Override
//	public String toString() {
//		 return "NetworkElements{networkElements["+networkElements+"],recordsTotal="+recordsTotal+",recordsFiltered="+recordsFiltered+"}";
//	}
	
	
	
}
