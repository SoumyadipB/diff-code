/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

/**
 *
 * @author ekarath
 */
public class ScopeAndMasterTaskModel {
    private TaskModel masterTaskModels;
    private ScopeTaskMappingModel scopeTaskMapping;

    public TaskModel getMasterTaskModels() {
        return masterTaskModels;
    }

    public void setMasterTaskModels(TaskModel masterTaskModels) {
        this.masterTaskModels = masterTaskModels;
    }

    public ScopeTaskMappingModel getScopeTaskMapping() {
        return scopeTaskMapping;
    }

    public void setScopeTaskMapping(ScopeTaskMappingModel scopeTaskMapping) {
        this.scopeTaskMapping = scopeTaskMapping;
    }

    @Override
    public String toString() {
        return "ScopeAndMasterTaskModel{" + "masterTaskModels=" + masterTaskModels + ", scopeTaskMapping=" + scopeTaskMapping + '}';
    }

    
    
    
}
