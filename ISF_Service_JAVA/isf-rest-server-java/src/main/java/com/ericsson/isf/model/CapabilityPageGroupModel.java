/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.List;

/**
 *
 * @author ekarath
 */
public class CapabilityPageGroupModel {

    private String id;
    private String groupTitle;
    private String groupHref;
    private String groupOnClick;
    
    public String getGroupOnClick() {
		return groupOnClick;
	}

	public void setGroupOnClick(String groupOnClick) {
		this.groupOnClick = groupOnClick;
	}

	public String getGroupHref() {
		return groupHref;
	}

	public void setGroupHref(String groupHref) {
		this.groupHref = groupHref;
	}

	public String getGroupTitle() {
		return groupTitle;
	}

	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}

	public String getGroupIcon() {
		return groupIcon;
	}

	public void setGroupIcon(String groupIcon) {
		this.groupIcon = groupIcon;
	}

	private String groupIcon;
	private List<CapabilityPageNameModel> submenu;
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public List<CapabilityPageNameModel> getSubmenu() {
		return submenu;
	}

	public void setSubmenu(List<CapabilityPageNameModel> submenu) {
		this.submenu = submenu;
	}
    
}