package com.ericsson.isf.model;

import java.util.List;

public class DynamicClause {
	private List<String> orderBy;
	private List<ColumnModel> andCondition;
	
	public List<String> getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(List<String> orderBy) {
		this.orderBy = orderBy;
	}
	public List<ColumnModel> getAndCondition() {
		return andCondition;
	}
	public void setAndCondition(List<ColumnModel> andCondition) {
		this.andCondition = andCondition;
	}	
}
