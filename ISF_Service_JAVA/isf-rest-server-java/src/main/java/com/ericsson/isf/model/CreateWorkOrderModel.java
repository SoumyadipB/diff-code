/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;



/**
 *
 * @author ekarath
 */
@SuppressWarnings("serial")
public class CreateWorkOrderModel implements Serializable{
	
	
	
	private int executionPlanId;
    private int wOPlanID;
    private int workOrderPlanID;
    @NotNull
    private Integer projectID;
    private int scopeID;
    private int subActivityID;
    private String periodicityDaily;
    private String periodicityWeekly;
    private String startDate;
    private Time startTime;
    private String endDate;
    private String wOName;
    private List<String> lstSignumID;
    private Boolean active;
    private String createdBy;
    private String createdDate;
    private String lastModifiedBy;
    private String lastModifiedDate;
    private String priority;
    private String executionPlanName;
    private String assignedTo;
    private int wfVersion;
    private List<WorkOrderPlanNodesModel> listOfNode;
    private float slaHrs; 
    private String type;
    private int flowchartDefId;
    private String comment;
    private int woCount;
    private WorkOrderInputFileModel workOrderInputFileModel;
    private List<Time> periodicityHourly;
    private String nodeNames;
	private String nodeType;
	private int id;
	private int woHistoryID;
	private int woCreationID;
	private String workFlowName;
	private int subActivityFlowChartDefID;
	private int executionPlanDetailID;
	private String inputName;
	private String inputUrl;
	private boolean isNodeWise;
	private int doVolume;
	private Date plannedStartDate;
	private Date plannedEndDate;
	private String lstPeriodicityHourly;
	private Integer parentWOID;
    private String externalSourceName;
    private String activity;
    private String subActivity;
    private String status;
    private String uploadedBy;
    private Boolean workOrderAutoSenseEnabled;
    
    //Added parameter due to CNEDB Changes
    private int count;
    public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getNeTextName() {
		return neTextName;
	}

	public void setNeTextName(String neTextName) {
		this.neTextName = neTextName;
	}

	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	private String tableName;
    private String neTextName;
    private String elementType;
    

    public Boolean getWorkOrderAutoSenseEnabled() {
		return workOrderAutoSenseEnabled;
	}

	public void setWorkOrderAutoSenseEnabled(Boolean workOrderAutoSenseEnabled) {
		this.workOrderAutoSenseEnabled = workOrderAutoSenseEnabled;
	}

	public List<Time> getPeriodicityHourly() {
		return periodicityHourly;
	}

	public void setPeriodicityHourly(List<Time> periodicityHourly) {
		this.periodicityHourly = periodicityHourly;
	}

	public int getWoCount() {
		return woCount;
	}

