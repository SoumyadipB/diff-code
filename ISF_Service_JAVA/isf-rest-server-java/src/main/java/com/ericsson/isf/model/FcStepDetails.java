/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

/**
 *
 * @author EDHHKLU
 */
	public class FcStepDetails {
		private int woFcStepDetailsId;
		private int woId;
		private int flowChartDefId;
		private String flowChartStepId;
		private int taskID;
		private String executionType;
		private int bookingID;
		private String status;
		private String decisionValue;
		private String signumId;
		private Date bookedOn;
		private String botPlatform;

		public int getWoFcStepDetailsId() {
			return woFcStepDetailsId;
		}
		public void setWoFcStepDetailsId(int woFcStepDetailsId) {
			this.woFcStepDetailsId = woFcStepDetailsId;
		}
		public int getWoId() {
			return woId;
		}
		public void setWoId(int woId) {
			this.woId = woId;
		}
		public int getFlowChartDefId() {
			return flowChartDefId;
		}
		public void setFlowChartDefId(int flowChartDefId) {
			this.flowChartDefId = flowChartDefId;
		}

		public String getFlowChartStepId() {
			return flowChartStepId;
		}
		public void setFlowChartStepId(String flowChartStepId) {
			this.flowChartStepId = flowChartStepId;
		}
		public int getTaskID() {
			return taskID;
		}
		public void setTaskID(int taskID) {
			this.taskID = taskID;
		}
		public String getExecutionType() {
			return executionType;
		}
		public void setExecutionType(String executionType) {
			this.executionType = executionType;
		}
		public int getBookingID() {
			return bookingID;
		}
		public void setBookingID(int bookingID) {
			this.bookingID = bookingID;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getDecisionValue() {
			return decisionValue;
		}
		public void setDecisionValue(String decisionValue) {
			this.decisionValue = decisionValue;
		}
		public String getSignumId() {
			return signumId;
		}
		public void setSignumId(String signumId) {
			this.signumId = signumId;
		}
		public Date getBookedOn() {
			return bookedOn;
		}
		public void setBookedOn(Date bookedOn) {
			this.bookedOn = bookedOn;
		}
		public String getBotPlatform() {
			return botPlatform;
		}
		public void setBotPlatform(String botPlatform) {
			this.botPlatform = botPlatform;
		}

}
