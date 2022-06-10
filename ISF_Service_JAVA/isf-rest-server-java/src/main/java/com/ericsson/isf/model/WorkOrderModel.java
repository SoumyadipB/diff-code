/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

import com.ericsson.isf.util.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 * @author eguphee
 */
public class WorkOrderModel {
    
    private int wOID;
    private int wOPlanID;
    private Date plannedStartDate;
    private Date plannedEndDate;
    private Date actualStartDate;
    private Date actualEndDate;
    private String signumID;
    private String status;
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    private String priority;
    private boolean active;
    private Date closedOn;
    private int adHocWoID;
    private String reason;
    private int wfVersion;
    private double loe ;
    private String WfVersionName;
    private int flowchartdefid;
    private int projectid;
    private int parentWorkOrderID;
    private String uiStatus;
    private String senderSignum;
    private int wfid;
    private int doID;
    private String wOName;
    private float slaHrs; 
    private int subActivityID;
    private String comment;
    private List<WorkOrderNodesModel> listOfNode;
    private List<WorkOrderBookingDetailsModel> listOfBookingDetails;
    private Boolean workOrderAutoSenseEnabled;
	/*private Date actualStartDateUTC;
    private Date actualEndDateUTC;
    */
   
    private Date actualStartDateUTC;
    
    private Date actualEndDateUTC;
    private Integer proficiencyID;
    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    public Date getActualStartDateUTC() {
		return actualStartDateUTC;
	}

	public void setActualStartDateUTC(Date actualStartDateUTC) {
		this.actualStartDateUTC = actualStartDateUTC;
	}

	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	public Date getActualEndDateUTC() {
		return actualEndDateUTC;
	}

	public void setActualEndDateUTC(Date actualEndDateUTC) {
		this.actualEndDateUTC = actualEndDateUTC;
	}

	public Boolean getWorkOrderAutoSenseEnabled() {
		return workOrderAutoSenseEnabled;
	}

	public void setWorkOrderAutoSenseEnabled(Boolean workOrderAutoSenseEnabled) {
		this.workOrderAutoSenseEnabled = workOrderAutoSenseEnabled;
	}

	public int getParentWorkOrderID() {
		return parentWorkOrderID;
	}

	public void setParentWorkOrderID(int parentWorkOrderID) {
		this.parentWorkOrderID = parentWorkOrderID;
	}

	public int getProjectid() {
		return projectid;
	}

	public void setProjectid(int projectid) {
		this.projectid = projectid;
	}

	public int getFlowchartdefid() {
		return flowchartdefid;
	}

	public void setFlowchartdefid(int flowchartdefid) {
		this.flowchartdefid = flowchartdefid;
	}

	public String getWfVersionName() {
        return WfVersionName;
    }

    public void setWfVersionName(String WfVersionName) {
        this.WfVersionName = WfVersionName;
    }

    public double getLoe() {
        return loe;
    }

    public void setLoe(double loe) {
        this.loe = loe;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    
     public int getAdHocWoID() {
        return adHocWoID;
    }

    public void setAdHocWoID(int adHocWoID) {
        this.adHocWoID = adHocWoID;
    }    
    
    public Date getClosedOn() {
        return closedOn;
    }

    public void setClosedOn(Date closedOn) {
        this.closedOn = closedOn;
    }

    public List<WorkOrderBookingDetailsModel> getListOfBookingDetails() {
        return listOfBookingDetails;
    }

    public void setListOfBookingDetails(List<WorkOrderBookingDetailsModel> listOfBookingDetails) {
        this.listOfBookingDetails = listOfBookingDetails;
    }
    
    public List<WorkOrderNodesModel> getListOfNode() {
        return listOfNode;
    }

    public void setListOfNode(List<WorkOrderNodesModel> listOfNode) {
        this.listOfNode = listOfNode;
    }

    public int getwOID() {
        return wOID;
    }

    public void setwOID(int wOID) {
        this.wOID = wOID;
    }

    public int getwOPlanID() {
        return wOPlanID;
    }

    public void setwOPlanID(int wOPlanID) {
        this.wOPlanID = wOPlanID;
    }

    public Date getPlannedStartDate() {
        return plannedStartDate;
    }

    public void setPlannedStartDate(Date plannedStartDate) {
        this.plannedStartDate = plannedStartDate;
    }

    public Date getPlannedEndDate() {
        return plannedEndDate;
    }

    public void setPlannedEndDate(Date plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }

    public Date getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public String getSignumID() {
        return signumID;
    }

    public void setSignumID(String signumID) {
        this.signumID = signumID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getWfVersion() {
        return wfVersion;
    }

    public void setWfVersion(int wfVersion) {
        this.wfVersion = wfVersion;
    }
    
    

	@Override
	public String toString() {
		return "WorkOrderModel [wOID=" + wOID + ", wOPlanID=" + wOPlanID + ", plannedStartDate=" + plannedStartDate
				+ ", plannedEndDate=" + plannedEndDate + ", actualStartDate=" + actualStartDate + ", actualEndDate="
				+ actualEndDate + ", signumID=" + signumID + ", status=" + status + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedDate="
				+ lastModifiedDate + ", priority=" + priority + ", active=" + active + ", closedOn=" + closedOn
				+ ", adHocWoID=" + adHocWoID + ", reason=" + reason + ", wfVersion=" + wfVersion + ", loe=" + loe
				+ ", WfVersionName=" + WfVersionName + ", flowchartdefid=" + flowchartdefid + ", projectid=" + projectid
				+ ", parentWorkOrderID=" + parentWorkOrderID + ", uiStatus=" + uiStatus + ", senderSignum="
				+ senderSignum + ", wfid=" + wfid + ", doID=" + doID + ", wOName=" + wOName + ", slaHrs=" + slaHrs
				+ ", subActivityID=" + subActivityID + ", comment=" + comment + ", listOfNode=" + listOfNode
				+ ", listOfBookingDetails=" + listOfBookingDetails + ", workOrderAutoSenseEnabled="
				+ workOrderAutoSenseEnabled + ", actualStartDateUTC=" + actualStartDateUTC + ", actualEndDateUTC="
				+ actualEndDateUTC + "]";
	}

	public String getUiStatus() {
		return uiStatus;
	}

	public void setUiStatus(String uiStatus) {
		this.uiStatus = uiStatus;
	}

	public String getSenderSignum() {
		return senderSignum;
	}

	public void setSenderSignum(String senderSignum) {
		this.senderSignum = senderSignum;
	}

	public int getWfid() {
		return wfid;
	}

	public void setWfid(int wfid) {
		this.wfid = wfid;
	}

	public String getwOName() {
		return wOName;
	}

	public void setwOName(String wOName) {
		this.wOName = wOName;
	}

	public float getSlaHrs() {
		return slaHrs;
	}

	public void setSlaHrs(float slaHrs) {
		this.slaHrs = slaHrs;
	}

	public int getSubActivityID() {
		return subActivityID;
	}

	public void setSubActivityID(int subActivityID) {
		this.subActivityID = subActivityID;
	}

	public int getDoID() {
		return doID;
	}

	public void setDoID(int doID) {
		this.doID = doID;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getProficiencyID() {
		return proficiencyID;
	}

	public void setProficiencyID(Integer proficiencyID) {
		this.proficiencyID = proficiencyID;
	}
	
	
	
}
