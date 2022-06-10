/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;


/**
 *
 * @author ekarath
 */
public class CapabilityPageNameModel {

    
    private String id;
    private String subMenuTitle;
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



	private String subMenuHref;
    private String onClick;
    

	public String getOnClick() {
		return onClick;
	}

	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
    
}