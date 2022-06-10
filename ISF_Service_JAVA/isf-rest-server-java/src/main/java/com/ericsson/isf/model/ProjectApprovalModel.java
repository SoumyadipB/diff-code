package com.ericsson.isf.model;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ProjectApprovalModel {
	
	private int projectApprovalId;
	private String approverSignum;
	private int projectId;
	private Boolean approvedOrRejected;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date approvedOrRejectedOn;
	private String requestedBy;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date requestedOn;
	
	public int getProjectApprovalId() {
		return projectApprovalId;
	}
	public void setProjectApprovalId(int projectApprovalId) {
		this.projectApprovalId = projectApprovalId;
	}
	public String getApproverSignum() {
		return approverSignum;
	}
	public void setApproverSignum(String approverSignum) {
		this.approverSignum = approverSignum;
	}

	public int getProjectId() {
		return projectId;
	}
	public String getRequestedBy() {
		return requestedBy;
	}
	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}
	public Date getRequestedOn() {
		return requestedOn;
	}
	public void setRequestedOn(Date requestedOn) {
		this.requestedOn = requestedOn;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public Boolean isApprovedOrRejected() {
		return approvedOrRejected;
	}
	public void setApprovedOrRejected(Boolean approvedOrRejected) {
		if(approvedOrRejected==null){
			this.approvedOrRejected=null;
		}
		else
		this.approvedOrRejected = approvedOrRejected;
	}
	
	public Date getApprovedOrRejectedOn() {
		return approvedOrRejectedOn;
	}
	public void setApprovedOrRejectedOn(Date approvedOrRejectedOn) {
		this.approvedOrRejectedOn = approvedOrRejectedOn;
	}
	
	
	@Override
    public String toString() {
		
		return "ProjectApprovalModel{" +"projectApprovalId="+projectApprovalId+", approverSignum="+approverSignum+", projectId="+projectId+", approvedOrRejected="+approvedOrRejected+"}";
       // return "TaskModel{" + "task=" + task + ", executionType=" + executionType + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedDate=" + lastModifiedDate + '}';
    }
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private String status;
}
