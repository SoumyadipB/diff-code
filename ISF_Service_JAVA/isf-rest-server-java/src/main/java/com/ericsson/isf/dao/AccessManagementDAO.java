/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.hibernate.SessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.mapper.AccessManagementMapper;
import com.ericsson.isf.model.AccessProfileModel;
import com.ericsson.isf.model.AccessProfileRequestModel;
import com.ericsson.isf.model.AccessProfileUserDetailModel;
import com.ericsson.isf.model.AccessRequestApprovalModel;
import com.ericsson.isf.model.ApiManagerUserModel;
import com.ericsson.isf.model.AspLoginModel;
import com.ericsson.isf.model.CPMModel;
import com.ericsson.isf.model.CapabilityModel;
import com.ericsson.isf.model.CapabilityPageGroupModel;
import com.ericsson.isf.model.CityModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DemandTypeModel;
import com.ericsson.isf.model.DynamicMessageModel;
import com.ericsson.isf.model.EmployeeModel;
import com.ericsson.isf.model.EricssonOrganizationModel;
import com.ericsson.isf.model.ExternalSourceModel;
import com.ericsson.isf.model.GroupMenuModel;
import com.ericsson.isf.model.JwtUser;
import com.ericsson.isf.model.LocationTypeModel;
import com.ericsson.isf.model.OrganizationModel;
import com.ericsson.isf.model.RequestRoleAccessModel;
import com.ericsson.isf.model.ResetLoginAspModel;
import com.ericsson.isf.model.RoleModel;
import com.ericsson.isf.model.TokenApiMappingModel;
import com.ericsson.isf.model.TokenMappingModel;
import com.ericsson.isf.model.UserAccessProfileModel;
import com.ericsson.isf.model.UserDetailsAccessModel;
import com.ericsson.isf.model.UserImageURIModel;
import com.ericsson.isf.model.UserLocationAddressModel;
import com.ericsson.isf.model.UserLoginModel;
import com.ericsson.isf.model.UserPreferencesModel;
import com.ericsson.isf.model.UserProfileHistoryModel;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.IsfCustomIdInsert;

/**
 *
 * @author esanpup
 */
@Repository
public class AccessManagementDAO {

	@Qualifier("sqlSession")
	/* Create session from SQLSessionFactory */
	@Autowired
	private SqlSessionTemplate sqlSession;

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	private IsfCustomIdInsert isfCustomIdInsert;

	
	public List<OrganizationModel> getOrganizationList() {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getOrganizationList();
	}

	public List<AccessProfileModel> getAccessProfiles() {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getAccessProfiles();
	}

