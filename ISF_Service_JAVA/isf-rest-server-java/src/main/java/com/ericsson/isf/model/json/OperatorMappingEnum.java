package com.ericsson.isf.model.json;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

public enum OperatorMappingEnum {

	CONTAINS("contains", "in", "neid_in", "market_in"),

	CONTAINS_CASE_INSENSITIVE("containscaseinsensitive", "in_i", "neid_in_i", "market_in_i"),

	EQUAL_CASE_INSENSITIVE("equalscaseinsensitive", "===", "neid_eq_i", "market_eq_i"),

	EQUAL("equals", "==", "neid_eq", "market_eq"),

	NOT_IN("notin", "not_in", "neid_not_in", "market_not_in"),

	NOT_IN_CASE_INSENSITIVE("notincaseinsensitive", "not_in_i", "neid_not_in_i", "market_not_in_i"),

	NOT_EQUALS("notequals", "!=", "neid_not_eq", "market_not_eq"),

	NOT_EQUALS_CASE_INSENSITIVE("notequalscaseinsensitive", "!==", "neid_not_eq_i", "market_not_eq_i"),

	GREATER_THAN("greaterthan", ">", ">", ">"),

	LESSER_THAN("lesserthan", "<", "<", "<"),

	GREATER_THAN_EQUAL("greaterthanequals", ">=", ">=", ">="),

	LESSER_THAN_EQUAL("lesserthanequals", "<=", "<=", "<="),

	BLANK("blank", "", "", "");

	private String uiOperator;
	private String apiOperator;
	private String neidOperator;
	private String marketOperator;

	OperatorMappingEnum(final String uiOperator, final String apiOperator, final String neidOperator,
			final String marketOperator) {
		this.uiOperator = uiOperator;
		this.apiOperator = apiOperator;
		this.neidOperator = neidOperator;
		this.marketOperator = marketOperator;
	}

	public String getUIOperator() {
		return uiOperator;
	}

	public String getAPIOperator() {
		return apiOperator;
	}

	public String getNeidOperator() {
		return neidOperator;
	}

	public String getMarketOperator() {
		return marketOperator;
	}

	public static String getAPIOperator(final String uiOperator) {
		OperatorMappingEnum operatorMappingEnum = Arrays.stream(OperatorMappingEnum.values())
				.filter(operator -> operator.getUIOperator().equalsIgnoreCase(uiOperator)).findFirst().orElse(BLANK);
		return operatorMappingEnum.getAPIOperator();
	}

	public static String getOperatorByDynamicValue(final String uiOperator, final String dynamicValue) {

		if (StringUtils.equalsIgnoreCase("@nodes@", dynamicValue)) {

			OperatorMappingEnum operatorMappingEnum = Arrays.stream(OperatorMappingEnum.values())
					.filter(operator -> operator.getUIOperator().equalsIgnoreCase(uiOperator)).findFirst()
					.orElse(BLANK);
			return operatorMappingEnum.getNeidOperator();

		} else if (StringUtils.equalsIgnoreCase("@market@", dynamicValue)) {

			OperatorMappingEnum operatorMappingEnum = Arrays.stream(OperatorMappingEnum.values())
					.filter(operator -> operator.getUIOperator().equalsIgnoreCase(uiOperator)).findFirst()
					.orElse(BLANK);
			return operatorMappingEnum.getMarketOperator();

		} else {

			OperatorMappingEnum operatorMappingEnum = Arrays.stream(OperatorMappingEnum.values())
					.filter(operator -> operator.getUIOperator().equalsIgnoreCase(uiOperator)).findFirst()
					.orElse(BLANK);
			return operatorMappingEnum.getAPIOperator();
		}
	}

}
