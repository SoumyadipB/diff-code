package com.ericsson.isf.model.json;

public class Scanner {
	
	private String id;
	private String port;
	private String event_module;         //app_scanner
	private String scanner_combinator;	 //and
	private String[] valid_apps; 	//[Microsoft Word]  // it is type array in JSON
	private String type;
	private String typeName;
	private String fs_valid_app;
	private String email_valid_app;
	
	// Additional var for AppScanner
	private String customApp;
//	private boolean triggerOnClose;
	private String triggerOnClose="False";
	
	// Additional var for FileScanner
	private String customPath;
	private boolean lookForSubfolder;
	
	// Additional var for EmailScanner
	private String lookForPastEmail="";
	private String look_back_time="";
//	private String look_back;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getEvent_module() {
		return event_module;
	}
	public void setEvent_module(String event_module) {
		this.event_module = event_module;
	}
	public String getScanner_combinator() {
		return scanner_combinator;
	}
	public void setScanner_combinator(String scanner_combinator) {
		this.scanner_combinator = scanner_combinator;
	}
//	public String getValid_apps() {
//		return valid_apps;
//	}
//	public void setValid_apps(String valid_apps) {
//		this.valid_apps = valid_apps;
//	}
	public String getCustomApp() {
		return customApp;
	}
	public void setCustomApp(String customApp) {
		this.customApp = customApp;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getCustomPath() {
		return customPath;
	}
	public void setCustomPath(String customPath) {
		this.customPath = customPath;
	}
	public boolean isLookForSubfolder() {
		return lookForSubfolder;
	}
	public void setLookForSubfolder(boolean lookForSubfolder) {
		this.lookForSubfolder = lookForSubfolder;
	}
//	public boolean isLookForPastEmail() {
//		return lookForPastEmail;
//	}
//	public void setLookForPastEmail(boolean lookForPastEmail) {
//		this.lookForPastEmail = lookForPastEmail;
//	}
//	public String getTimeField() {
//		return timeField;
//	}
//	public void setTimeField(String timeField) {
//		this.timeField = timeField;
//	}
//	public boolean isTriggerOnClose() {
//		return triggerOnClose;
//	}
//	public void setTriggerOnClose(boolean triggerOnClose) {
//		this.triggerOnClose = triggerOnClose;
//	}
	public String getLook_back_time() {
		return look_back_time;
	}
	public void setLook_back_time(String look_back_time) {
		this.look_back_time = look_back_time;
	}
//	public String getLook_back() {
//		return look_back;
//	}
//	public void setLook_back(String look_back) {
//		this.look_back = look_back;
//	}
	public String getLookForPastEmail() {
		return lookForPastEmail;
	}
	public void setLookForPastEmail(String lookForPastEmail) {
		this.lookForPastEmail = lookForPastEmail;
	}
	public String getTriggerOnClose() {
		return triggerOnClose;
	}
	public void setTriggerOnClose(String triggerOnClose) {
		this.triggerOnClose = triggerOnClose;
	}
	public String[] getValid_apps() {
		return valid_apps;
	}
	public void setValid_apps(String[] valid_apps) {
		this.valid_apps = valid_apps;
	}
	public String getFs_valid_app() {
		return fs_valid_app;
	}
	public void setFs_valid_app(String fs_valid_app) {
		this.fs_valid_app = fs_valid_app;
	}
	public String getEmail_valid_app() {
		return email_valid_app;
	}
	public void setEmail_valid_app(String email_valid_app) {
		this.email_valid_app = email_valid_app;
	}
	
	
	
	
	
	
	
}
