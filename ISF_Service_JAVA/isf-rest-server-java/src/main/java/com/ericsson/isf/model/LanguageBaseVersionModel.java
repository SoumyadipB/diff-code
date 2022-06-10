package com.ericsson.isf.model;


import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ericsson.isf.model.botstore.TblRpaBotstaging;
import com.ericsson.isf.model.botstore.TblRpaDeployedBot;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "TBL_LanguageBaseVersion", schema = "refData")
public class LanguageBaseVersionModel {
	
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "LanguageBaseVersionID", unique = true, nullable = false)
	private int languageBaseVersionID;
	
	@Column(name = "LanguageBaseVersion",nullable = false)
	private String languageBaseVersion;
	
	@Column(name = "BotLanguageName",nullable = false)
	private String botLanguageName;
	
	@Column(name = "IsActive")
	private Boolean isActive;
	
	@Column(name = "UpdatedOn",nullable = false)
	private String updatedOn;
	
	public int getLanguageBaseVersionID() {
		return languageBaseVersionID;
	}
	public void setLanguageBaseVersionID(int languageBaseVersionID) {
		this.languageBaseVersionID = languageBaseVersionID;
	}
	public String getLanguageBaseVersion() {
		return languageBaseVersion;
	}
	public void setLanguageBaseVersion(String languageBaseVersion) {
		this.languageBaseVersion = languageBaseVersion;
	}
	public String getBotLanguageName() {
		return botLanguageName;
	}
	public void setBotLanguageName(String botLanguageName) {
		this.botLanguageName = botLanguageName;
	}
	
	@JsonIgnore
	public String getUpdatedOn() {
		return updatedOn;
	}
	@JsonProperty
	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}
	@JsonIgnore
	public Boolean getIsActive() {
		return isActive;
	}
	@JsonProperty
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
