package com.ericsson.isf.dao;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.NetworkElementMapper;
import com.ericsson.isf.model.CreateWorkOrderModel2;
import com.ericsson.isf.model.CustomerModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.MqttModel;
import com.ericsson.isf.model.NetworkElementCountModel;
import com.ericsson.isf.model.NetworkElementModel;
import com.ericsson.isf.model.NetworkElementNewModel;
import com.ericsson.isf.model.NetworkElementUploadMasterModel;
import com.ericsson.isf.model.NetworkElemntViewModel;
import com.ericsson.isf.model.NodeFilterModel;
import com.ericsson.isf.model.NetworkElementValidateMasterModel;
import com.ericsson.isf.model.WebNotificationModel;
import com.ericsson.isf.model.WorkOrderNetworkElementModel;

/**
 * @author ekarmuj
 *
 */

@Repository
public class NetworkElementDao {
	
	
	@Qualifier("sqlSession")
	/* Create session from SQLSessionFactory */
	@Autowired
	private SqlSessionTemplate sqlSession;

public List<NetworkElementModel> getNetworkElementDetailsv1(int projectID, DataTableRequest dataTableReq, int recordsTotal) {
		
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.getNetworkElementDetailsv1(projectID, dataTableReq, recordsTotal );
}
	
	public List<NetworkElementModel> getNetworkElementDetailsv2(int projectID, DataTableRequest dataTableReq) {
		
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.getNetworkElementDetailsv2(projectID, dataTableReq );
	
		
	}

