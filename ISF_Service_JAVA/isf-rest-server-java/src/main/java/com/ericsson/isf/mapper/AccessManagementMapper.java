/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.mapper;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

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
import com.ericsson.isf.model.CountryModel;
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
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.RoleModel;
import com.ericsson.isf.model.TokenApiMappingModel;
import com.ericsson.isf.model.TokenMappingModel;
import com.ericsson.isf.model.UserAccessProfileModel;
import com.ericsson.isf.model.UserDetailsAccessModel;
import com.ericsson.isf.model.UserImageURIModel;
import com.ericsson.isf.model.UserLocationAddressModel;
import com.ericsson.isf.model.UserLoginModel;
import com.ericsson.isf.model.UserProfileHistoryModel;


/**
 *
 * @author esanpup
 */
public interface AccessManagementMapper {

    public List<OrganizationModel> getOrganizationList();

    public List<AccessProfileModel> getAccessProfiles();
    
    public List<AccessProfileModel> getAccessProfilesByRole(@Param("roleID") int roleID);

    public String assignAccessToUser(@Param("roleID")int roleID, 
                                   @Param("accessProfileID") int accessProfileID, 
                                   @Param("empSignumID") String empSignumID,
                                   @Param("loggedInUser") String loggedInUser,
                                   @Param("expiryDate") Date expiryDate);

    public String getAccessProfileName(@Param("roleID") int roleID,
                                     @Param("accessProfileID") int accessProfileID);

    public Boolean checkIFAccessDetailsExists(@Param("accessProfileID") int accessProfileID,
                                              @Param("signumID") String signumID);

    public void disableAccessToUser(@Param("userAccessProfID")int userAccessProfID,
                                    @Param("accessProfileID") int accessProfileID,
                                    @Param("signumID") String signumID,
                                    @Param("modifiedBy") String modifiedBy);

    public void updateApprovalStatus(@Param("userAccessProfID") int userAccessProfID,
                                     @Param("signumID") String signumID, 
                                     @Param("approvalStatus") String approvalStatus,
                                     @Param("approvedBy") String approvedBy);

    public List<UserAccessProfileModel> searchAccessDetailsByFilter(@Param("signumID") String signumID,
                                         @Param("roleID") int roleID,
                                         @Param("organisationID") int organisationID, 
                                         @Param("lineManagerSignum") String lineManagerSignum, 
                                         @Param("dataTableReq") DataTableRequest dataTableReq);
    
    public Boolean checkIFProfileExists(@Param("profileName") String profileName);
    
    public void createNewAccessProfile(@Param("profileName") String profileName,
                                     @Param("roleID") int role, 
                                     @Param("organizationID") int organization);
     
    public Boolean IsRoleExists(@Param("roleID") int roleID);

    public Boolean IsSignumExists(@Param("signumID") String signumID);
    
    public List<UserDetailsAccessModel> getUserAccessProfile(@Param("signumID") String signumID);

    public List<RoleModel> getRoleList();
   
    
    public List<CPMModel> getCPMdetails(@Param("signumID") String signumID);
    
    public Boolean IsRoleAndOrgExists(@Param("roleID") int roleID,
                                     @Param("orgID") int orgID);
    
    public List<String> getUserByFilter(@Param("roleID") int roleID,
                                  @Param("orgID") int orgID);
    
    public List<String> getUsersByAlias(@Param("alias") String alias,
            @Param("orgID") int orgID);
    
    
    public List<CapabilityModel> getCapability();
    public List<HashMap<String, Object>> getAccessEmployee();

    public List<UserDetailsAccessModel> getUserProfileByFilter(@Param("signumID") String signumID,@Param("roleID") int roleID,@Param("organisationID") int organisationID);
    
    public void editAccessProfile(@Param("accessProfile") AccessProfileModel accessProfileModel);
    
    public Boolean checkAccessProfileID(@Param("accessProfileID") int accessProfileID);
    
    public void deleteAccessProfile(@Param("accessProfileID") int accessProfileID);
    
	public List<AccessProfileUserDetailModel> searchAccessDetails(@Param("accessProfileID") int accessProfileID,@Param("roleID") int roleID,@Param("organisationID") int organisationID);
    public Boolean IsRoleDetailsExists(@Param("roleID") String roleID);