	public void setWoCount(int woCount) {
		this.woCount = woCount;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getExecutionPlanId() {
		return executionPlanId;
	}

	public void setExecutionPlanId(int executionPlanId) {
		this.executionPlanId = executionPlanId;
	}

    public float getSlaHrs() {
        return slaHrs;
    }

    public void setSlaHrs(float slaHrs) {
        this.slaHrs = slaHrs;
    }
   
    public int getwOPlanID() {
        return wOPlanID;
    }

    public void setwOPlanID(int wOPlanID) {
        this.wOPlanID = wOPlanID;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public int getWorkOrderPlanID() {
		return workOrderPlanID;
	}

	public void setWorkOrderPlanID(int workOrderPlanID) {
		this.workOrderPlanID = workOrderPlanID;
	}

	public int getScopeID() {
        return scopeID;
    }

    public void setScopeID(int scopeID) {
        this.scopeID = scopeID;
    }

    public int getSubActivityID() {
        return subActivityID;
    }

    public void setSubActivityID(int subActivityID) {
        this.subActivityID = subActivityID;
    }

    public String getPeriodicityDaily() {
        return periodicityDaily;
    }

    public void setPeriodicityDaily(String periodicityDaily) {
        this.periodicityDaily = periodicityDaily;
    }

    public String getPeriodicityWeekly() {
        return periodicityWeekly;
    }

    public void setPeriodicityWeekly(String periodicityWeekly) {
        this.periodicityWeekly = periodicityWeekly;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getwOName() {
        return wOName;
    }

    public void setwOName(String wOName) {
        this.wOName = wOName;
    }

    public List<String> getLstSignumID() {
        return lstSignumID;
    }

    public void setLstSignumID(List<String> lstSignumID) {
        this.lstSignumID = lstSignumID;
    }


    public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public List<WorkOrderPlanNodesModel> getListOfNode() {
        return listOfNode;
    }

    public void setListOfNode(List<WorkOrderPlanNodesModel> listOfNode) {
        this.listOfNode = listOfNode;
    }

    public int getWfVersion() {
        return wfVersion;
    }

    public void setWfVersion(int wfVersion) {
        this.wfVersion = wfVersion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFlowchartDefId() {
		return flowchartDefId;
	}

	public void setFlowchartDefId(int flowchartDefId) {
		this.flowchartDefId = flowchartDefId;
	}

	/**
     * Copy constructor
     */
	public CreateWorkOrderModel(CreateWorkOrderModel sourceModel) {

		this.executionPlanId = sourceModel.executionPlanId;
		this.wOPlanID = sourceModel.wOPlanID;
		this.workOrderPlanID = sourceModel.workOrderPlanID;
		this.projectID = sourceModel.projectID;
		this.scopeID = sourceModel.scopeID;
		this.subActivityID = sourceModel.subActivityID;
		this.periodicityDaily = sourceModel.periodicityDaily;
		this.periodicityWeekly = sourceModel.periodicityWeekly;
		this.startDate = sourceModel.startDate;
		this.startTime = sourceModel.startTime;
		this.endDate = sourceModel.endDate;
		this.wOName = sourceModel.wOName;
		this.lstSignumID = sourceModel.lstSignumID;
		this.active = sourceModel.active;
		this.createdBy = sourceModel.createdBy;
		this.createdDate = sourceModel.createdDate;
		this.lastModifiedBy = sourceModel.lastModifiedBy;
		this.lastModifiedDate = sourceModel.lastModifiedDate;
		this.priority = sourceModel.priority;
		this.wfVersion = sourceModel.wfVersion;
		this.listOfNode = sourceModel.listOfNode;
		this.slaHrs = sourceModel.slaHrs;
		this.type = sourceModel.type;
		this.flowchartDefId = sourceModel.flowchartDefId;
		this.comment = sourceModel.comment;
		this.woCount = sourceModel.woCount;
		this.workOrderInputFileModel = sourceModel.workOrderInputFileModel;
		this.periodicityHourly = sourceModel.periodicityHourly;
		this.externalSourceName = sourceModel.externalSourceName;
		this.nodeNames = sourceModel.nodeNames;
		this.nodeType = sourceModel.nodeType;
		this.id = sourceModel.id;
		this.woHistoryID = sourceModel.woHistoryID;
		this.woCreationID = sourceModel.woCreationID;
		this.workFlowName = sourceModel.workFlowName;
		this.assignedTo = sourceModel.assignedTo;
		this.subActivityFlowChartDefID = sourceModel.subActivityFlowChartDefID;
		this.executionPlanDetailID = sourceModel.executionPlanDetailID;
		this.inputName = sourceModel.inputName;
		this.inputUrl = sourceModel.inputUrl;
		this.isNodeWise = sourceModel.isNodeWise;
		this.doVolume = sourceModel.doVolume;
		this.plannedStartDate = sourceModel.plannedStartDate;
		this.plannedEndDate = sourceModel.plannedEndDate;
		this.executionPlanName = sourceModel.executionPlanName;
		this.lstPeriodicityHourly=sourceModel.lstPeriodicityHourly;
		this.activity=sourceModel.activity;
		this.subActivity = sourceModel.getSubActivity();
		this.status=sourceModel.status;
		this.uploadedBy = sourceModel.uploadedBy;
	}

	public CreateWorkOrderModel() {
	}

	public WorkOrderInputFileModel getWorkOrderInputFileModel() {
		return workOrderInputFileModel;
	}

	public void setWorkOrderInputFileModel(WorkOrderInputFileModel workOrderInputFileModel) {
		this.workOrderInputFileModel = workOrderInputFileModel;
	}

	public String getExternalSourceName() {
		return externalSourceName;
	}

	public void setExternalSourceName(String externalSourceName) {
		this.externalSourceName = externalSourceName;
	}

	public String getNodeNames() {
		return nodeNames;
	}

	public void setNodeNames(String nodeNames) {
		this.nodeNames = nodeNames;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWoHistoryID() {
		return woHistoryID;
	}

	public void setWoHistoryID(int woHistoryID) {
		this.woHistoryID = woHistoryID;
	}

	public int getWoCreationID() {
		return woCreationID;
	}

	public void setWoCreationID(int woCreationID) {
		this.woCreationID = woCreationID;
	}

	public String getWorkFlowName() {
		return workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public int getSubActivityFlowChartDefID() {
		return subActivityFlowChartDefID;
	}

	public void setSubActivityFlowChartDefID(int subActivityFlowChartDefID) {
		this.subActivityFlowChartDefID = subActivityFlowChartDefID;
	}

	public int getExecutionPlanDetailID() {
		return executionPlanDetailID;
	}

	public void setExecutionPlanDetailID(int executionPlanDetailID) {
		this.executionPlanDetailID = executionPlanDetailID;
	}
	
	public String getInputName() {
		return inputName;
	}

	public void setInputName(String inputName) {
		this.inputName = inputName;
	}

	public String getInputUrl() {
		return inputUrl;
	}

	public void setInputUrl(String inputUrl) {
		this.inputUrl = inputUrl;
	}
	
	@JsonProperty(value="isNodeWise") 
	public boolean isNodeWise() {
		return isNodeWise;
	}

	public void setNodeWise(boolean isNodeWise) {
		this.isNodeWise = isNodeWise;
	}
	
	public int getDoVolume() {
		return doVolume==0?1:doVolume;
	}

	public void setDoVolume(int doVolume) {
		this.doVolume = doVolume;
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
	
	public String getExecutionPlanName() {
		return executionPlanName;
	}

	public void setExecutionPlanName(String executionPlanName) {
		this.executionPlanName = executionPlanName;
	}

	public String getLstPeriodicityHourly() {
		return lstPeriodicityHourly;
	}

	public void setLstPeriodicityHourly(String lstPeriodicityHourly) {
		this.lstPeriodicityHourly = lstPeriodicityHourly;
	}

	public Integer getParentWOID() {
		return parentWOID;
	}

	public void setParentWOID(Integer parentWOID) {
		this.parentWOID = parentWOID;
	}
	
	public String getSubActivity() {
		return subActivity;
	}

	public void setSubActivity(String subActivity) {
		this.subActivity = subActivity;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	@Override
	public String toString() {
		return "CreateWorkOrderModel [executionPlanId=" + executionPlanId + ", wOPlanID=" + wOPlanID
				+ ", workOrderPlanID=" + workOrderPlanID + ", projectID=" + projectID + ", scopeID=" + scopeID
				+ ", subActivityID=" + subActivityID + ", periodicityDaily=" + periodicityDaily + ", periodicityWeekly="
				+ periodicityWeekly + ", startDate=" + startDate + ", startTime=" + startTime + ", endDate=" + endDate
				+ ", wOName=" + wOName + ", lstSignumID=" + lstSignumID + ", active=" + active + ", createdBy="
				+ createdBy + ", createdDate=" + createdDate + ", lastModifiedBy=" + lastModifiedBy
				+ ", lastModifiedDate=" + lastModifiedDate + ", priority=" + priority + ", executionPlanName="
				+ executionPlanName + ", assignedTo=" + assignedTo + ", wfVersion=" + wfVersion + ", listOfNode="
				+ listOfNode + ", slaHrs=" + slaHrs + ", type=" + type + ", flowchartDefId=" + flowchartDefId
				+ ", comment=" + comment + ", woCount=" + woCount + ", workOrderInputFileModel="
				+ workOrderInputFileModel + ", periodicityHourly=" + periodicityHourly + ", nodeNames=" + nodeNames
				+ ", nodeType=" + nodeType + ", id=" + id + ", woHistoryID=" + woHistoryID + ", woCreationID="
				+ woCreationID + ", workFlowName=" + workFlowName + ", subActivityFlowChartDefID="
				+ subActivityFlowChartDefID + ", executionPlanDetailID=" + executionPlanDetailID + ", inputName="
				+ inputName + ", inputUrl=" + inputUrl + ", isNodeWise=" + isNodeWise + ", doVolume=" + doVolume
				+ ", plannedStartDate=" + plannedStartDate + ", plannedEndDate=" + plannedEndDate
				+ ", lstPeriodicityHourly=" + lstPeriodicityHourly + ", parentWOID=" + parentWOID
				+ ", externalSourceName=" + externalSourceName + ", activity=" + activity + ", subActivity="
				+ subActivity + ", status=" + status + ", uploadedBy=" + uploadedBy + "]";
	}

	public CreateWorkOrderModel(int executionPlanId, int wOPlanID, int workOrderPlanID, int projectID, int scopeID,
			int subActivityID, String periodicityDaily, String periodicityWeekly, String startDate, Time startTime,
			String endDate, String wOName, List<String> lstSignumID, Boolean active, String createdBy,
			String createdDate, String lastModifiedBy, String lastModifiedDate, String priority,
			String executionPlanName, String assignedTo, int wfVersion, List<WorkOrderPlanNodesModel> listOfNode,
			float slaHrs, String type, int flowchartDefId, String comment, int woCount,
			WorkOrderInputFileModel workOrderInputFileModel, List<Time> periodicityHourly, String nodeNames,
			String nodeType, int id, int woHistoryID, int woCreationID, String workFlowName,
			int subActivityFlowChartDefID, int executionPlanDetailID, String inputName, String inputUrl,
			boolean isNodeWise, int doVolume, Date plannedStartDate, Date plannedEndDate, String lstPeriodicityHourly,
			Integer parentWOID, String externalSourceName, String activity, String subActivity, String status,
			String uploadedBy) {
		super();
		this.executionPlanId = executionPlanId;
		this.wOPlanID = wOPlanID;
		this.workOrderPlanID = workOrderPlanID;
		this.projectID = projectID;
		this.scopeID = scopeID;
		this.subActivityID = subActivityID;
		this.periodicityDaily = periodicityDaily;
		this.periodicityWeekly = periodicityWeekly;
		this.startDate = startDate;
		this.startTime = startTime;
		this.endDate = endDate;
		this.wOName = wOName;
		this.lstSignumID = lstSignumID;
		this.active = active;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.priority = priority;
		this.executionPlanName = executionPlanName;
		this.assignedTo = assignedTo;
		this.wfVersion = wfVersion;
		this.listOfNode = listOfNode;
		this.slaHrs = slaHrs;
		this.type = type;
		this.flowchartDefId = flowchartDefId;
		this.comment = comment;
		this.woCount = woCount;
		this.workOrderInputFileModel = workOrderInputFileModel;
		this.periodicityHourly = periodicityHourly;
		this.nodeNames = nodeNames;
		this.nodeType = nodeType;
		this.id = id;
		this.woHistoryID = woHistoryID;
		this.woCreationID = woCreationID;
		this.workFlowName = workFlowName;
		this.subActivityFlowChartDefID = subActivityFlowChartDefID;
		this.executionPlanDetailID = executionPlanDetailID;
		this.inputName = inputName;
		this.inputUrl = inputUrl;
		this.isNodeWise = isNodeWise;
		this.doVolume = doVolume;
		this.plannedStartDate = plannedStartDate;
		this.plannedEndDate = plannedEndDate;
		this.lstPeriodicityHourly = lstPeriodicityHourly;
		this.parentWOID = parentWOID;
		this.externalSourceName = externalSourceName;
		this.activity = activity;
		this.subActivity = subActivity;
		this.status = status;
		this.uploadedBy = uploadedBy;
	}

}
