package com.ericsson.isf.model;

public class TaskDetailModel {
    
    int taskID;
    int subActivityID;
    String task;
    String executionType;
    boolean active;
    public int getTaskID() {
        return taskID;
    }
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
    public int getSubActivityID() {
        return subActivityID;
    }
    public void setSubActivityID(int subActivityID) {
        this.subActivityID = subActivityID;
    }
    public String getTask() {
        return task;
    }
    public void setTask(String task) {
        this.task = task;
    }
    public String getExecutionType() {
        return executionType;
    }
    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    


}
 
