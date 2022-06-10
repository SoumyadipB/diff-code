/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.ericsson.isf.model.AuditDataModel;
import com.ericsson.isf.model.AuditGroupModel;



/**
 *
 * @author edhhklu
 */
public interface AuditManagementMapper {

	public List<AuditDataModel> getAuditDataByPageId(@Param("auditGroupCategory")String auditGroupCategory,@Param("auditPageId")String auditPageId, @Param("searchString")String searchString,@Param("offset") Integer offset, @Param("limit") Integer limit);
	public AuditGroupModel getAuditGroupByPageId(@Param("pageId")String pageId,@Param("groupCategory") String groupCategory);
	public void insertAuditData(@Param("auditData") AuditDataModel auditData);
	public void insertAuditGroup(@Param("auditGroup") AuditGroupModel auditGroupModel);

    
}
