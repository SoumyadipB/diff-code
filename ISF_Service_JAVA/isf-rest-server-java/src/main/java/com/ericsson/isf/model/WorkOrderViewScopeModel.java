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
public class WorkOrderViewScopeModel {

    private String scopeID;
    private String scopeName;
    private List<WorkOrderViewActivityModel> listOfActivities;
    
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

    public List<WorkOrderViewActivityModel> getListOfActivities() {
        return listOfActivities;
    }

    public void setListOfActivities(List<WorkOrderViewActivityModel> listOfActivities) {
        this.listOfActivities = listOfActivities;
    }
    
}
