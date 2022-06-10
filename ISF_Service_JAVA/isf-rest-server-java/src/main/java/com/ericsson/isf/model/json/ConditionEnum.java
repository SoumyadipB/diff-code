package com.ericsson.isf.model.json;

public class ConditionEnum {

	public static String CONDITION_STRING = "{\"PARENT_PORT\":CHILD_MATCH}";
	
	public static String CHILD_MATCH = "{\"OPERATOR\":[\"MATCH_VALUE\",{\"var\":\"MATCH_ATTR\"}]}";
	
	public static String KEY_MATCH = "{\"KEY_MATCH\":[KEY_VALUE_MATCH,{\"var\":\"INPUT_BUFFER\"}]}";
	
	public static String NESTED_LEVEL = "{\"NESTED_LEVEL\":[{\"operator\":\"OPERATOR_VALUE\",\"depth\":DEPTH_VALUE},{\"var\":\"PATH\"}]}";
	
}
