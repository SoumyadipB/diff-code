/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.AuditManagementMapper;
import com.ericsson.isf.model.AuditDataModel;
import com.ericsson.isf.model.AuditGroupModel;


/**
 *
 * @author edhhklu
 */
@Repository
public class AuditManagementDAO {
    
    @Qualifier("sqlSession")
    /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;

    public HashMap<String, Object> getAuditDataByPageId(String pageId, Integer start, Integer length, String searchString){
    	AuditManagementMapper auditManagementMapper = sqlSession.getMapper(AuditManagementMapper.class);
    	HashMap<String, Object> data = new HashMap<>();
    	String auditPageId = pageId.substring(pageId.lastIndexOf('_')+1);
    	String auditGroupCategory = pageId.substring(0, pageId.lastIndexOf('_'));
		if (start != null && start < 0) {
			start = 0;
		}
		if (length != null && length < 0) {
			length=Integer.MAX_VALUE;
		}
    	if(StringUtils.isNotEmpty(auditPageId) && StringUtils.isNotEmpty(auditGroupCategory) 
    			&& !auditPageId.equalsIgnoreCase("NULL") && !auditGroupCategory.equalsIgnoreCase("NULL")) {

    		/*if(start == null || length == null)
    		{
//    			RowBounds rowBounds=new RowBounds();
    			List<AuditDataModel> allcomments = auditManagementMapper.getAuditDataByPageId(auditGroupCategory,auditPageId,(searchString==null)?null:'%'+searchString+'%', 0, 5);
    			data.put("data",allcomments);
    			return data;
    		}*/

//    		RowBounds rowBounds=new RowBounds(start,length);
    		List<AuditDataModel> allcomments = auditManagementMapper.getAuditDataByPageId(auditGroupCategory,auditPageId,(searchString==null)?null:'%'+searchString+'%', start, length);
    		data.put("data",allcomments);
    	}else {
    		data.put("ErrorFlag", true);
			data.put("Error", "AuditPageId/AuditGroupCategory is null, Please provide valid pageID");
    	}
    	return data;
    }
    
    
    public AuditGroupModel getAuditGroupByPageId(String pageId, String groupCategory){
    	AuditManagementMapper auditManagementMapper = sqlSession.getMapper(AuditManagementMapper.class);
        return auditManagementMapper.getAuditGroupByPageId(pageId,groupCategory);
    }
    
    
    public void insertAuditData(AuditDataModel auditDataModel){
    	AuditManagementMapper auditManagementMapper = sqlSession.getMapper(AuditManagementMapper.class);
        auditManagementMapper.insertAuditData(auditDataModel);
    }
    
    public void insertAuditGroup(AuditGroupModel auditGroupModel){
    	AuditManagementMapper auditManagementMapper = sqlSession.getMapper(AuditManagementMapper.class);
        auditManagementMapper.insertAuditGroup(auditGroupModel);
    }

}
