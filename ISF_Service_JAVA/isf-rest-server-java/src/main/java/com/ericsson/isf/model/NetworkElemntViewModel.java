package com.ericsson.isf.model;

public class NetworkElemntViewModel {
	private String name;
	private String networkElementType;
	private String networkSubElementType;
	private String market;
	private String domainSubDomain;
	private String technology;
	private String vendor;
	private String status;
	private int networkElementId;
	private String tableName;
	private boolean radioSelection;
	private int recordsTotal;
	
	//for selectAll
	private boolean selectMultiple;
	private int radioSelectionForSelectAll;
	private String listOfNetworkElementId;
	
	

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNetworkElementType() {
		return networkElementType;
	}

	public void setNetworkElementType(String networkElementType) {
		this.networkElementType = networkElementType;
	}

	
	public String getNetworkSubElementType() {
		return networkSubElementType;
	}

	public void setNetworkSubElementType(String networkSubElementType) {
		this.networkSubElementType = networkSubElementType;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public String getDomainSubDomain() {
		return domainSubDomain;
	}

	public void setDomainSubDomain(String domainSubDomain) {
		this.domainSubDomain = domainSubDomain;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getNetworkElementId() {
		return networkElementId;
	}

	public void setNetworkElementId(int networkElementId) {
		this.networkElementId = networkElementId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public boolean isRadioSelection() {
		return radioSelection;
	}

	public void setRadioSelection(boolean radioSelection) {
		this.radioSelection = radioSelection;
	}

	@Override
	public String toString() {
		return "NetworkElemntViewModel [name=" + name + ", networkElementType=" + networkElementType
				+ ", networkSubElementType=" + networkSubElementType + ", market=" + market + ", domainSubDomain="
				+ domainSubDomain + ", technology=" + technology + ", vendor=" + vendor + ", status=" + status
				+ ", networkElementId=" + networkElementId + ", tableName=" + tableName + ", radioSelection="
				+ radioSelection + "]";
	}

	public boolean isSelectMultiple() {
		return selectMultiple;
	}

	public void setSelectMultiple(boolean selectMultiple) {
		this.selectMultiple = selectMultiple;
	}

	public int getRadioSelectionForSelectAll() {
		return radioSelectionForSelectAll;
	}

	public void setRadioSelectionForSelectAll(int radioSelectionForSelectAll) {
		this.radioSelectionForSelectAll = radioSelectionForSelectAll;
	}

	public String getListOfNetworkElementId() {
		return listOfNetworkElementId;
	}

	public void setListOfNetworkElementId(String listOfNetworkElementId) {
		this.listOfNetworkElementId = listOfNetworkElementId;
	}

}
