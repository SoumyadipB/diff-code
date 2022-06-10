package com.ericsson.isf.iva.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ericsson.isf.iva.model.WorkFlowModel;
import com.ericsson.isf.iva.model.ServerBotModel;
import com.ericsson.isf.iva.model.WorkOrderModel;

@Mapper
public interface IvaManagementMapper {

	public boolean isEmployeeExist(@Param("signum") String signum);

	public Map<String, Object> getAndValidateSource(@Param("ownerSignum") String ownerSignum,
			@Param("token") String token,@Param("apiName") String apiName);

	public boolean isWorkOrderAssignedToSignum(@Param("signum") String signum,@Param("workOrderID") int workOrderID);

	public WorkOrderModel getWorkOrderDetailsById(@Param("woID") int wOID);

	public Map<String, Object> getWorkFlowNameForWoID(@Param("wOID") int wOID);

	public Integer getExpertDefID(@Param("wfName") String wfName, @Param("wfID") String wfID);

	public List<WorkFlowModel> getExpertWorkFlow(@Param("wOID") int wOID, @Param("wfName") String wfName, @Param("expertDefId") Integer expertDefId);

	public List<WorkFlowModel> getWOWorkFlowWithWFNoAndWOID(@Param("wOID")int wOID, @Param("wfVersion") int wfVersion);

	public boolean isHeaderSignumExist(@Param("wOID") int wOID,@Param("signum") String signum);

	public WorkFlowModel[] getStepDetails(@Param("workFlowModels") WorkFlowModel[] workFlowModels, @Param("wOID") int wOID);

	public ServerBotModel getStepIdAndExecutionType(@Param("subActivityFlowChartDefID") int subActivityFlowChartDefID, @Param("stepID")String stepID);

	public String getSignumForBookingId(@Param("bookingId")int bookingId);

	public boolean validateSignumProficientforWO(@Param("subActivityID") int subActivityID,@Param("wFID") int wFID,
										@Param("signum") String signum);

	public List<String> getValidateJsonForApi(@Param("apiName") String apiName);
}
