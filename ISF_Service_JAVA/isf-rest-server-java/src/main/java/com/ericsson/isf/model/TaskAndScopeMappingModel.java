/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;

/**
 *
 * @author enalpra
 */
public class TaskAndScopeMappingModel {
    private List<TaskModel> lstMasterTaskModels;
    private List<ScopeTaskMappingModel> lstScopeTaskMapping;

    public List<TaskModel> getLstMasterTaskModels() {
        return lstMasterTaskModels;
    }

    public void setLstMasterTaskModels(List<TaskModel> lstMasterTaskModels) {
        this.lstMasterTaskModels = lstMasterTaskModels;
    }
    
    public List<ScopeTaskMappingModel> getLstScopeTaskMapping() {
        return lstScopeTaskMapping;
    }

    public void setLstScopeTaskMapping(List<ScopeTaskMappingModel> lstScopeTaskMapping) {
        this.lstScopeTaskMapping = lstScopeTaskMapping;
    }

   
    @Override
    public String toString() {
        return "TaskAndScopeMappingModel{" + "lstTaskModels=" + lstMasterTaskModels + ", lstScopeTaskMapping=" + lstScopeTaskMapping + '}';
    }
    
    
}
