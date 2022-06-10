package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TblBulkWorkOrderCreation generated by hbm2java
 */
@Entity
@Table(name = "TBL_BULK_WORK_ORDER_CREATION", schema = "transactionalData")
public class TblBulkWorkOrderCreation implements java.io.Serializable {

	private long wocreationId;
	private Integer projectId;
	private String subserviceArea;
	private String domain;
	private String subDomain;
	private String technology;
	private Integer woid;
	private String scopeName;
	private Integer subActivityId;
	private String subActivityName;
	private String priority;
	private String slahours;
	private String assignTo;
	private Date startDate;
	private Date startTime;
	private String workOrderName;
	private String workFlowName;
	private String nodeType;
	private String nodeNames;
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private Integer externalSourceId;
	private String externalSourceName;
	private String uploadedBy;
	private Integer executionPlanId;
	private Set<TblBulkWorkOrderCreationHistory> tblBulkWorkOrderCreationHistoriesForWocreationFromId = new HashSet<TblBulkWorkOrderCreationHistory>(
			0);
	private Set<TblBulkWorkOrderCreationHistory> tblBulkWorkOrderCreationHistoriesForWocreationToId = new HashSet<TblBulkWorkOrderCreationHistory>(
			0);

	public TblBulkWorkOrderCreation() {
	}

	public TblBulkWorkOrderCreation(long wocreationId) {
		this.wocreationId = wocreationId;
	}

	public TblBulkWorkOrderCreation(long wocreationId, Integer projectId, String subserviceArea, String domain,
			String subDomain, String technology, Integer woid, String scopeName, Integer subActivityId,
			String subActivityName, String priority, String slahours, String assignTo, Date startDate, Date startTime,
			String workOrderName, String workFlowName, String nodeType, String nodeNames, String createdBy,
			Date createdDate, String lastModifiedBy, Date lastModifiedDate, Integer externalSourceId,
			String externalSourceName, String uploadedBy, Integer executionPlanId,
			Set<TblBulkWorkOrderCreationHistory> tblBulkWorkOrderCreationHistoriesForWocreationFromId,
			Set<TblBulkWorkOrderCreationHistory> tblBulkWorkOrderCreationHistoriesForWocreationToId) {
		this.wocreationId = wocreationId;
		this.projectId = projectId;
		this.subserviceArea = subserviceArea;
		this.domain = domain;
		this.subDomain = subDomain;
		this.technology = technology;
		this.woid = woid;
		this.scopeName = scopeName;
		this.subActivityId = subActivityId;
		this.subActivityName = subActivityName;
		this.priority = priority;
		this.slahours = slahours;
		this.assignTo = assignTo;
		this.startDate = startDate;
		this.startTime = startTime;
		this.workOrderName = workOrderName;
		this.workFlowName = workFlowName;
		this.nodeType = nodeType;
		this.nodeNames = nodeNames;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.externalSourceId = externalSourceId;
		this.externalSourceName = externalSourceName;
		this.uploadedBy = uploadedBy;
		this.executionPlanId = executionPlanId;
		this.tblBulkWorkOrderCreationHistoriesForWocreationFromId = tblBulkWorkOrderCreationHistoriesForWocreationFromId;
		this.tblBulkWorkOrderCreationHistoriesForWocreationToId = tblBulkWorkOrderCreationHistoriesForWocreationToId;
	}

	@Id

	@Column(name = "WOCreationID", unique = true, nullable = false)
	public long getWocreationId() {
		return this.wocreationId;
	}

	public void setWocreationId(long wocreationId) {
		this.wocreationId = wocreationId;
	}

	@Column(name = "ProjectID")
	public Integer getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	@Column(name = "SubserviceArea", length = 1024)
	public String getSubserviceArea() {
		return this.subserviceArea;
	}

	public void setSubserviceArea(String subserviceArea) {
		this.subserviceArea = subserviceArea;
	}

	@Column(name = "Domain", length = 1024)
	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Column(name = "SubDomain", length = 1024)
	public String getSubDomain() {
		return this.subDomain;
	}

	public void setSubDomain(String subDomain) {
		this.subDomain = subDomain;
	}

