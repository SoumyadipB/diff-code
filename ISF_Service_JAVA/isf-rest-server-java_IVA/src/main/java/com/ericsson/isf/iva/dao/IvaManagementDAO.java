package com.ericsson.isf.iva.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.iva.mapper.IvaManagementMapper;

import com.ericsson.isf.iva.model.ServerBotModel;
import com.ericsson.isf.iva.model.WorkFlowModel;
import com.ericsson.isf.iva.model.WorkOrderModel;

@Repository
public class IvaManagementDAO {

	@Autowired
	private IvaManagementMapper ivaManagementMapper;

	public boolean isEmployeeExist(String signum) {
		return ivaManagementMapper.isEmployeeExist(signum);

	}

	public Map<String, Object> getAndValidateSource(String ownerSignum, String token, String apiName) {
		return ivaManagementMapper.getAndValidateSource(ownerSignum, token, apiName);
	}

	public boolean isWorkOrderAssignedToSignum(String signum, int workOrderID) {
		return ivaManagementMapper.isWorkOrderAssignedToSignum(signum, workOrderID);
	}

	public WorkOrderModel getWorkOrderDetailsById(int wOID) {
		return ivaManagementMapper.getWorkOrderDetailsById(wOID);

	}

	public Map<String, Object> getWorkFlowNameForWoID(int wOID) {
		return ivaManagementMapper.getWorkFlowNameForWoID(wOID);
	}

	public Integer getExpertDefID(String wfName, String wfID) {
		return ivaManagementMapper.getExpertDefID(wfName, wfID);
	}

	public List<WorkFlowModel> getExpertWorkFlow(int wOID, String wfName, Integer expertDefId) {
		return ivaManagementMapper.getExpertWorkFlow(wOID, wfName, expertDefId);
	}

	public List<WorkFlowModel> getWOWorkFlowWithWFNoAndWOID(int wOID, int wfVersion) {
		return ivaManagementMapper.getWOWorkFlowWithWFNoAndWOID(wOID, wfVersion);
	}

	public boolean isHeaderSignumExist(int wOID,String signum) {
		return ivaManagementMapper.isHeaderSignumExist(wOID, signum);
	}

	public WorkFlowModel[] getStepDetails(WorkFlowModel[] workFlowModels, int wOID) {
		return ivaManagementMapper.getStepDetails(workFlowModels, wOID);
	}

	public ServerBotModel getStepIdAndExecutionType(int subActivityFlowChartDefID, String stepID) {
		return ivaManagementMapper.getStepIdAndExecutionType(subActivityFlowChartDefID,stepID);
	}

	public String getSignumForBookingId(int bookingId) {
		return ivaManagementMapper.getSignumForBookingId(bookingId);
	}

	public boolean validateSignumProficientforWO(int subActivityID, int wFID, String signum) {
		return ivaManagementMapper.validateSignumProficientforWO(subActivityID, wFID,signum);
	}

	public List<String> getValidateJsonForApi(String apiName) {
		return ivaManagementMapper.getValidateJsonForApi(apiName);
	}
}
