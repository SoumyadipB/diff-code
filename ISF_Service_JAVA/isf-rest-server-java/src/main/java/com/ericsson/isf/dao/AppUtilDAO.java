/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.AppUtilMapper;
import com.ericsson.isf.model.DynamicClause;
import com.ericsson.isf.model.EmailModel;
import com.ericsson.isf.model.ProficiencyTypeModal;
import com.ericsson.isf.model.SharePointModel;
import com.ericsson.isf.model.botstore.BotDetail;


/**
 *
 * @author edhhklu
 */
@Repository
public class AppUtilDAO {
    
    @Qualifier("sqlSession")
    /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;

    public boolean sendEmail(EmailModel mailRequest) {
        AppUtilMapper appUtilMapper = sqlSession.getMapper(AppUtilMapper.class);
        return appUtilMapper.sendMail(mailRequest);
    }
    public EmailModel getNotificationsDetailsByTemplateName(String templateName) {
        AppUtilMapper appUtilMapper = sqlSession.getMapper(AppUtilMapper.class);
        return appUtilMapper.getNotificationsDetailsByTemplateName(templateName);
    }

    public void  botDownloadUsingProc( BotDetail botDetail) {
        AppUtilMapper appUtilMapper = sqlSession.getMapper(AppUtilMapper.class);
        appUtilMapper.botDownloadUsingProc(botDetail);
    }
	
	public void botUpload(BotDetail botDetail) {
		AppUtilMapper appUtilMapper = sqlSession.getMapper(AppUtilMapper.class);
        appUtilMapper.botUpload(botDetail);
		
	}
	
	public void sendUnsentSmtpMail(EmailModel emailDetails) {
		 AppUtilMapper appUtilMapper = sqlSession.getMapper(AppUtilMapper.class);
		appUtilMapper.sendUnsentSmtpMail(emailDetails);
	}
	
	public void sendSmtpMail(EmailModel mailRequest) {
        AppUtilMapper appUtilMapper = sqlSession.getMapper(AppUtilMapper.class);
         appUtilMapper.sendSmtpMail(mailRequest);
    }
	
    public List<EmailModel> getListOfUnsentMails() {
	 AppUtilMapper appUtilMapper = sqlSession.getMapper(AppUtilMapper.class);
    return appUtilMapper.getListOfUnsentMails();
	
}
	public SharePointModel getSharePointDetails(String marketArea) {
		AppUtilMapper appUtilMapper = sqlSession.getMapper(AppUtilMapper.class);
	    return appUtilMapper.getSharePointDetails(marketArea);	
	    }
	
	public ArrayList<Map<String, Object>> getViewData(String viewName, DynamicClause dynamicClause) {
		AppUtilMapper appUtilMapper = sqlSession.getMapper(AppUtilMapper.class);
	    return appUtilMapper.getViewData(viewName,dynamicClause);	
	    }
	public ProficiencyTypeModal getProficiencyID(String proficiencyName) {
		AppUtilMapper appUtilMapper = sqlSession.getMapper(AppUtilMapper.class);
	    return appUtilMapper.getProficiencyID(proficiencyName);	
	}
	public int getCountryCustomerGroupIDByProjectID(int projectID) {
		AppUtilMapper appUtilMapper = sqlSession.getMapper(AppUtilMapper.class);
	    return appUtilMapper.getCountryCustomerGroupIDByProjectID(projectID);	
	}

	public boolean insertSharePointDetailsForServerBot(SharePointModel sharePointModel) {
		AppUtilMapper appUtilMapper = sqlSession.getMapper(AppUtilMapper.class);
		return appUtilMapper.insertSharePointDetailsForServerBot(sharePointModel);

	}

	public boolean checkSharePointDetailsForServerBot(SharePointModel sharePointModel) {
		AppUtilMapper appUtilMapper = sqlSession.getMapper(AppUtilMapper.class);
		return appUtilMapper.checkSharePointDetailsForServerBot(sharePointModel);
	}
	public String getClientIDbySiteName(String siteName) {
		AppUtilMapper appUtilMapper = sqlSession.getMapper(AppUtilMapper.class);
		return appUtilMapper.getClientIDbySiteName(siteName);
	}
	public boolean updateSharePointDetailsForServerBot(SharePointModel sharePointModel) {
		AppUtilMapper appUtilMapper = sqlSession.getMapper(AppUtilMapper.class);
		return appUtilMapper.updateSharePointDetailsForServerBot(sharePointModel);
	}
	
	
}