    public List<String> getUserDetailsForDR(@Param("roleID") String roleID,@Param("marketArea") String marketArea,@Param("projectID") String projectID);

    public void deleteDeliveryResponsible(@Param("deliveryResponsibleID") int deliveryResponsibleID, @Param("signumID") String signumID);

    public boolean checkDeliveryResponsibleID(@Param("deliveryResponsibleID") int deliveryResponsibleID);

	public boolean updateAccessProfileStatus(@Param("profileRequest") AccessProfileRequestModel profileRequest);
	
	public void insertAccessProfileStatus(@Param("profileRequest") AccessProfileRequestModel profileRequest);
	
	public String requestAccessProfile(@Param("requestRoleAccess") RequestRoleAccessModel requestRoleAccess);

	public List<Map<String,Object>> requestAccessProfileDetail(@Param("requestRoleAccess") RequestRoleAccessModel requestRoleAccess);

	public String requestNameBySignum(@Param("requestRoleAccess")  RequestRoleAccessModel requestRoleAccess);

	public void insertCpmDetails(@Param("mailID") String mailID, @Param("name") String name,@Param("signum") String signum);

    public boolean saveLoginDetails(@Param("userLogin") UserLoginModel userLogin);

    public void updateLoginHistory(@Param("signumID") String signumID, @Param("logID") int logID, @Param("logOutdate") Date logOutdate);

    public List<UserLoginModel> getLoginHistory(@Param("signumID") String signumID);

	public List<CapabilityPageGroupModel> getUserMenu();

	public Map<String, Object> getSiteStatus();

	public List<Map<String, String>> checkEmpAsPM(@Param("signumID") String signumID, @Param("projectID") int projectID);

	public List<EricssonOrganizationModel> getEricssonOrgDetails();

	public List<String> getEmployeeWithNoEmail();

	public boolean updateEmployeeEmail(@Param("cpmModel") CPMModel cpmModel);

	public void updateEmployeeData(@Param("signum") String signum);

	public void updateEricssonOrg(@Param("newParts") List<String> newParts);

	public void inactiveEricssonOrg();

	public boolean insertAspEmployee(@Param("aspLogin") AspLoginModel aspLogin, @Param("generateMD5") String generateMD5);

	public boolean insertAspEricssonEmployee(@Param("aspLogin") AspLoginModel aspLogin);

	public boolean insertUserAccessProfile(@Param("signum") String signum);

	public Map<String, Object> checkIfAspExists(@Param("resetLoginAsp") ResetLoginAspModel resetLoginAsp);

	public boolean updateNewPassword(@Param("resetLoginAsp") ResetLoginAspModel resetLoginAsp);
	public void updateAccessRole( @Param("roleModel") RoleModel roleModel);
	public void deleteAccessRole(@Param("accessRoleID") int accessRoleID);

	public Map<String, Object> checkIfAspExistsByEmail(@Param("email") String email);

	public Map<String, Object> checkIfAspExistsBySignum(@Param("signum") String signum);
	public  boolean deleteAccessProfileBySignum(@Param("signum") String signum, @Param("accessRequestApprovalModel")  AccessRequestApprovalModel  accessRequestApprovalModel);

	public List<String> countToBeAdded();

	public List<String> countToBeDeleted();

	public void deleteTmpTable();
	public Map<String, Object> getDownStatus();
	
 public List<String> getMarketAreaBySignum(@Param("signumID") String signumID);

	public Boolean IsSignumExistsHRMS(@Param("signumID") String signumID);

	public void saveUserDetailsHRMS(@Param("signumID") String signumID);

	public void saveUserAccessProfileHRMS(@Param("signumID") String signumID);
	public List<UserDetailsAccessModel> getUserSignumByEmail(@Param("userDetailsAccessModel") UserDetailsAccessModel userDetailsAccessModel);

	public Map<String, Object> validateUserPassword(@Param("decodedUserName") String decodedUserName,@Param("decodedPassword") String decodedPassword);

	public void insertTokenMappingDetails(@Param("token") String token,@Param("type") String type,@Param("sourceID") int sourceID,@Param("activationDate") Date activationDate,
			@Param("expirationDate") Date expirationDate, @Param("active") int active,@Param("signum") String signum,@Param("employeeEmailID") String employeeEmailID);

