package com.ericsson.isf.model;

import java.util.List;

public class WorkOrderMarketModel {
	private int wOID;
	private List<String> markets;
	public int getwOID() {
		return wOID;
	}
	public void setwOID(int wOID) {
		this.wOID = wOID;
	}
	public List<String> getMarkets() {
		return markets;
	}
	public void setMarkets(List<String> markets) {
		this.markets = markets;
	}
}
