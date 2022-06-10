package com.ericsson.isf.model;

public class EngineerDetailsModel {
	private int plannedAssignedWOCount;
	private int inprogressWOCount;
	private double manualHours;
	private double digitalHours;	
	private double projectAdhocHours;
	private double internalAdhocHours;
	private double leaveHours;
	private boolean isOnLeave;
	
	public int getPlannedAssignedWOCount() {
		return plannedAssignedWOCount;
	}
	public void setPlannedAssignedWOCount(int plannedAssignedWOCount) {
		this.plannedAssignedWOCount = plannedAssignedWOCount;
	}
	public void addToPlannedAssignedWOCount(int plannedAssignedWOCount) {
		this.plannedAssignedWOCount += plannedAssignedWOCount;
	}
	public int getInprogressWOCount() {
		return inprogressWOCount;
	}
	public void setInprogressWOCount(int inprogressWOCount) {
		this.inprogressWOCount = inprogressWOCount;
	}
	public void addToInprogressWOCount(int inprogressWOCount) {
		this.inprogressWOCount += inprogressWOCount;
	}
	
	public double getManualHours() {
		return manualHours;
	}
	public void setManualHours(double manualHours) {
		this.manualHours = manualHours;
	}
	public void addToManualHours(double manualHours) {
		this.manualHours += manualHours;
	}
	
	public double getDigitalHours() {
		return digitalHours;
	}
	public void setDigitalHours(double digitalHours) {
		this.digitalHours = digitalHours;
	}
	public void addToDigitalHours(double digitalHours) {
		this.digitalHours += digitalHours;
	}
	
	public double getProjectAdhocHours() {
		return projectAdhocHours;
	}
	public void setProjectAdhocHours(double projectAdhocHours) {
		this.projectAdhocHours = projectAdhocHours;
	}
	public void addToProjectAdhocHours(double projectAdhocHours) {
		this.projectAdhocHours += projectAdhocHours;
	}
	
	
	public double getInternalAdhocHours() {
		return internalAdhocHours;
	}
	public void setInternalAdhocHours(double internalAdhocHours) {
		this.internalAdhocHours = internalAdhocHours;
	}
	public void addToInternalAdhocHours(double internalAdhocHours) {
		this.internalAdhocHours += internalAdhocHours;
	}

	public double getLeaveHours() {
		return leaveHours;
	}
	public void setLeaveHours(double leaveHours) {
		this.leaveHours = leaveHours;
	}
	public boolean isOnLeave() {
		return isOnLeave;
	}
	public void setOnLeave(boolean isOnLeave) {
		this.isOnLeave = isOnLeave;
	}

}
