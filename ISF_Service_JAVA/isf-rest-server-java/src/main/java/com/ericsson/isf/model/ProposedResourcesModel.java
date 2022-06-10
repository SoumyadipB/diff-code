/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;

/**
 *
 * @author eefhiio
 */
public class ProposedResourcesModel {
    
    private int resourcePositionID;
    private String signum;
    private String employeeName;
    private String startDate;
    private String endDate;
    private int availability;
    public String getDemandType() {
		return demandType;
	}

	public void setDemandType(String demandType) {
		this.demandType = demandType;
	}

	private String managerSignum;
    private String managerName;
    private String jobStage;
    private String domain;
    private String subDomain;
    private String serviceArea;
    private String subServiceArea;
    private String demandType;
    private List<String> technology;
    
    public List<String> getTechnology() {
		return technology;
	}

	public void setTechnology(List<String> technology) {
		this.technology = technology;
	}

	private int projectID;
    private String projectName;
    private int cRID;
    public String getRemote_Onsite() {
		return remote_Onsite;
	}

	public void setRemote_Onsite(String remote_Onsite) {
		this.remote_Onsite = remote_Onsite;
	}

	public int getWorkEffortID() {
		return workEffortID;
	}

	public void setWorkEffortID(int workEffortID) {
		this.workEffortID = workEffortID;
	}

	public String getWorkEffortStatus() {
		return workEffortStatus;
	}

	public void setWorkEffortStatus(String workEffortStatus) {
		this.workEffortStatus = workEffortStatus;
	}

	public String getPositionStatus() {
		return positionStatus;
	}

	public void setPositionStatus(String positionStatus) {
		this.positionStatus = positionStatus;
	}

	public double getHours() {
		return hours;
	}

	public void setHours(double hours) {
		this.hours = hours;
	}

	public double getFte() {
		return fte;
	}

	public void setFte(double fte) {
		this.fte = fte;
	}

	public String getPositionStartDate() {
		return positionStartDate;
	}

	public void setPositionStartDate(String positionStartDate) {
		this.positionStartDate = positionStartDate;
	}

	

	public String getPositionEndDate() {
		return positionEndDate;
	}

	public void setPositionEndDate(String positionEndDate) {
		this.positionEndDate = positionEndDate;
	}

	public int getResourceRequestID() {
		return resourceRequestID;
	}

	public void setResourceRequestID(int resourceRequestID) {
		this.resourceRequestID = resourceRequestID;
	}

	public String getCommentsByFm() {
		return commentsByFm;
	}

	public void setCommentsByFm(String commentsByFm) {
		this.commentsByFm = commentsByFm;
	}

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

	private Boolean isPendingCR;
    private String remote_Onsite;
    private int workEffortID;
    private String workEffortStatus;
    private String positionStatus;
    private double hours;
	private double fte;
	private String positionStartDate;
	private String positionEndDate;
	private int resourceRequestID;
	private String commentsByFm;
	 private Boolean isVisible;
	
    

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getcRID() {
        return cRID;
    }

    public void setcRID(int cRID) {
        this.cRID = cRID;
    }

    public Boolean getIsPendingCR() {
        return isPendingCR;
    }

    public void setIsPendingCR(Boolean isPendingCR) {
        this.isPendingCR = isPendingCR;
    }
    
    

    public int getResourcePositionID() {
        return resourcePositionID;
    }

    public void setResourcePositionID(int resourcePositionID) {
        this.resourcePositionID = resourcePositionID;
    }

    public String getSignum() {
        return signum;
    }

    public void setSignum(String signum) {
        this.signum = signum;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getManagerSignum() {
        return managerSignum;
    }

    public void setManagerSignum(String managerSignum) {
        this.managerSignum = managerSignum;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getJobStage() {
        return jobStage;
    }

    public void setJobStage(String jobStage) {
        this.jobStage = jobStage;
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

   
    
    
}
