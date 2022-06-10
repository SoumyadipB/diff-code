/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.isf.controller.WOManagementController;
import com.ericsson.isf.util.IsfCustomDateDeserializer;
import com.ericsson.isf.util.IsfCustomDateDeserializerForGantt;
import com.ericsson.isf.util.IsfCustomDateSerializer;
import com.ericsson.isf.util.IsfCustomDateSerializerForGantt;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.Gson;

/**
 *
 * @author edhhklu
 */
public class ExecutionPlanDetail {
	private static final Logger LOG = LoggerFactory.getLogger(ExecutionPlanDetail.class);
	
	private int executionPlanDetailId;
	private int executionPlanId;
	private long id;
	@JsonSerialize(using = IsfCustomDateSerializer.class)
    @JsonDeserialize(using = IsfCustomDateDeserializer.class)
	private Date start_date;
	private String text;
	private int duration;
	@JsonSerialize(using = IsfCustomDateSerializer.class)
    @JsonDeserialize(using = IsfCustomDateDeserializer.class)
	private Date end_date;
	private boolean $no_start;
    boolean $no_end;
    private String rendered_type;
    private long [] $source;
    private long [] $target;
    private int parent;
    private boolean $open;
    private int $level;
    private int $rendered_parent;
    private int $index;
    private int $wbs;
    private int workFlowVersionNo;
    private int subActivityID;
    private int scopeId;
    private String subActivityDetails;
    private int day;
    private int hour;
    private String workflow;
    private Integer activityScopeId;
    private boolean isActive;
    
    public Integer getActivityScopeId() {
		return activityScopeId;
	}

	public void setActivityScopeId(Integer activityScopeId) {
		this.activityScopeId = activityScopeId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	private void deepCopy(ExecutionPlanDetail from, ExecutionPlanDetail to){
    	to.id = from.id;
    	to.start_date = from.start_date;
    	to.text = from.text;
    	to.duration = from.duration;
    	to.end_date = from.end_date;
    	to.$no_start = from.$no_start;
    	to.$no_end = from.$no_end;
    	to.rendered_type = from.rendered_type;
    	to.$source = from.$source;
    	to.$target = from.$target;
    	to.parent = from.parent;
    	to.$open = from.$open;
    	to.$level = from.$level;
    	to.$rendered_parent = from.$rendered_parent;
    	to.$index = from.$index;
    	to.$wbs = from.$wbs;
    	to.subActivityDetails=from.subActivityDetails;
    	to.workFlowVersionNo = from.workFlowVersionNo;
    	to.subActivityID = from.subActivityID;
    	to.scopeId=from.scopeId;
    	to.hour=from.hour;
    	to.day=from.day;
    	to.workflow=from.workflow;
    }

	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getExecutionPlanId() {
		return executionPlanId;
	}
	public void setExecutionPlanId(int executionPlanId) {
		this.executionPlanId = executionPlanId;
	}
	public int getExecutionPlanDetailId() {
		return executionPlanDetailId;
	}
	public void setExecutionPlanDetailId(int executionPlanDetailId) {
		this.executionPlanDetailId = executionPlanDetailId;
	}
	public String getSubActivityDetails() {
		return subActivityDetails;
	}
	public void setSubActivityDetails(String subActivityDetails) {
		this.subActivityDetails = subActivityDetails;
	}
	public int getScopeId() {
		return scopeId;
	}
	public void setScopeId(int scopeId) {
		this.scopeId = scopeId;
	}



	public boolean isRoot() {
		if(this.get$target()!=null &&  this.get$target().length==0){
			return true;
		}else{
			return false;
		}
	}



	public int getWorkFlowVersionNo() {
		return workFlowVersionNo;
	}
	public void setWorkFlowVersionNo(int workFlowVersionNo) {
		this.workFlowVersionNo = workFlowVersionNo;
	}
	public int getSubActivityID() {
		return subActivityID;
	}
	public void setSubActivityID(int subActivityID) {
		this.subActivityID = subActivityID;
	}
	@JsonIgnore
	public String getTaskJson(){
		LOG.info("inside getTaskJSON");
    	Gson gson = new Gson();
        String json = gson.toJson(this);
    	return json;
    }
    
	@JsonIgnore
    public void setTaskJson(String stringJson){
		LOG.info("inside setTaskJSON");
		try {
			Gson gson = new Gson();
	    	ExecutionPlanDetail obj = gson.fromJson(stringJson, ExecutionPlanDetail.class);  
	    	deepCopy(obj, this);
		}
    	catch(Exception e) {
    		LOG.info("==============inside stringJson catch block==================:::::::::::::   "+e.getMessage());
    		LOG.error(e.getMessage());
    		e.printStackTrace();
    	}
    	
    	LOG.info("end setTaskJSON");
    }
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getStart_date() {
		return start_date;
	}
	
	
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public boolean is$no_start() {
		return $no_start;
	}
	public void set$no_start(boolean $no_start) {
		this.$no_start = $no_start;
	}
	public boolean is$no_end() {
		return $no_end;
	}
	public void set$no_end(boolean $no_end) {
		this.$no_end = $no_end;
	}
	public String getRendered_type() {
		return rendered_type;
	}
	public void setRendered_type(String rendered_type) {
		this.rendered_type = rendered_type;
	}
	public long[] get$source() {
		return $source;
	}
	public void set$source(long[] $source) {
		this.$source = $source;
	}
	public long[] get$target() {
		return $target;
	}
	public void set$target(long[] $target) {
		this.$target = $target;
	}
	public int getParent() {
		return parent;
	}
	public void setParent(int parent) {
		this.parent = parent;
	}
	public boolean is$open() {
		return $open;
	}
	public void set$open(boolean $open) {
		this.$open = $open;
	}
	public int get$level() {
		return $level;
	}
	public void set$level(int $level) {
		this.$level = $level;
	}
	public int get$rendered_parent() {
		return $rendered_parent;
	}
	public void set$rendered_parent(int $rendered_parent) {
		this.$rendered_parent = $rendered_parent;
	}
	public int get$index() {
		return $index;
	}
	public void set$index(int $index) {
		this.$index = $index;
	}
	public int get$wbs() {
		return $wbs;
	}
	public void set$wbs(int $wbs) {
		this.$wbs = $wbs;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}


	//this might create problem when used across different execution plans
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExecutionPlanDetail other = (ExecutionPlanDetail) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ExecutionPlanDetail [executionPlanDetailId=" + executionPlanDetailId + ", executionPlanId="
				+ executionPlanId + ", id=" + id + ", start_date=" + start_date + ", text=" + text + ", duration="
				+ duration + ", end_date=" + end_date + ", $no_start=" + $no_start + ", $no_end=" + $no_end
				+ ", rendered_type=" + rendered_type + ", $source=" + Arrays.toString($source) + ", $target="
				+ Arrays.toString($target) + ", parent=" + parent + ", $open=" + $open + ", $level=" + $level
				+ ", $rendered_parent=" + $rendered_parent + ", $index=" + $index + ", $wbs=" + $wbs
				+ ", workFlowVersionNo=" + workFlowVersionNo + ", subActivityID=" + subActivityID + ", scopeId="
				+ scopeId + ", subActivityDetails=" + subActivityDetails + ", day=" + day + ", hour=" + hour
				+ ", workflow=" + workflow + ", activityScopeId=" + activityScopeId + ", isActive=" + isActive + "]";
	}
	
	
	
}
