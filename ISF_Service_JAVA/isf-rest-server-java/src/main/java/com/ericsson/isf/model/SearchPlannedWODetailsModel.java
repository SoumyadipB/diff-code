/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author ekumvsu
 */
public class SearchPlannedWODetailsModel {
	String woPlanId;
    int woID;
    String woName;
    String priority;
    String priorityModifiedOn;
    
    private int defID;
    private Boolean workOrderAutoSenseEnabled;
    
    public int getDefID() {
		return defID;
	}

	public void setDefID(int defID) {
		this.defID = defID;
	}

	public Boolean getWorkOrderAutoSenseEnabled() {
		return workOrderAutoSenseEnabled;
	}

	public void setWorkOrderAutoSenseEnabled(Boolean workOrderAutoSenseEnabled) {
		this.workOrderAutoSenseEnabled = workOrderAutoSenseEnabled;
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

	String status;
//    String startDate;
//    String startTime;
//    String endDate;
    Date plannedStartDate;
    String plannedStartTime;
    Date plannedEndDate;
    int subActivityID;
    String activity;
    String subActivity;
    int loe;
    String signumID;
    
    private int doid;
    private String deliverableUnitName;
    private String deliverableName;
    
    
    
    public int getDoid() {
		return doid;
	}

	public void setDoid(int doid) {
		this.doid = doid;
	}

	public String getDeliverableUnitName() {
		return deliverableUnitName;
	}

	public void setDeliverableUnitName(String deliverableUnitName) {
		this.deliverableUnitName = deliverableUnitName;
	}

	public String getDeliverableName() {
		return deliverableName;
	}

	public void setDeliverableName(String deliverableName) {
		this.deliverableName = deliverableName;
	}

	public String getWoPlanId() {
		return woPlanId;
	}

	public void setWoPlanId(String woPlanId) {
		this.woPlanId = woPlanId;
	}

	public String getSignumID() {
		return signumID;
	}

	public void setSignumID(String signumID) {
		this.signumID = signumID;
	}

	private List<SearchPlannedWOForTasksModel> listOfTaskDetails;
    
    private List<WorkOrderNodesModel> listOfNode;

    public List<WorkOrderNodesModel> getListOfNode() {
        return listOfNode;
    }

    public void setListOfNode(List<WorkOrderNodesModel> listOfNode) {
        this.listOfNode = listOfNode;
    }
    

    public List<SearchPlannedWOForTasksModel> getListOfTaskDetails() {
        return listOfTaskDetails;
    }

    public void setListOfTaskDetails(List<SearchPlannedWOForTasksModel> listOfTaskDetails) {
        this.listOfTaskDetails = listOfTaskDetails;
    }

    public int getSubActivityID() {
        return subActivityID;
    }

    public void setSubActivityID(int subActivityID) {
        this.subActivityID = subActivityID;
    }
    
    public int getWoID() {
        return woID;
    }

    public void setWoID(int woID) {
        this.woID = woID;
    }
    
    

    public String getWoName() {
        return woName;
    }

    public void setWoName(String woName) {
        this.woName = woName;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getPlannedStartTime() {
        return plannedStartTime;
    }

    public void setPlannedStartTime(String plannedStartTime) {
        this.plannedStartTime = plannedStartTime;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getSubActivity() {
        return subActivity;
    }

    public void setSubActivity(String subActivity) {
        this.subActivity = subActivity;
    }

    public int getLoe() {
        return loe;
    }

    public void setLoe(int loe) {
        this.loe = loe;
    }

	public String getPriorityModifiedOn() {
		return priorityModifiedOn;
	}

	public void setPriorityModifiedOn(String priorityModifiedOn) {
		this.priorityModifiedOn = priorityModifiedOn;
	}
    
    
    
}
