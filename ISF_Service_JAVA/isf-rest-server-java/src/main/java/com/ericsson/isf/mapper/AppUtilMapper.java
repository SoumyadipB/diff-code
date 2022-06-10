/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ericsson.isf.model.DynamicClause;
import com.ericsson.isf.model.EmailModel;
import com.ericsson.isf.model.ProficiencyTypeModal;
import com.ericsson.isf.model.SharePointModel;
import com.ericsson.isf.model.botstore.BotDetail;

/**
 *
 * @author edhhklu
 */
public interface AppUtilMapper {

    public boolean sendMail(@Param("mailRequest") EmailModel mailRequest);
    public EmailModel getNotificationsDetailsByTemplateName(@Param("templateName") String templateName);

    public void botDownloadUsingProc(@Param("botDetail") BotDetail botDetail);
	public void botUpload(@Param("botDetail") BotDetail botDetail);
	
	//smtp mail
	public void sendSmtpMail(@Param("smtpRequest") EmailModel smtpRequest);
	public List<EmailModel> getListOfUnsentMails();
	public void sendUnsentSmtpMail(@Param("emailDetails") EmailModel emailDetails);
	public SharePointModel getSharePointDetails(@Param("marketArea") String marketArea);
	public ArrayList<Map<String, Object>> getViewData(@Param("viewName") String viewName,@Param("dynamicClause") DynamicClause dynamicClause);
	public ProficiencyTypeModal getProficiencyID(@Param("proficiencyName") String proficiencyName);
	public int getCountryCustomerGroupIDByProjectID(@Param("projectID") int projectID);
	public boolean insertSharePointDetailsForServerBot(@Param("sharePointModel") SharePointModel sharePointModel);
	public boolean checkSharePointDetailsForServerBot(@Param("sharePointModel") SharePointModel sharePointModel);
	public String getClientIDbySiteName(@Param("siteName") String siteName);
	public boolean updateSharePointDetailsForServerBot(@Param("sharePointModel") SharePointModel sharePointModel);

}

