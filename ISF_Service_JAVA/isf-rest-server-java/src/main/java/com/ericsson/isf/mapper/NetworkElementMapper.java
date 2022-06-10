
package com.ericsson.isf.mapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.ericsson.isf.model.CreateWorkOrderModel2;
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
public interface NetworkElementMapper {

	List<NetworkElementModel> getNetworkElementDetailsv1(@Param("projectID") int projectID,
			@Param("dataTableReq") DataTableRequest dataTableReq, @Param("recordsTotal") int recordsTotal);
	
	List<NetworkElementModel> getNetworkElementDetailsv2(@Param("projectID") int projectID,
			@Param("dataTableReq") DataTableRequest dataTableReq);

	void appendNetworkElement(@Param("elements") NetworkElementNewModel elements);

	int getCountryCustomerGroupIDByProjectID(@Param("projectID") int projectID);

	List<Map<String, Object>> downloadNetworkElementv1(@Param("projectID") int projectID);

	List<String> getValidateJsonForApi(@Param("apiName") String apiName);

	String checkDomain(@Param("stringValue") String stringValue);

	String checkSubDomain(@Param("stringValue") String stringValue);

	String checkTechnology(@Param("stringValue") String stringValue);

	String checkVendor(@Param("stringValue") String stringValue);

	String checkUploadedBy(@Param("stringValue") String stringValue);

	boolean checkIFRoleExists(@Param("signum") String signum,@Param("projectID") int projectID);

	boolean checkDomainSubDomainCombination( @Param("domain")  String domain,  @Param("subDomain")  String subDomain);

	boolean checkPMDRRoleForProjectID(@Param("uploadedBy") String uploadedBy, @Param("projectID") int projectID);

	boolean validateDuplicateNE(@Param("elements") NetworkElementNewModel elements);

	List<Map<String, Object>> downloadReferenceMappingFile();

	MqttModel getUploadedFileStatus(@Param("referenceId")int referenceId,@Param("flag") boolean flag);

	NetworkElementUploadMasterModel checkStatusOfUserForNEUpload(@Param("projectID") int projectID);

	void insertStatusInUploadMasterTable(@Param("networkElementUploadMasterModel") NetworkElementUploadMasterModel networkElementUploadMasterModel,@Param("expiryTime") int expiryTime,  @Param("originalFilename") String originalFilename );

	void insertStatusInWebNotificationTable(@Param("webNotificationModel")  WebNotificationModel webNotificationModel);

	String migrateDataTempToMaster(@Param("signum") String signum,@Param("neuploadId") int neuploadId,@Param("projectID") int projectID,@Param("status") String status,@Param("countryCustomerGroupID") int countryCustomerGroupID,@Param("tableName") String tableName);

	void updatePreviousStatusInWebNotificationTable( @Param("webNotificationModel") WebNotificationModel webNotificationModel);

	void validateNetworkElementFile(@Param("FileTable") String fileTable, @Param("networkElementUploadMasterModel")  NetworkElementUploadMasterModel networkElementUploadMasterModel,
			@Param("projectID") int projectID);

	List<Map<String, Object>> downloadSuccessFile(@Param("Filetable") String fileTable);

	List<Map<String, Object>> downloadFailureFile(@Param("Filetable") String fileTable);

	void deleteStatusInUploadMasterTable( @Param("neUploadID") int neUploadID, @Param("fileName") String fileName);

	void updateLinkInNeUploadMaster(@Param("neUploadId")int neUploadId, @Param("successLink")String successLink,@Param("failureLink") String failureLink);

	NetworkElementUploadMasterModel getPassFailCount(@Param("neUploadId")int neUploadId);

	void updateStatusInMasterTable(@Param("neUploadID")int neUploadID,@Param("currentUploadStatus") String currentUploadStatus,@Param("modifiedBy") String modifiedBy, @Param("fileName") String fileName);

	MqttModel getPreviousUploadStatusCnedb(@Param("neuploadId")int neuploadId);

	List<MqttModel> getAllFileBetweenTwoDates(@Param("startDate") LocalDateTime startDate,@Param("endDate") LocalDateTime endDate);

	public Set<String> getAllPmBasedOnCountryCustomerGroupId(@Param("countryCustomerGroupID") int countryCustomerGroupID);

	public Set<String> getAllDrBasedOnCountryCustomerGroupId(@Param("countryCustomerGroupID") int countryCustomerGroupID);

	List<String> getNetworkElementNameByTerm(@Param("projectID") int projectID, @Param("techCommaSeparated") String techCommaSeparated, @Param("domainCommaSeparated") String domainCommaSeparated,
			@Param("term") String term, RowBounds rowBounds);

