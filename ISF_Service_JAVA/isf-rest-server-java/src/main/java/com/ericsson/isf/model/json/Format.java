package com.ericsson.isf.model.json;

public class Format {
	public static String jsonCondition="{\"SOURCE_PORT\":[MATCHER_VALUE]}";
	public static String jsonMatch="{\r\n" + 
			"  \"TARGET_PORT\": [\r\n" + 
			"    \"MATCH_MESSAGE\",\r\n" + 
			"    {\r\n" + 
			"      \"var\": \"MATCH_ATTR\"\r\n" + 
			"    }\r\n" + 
			"  ]\r\n" + 
			"}";
	public static String jsonMatches="{\"SOURCE_PORT\":[JSON_MATCH_ARR]}";
	public static String jsonNested="{\r\n" + 
			"  \"nested_level\": [\r\n" + 
			"    {\r\n" + 
			"      \"operator\": \"OPERATOR_TYPE\",\r\n" + 
			"      \"depth\": TEXT_VAL\r\n" + 
			"    },\r\n" + 
			"    {\r\n" + 
			"      \"var\": \"VAR_PATH\"\r\n" + 
			"    }\r\n" + 
			"  ]\r\n" + 
			"}";
	public static String jsonWhiteConnector="{\"WHITE_CONNECTOR\":[KEYSTROKE_KEYEVENT]}";
}
