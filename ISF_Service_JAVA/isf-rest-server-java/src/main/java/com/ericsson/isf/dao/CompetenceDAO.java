package com.ericsson.isf.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.mapper.CompetenceMapper;
import com.ericsson.isf.model.BulkUploadModel;
import com.ericsson.isf.model.CompetenceServiceAreaModel;
import com.ericsson.isf.model.CompetenceTrainingModel;
import com.ericsson.isf.model.UserCompetenceModel;

/**
 * 
 * @author eakinhm
 *
 */
@Repository
public class CompetenceDAO {

	@Qualifier("sqlSession")

	/* Create session from SQLSessionFactory */
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Autowired
	SessionFactory sessionFactory;

	public List<Map<String, Object>> getDomain(int competenceTypeID) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getDomain(competenceTypeID);
	}

	public List<Map<String, Object>> getTechnology(int competenceTypeID, int subdomainID) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getTechnology(competenceTypeID, subdomainID);
	}

	public List<Map<String, Object>> getVendor(int competenceTypeID, int subdomainID, int technologyID) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getVendor(competenceTypeID,subdomainID,technologyID );
	}

	public List<Map<String, Object>> getCompetence() {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getCompetence();
	}

	public List<Map<String, Object>> getServiceArea(int competenceTypeID, int subdomainID, int technologyID, int vendorID) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getServiceArea(competenceTypeID,subdomainID,technologyID, vendorID);
	}

	public List<Map<String, Object>> getBaseline() {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getBaseline();
	}

	public List<Map<String, Object>> getAmbition(String flag, int competenceGradeID) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getAmbition(flag,competenceGradeID);
	}

	public boolean insertCompetenceData(UserCompetenceModel userCompetence) {
		boolean isSuccess=false;
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		isSuccess = competenceMapper.insertCompetenceData(userCompetence);
		if(userCompetence.getParentSystemID()!=0) {
			inactivateSystemID(userCompetence.getParentSystemID());
		}
		return isSuccess;
	}

	public String getManagerSignum(String signum) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getManagerSignum(signum);
	}

	public HashMap<String,Object> getDeliveryCompetanceDetail(UserCompetenceModel userCompetence) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getDeliveryCompetanceDetail(userCompetence);
	}

	public HashMap<String, Object> getCompetenceUpgradeDetail(UserCompetenceModel userCompetence) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getCompetenceUpgradeDetail(userCompetence);
	}

	public List<Map<String, Object>> getUserCompetenceData(UserCompetenceModel userCompetenceModel, String statusString, String role) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getUserCompetenceData(userCompetenceModel, statusString, role);
	}

	public List<Map<String, Object>> getWBLData() {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getWBLData();
	}

	public void insertCompetenceServiceArea(CompetenceServiceAreaModel competenceServiceAreaModel) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		  competenceMapper.insertCompetenceServiceArea(competenceServiceAreaModel);
		
	}

	public void insertDeliveryCompetenceServiceArea(CompetenceServiceAreaModel competenceServiceAreaModel) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		  competenceMapper.insertDeliveryCompetenceServiceArea(competenceServiceAreaModel);
	}

	public Boolean checkCompetenceExists(CompetenceServiceAreaModel competenceServiceAreaModel) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		  return competenceMapper.checkCompetenceExists(competenceServiceAreaModel);
	}

	public List<Map<String, Object>> getAllCompetenceServiceArea() {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		  return competenceMapper.getAllCompetenceServiceArea();
	}

	public List<Map<String, Object>> getCompetenceUpgrade() {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		  return competenceMapper.getCompetenceUpgrade();
	}

	public void insertCompetenceDataBulk(BulkUploadModel bulk) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		competenceMapper.insertCompetenceDataBulk(bulk);
	}


	public Integer checkTrainingExists(CompetenceTrainingModel competenceTrainingModel) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		  return competenceMapper.checkTrainingExists(competenceTrainingModel);
	}

	public void insertTrainingCatalog(CompetenceTrainingModel competenceTrainingModel) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		  competenceMapper.insertTrainingCatalog(competenceTrainingModel);
		
	}

	public void insertCompetanceTrainingDetail(CompetenceTrainingModel competenceTrainingModel) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		  competenceMapper.insertCompetanceTrainingDetail(competenceTrainingModel);
	}

	public List<Map<String, Object>> getTrainingData(int competenceID) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		  return competenceMapper.getTrainingData(competenceID);
	}

	public Boolean enableCompExistence(int competenceID) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		  return competenceMapper.enableCompExistence(competenceID);
	}

	public void enableDisableCompetence(int competenceID, boolean active) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		  competenceMapper.enableDisableCompetence(competenceID,active);
		
	}

	public void insertMasterTrainingDataBulk(BulkUploadModel bulk) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		  competenceMapper.insertMasterTrainingDataBulk(bulk);
		
	}


	public void insertItmTrainingBulk(BulkUploadModel bulk) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		  competenceMapper.insertItmTrainingBulk(bulk);
		
	}

	public UserCompetenceModel getUserCompetenceRow(int systemID) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getUserCompetenceRow(systemID);
	}

	public void inactivateSystemID(int parentSystemID) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		competenceMapper.inactivateSystemID(parentSystemID);
	}

	public String getCompetenceGradeById(int competenceGradeID) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getCompetenceGradeById(competenceGradeID);
	}

	public String getCompetenceServiceAreaById(int competanceID) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getCompetenceServiceAreaById(competanceID);
	}

	public String getCompetenceUpgradeById(int competenceUpgradeID) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getCompetenceUpgradeById(competenceUpgradeID);
	}

	public List<Map<String, Object>> getRequestRaisedCountByCompetanceType(UserCompetenceModel userCompetenceModel) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getRequestRaisedCountByCompetanceType(userCompetenceModel);
	}

	public List<Map<String, Object>> getTotalRequestRaisedCount(UserCompetenceModel userCompetenceModel) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getTotalRequestRaisedCount(userCompetenceModel);
	}

	public List<Map<String, Object>> getRaisedAmbitionCount(UserCompetenceModel userCompetenceModel) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getRaisedAmbitionCount(userCompetenceModel);
	}

	public boolean isValidRequest(UserCompetenceModel userCompetence, int deliveryCompetanceID, String statusString) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.isValidRequest(userCompetence, deliveryCompetanceID, statusString);
	}

	public String getDeployedEnv() {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getDeployedEnv();
	}

	public List<Map<String, Object>> downloadCompetenceData() {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.downloadCompetenceData();
	}

	public List<Map<String, Object>> getAllDomain() {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getAllDomain();
	}

	public List<Map<String, Object>> getAllTechnology() {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getAllTechnology();	}

	public List<Map<String, Object>> getAllVendor() {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getAllVendor();	}

	public Map<String, Object> getCompetenceDetailsBySystemID(int systemID) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getCompetenceDetailsBySystemID(systemID);
	}

	public List<Map<String, Object>> getAllCompetenceType() {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getAllCompetenceType();
	}

	public List<UserCompetenceModel> getUserCompetenceDetailsByStatusDeliveryCompetenceID(int deliveryCompetanceID,
			String statusString, UserCompetenceModel userCompetence) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getUserCompetenceDetailsByStatusDeliveryCompetenceID(deliveryCompetanceID,
				statusString, userCompetence);
	}

	public boolean checkCompetenceTrainingMappingExists(CompetenceTrainingModel competenceTrainingModel) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.checkCompetenceTrainingMappingExists(competenceTrainingModel);
	}

	public boolean validateInsertCompetenceData(UserCompetenceModel newUserCompetenceModel) 
	{
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.validateInsertCompetenceData(newUserCompetenceModel);
	}

	public int getPreviousCompetenceID(UserCompetenceModel newUserCompetenceModel) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getPreviousCompetenceID(newUserCompetenceModel);
	}

	public int getDeliveryCompetenceID(UserCompetenceModel newUserCompetenceModel) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.getDeliveryCompetenceID(newUserCompetenceModel);
	}

	public boolean isUniqueCompetence(UserCompetenceModel userCompetence) {
		CompetenceMapper competenceMapper = sqlSession.getMapper(CompetenceMapper.class);
		return competenceMapper.isUniqueCompetence(userCompetence);
	}

}
