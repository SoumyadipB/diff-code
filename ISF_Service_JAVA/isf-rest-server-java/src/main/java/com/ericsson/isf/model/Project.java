package com.ericsson.isf.model;

import java.util.List;

public class Project {
	
	private Integer projectId;
	private List<Integer> woid;
	private List<Integer> wfid;
	private List<String> nodeName;
	private List<Integer> woplan;
	
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public List<Integer> getWoid() {
		return woid;
	}
	public void setWoid(List<Integer> woid) {
		this.woid = woid;
	}
	public List<Integer> getWfid() {
		return wfid;
	}
	public void setWfid(List<Integer> wfid) {
		this.wfid = wfid;
	}

	public List<String> getNodeName() {
		return nodeName;
	}
	public void setNodeName(List<String> nodeName) {
		this.nodeName = nodeName;
	}
	public List<Integer> getWoplan() {
		return woplan;
	}
	public void setWoplan(List<Integer> woplan) {
		this.woplan = woplan;
	}
	
	
	
	
}