	public String assignAccessToUser(int roleID, int accessProfileID, String empSignumID, String loggedInUser, Date expiryDate) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.assignAccessToUser(roleID, accessProfileID, empSignumID, loggedInUser, expiryDate);
	}

	public String getAccessProfileName(int roleID, int accessProfileID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getAccessProfileName(roleID, accessProfileID);
	}

	public Boolean checkIFAccessDetailsExists(int accessProfileID, String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.checkIFAccessDetailsExists(accessProfileID, signumID);
	}

	public void disableAccessToUser(int userAccessProfID, int accessProfileID, String signumID, String modifiedBy) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.disableAccessToUser(userAccessProfID, accessProfileID, signumID, modifiedBy);
	}

	public void updateApprovalStatus(int userAccessProfID, String signumID, String approvalStatus, String approvedBy) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.updateApprovalStatus(userAccessProfID, signumID, approvalStatus, approvedBy);
	}

	public List<UserAccessProfileModel> searchAccessDetailsByFilter(String signumID, int roleID, int organisationID, String lineManagerSignum, DataTableRequest dataTableReq) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.searchAccessDetailsByFilter(signumID, roleID, organisationID, lineManagerSignum, dataTableReq);
	}

	public Boolean checkIFProfileExists(String profileName) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.checkIFProfileExists(profileName);
	}

	public void createNewAccessProfile(String profileName, int role, int organization) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.createNewAccessProfile(profileName, role, organization);
	}

	public List<AccessProfileModel> getAccessProfilesByRole(int roleID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getAccessProfilesByRole(roleID);
	}

	public Boolean IsRoleExists(int roleID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.IsRoleExists(roleID);
	}

	public Boolean IsSignumExists(String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.IsSignumExists(signumID);
	}

	public List<UserDetailsAccessModel> getUserAccessProfile(String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserAccessProfile(signumID);
	}

	public List<RoleModel> getRoleList() {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getRoleList();
	}

	public List<CPMModel> getCPMdetails(String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getCPMdetails(signumID);
	}

	public Boolean IsRoleAndOrgExists(int roleID, int orgID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.IsRoleAndOrgExists(roleID, orgID);
	}

	public List<String> getUserByFilter(int roleID, int orgID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserByFilter(roleID, orgID);
	}

	public List<String> getUsersByAlias(String alias, int orgID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUsersByAlias(alias, orgID);
	}

	public List<CapabilityModel> getCapability() {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getCapability();
	}

	public List<HashMap<String, Object>> getAccessEmployee() {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getAccessEmployee();
	}

	public void editAccessProfile(AccessProfileModel accessProfileModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.editAccessProfile(accessProfileModel);
	}

	public List<AccessProfileUserDetailModel> searchAccessDetails(int accessProfileID, int roleID, int organisationID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.searchAccessDetails(accessProfileID, roleID, organisationID);
	}

	public Boolean checkAccessProfileID(int accessProfileID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.checkAccessProfileID(accessProfileID);
	}

	public void deleteAccessProfile(int accessProfileID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.deleteAccessProfile(accessProfileID);
	}

	public List<UserDetailsAccessModel> getUserProfileByFilter(String signumID, int roleID, int organisationID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserProfileByFilter(signumID, roleID, organisationID);
	}

	public Boolean IsRoleDetailsExists(String roleID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.IsRoleDetailsExists(roleID);
	}

	public List<String> getUserDetailsForDR(String roleID, String marketArea, String projectID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserDetailsForDR(roleID, marketArea, projectID);
	}

	public List<String> getRPMByMarket(String marketArea) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getRPMByMarket(marketArea);
	}

	public void deleteDeliveryResponsible(int deliveryResponsibleID, String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.deleteDeliveryResponsible(deliveryResponsibleID, signumID);
	}

	public Boolean checkDeliveryResponsibleID(int deliveryResponsibleID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.checkDeliveryResponsibleID(deliveryResponsibleID);
	}

	public boolean updateAccessProfileStatus(AccessProfileRequestModel profileRequest) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.updateAccessProfileStatus(profileRequest);
	}

	public String requestAccessProfile(RequestRoleAccessModel requestRoleAccess) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.requestAccessProfile(requestRoleAccess);
	}

	public List<Map<String, Object>> requestAccessProfileDetail(RequestRoleAccessModel requestRoleAccess) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.requestAccessProfileDetail(requestRoleAccess);
	}

	public void insertCpmDetails(String mailID, String name, String signum) {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.insertCpmDetails(mailID, name, signum);

	}

	public void insertAccessProfileStatus(AccessProfileRequestModel profileRequest) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.insertAccessProfileStatus(profileRequest);
	}

	public boolean saveLoginDetails(UserLoginModel userLogin) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		boolean response=accessManagementMapper.saveLoginDetails(userLogin);
		
		int customId = isfCustomIdInsert.generateCustomId(userLogin.getLogID());
		userLogin.setLogID(customId);
		return response;
	}

	public List<UserLoginModel> getLoginHistory(String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getLoginHistory(signumID);
	}

	public void updateLoginHistory(String signumID, int logID, Date logOutdate) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.updateLoginHistory(signumID, logID, logOutdate);
	}

	public List<CapabilityPageGroupModel> getUserMenu() {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserMenu();

	}

	public Map<String, Object> getSiteStatus() {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getSiteStatus();
	}

	public List<Map<String, String>> checkEmpAsPM(String signumID, int projectID) {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.checkEmpAsPM(signumID, projectID);
	}

	public List<EricssonOrganizationModel> getEricssonOrgDetails() {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getEricssonOrgDetails();
	}

	public List<String> getEmployeeWithNoEmail() {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getEmployeeWithNoEmail();
	}

	public boolean updateEmployeeEmail(CPMModel cpmModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.updateEmployeeEmail(cpmModel);
	}

	public void updateAccessRole(RoleModel roleModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.updateAccessRole(roleModel);
	}

	public void deleteAccessRole(int accessRoleID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.deleteAccessRole(accessRoleID);
	}

	public void updateEmployeeData(String signum) {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.updateEmployeeData(signum);

	}

	public void updateEricssonOrg(List<String> newParts) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.updateEricssonOrg(newParts);
	}

	public void inactiveEricssonOrg() {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.inactiveEricssonOrg();
	}

	public boolean insertAspEmployee(AspLoginModel aspLogin, String generateMD5) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.insertAspEmployee(aspLogin, generateMD5);
	}

	public boolean insertAspEricssonEmployee(AspLoginModel aspLogin) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.insertAspEricssonEmployee(aspLogin);
	}

	public boolean insertUserAccessProfile(String signum) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.insertUserAccessProfile(signum);
	}

	public Map<String, Object> checkIfAspExists(ResetLoginAspModel resetLoginAsp) {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.checkIfAspExists(resetLoginAsp);
	}

	public boolean updateNewPassword(ResetLoginAspModel resetLoginAsp) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.updateNewPassword(resetLoginAsp);
	}

	public Map<String, Object> checkIfAspExistsByEmail(String email) {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.checkIfAspExistsByEmail(email);
	}

	public Map<String, Object> checkIfAspExistsBySignum(String signum) {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.checkIfAspExistsBySignum(signum);
	}

	 public  boolean deleteAccessProfileBySignum( String signum,AccessRequestApprovalModel  accessRequestApprovalModel) {
			AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
			return accessManagementMapper.deleteAccessProfileBySignum(signum, accessRequestApprovalModel);
		}

	public List<String> countToBeAdded() {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.countToBeAdded();
	}

	public List<String> countToBeDeleted() {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.countToBeDeleted();
	}

	public void deleteTmpTable() {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.deleteTmpTable();

	}

	public Map<String, Object> getDownStatus() {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getDownStatus();

	}

	
	public List<String> getMarketAreaBySignum(String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getMarketAreaBySignum(signumID);
	}

	public Boolean IsSignumExistsHRMS(String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.IsSignumExistsHRMS(signumID);
	}

	public void saveUserDetailsHRMS(String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.saveUserDetailsHRMS(signumID);

	}

	public void saveUserAccessProfileHRMS(String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.saveUserAccessProfileHRMS(signumID);
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getActivePrefByVal(String profileOrZone, String signum) {
		return sessionFactory.getCurrentSession()
				.createSQLQuery("select PreferencesId, DefaultValue from transactionalData.TBL_User_Preferences "
						+ "where IsActive=1 and DefaultName = '" + profileOrZone + "' and UserSignum='" + signum + "'")
				.list();
	}

	@SuppressWarnings("unchecked")
	public int deletePref(Integer prefId, String modifiedBy) {
		return sessionFactory.getCurrentSession()
				.createSQLQuery(
						"update transactionalData.TBL_User_Preferences " + "set IsActive=0, " + "LastModifiedBy='"
								+ modifiedBy + "', " + "LastModifiedOn= :now " + "where PreferencesId=" + prefId)
				.setTimestamp("now", new Date()).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public int addPref(String profileOrZone, UserPreferencesModel preferences) {

		if (profileOrZone.equalsIgnoreCase("profile")) {
			return sessionFactory.getCurrentSession()
					.createSQLQuery(AppConstants.INSERT_TBL_USER_PREFERENCES + AppConstants.USER_SIGNUM
							+ AppConstants.DEFAULT_NAME + AppConstants.DEFAULT_ID + AppConstants.DEFAULT_VALUE + AppConstants.CREATED_BY + AppConstants.CREATED_ON
							+ AppConstants.IS_ACTIVE + ") " + AppConstants.QUERY_VALUES + "( " + "'" + preferences.getUserSignum() + "', "
							+ " 'profile', " + preferences.getDefaultProfileId() + ", " + "'"
							+ preferences.getDefaultProfileValue() + "', " + "'" + preferences.getCreatedBy() + "', "
							+ AppConstants.NOW + 1 + ") ")
					.setTimestamp("now", new Date()).executeUpdate();
		} else if (profileOrZone.equalsIgnoreCase("timezone")) {
			return sessionFactory.getCurrentSession()
					.createSQLQuery(AppConstants.INSERT_TBL_USER_PREFERENCES + AppConstants.USER_SIGNUM
							+ AppConstants.DEFAULT_NAME + AppConstants.DEFAULT_ID + AppConstants.DEFAULT_VALUE + AppConstants.CREATED_BY + AppConstants.CREATED_ON
							+ AppConstants.IS_ACTIVE + ") " + AppConstants.QUERY_VALUES + "( " + "'" + preferences.getUserSignum() + "', "
							+ " 'timezone', " + preferences.getDefaultZoneId() + ", " + "'"
							+ preferences.getDefaultZoneValue() + "', " + "'" + preferences.getCreatedBy() + "', "
							+ AppConstants.NOW + 1 + ") ")
					.setTimestamp("now", new Date()).executeUpdate();

		} else {
			return sessionFactory.getCurrentSession()
					.createSQLQuery(
							AppConstants.INSERT_TBL_USER_PREFERENCES + AppConstants.USER_SIGNUM + AppConstants.DEFAULT_NAME
									+ AppConstants.DEFAULT_ID + AppConstants.DEFAULT_VALUE + AppConstants.CREATED_BY + AppConstants.CREATED_ON + AppConstants.IS_ACTIVE
									+ ") " + AppConstants.QUERY_VALUES + "( " + "'" + preferences.getUserSignum() + "', " + " 'page', "
									+ preferences.getDefaultPageId() + ", " + "'" + preferences.getDefaultPageName()
									+ "', " + "'" + preferences.getCreatedBy() + "', " + AppConstants.NOW + 1 + ") ")
					.setTimestamp("now", new Date()).executeUpdate();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getActivePrefBySignum(String signum) {
		return sessionFactory.getCurrentSession()
				.createSQLQuery("select DefaultName,DefaultId,DefaultValue from transactionalData.TBL_User_Preferences "
						+ "where IsActive=1 and UserSignum='" + signum + "'")
				.list();
	}

	public List<UserDetailsAccessModel> getUserSignumByEmail(UserDetailsAccessModel userDetailsAccessModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserSignumByEmail(userDetailsAccessModel);
	}

	public Map<String, Object> validateUserPassword(String decodedUserName, String decodedPassword) {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.validateUserPassword(decodedUserName, decodedPassword);
	}

	public void insertTokenMappingDetails(String token, String type, int sourceID, Date activationDate,
			Date expirationDate, int active, String signum, String employeeEmailID) {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.insertTokenMappingDetails(token, type, sourceID, activationDate, expirationDate, active,
				signum, employeeEmailID);
	}

	public void updateTokenMappingDetails(String token) {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.updateTokenMappingDetails(token);
	}

	public Map<String, Object> getAndValidateSource(String userName, String token, String path) {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getAndValidateSource(userName, token, path);
	}

	public boolean validateEmployeeEmailAndSignum(String employeeEmail) {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.validateEmployeeEmailAndSignum(employeeEmail);
	}

	public List<AccessProfileRequestModel> getAccessRequestsBySignum(String signum, String role,DataTableRequest dataTableReq) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getAccessRequestsBySignum(signum, role,dataTableReq);
	}

	public boolean updateAccessRequestStatus(AccessRequestApprovalModel accessRequestApprovalModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.updateAccessRequestStatus(accessRequestApprovalModel);
	}

	public boolean approveAccessRequest(AccessRequestApprovalModel accessRequestApprovalModel, String role) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.approveAccessRequest(accessRequestApprovalModel, role);
	}

	public List<AccessRequestApprovalModel> getRenewRequestsBySignum(String signum,String role) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getRenewRequestsBySignum(signum,role);
	}

	public boolean updateAccessStatus(AccessRequestApprovalModel accessRequestApprovalModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.updateAccessStatus(accessRequestApprovalModel);
	}

	public boolean checkIfRequestExists(RequestRoleAccessModel requestRoleAccess) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.checkIfRequestExists(requestRoleAccess);
	}

	public boolean checkIfAlreadyApproved(RequestRoleAccessModel requestRoleAccess) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.checkIfAlreadyApproved(requestRoleAccess);
	}

	public Map<String, Object> validateUiToken(String token) {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.validateUiToken(token);
	}

	public String getProfileNameById(Integer accessProfileId) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getProfileNameById(accessProfileId);
	}

	public List<String> getAllEmailforRole(String role) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getAllEmailforRole(role);
	}

	public List<String> getAllAdminEmail() {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getAllAdminEmail();
	}
	
	public List<Map<String, Object>> getPageAccessDetailsByRoleOfSignum(String signumID, String role) {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getPageAccessDetailsByRoleOfSignum(signumID, role);
	}

	public List<GroupMenuModel> getAllPaths(String name) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getAllPaths(name);
	}

	public List<UserDetailsAccessModel> getUserSignumByEmailHRMS(UserDetailsAccessModel userDetailsAccessModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserSignumByEmailHRMS(userDetailsAccessModel);
	}

	public boolean checkIfAspRegistered(String email) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.checkIfAspRegistered(email);
	}

	public String getEmployeeFromHrms(String signum) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getEmployeeFromHrms(signum);
	}

	public Boolean isSignumExistsEmp(String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.isSignumExistsEmp(signumID);
	}

	public void insertUnregisteredAsp(UserDetailsAccessModel userDetailsAccessModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.insertUnregisteredAsp(userDetailsAccessModel);

	}

	public List<RoleModel> getRoleListByType(String userType) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getRoleListByType(userType);
	}

	public List<Map<String, Object>> getPageAccessDetailsByAccessProfileId(Integer accessProfileId) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getPageAccessDetailsByAccessProfileId(accessProfileId);
	}

	public boolean updateLoginDetail(UserLoginModel userLogin) {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.updateLoginDetail(userLogin);
	}

	public List<String> getEmployeeWithNoEmailBySignum(String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getEmployeeWithNoEmailBySignum(signumID);
	}

	public UserImageURIModel getUserImageURIFromDB(String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserImageURIFromDB(signumID);
	}

	public void insertIamgeURI(UserImageURIModel imgData) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.insertIamgeURI(imgData);
	}

	public List<String> getEmployeeWithNoEmailHRMS() {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getEmployeeWithNoEmailHRMS();
	}

	public boolean updateEmployeeEmailHRMS(CPMModel cpmModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.updateEmployeeEmailHRMS(cpmModel);

	}

	public void insertTokenForExternalSource(JwtUser jwtUser, String token) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.insertTokenForExternalSource(jwtUser, token);
	}

	public boolean isRequestedAPIAllowedForSource(String requestedAPI, JwtUser jwtUser) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.isRequestedAPIAllowedForSource(requestedAPI, jwtUser);
	}

	public void insertTokenMappingDetails(TokenMappingModel tokenMappingModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.insertTokenMappingDetails1(tokenMappingModel);

	}

	public void insertHitCountForAPI(String requestedAPI, String token, JwtUser jwtUser) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.insertHitCountForAPI(requestedAPI, token, jwtUser);

	}

	public String getActiveToken(String externalSourceName) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getActiveToken(externalSourceName);
	}

	public void updateSourceTokenMappingDetails(String token) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.updateSourceTokenMappingDetails(token);
	}

	public List<UserDetailsAccessModel> getUserProfileBySignum(String emailID) {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserProfileBySignum(emailID);
	}

	public List<Map<String, Object>> getSubscribedApiList(String externalSourceName) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getSubscribedApiList(externalSourceName);
	}

	public void insertTokenApi(TokenApiMappingModel tokenApiMappingModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.insertTokenApi(tokenApiMappingModel);
	}

	public String getActiveApiToken(int requestedAPI, String ownerSignum, int externalRefID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getActiveApiToken(requestedAPI, ownerSignum, externalRefID);
	}

	public String getApiName(int requestedAPI) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getApiName(requestedAPI);
	}

	public String checkTokenForExternalRefId(int externalRefID, String ownerSignum) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.checkTokenForExternalRefId(externalRefID, ownerSignum);
	}

	public List<Map<String, Object>> getUserTokenList(String signum) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserTokenList(signum);
	}

	public boolean disableActiveToken(String externalSourceName, String signum) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.disableActiveToken(externalSourceName, signum);
	}

	public ExternalSourceModel getExternalReferenceDetails(int externalRefId) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getExternalReferenceDetails(externalRefId);
	}

	public boolean testUpdateWOModifiedDate(String signum) {
		
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.testUpdateWOModifiedDate(signum);
	}

	public boolean saveUserProfileHistory(UserProfileHistoryModel userProfileHistoryModel) {

		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.saveUserProfileHistory(userProfileHistoryModel);
	}

	public int getRoleIDByRoleName(String role) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		Integer roleIDByRoleName = accessManagementMapper.getRoleIDByRoleName(role);
		int role1 = roleIDByRoleName == null ? 0 : roleIDByRoleName;
		return role1;
	}

	public List<CapabilityPageGroupModel> getUserMenuByRole(int roleID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserMenuByRole(roleID);
	}

	public int getUserPreferredRole(String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		Integer userPreferredRole = accessManagementMapper.getUserPreferredRole(signumID);
		int role1 = userPreferredRole == null ? 0 : userPreferredRole;
		return role1;
	}

	public int getRoleIDByAccessProfileId(String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		Integer roleIDByAccessProfileId = accessManagementMapper.getRoleIDByAccessProfileId(signumID);
		int role1 = roleIDByAccessProfileId == null ? 0 : roleIDByAccessProfileId;
		return role1;
	}

	public List<AccessProfileModel> getUserAccessProfileBySignum(String signum) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserAccessProfileBySignum(signum);
	}

	public List<UserDetailsAccessModel> getAccessProfileOfUser(String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getAccessProfileOfUser(signumID);
	}

	public List<AccessProfileModel> getUserAccessProfileBySignumAndAccProfID(String signumID, int accProfId) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserAccessProfileBySignumAndAccProfID(signumID,accProfId);
	}


	public List<CityModel> getCityByCountryID( CityModel cityModel) {
		
		int pageSize = 50;
		int pageNo = 0;
		RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
		
		String term = cityModel.getTerm();
		String term1 = term.replaceAll("[^a-zA-Z0-9]","");
		cityModel.setTerm(term1);
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getCityByCountryID( rowBounds, cityModel);
	}

	public boolean saveUserLocation(String Signum, UserLocationAddressModel userLocationAddressModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.saveUserLocation(Signum,userLocationAddressModel);
		
		
	}

	public CityModel getUserLocationBySignum(String Signum) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserLocationBySignum(Signum);
		
	}

	public CityModel getCityId(String signum) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getCityId(signum);
		
	}

	public List<LocationTypeModel> getLocationType() {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getLocationType();
	}

	public String getEmployeeGroup(String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getEmployeeGroup(signumID);
	}

	public List<LocationTypeModel> getAllLocationTypes() {
		
	
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getAllLocationTypes();
		
	}
	
	public boolean editLocationType(String signum, LocationTypeModel locationTypeModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.editLocationType(signum,locationTypeModel);
	}

	public boolean addLocationType(String signum, LocationTypeModel locationTypeModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.addLocationType(signum,locationTypeModel);
	}

	public boolean checkDuplicateLocation(LocationTypeModel locationTypeModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.checkDuplicateLocation(locationTypeModel);
	}

	public LocationTypeModel fetchLocationTypeForEdit(LocationTypeModel locationTypeModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.fetchLocationTypeForEdit(locationTypeModel);
	}

	public void changeLocationTypeStatus(String signum,LocationTypeModel locationTypeModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.changeLocationTypeStatus(signum,locationTypeModel);
	}

	public void updatePreviousDefaultLocationType(String signum,LocationTypeModel locationTypeModel) {
	
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.updatePreviousDefaultLocationType(signum, locationTypeModel);		
	}
	

	public boolean getNotificationForExpiryDate(String signum) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
	    return accessManagementMapper.getNotificationForExpiryDate(signum);	
	}
	public UserLocationAddressModel getLocationTypeModelData(String signum) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
	    return accessManagementMapper.getLocationTypeModelData(signum);	
	}

	public void updateCurrentDefaultLocationType(String signum,LocationTypeModel locationTypeModel)
	{
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
	    accessManagementMapper.updateCurrentDefaultLocationType(signum, locationTypeModel);
		
	}

	public boolean isLocationTypeValidForChange(String signum,LocationTypeModel locationTypeModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
	    return accessManagementMapper.isLocationTypeValidForChange(signum,locationTypeModel);
	}

	public List<Integer> getUserCurrentDeviceOpenSessions(UserLoginModel userLogin) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
	    return accessManagementMapper.getUserCurrentDeviceOpenSessions(userLogin);
		
	}

	public void closeSessions(List<Integer> openLogIDs, String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
	    accessManagementMapper.closeSessions(openLogIDs, signumID);
		
	}

	public Boolean getActiveSessionForUser(String signum) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
	   return accessManagementMapper.getActiveSessionForUser(signum);
	}

	public boolean validateReferer(String referer, String apiName) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.validateReferer(referer, apiName);
	}
	public String checkIfRequestCountExceedsLimit(String accessProfileId, String signumId) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.checkIfRequestCountExceedsLimit(accessProfileId, signumId);
	}

	public void checkAndInsertUserAzureObjectDetails(UserDetailsAccessModel userDetailsAccessModel, String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.checkAndInsertUserAzureObjectDetails(userDetailsAccessModel, signumID);
	}
		public List<AccessProfileModel> getUserAccessProfileOFNetworkEngineer(String signumID, int accProfId) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserAccessProfileOFNetworkEngineer(signumID,accProfId);
	
	}

	public List<AccessProfileModel> getUserAccessProfileOFASP(String signumID, int accProfId) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserAccessProfileOFASP(signumID,accProfId);
	}
	
	public List<ApiManagerUserModel> getExpiredUsersDetails() {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getExpiredUsersDetails();
	}

	public void updateRevokedUsers(List<String> revokedSignums) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.updateRevokedUsers(revokedSignums);		
	}

    public List<DynamicMessageModel> getMessageTable() {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getMessageTable();
	}

	public String getRoleByAccessProfileId(String accessProfileId) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getRoleByAccessProfileId(accessProfileId);
	}

	public boolean checkDuplicateMessage(DynamicMessageModel dynamicMessageModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		   return accessManagementMapper.checkDuplicateMessage(dynamicMessageModel);
	}

	public boolean insertDownStatus(String signum, DynamicMessageModel dynamicMessageModel) {
		boolean flag=false;
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
			if(accessManagementMapper.insertDownStatus(signum, dynamicMessageModel)) {
				flag=true;
			}else {
				throw new ApplicationException(200, " Message not added successfully");
			}
		   return flag;
	}

	public boolean updateDownStatus(String signum, DynamicMessageModel dynamicMessageModel) {
		boolean flag=false;
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
			if(accessManagementMapper.updateDownStatus(signum, dynamicMessageModel)) {
				flag=true;
			}else {
				throw new ApplicationException(200, " Message not updated successfully");
			}
		   return flag;
	}
	
	public void enableCurrentMessage(String signum,DynamicMessageModel dynamicMessageModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
	    accessManagementMapper.enableCurrentMessage(signum, dynamicMessageModel);			
    }
	
	public void disableCurrentMessage(String signum,DynamicMessageModel dynamicMessageModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
	    accessManagementMapper.disableCurrentMessage(signum, dynamicMessageModel);			
    }

	public boolean checkDuplicateMessageForEdit(DynamicMessageModel dynamicMessageModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		   return accessManagementMapper.checkDuplicateMessageForEdit(dynamicMessageModel);
			}

	public List<DynamicMessageModel> getEnabledMessage() {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getEnabledMessage();	
		}

	public void disablePreviousEnabledMessage(String signum, DynamicMessageModel dynamicMessageModel) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
	    accessManagementMapper.disablePreviousEnabledMessage(signum, dynamicMessageModel);	
		
	}
	public ApiManagerUserModel getUserAzureObjectDetails(String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserAzureObjectDetails(signumID);
	}

	public List<String> getValidateJsonForApi(String apiName) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getValidateJsonForApi(apiName);
	}

	public String getUserSignum(String emailID, String employeeType) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserSignum(emailID,employeeType);
		
	}

	public UserDetailsAccessModel getUserDetails(String signum) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getUserDetails(signum);
	}

	public Boolean checkifProfileRequestExist(int accessProfileID, String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.checkifProfileRequestExist(accessProfileID, signumID);
	}
	
	public void updateProfileRequest(int accessProfileID, String signumID, String loggedInUser) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.updateProfileRequest(accessProfileID, signumID , loggedInUser);
	}

	public void insertDemandType(String demandType, String createdBy, String demandTypeDescription) {
	
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.insertDemandType(demandType, createdBy,demandTypeDescription);
			
	}

	public void updateDemandType(int demandTypeId, String updatedDemandType, String demandTypeDescription, String lastModifyBy) {
		AccessManagementMapper accessManagementMapper= sqlSession.getMapper(AccessManagementMapper.class);
		accessManagementMapper.updateDemandType(demandTypeId,updatedDemandType,demandTypeDescription,lastModifyBy);
		
	}

	public void inactiveDemandType(int demandTypeId, String lastModifyBy) {
		AccessManagementMapper accessManagementMapper= sqlSession.getMapper(AccessManagementMapper.class);
		 accessManagementMapper.inactiveDemandType(demandTypeId,lastModifyBy);
	}

	public boolean isValidDemandTypeId(int demandTypeId) {
		AccessManagementMapper accessManagementMapper= sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.isValidDemandTypeId(demandTypeId);
	}
	
	public Boolean isSignumExistsEmpAndNotResigned(String lastModifyBy) {
		AccessManagementMapper accessManagementMapper= sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.isSignumExistsEmpAndNotResigned(lastModifyBy);
	}

	public boolean isSameDemandTypeAlreadyExist(String demandType) {
		AccessManagementMapper accessManagementMapper= sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.isSameDemandTypeAlreadyExist(demandType);
	}
	public List<DemandTypeModel> getDemandType() {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.getDemandType();
	}

	
    public boolean ifProfileExistswithsSignum(String signum, Integer accProfId) {
		AccessManagementMapper accessManagementMapper= sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.ifProfileExistswithsSignum(signum, accProfId);
	}

    public EmployeeModel getEmployeeGroupAndSignum(String signum) {
	    AccessManagementMapper accessManagementMapper= sqlSession.getMapper(AccessManagementMapper.class);
	    return accessManagementMapper.getEmployeeGroupAndSignum(signum);
    }

    public String getPreviousOneDemandType(int demandTypeId) {
	     AccessManagementMapper accessManagementMapper= sqlSession.getMapper(AccessManagementMapper.class);
	     return accessManagementMapper.getPreviousOneDemandType(demandTypeId);
    }

   public boolean isSameDemandTypeAlreadyExistOtherThanThePresentOne(int demandTypeId, String demandType) {
	     AccessManagementMapper accessManagementMapper= sqlSession.getMapper(AccessManagementMapper.class);
	     return accessManagementMapper.isSameDemandTypeAlreadyExistOtherThanThePresentOne(demandTypeId,demandType);
   }

	

	public boolean isSignumExistsEmpAndNotInResigned(String signumID) {
		AccessManagementMapper accessManagementMapper = sqlSession.getMapper(AccessManagementMapper.class);
		return accessManagementMapper.isSignumExistsEmpAndNotInResigned(signumID);
	}
}
