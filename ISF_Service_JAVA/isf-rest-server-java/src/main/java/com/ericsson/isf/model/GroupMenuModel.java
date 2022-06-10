package com.ericsson.isf.model;

public class GroupMenuModel {
	private String groupTitle;
	private String subMenuTitle;
	private String subMenuHref;
	private String groupHref;
	
	
	public String getGroupTitle() {
		return groupTitle;
	}
	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}
	public String getSubMenuTitle() {
		return subMenuTitle;
	}
	public void setSubMenuTitle(String subMenuTitle) {
		this.subMenuTitle = subMenuTitle;
	}
	public String getSubMenuHref() {
		return subMenuHref;
	}
	public void setSubMenuHref(String subMenuHref) {
		this.subMenuHref = subMenuHref;
	}
	public String getGroupHref() {
		return groupHref;
	}
	public void setGroupHref(String groupHref) {
		this.groupHref = groupHref;
	}
}
