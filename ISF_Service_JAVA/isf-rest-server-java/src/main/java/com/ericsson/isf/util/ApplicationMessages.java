package com.ericsson.isf.util;

/**
 * @author edhhklu
 * Class for defining error messages and application messages
 */
public class ApplicationMessages {
	
	
	//*******************************************Error Messages*************************************//
	public static final String SUCCESS_BCP_FILE_UPLOAD="File uploaded successfully";
	
	
	//*******************************************Error Messages*************************************//
	public static final String ERROR_BCP_FILE_UPLOAD="Error occured while uploading file to database.";
	public static final String ERROR_NETWORK_ELEMENTS_HEADER="No of columns in row and header are not matching";
	
	public static final String INVALID_NODENAME_NODETYPE = "Node name or Node type given is invalid, please provide valid node name and node type.";
	public static final String INVALID_SOURCE_NAME = "Enter a valid source name.";
	public static final String INVALID_REFERER_NAME = "Enter a valid referrer name.";
	public static final String INVALID_PRIORITY_NAME = "Enter a valid priority.";
	public static final String INVALID_DATE_BY_CURRENTDATE = "Given date and time should not be less than today's date and time.";
	public static final String INVALID_DATE_FORMAT = "Enter a valid date ; Accepted date format is ";
	public static final String INVALID_TIME_24HOUR_PATTERN = "Enter a valid time in 24 hour format.";
	public static final String INVALID_DELIVERABLE_PLAN_NAME = "Enter a valid deliverable plan name.";
	public static final String INVALID_DATE_TIME_FORMAT = "Enter a valid time & date; Accepted format is ";
	public static final String INVALID_NODENAME = "Enter a valid nodeName.";
	public static final String INACTIVE_PROJECTID = "Project id is inactive or closed or rejected, please provide valid project id.";
	public static final String INVALID_PROJECTID = "Enter a valid Project id.";
	public static final String INVALID_DELIVERABLE_PLAN_ID = "Enter a valid Deliverable Plan id.";
	public static final String INVALID_NODETYPE = "Enter a valid nodeType.";
	public static final String INVALID_SIGNUM = "Signum is not Valid,Please enter valid Signum";
	public static final String INVALID_WO_NAME = "Work Order Name is Mandatory,Can not be Null/Blank";
	public static final String EMPTY_DATE = "Enter valid date.";
	public static final String INACTIVE_DELIVERABLE_PLAN = "Deliverable plan is inactive, please provide valid Deliverable plan.";
	public static final String INACTIVE_STATUS_DELIVERABLE_PLAN = "Deliverable status is Inactive, please provide valid Deliverable plan with status New or Active.";

	//*******************************************Steps related Messages*************************************//
	public static final String STEP_ALREADY_COMPLETED = "Step is already completed";
	public static final String COMPLETED_WITH_ERROR = "Automatic Step not Completed Successfully !!";
	public static final String STEP_STOPPED = "Step Stoped";
	public static final String INVALID_ENTITY = " is not a valid url.";
	public static final String EMPTY_ENTITY = "UrlName and urlLink can not be empty.";
	public static final String INVALID_GLOBAL_ID = "Enter a valid global Id and action type.";
	public static final String ALREADY_PRESENT_ENTITY = "UrlLink or UrlName already present in Global Url.";
	public static final String URLNAME_URLLINK_BOTH_PRESENT = "UrlName and UrlLink already present in Global Url.";
	public static final String INVALID_HEADER = "Please provide valid signum and role in header request.";
	public static final String INVALID_LOCAL_ID = "Enter a valid local Id. ";
	public static final String ALREADY_PRESENT_LOCAL_ENTITY = "UrlLink already present in Local Url.";
	public static final String ALREADY_PRESENT_GLOBAL_ENTITY = "UrlLink already present in Global Url.";
	public static final String ALREADY_PRESENT_LOCAL_NAME_URL = "UrlName or urlLink  already present in Local Url.";
	public static final String INVALID_INPUT_URL = "  is not valid url.";
	public static final String INVALID_ENTITY_UPDATE = "Invalid Entity. ";
	public static final String ALREADY_PRESENT_LOCAL_NAME = "UrlName already present in Local Url.";
	public static final String ALREADY_PRESENT_GLOBAL_NAME = "UrlName already present in Global Url.";
	public static final String ALREADY_PRESENT_LOCAL_ENTITY_AND_LINK = "UrlLink is not valid  or UrlLink already present in Local/Global Url.";
	public static final String INVALID_WO_ID = "Enter a valid workOrder Id.";
	public static final String INVALID_TASKID = "Enter a valid Task id.";
	public static final String INVALID_STEPID = "Enter a valid Step id.";
	public static final String INVALID_FEEDBACK_TYPE = "feedback type can not be blank.";
	public static final String FEEDBACK_SAVE_MESSAGE = "WorkFlow Feedback Comments saved Successfully.";
	public static final String FEEDBACK_SAVE_ERROR_MESSAGE = "WorkFlow Feedback Comments saved unSuccessful.";
	public static final String AUTO_SENSE_RULE_SAVE_ERROR_MESSAGE = "Auto Sense Rule saved UnSuccessful.";
	public static final String AUTO_SENSE_RULE_EDIT_ERROR_MESSAGE = "Auto Sense Rule edit UnSuccessful.";
	public static final String BULK_MSS_LEAVE_UPLOAD_ERROR = "Error in uploaded files";
	public static final String URL_AND_NAME_LENGTH_ERROR = "URL length should not exceed 500 characters, URL Name should not exceed 50 characters";
	}
