package com.ericsson.isf.model;

import java.util.List;

public class DeleteWOListModel {
	private String signum;
	public String getSignum() {
		return signum;
	}
	public void setSignum(String signum) {
		this.signum = signum;
	}

	private List<Integer> woID;
	public List<Integer> getWoID() {
		return woID;
	}
	public void setWoID(List<Integer> woID) {
		this.woID = woID;
	}

}
