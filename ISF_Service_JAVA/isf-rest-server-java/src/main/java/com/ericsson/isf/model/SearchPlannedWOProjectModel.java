/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;

/**
 *
 * @author ekumvsu
 */
public class SearchPlannedWOProjectModel {
    
    private String projectID;
    private String projectName;
    private List<SearchPlannedWODetailsModel> listOfDetails;
    private int recordsTotal;
	
    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<SearchPlannedWODetailsModel> getListOfDetails() {
        return listOfDetails;
    }

    public void setListOfDetails(List<SearchPlannedWODetailsModel> listOfDetails) {
        this.listOfDetails = listOfDetails;
    }

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	

    
}
