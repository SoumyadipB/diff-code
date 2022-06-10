/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;

import org.springframework.web.util.HtmlUtils;

/**
 *
 * @author ekarath
 */
public class WorkOrderProgressModel {
    private int woID;
    private String woName;
    private int projectID;
    private int subActivityID;
    private String status;
    private String priority;
    private String workFlowName;
    private String digitialHours;
    private String manualHours;
    private String totalHours;
    private String plannedstartDate;
    private String plannedclosedOn;
    private String actualstartDate;
    private String actualclosedOn;
    private String nodeType;
    private int nodeCount=0;
//    private StringBuilder nodeName = new StringBuilder();
//    private StringBuilder market = new StringBuilder();
//    private String market;
//    private String nodeNames;
    private String woPriority;
    private String flowChartDefID;
    private int versionNumber;
    private boolean neRequired;
    private String flowChartType;
    private double completedPercentage;
    private String progressDescription;
    private int wfid;
    
    private List<String> nodeName;
    private List<String> market;
    
    private String wFOwnerName;
//    private double inProgressPrecentage;
    
    private ProficiencyTypeModal proficiencyType;

	private int doid;
	
	 public boolean isQualified() {
		return qualified;
	}

	public void setQualified(boolean qualified) {
		this.qualified = qualified;
	}

	private boolean qualified;
    

	public String getwFOwnerName() {
		return wFOwnerName;
	}

	public void setwFOwnerName(String wFOwnerName) {
		this.wFOwnerName = wFOwnerName;
	}

	private String deliverablePlanName;
    private String deliverableUnitName;
    private int parentWOID;
    private int woPlanID;
    private boolean workOrderAutoSenseEnabled;
    private List<String> nodeNames;
    
    
    public boolean isWorkOrderAutoSenseEnabled() {
		return workOrderAutoSenseEnabled;
	}

	public void setWorkOrderAutoSenseEnabled(boolean workOrderAutoSenseEnabled) {
		this.workOrderAutoSenseEnabled = workOrderAutoSenseEnabled;
	}

	public int getWoPlanID() {
		return woPlanID;
	}

	public void setWoPlanID(int woPlanID) {
		this.woPlanID = woPlanID;
	}

	public int getParentWOID() {
		return parentWOID;
	}

	public void setParentWOID(int parentWOID) {
		this.parentWOID = parentWOID;
	}

//	public StringBuilder getMarket() {
//		return market;
//	}
//
//	public void setMarket(String market) {
//		this.market =new StringBuilder(market);
//	}
//
//	public void addMarket(String market) {
//		this.market.append(market+",");
//	}
	
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

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getSubActivityID() {
        return subActivityID;
    }

    public void setSubActivityID(int subActivityID) {
        this.subActivityID = subActivityID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getWorkFlowName() {
        return workFlowName;
    }

    public void setWorkFlowName(String workFlowName) {
        this.workFlowName = workFlowName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(int nodeCount) {
        this.nodeCount += nodeCount;
    }

    public String getDigitialHours() {
        return formatHoursToHHMMSS(digitialHours);
    }

    public void setDigitialHours(String digitialHours) {
        this.digitialHours = digitialHours;
    }

    public String getManualHours() {
    	return formatHoursToHHMMSS(manualHours);
    }
    
     private String formatHoursToHHMMSS(String hour){
    	if (hour==null){
    		return "0";
    	}
    	int totalSeconds=(int) (Double.parseDouble(hour)*60*60);
    	int hours=totalSeconds / (60*60);
    	int mins= (totalSeconds % (60*60)) / 60;
    	int seconds =((totalSeconds % (60)));
    	return String.format("%d:%d:%d", hours,mins,seconds);
    }

    public void setManualHours(String manualHours) {
        this.manualHours = manualHours;
    }

    public String getTotalHours() {
        return formatHoursToHHMMSS(totalHours);
    }

    public void setTotalHours(String totalHours) {
        this.totalHours = totalHours;
    }

    public String getPlannedstartDate() {
        return plannedstartDate;
    }

    public void setPlannedstartDate(String plannedstartDate) {
        this.plannedstartDate = plannedstartDate;
    }

    public String getPlannedclosedOn() {
        return plannedclosedOn;
    }

    public void setPlannedclosedOn(String plannedclosedOn) {
        this.plannedclosedOn = plannedclosedOn;
    }

    public String getActualstartDate() {
        return actualstartDate;
    }

    public void setActualstartDate(String actualstartDate) {
        this.actualstartDate = actualstartDate;
    }

    public String getActualclosedOn() {
        return actualclosedOn;
    }

    public void setActualclosedOn(String actualclosedOn) {
        this.actualclosedOn = actualclosedOn;
    }


//    public StringBuilder getNodeName() {
//        return nodeName;
//    }
//
//    public void setNodeName(String nodeName) {
//        this.nodeName=new StringBuilder(nodeName);
//    }
//
//    public void addNodeName(String nodeName) {
//        this.nodeName.append(HtmlUtils.htmlEscape(nodeName)+',') ;
//    }
    public String getFlowChartDefID() {
        return flowChartDefID;
    }

    public void setFlowChartDefID(String flowChartDefID) {
        this.flowChartDefID = flowChartDefID;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public boolean isNeRequired() {
        return neRequired;
    }

    public void setNeRequired(boolean neRequired) {
        this.neRequired = neRequired;
    }

    public String getFlowChartType() {
        return flowChartType;
    }

    public void setFlowChartType(String flowChartType) {
        this.flowChartType = flowChartType;
    }

	public double getCompletedPercentage() {
		return completedPercentage;
	}

	public void setCompletedPercentage(double completedPercentage) {
		this.completedPercentage = completedPercentage;
	}

	public String getProgressDescription() {
		return progressDescription;
	}

	public void setProgressDescription(String progressDescription) {
		this.progressDescription = progressDescription;
	}

	public String getWoPriority() {
		return woPriority;
	}

	public void setWoPriority(String woPriority) {
		this.woPriority = woPriority;
	}

	public int getWfid() {
		return wfid;
	}

	public void setWfid(int wfid) {
		this.wfid = wfid;
	}

	public int getDoid() {
		return doid;
	}

	public void setDoid(int doid) {
		this.doid = doid;
	}

	public String getDeliverablePlanName() {
		return deliverablePlanName;
	}

	public void setDeliverablePlanName(String deliverablePlanName) {
		this.deliverablePlanName = deliverablePlanName;
	}

	public String getDeliverableUnitName() {
		return deliverableUnitName;
	}

	public void setDeliverableUnitName(String deliverableUnitName) {
		this.deliverableUnitName = deliverableUnitName;
	}

	public List<String> getNodeNames() {
		return nodeNames;
	}

	public void setNodeNames(List<String> nodeNames) {
		this.nodeNames = nodeNames;
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

	public ProficiencyTypeModal getProficiencyType() {
		return proficiencyType;
	}

	public void setProficiencyType(ProficiencyTypeModal proficiencyType) {
		this.proficiencyType = proficiencyType;
	}  
}
