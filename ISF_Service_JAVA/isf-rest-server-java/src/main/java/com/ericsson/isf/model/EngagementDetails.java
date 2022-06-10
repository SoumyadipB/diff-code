package com.ericsson.isf.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EngagementDetails{
	    	 
	    	double totalHours;
	    	int totalNoOfWos;
	    	double plannedHours;
	    	double backlogHours;
	    	double availableHours;
	    	
	    	double plannedProjectHours;
	    	double plannedInternalHours;
	    	double adhocHours;
	    	
	    	double leaveHours;
	    	boolean isOnLeave;
	    	
	    	public double getLeaveHours() {
				return leaveHours;
			}

			public void setLeaveHours(double leaveHours) {
				this.leaveHours = leaveHours;
			}

			public boolean getIsOnLeave() {
				return isOnLeave;
			}

			public void setIsOnLeave(boolean isOnLeave) {
				this.isOnLeave = isOnLeave;
			}

			public void addToPlannedProjectHours(double hoursToAdd){
	    		plannedProjectHours=plannedProjectHours+hoursToAdd;
	    	}
	    	
	    	public double getPlannedProjectHours() {
				return plannedProjectHours;
			}

			public void setPlannedProjectHours(double plannedProjectHours) {
				this.plannedProjectHours = plannedProjectHours;
			}

			public double getPlannedInternalHours() {
				return plannedInternalHours;
			}

			public void setPlannedInternalHours(double plannedInternalHours) {
				this.plannedInternalHours = plannedInternalHours;
			}

			public double getAdhocHours() {
				return adhocHours;
			}

			public void setAdhocHours(double adhocHours) {
				this.adhocHours = adhocHours;
			}

			public void addToPlannedInternalHours(double hoursToAdd){
				plannedInternalHours=plannedInternalHours+hoursToAdd;
	    	}
			
			public void addToAdhocHours(double hoursToAdd){
				adhocHours=adhocHours+hoursToAdd;
	    	}
			
		
			@JsonIgnore
	    	private Set<Integer> uniqueWorkOrders=new HashSet<>();
	    	
	    	public void addToBacklogHours(double hoursToAdd){
	    		backlogHours=backlogHours+hoursToAdd;
	    	}
	    	
	    	public double getBacklogHours() {
				return backlogHours;
			}

			public void setBacklogHours(double backlogHours) {
				this.backlogHours = backlogHours;
			}

			public void addToAvailableHours(double hoursToAdd){
				availableHours=availableHours+hoursToAdd;
	    	}
			
			public double getAvailableHours() {
				return availableHours;
			}

			public void setAvailableHours(double availableHours) {
				this.availableHours = availableHours;
			}
			
			public void addToPlannedHours(double hoursToAdd){
				plannedHours=plannedHours+hoursToAdd;
	    	}
			
	    	
			public double getPlannedHours() {
				return plannedHours;
			}

			public void setPlannedHours(double plannedHours) {
				this.plannedHours = plannedHours;
			}

			public double getTotalHours() {
				return  plannedHours
		    	+ plannedProjectHours
		    	+ plannedInternalHours
		    	+ adhocHours
		    	+ backlogHours;
			}
			
			public void addToUniqueWorkOrders(Integer woid){
				uniqueWorkOrders.add(woid);
	    	}

			public int getTotalNoOfWos() {
				return uniqueWorkOrders.size();
			}

			public void setTotalNoOfWos(int totalNoOfWos) {
				this.totalNoOfWos = totalNoOfWos;
			}

			public Set<Integer> getUniqueWorkOrders() {
				return uniqueWorkOrders;
			}
			
	     }