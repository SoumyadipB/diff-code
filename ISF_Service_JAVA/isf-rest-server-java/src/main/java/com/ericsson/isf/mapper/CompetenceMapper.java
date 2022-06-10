package com.ericsson.isf.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ericsson.isf.model.BulkUploadModel;
import com.ericsson.isf.model.CompetenceServiceAreaModel;
import com.ericsson.isf.model.CompetenceTrainingModel;
import com.ericsson.isf.model.UserCompetenceModel;

public interface CompetenceMapper {

	List<Map<String, Object>> getDomain(@Param("competenceTypeID") int competenceTypeID);

	List<Map<String, Object>> getTechnology(@Param("competenceTypeID") int competenceTypeID,@Param("subdomainID") int subdomainID);

	List<Map<String, Object>> getVendor(@Param("competenceTypeID") int competenceTypeID,@Param("subdomainID") int subdomainID,@Param("technologyID") int technologyID);

	List<Map<String, Object>> getCompetence();

	List<Map<String, Object>> getServiceArea(@Param("competenceTypeID") int competenceTypeID,
			@Param("subdomainID") int subdomainID, @Param("technologyID") int technologyID,@Param("vendorID") int vendorID);

	List<Map<String, Object>> getBaseline();

	List<Map<String, Object>> getAmbition(@Param("flag") String flag, @Param("competenceGradeID") int competenceGradeID);

	boolean insertCompetenceData(@Param("userCompetence") UserCompetenceModel userCompetence);

	String getManagerSignum(@Param("signum") String signum);

	HashMap<String,Object> getDeliveryCompetanceDetail(@Param("userCompetence") UserCompetenceModel userCompetence);

	HashMap<String, Object> getCompetenceUpgradeDetail(@Param("userCompetence") UserCompetenceModel userCompetence);

	List<Map<String, Object>> getUserCompetenceData(@Param("userCompetenceModel") UserCompetenceModel userCompetenceModel ,@Param("statusString")  String statusString,@Param("role")  String role);

	List<Map<String, Object>> getWBLData();

	void insertCompetenceServiceArea(@Param("competenceServiceAreaModel") CompetenceServiceAreaModel competenceServiceAreaModel);

	void insertDeliveryCompetenceServiceArea(@Param("competenceServiceAreaModel") CompetenceServiceAreaModel competenceServiceAreaModel);

	Boolean checkCompetenceExists(@Param("competenceServiceAreaModel") CompetenceServiceAreaModel competenceServiceAreaModel);

	List<Map<String, Object>> getAllCompetenceServiceArea();

	List<Map<String, Object>> getCompetenceUpgrade();

	void insertCompetenceDataBulk(@Param("bulk") BulkUploadModel bulk);

	Integer checkTrainingExists(@Param("competenceTrainingModel") CompetenceTrainingModel competenceTrainingModel);

	void insertTrainingCatalog(@Param("competenceTrainingModel") CompetenceTrainingModel competenceTrainingModel);

	void insertCompetanceTrainingDetail(@Param("competenceTrainingModel") CompetenceTrainingModel competenceTrainingModel);

	List<Map<String, Object>> getTrainingData(@Param("competenceID") int competenceID);

	Boolean enableCompExistence(@Param("competenceID") int competenceID);

	void enableDisableCompetence(@Param("competenceID") int competenceID,@Param("active") boolean active);

	void insertMasterTrainingDataBulk(@Param("bulk") BulkUploadModel bulk);

	void insertItmTrainingBulk(@Param("bulk") BulkUploadModel bulk);

	UserCompetenceModel getUserCompetenceRow(@Param("systemID") int systemID);

	void inactivateSystemID(@Param("parentSystemID") int parentSystemID);

	String getCompetenceGradeById(@Param("competenceGradeID") int competenceGradeID);

	String getCompetenceServiceAreaById(@Param("competanceID") int competanceID);

	String getCompetenceUpgradeById(@Param("competenceUpgradeID") int competenceUpgradeID);

	List<Map<String, Object>> getRequestRaisedCountByCompetanceType(@Param("userCompetenceModel") UserCompetenceModel userCompetenceModel);

	List<Map<String, Object>> getTotalRequestRaisedCount(@Param("userCompetenceModel") UserCompetenceModel userCompetenceModel);

	List<Map<String, Object>> getRaisedAmbitionCount(@Param("userCompetenceModel") UserCompetenceModel userCompetenceModel);

	boolean isValidRequest(@Param("userCompetenceModel") UserCompetenceModel userCompetence,
			@Param("deliveryCompetanceID") int deliveryCompetanceID, @Param("statusString") String statusString);

	String getDeployedEnv();

	List<Map<String, Object>> downloadCompetenceData();

	List<Map<String, Object>> getAllDomain();

	List<Map<String, Object>> getAllTechnology();

	List<Map<String, Object>> getAllVendor();

	Map<String, Object> getCompetenceDetailsBySystemID(@Param("systemID") int systemID);

	List<Map<String, Object>> getAllCompetenceType();

	List<UserCompetenceModel> getUserCompetenceDetailsByStatusDeliveryCompetenceID(
			@Param("deliveryCompetanceID") int deliveryCompetanceID, @Param("statusString") String statusString,
			@Param("userCompetenceModel") UserCompetenceModel userCompetence);

	boolean checkCompetenceTrainingMappingExists(@Param("competenceTrainingModel") CompetenceTrainingModel competenceTrainingModel);

	boolean validateInsertCompetenceData(@Param("newUserCompetenceModel")UserCompetenceModel newUserCompetenceModel);

	int getPreviousCompetenceID(@Param("newUserCompetenceModel") UserCompetenceModel newUserCompetenceModel);

	int getDeliveryCompetenceID(@Param("newUserCompetenceModel") UserCompetenceModel newUserCompetenceModel);

	boolean isUniqueCompetence(@Param("userCompetence") UserCompetenceModel userCompetence);

}