	List<NetworkElemntViewModel> viewNetworkElementDetails(@Param("tablename") String tablename,
			@Param("status") String status, @Param("dataTableRequest") DataTableRequest dataTableRequest);
	void insertStatusInValidateMasterTable(@Param("networkElementValidateMasterModel") NetworkElementValidateMasterModel networkElementValidateMasterModel);

	String validateCommaSeparatedNetworkElementData(@Param("signum") String signum,
			@Param("techCommaSeparated") String techCommaSeparated,
			@Param("domainCommaSeparated") String domainCommaSeparated, @Param("projectID") int projectID,
			@Param("neValidateID") int neValidateID, @Param("networkElementNames") String networkElementNames,
			@Param("source") String source, @Param("executionPlanName") String executionPlanName,
			@Param("nodeType") String nodeType, @Param("ccGroupID") int ccGroupID);

	void updateInactiveRadioSelectionByNEname(@Param("networkElementName") String networkElementName, @Param("tableName") String tableName);

	void updateActiveRadioSelectionByID(@Param("networkElementName") String networkElementName, @Param("networkElementId") int networkElementId, @Param("tableName") String tableName);

	List<String> getDistinctNetworkElementType(@Param("tableName") String tablename);

	void addWorkOrderPlanNodes(@Param("tableName") String tableName,@Param("woPlanId") int woPlanId,@Param("signum") String signum);

	void createNodesForWorkOrder(@Param("createWorkOrderModel")  CreateWorkOrderModel2 createModel, @Param("tableName")  String tableName,@Param("nodeNames") String[] nodeNames);

	void insertWorkOrderNeCount(@Param("createModel")  CreateWorkOrderModel2 createModel, @Param("woid") int woid);

	void deleteTempTableForNE(@Param("tablename") String tablename);

	void insertWoNeCount(@Param("createModel") CreateWorkOrderModel2 createModel,@Param("neCount") int neCount, @Param("neTextName") String neTextName);


	void updateActiveRadioSelectionForValid(@Param("networkElementId") int networkElementId,@Param("tableName") String tableName);

	WorkOrderNetworkElementModel getDistinctNetworkElementNameAndCount(@Param("tableName") String tablename);

	String getDistinctNetworkElementName(@Param("tableName") String tableName);

	int getRadioSelectionByID(@Param("networkElementId") int networkElementId, @Param("tableName") String tableName);

	void updateInActiveRadioSelectionByID(@Param("networkElementId") int networkElementId, @Param("tableName") String tableName);

	void insertNetworkClusterNetworkElementCount(@Param("createModel") CreateWorkOrderModel2 createModel, @Param("neCount") int count, @Param("neTextName") String neTextName);

	String checkNodeNameandTypeExistsInCNEDB(@Param("countryCustomerGroupID") int countryCustomerGroupID,
			@Param("nodeNames") String nodeNames, @Param("nodeType") String nodeType, @Param("source") String source,
			@Param("executionPlanName") String executionPlanName, @Param("projectID") Integer projectID);
	
	List<Map<String, Object>> downloadNeDataOnStatus(@Param("status") String status,@Param("tablename")  String tablename);

	void insertIntoTempTable(@Param("elements") NetworkElementNewModel elements, @Param("tableName") String tableName);

	int getNeValidateIDByTableName(@Param("tableName") String tableName);

	void updateNeValidateMasterTable(@Param("neValidateID") int neValidateID, @Param("createdBy") String createdBy);

	String getValidationMessage(@Param("tableName") String tableName);

	String checkMigrationSuccess(@Param("neUploadID") int neUploadID);

	String checkNetworkElementType(@Param("elementType") String elementType);

	String checkNetworkSubElementType(@Param("type") String type);

	HashMap<String,Object> getCountryCustomerByID(@Param("countryCustomerGroupID") int countryCustomerGroupID,@Param("projectID") int projectID);
	
	long getTotalNetworkElementCount(@Param("networkElementCountModel") NetworkElementCountModel networkElementCountModel);
	
	void updateInActiveRadioSelectionByIDForSelectAll(@Param("listOfNetworkElementId") String listOfNetworkElementId,
			@Param("tableName") String tableName);
	
	void updateInactiveRadioSelectionByNEnameForSelectAll(@Param("listOfName") String listOfName,
			@Param("tableName") String tableName);
	
	void updateActiveRadioSelectionByIDForSelectAll(@Param("listOfNetworkElementId") String listOfNetworkElementId,
			@Param("tableName") String tableName);
	
	void updateActiveRadioSelectionForValidForSelectAll(@Param("listOfNetworkElementId") String listOfNetworkElementId,
			@Param("tableName") String tableName,@Param("radioSelectionForSelectAll") int radioSelectionForSelectAll);
	
	void resetSelectedRecordsForNE(@Param("status") String status,@Param("tableName") String tableName);
	
}
