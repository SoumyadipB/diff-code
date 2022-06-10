package com.ericsson.isf.util;

/**
 * This class contains all keys of properties file as constants.
 * 
 * @author eakinhm
 *
 */
public class ConfigurationFilesConstants {

	/* app.properties constants starts */

	// Erisite
	public static final String ERISITE_SAPBO_BASEFOLDER = "erisite.sapbo.basefolder";
	public static final String ERISITE_REVERSE_URL = "erisite.reverse.url";
	public static final String ERISITE_HEADERS_CONSUMERINTERFACEID = "erisite.headers.consumerinterfaceid";
	public static final String ERISITE_HEADERS_PROVIDERINTERFACEID = "erisite.headers.providerinterfaceid";
	public static final String ERISITE_AUTHENTICATION_USERNAME = "erisite.authentication.username";
	public static final String ERISITE_AUTHENTICATION_PASSWORD = "erisite.authentication.password";
	// Erisite WO Closure
	public static final String ERISITE_HEADERS_WOCLOSURE_RECIEVERID = "erisite.3.woclosure.receiverid";

	public static final String ERISITE_HEADERS_WOCLOSURE_INTERFACEID = "erisite.3.woclosure.interfaceid";
	public static final String ERISITE_HEADERS_WOCLOSURE_METHOD = "erisite.3.woclosure.method";
	public static final String ERISITE_HEADERS_WOCLOSURE_DOC_TYPE = "erisite.3.woclosure.documenttype";
	public static final String ERISITE_HEADERS_WOCLOSURE_DOC_NAME_SPACE = "erisite.3.woclosure.documentnamespace";
	public static final String ERISITE_HEADERS_WOCLOSURE_PARTY_ID = "erisite.3.woclosure.partyid";
	public static final String ERISITE_HEADERS_WOCLOSURE_PARTY_TYPE = "erisite.3.woclosure.partytype";
	public static final String ERISITE_HEADERS_WOCLOSURE_MESSAGE_FLOW = "erisite.3.woclosure.messageflow";
	public static final String ERISITE_HEADERS_WOCLOSURE_BUSINESS_ENTITY_TYPE = "erisite.3.woclosure.businessentitytype";
	public static final String ERISITE_HEADERS_WOCLOSURE_MESSAGE_SERVICE_NAME = "erisite.3.woclosure.message.service.name";
	public static final String ERISITE_HEADERS_WOCLOSURE_MESSAGE_REQUEST_TYPE = "erisite.3.woclosure.message.request.type";
	public static final String ERISITE_HEADERS_WOCLOSURE_MESSAGE_SERVICE_TYPE = "erisite.3.woclosure.message.service.type";
	public static final String ERISITE_HEADERS_WOCLOSURE_CONTENT_TYPE = "erisite.3.woclosure.content.type";

	// azure properties
	public static final String AZURE_FUNCTION_BASE_URL = "azure.function.base.url";
	// signalR properties
	public static final String TIME_SLEEP = "signalr.time.sleep";
	
	public static final String IS_ERISITE_UP = "erisite.up.status";
	
	public static final String SIGNALR_CLIENT_URL = "signalR.client.url";
}
