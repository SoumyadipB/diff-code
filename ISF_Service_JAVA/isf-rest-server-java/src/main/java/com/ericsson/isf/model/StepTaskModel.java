/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;

/**
 *
 * @author ekarath
 */
public class StepTaskModel {

    private String stepID;
    private String stepName;
    List<TaskModel> listOfTaskModel;

    public String getStepID() {
        return stepID;
    }

    public void setStepID(String stepID) {
        this.stepID = stepID;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public List<TaskModel> getListOfTaskModel() {
        return listOfTaskModel;
    }

    public void setListOfTaskModel(List<TaskModel> listOfTaskModel) {
        this.listOfTaskModel = listOfTaskModel;
    }
    
    
    
}
