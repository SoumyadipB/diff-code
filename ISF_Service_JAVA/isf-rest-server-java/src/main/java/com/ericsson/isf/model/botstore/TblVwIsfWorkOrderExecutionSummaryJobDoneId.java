package com.ericsson.isf.model.botstore;
// Generated Jul 24, 2018 11:07:24 AM by Hibernate Tools 4.3.1.Final

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TblVwIsfWorkOrderExecutionSummaryJobDoneId generated by hbm2java
 */
@Embeddable
public class TblVwIsfWorkOrderExecutionSummaryJobDoneId implements java.io.Serializable {

	private String marketAreaId;
	private String marketAreaName;
	private String projectId;
	private String projectName;
	private String countryName;
	private String spmname;
	private String pcode;
	private String servAreaId;
	private String serviceArea;
	private String serviceAreaId;
	private String subServiceArea;
	private String customerId;
	private String customerName;
	private String technologyId;
	private String technology;
	private String domainId;
	private String domain;
	private String subdomain;
	private String subActivityId;
	private String activity;
	private String subActivity;
	private String projectScopeId;
	private String scopeName;
	private String woid;
	private String woplanId;
	private String woActualStartDate;
	private String woActualEndDate;
	private String woPlannedStartDate;
	private String woPlannedEndDate;
	private String woSignum;
	private String woStatus;
	private String executionType;
	private String botId;
	private String taskId;
	private String task;
	private String bookingId;
	private String parentBookingDetailsId;
	private String bookStartDate;
	private String bookEndDate;
	private String hours;
	private String type;
	private String status;
	private String serviceProfessionalSignum;
	private String serviceProfessionalName;
	private String serviceProfessionalJobStage;
	private String stepName;

	private String actualStartDateForStep;

	private String actualEndDateForStep;

	private String workFlowName;
	private String wofcstepDetailsId;
	private String subActivityFlowChartDefId;
	private String workFlowVersion;
	private String reason;
	private String outputLink;
	private String parallelcount;
	private String uploadedOn;

	public TblVwIsfWorkOrderExecutionSummaryJobDoneId() {
	}

	public TblVwIsfWorkOrderExecutionSummaryJobDoneId(String marketAreaId, String marketAreaName, String projectId, String projectName, String countryName, String spmname, String pcode, String servAreaId, String serviceArea, String serviceAreaId, String subServiceArea, String customerId, String customerName, String technologyId, String technology, String domainId, String domain, String subdomain, String subActivityId, String activity, String subActivity, String projectScopeId, String scopeName, String woid, String woplanId, String woActualStartDate, String woActualEndDate, String woPlannedStartDate, String woPlannedEndDate, String woSignum, String woStatus, String executionType, String botId, String taskId, String task, String bookingId, String parentBookingDetailsId, String bookStartDate, String bookEndDate, String hours, String type, String status, String serviceProfessionalSignum, String serviceProfessionalName, String serviceProfessionalJobStage, String stepName,

	String actualStartDateForStep,

	String actualEndDateForStep, String workFlowName, String wofcstepDetailsId, String subActivityFlowChartDefId, String workFlowVersion, String reason, String outputLink, String parallelcount, String uploadedOn) {
       this.marketAreaId = marketAreaId;
       this.marketAreaName = marketAreaName;
       this.projectId = projectId;
       this.projectName = projectName;
       this.countryName = countryName;
       this.spmname = spmname;
       this.pcode = pcode;
       this.servAreaId = servAreaId;
       this.serviceArea = serviceArea;
       this.serviceAreaId = serviceAreaId;
       this.subServiceArea = subServiceArea;
       this.customerId = customerId;
       this.customerName = customerName;
       this.technologyId = technologyId;
       this.technology = technology;
       this.domainId = domainId;
       this.domain = domain;
       this.subdomain = subdomain;
       this.subActivityId = subActivityId;
       this.activity = activity;
       this.subActivity = subActivity;
       this.projectScopeId = projectScopeId;
       this.scopeName = scopeName;
       this.woid = woid;
       this.woplanId = woplanId;
       this.woActualStartDate = woActualStartDate;
       this.woActualEndDate = woActualEndDate;
       this.woPlannedStartDate = woPlannedStartDate;
       this.woPlannedEndDate = woPlannedEndDate;
       this.woSignum = woSignum;
       this.woStatus = woStatus;
       this.executionType = executionType;
       this.botId = botId;
       this.taskId = taskId;
       this.task = task;
       this.bookingId = bookingId;
       this.parentBookingDetailsId = parentBookingDetailsId;
       this.bookStartDate = bookStartDate;
       this.bookEndDate = bookEndDate;
       this.hours = hours;
       this.type = type;
       this.status = status;
       this.serviceProfessionalSignum = serviceProfessionalSignum;
       this.serviceProfessionalName = serviceProfessionalName;
       this.serviceProfessionalJobStage = serviceProfessionalJobStage;
       this.stepName = stepName;
       this.actualStartDateForStep = actualStartDateForStep;
       this.actualEndDateForStep = actualEndDateForStep;
       this.workFlowName = workFlowName;
       this.wofcstepDetailsId = wofcstepDetailsId;
       this.subActivityFlowChartDefId = subActivityFlowChartDefId;
       this.workFlowVersion = workFlowVersion;
       this.reason = reason;
       this.outputLink = outputLink;
       this.parallelcount = parallelcount;
       this.uploadedOn = uploadedOn;
    }

