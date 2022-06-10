package com.ericsson.isf.model;

public class WorkflowProficiencyModel {
	    private int wfUserProficenctID;
        private String workFlowName;
        private String signum;
        private String proficiencyLevel;
        private String triggeredBy;
        private String modifiedBy;
        private int workFlowId;
        private int projectId;
        private int displayMode;
        private int subActivityId;
        private String comments;
        private boolean futureWorkOrdersUpdate;
		private int versionNumber;
		private int subActivityFlowchartDefID;
		private int previousWfUserProficenctID;
		private String previousProficiencyLevel;
		private String recordsTotal;
		private String recordsFiltered;
		private String lastModifiedOn;
		
		public String getLastModifiedOn() {
			return lastModifiedOn;
		}
		public void setLastModifiedOn(String lastModifiedOn) {
			this.lastModifiedOn = lastModifiedOn;
		}
		public String getPreviousProficiencyLevel() {
			return previousProficiencyLevel;
		}
		public void setPreviousProficiencyLevel(String previousProficiencyLevel) {
			this.previousProficiencyLevel = previousProficiencyLevel;
		}
		public String getComments() {
			return comments;
		}
		public void setComments(String comments) {
			this.comments = comments;
		}
		public String getWorkFlowName() {
			return workFlowName;
		}
		public void setWorkFlowName(String workFlowName) {
			this.workFlowName = workFlowName;
		}
		public String getSignum() {
			return signum;
		}
		public void setSignum(String signum) {
			this.signum = signum;
		}
		public String getProficiencyLevel() {
			return proficiencyLevel;
		}
		public void setProficiencyLevel(String proficiencyLevel) {
			this.proficiencyLevel = proficiencyLevel;
		}
		public String getTriggeredBy() {
			return triggeredBy;
		}
		public void setTriggeredBy(String triggeredBy) {
			this.triggeredBy = triggeredBy;
		}
		public String getModifiedBy() {
			return modifiedBy;
		}
		public void setModifiedBy(String modifiedBy) {
			this.modifiedBy = modifiedBy;
		}
		public int getWorkFlowId() {
			return workFlowId;
		}
		public void setWorkFlowId(int workFlowId) {
			this.workFlowId = workFlowId;
		}
		public int getProjectId() {
			return projectId;
		}
		public void setProjectId(int projectId) {
			this.projectId = projectId;
		}
	
		public int getDisplayMode() {
			return displayMode;
		}
		public void setDisplayMode(int displayMode) {
			this.displayMode = displayMode;
		}
		public int getSubActivityId() {
			return subActivityId;
		}
		public void setSubActivityId(int subActivityId) {
			this.subActivityId = subActivityId;
		}
		public boolean isFutureWorkOrdersUpdate() {
			return futureWorkOrdersUpdate;
		}

		public void setFutureWorkOrdersUpdate(boolean futureWorkOrdersUpdate) {
			this.futureWorkOrdersUpdate = futureWorkOrdersUpdate;
		}
		public int getVersionNumber() {
			return versionNumber;
		}
		public void setVersionNumber(int versionNumber) {
			this.versionNumber = versionNumber;
		}
		public int getSubActivityFlowchartDefID() {
			return subActivityFlowchartDefID;
		}
		public void setSubActivityFlowchartDefID(int subActivityFlowchartDefID) {
			this.subActivityFlowchartDefID = subActivityFlowchartDefID;
		}
		public int getWfUserProficenctID() {
			return wfUserProficenctID;
		}
		public void setWfUserProficenctID(int wfUserProficenctID) {
			this.wfUserProficenctID = wfUserProficenctID;
		}
		public int getPreviousWfUserProficenctID() {
			return previousWfUserProficenctID;
		}
		public void setPreviousWfUserProficenctID(int previousWfUserProficenctID) {
			this.previousWfUserProficenctID = previousWfUserProficenctID;
		}
		public String getRecordsTotal() {
			return recordsTotal;
		}
		public void setRecordsTotal(String recordsTotal) {
			this.recordsTotal = recordsTotal;
		}
		public String getRecordsFiltered() {
			return recordsFiltered;
		}
		public void setRecordsFiltered(String recordsFiltered) {
			this.recordsFiltered = recordsFiltered;
		}
		
		
        
}
