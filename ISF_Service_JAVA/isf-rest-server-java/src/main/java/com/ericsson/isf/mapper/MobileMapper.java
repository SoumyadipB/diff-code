package com.ericsson.isf.mapper;



import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.ericsson.isf.model.AuditDataModel;
import com.ericsson.isf.model.ProjectScopeModel;
import com.ericsson.isf.model.ServiceRequestModel;
import com.ericsson.isf.model.WebNotificationModel;
import com.ericsson.isf.model.WorkOrderDetailsModel;
import com.ericsson.isf.model.FieldUtilityResourceMappingModel;
import com.ericsson.isf.model.MobileNotificationModel;
import com.ericsson.isf.model.NodeNameValidationModel;

public interface MobileMapper {

	public List<ProjectScopeModel> getFUDeliverablesByProject(@Param("projectID")int projectID,@Param("term") String term, RowBounds rowBounds);

	public List<NodeNameValidationModel> getNodeNamesByCountryCustomerGroupID(@Param("ccGroupID") int ccGroupID,@Param("type") String type,@Param("term") String term, RowBounds rowBounds);
	
	public List<NodeNameValidationModel> getNodeTypeByCountryCustomerGroupID(@Param("ccGroupID") Integer ccGroupID);

	public List<FieldUtilityResourceMappingModel> getFUProjectsBySignum(@Param("Signum")String signum,@Param("Role") String role);

	public void createServiceRequest(@Param("serviceRequest") ServiceRequestModel serviceRequest);


	public List<ServiceRequestModel> getServiceRequestsBySignum(@Param("signum") String signum, @Param("status") String status,
			@Param("rangeStartDateTime") LocalDateTime rangeStartDateTime,@Param("rangeEndDateTime") LocalDateTime rangeEndDateTime,
			@Param("offset") int offset,@Param("length") int length);

	public List<Integer> getWoidBySrid( @Param("srID") Integer srID);

	public List<AuditDataModel> getAuditDataForServiceRequest(@Param("workOrderIds") String workOrderIds,@Param("addSrComments") boolean addSrComments, RowBounds rowBounds);

	public List<ProjectScopeModel> getAllFUDeliverablesByProjectWithoutAnyTerm(@Param("projectID") int projectID);

	public ServiceRequestModel getSRDetailsBySRID(@Param("srID") int srID);

	public ServiceRequestModel getServiceRequestByWoid(@Param("woid") Integer woid);

	public void saveMobileNotification(@Param("mobileNotification") MobileNotificationModel mobileNotificationModel);

	public List<MobileNotificationModel> getMobileNotifications(@Param("signum") String signum,@Param("start") int start,@Param("length") int length);

	public List<WorkOrderDetailsModel> getWODetailsBySRID(@Param("srID") int srID);

	public void saveWebNotification(@Param("webNotificationModel") WebNotificationModel webNotificationModel);
}