	public void appendNetworkElement(NetworkElementNewModel elements) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.appendNetworkElement(elements);
	}

	public int getCountryCustomerGroupIDByProjectID(int projectID) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.getCountryCustomerGroupIDByProjectID(projectID);
	}

	public List<Map<String, Object>> downloadNetworkElementv1(int projectID) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.downloadNetworkElementv1(projectID);
	}

	public List<String> getValidateJsonForApi(String apiName) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.getValidateJsonForApi(apiName);
	}

	public String checkDomain(String stringValue) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.checkDomain(stringValue);
	}

	public String checkSubDomain(String stringValue) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.checkSubDomain(stringValue);
	}

	public String checkTechnology(String stringValue) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.checkTechnology(stringValue);
	}

	public String checkVendor(String stringValue) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.checkVendor(stringValue);
	}

	public String checkUploadedBy(String stringValue) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.checkUploadedBy(stringValue);
	}
	
	public boolean checkIFRoleExists(String signum, int projectId) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.checkIFRoleExists(signum,projectId);
	}

	public boolean checkDomainSubDomainCombination(String domain, String subDomain) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.checkDomainSubDomainCombination(domain, subDomain);
	}

	public boolean checkPMDRRoleForProjectID(String uploadedBy, int projectID) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.checkPMDRRoleForProjectID(uploadedBy, projectID);
	}

	public boolean validateDuplicateNE(NetworkElementNewModel elements) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.validateDuplicateNE(elements);
	}

	public List<Map<String, Object>> downloadReferenceMappingFile() {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.downloadReferenceMappingFile();
	}

	public MqttModel getUploadedFileStatus(int referenceId, boolean flag) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.getUploadedFileStatus(referenceId,flag);
		
	}

	public NetworkElementUploadMasterModel checkStatusOfUserForNEUpload(int projectID) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.checkStatusOfUserForNEUpload(projectID);
	}

	public List<Map<String, Object>> successFile() {
		return returnSuccessFailureData();
		
	}

	private List<Map<String, Object>> returnSuccessFailureData() {
		Map<String,Object> map=new HashMap<>();
		map.put("Market", "BIHAR & JHARKHAND");
		map.put("Domain", "Radio Access Networks");
		map.put("SubDomain", "LTE RAN");
		map.put("Technology", "LTE");
		map.put("Vendor", "ERICSSON");
		map.put("NetworkElementType", "SITE");
		map.put("NetworkElementSubType", "Macro");
		map.put("NetworkElementId", "DAL_1703");
		map.put("Langitude", "");
		map.put("Longitude", "");
		map.put("errorType", "Error message for duplicate combination");
		map.put("errorMessage", "Network Element already exists in CNEDB");
		List<Map<String, Object>> listOfSuccessFile=new ArrayList<>();
		listOfSuccessFile.add(map);
		return listOfSuccessFile;
	}

	public List<Map<String, Object>> failureFile() {
		return returnSuccessFailureData();
	}

	public void insertStatusInUploadMasterTable(NetworkElementUploadMasterModel networkElementUploadMasterModel, int expiryTime, String originalFilename) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		 networkElementMapper.insertStatusInUploadMasterTable(networkElementUploadMasterModel,expiryTime, originalFilename);
		
	}

	public void insertStatusInWebNotificationTable(WebNotificationModel webNotificationModel) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		 networkElementMapper.insertStatusInWebNotificationTable(webNotificationModel);
	}

	public String migrateDataTempToMaster(String signum, int neuploadId, int projectID, String status, int countryCustomerGroupID, String tableName) {
		 NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		 return networkElementMapper.migrateDataTempToMaster(signum,neuploadId,projectID,status,countryCustomerGroupID,tableName);
	}

	public void updatePreviousStatusInWebNotificationTable(WebNotificationModel webNotificationModel) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		 networkElementMapper.updatePreviousStatusInWebNotificationTable(webNotificationModel);
		
	}

	public void validateNetworkElementFile(String fileName,
			NetworkElementUploadMasterModel networkElementUploadMasterModel, int projectID) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		 networkElementMapper.validateNetworkElementFile(fileName,networkElementUploadMasterModel, projectID  );

	}

	public List<Map<String, Object>> downloadSuccessFile(String fileTable) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.downloadSuccessFile(fileTable);
	}

	public List<Map<String, Object>> downloadFailureFile(String fileTable) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.downloadFailureFile(fileTable);
	}

	public void deleteStatusInUploadMasterTable(int neUploadID, String fileName) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		 networkElementMapper.deleteStatusInUploadMasterTable(neUploadID, fileName);
		
	}

	public void updateLinkInNeUploadMaster(int neUploadId, String successLink, String failureLink) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		 networkElementMapper.updateLinkInNeUploadMaster(neUploadId,successLink,failureLink);
		
	}

	public NetworkElementUploadMasterModel getPassFailCount(int neUploadId) {
		 NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		 return networkElementMapper.getPassFailCount(neUploadId);
	}

	public void updateStatusInMasterTable(int neUploadID, String currentUploadStatus, String modifiedBy, String fileName) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.updateStatusInMasterTable(neUploadID,currentUploadStatus,modifiedBy, fileName);
		
	}

	

	public MqttModel getPreviousUploadStatusCnedb(int neuploadId) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.getPreviousUploadStatusCnedb(neuploadId);
	}

	public List<MqttModel> getAllFileBetweenTwoDates(LocalDateTime startDate, LocalDateTime endDate) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.getAllFileBetweenTwoDates(startDate,endDate);
	}
	public Set<String> getAllPmBasedOnCountryCustomerGroupId(int countryCustomerGroupID) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.getAllPmBasedOnCountryCustomerGroupId(countryCustomerGroupID);
	}

	public Set<String> getAllDrBasedOnCountryCustomerGroupId(int countryCustomerGroupID) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.getAllDrBasedOnCountryCustomerGroupId(countryCustomerGroupID);
	}

	public List<String> getNetworkElementNameByTerm(int projectID, String techCommaSeparated,
			String domainCommaSeparated, String term, int count) {
		int pageSize = count;
		int pageNo = 0;
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.getNetworkElementNameByTerm(projectID, techCommaSeparated, domainCommaSeparated,
				'%' + term + '%', rowBounds);
	}

	public List<NetworkElemntViewModel> viewNetworkElementDetails(String tablename, String status, DataTableRequest dataTableRequest) {
		
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.viewNetworkElementDetails(tablename, status, dataTableRequest );
	
		
	}
	public void insertStatusInValidateMasterTable(NetworkElementValidateMasterModel networkElementValidateMasterModel) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.insertStatusInValidateMasterTable(networkElementValidateMasterModel);
		
		
	}

	public String validateCommaSeparatedNetworkElementData(String signum, String techCommaSeparated,
			String domainCommaSeparated, int projectID, int neValidateID, String networkElementNames, String source, String executionPlanName, String nodeType, int ccGroupID) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.validateCommaSeparatedNetworkElementData(signum, techCommaSeparated,
				domainCommaSeparated, projectID, neValidateID, networkElementNames, source, executionPlanName,nodeType, ccGroupID );

	}

	public void updateInactiveRadioSelectionByNEname(String networkElementName, String tableName) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.updateInactiveRadioSelectionByNEname(networkElementName,tableName);
		
		
	}

	public void updateActiveRadioSelectionByID(String networkElementName, int networkElementId, String tableName) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.updateActiveRadioSelectionByID(networkElementName,networkElementId,tableName);
	}

	public List<String> getDistinctNetworkElementType(String tablename) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.getDistinctNetworkElementType(tablename);
	}

	public void addWorkOrderPlanNodes(String tableName, int woPlanId, String signum) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.addWorkOrderPlanNodes(tableName,woPlanId,signum);
		
	}

	public void createNodesForWorkOrder(CreateWorkOrderModel2 createModel, String tableName, String[] nodeNames) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.createNodesForWorkOrder(createModel,tableName, nodeNames);
		
	}

	public void insertWorkOrderNeCount(CreateWorkOrderModel2 createModel, int woid) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.insertWorkOrderNeCount(createModel,woid);
		
	}

	public void deleteTempTableForNE(String tablename) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.deleteTempTableForNE(tablename);
		
	}

	public void insertWorkOrderNeCount(CreateWorkOrderModel2 createModel, int count, String neTextName) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.insertWoNeCount(createModel,count,neTextName);
		
	}

	public void updateActiveRadioSelectionForValid(int networkElementId, String tableName) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.updateActiveRadioSelectionForValid(networkElementId, tableName);
		
	}

	public WorkOrderNetworkElementModel getDistinctNetworkElementNameAndCount(String tablename) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.getDistinctNetworkElementNameAndCount(tablename);
	}

	public String getDistinctNetworkElementName(String tableName) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.getDistinctNetworkElementName(tableName);
	}

	public int getRadioSelectionByID(int networkElementId, String tableName) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.getRadioSelectionByID(networkElementId,tableName);
	}

	public void updateInActiveRadioSelectionByID(int networkElementId, String tableName) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.updateInActiveRadioSelectionByID(networkElementId, tableName);
	}

	public void insertNetworkClusterNetworkElementCount(CreateWorkOrderModel2 createModel, int count, String neTextName) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.insertNetworkClusterNetworkElementCount(createModel,count,neTextName);		
	}

	public String checkNodeNameandTypeExistsInCNEDB(int countryCustomerGroupID, String nodeNames, String nodeType,
			String source, String executionPlanName, Integer projectID) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.checkNodeNameandTypeExistsInCNEDB(countryCustomerGroupID, nodeNames,
				nodeType, source, executionPlanName, projectID);
	}
	public List<Map<String, Object>> downloadNeDataOnStatus(String status, String tablename) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.downloadNeDataOnStatus(status,tablename);
	}

	public void insertIntoTempTable(NetworkElementNewModel elements, String tableName) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.insertIntoTempTable(elements,tableName);
	}

	public int getNeValidateIDByTableName(String tableName) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.getNeValidateIDByTableName(tableName);
	}

	public void updateNeValidateMasterTable(int neValidateID, String createdBy) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.updateNeValidateMasterTable(neValidateID,createdBy);
	}

	public String getValidationMessage(String tableName) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.getValidationMessage(tableName);
		 
	}

	public String checkMigrationSuccess(int neUploadID) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.checkMigrationSuccess(neUploadID);
	}

	public String checkNetworkElementType(String elementType ) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.checkNetworkElementType(elementType);
		}

	public String checkNetworkSubElementType(String type) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.checkNetworkSubElementType(type);
	}

	public HashMap<String,Object> getCountryCustomerByID(int countryCustomerGroupID, int projectID) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.getCountryCustomerByID(countryCustomerGroupID, projectID);
	}
	
	public long getTotalNetworkElementCount(NetworkElementCountModel networkElementCountModel) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		return networkElementMapper.getTotalNetworkElementCount(networkElementCountModel);
	}

	
	public void updateInActiveRadioSelectionByIDForSelectAll(String listOfNetworkElementId, String tableName) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.updateInActiveRadioSelectionByIDForSelectAll(listOfNetworkElementId, tableName);
	}
	
	public void updateInactiveRadioSelectionByNEnameForSelectAll(String listOfName, String tableName) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.updateInactiveRadioSelectionByNEnameForSelectAll(listOfName,tableName);	
	}
	
	public void updateActiveRadioSelectionByIDForSelectAll(String listOfNetworkElementId, String tableName) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.updateActiveRadioSelectionByIDForSelectAll(listOfNetworkElementId,tableName);
	}
	
	public void updateActiveRadioSelectionForValidForSelectAll(String listOfNetworkElementId, String tableName,int radioSelectionForSelectAll) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.updateActiveRadioSelectionForValidForSelectAll(listOfNetworkElementId, tableName,radioSelectionForSelectAll);
	}
	
	public void resetSelectedRecordsForNE(String status, String tableName) {
		NetworkElementMapper networkElementMapper = sqlSession.getMapper(NetworkElementMapper.class);
		networkElementMapper.resetSelectedRecordsForNE(status, tableName);
	}
	
}
