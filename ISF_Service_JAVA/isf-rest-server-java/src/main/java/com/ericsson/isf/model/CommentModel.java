/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.model;

import java.util.Date;

/**
 *
 * @author edhhklu
 */
public class CommentModel {
	String id;                      // Required
	Integer parent ;                 // Required if replying is enabled
	Date created ;                // Required
	Date modified  ;              // Required if editing is enabled
	String content;                 // Either content or fileURL must be present
	String fileURL ;                // Either content or fileURL must be present
	//file                    // Required when uploading an attachment
	//fileMimeType ;           // Optional
	//pings                   // Required if pinging is enabled
	//creator                 // Required if pinging is enabled
	String fullname ;               // Required
	//profileURL  ;            // Optional
	//profilePictureURL   ;    // Optional
	//boolean isNew    ;               // Optional
	//boolean createdByAdmin ;         // Optional
	//createdByCurrentUser    // Required if editing is enabled
	//upvoteCount             // Required if upvoting is enabled
	//userHasUpvoted          // Required if upvoting is enabled
	
	
	public Integer getParent() {
		return parent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setParent(Integer parent) {
		this.parent = parent;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFileURL() {
		return fileURL;
	}
	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
	
}
