/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.sql.Time;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author eguphee
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class WorkOrderCompleteDetailsModel {
    
    private int wOID;
    private int wOPlanID;
    private int projectID;
    private String project;
    private String wOName;
    private String startDate;
    private Time startTime;
    
	private String oldWoName;

	private String oldStartDate;
      
	private String priority;
    private int domainID;
    private String domain;
    private int technologyID;
    private String technology;
    private int serviceAreaID;
    private String serviceArea;
    private int subActivityID;
    private String activity;
    private float effort;
    private String market;
    private String vendor;
    private String signumID;
    private String endDate;
    private String endTime;
    private String workFlowName;
    private String customerName;
	private float slaHrs;
    private String woNameAlias;
    private String status;
    private String externalGroup;
    private String lastModifiedBy;
    private List <WorkOrderNodesModel> listOfNode;
    private List<WOInputFileModel> listOfInputUrl;
	private int doID;
	private String assignedToSignum;
	private boolean inactivateWO;
	private String oldSignumID;
    
	public String getOldSignumID() {
		return oldSignumID;
	}

	public void setOldSignumID(String oldSignumID) {
		this.oldSignumID = oldSignumID;
	}

	public String getAssignedToSignum() {
		return assignedToSignum;
	}

	public void setAssignedToSignum(String assignedToSignum) {
		this.assignedToSignum = assignedToSignum;
	}

	public boolean isInactivateWO() {
		return inactivateWO;
	}

	public void setInactivateWO(boolean inactivateWO) {
		this.inactivateWO = inactivateWO;
	}

	private String externalSourceName;
	
	public String getOldWoName() {
		return oldWoName;
	}

	public void setOldWoName(String oldWoName) {
		this.oldWoName = oldWoName;
	}

	public String getOldStartDate() {
		return oldStartDate;
	}

	public void setOldStartDate(String oldStartDate) {
		this.oldStartDate = oldStartDate;
	}
  
	 public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

  
    public List<WOInputFileModel> getListOfInputUrl() {
		return listOfInputUrl;
	}

	public void setListOfInputUrl(List<WOInputFileModel> listOfInputUrl) {
		this.listOfInputUrl = listOfInputUrl;
	}

	public float getSlaHrs() {
		return slaHrs;
	}

	public void setSlaHrs(float slaHrs) {
		this.slaHrs = slaHrs;
	}

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
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
    
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExternalGroup() {
		return externalGroup;
	}

	public void setExternalGroup(String externalGroup) {
		if(externalGroup==null) {
			this.externalGroup="ISF";
		}
		else {
			this.externalGroup = externalGroup;
		}
	}


    public void setwOPlanID(int wOPlanID) {
        this.wOPlanID = wOPlanID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getwOName() {
        return wOName;
    }

    public void setwOName(String wOName) {
        this.wOName = wOName;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getDomainID() {
        return domainID;
    }

    public void setDomainID(int domainID) {
        this.domainID = domainID;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getTechnologyID() {
        return technologyID;
    }

    public void setTechnologyID(int technologyID) {
        this.technologyID = technologyID;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public int getServiceAreaID() {
        return serviceAreaID;
    }

    public void setServiceAreaID(int serviceAreaID) {
        this.serviceAreaID = serviceAreaID;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public int getSubActivityID() {
        return subActivityID;
    }

    public void setSubActivityID(int subActivityID) {
        this.subActivityID = subActivityID;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public float getEffort() {
        return effort;
    }

    public void setEffort(float effort) {
        this.effort = effort;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<WorkOrderNodesModel> getListOfNode() {
        return listOfNode;
    }

    public void setListOfNode(List<WorkOrderNodesModel> listOfNode) {
        this.listOfNode = listOfNode;
    }

    public String getSignumID() {
        return signumID;
    }

    public void setSignumID(String signumID) {
        this.signumID = signumID;
    }

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getWorkFlowName() {
		return workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getWoNameAlias() {
		return woNameAlias;
	}

	public void setWoNameAlias(String woNameAlias) {
		this.woNameAlias = woNameAlias;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	
	public int getDoID() {
		return doID;
	}

	public void setDoID(int doID) {
		this.doID = doID;
	}

	@Override
	public String toString() {
		return "WorkOrderCompleteDetailsModel [wOID=" + wOID + ", wOPlanID=" + wOPlanID + ", projectID=" + projectID
				+ ", project=" + project + ", wOName=" + wOName + ", startDate=" + startDate + ", startTime="
				+ startTime + ", priority=" + priority + ", domainID=" + domainID + ", domain=" + domain
				+ ", technologyID=" + technologyID + ", technology=" + technology + ", serviceAreaID=" + serviceAreaID
				+ ", serviceArea=" + serviceArea + ", subActivityID=" + subActivityID + ", activity=" + activity
				+ ", effort=" + effort + ", market=" + market + ", vendor=" + vendor + ", signumID=" + signumID
				+ ", endDate=" + endDate + ", workFlowName=" + workFlowName + ", customerName=" + customerName
				+ ", slaHrs=" + slaHrs + ", woNameAlias=" + woNameAlias + ", status=" + status + ", externalGroup="
				+ externalGroup + ", lastModifiedBy=" + lastModifiedBy + ", listOfNode=" + listOfNode + ", doID=" + doID
				+ "]";
	}
	
	public String getExternalSourceName() {
		return externalSourceName;
	}

	public void setExternalSourceName(String externalSourceName) {
		this.externalSourceName = externalSourceName;
	}
	
	
}