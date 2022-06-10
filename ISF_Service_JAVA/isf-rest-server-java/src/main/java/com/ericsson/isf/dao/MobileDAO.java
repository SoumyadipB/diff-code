package com.ericsson.isf.dao;

import java.time.LocalDateTime;
import java.util.List;


import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.MobileMapper;
import com.ericsson.isf.model.AuditDataModel;
import com.ericsson.isf.model.ProjectScopeModel;
import com.ericsson.isf.model.ServiceRequestModel;
import com.ericsson.isf.model.WebNotificationModel;
import com.ericsson.isf.model.WorkOrderDetailsModel;
import com.ericsson.isf.model.FieldUtilityResourceMappingModel;
import com.ericsson.isf.model.MobileNotificationModel;
import com.ericsson.isf.model.NodeNameValidationModel;

@Repository
public class MobileDAO {
	
	@Qualifier("sqlSession")
    /*Create session from SQLSessionFactory */
    @Autowired
    private SqlSessionTemplate sqlSession;

	public List<NodeNameValidationModel> getNodeNamesByCountryCustomerGroupID(int ccGroupID, String type, String term) {
		int pageSize = 50;
		int pageNo = 0;
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		MobileMapper mobileMapper = sqlSession.getMapper(MobileMapper.class);
		return mobileMapper.getNodeNamesByCountryCustomerGroupID(ccGroupID, type,  '%' + term + '%', rowBounds);	
	}
	
	public List<ProjectScopeModel> getFUDeliverablesByProject(int projectID, String term) {
		int pageSize = 20;
		int pageNo = 0;
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		
		MobileMapper mobileMapper = sqlSession.getMapper(MobileMapper.class);
        return mobileMapper.getFUDeliverablesByProject(projectID, term,rowBounds);
	}
		
	public List<NodeNameValidationModel> getNodeTypeByCountryCustomerGroupID(Integer ccGroupID) {
		MobileMapper mobileMapper = sqlSession.getMapper(MobileMapper.class);
		return mobileMapper.getNodeTypeByCountryCustomerGroupID(ccGroupID);
	}
	

	

	public List<FieldUtilityResourceMappingModel> getFUProjectsBySignum(String signum, String role) {
		MobileMapper mobileMapper = sqlSession.getMapper(MobileMapper.class);
        return mobileMapper.getFUProjectsBySignum(signum, role);
	
}

	public void createServiceRequest(ServiceRequestModel serviceRequest) {
		MobileMapper mobileMapper = sqlSession.getMapper(MobileMapper.class);
        mobileMapper.createServiceRequest(serviceRequest);
		
	}

	public List<ServiceRequestModel> getServiceRequestsBySignum(String signum, String status,
			LocalDateTime rangeStartDateTime, LocalDateTime rangeEndDateTime, int offset, int length) {
		MobileMapper mobileMapper = sqlSession.getMapper(MobileMapper.class);
        return mobileMapper.getServiceRequestsBySignum(signum, status, rangeStartDateTime, rangeEndDateTime, offset, length);
	}

	
	public List<Integer> getWoidBySrid(Integer srID) {
		MobileMapper mobileMapper = sqlSession.getMapper(MobileMapper.class);
	     return mobileMapper.getWoidBySrid(srID);
	}

	public List<AuditDataModel> getAuditDataForServiceRequest(String workOrderIds, Integer start, Integer length, boolean addSrComments) {
		RowBounds rowBounds;
		if(start == null || length == null) {
			rowBounds = new RowBounds();
		}
		else {
			int pageNo = start / length;
			rowBounds = new RowBounds(pageNo * length, length);
		}
		MobileMapper mobileMapper = sqlSession.getMapper(MobileMapper.class);
		return mobileMapper.getAuditDataForServiceRequest(workOrderIds, addSrComments, rowBounds);
	}

	public List<ProjectScopeModel> getAllFUDeliverablesByProjectWithoutAnyTerm(int projectID) {
		MobileMapper mobileMapper = sqlSession.getMapper(MobileMapper.class);
        return mobileMapper.getAllFUDeliverablesByProjectWithoutAnyTerm(projectID);
		
	}

	public ServiceRequestModel getSRDetailsBySRID(int srID) {
		MobileMapper mobileMapper = sqlSession.getMapper(MobileMapper.class);
        return mobileMapper.getSRDetailsBySRID(srID);
	}
	
	public ServiceRequestModel getServiceRequestByWoid(Integer woid) {
		MobileMapper mobileMapper = sqlSession.getMapper(MobileMapper.class);
        return mobileMapper.getServiceRequestByWoid(woid);
	}

	public void saveMobileNotification(MobileNotificationModel mobileNotificationModel) {
		MobileMapper mobileMapper = sqlSession.getMapper(MobileMapper.class);
        mobileMapper.saveMobileNotification(mobileNotificationModel);
	}

	public List<MobileNotificationModel> getMobileNotifications(String signum, int start, int length) {
		MobileMapper mobileMapper = sqlSession.getMapper(MobileMapper.class);
        return mobileMapper.getMobileNotifications(signum, start, length);
	}

	public List<WorkOrderDetailsModel> getWODetailsBySRID(int srID) {
		MobileMapper mobileMapper = sqlSession.getMapper(MobileMapper.class);
        return mobileMapper.getWODetailsBySRID(srID);
	}

	public void saveWebNotification(WebNotificationModel webNotificationModel) {
		MobileMapper mobileMapper = sqlSession.getMapper(MobileMapper.class);
        mobileMapper.saveWebNotification(webNotificationModel);
		
	}
	
}