	public void updateTokenMappingDetails(@Param("token") String token);

	public Map<String, Object> getAndValidateSource(@Param("userName") String userName,@Param("token") String token,@Param("path")  String path);

	public boolean validateEmployeeEmailAndSignum(@Param("employeeEmail") String employeeEmail);
    
	public List<AccessProfileRequestModel> getAccessRequestsBySignum(@Param("signum") String signum,@Param("role") String role,	@Param("dataTableReq") DataTableRequest dataTableReq);
//	public int getAccessRequestsBySignumCount(@Param("signum") String signum,@Param("role") String role,@Param("dataTableReq") DataTableRequest dataTableReq);
	
	public boolean updateAccessRequestStatus(@Param("accessRequestApprovalModel") AccessRequestApprovalModel accessRequestApprovalModel);
	public boolean approveAccessRequest(@Param("accessRequestApprovalModel") AccessRequestApprovalModel accessRequestApprovalModel,@Param("role") String role);

	public List<AccessRequestApprovalModel> getRenewRequestsBySignum(@Param("signum") String signum,@Param("role") String role);
	public boolean updateAccessStatus(@Param("accessRequestApprovalModel") AccessRequestApprovalModel accessRequestApprovalModel);

	public boolean checkIfRequestExists(@Param("requestRoleAccess") RequestRoleAccessModel requestRoleAccess);
	public boolean checkIfAlreadyApproved(@Param("requestRoleAccess") RequestRoleAccessModel requestRoleAccess);

	
	public Map<String, Object> validateUiToken(@Param("token") String token);
	public String getProfileNameById(@Param("accessProfileId") Integer accessProfileId);
	
	public List<String> getAllEmailforRole(@Param("role") String role);

	public List<String> getAllAdminEmail();
	
	public List<Map<String, Object>> getPageAccessDetailsByRoleOfSignum(@Param("signumID") String signumID,@Param("role") String role);

	public List<GroupMenuModel> getAllPaths(@Param("name") String name);

	public List<UserDetailsAccessModel> getUserSignumByEmailHRMS(@Param("userDetailsAccessModel") UserDetailsAccessModel userDetailsAccessModel);

	public boolean checkIfAspRegistered(@Param("email")String email);

	public String getEmployeeFromHrms(@Param("signum")String signum);

	public Boolean isSignumExistsEmp(@Param("signumID") String signumID);

	public void insertUnregisteredAsp(@Param("userDetailsAccessModel") UserDetailsAccessModel userDetailsAccessModel);

	public List<RoleModel> getRoleListByType(@Param("userType")String userType);

	public List<Map<String, Object>> getPageAccessDetailsByAccessProfileId(@Param("accessProfileId")Integer accessProfileId);

	public boolean updateLoginDetail(@Param("userLogin") UserLoginModel userLogin);

	public List<String> getEmployeeWithNoEmailBySignum(@Param("signumID") String signumID);

	public List<String> getRPMByMarket(@Param("marketArea") String marketArea);

	public UserImageURIModel getUserImageURIFromDB(@Param("signumID") String signumID);

	public void insertIamgeURI(@Param("imgData") UserImageURIModel imgData);
	
	public List<String> getEmployeeWithNoEmailHRMS();

	public boolean updateEmployeeEmailHRMS(@Param("cpmModel") CPMModel cpmModel);

	public void insertTokenForExternalSource(@Param("jwtUser") JwtUser jwtUser,
			@Param("token") String token);

	public boolean isRequestedAPIAllowedForSource(@Param("requestedAPI") String requestedAPI, @Param("jwtUser") JwtUser jwtUser);

	public void insertTokenMappingDetails1(@Param("tokenMappingModel") TokenMappingModel tokenMappingModel);

	public void insertHitCountForAPI(@Param("requestedAPI") String requestedAPI, @Param("token") String token, @Param("jwtUser") JwtUser jwtUser);

	public String getActiveToken(@Param("externalSourceName") String externalSourceName);

	public void updateSourceTokenMappingDetails(@Param("token") String token);

