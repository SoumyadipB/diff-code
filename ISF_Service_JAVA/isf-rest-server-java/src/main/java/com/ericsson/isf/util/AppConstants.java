package com.ericsson.isf.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public interface AppConstants {

	// Mail Related Constants
	public static final String CURRENT_USER = "currentUser";
	public static final String CURRENT_USER_NAME = "currentUsername";
	public static final String NOTIFICATION_TYPE_PROPOSE = "PROPOSE_RESOURCES";
	public static final String NOTIFICATION_TYPE_ACCPET_PROPOSE = "ACCEPT_PROPOSE";
	public static final String NOTIFICATION_TYPE_REJECT_PROPOSE = "REJECT_PROPOSE";
	public static final String NOTIFICATION_TYPE_RAISE_CR = "RAISE_CR";
	public static final String NOTIFICATION_TYPE_ACCEPT_CR = "ACCEPT_CR";
	public static final String NOTIFICATION_TYPE_REJECT_CR = "REJECT_CR";
	public static final String NOTIFICATION_TYPE_CREATE_DR = "CREATE_DR";
	public static final String NOTIFICATION_TYPE_CREATE_DR_SPM = "CREATE_RPM";
	public static final String NOTIFICATION_TYPE_CREATE_DR_RPM = "CREATE_SPM_TO_RPM";
	public static final String NOTIFICATION_TYPE_CREATE_DR_PM = "CREATE_RPM_TO_SPM";
	public static final String NOTIFICATION_RESIGN_RESOURCE = "RESIGN";
	public static final String NOTIFICATION_ADD_COMMENT = "NOTIFICATION_ADD_COMMENT";
	public static final String NOTIFICATION_CREATE_WORKORDER = "CREATE_WORKORDER";
	public static final String NOTIFICATION_MASS_UP_WO = "WO_MASS_UPDATE";
	public static final String NOTIFICATION_TYPE_FEEDBACK_STATUS = "FEEDBACK_STATUS_CHANGED";
	public static final String CUSTOM_NOTIFICATIONS_CC = "CUSTOM_NOTIFICATIONS_CC";
	public static final String CUSTOM_NOTIFICATIONS_TO = "CUSTOM_NOTIFICATIONS_TO";
	public static final String ISF_ASP_REGITER = "ISF ASP REGITER";
	public static final String ISF_ASP_MANAGER_RESPONSE = "ISF ASP MANAGER RESPONSE";
	public static final String ISF_ASP_FORGOT_PASSWORD = "ISF_ASP_FORGOT_PASSWORD";
	public static final String ISF_ASP_APPROVE_REJECT = "ISF_ASP_APPROVE_REJECT";
	public static final String ISF_ASP_REVOKED = "ISF_ASP_REVOKE";
	public static final String ISF_ASP_EXTENDED = "ISF_ASP_EXTENDED";
	public static final String ISF_ACCESS_REQUEST_APPROVE = "ISF_ACCESS_APPROVE";
	public static final String ISF_ACCESS_REQUEST_REJECT = "ISF_ACCESS_REJECT";
	public static final String PROFILE_ACCESS_REQUEST = "PROFILE_ACCESS_REQUEST";
	public static final String ISF_ACCESS_REVOKE = "ISF_ACCESS_REVOKE";
	public static final String ISF_ACCESS_RENEW = "ISF_ACCESS_RENEW";
	public static final String ISF_WO_TRANSFER = "ISF_WO_TRANSFER";
	public static final String NOTIFICATION_CREATE_WORKFLOW = "CREATE_WORKFLOW";
	public static final String NOTIFICATION_UPDATE_WORKFLOW = "UPDATE_WORKFLOW";
	public static final String ISF_BOT_SUCCESS = "ISF_BOT_SUCCESS";
	public static final String NOTIFICATION_URL_PATH = "pageLink";
	public static final String NOTIFICATION_TYPE_RAISE_CR_PM = "RAISE_CR_PM";
	public static final String NOTIFICATION_ID_BULK_WO_EMAIL = "BULK_WO_EMAIL";
	public static final String NOTIFICATION_SUB_BULK_WO_EMAIL = "Bulk work order creation request";
	public static final String NOTIFICATION_ID_NOT_PROCESSED_EMAIL = "NOT_PROCESSED_EMAIL";
	public static final String NOTIFICATION_SUB_NOT_PROCESSED_EMAIL = "Email not processed by ISF";
	public static final String NOTIFICATION_ID_NOT_PROCESSED_MEETING = "NOT_PROCESSED_MEETING";
	public static final String NOTIFICATION_SUB_NOT_PROCESSED_MEETING = "Meeting not processed by ISF";
	public String NOTIFICATION_EXTERNAL_REFERENCE_TOKEN_CREATION = "New External Reference creation Notification";
	public String EXTERNAL_REFERENCE_TOKEN_CREATION = "EXTERNAL_REFERENCE_TOKEN_CREATION";
	public static final String ISF_COMPETENCE_REQUEST = "ISF_COMPETENCE_REQUEST";// sent to manager
	public static final String ISF_COMPETENCE_INITIATE_DELETE = "ISF_COMPETENCE_INITIATE_DELETE";// initiate,delete
	public static final String ISF_COMPETENCE_APPROVE_REJECT = "ISF_COMPETENCE_APPROVE_REJECT";// approve,reject
	public static final String ISF_COMPETENCE_EDIT = "ISF_COMPETENCE_EDIT";
	public static final String MAIL_API_TOKEN_ERROR = "MAIL_API_TOKEN_ERROR";
	public static final String MESSAGE_PROCESS_ERROR = "MESSAGE_PROCESS_ERROR";
	public static final String GENERIC_TEMPLATE_WITH_TABLE_BODY = "GENERIC_TEMPLATE_WITH_TABLE_BODY";
	public static final String GENERIC_TEMPLATE_WITHOUT_TABLE_BODY = "GENERIC_TEMPLATE_WITHOUT_TABLE_BODY";
	public static final String BULK_WO_MAIL_UPLOADED_BY = "BULK_WO_MAIL_UPLOADED_BY";
	public static final String smtp_port = "25";
	public static final String smtp_host = "153.88.115.39";
	public static final String SUBJECT = "subject";
	public static final String GENERIC_MAIL_MODEL = "genericMailModel";

	// errors
	public static final int EMAIL_NO_ERROR = -1;
	public static final int EMAIL_ERROR_ATTACHEMENT = 1;
	public static final int EMAIL_ERROR_NO_PROCESS_ERROR = 2;
	public static final int EMAIL_ERROR_MEETING_NOT_PROCESSED = 3;
	public static final String GLOBAL_EXCEPTION="Global Exception Occured: %s";

	// Date Time related Constants
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String DEFAULT_TIME_FORMAT="hh:mm:ss";
	public static final String DEMAND_DATE_FORMAT = "MMM yyyy";
	public static final String UI_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
	public static final String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
	public static final String TIMEZONE_IST = "Asia/Kolkata";
	public static final String MAIL_DATE_FORMAT ="dd-MM-yyyy HH:mm";
	public static final String NOT_VALID_DATE_FORMAT = "Please provide valid start/end date in yyyy-MM-dd format !!";

	// Date Range Constants
	public static final String RANGE_TODAY = "TODAY";
	public static final String RANGE_THIS_WEEK = "THIS WEEK";
	public static final String RANGE_THIS_MONTH= "THIS MONTH";
	
	// Special Characters Constants
	public static final String CSV_CHAR = "\\t";
	public static final String CSV_CHAR_DOUBLE_SEMICOLON = ",";
	public static final String CSV_CHAR_COMMA = ",";
	public static final String CSV_CHAR_PIPE = "\\|";
	public static final String SINGLE_QUOTE = "'";
	public static final String SEMICOLON = ";";
	public static final String UNDERSCORE = "_";
	public static final String CHAR_HYPHEN = "-";
	public static final String COLON = ":";
	public static final String ASTERISK = "*";
	public static final String EQUAL = "=";

	public static final String CHAR_AT="@";
	public static final String FORWARD_SLASH = "/";
	public static final String FORWARD_SLASH_WITH_SPACE = " / ";
	public static final String FORWARD_DOUBLE_SLASH="//"; 
	public static final String BACKWARD_DOUBLE_SLASH_DOT="\\."; 
	public static final String SQUARE_OPEN_BRACKET="[";
	public static final String SQUARE_CLOSE_BRACKET="]";
	public static final String COMMA_DOUBLE_QUOTE = "','";
	public static final String COMMA_SINGLE_QUOTE = "',";
	public static final String COMMA_DOLLER=",$";
	public static final String OPENING_BRACES_WITH_SPACE=" (";
	public static final String OPENING_BRACES="(";
	public static final String CLOSING_BRACES=")";
	public static final String CLOSING_BRACES_WITH_COLON_SPACE="): ";
	public static final String PIPE_WITH_SPACE="  |  ";
	public static final String BACKWARD_DOUBLE_SLASH_SPLUS="\\s+"; 
	public static final String CHAR_ZERO="0";

	// ISF related different status Constants
	public static final String BOOKING_TYPE_QUEUED = "QUEUED";
	public static final String BOOKING = "BOOKING";
	public static final String BOOKING_TYPE_FORCE_STOP = "FORCE_STOP";
	public static final String STARTED = "STARTED";
	public static final String COMPLETED = "COMPLETED";
	public static final String BOOKING_REASON_PARALLEL = "PARALLEL BOOKING";
	public static final String PROPOSAL_PENDING_STATUS = "Proposal Pending";
	public static final String PROPOSAL_ALLOCATED_STATUS = "Resource Allocated";
	public static final String PROPOSAL_PORPOSED_STATUS = "Proposed";
	public static final String PROPOSAL_DEPLOYED_STATUS = "Deployed";
	public static final String DELETE_STATUS = "DELETED";
	public static String WO_STATUS_REPOENED = "REOPENED";
	public static String WO_STATUS_ASSIGNED = "ASSIGNED";
	public static String CLOSED = "CLOSED";
	public static String WO_STATUS_DEFERRED = "DEFERRED";
	public static String WO_STATUS_REJECTED = "REJECTED";
	public static String STATUS_PLANNED = "PLANNED";
	public static String STATUS = "Status";
	public static String STATUS_STRING = "status";
	public static String SKIPPED = "SKIPPED";
	public static final String ONHOLD = "ONHOLD";
	public static final String INPROGRESS = "INPROGRESS";
	public static String ERISITE_STATUS="Pending Predecessors";
	public static String STOPPED = "STOPPED";
	
	public static String NOT_FEASIBLE = "NOT FEASIBLE";

	// Tmp Table Constants
	public static final String NETWORK_ELEMENTS_TEMP_TABLE_NAME = "NetworkElementsTmp";
	public static final String ITM_TRAINING_TEMP_TABLE_NAME = "ITMTemp";
	public static final String COMPETENCY_TEMP_TABLE_NAME = "CompetencyTemp";
	public static final String MASTER_DATA_TEMP_TABLE_NAME = "MasterDataTemp";
	public static final String MSS_LEAVE_UPLOAD_TABLE_NAME = "MSSLeaveTemp";
	public static final String CNEDB_UPLOAD_TEMPLATE_TABLE_NAME = "NE_Upload_Template";

	// constants related to auditing
	public static final String FIELD_WORK_ORDER_ID = "WORK_ORDER_ID";
	public static final String FIED_SERVICE_REQUEST="SERVICE_REQUEST";
	public static final String FIELD_STEP_ID = "FIELD_STEP_ID";
	public static final String ACTOT_TYPE_SP="Network Engineer";
	public static final String ACTOT_TYPE_DR="Delivery Responsible";
	public static final String ACTOT_TYPE_PM="Project Manager";
	public static final String MSG_FIELDNAME_NEWVALUE = "MSG_FIELDNAME_NEWVALUE";

	// audit actions
	public static final String WORK_ORDER_START = "WORK_ORDER_START";
	public static final String WO_CREATE_PLAN = "WO_CREATE_PLAN";
	public static final String WO_TRANSFER = "WO_TRANSFER";
	public static final String WO_PRIORITY_EDIT = "WO_PRIORITY_EDIT";
	public static final String WO_PRIORITY_COMMENT = "Editing Priority";
	public static final String SR_COMMENT_CATEGORY="CREATE_SERVICE_REQUEST";
	
	
	public static final String WO_TRANSFERRED = "TRANSFERRED";

	// Audit group prefixes
	public static final String AUDIT_PREFIX_WORK_ORDER = "WORK_ORDER";
	public static final String AUDIT_PREFIX_DEMAND_SUMMARY = "DEMAND_SUMMARY";

	// audit messages
//	@SuppressWarnings("serial")
//	public static final Map<String, String> AUDIT_MSGS = new HashMap<String, String>() {
//		{
//			put("DEFAULT", "{filedName} changed from {oldValue} to {newValue}");
//			put("MSG_FIELDNAME_NEWVALUE", "{filedName} changed to {newValue}");
//		}
//	};
	
	public static final String AUDIT_TYPE_USER_COMMENT = "USER_COMMENT";
	public static final String AUDIT_TYPE_AUDIT = "AUDIT";
	public static final String AUDIT_TYPE_DEFAULT = "DEFAULT";
	public static final String TO_EMAIL_DEFAULTS = "tomail@test.com";
	public static final String CC_EMAIL_DEFAULTS = "ccemail@test.com";

	public static final String SECRET = "db3OIsj+BXE9NZDy0t8W3TcNekrF+2d/1sFnWG4HnV8TZY30iTOdtVWJG8abWvB1GlOgJuQZdcF2Luqm/hccMw==";

	// SAP BO Related Constants
	public static final String SAPBOTMP_TABLE_5COLUMNS = "SAPBOTmp5";
	public static final String SAPBOTMP_TABLE_11COLUMNS = "SAPBOTmp11";
	public static final int NUMBER_5 = 5;
	public static final int NUMBER_11 = 11;
	public static final String SAP_BO_SUCCESS_RESPONSE = "Monitor Sap BO API Response : Success ";
	public static final String SAP_BO_FAILURE_RESPONSE = "Monitor Sap BO API Response : Failure ";
	public static final int SAP_BO_RESPONSE_CODE = 200;
	public static final String SAP_BO_FAILURE = "SAP_BO_FAILURE";
	public static final String REPORTS = "reports";
	public static final String REPORT = "report";
	public static final String ID = "id";
	public static final String ENTRIES = "entries";
	public static final String LOGON_TOKEN = "logonToken";
	public static final String SCHEDULES = "schedules";
	public static final String SCHEDULE = "schedule";
	public static final String PARAMETERS = "parameters";
	public static final String PARAMETER = "parameter";
	public static final String ANSWER = "answer";
	public static final String VALUES = "values";
	public static final String VALUE = "value";
	public static final String AT_RATE_ID = "@id";
	public static final String DOLLAR = "$";
	public static final String AMERICAS = "americas";
	public static final String EMEA = "emea";
	public static final String APAC = "apac";

	// HTTP Request Constants
	public static final String APPLICATION_JSON = "application/json";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String ACCEPT = "Accept";
	public static final String X_SAP_LOGON_TOKEN = "X-SAP-LogonToken";
	public static final String TEXT_PLAIN = "text/plain";
	public static final String TEXT_CSV = "text/csv";
	public static final String APPLICATION_XML = "application/xml";

	// BOT Related Constants
	public static final String BOT_CONFIG_TYPE_BOT = "BOT";
	public static final String BOT_CONFIG_TYPE_WO_STEP = "WO_STEP";
	public static final String NEW_BOT_REQUEST = "New_BOT_Request";
	public static final String BOT_PLATFORM_SERVER = "SERVER";
	public static final String SOFT_HUMAN = "softhuman";
	public static final String LOCAL = "local";
	public static final String AUTOMATIC = "Automatic";
	public static final String MANUAL = "Manual";
	public static final String MANUAL_DISABLED = "ManualDisabled";
	public static final String DISABLED_MANUAL = "Disabled Manual";

	// External Source Name
	public static final String WEB = "WEB";
	public static final String ISF = "ISF";
	public static final String GNET = "GNET";
	public static final String EMS = "EMS";
	public static final String ERISITE = "ERISITE";
	public static final String UI = "UI";
	public static final String ISF_DESKTOP = "isfdesktop";
	public static final String VOICE = "VOICE";
	public static final String DESKTOP = "ISFDESKTOP";
	public static final String AUTO_SENSE ="AUTO-SENSE";
	// Other Constants
	public static final String PROJECT_ID_2 = "ProjectID";
	public static final String PROJECT_ID = "projectid";
	public static final String SITE_COL = "site";
	public static final String CR_ACTION_TYPE = "actionType";
	public static final String SUCCESS = "Success";
	public static final String HOUR = "hour";
	public static final String ERROR = "Error";
	public static final String AUTOMATIC_ERROR = "Automatic_Error";
	public static final String ISF_SUPPORT_TEAM = "vaibhav.kumar.singh@ericsson.com;gourav.debi.prasad.mishra@ericsson.com;sudhanshu.bhukmaria@ericsson.com;tushar.kundu@ericsson.com;sandeep.c.singh@ericsson.com;suman@ericsson.com;nitish.tandon@ericsson.com;prateek.ranjan@ericsson.com";
	public static final String PAGE_LINK_STRING = "pageLink";
	public static final String LIVE = "LIVE";
	public static final String NORMAL = "NORMAL";
	public static final String MSG = "msg";
	public static final String NA = "NA";
	public static final String FAILED = "Failed";
	public static final String FAILURE = "Failure";
	public static final String SAVING_DEMAND_REQUEST = "Saving demand request";
	public static final String AUTO = "AUTO";
	public static final String DEFAULT_TIME_OUT = "10";
	public static final String DELETE = "delete";
	public static final String UPDATE = "update";
	public static final String UPDATED = "Updated";
	public static final String DELIVERY_FAILURE="DeliveryFailure";
	public static final String REASON="reason";
	//public static final String HTTP_URL_VALIDATION_REGEX ="((https)://)?[a-zA-Z0-9]\\w*(\\.\\w+)+(/\\w*(\\.\\w+)*)*(\\?.+)*";
	public static final String HTTP_URL_VALIDATION_REGEX ="^https?:\\/\\/[a-zA-Z0-9]([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z].?([a-z]+)(.*)";
	public static final String HTTP_SECOND_URL_VALIDATION_REGEX = "(https://){1}(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])(.*)";
	public static final String ALPHA_NUMBERIC_REGEX = "[^a-zA-Z0-9]";
	//public static final String SHARED_FOLDER_REGEX = "\\\\{1}[a-zA-Z0-9].*";
	public static final String SHARED_FOLDER_REGEX = "\\\\\\\\{1}[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9](?:\\.[a-zA-Z]{2,})(.*)";
	public static final String PRIORITY = "Priority";
	public static final String ALL = "all";
	//public static final String FTP_URL_VALIDATION_REGEX = "(ftp://){1}[a-zA-z0-9./]+";
	public static final String FTP_URL_VALIDATION_REGEX = "(ftp://){1}(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])(.*)";
	public static final String TOGGLE = "toggle";
	public static final String RESULT = "result";
	public static final String MESSAGE = "message";
	public static final String SIGNALR_EXECUTION_TYPE = "signalR";
	public static final String CALL_DEV_ENVIRONMENT ="SignalRUrl";
	public static final String CALL_SIGNALR_API="signalrService/sendSignalrMessage";
	public static final String EXECUTE_FLAG  = "executeFlag";
	public static final String NO = "NO";
	public static final String YES = "YES";
	public static final String IS_SUCCESS = "isSuccess";
	public static final String AZURE_API_VERSION = "2019-12-01";
	public static final String PLEASE_PROVIDE ="Please provide %s";
	public static final String API_BEGIN ="BEGIN %s";
//	public static final String VOICE = "IVA";
//	public static final String DESKTOP = "ISFDesktop";
	public static final String PAGELINK = "pageLink";
	public static final String WOID ="woid";
	public static final String REQUEST ="request";
	public static final String CONTEXT ="context";
	public static final String WORK_ORDER ="WorkOrder";
	public static final String STEP_NAME_WITH_COLON ="Step Name:";
	public static final String NULL_AS_A_STRING ="NULL";
	public static final String ROLE ="role";

	
	public static final String FEEDBACK_ON_WF  = "DEL_EXE_WF";
	public static final String FEEDBACK_TYPE_WF  = "ONWF_FEEDBACK_FORM";
	public static final String FEEDBACK_ON_STEP  = "DEL_EXE_ONSTEP";
	public static final String FEEDBACK_TYPE_STEP  = "ONSTEP_FEEDBACK_FORM";
	public static final String FEEDBACK_TYPE_STEP_SAD_COUNT  = "ONSTEP_SAD_COUNT";
	public static final String ONHOLD_WITHOUTSPACE  = "ONHOLD";
	public static final String ONHOLD_WITH_SPACE  = "ON HOLD";
	public static final String SUPER_BOTS = "superbots";
	public static final String OUTPUT_FILES = "outputfiles";
	public static final String BOT_CONF_FILES = "BotConfFiles";
	public static final String SERVER_BOT_INPUTS = "serverbotinputs";
	public static final String NO_DETAILS_EXISTS = "No details exists";
	public static final String AUTO_SENSE_RULE_ADDED_SUCCESSFULLY = "Rule Added Successfully";
	public static final String TASK_ID = "Task ID";
	public static final String RULE_ID = "Rule ID";
	public static final String RULE_JSON = "Parse Rule Json";
	public static final String TASK_ACTION_NAME = "Task Action Name";
	public static final String TEMP_AUTOSENSE_RULEID = "Temp Auto Sense Rule ID";
	public static final String AUTO_SENSE_RULE_DELETED_SUCCESSFULLY = "Auto Sense Rule Deleted Successfully. ";
	public static final String DETAILS_ALREADY_EXIST = "Details already Exist!";
	public static final String SUBACTIVITY_FLOWCHART_DEFID = "subactivity flowchart def ID.";
	public static final String RULE_TYPE = "Rule Type";
	public static final String STOP = "STOP";
	public static final String START = "START";
	public static final String UPDATE_EXISTING = "updateexisting";
	public static final String PLANNED = "planned";
	public static final String MANUAL_STEP_CATEGORY ="DeliveryExecution";
	public static final String AUTOMATIC_STEP_CATEGORY = "Automatic_Forced_stop";
	public static final String FIRST_START_PREVIOUS_STEP = "Please firstly start the Previous Step !";
	public static final String ERROR_STEP_MSG = "Can not start the step. Please add the decision step first!";
	

	public static final String EXTENSION_XLSX = ".xlsx";
	public static final String ROLE_ISF_ACCESS_MANAGER = "ISF Access Manager";
	public static final String RECORD_TOTAL = "recordsTotal";
	public static final String RECORD_FILTERED = "recordsFiltered";
	public static final String DATA_IN_RESPONSE = "data";
	public static final String DRAW = "draw";
	
	
	//WorkFlow Constants
	public static final String PROJECTDEFINED="PROJECTDEFINED";
	public static final String TASK="task";
	public static final String TASKID="taskID";
	public static final String TYPE="type";
	public static final String TASK_NAME="taskName";
	public static final String SAD_COUNT="sadCount";
	public static final String EXECUTIONTYPE="executionType";
	public static final String EXEC_TYPE="execType";
	public static final String NAME="name";
	public static final String APP_LINK="app.Link";
	public static final String ERICSSON_MANUAL="ericsson.Manual";
	public static final String ERICSSON_AUTOMATIC="ericsson.Automatic";
	public static final String ERICSSON_DECISION="ericsson.Decision";
	public static final String ERICSSON_STEP="app.ericssonStep";
	public static final String ERICSSON_WEAK_ENTITY="app.ericssonWeakEntity";
	public static final String ERD_RELATIONSHIPS="erd.RelationShip";
//	public static final String BOOKING_ID="BookingID";
	public static final String BOOKINGID="bookingID";
	public static final String FLOW_CHART_STEP_ID="FlowChartStepID";
	public static final String FLOW_CHART_DEF_ID="FlowChartDefID";
	public static final String UNDERSCORE_EXPERT="_Expert";
	public static final String DECISION_VALUE="DecisionValue";
	public static final String TOOL_ID="ToolID";
	public static final String TOOLID="toolID";
	public static final String TOOL="tool";
	public static final String TOOL_NAME="ToolName";
	public static final String TOOLNAME="toolName";
	public static final String RPA_ID="RpaID";
	public static final String RPAID="RPAID";
	public static final String RPAID_SMALL="rpaid";
	public static final String IS_RUN_ON_SERVER="isRunOnServer";
	public static final String IS_INPUT_REQUIRED="isInputRequired";
	public static final String OUTPUT_LINK="OutputLink";
	public static final String OUTPUTLINK="outputLink";
	public static final String OUTPUT_UPLOAD="outputUpload";
	public static final String WOI_D="WOID";
	public static final String REASON_SMALL="reason";
	public static final String BOOKING_TYPE="bookingType";
	public static final String HOURS="Hours";
	public static final String CELLS="cells";
	public static final String NO_TOOL="NoTool";
	public static final String TOOL_NAME_COLON="Tool Name:";
	public static final String TWO_AS_A_STRING="2";
	public static final String SMALL_NULL_AS_A_STRING="null";
	public static final String BODY_TEXT="bodyText";
	public static final String BODY="body";
	public static final String LABEL="label";
	public static final String TEXT="text";
	public static final String RECT="rect";
	public static final String FILL="fill";
	public static final String RPA="RPA";
	public static final String ISF_REF_ID="isfRefId";
	public static final String SUCCESS_CAPS="SUCCESS";
	public static final String STARTED_COLOR_CODE="#7C68FC";
	public static final String ONHOLD_COLOR_CODE="#E5E500";
	public static final String SKIPPED_COLOR_CODE="#FFAB73";
	public static final String DEFERRED_COLOR_CODE="#8B0000";
	public static final String COMPLETED_COLOR_CODE="#FFFFFF";
	public static final String OTHER_COLOR_CODE="#008000";
	public static final String REG_ZERO_AT="0@0";
	public static final String AVG_ESTD_EFFORT="AvgEstdEffort";
	
	
	//SignalR related constants
	public static final String UPDATE_FLOATING_WINDOW="UpdateFloatingWindow";
	public static final String UPDATE_WO_STATUS_WEB_FW="UpdateWorkOrderStatusOnWebAndFW";
	public static final String UPDATE_FLOATING_WINDOW_SCANNER_START="refreshFloatingWindowOnSensingStart";
	public static final String UPDATE_FLOATING_WINDOW_SCANNER_STOP="refreshFloatingWindowOnSensingStop";
	public static final String DECSION_VALUE="SetDecisionValue";
	 
	
	public static final String BOT_APPROVAL_PAGE = "BOTAPPROVALVIEW";
	public static final String SIGNUM = "signum";
	public static final String START_TYPE = "ericsson.StartStep";
	public static final String END_TYPE = "ericsson.EndStep";
	public static final String AND = "and";
	public static final String OR = "or";
	public static final String STEP_ID = "Step Id";
	public static final String NOTIFICATION_PURPOSE = "notification purpose";
	public static final String NOTIFICATION_FROM = "notification from";
	public static final String NOTIFICATION_TO = "notification to";
	public static final String NOTIFICATION_ID = "notificationId";
	
	//User Profiles
	public static final String BOT_DEVELOPER="Bot Developer";
	public static final String ACCESS_ADMIN="Access Admin";
	
	public static final String TEST_SIGNALR_API="signalrService/testSignalR";
	public static final String CALL_SEND_REQUEST_TO_EXTERNAL_SOURCE="eventPublisher/sendRequestToExternalSources";

	public static final String CALL_SIGNALR_CLIENT = "eventPublisher/callSiganlRClient";
	public static final String LOG_ID = "log Id";
	
	public static final String INSTRUCTION_ON_WF = "WF_INSTRUCTION";
	public static final String INSTRUCTION_ON_STEP = "STEP_INSTRUCTION";
	
	public static final String SEND_BACK_TO_PM_Implemented = "Send Back to PM (I)";
	public static final String SEND_BACK_TO_PM_NOT_FEASIBLE = "Send Back to PM (NF)";
	public static final String ACCEPTED = "Accepted";
	
	
	//AccessManagmet relate constants 
	public static final String LOGIN="login";
	public static final String FLAG="flag";
	public static final String LOGID="logID";
	public static final String LOGOUT="logout";
	
	
	public static final String SENDER_NAME="senderName";
	public static final String WO_ID="workOrderID";
	public static final String NE_ID="neID";
	public static final String RECIEVER_NAME="receiverName";
	public static final String RECIEVER_ID="receiverID";
	public static final String SENDER_ID="senderID";
	public static final String PAGE_LINK_MANAGER="pageLinkManager";
	public static final String PAGE_LINK_NE="pageLinkNE";
	public static final String ENVIRONMENT="environment";
	
	
	public static final String FLOWCHART_TYPE="flowChartType";
	public static final String PLAY_NEXT="playNext";
	public static final String START_STEP_FIRST="Please Start Step First!";
	public static final String STEP_ALREADY_COMPLETED="Step is already completed";
	
	public static final String MANUAL_STEP_STOP_SUCCESS="Manual Step Stopped!!";
	public static final String MANUAL_DISABLED_STEP_STOP_SUCCESS="Manual Disabled Step Stopped!!";
	public static final String BOT_QUEUED_STATE="Bot in queued state!!";
	public static final String STEP_STOPPED="Step Stopped!!";
	
	public static final String NOT_CLOSED_SUCCESSFULL="Task not Closed Successful. Current Step Type was in Booking State and reason is not Successfull";
	public static final String CLOSED_SUCCESSFULL="Task Closure Successful. Current Step Type was in Booking State!!";
	
	public static final String ALREADY_EXECUTING="Bot is already executing. It is not recommended to stop it in between. Please wait!!!";
	
	
	public static final String EXECUTION_TYPE=	"ExecutionType";
	
	public static final String TASK_SIGNUM_ERROR="This task is not assigned to You!";
	
	public static final String BOOKING_ID="bookingID";
	 
	public static final String SERVER_BOT_START_SUCCESS="Server BOT started successfully.";
	
	public static final String SERVER_BOT_START_FAILURE="There is some server connectivity issue now, please try later OR you may run your BOT on Local.";
	
	
	public static final String RUN_SERVER_BOT_ENDPOINT="/server-bot-app-java-Bot/serverBotExecutor/runBot";
	public static final String TRUE_STRING = "true";

	public static final String SIGNUM_ID = "signum ID";
	
	public static final String STEP_ALREADY_STARTED_ERROR_STRING = "A Step is already started for WOID : ";

	public static final String WO_UNASSIGNED_ERROR = "Given Work Order is unassigned!";
	
	public static final String STEP_STARTED = "Step Started";
	public static final String WO_CLOSED_ERROR = "Work order is closed!";
	public static final String WO_STARTED_ERROR = "Work order is already Started Please Force Stop It and Execute it again!!";
	public static final String WOID_ERROR = "Work Order is either Rejected, Deferred or already Closed, Kindly provide the correct WOID";
	public static final String WO_SIGNUM_ERROR = "Work Order is not assigned to signum ";
	public static final String WO_PLANNED_STATE_ERROR = "Work Order is in Planned state, Kindly provide the correct WOID";
	
	public static final String FLOWCHART_DEFID = "FlowChartDefID";
	
	public static final String PARALLEL_COUNT = "parallelCount";
	public static final String START_TASK_ERROR = "Unable to start task!";
	public static final String INVALID_FOR_STEP_STRING =  " is invalid for Step ID ";
	public static final String AND_WOID_STRING =  " and woID ";
	public static final String COMPLETE_PREV_TASK_ERROR=  "Please complete previous task/step or mark decision yes/no.";
	public static final String BOTNEST=  "BOTNEST";

	public static final String DEPLOYED_ENV_KEY="BaseUrl";
	
	//
	public static final String WO_UNASSIGNED = "Work Order Not Assigned Yet";
	public static final String TASK_INPROGRESS_ERROR = "Task Inprogress, Work Order cannot be closed! Please complete all task before closing Work Order";
	public static final String WO_ALREADY_CLOSED_ERROR = "Work Order is already Closed";
	public static final String REASON_NULL_ERROR = "Reason is mandatory, when Delivery Status = Failed/Success";
	public static final String OTHERS = "Others";
	public static final String COMMENT_MANDATORY_ERROR = "Comment is mandatory, when Delivery Status = Success and Reason = Others ";
	public static final String STEP_FAILED_ERROR = "Automatic Step failed/Force Stopped, Work Order cannot be closed with success! Please complete/skip all task before closing Work Order";
	public static final String UTF_8_STRING = "UTF-8";
	public static final String WO_CLOSED_SUCCESSFULLY = "Work Order closed successfully !!";
	public static final String STRING_PATTERN = "(.*?)_.*?$";
	public static final String EXTERNAL_GROUP = "externalGroup";
	public static final String WO_COMPLETED = "woCompleted";
	public static final String STRING_PATTERN_MATCH = "(.*)_.*?_.*?_.*?$";
	public static final String WO_CLOSURE = "WO_CLOSURE";
	public static final String API_RESPONSE = "API RESPONSE";
	public static final String SYSTEM_RECORD_ID = "SYSTEM_RECORD_ID";
	public static final String SYSTEM_RECORD_ID_KEY = "SystemRecordId";
	public static final String RECORD_NAME	=	"RECORD_NAME";
	public static final String RECORD_NAME_KEY	=	"RecordName";
	public static final String PARENT_PROJECT_INTEGRATED_SCOPING	=	"PARENT_PROJECT_INTEGRATED_SCOPING";
	public static final String ENSURE_ID	=	"enduserid";
	public static final String PARENT_PROJECT_INTEGRATED_SCOPING_KEY	=	"ParentProjectIntegratedScoping";
	public static final String PARENT_WORK_PLAN_ID_KEY	=	"ParentWorkPlanId";
	public static final String ASSIGNED_GROUP	=	"ASSIGNED_GROUP";
	public static final String ASSIGNED_GROUP_KEY	=	"AssignedGroup";
	public static final String PARENT_WORK_PLAN_RECORD_ID_KEY	=	"ParentWorkPlanRecordID";
	public static final String FORECASTSTARTDATE	=	"FORECASTSTARTDATE";
	public static final String ACTUAL_START_DATE_UTC	=	"ActualStartDateUTC";
	
	public static final String ACTUALENDDATE	=	"ACTUALENDDATE";
	public static final String ACTUALENDDATE_UTC	=	"ActualEndDateUTC";
	public static final String XML_STRING	=	"XML String";
	
	public static final String NE_ROLE	=	"Default User";
	public static final CharSequence ADMIN = "Application Admin";
	public static final String ADMIN_PROFILE_NAME	=	"Admin";
	
	//BotStoreService. stopInprogressBOT specific constants
	public static final int CHAR_ONE=1;
	public static final String INPROGRESS_BOT="INPROGRESS BOT";
	public static final String CH_ZERO="0.0";
	public static final String NULL=null;
	public static final String WF_PROFICIENCY_ERROR="Please provide valid wfID/subActivityID/Signum";
	
	public static final String WF_PROFICIENCY="Please provide WfId_WfName/Signum";
	
	public static final String WORKFLOW_ID="WorkFlowId";
	public static final String NOTIFICATION_UPDATE_PROFICIENCY = "UPDATE_PROFICIENCY";
	public static final String NOTIFICATION_SAVE_PROFICIENCY = "SAVE_WF_PROFICIENCY";
	public static final String SAVE_PROFICIENCY_WITHOUT_TRANSITION = "PROFICIENCY SAVED SUCCESSFULLY , but not changed";
	
	public static final String PROFICIENCY = "Proficiency";
	
	public static final String PREV_PROFICIENCY = "Prev_Proficiency";
	
	public static final String ERROR_SAVE_PROFICIENCY = "Error while saving proficiency";
	
	public static final String WORKFLOW="WorkFlow";
	
	
	public static final String ERICSSON_MANUAL_DISABLED = "ericsson.ManualDisabled";
	public static final String MANUAL_DISABLED_COLOR_CODE = "#9D9D9D";
	public static final String DISPLAY_MODE = "DisplayMode";
	public static final String STEP_NAME = "stepName";
	public static final String USER_COMMENTS = "userComments";
	public static final String EXPERIENCED = "Experienced";
	public static final String UPGRADED = "Upgraded";
	public static final String ASSESSED = "Assessed";
	public static final String DEGRADED = "Degraded";
	
	public static final String DETAILS_KEY = "details";
	public static final String TRANSITION = "transition";
	
	public static final String OPENING_SQAURE_BRACES = "[";
	public static final String CLOSING_SQAURE_BRACES = "]";
	public static final String CREATE_NEW =  "CreateNew";
	public static final String TRIGGER_BY = "triggerBy";
	
	public static final int ASSESSED_PID = 21;
	public static final int EXPERIENCED_PID = 22;
	
	public static final String  SAME_WORKFLOWNAME = "Same WorkFlowName";
	public static final String MODIFIED_BY = "ModifiedBy";
	public static final String SUBACTIVITY_ID = "subactivityID";
	public static final String SUBACTIVITY = "subactivity";
	public static final String ERROR_MSG= "ErrorMsg";
	public static final String ERROR_FLAG= "ErrorFlag";
	public static final String ERROR_DATA= "ErrorData";
	public static final String STROKE = "stroke";
	public static final String VIEW_MODE = "viewMode";
	public static final String QUALIFY_COUNT = "Qualify Count";
	public static final String WORKFLOW_NAME = "workflowName";
	public static final String NO_DATA_FOUND = "No Data Found!";
	public static final String FORWARD = "forward";
	public static final String REVERSE = "reverse";
	
	// AccessManagment API's specific constants
	public static final String START_DATE="StartDate";
	public static final String END_DATE="EndDate";
	public static final String DELETE_ACCESS_PROFILE_SUCCESS="Delete Access Profile: Success";
	public static final String DISPLAY_NAME="displayName";
	public static final String THUMBNAILPHOTO="thumbnailphoto";
	public static final String ISF123="isf@123";
	public static final String DC_ERICSSON="DC=ericsson,DC=se";
	public static final String DEVELOPER_PORTAL="developerPortal";
	public static final String EXCEPTION="Exception thrown {}";
	public static final String ISUPLOADERROR="isUploadError";
	public static final String FALSE="false";
	public static final String ISSUCCESS="isSuccess";
	public static final String PROFILE="profile";
	public static final String TIMEZONE="timezone";
	public static final String MANAGER_NAME="managerName";
	public static final String API_NAME="apiName";
	public static final String USER_LOCATION_SAVED="user location saved";
	public static final String USER_LOCATION_NOT_SAVED="user location not saved Successfully";
	public static final String DATE_PATTERN="yyyy-MM-dd";
	public static final String INSERT_TBL_USER_PREFERENCES="insert into transactionalData.TBL_User_Preferences ";
	public static final String USER_SIGNUM="( UserSignum, ";
	public static final String CREATED_BY="CreatedBy, ";
	public static final String CREATED_ON="CreatedOn, ";
	public static final String DEFAULT_ID="DefaultId, ";
	public static final String DEFAULT_NAME="DefaultName, ";
	public static final String DEFAULT_VALUE="DefaultValue, ";
	public static final String QUERY_VALUES="VALUES ";
	public static final String IS_ACTIVE="IsActive ";
	public static final String NOW=":now, ";
	public static final String SIGNUM_NOT_FOUND="The signumID specified does not exists!!!";
	public static final String ROLEID_AND_CAPABILITY_EXISTS="RoleID and CapabilityPageID combination already exists:: {} ";
	public static final String ROLEID_AND_CAPABILITY_UPDATED="Role Capability Updated Successfully.";
	public static final String ERROR_IN_UPDATING_CAPABILITIES="Error while Updating Capability: {}";
	
	public static final String EMAIL = "emailId";
	public static final String ERICSSON_EMPLOYEE = "ericsson";
	public static final String EMPLOYEE_TYPE = "employeeType";
	public static final String TWENTY_FOUR_HOURS_TIME_FORMAT="kk:mm:ss";
	//ActivityMaster API's related constants
	public static final String TBL_DOMAIN_LIST_SUCCESS="TBL_DOMAIN: DomainList: SUCCESS";
	public static final String TBL_SERVICEAREA_SUCCESS="TBL_SERVICEAREA: Success";
	public static final String TBL_TECHNOLOGY_SUCCESS="TBL_TECHNOLOGY: Success";
	public static final String TBL_WF_WORK_INSTRUCTION_SUCCESS="TBL_WF_WORK_INSTRUCTION:WORK LIST: SUCCESS";
	public static final String DATE_FORMAT="yyyy-MM-dd_HH-mm-ss";
	public static final String XLSX=".xlsx";
	public static final String DATEFORMAT="yyyy-MM-dd";
	public static final String URL_LINK="UrlLink";
	public static final String URL_NAME="UrlName";
	public static final String SRID = "srid";
	public static final String ASSIGNED = "ASSIGNED";
	public static final String REOPENED = "REOPENED";
	
	
	//AdhocActivity API's related constants
	public static final String ERROR_CREATING_ADHOC_BOOKING="Error in Creating Adhoc Booking: %s {}";
	
	//OutlookAndEmailService related constants
	public static final String ERROR_IN_SENDSMTPMAIL="error in sendSmtpMail {}";
	
	
	public static final String KEY="yAQgEyPoHNHvS8OQAfWiFovhweHXQwi650MTmYxObfE=";
	// Auditing API's related constants 
	public static final String COMMENT_DATA ="commentsData";
	
    // Autosense API's related constants
	public static final String GOT_AN_ERROR_WHILE_CONVERTING_RULE ="Got an Error while converting rule, Please try again!!";
	public static final String NEXT_STEP_TYPE="nextStepType";
	public static final String NEXT_STEP_ID="NextStepID";
	public static final String PTRN_POSITION="%POSITION%";
	public static final String POSITION="position";
	public static final String PROPERTIES="properties";
	public static final String CONTENT="content";
	public static final String PTRN_APP="%APP%";
	public static final String PTRN_CUSTOMAPPS="%CUSTOMAPPS%";
	public static final String PTRN_ATTRIBUTE="%ATTRIBUTE%";
	public static final String ATTRIBUTE="attribute";
	public static final String PTRN_OPERATOR="%OPERATOR%";
	public static final String OPERATOR="operator";
	public static final String PTRN_MATCH="%MATCH%";
	public static final String MATCH="match";
	public static final String PTRN_OPERATOR_CHILDREN="%OPERATOR_CHILDREN%";
	public static final String PTRN_MODE="%MODE%";
	public static final String REFERENCE="reference";
	public static final String FILE_OPERATION="FILE_OPERATION";
	public static final String NESTED_LEVEL="nested_level";
	public static final String FS_CONDITIONAL="fs_conditional";
	public static final String PTRN_TEXT_CONTENT="%TEXT_CONTENT%";
	public static final String SCAN_TYPE="scanType";
	public static final String ATTRIBUTES="attributes";
	public static final String PTRN_VALUE="%VALUE%";
	public static final String PTRN_ATTRS_NAME="%ATTRS_NAME%";
	public static final String OPERATORS="operators";
	public static final String OPNAME="opName";
	public static final String BOT_ID = "botID";
	public static final String FAILURE2 = "FAILURE";
	public static final String RECORDS_TOTAL = "recordsTotal";
	public static final String RECORDS_FILTERED = "recordsFiltered";
	public static final String SOURCE_ID = "sourceId";
	public static final String LATESTDESTOPVERSION = "_LatestDesktopVersion";
	public static final String EXTERNAL_PROJECT_ID = "externalProjectID";
	public static final String WORK_PLAN_TEMPLATE_NAME = "workPlanTemplateName";
	
	public static final String TBL_TOOL_INVENTORY  = "TBL_TOOL_INVENTORY : SUCCESS";
	public static final String TBL_EXTERNAL_ACTIVITY_SUBACTIVITY_MAPPING = "TBL_EXTERNAL_ACTIVITY_SUBACTIVITY_MAPPING : SUCCESS";
	public static final String LENGTH_EXCEPTION = " should be maximum 200 characters!";
	public static final String LENGTH_BORDER = " should be maximum 100 characters!";
	public static final String BAND = "band";
	public static final String SECTOR = "sector";
	public static final String SOFTWARE_RELEASE = "software release";
	public static final String UPLOADED_BY = "UploadedBy";
	public static final String LONGITUDE_LENGTH = " should be maximum 256 characters!";
	public static final String COUNTRY_CUSTOMER_ID = "countryCustomerGroupId";
	public static final String DASH = "-";
	
	public static final String CALL_MQTT_API="mqttManagement/publishNotification";
	public static final String REFERENCE_ID = "referenceId";
	public static final String NETWORK_ELEMENT_UPLOAD = "NETWORK_ELEMENT_UPLOAD";
	public static final String NOTIFICATION = "Notification";
	
	public static final String WORK_ORDER_PLAN_HAS_BEEN_CREATED_SUCCESSFULLY = "Work order plan has been created successfully!";
	public static final String NETWORK = "NETWORK";
	public static final String CLUSTER = "CLUSTER";
	
	public static final String ERROR_CREATING_RPA_REQUEST  ="Error while Creating RPA Request: {}";
	public static final String ERROR_CREATING_RPA_REQUEST_DOUBLE = "Error in creating RPA request:: ";
	public static final String EXE_ERROR = "The exeFile did not unzip correctly.";
	public static final String WHL_ERROR = "Please provide  WHL file  correctly.";
	public static final String RPA_REQUEST_CREATION = "RPA Request Created Successfully. Request id is : {}";
	public static final String EXE_FILE_ERROR = "The exeFile did not unzip correctly.";
	public static final String  REQUEST_SUCCESSFUL = "Request submitted Successfully";
	public static final String  ERROR_BOTSTAGE= "Error while Creating BOT Stage: {}";
	public static final String  ERROR_SUBMITTING_BOT = "Error in submitting Bot:: ";
	
	// subscribe isf feed related constants
	public static final String NULLV="[null]";
	public static final String REPLACE_FORMAT="'', ''";
	public static final String BLANK_VAL="''";
	public static final String UUNDERSCORE="__";
	public static final String DOT=".";
	public static final String TRANSACTIONALDATA="transactionaldata.";
	public static final String REFDATA="refdata.";
	public static final String STRINGPROJECTID="StringProjectID";
	public static final String NOTBLANK="'TEST'";
	public static final String REPLACE_FORMAT2="', '";
	public static final String TIMESTAMP_CALCULATIVE_COL="[TRANSACTIONALDATA].[TBL_AUDITDETAILS].[DATECREATED]";
	public static final String AS=" AS ";
	public static final String TIMESTAMP="'time_stamp'";
	public static final String TIME_STAMP="time_stamp";
	public static final String SUCCESS_RESULT="Result=1";
	public static final String PROJECTID_MSG = "ProjectID: ";
	public static final String CONVERT_VARCHAR="convert(varchar(50),";
	
	public static final String TBL_WORK_ORDER_PLANNEDSTARTDATE="[transactionaldata].[TBL_WORK_ORDER].[PlannedStartDate]";
	public static final String TBL_WORK_ORDER_PLANNEDENDDATE="[transactionaldata].[TBL_WORK_ORDER].[PlannedEndDate]";
	public static final String TBL_WORK_ORDER_ACTUALSTARTDATE="[transactionaldata].[TBL_WORK_ORDER].[ActualStartDate]";
	public static final String TBL_WORK_ORDER_ACTUALENDDATE="[transactionaldata].[TBL_WORK_ORDER].[ActualEndDate]";
	public static final String TBL_WORK_ORDER_CREATED_DATE="[transactionaldata].[TBL_WORK_ORDER].[CreatedDate]";
	public static final String TBL_WORK_ORDER_LASTMODIFIEDDATE="[transactionaldata].[TBL_WORK_ORDER].[LastModifiedDate]";
	public static final String TBL_WORK_ORDER_PLAN_STARTTIME="[transactionaldata].[TBL_WORK_ORDER_PLAN].[StartTime]";
	public static final String TBL_WORK_ORDER_PLAN_STARTDATE="[transactionaldata].[TBL_WORK_ORDER_PLAN].[StartDate]";
	public static final String TBL_WORK_ORDER_PLAN_LASTMODIFIEDDATE="[transactionaldata].[TBL_WORK_ORDER_PLAN].[LastModifiedDate]";
	public static final String TBL_AUDITDETAILS_DATECREATED="[transactionaldata].[TBL_AuditDetails].[dateCreated]";
	
	
	// Demand Related constants
	public static final String DEMAND_EVENTS="DEMAND_EVENTS";
	public static final String DEMAND_EVENT_WORK_EFFORT="DEMAND_EVENT_WORK_EFFORT";
	public static final String DEMAND_EVENT_RESOURCE_POSITION="DEMAND_EVENT_RESOURCE_POSITION";
	public static final String DEMAND_EVENT_CHANGE_REQUEST="DEMAND_EVENT_CHANGE_REQUEST";
	public static final String DEMAND_EVENT_RESOURCE_REQUEST="DEMAND_EVENT_RESOURCE_REQUEST";
	public static final String FULFILLMENT_MANAGER="Fulfillment Manager";
	public static final String PROJECT_MANAGER="Project Manager";
	
	public static final String POSITION_STATUS="Position Status";
	public static final String PROPOSAL_PENDING="Proposal Pending";
	public static final String REJECTED_NO_CAPACITY="Rejected_NO_CAPACITY";
	public static final String REJECTED_NO_COMPETENCE="Rejected_NO_COMPETENCE";
	public static final String PROPOSED="Proposed";
	public static final String RESOURCE_ALLOCATED="Resource Allocated";
	public static final String RESOURCE_DELETED="Deleted";
	public static final String AUTO_APPROVED="Auto Approved";
	
	
	public static final String AUDIT="AUDIT";
	
	public static final String RESOURCE_PROPOSAL_DATE_BY_FM="ResourceProposalDateByFM";
	public static final String RESOURCE_REJECTION_DATE_BY_FM="ResourceRejectionDateByFM";
	public static final String RESOURCE_PROPOSAL_ACCEPTANCE_DATE_BY_PM="ResourceProposalAcceptanceDateByPM";
	public static final String RESOURCE_PROPOSAL_REJECTION_DATE_BY_PM="ResourceProposalRejectionDateByPM";
	public static final String PROPOSAL_PENDING_RESOURCE_DELETION_BY_PM="ProposalPendingResourceDeletionDateByPM";
	public static final String PROPOSAL_PENDING_DATE_CHANGE_CR_DATE_BY_PM="ProposalPendingDateChangeCRDateByPM";
	public static final String RESOURCE_ALLOCATED_DATE_PREPONDED_CR_DATE_BY_PM="ResourceAllocatedDatePreponedCRDateByPM";
	public static final String RESOURCE_ALLOCATED_DATE_EXTEND_CR_DATE_BY_PM="ResourceAllocatedDateExtendCRDateByPM";
	public static final String RESOURCE_ALLOCATED_RESOURCE_CHANGE_CR_DATE_BY_FM="ResourceAllocatedResourceChangeCRDateByFM";
	public static final String RESOURCE_ALLOCATED_DATE_EXTEND_CR_APPROVAL_DATE_BY_FM="ResourceAllocatedDateExtendCRApprovalDateByFM";
	public static final String RESOURCE_CHANGE_CR_APPROVAL_DATE_BY_PM="ResourceChangeCRApprovalDateByPM";
	public static final String RESOURCE_ALLOCATED_DATE_EXTEND_CR_REJECTION_DATE_BY_FM="ResourceAllocatedDateExtendCRRejectionDateByFM";
	public static final String RESOURCE_CHANGE_CR_REJECTION_DATE_BY_PM="ResourceChangeCRRejectionDateByPM";
	public static final String PROPOSED_RESOURCE_CANCEL_DATE_BY_FM="ProposedResourceCancelDateByFM";
	public static final String CR_WITHDRAWAL_DATE_BY_PM="CRWithdrawalDateByPM";
	public static final String CALL_AMQP_API="rabbitmq/amqpPublisher";
	
	
} 
