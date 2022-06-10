package com.ericsson.isf.model;

import java.util.List;

public class NodeFilterModel {

	private int projectID;
    private String elementType;
    private String type;
    private String vendor;
    private String market;
    private List<String> technologyIdList;
    private List<String> domainIdList;
    private String term;
    private String nodeNames;
    private int count;
    
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public List<String> getTechnologyIdList() {
		return technologyIdList;
	}
	public void setTechnologyIdList(List<String> technologyIdList) {
		this.technologyIdList = technologyIdList;
	}
	public List<String> getDomainIdList() {
		return domainIdList;
	}
	public void setDomainIdList(List<String> domainIdList) {
		this.domainIdList = domainIdList;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getNodeNames() {
		return nodeNames;
	}
	public void setNodeNames(String nodeNames) {
		this.nodeNames = nodeNames;
	}
    
}
