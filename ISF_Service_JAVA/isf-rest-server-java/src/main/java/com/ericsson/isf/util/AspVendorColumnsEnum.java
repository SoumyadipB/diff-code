package com.ericsson.isf.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public enum AspVendorColumnsEnum {

	VENDOR_CODE("vendorCode"),
	COUNTRY("country"),
	VENDOR_NAME("vendorName"),
	VENDOR_CONTACT_DETAILS("vendorContactDetails"),
	MANAGER_SIGNUM("managerSignum");

	private String columnName;
	
	AspVendorColumnsEnum(final String columnName){
		this.columnName = columnName;
	}
	
	public String getValue() {
		return columnName;
	}
	
	public static List<String> getAllColumnsForAspVendor(){
		List<String> aspVendorColumns = new LinkedList<String>();
		Arrays.stream(AspVendorColumnsEnum.values()).forEach(column-> aspVendorColumns.add(column.getValue()));
		return aspVendorColumns;
	}
}