	public List<UserDetailsAccessModel> getUserProfileBySignum(@Param("emailID")  String emailID);

	public List<Map<String,Object>> getSubscribedApiList(@Param("externalSourceName") String externalSourceName);

	public void insertTokenApi(@Param("tokenApiMappingModel") TokenApiMappingModel tokenApiMappingModel);

	public String getActiveApiToken(@Param("requestedAPI") int requestedAPI,@Param("ownerSignum") String ownerSignum,@Param("externalRefID") int externalRefID);

	public String getApiName(@Param("requestedAPI") int requestedAPI);

	public String checkTokenForExternalRefId(@Param("externalRefID") int externalRefID, @Param("ownerSignum") String ownerSignum);

	public List<Map<String,Object>> getUserTokenList(@Param("signum") String signum);

	public boolean disableActiveToken(@Param("externalSourceName") String externalSourceName, @Param("signum") String signum);

	public ExternalSourceModel getExternalReferenceDetails(@Param("externalRefId")int externalRefId);

	public boolean testUpdateWOModifiedDate(@Param("signum") String signum);

	public boolean saveUserProfileHistory(@Param("userProfileHistoryModel")UserProfileHistoryModel userProfileHistoryModel);

	public List<CityModel> getCityByCountryID( RowBounds rowBounds, @Param("cityModel") CityModel cityModel);

	public boolean saveUserLocation(@Param("Signum") String Signum, @Param("userLocationAddressModel") UserLocationAddressModel userLocationAddressModel);

	public CityModel getUserLocationBySignum(@Param("Signum") String Signum);

	public CityModel getCityId(@Param("signum")String signum);

	public Integer getRoleIDByRoleName(@Param("role") String role);

	public List<CapabilityPageGroupModel> getUserMenuByRole(@Param("roleID") int roleID);

	public Integer getUserPreferredRole(@Param("signumID") String signumID);

	/*
	 * public Integer getRoleIDByAccessProfileId(@Param("accProfId") int accProfId);
	 */
	public Integer getRoleIDByAccessProfileId(@Param("signumID") String signumID);
	

	public List<AccessProfileModel> getUserAccessProfileBySignum(@Param("signum") String signum);

	public List<UserDetailsAccessModel> getAccessProfileOfUser(@Param("signumID") String signumID);

	public List<AccessProfileModel> getUserAccessProfileBySignumAndAccProfID(@Param("signumID") String signumID,@Param("accProfId") int accProfId);

	public String getEmployeeGroup(@Param("signumID") String signumID);

	public List<LocationTypeModel> getLocationType();

	public List<LocationTypeModel> getAllLocationTypes();

	public boolean editLocationType(@Param("signum") String signum, @Param("locationTypeModel") LocationTypeModel locationTypeModel);

	public boolean addLocationType(@Param("signum") String signum, @Param("locationTypeModel") LocationTypeModel locationTypeModel);

	public boolean checkDuplicateLocation(@Param("locationTypeModel") LocationTypeModel locationTypeModel);

	public LocationTypeModel fetchLocationTypeForEdit(@Param("locationTypeModel") LocationTypeModel locationTypeModel);

	public void changeLocationTypeStatus(@Param("signum") String signum,@Param("locationTypeModel") LocationTypeModel locationTypeModel);

	public void updatePreviousDefaultLocationType(@Param("signum") String signum, @Param("locationTypeModel") LocationTypeModel locationTypeModel);

	public void updateCurrentDefaultLocationType(@Param("signum") String signum, @Param("locationTypeModel") LocationTypeModel locationTypeModel);

	public  boolean getNotificationForExpiryDate(@Param("signum") String signum);
	
	public  UserLocationAddressModel getLocationTypeModelData(@Param("signum") String signum);

	public boolean isLocationTypeValidForChange(@Param("signum") String signum,@Param("locationTypeModel") LocationTypeModel locationTypeModel);

	public List<Integer> getUserCurrentDeviceOpenSessions(@Param("userLogin") UserLoginModel userLogin);

	public void closeSessions(@Param("openLogIDs")List<Integer> openLogIDs, @Param("signumID") String signumID);

	public Boolean getActiveSessionForUser(@Param("signum")String signum);

