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

	//*******************************************Steps related Messages*************************************//
	public static final String STEP_ALREADY_COMPLETED = "Step is already completed";
	public static final String COMPLETED_WITH_ERROR = "Automatic Step not Completed Successfully !!";
	public static final String STEP_STOPPED = "Step Stoped";
	
	 

}