	@Column(name = "Technology", length = 128)
	public String getTechnology() {
		return this.technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	@Column(name = "WOID")
	public Integer getWoid() {
		return this.woid;
	}

	public void setWoid(Integer woid) {
		this.woid = woid;
	}

	@Column(name = "ScopeName", length = 1024)
	public String getScopeName() {
		return this.scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

	@Column(name = "SubActivityID")
	public Integer getSubActivityId() {
		return this.subActivityId;
	}

	public void setSubActivityId(Integer subActivityId) {
		this.subActivityId = subActivityId;
	}

	@Column(name = "SubActivityName", length = 1024)
	public String getSubActivityName() {
		return this.subActivityName;
	}

	public void setSubActivityName(String subActivityName) {
		this.subActivityName = subActivityName;
	}

	@Column(name = "Priority", length = 1024)
	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	@Column(name = "SLAHours", length = 20)
	public String getSlahours() {
		return this.slahours;
	}

	public void setSlahours(String slahours) {
		this.slahours = slahours;
	}

	@Column(name = "AssignTo", length = 1024)
	public String getAssignTo() {
		return this.assignTo;
	}

	public void setAssignTo(String assignTo) {
		this.assignTo = assignTo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "StartDate", length = 10)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIME)
	@Column(name = "StartTime", length = 16)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "WorkOrderName", length = 1024)
	public String getWorkOrderName() {
		return this.workOrderName;
	}

	public void setWorkOrderName(String workOrderName) {
		this.workOrderName = workOrderName;
	}

	@Column(name = "WorkFlowName", length = 1024)
	public String getWorkFlowName() {
		return this.workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}

	@Column(name = "NodeType", length = 1024)
	public String getNodeType() {
		return this.nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	@Column(name = "NodeNames", length = 1024)
	public String getNodeNames() {
		return this.nodeNames;
	}

	public void setNodeNames(String nodeNames) {
		this.nodeNames = nodeNames;
	}

	@Column(name = "CreatedBy", length = 1024)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CreatedDate", length = 23)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "LastModifiedBy", length = 1024)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LastModifiedDate", length = 23)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "ExternalSourceID")
	public Integer getExternalSourceId() {
		return this.externalSourceId;
	}

	public void setExternalSourceId(Integer externalSourceId) {
		this.externalSourceId = externalSourceId;
	}

	@Column(name = "ExternalSourceName", length = 1024)
	public String getExternalSourceName() {
		return this.externalSourceName;
	}

	public void setExternalSourceName(String externalSourceName) {
		this.externalSourceName = externalSourceName;
	}

	@Column(name = "UploadedBy", length = 1024)
	public String getUploadedBy() {
		return this.uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	@Column(name = "ExecutionPlanID")
	public Integer getExecutionPlanId() {
		return this.executionPlanId;
	}

	public void setExecutionPlanId(Integer executionPlanId) {
		this.executionPlanId = executionPlanId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblBulkWorkOrderCreationByWocreationFromId")
	public Set<TblBulkWorkOrderCreationHistory> getTblBulkWorkOrderCreationHistoriesForWocreationFromId() {
		return this.tblBulkWorkOrderCreationHistoriesForWocreationFromId;
	}

	public void setTblBulkWorkOrderCreationHistoriesForWocreationFromId(
			Set<TblBulkWorkOrderCreationHistory> tblBulkWorkOrderCreationHistoriesForWocreationFromId) {
		this.tblBulkWorkOrderCreationHistoriesForWocreationFromId = tblBulkWorkOrderCreationHistoriesForWocreationFromId;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tblBulkWorkOrderCreationByWocreationToId")
	public Set<TblBulkWorkOrderCreationHistory> getTblBulkWorkOrderCreationHistoriesForWocreationToId() {
		return this.tblBulkWorkOrderCreationHistoriesForWocreationToId;
	}

	public void setTblBulkWorkOrderCreationHistoriesForWocreationToId(
			Set<TblBulkWorkOrderCreationHistory> tblBulkWorkOrderCreationHistoriesForWocreationToId) {
		this.tblBulkWorkOrderCreationHistoriesForWocreationToId = tblBulkWorkOrderCreationHistoriesForWocreationToId;
	}

}