	public boolean validateReferer(@Param("referer") String referer,@Param("apiName") String apiName);
	
	public void checkAndInsertUserAzureObjectDetails(@Param("userDetailsAccess") UserDetailsAccessModel userDetailsAccessModel,@Param("signumID") String signumID);

	public ApiManagerUserModel getUserAzureObjectDetails(@Param("signumID") String signumID);
	
	public List<ApiManagerUserModel> getExpiredUsersDetails();

	public void updateRevokedUsers(@Param("revokedSignums") List<String> revokedSignums);
	
	public String checkIfRequestCountExceedsLimit(@Param("accessProfileId") String accessProfileId, @Param("signumId") String signumId);

	public List<AccessProfileModel> getUserAccessProfileOFNetworkEngineer(@Param("signumID") String signumID,@Param("accProfId") int accProfId);

	public List<AccessProfileModel> getUserAccessProfileOFASP(@Param("signumID") String signumID,@Param("accProfId") int accProfId);

	
	public List<DynamicMessageModel> getMessageTable();

	public String getRoleByAccessProfileId(@Param("accessProfileId") String accessProfileId);

	public boolean checkDuplicateMessage(@Param("dynamicMessageModel") DynamicMessageModel dynamicMessageModel);

	public boolean insertDownStatus(@Param("signum") String signum, @Param("dynamicMessageModel") DynamicMessageModel dynamicMessageModel);

	public boolean updateDownStatus(@Param("signum") String signum, @Param("dynamicMessageModel") DynamicMessageModel dynamicMessageModel);
	
	public void enableCurrentMessage(@Param("signum") String signum, @Param("dynamicMessageModel") DynamicMessageModel dynamicMessageModel);
	
	public void disableCurrentMessage(@Param("signum") String signum, @Param("dynamicMessageModel") DynamicMessageModel dynamicMessageModel);

	public boolean checkDuplicateMessageForEdit(@Param("dynamicMessageModel") DynamicMessageModel dynamicMessageModel);

	public List<DynamicMessageModel> getEnabledMessage();

	public void disablePreviousEnabledMessage( @Param("signum") String signum,  @Param("dynamicMessageModel") DynamicMessageModel dynamicMessageModel);

	public List<String> getValidateJsonForApi(@Param("apiName") String apiName);

	public String getUserSignum(@Param("emailID") String emailID,@Param("employeeType") String employeeType);

	public UserDetailsAccessModel getUserDetails(@Param("signum") String signum);
	
	public Boolean checkifProfileRequestExist(@Param("accessProfileID") int accessProfileID, @Param("signumID") String signumID);
	
	public void updateProfileRequest(
            @Param("accessProfileID") int accessProfileID, 
            @Param("signumID") String signumID,
            @Param("loggedInUser") String loggedInUser);

	public void insertDemandType(@Param("demandType")String demandType, @Param("createdBy")String createdBy, @Param("demandTypeDescription")String demandTypeDescription);
	
	public void updateDemandType(@Param("demandTypeId")int demandTypeId,@Param("updatedDemandType")String updatedDemandType, @Param("demandTypeDescription")String demandTypeDescription,@Param("lastModifyBy")String lastModifyBy);

	public void inactiveDemandType(@Param("demandTypeId")int demandTypeId,@Param("lastModifyBy")String lastModifyBy);

	public boolean isValidDemandTypeId(@Param("demandTypeId") int demandTypeId);

	public Boolean isSignumExistsEmpAndNotResigned(@Param("lastModifyBy") String lastModifyBy);

	public boolean isSameDemandTypeAlreadyExist(@Param("demandType") String demandType);
	public List<DemandTypeModel> getDemandType();

	public boolean ifProfileExistswithsSignum(@Param("signumID") String signum,@Param("accProfId") Integer accProfId);

	public EmployeeModel getEmployeeGroupAndSignum(@Param("signum")String signum);

	public String getPreviousOneDemandType(@Param("demandTypeId") int demandTypeId);

	public boolean isSameDemandTypeAlreadyExistOtherThanThePresentOne(@Param("demandTypeId") int demandTypeId,@Param("demandType") String demandType);





	public boolean isSignumExistsEmpAndNotInResigned(@Param("signumID") String signumID);
}