	@Column(name = "MarketAreaID")
	public String getMarketAreaId() {
		return this.marketAreaId;
	}

	public void setMarketAreaId(String marketAreaId) {
		this.marketAreaId = marketAreaId;
	}

	@Column(name = "MarketAreaName")
	public String getMarketAreaName() {
		return this.marketAreaName;
	}

	public void setMarketAreaName(String marketAreaName) {
		this.marketAreaName = marketAreaName;
	}

	@Column(name = "ProjectID")
	public String getProjectId() {
		return this.projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	@Column(name = "ProjectName")
	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Column(name = "CountryName")
	public String getCountryName() {
		return this.countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	@Column(name = "SPMName")
	public String getSpmname() {
		return this.spmname;
	}

	public void setSpmname(String spmname) {
		this.spmname = spmname;
	}

	@Column(name = "Pcode")
	public String getPcode() {
		return this.pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	@Column(name = "ServAreaID")
	public String getServAreaId() {
		return this.servAreaId;
	}

	public void setServAreaId(String servAreaId) {
		this.servAreaId = servAreaId;
	}

	@Column(name = "ServiceArea")
	public String getServiceArea() {
		return this.serviceArea;
	}

	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}

	@Column(name = "ServiceAreaID")
	public String getServiceAreaId() {
		return this.serviceAreaId;
	}

	public void setServiceAreaId(String serviceAreaId) {
		this.serviceAreaId = serviceAreaId;
	}

	@Column(name = "SubServiceArea")
	public String getSubServiceArea() {
		return this.subServiceArea;
	}

	public void setSubServiceArea(String subServiceArea) {
		this.subServiceArea = subServiceArea;
	}

	@Column(name = "CustomerID")
	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Column(name = "CustomerName")
	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Column(name = "TechnologyID")
	public String getTechnologyId() {
		return this.technologyId;
	}

	public void setTechnologyId(String technologyId) {
		this.technologyId = technologyId;
	}

	@Column(name = "Technology")
	public String getTechnology() {
		return this.technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	@Column(name = "DomainID")
	public String getDomainId() {
		return this.domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	@Column(name = "Domain")
	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Column(name = "subdomain")
	public String getSubdomain() {
		return this.subdomain;
	}

	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}

	@Column(name = "SubActivityID")
	public String getSubActivityId() {
		return this.subActivityId;
	}

	public void setSubActivityId(String subActivityId) {
		this.subActivityId = subActivityId;
	}

	@Column(name = "Activity")
	public String getActivity() {
		return this.activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	@Column(name = "SubActivity")
	public String getSubActivity() {
		return this.subActivity;
	}

	public void setSubActivity(String subActivity) {
		this.subActivity = subActivity;
	}

	@Column(name = "ProjectScopeID")
	public String getProjectScopeId() {
		return this.projectScopeId;
	}

	public void setProjectScopeId(String projectScopeId) {
		this.projectScopeId = projectScopeId;
	}

	@Column(name = "ScopeName")
	public String getScopeName() {
		return this.scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

	@Column(name = "WOID")
	public String getWoid() {
		return this.woid;
	}

	public void setWoid(String woid) {
		this.woid = woid;
	}

	@Column(name = "WOPlanID")
	public String getWoplanId() {
		return this.woplanId;
	}

	public void setWoplanId(String woplanId) {
		this.woplanId = woplanId;
	}

	@Column(name = "WO Actual start Date")
	public String getWoActualStartDate() {
		return this.woActualStartDate;
	}

	public void setWoActualStartDate(String woActualStartDate) {
		this.woActualStartDate = woActualStartDate;
	}

	@Column(name = "WO Actual End Date")
	public String getWoActualEndDate() {
		return this.woActualEndDate;
	}

	public void setWoActualEndDate(String woActualEndDate) {
		this.woActualEndDate = woActualEndDate;
	}

	@Column(name = "WO Planned Start Date")
	public String getWoPlannedStartDate() {
		return this.woPlannedStartDate;
	}

	public void setWoPlannedStartDate(String woPlannedStartDate) {
		this.woPlannedStartDate = woPlannedStartDate;
	}

	@Column(name = "WO Planned End Date")
	public String getWoPlannedEndDate() {
		return this.woPlannedEndDate;
	}

	public void setWoPlannedEndDate(String woPlannedEndDate) {
		this.woPlannedEndDate = woPlannedEndDate;
	}

	@Column(name = "WO Signum")
	public String getWoSignum() {
		return this.woSignum;
	}

	public void setWoSignum(String woSignum) {
		this.woSignum = woSignum;
	}

	@Column(name = "WO Status")
	public String getWoStatus() {
		return this.woStatus;
	}

	public void setWoStatus(String woStatus) {
		this.woStatus = woStatus;
	}

	@Column(name = "Execution Type")
	public String getExecutionType() {
		return this.executionType;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	@Column(name = "BOT Id")
	public String getBotId() {
		return this.botId;
	}

	public void setBotId(String botId) {
		this.botId = botId;
	}

	@Column(name = "TaskID")
	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Column(name = "Task")
	public String getTask() {
		return this.task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	@Column(name = "BookingID")
	public String getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	@Column(name = "ParentBookingDetailsID")
	public String getParentBookingDetailsId() {
		return this.parentBookingDetailsId;
	}

	public void setParentBookingDetailsId(String parentBookingDetailsId) {
		this.parentBookingDetailsId = parentBookingDetailsId;
	}

	@Column(name = "Book Start Date")
	public String getBookStartDate() {
		return this.bookStartDate;
	}

	public void setBookStartDate(String bookStartDate) {
		this.bookStartDate = bookStartDate;
	}

	@Column(name = "Book End Date")
	public String getBookEndDate() {
		return this.bookEndDate;
	}

	public void setBookEndDate(String bookEndDate) {
		this.bookEndDate = bookEndDate;
	}

	@Column(name = "Hours")
	public String getHours() {
		return this.hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	@Column(name = "Type")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "Status")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "Service Professional Signum")
	public String getServiceProfessionalSignum() {
		return this.serviceProfessionalSignum;
	}

	public void setServiceProfessionalSignum(String serviceProfessionalSignum) {
		this.serviceProfessionalSignum = serviceProfessionalSignum;
	}

	@Column(name = "Service Professional Name")
	public String getServiceProfessionalName() {
		return this.serviceProfessionalName;
	}

	public void setServiceProfessionalName(String serviceProfessionalName) {
		this.serviceProfessionalName = serviceProfessionalName;
	}

	@Column(name = "Service Professional Job Stage")
	public String getServiceProfessionalJobStage() {
		return this.serviceProfessionalJobStage;
	}

	public void setServiceProfessionalJobStage(String serviceProfessionalJobStage) {
		this.serviceProfessionalJobStage = serviceProfessionalJobStage;
	}

	@Column(name = "StepName")
	public String getStepName() {
		return this.stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	@Column(name="Actual Start Date (Step)")
    public String getActualStartDateForStep() {
        return this.actualStartDateForStep;
    }

	public void setActualStartDateForStep(

	String actualStartDateForStep) {
        this.actualStartDateForStep = actualStartDateForStep;
    }

	@Column(name="Actual End Date (Step)")
    public String getActualEndDateForStep() {
        return this.actualEndDateForStep;
    }

	public void setActualEndDateForStep(

	String actualEndDateForStep) {
        this.actualEndDateForStep = actualEndDateForStep;
    }

	@Column(name = "WorkFlowName")
	public String getWorkFlowName() {
		return this.workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}

	@Column(name = "WOFCStepDetailsID")
	public String getWofcstepDetailsId() {
		return this.wofcstepDetailsId;
	}

	public void setWofcstepDetailsId(String wofcstepDetailsId) {
		this.wofcstepDetailsId = wofcstepDetailsId;
	}

	@Column(name = "SubActivityFlowChartDefID")
	public String getSubActivityFlowChartDefId() {
		return this.subActivityFlowChartDefId;
	}

	public void setSubActivityFlowChartDefId(String subActivityFlowChartDefId) {
		this.subActivityFlowChartDefId = subActivityFlowChartDefId;
	}

	@Column(name = "WorkFlowVersion")
	public String getWorkFlowVersion() {
		return this.workFlowVersion;
	}

	public void setWorkFlowVersion(String workFlowVersion) {
		this.workFlowVersion = workFlowVersion;
	}

	@Column(name = "Reason")
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "OutputLink")
	public String getOutputLink() {
		return this.outputLink;
	}

	public void setOutputLink(String outputLink) {
		this.outputLink = outputLink;
	}

	@Column(name = "parallelcount")
	public String getParallelcount() {
		return this.parallelcount;
	}

	public void setParallelcount(String parallelcount) {
		this.parallelcount = parallelcount;
	}

	@Column(name = "UploadedOn")
	public String getUploadedOn() {
		return this.uploadedOn;
	}

	public void setUploadedOn(String uploadedOn) {
		this.uploadedOn = uploadedOn;
	}

	public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof TblVwIsfWorkOrderExecutionSummaryJobDoneId) ) return false;
		 TblVwIsfWorkOrderExecutionSummaryJobDoneId castOther = ( TblVwIsfWorkOrderExecutionSummaryJobDoneId ) other; 
         
		 return ( (this.getMarketAreaId()==castOther.getMarketAreaId()) || ( this.getMarketAreaId()!=null && castOther.getMarketAreaId()!=null && this.getMarketAreaId().equals(castOther.getMarketAreaId()) ) )
 && ( (this.getMarketAreaName()==castOther.getMarketAreaName()) || ( this.getMarketAreaName()!=null && castOther.getMarketAreaName()!=null && this.getMarketAreaName().equals(castOther.getMarketAreaName()) ) )
 && ( (this.getProjectId()==castOther.getProjectId()) || ( this.getProjectId()!=null && castOther.getProjectId()!=null && this.getProjectId().equals(castOther.getProjectId()) ) )
 && ( (this.getProjectName()==castOther.getProjectName()) || ( this.getProjectName()!=null && castOther.getProjectName()!=null && this.getProjectName().equals(castOther.getProjectName()) ) )
 && ( (this.getCountryName()==castOther.getCountryName()) || ( this.getCountryName()!=null && castOther.getCountryName()!=null && this.getCountryName().equals(castOther.getCountryName()) ) )
 && ( (this.getSpmname()==castOther.getSpmname()) || ( this.getSpmname()!=null && castOther.getSpmname()!=null && this.getSpmname().equals(castOther.getSpmname()) ) )
 && ( (this.getPcode()==castOther.getPcode()) || ( this.getPcode()!=null && castOther.getPcode()!=null && this.getPcode().equals(castOther.getPcode()) ) )
 && ( (this.getServAreaId()==castOther.getServAreaId()) || ( this.getServAreaId()!=null && castOther.getServAreaId()!=null && this.getServAreaId().equals(castOther.getServAreaId()) ) )
 && ( (this.getServiceArea()==castOther.getServiceArea()) || ( this.getServiceArea()!=null && castOther.getServiceArea()!=null && this.getServiceArea().equals(castOther.getServiceArea()) ) )
 && ( (this.getServiceAreaId()==castOther.getServiceAreaId()) || ( this.getServiceAreaId()!=null && castOther.getServiceAreaId()!=null && this.getServiceAreaId().equals(castOther.getServiceAreaId()) ) )
 && ( (this.getSubServiceArea()==castOther.getSubServiceArea()) || ( this.getSubServiceArea()!=null && castOther.getSubServiceArea()!=null && this.getSubServiceArea().equals(castOther.getSubServiceArea()) ) )
 && ( (this.getCustomerId()==castOther.getCustomerId()) || ( this.getCustomerId()!=null && castOther.getCustomerId()!=null && this.getCustomerId().equals(castOther.getCustomerId()) ) )
 && ( (this.getCustomerName()==castOther.getCustomerName()) || ( this.getCustomerName()!=null && castOther.getCustomerName()!=null && this.getCustomerName().equals(castOther.getCustomerName()) ) )
 && ( (this.getTechnologyId()==castOther.getTechnologyId()) || ( this.getTechnologyId()!=null && castOther.getTechnologyId()!=null && this.getTechnologyId().equals(castOther.getTechnologyId()) ) )
 && ( (this.getTechnology()==castOther.getTechnology()) || ( this.getTechnology()!=null && castOther.getTechnology()!=null && this.getTechnology().equals(castOther.getTechnology()) ) )
 && ( (this.getDomainId()==castOther.getDomainId()) || ( this.getDomainId()!=null && castOther.getDomainId()!=null && this.getDomainId().equals(castOther.getDomainId()) ) )
 && ( (this.getDomain()==castOther.getDomain()) || ( this.getDomain()!=null && castOther.getDomain()!=null && this.getDomain().equals(castOther.getDomain()) ) )
 && ( (this.getSubdomain()==castOther.getSubdomain()) || ( this.getSubdomain()!=null && castOther.getSubdomain()!=null && this.getSubdomain().equals(castOther.getSubdomain()) ) )
 && ( (this.getSubActivityId()==castOther.getSubActivityId()) || ( this.getSubActivityId()!=null && castOther.getSubActivityId()!=null && this.getSubActivityId().equals(castOther.getSubActivityId()) ) )
 && ( (this.getActivity()==castOther.getActivity()) || ( this.getActivity()!=null && castOther.getActivity()!=null && this.getActivity().equals(castOther.getActivity()) ) )
 && ( (this.getSubActivity()==castOther.getSubActivity()) || ( this.getSubActivity()!=null && castOther.getSubActivity()!=null && this.getSubActivity().equals(castOther.getSubActivity()) ) )
 && ( (this.getProjectScopeId()==castOther.getProjectScopeId()) || ( this.getProjectScopeId()!=null && castOther.getProjectScopeId()!=null && this.getProjectScopeId().equals(castOther.getProjectScopeId()) ) )
 && ( (this.getScopeName()==castOther.getScopeName()) || ( this.getScopeName()!=null && castOther.getScopeName()!=null && this.getScopeName().equals(castOther.getScopeName()) ) )
 && ( (this.getWoid()==castOther.getWoid()) || ( this.getWoid()!=null && castOther.getWoid()!=null && this.getWoid().equals(castOther.getWoid()) ) )
 && ( (this.getWoplanId()==castOther.getWoplanId()) || ( this.getWoplanId()!=null && castOther.getWoplanId()!=null && this.getWoplanId().equals(castOther.getWoplanId()) ) )
 && ( (this.getWoActualStartDate()==castOther.getWoActualStartDate()) || ( this.getWoActualStartDate()!=null && castOther.getWoActualStartDate()!=null && this.getWoActualStartDate().equals(castOther.getWoActualStartDate()) ) )
 && ( (this.getWoActualEndDate()==castOther.getWoActualEndDate()) || ( this.getWoActualEndDate()!=null && castOther.getWoActualEndDate()!=null && this.getWoActualEndDate().equals(castOther.getWoActualEndDate()) ) )
 && ( (this.getWoPlannedStartDate()==castOther.getWoPlannedStartDate()) || ( this.getWoPlannedStartDate()!=null && castOther.getWoPlannedStartDate()!=null && this.getWoPlannedStartDate().equals(castOther.getWoPlannedStartDate()) ) )
 && ( (this.getWoPlannedEndDate()==castOther.getWoPlannedEndDate()) || ( this.getWoPlannedEndDate()!=null && castOther.getWoPlannedEndDate()!=null && this.getWoPlannedEndDate().equals(castOther.getWoPlannedEndDate()) ) )
 && ( (this.getWoSignum()==castOther.getWoSignum()) || ( this.getWoSignum()!=null && castOther.getWoSignum()!=null && this.getWoSignum().equals(castOther.getWoSignum()) ) )
 && ( (this.getWoStatus()==castOther.getWoStatus()) || ( this.getWoStatus()!=null && castOther.getWoStatus()!=null && this.getWoStatus().equals(castOther.getWoStatus()) ) )
 && ( (this.getExecutionType()==castOther.getExecutionType()) || ( this.getExecutionType()!=null && castOther.getExecutionType()!=null && this.getExecutionType().equals(castOther.getExecutionType()) ) )
 && ( (this.getBotId()==castOther.getBotId()) || ( this.getBotId()!=null && castOther.getBotId()!=null && this.getBotId().equals(castOther.getBotId()) ) )
 && ( (this.getTaskId()==castOther.getTaskId()) || ( this.getTaskId()!=null && castOther.getTaskId()!=null && this.getTaskId().equals(castOther.getTaskId()) ) )
 && ( (this.getTask()==castOther.getTask()) || ( this.getTask()!=null && castOther.getTask()!=null && this.getTask().equals(castOther.getTask()) ) )
 && ( (this.getBookingId()==castOther.getBookingId()) || ( this.getBookingId()!=null && castOther.getBookingId()!=null && this.getBookingId().equals(castOther.getBookingId()) ) )
 && ( (this.getParentBookingDetailsId()==castOther.getParentBookingDetailsId()) || ( this.getParentBookingDetailsId()!=null && castOther.getParentBookingDetailsId()!=null && this.getParentBookingDetailsId().equals(castOther.getParentBookingDetailsId()) ) )
 && ( (this.getBookStartDate()==castOther.getBookStartDate()) || ( this.getBookStartDate()!=null && castOther.getBookStartDate()!=null && this.getBookStartDate().equals(castOther.getBookStartDate()) ) )
 && ( (this.getBookEndDate()==castOther.getBookEndDate()) || ( this.getBookEndDate()!=null && castOther.getBookEndDate()!=null && this.getBookEndDate().equals(castOther.getBookEndDate()) ) )
 && ( (this.getHours()==castOther.getHours()) || ( this.getHours()!=null && castOther.getHours()!=null && this.getHours().equals(castOther.getHours()) ) )
 && ( (this.getType()==castOther.getType()) || ( this.getType()!=null && castOther.getType()!=null && this.getType().equals(castOther.getType()) ) )
 && ( (this.getStatus()==castOther.getStatus()) || ( this.getStatus()!=null && castOther.getStatus()!=null && this.getStatus().equals(castOther.getStatus()) ) )
 && ( (this.getServiceProfessionalSignum()==castOther.getServiceProfessionalSignum()) || ( this.getServiceProfessionalSignum()!=null && castOther.getServiceProfessionalSignum()!=null && this.getServiceProfessionalSignum().equals(castOther.getServiceProfessionalSignum()) ) )
 && ( (this.getServiceProfessionalName()==castOther.getServiceProfessionalName()) || ( this.getServiceProfessionalName()!=null && castOther.getServiceProfessionalName()!=null && this.getServiceProfessionalName().equals(castOther.getServiceProfessionalName()) ) )
 && ( (this.getServiceProfessionalJobStage()==castOther.getServiceProfessionalJobStage()) || ( this.getServiceProfessionalJobStage()!=null && castOther.getServiceProfessionalJobStage()!=null && this.getServiceProfessionalJobStage().equals(castOther.getServiceProfessionalJobStage()) ) )
 && ( (this.getStepName()==castOther.getStepName()) || ( this.getStepName()!=null && castOther.getStepName()!=null && this.getStepName().equals(castOther.getStepName()) ) )
 && ( (this.getActualStartDateForStep()==castOther.getActualStartDateForStep()) || ( this.getActualStartDateForStep()!=null && castOther.getActualStartDateForStep()!=null && this.getActualStartDateForStep().equals(castOther.getActualStartDateForStep()) ) )
 && ( (this.getActualEndDateForStep()==castOther.getActualEndDateForStep()) || ( this.getActualEndDateForStep()!=null && castOther.getActualEndDateForStep()!=null && this.getActualEndDateForStep().equals(castOther.getActualEndDateForStep()) ) )
 && ( (this.getWorkFlowName()==castOther.getWorkFlowName()) || ( this.getWorkFlowName()!=null && castOther.getWorkFlowName()!=null && this.getWorkFlowName().equals(castOther.getWorkFlowName()) ) )
 && ( (this.getWofcstepDetailsId()==castOther.getWofcstepDetailsId()) || ( this.getWofcstepDetailsId()!=null && castOther.getWofcstepDetailsId()!=null && this.getWofcstepDetailsId().equals(castOther.getWofcstepDetailsId()) ) )
 && ( (this.getSubActivityFlowChartDefId()==castOther.getSubActivityFlowChartDefId()) || ( this.getSubActivityFlowChartDefId()!=null && castOther.getSubActivityFlowChartDefId()!=null && this.getSubActivityFlowChartDefId().equals(castOther.getSubActivityFlowChartDefId()) ) )
 && ( (this.getWorkFlowVersion()==castOther.getWorkFlowVersion()) || ( this.getWorkFlowVersion()!=null && castOther.getWorkFlowVersion()!=null && this.getWorkFlowVersion().equals(castOther.getWorkFlowVersion()) ) )
 && ( (this.getReason()==castOther.getReason()) || ( this.getReason()!=null && castOther.getReason()!=null && this.getReason().equals(castOther.getReason()) ) )
 && ( (this.getOutputLink()==castOther.getOutputLink()) || ( this.getOutputLink()!=null && castOther.getOutputLink()!=null && this.getOutputLink().equals(castOther.getOutputLink()) ) )
 && ( (this.getParallelcount()==castOther.getParallelcount()) || ( this.getParallelcount()!=null && castOther.getParallelcount()!=null && this.getParallelcount().equals(castOther.getParallelcount()) ) )
 && ( (this.getUploadedOn()==castOther.getUploadedOn()) || ( this.getUploadedOn()!=null && castOther.getUploadedOn()!=null && this.getUploadedOn().equals(castOther.getUploadedOn()) ) );
   }

	public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getMarketAreaId() == null ? 0 : this.getMarketAreaId().hashCode() );
         result = 37 * result + ( getMarketAreaName() == null ? 0 : this.getMarketAreaName().hashCode() );
         result = 37 * result + ( getProjectId() == null ? 0 : this.getProjectId().hashCode() );
         result = 37 * result + ( getProjectName() == null ? 0 : this.getProjectName().hashCode() );
         result = 37 * result + ( getCountryName() == null ? 0 : this.getCountryName().hashCode() );
         result = 37 * result + ( getSpmname() == null ? 0 : this.getSpmname().hashCode() );
         result = 37 * result + ( getPcode() == null ? 0 : this.getPcode().hashCode() );
         result = 37 * result + ( getServAreaId() == null ? 0 : this.getServAreaId().hashCode() );
         result = 37 * result + ( getServiceArea() == null ? 0 : this.getServiceArea().hashCode() );
         result = 37 * result + ( getServiceAreaId() == null ? 0 : this.getServiceAreaId().hashCode() );
         result = 37 * result + ( getSubServiceArea() == null ? 0 : this.getSubServiceArea().hashCode() );
         result = 37 * result + ( getCustomerId() == null ? 0 : this.getCustomerId().hashCode() );
         result = 37 * result + ( getCustomerName() == null ? 0 : this.getCustomerName().hashCode() );
         result = 37 * result + ( getTechnologyId() == null ? 0 : this.getTechnologyId().hashCode() );
         result = 37 * result + ( getTechnology() == null ? 0 : this.getTechnology().hashCode() );
         result = 37 * result + ( getDomainId() == null ? 0 : this.getDomainId().hashCode() );
         result = 37 * result + ( getDomain() == null ? 0 : this.getDomain().hashCode() );
         result = 37 * result + ( getSubdomain() == null ? 0 : this.getSubdomain().hashCode() );
         result = 37 * result + ( getSubActivityId() == null ? 0 : this.getSubActivityId().hashCode() );
         result = 37 * result + ( getActivity() == null ? 0 : this.getActivity().hashCode() );
         result = 37 * result + ( getSubActivity() == null ? 0 : this.getSubActivity().hashCode() );
         result = 37 * result + ( getProjectScopeId() == null ? 0 : this.getProjectScopeId().hashCode() );
         result = 37 * result + ( getScopeName() == null ? 0 : this.getScopeName().hashCode() );
         result = 37 * result + ( getWoid() == null ? 0 : this.getWoid().hashCode() );
         result = 37 * result + ( getWoplanId() == null ? 0 : this.getWoplanId().hashCode() );
         result = 37 * result + ( getWoActualStartDate() == null ? 0 : this.getWoActualStartDate().hashCode() );
         result = 37 * result + ( getWoActualEndDate() == null ? 0 : this.getWoActualEndDate().hashCode() );
         result = 37 * result + ( getWoPlannedStartDate() == null ? 0 : this.getWoPlannedStartDate().hashCode() );
         result = 37 * result + ( getWoPlannedEndDate() == null ? 0 : this.getWoPlannedEndDate().hashCode() );
         result = 37 * result + ( getWoSignum() == null ? 0 : this.getWoSignum().hashCode() );
         result = 37 * result + ( getWoStatus() == null ? 0 : this.getWoStatus().hashCode() );
         result = 37 * result + ( getExecutionType() == null ? 0 : this.getExecutionType().hashCode() );
         result = 37 * result + ( getBotId() == null ? 0 : this.getBotId().hashCode() );
         result = 37 * result + ( getTaskId() == null ? 0 : this.getTaskId().hashCode() );
         result = 37 * result + ( getTask() == null ? 0 : this.getTask().hashCode() );
         result = 37 * result + ( getBookingId() == null ? 0 : this.getBookingId().hashCode() );
         result = 37 * result + ( getParentBookingDetailsId() == null ? 0 : this.getParentBookingDetailsId().hashCode() );
         result = 37 * result + ( getBookStartDate() == null ? 0 : this.getBookStartDate().hashCode() );
         result = 37 * result + ( getBookEndDate() == null ? 0 : this.getBookEndDate().hashCode() );
         result = 37 * result + ( getHours() == null ? 0 : this.getHours().hashCode() );
         result = 37 * result + ( getType() == null ? 0 : this.getType().hashCode() );
         result = 37 * result + ( getStatus() == null ? 0 : this.getStatus().hashCode() );
         result = 37 * result + ( getServiceProfessionalSignum() == null ? 0 : this.getServiceProfessionalSignum().hashCode() );
         result = 37 * result + ( getServiceProfessionalName() == null ? 0 : this.getServiceProfessionalName().hashCode() );
         result = 37 * result + ( getServiceProfessionalJobStage() == null ? 0 : this.getServiceProfessionalJobStage().hashCode() );
         result = 37 * result + ( getStepName() == null ? 0 : this.getStepName().hashCode() );
         result = 37 * result + ( getActualStartDateForStep() == null ? 0 : this.getActualStartDateForStep().hashCode() );
         result = 37 * result + ( getActualEndDateForStep() == null ? 0 : this.getActualEndDateForStep().hashCode() );
         result = 37 * result + ( getWorkFlowName() == null ? 0 : this.getWorkFlowName().hashCode() );
         result = 37 * result + ( getWofcstepDetailsId() == null ? 0 : this.getWofcstepDetailsId().hashCode() );
         result = 37 * result + ( getSubActivityFlowChartDefId() == null ? 0 : this.getSubActivityFlowChartDefId().hashCode() );
         result = 37 * result + ( getWorkFlowVersion() == null ? 0 : this.getWorkFlowVersion().hashCode() );
         result = 37 * result + ( getReason() == null ? 0 : this.getReason().hashCode() );
         result = 37 * result + ( getOutputLink() == null ? 0 : this.getOutputLink().hashCode() );
         result = 37 * result + ( getParallelcount() == null ? 0 : this.getParallelcount().hashCode() );
         result = 37 * result + ( getUploadedOn() == null ? 0 : this.getUploadedOn().hashCode() );
         return result;
   }

}
