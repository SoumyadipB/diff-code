/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;


/**
 *
 * @author ekumvsu
 */
public class WorkFlowAvailabilitySearchModel {
    
    private String projectID;
    private String projectName;
    private String subActivityID;
    private String domain;
    private String subDomain;
    private String domainID;
    private String serviceArea;
    private String subServiceArea;
    private String technology;
    private String technologyID;
    private String activity;
    private String subActivity;
    private String wfAvailability;
    private int lstVersionNumber;
    private String experiencedMode;
    private Integer SubActivityFlowChartDefID;
    private String wfOwnerName;
    private String neNeededForExecution;
    private String slaHours;
 
    private String ftr;
    private String scopeID;
    private String scopeName;
    private String wfid;
    public String getScopeID() {
		return scopeID;
	}

	public void setScopeID(String scopeID) {
		this.scopeID = scopeID;
	}

	public String getScopeName() {
		return scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

	public Integer getSubActivityFlowChartDefID() {
		return SubActivityFlowChartDefID;
	}

	public void setSubActivityFlowChartDefID(Integer subActivityFlowChartDefID) {
		SubActivityFlowChartDefID = subActivityFlowChartDefID;
	}

	private String lstWFName;

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getSubActivityID() {
        return subActivityID;
    }

    public void setSubActivityID(String subActivityID) {
        this.subActivityID = subActivityID;
    }
   
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public String getSubServiceArea() {
        return subServiceArea;
    }

    public void setSubServiceArea(String subServiceArea) {
        this.subServiceArea = subServiceArea;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
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

    public String getWfAvailability() {
        return wfAvailability;
    }

    public void setWfAvailability(String wfAvailability) {
        this.wfAvailability = wfAvailability;
    }

 
    public void setLstVersionNumber(int lstVersionNumber) {
        this.lstVersionNumber = lstVersionNumber;
    }

    public void setLstWFName(String lstWFName) {
        this.lstWFName = lstWFName;
    }

    /**
     * @return the lstVersionNumber
     */
    public int getLstVersionNumber() {
        return lstVersionNumber;
    }

    /**
     * @return the lstWFName
     */
    public String getLstWFName() {
        return lstWFName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    public String getWfOwnerName() {
        return wfOwnerName;
    }

    public void setWfOwnerName(String wfOwnerName) {
        this.wfOwnerName = wfOwnerName;
    }

    public String getNeNeededForExecution() {
        return neNeededForExecution;
    }

    public String getExperiencedMode() {
		return experiencedMode;
	}

	public void setExperiencedMode(String experiencedMode) {
		this.experiencedMode = experiencedMode;
	}

	public void setNeNeededForExecution(String neNeededForExecution) {
        this.neNeededForExecution = neNeededForExecution;
    }

    public String getSlaHours() {
        return slaHours;
    }

    public void setSlaHours(String slaHours) {
        this.slaHours = slaHours;
    }

  

    public String getFtr() {
        return ftr;
    }

    public void setFtr(String ftr) {
        this.ftr = ftr;
    }

	public String getDomainID() {
		return domainID;
	}

	public void setDomainID(String domainID) {
		this.domainID = domainID;
	}

	public String getTechnologyID() {
		return technologyID;
	}

	public void setTechnologyID(String technologyID) {
		this.technologyID = technologyID;
	}

	public String getWfid() {
		return wfid;
	}

	public void setWfid(String wfid) {
		this.wfid = wfid;
	}
    
    
    
    
}
