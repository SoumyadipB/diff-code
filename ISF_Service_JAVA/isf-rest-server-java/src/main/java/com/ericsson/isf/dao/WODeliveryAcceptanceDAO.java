/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import com.ericsson.isf.mapper.WODeliveryAcceptanceMapper;
import com.ericsson.isf.model.ClosedWODetailsModel;
import com.ericsson.isf.model.ReasonFromWOModel;
import com.ericsson.isf.model.WORelatedDetails;
import java.util.List;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author esanpup
 */
@Repository
public class WODeliveryAcceptanceDAO {
    
    @Qualifier("sqlSession")
    /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;

    public List<String> getAcceptanceRatings() {
        WODeliveryAcceptanceMapper woDeliveryAcceptanceMapper = sqlSession.getMapper(WODeliveryAcceptanceMapper.class);
        return woDeliveryAcceptanceMapper.getAcceptanceRatings();
    }

    public void acceptWorkOrder(Integer woID, String rating, String comment,String acceptedOrRejectedBy,String lastModifiedBy) {
        WODeliveryAcceptanceMapper woDeliveryAcceptanceMapper = sqlSession.getMapper(WODeliveryAcceptanceMapper.class);
        woDeliveryAcceptanceMapper.acceptWorkOrder(woID,rating,comment,acceptedOrRejectedBy,lastModifiedBy);
    }
	
	public void sendWorkOrderForAcceptance(Integer woID, String accOrRejBy, String comment, String lastModifiedBy){
        WODeliveryAcceptanceMapper woDeliveryAcceptancMapper = sqlSession.getMapper(WODeliveryAcceptanceMapper.class);
        woDeliveryAcceptancMapper.sendWorkOrderForAcceptance(woID, accOrRejBy, comment, lastModifiedBy);
        
    }
    
    public int rejectWorkOrder(Integer woID,String acceptance,String reason,String comment,String accOrRejBy,String lastModifiedBy){
        WODeliveryAcceptanceMapper wODeliveryAcceptanceMapper  =sqlSession.getMapper(WODeliveryAcceptanceMapper.class);
       return wODeliveryAcceptanceMapper.rejectWorkOrder(woID,acceptance,reason,comment,accOrRejBy,lastModifiedBy);
    }

    public List<ClosedWODetailsModel> getclosedWODetails(int projectID, String projectName, String signumID, String role) {
        WODeliveryAcceptanceMapper woDeliveryAcceptanceMapper = sqlSession.getMapper(WODeliveryAcceptanceMapper.class);
        return woDeliveryAcceptanceMapper.getclosedWODetails(projectID, projectName,signumID,role);
    }

    public List<ReasonFromWOModel> getReasonFromWO(int wOID) {
        WODeliveryAcceptanceMapper woDeliveryAcceptanceMapper = sqlSession.getMapper(WODeliveryAcceptanceMapper.class);
        return woDeliveryAcceptanceMapper.getReasonFromWO(wOID);
    }

    public WORelatedDetails getStatusReasons(int woID) {
        WODeliveryAcceptanceMapper woDeliveryAcceptanceMapper = sqlSession.getMapper(WODeliveryAcceptanceMapper.class);
        return woDeliveryAcceptanceMapper.getStatusReasons(woID);
    }

    public String getWOStatus(int woID) {
        WODeliveryAcceptanceMapper woDeliveryAcceptanceMapper = sqlSession.getMapper(WODeliveryAcceptanceMapper.class);
        return woDeliveryAcceptanceMapper.getWOStatus(woID);
    }

    public String getDelieveryAcceptance(int woID) {
        WODeliveryAcceptanceMapper woDeliveryAcceptanceMapper = sqlSession.getMapper(WODeliveryAcceptanceMapper.class);
        return woDeliveryAcceptanceMapper.getDelieveryAcceptance(woID);
    }

//    public WORelatedDetails getReopenedReasons(int pWOID) {
//        WODeliveryAcceptanceMapper woDeliveryAcceptanceMapper = sqlSession.getMapper(WODeliveryAcceptanceMapper.class);
//        return woDeliveryAcceptanceMapper.getReopenedReasons(pWOID);
//    }

    public int getWOForParentWOID(int woID) {
        WODeliveryAcceptanceMapper woDeliveryAcceptanceMapper = sqlSession.getMapper(WODeliveryAcceptanceMapper.class);
        return woDeliveryAcceptanceMapper.getWOForParentWOID(woID);
    }

	public List<ClosedWODetailsModel> getacceptedWODetails(int projectID, String projectName, String signumID,
			String role) {
		 WODeliveryAcceptanceMapper woDeliveryAcceptanceMapper = sqlSession.getMapper(WODeliveryAcceptanceMapper.class);
	        return woDeliveryAcceptanceMapper.getacceptedWODetails(projectID, projectName,signumID,role);
	}

//    public List<String> getStandardFailureReasons(String failureStatus) {
//        WODeliveryAcceptanceMapper woDeliveryAcceptanceMapper = sqlSession.getMapper(WODeliveryAcceptanceMapper.class);
//        return woDeliveryAcceptanceMapper.getStandardFailureReasons(failureStatus);
//    }
}
