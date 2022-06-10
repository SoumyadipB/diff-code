package com.ericsson.isf.model;

import java.util.List;

public class DOWOModel {
	private int doid;
	private int woid;
	private int wfid;
	private String wfName;
	private int wfVersion;
	private String status;
	private String signum;
	private List<WorkOrderNodesModel> listOfNode;
	private String wfInfo;
	private int woplanid;
	public String getWfInfo() {
		return wfInfo;
	}
	public void setWfInfo(String wfInfo) {
		this.wfInfo = wfInfo;
	}
	public int getWoplanid() {
		return woplanid;
	}
	public void setWoplanid(int woplanid) {
		this.woplanid = woplanid;
	}
	public int getDoid() {
		return doid;
	}
	public void setDoid(int doid) {
		this.doid = doid;
	}
	public int getWoid() {
		return woid;
	}
	public void setWoid(int woid) {
		this.woid = woid;
	}
	public int getWfid() {
		return wfid;
	}
	public void setWfid(int wfid) {
		this.wfid = wfid;
	}
	public String getWfName() {
		return wfName;
	}
	public void setWfName(String wfName) {
		this.wfName = wfName;
	}
	public int getWfVersion() {
		return wfVersion;
	}
	public void setWfVersion(int wfVersion) {
		this.wfVersion = wfVersion;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}
	public List<WorkOrderNodesModel> getListOfNode() {
		return listOfNode;
	}
	public void setListOfNode(List<WorkOrderNodesModel> listOfNode) {
		this.listOfNode = listOfNode;
	}
	

}
