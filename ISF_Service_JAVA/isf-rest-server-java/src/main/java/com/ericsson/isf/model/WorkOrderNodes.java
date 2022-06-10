package com.ericsson.isf.model;

import java.util.List;


public class WorkOrderNodes {
	private int woID;
	private String nodeType;
    private List<Integer> nodeCount;
    private List<String> nodeName ;
    private List<String> market ;
	
	
	
	public int getWoID() {
		return woID;
	}
	
	public void setWoID(int woID) {
		this.woID = woID;
	}
	
	public String getNodeType() {
		return nodeType;
	}
	
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	
	public List<Integer> getNodeCount() {
		return nodeCount;
	}
	
	public void setNodeCount(List<Integer> nodeCount) {
		this.nodeCount = nodeCount;
	}
	public List<String> getNodeName() {
		return nodeName;
	}
	
	public void setNodeName(List<String> nodeName) {
		this.nodeName = nodeName;
	}
	
	public List<String> getMarket() {
		return market;
	}
	
	public void setMarket(List<String> market) {
		this.market = market;
	}
	
	/*
	 * public void addMarket(String market) { this.market.append(market+","); }
	 * 
	 * public void addNodeName(String nodeName) {
	 * this.nodeName.append(HtmlUtils.htmlEscape(nodeName)+',') ; }
	 */
}
