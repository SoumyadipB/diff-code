package com.ericsson.isf.model.json;

public class Match {
	private String id;
	private String port;
	private String header;
	private String matcher_name;	//"welcome";
	private String var;      		// It is AttributeType ex: Window Title, Email inbox, Subject inbox (INBOX_SUBJECT)
	private String operator;
	private String type;
	private String typeName;
	private String message;
	private boolean isKeyStroke;
	private boolean iskeyEvent;
	private String matchattr;
	private String attributeType; // IN CASE OF SIMPLE MATCHER, KEYSTROKE HAVE different type
	private String staticData=""; // special case of KeyStroke
	private String matchType;
	private String dynamicField;
	private String defaultValueForDynamic;
	
	public String getMatcher_name() {
		return matcher_name;
	}
	public void setMatcher_name(String matcher_name) {
		this.matcher_name = matcher_name;
	}
	public String getVar() {
		return var;
	}
	public void setVar(String var) {
		this.var = var;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
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
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isKeyStroke() {
		return isKeyStroke;
	}
	public void setKeyStroke(boolean isKeyStroke) {
		this.isKeyStroke = isKeyStroke;
	}
	public String getMatchattr() {
		return matchattr;
	}
	public void setMatchattr(String matchattr) {
		this.matchattr = matchattr;
	}
	public String getAttributeType() {
		return attributeType;
	}
	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}
	public String getStaticData() {
		return staticData;
	}
	public void setStaticData(String staticData) {
		this.staticData = staticData;
	}
	public String getMatchType() {
		return matchType;
	}
	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}
	public String getDynamicField() {
		return dynamicField;
	}
	public void setDynamicField(String dynamicField) {
		this.dynamicField = dynamicField;
	}
	public boolean isIskeyEvent() {
		return iskeyEvent;
	}
	public void setIskeyEvent(boolean iskeyEvent) {
		this.iskeyEvent = iskeyEvent;
	}
	public String getDefaultValueForDynamic() {
		return defaultValueForDynamic;
	}
	public void setDefaultValueForDynamic(String defaultValueForDynamic) {
		this.defaultValueForDynamic = defaultValueForDynamic;
	}

}
