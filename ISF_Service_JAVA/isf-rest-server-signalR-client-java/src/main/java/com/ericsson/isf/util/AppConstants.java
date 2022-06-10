package com.ericsson.isf.util;

import java.util.HashMap;
import java.util.Map;

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
	public static final String smtp_port = "25";
	public static final String smtp_host = "153.88.115.39";

	// errors
	public static final int EMAIL_NO_ERROR = -1;
	public static final int EMAIL_ERROR_ATTACHEMENT = 1;
	public static final int EMAIL_ERROR_NO_PROCESS_ERROR = 2;
	public static final int EMAIL_ERROR_MEETING_NOT_PROCESSED = 3;

	// Date Time related Constants
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String DEMAND_DATE_FORMAT = "MMM yyyy";
	public static final String UI_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
	public static final String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
	public static final String TIMEZONE_IST = "Asia/Kolkata";

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
	public static String STATUS_PLANNED = "PLANNED";
	public static String STATUS = "Status";
	public static String STATUS_STRING = "status";
	public static String SKIPPED = "SKIPPED";
	public static String ONHOLD = "ONHOLD";
	public static String INPROGRESS = "INPROGRESS";

	// Tmp Table Constants
	public static final String NETWORK_ELEMENTS_TEMP_TABLE_NAME = "NetworkElementsTmp";
	public static final String ITM_TRAINING_TEMP_TABLE_NAME = "ITMTemp";
	public static final String COMPETENCY_TEMP_TABLE_NAME = "CompetencyTemp";
	public static final String MASTER_DATA_TEMP_TABLE_NAME = "MasterDataTemp";

	// constants related to auditing
	public static final String FIELD_WORK_ORDER_ID = "WORK_ORDER_ID";
	public static final String FIELD_STEP_ID = "FIELD_STEP_ID";

	// audit actions
	public static final String WORK_ORDER_START = "WORK_ORDER_START";

	// Audit group prefixes
	public static final String AUDIT_PREFIX_WORK_ORDER = "WORK_ORDER";
	public static final String AUDIT_PREFIX_DEMAND_SUMMARY = "DEMAND_SUMMARY";

	// audit messages
	@SuppressWarnings("serial")
	public static final Map<String, String> AUDIT_MSGS = new HashMap<String, String>() {
		{
			put("DEFAULT", "{filedName} changed from {oldValue} to {newValue}");
			put("MSG_FIELDNAME_NEWVALUE", "{filedName} changed to {newValue}");
		}
	};
	public static final String AUDIT_TYPE_USER_COMMENT = "USER_COMMENT";
	public static final String AUDIT_TYPE_AUDIT = "AUDIT";

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

	// HTTP Request Constants
	public static final String APPLICATION_JSON = "application/json";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String ACCEPT = "Accept";
	public static final String X_SAP_LOGON_TOKEN = "X-SAP-LogonToken";
	public static final String TEXT_PLAIN = "text/plain";

	// BOT Related Constants
	public static final String BOT_CONFIG_TYPE_BOT = "BOT";
	public static final String BOT_CONFIG_TYPE_WO_STEP = "WO_STEP";
	public static final String NEW_BOT_REQUEST = "New_BOT_Request";
	public static final String BOT_PLATFORM_SERVER = "SERVER";
	public static final String SOFT_HUMAN = "softhuman";
	public static final String LOCAL = "local";
	public static final String AUTOMATIC = "Automatic";
	public static final String MANUAL = "Manual";

	// External Source Name
	public static final String WEB = "WEB";
	public static final String ISF = "ISF";
	public static final String GNET = "GNET";
	public static final String EMS = "EMS";

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
	public static final String MSG = "msg";
	public static final String NA = "NA";
	public static final String FAILED = "Failed";
	public static final String FAILURE = "Failure";
	public static final String SAVING_DEMAND_REQUEST = "Saving demand request";
	public static final String AUTO = "AUTO";
	public static final String DEFAULT_TIME_OUT = "10";

}
