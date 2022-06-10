package com.ericsson.isf.model;

public class WorkOrderNetworkElementModel {
	private int wOID;
	private String signum;
	public int getDoid() {
		return doid;
	}
	public void setDoid(int doid) {
		this.doid = doid;
	}
	private String tablename;
	private String networkElementType;
	private int doid;
	public String getNetworkElementType() {
		return networkElementType;
	}
	public void setNetworkElementType(String networkElementType) {
		this.networkElementType = networkElementType;
	}
	public int getwOID() {
		return wOID;
	}
	public void setwOID(int wOID) {
		this.wOID = wOID;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	public String getNeTextName() {
		return neTextName;
	}
	public void setNeTextName(String neTextName) {
		this.neTextName = neTextName;
	}
	private int count;
	
	private String neTextName;

}
