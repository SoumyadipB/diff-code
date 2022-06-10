package com.ericsson.isf.mqttclient.util;


public interface AppConstants {

	public static final String SECRET = "db3OIsj+BXE9NZDy0t8W3TcNekrF+2d/1sFnWG4HnV8TZY30iTOdtVWJG8abWvB1GlOgJuQZdcF2Luqm/hccMw==";
	
	public static final String DOUBLE_SLASH="//"; 
	public static final String SLASH="/";
	
	public static final String PROVIDE_VALID="Please provide valid %s !";
	public static final String APP_MESSAGE="app.message";
	public static final String INVALID_AUTHORIZATION_HEADER="Invalid Authorization header.";
	public static final String SIGNUM_ERROR_MESSAGE = "Signum in Header doesn't match with Payload Signum";
	public static final String PLEASE_PROVIDE_VALUE = "Please provide valid %s !";
	public static final String NULL_BLANK_SIGNUM = "Signum should not be null or blank.";
	
	public static final String ISF="ISF";
	public static final String DATASOURCE_URL="spring.datasource.url";

	
	public static final String WO_STATUS_DEFERRED = "DEFERRED";
	public static final String WO_STATUS_REJECTED = "REJECTED";
	public static final String STATUS_PLANNED = "PLANNED";
	public static final String EXECUTION_TYPE = "ExecutionType";
	public static final String STEPID = "StepID ";
	public static final String TASK_ID = "TaskID ";
	public static String CLOSED = "CLOSED";
	public static String PROJECT_ID = "ProjectID ";
	public static String VERSION_NUMBER = "Version Number ";
	public static String FLOWCHART_DEFID = "flowChartDefID ";
	public static final String SIGNALR_EXECUTION_TYPE = "signalR";
	public static final String UPDATE_FLOATING_WINDOW="updateFloatingWindow";
	public static final String CALL_SIGNALR_API="signalrService/sendSignalrMessage";
	
	public enum VALID_WO_STATUS {DEFERRED,INPROGRESS,ONHOLD,CLOSED,ASSIGNED,PLANNED,REOPENED,COMPLETED,REJECTED}
	
}
