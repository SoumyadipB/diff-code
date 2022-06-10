/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.controller;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.model.AccessProfileModel;
import com.ericsson.isf.model.AccessProfileRequestModel;
import com.ericsson.isf.model.AccessProfileUserDetailModel;
import com.ericsson.isf.model.AccessRequestApprovalModel;
import com.ericsson.isf.model.AspLoginModel;
import com.ericsson.isf.model.AuthRequestModel;
import com.ericsson.isf.model.CPMModel;
import com.ericsson.isf.model.CapabilityModel;
import com.ericsson.isf.model.CapabilityPageGroupModel;
import com.ericsson.isf.model.CityModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DemandTypeModel;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.DynamicMessageModel;
import com.ericsson.isf.model.EricssonOrganizationModel;
import com.ericsson.isf.model.LocationTypeModel;
import com.ericsson.isf.model.OrganizationModel;
import com.ericsson.isf.model.ProjectScope;
import com.ericsson.isf.model.RequestRoleAccessModel;
import com.ericsson.isf.model.ResetLoginAspModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.RoleModel;
import com.ericsson.isf.model.SessionDataStoreModel;
import com.ericsson.isf.model.UserDetailsAccessModel;
import com.ericsson.isf.model.UserLocationAddressModel;
import com.ericsson.isf.model.UserLoginModel;
import com.ericsson.isf.model.UserPreferencesModel;
import com.ericsson.isf.model.UserProfileHistoryModel;
import com.ericsson.isf.model.WorkFlowFilterModel;
import com.ericsson.isf.model.botstore.RpaApiResponse;
import com.ericsson.isf.model.botstore.TblAccessRole;
import com.ericsson.isf.model.botstore.TblCapabilityId;
import com.ericsson.isf.model.botstore.TblRoleCapabilityId;
import com.ericsson.isf.security.aes.Decrypt;
import com.ericsson.isf.security.aes.Encrypt;
import com.ericsson.isf.service.AccessManagementHibService;
import com.ericsson.isf.service.AccessManagementService;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author esanpup
 */

@RestController
@RequestMapping("/accessManagement")

public class AccessManagementController {
    
    private static final Logger LOG = LoggerFactory.getLogger(AccessManagementController.class);
    
    @Autowired /*Bind to bean/pojo  */
    private AccessManagementService accessManagementService;

    @Autowired
	private ApplicationConfigurations configurations;


    @Autowired
    private AccessManagementHibService accessManagementHibService;
    
    
    @RequestMapping(value = "/getPageAccessDetailsByRoleOfSignum", method = RequestMethod.GET)
    public List<Map<String,Object>> getPageAccessDetailsByRoleOfSignum(@RequestHeader("Signum") String signumID, @RequestHeader("Role") String role) {
        LOG.info("getPageAccessDetailsByRoleOfSignum: Success for signumID: " + signumID);
        return this.accessManagementService.getPageAccessDetailsByRoleOfSignum(signumID, role);
    }
    @RequestMapping(value = "/updateAccessProfileStatus", method = RequestMethod.POST)
    public boolean updateAccessProfileStatus(@RequestBody AccessProfileRequestModel profileRequest) {
        return this.accessManagementService.updateAccessProfileStatus(profileRequest);
    }
    
    @RequestMapping(value = "/aspIsfRegistration", method = RequestMethod.POST)
    public Map<String,Object> aspIsfRegistraion(@RequestBody AspLoginModel aspLogin) throws NoSuchAlgorithmException {
        return this.accessManagementService.aspIsfRegistraion(aspLogin);
        
    }
    
    @RequestMapping(value = "/aspIsfReset", method = RequestMethod.POST)
    public Map<String,Object> aspIsfReset(@RequestBody ResetLoginAspModel resetLoginAsp) throws NoSuchAlgorithmException {
        return this.accessManagementService.aspIsfReset(resetLoginAsp);
    }
    
    @RequestMapping(value = "/aspIsfLogin", method = RequestMethod.POST)
    public Response<Map<String,Object>> aspIsfLogin(@RequestBody ResetLoginAspModel resetLoginAsp) throws NoSuchAlgorithmException {
        return this.accessManagementService.aspIsfLogin(resetLoginAsp);
    }
    
    @RequestMapping(value = "/aspIsfForgetPassword", method = RequestMethod.POST)
    public Map<String,Object> aspIsfForgetPassword(@RequestBody ResetLoginAspModel resetLoginAsp) throws NoSuchAlgorithmException {
        return this.accessManagementService.aspIsfForgetPassword(resetLoginAsp);
    }
    
    @ApiOperation(value = "Find all tasks", notes = "Retrieving the collection of user tasks")
    @RequestMapping(value = "/getUserMenu", method = RequestMethod.GET)
    public List<CapabilityPageGroupModel> getUserMenu() {
    	return this.accessManagementService.getUserMenu();
    }
    
    @RequestMapping(value = "/getUserMenuByRole", method = RequestMethod.GET)
	public ResponseEntity<Response<List<CapabilityPageGroupModel>>> getUserMenuByRole(@RequestHeader("Signum") String signumID,
			@RequestHeader(value = "Role", required = false) String role) {
		return this.accessManagementService.getUserMenuByRole(signumID, role);
	}
    
    
    @RequestMapping(value = "/getEricssonOrgDetails", method = RequestMethod.GET)
    public List<EricssonOrganizationModel> getEricssonOrgDetails() {
    	return this.accessManagementService.getEricssonOrgDetails();
    }
    
    @RequestMapping(value = "/updateEricssonOrg/{data}", method = RequestMethod.GET)
    public void updateEricssonOrg(@PathVariable String data) {
    	this.accessManagementService.updateEricssonOrg(data);
    }


    @RequestMapping(value = "/validateEricssonEmployesDetails", method = RequestMethod.POST, consumes = "multipart/form-data")
    public Map<String, Object>  validateEricssonEmployesDetails(@RequestPart("empFile") MultipartFile empFile, @RequestParam("signum") String signum) throws IOException, SQLException {
    	return this.accessManagementService.validateEricssonEmployesDetails(empFile, signum);
    }
    
    @RequestMapping(value = "/UpdateEmailFromLDAP", method = RequestMethod.GET)
    public void  UpdateEmailFromLDAP() throws NamingException{
    	this.accessManagementService.UpdateEmailFromLDAP();
    }
    
    @RequestMapping(value = "/uploadEricssonEmployesDetails", method = RequestMethod.POST, consumes = "multipart/form-data")
    public Map<String, Object>  uploadEricssonEmployesDetails(@RequestParam("filePath") String filePath, @RequestParam("signum") String signum) throws IOException, SQLException {
    	return this.accessManagementService.uploadEricssonEmployesDetails(filePath,signum);
    }
    
    @RequestMapping(value = "/getSiteStatus", method = RequestMethod.GET)
    public Map<String,Object> getSiteStatus() throws ParseException {
    	Map<String,Object> data = this.accessManagementService.getSiteStatus();
    	String isDown = (data.get("isDown").toString());
    	if ( isDown.trim().equals("1") ){
    		if (data.get(AppConstants.START_DATE) == null && data.get(AppConstants.END_DATE) == null){
    			return   this.accessManagementService.getSiteStatus();
    			}else if (data.get(AppConstants.START_DATE) != null && data.get(AppConstants.END_DATE) != null){
    			Calendar cTime = Calendar.getInstance();
				Calendar downSTime = Calendar.getInstance();
				Calendar downETime = Calendar.getInstance();
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dStartTime = df.parse(data.get(AppConstants.START_DATE).toString());
				Date dEndTime = df.parse(data.get(AppConstants.END_DATE).toString());

				cTime.setTime(cTime.getTime());
				downSTime.setTime(dStartTime);
				downETime.setTime(dEndTime);
				
				if (cTime.after(downSTime) && cTime.before(downETime)){
					return   this.accessManagementService.getSiteStatus();
				}
    		}else{	
    			return   this.accessManagementService.getSiteStatus();
    		}
    	}else {
    		return null;
    	}
    	return null;
    }
    
    /**
     * @page Drop down in Tool Bar
     * 
     * @param requestRoleAccess
     * @return Response<Void>
     */
    @RequestMapping(value = "/requestAccessProfile", method = RequestMethod.POST)
    public Response<Void> requestAccessProfile(@RequestBody RequestRoleAccessModel requestRoleAccess) {
    	
         return this.accessManagementService.requestAccessProfile(requestRoleAccess);
    }
    
    @RequestMapping(value = "/requestAccessProfileDetail", method = RequestMethod.POST)
    public List<Map<String,Object>> requestAccessProfileDetail(@RequestBody RequestRoleAccessModel requestRoleAccess) {
         return this.accessManagementService.requestAccessProfileDetail(requestRoleAccess);
    }
    
    @RequestMapping(value = "/getOrganizationList", method = RequestMethod.GET)
    public List<OrganizationModel> getOrganizationList() {
        LOG.info("REFDATA.TBL_ORGANIZATION: Success");
        return this.accessManagementService.getOrganizationList();
        
    }

        
    
    
    @RequestMapping(value = "/getRoleList", method = RequestMethod.GET)
    public List<RoleModel> getRoleList(@RequestParam(value = "userType",required = false) String userType) {
        LOG.info("REFDATA.TBL_ROLE: Success");
        return this.accessManagementService.getRoleList(userType);
    }
   
    
    @RequestMapping(value = "/getAccessProfiles", method = RequestMethod.GET)
    public List<AccessProfileModel> getAccessProfiles() {
        LOG.info("TRANSACTIONALDATA.TBL_ACCESS_PROFILE: Success");
        return this.accessManagementService.getAccessProfiles();
    }
    
    /**
     * @modifiedBy ekmbuma
     * @page Assign Role
     * 
     * @param roleID
     * @param accessProfileID
     * @param empSignumID
     * @param signum
     * @return String
     */
    @RequestMapping(value = "/assignAccessToUser/{roleID}/{accessProfileID}/{empSignumID}", method = RequestMethod.POST)
    public String assignAccessToUser(@PathVariable int roleID,
                                   @PathVariable int accessProfileID,
                                   @PathVariable String empSignumID,
                                   @RequestHeader String signum) {
        String returnMessage =this.accessManagementService.assignAccessToUser(roleID,accessProfileID,empSignumID,signum);
        LOG.info("roleID : {} accessProfileID : {} signumID : {} TRANSACTIONALDATA.TBL_USER_ACCESS_PROFILE: Success", roleID,accessProfileID,empSignumID);
        return returnMessage;
    }

    @RequestMapping(value = "/disableAccessToUser/{userAccessProfID}/{accessProfileID}/{signumID}/{modifiedBy}", method = RequestMethod.POST)
    public void disableAccessToUser(@PathVariable int userAccessProfID,
                                    @PathVariable int accessProfileID,
                                    @PathVariable String signumID,
                                    @PathVariable String modifiedBy) {
        this.accessManagementService.disableAccessToUser(userAccessProfID,accessProfileID,signumID, modifiedBy);
        LOG.info("userAccessProfileID : {} accessProfileID : {} signumID : {} TRANSACTIONALDATA.TBL_USER_ACCESS_PROFILE: Success", userAccessProfID, accessProfileID,signumID);
    } 
    
    @RequestMapping(value = "/updateApprovalStatus/{userAccessProfID}/{signumID}/{approvalStatus}/{approvedBy}", method = RequestMethod.POST)
    public void updateApprovalStatus(@PathVariable int userAccessProfID,
                                    @PathVariable String signumID,
                                    @PathVariable String approvalStatus,
                                    @PathVariable String approvedBy) {
        this.accessManagementService.updateApprovalStatus(userAccessProfID,signumID,approvalStatus,approvedBy);
        LOG.info("userAccessProfileID : {} accessProfileID : {} signumID : {} TRANSACTIONALDATA.TBL_USER_ACCESS_PROFILE: Success",userAccessProfID, approvalStatus, signumID);
    }
    
    /**
     * @modifiedBy ekmbuma
     * @page View Access Page
     * 
     * @param signumID
     * @param roleID
     * @param organisationID
     * @param request
     * @return Map<String, Object>
     */
    @RequestMapping(value = "/searchAccessDetailsByFilter/{signumID}/{roleID}/{organisationID}/{lineManagerSignum}", method = RequestMethod.POST)
    public Map<String, Object> getAccessDetailsByFilter(@PathVariable String signumID,
                                    @PathVariable int roleID,
                                    @PathVariable int organisationID,
                                    @PathVariable String lineManagerSignum,
                                    HttpServletRequest request) {
    	LOG.info("userAccessProfileID :{} accessProfileID :{} signumID :{} TRANSACTIONALDATA.TBL_USER_ACCESS_PROFILE: Success",signumID,roleID,organisationID);
        return accessManagementService.searchAccessDetailsByFilter(signumID,roleID,organisationID,lineManagerSignum, new DataTableRequest(request));
    }
    
    @RequestMapping(value = "/createNewAccessProfile/{profileName}/{roleID}/{organizationID}", method = RequestMethod.POST)
    public void createNewAccessProfile(@PathVariable String profileName,
                                    @PathVariable int roleID,
                                    @PathVariable int organizationID) {
        this.accessManagementService.createNewAccessProfile(profileName,roleID,organizationID);
        LOG.info("createNewAccessProfile - Entered values:: profileName : " + profileName + "," + " role : " + roleID + "," + " organization : " + organizationID + "," + " refData.TBL_ACCESS_PROFILE: Success");
    }

    @RequestMapping(value = "/getAccessProfilesByRole/{roleID}", method = RequestMethod.GET)
    public List<AccessProfileModel> getAccessProfilesByRole(@PathVariable int roleID) {
        if(roleID == 0){
            throw new ApplicationException(500, "The roleID cannot be 0");
        }else{
        LOG.info("getAccessProfilesByRole: Success for roleID: " + roleID);
        return this.accessManagementService.getAccessProfilesByRole(roleID);
        }
    }
    
    @RequestMapping(value = "/getUserAccessProfile/{signumID}", method = RequestMethod.GET)
    public Response<List<UserDetailsAccessModel>> getUserAccessProfile(@PathVariable String signumID) {
        LOG.info("getUserAccessProfile: Success for signumID: {}", signumID);
        return this.accessManagementService.getUserAccessProfile(signumID);
    }
    
    @RequestMapping(value = "/getUserProfileByFilter/{signumID}/{roleID}/{organisationID}", method = RequestMethod.GET)
    public List<UserDetailsAccessModel> getUserProfileByFilter(@PathVariable String signumID,
                                                                     @PathVariable int roleID,
                                                                     @PathVariable int organisationID) {
        return this.accessManagementService.getUserProfileByFilter(signumID,roleID,organisationID);
    }
    
    @RequestMapping(value = "/getUserProfileByEmail", method = RequestMethod.POST)
    public List<UserDetailsAccessModel> getUserProfileByEmail(@RequestBody UserDetailsAccessModel userDetailsAccessModel) {
        return this.accessManagementService.getUserProfileBySignum(userDetailsAccessModel);
    }
    
    @RequestMapping(value = "/getUserSignumByEmail", method = RequestMethod.POST)
    public ResponseEntity<Response<List<UserDetailsAccessModel>>> getUserSignumByEmail(@RequestBody UserDetailsAccessModel userDetailsAccessModel, @RequestHeader("X-Auth-Token") String userToken) throws ServletException {
        return this.accessManagementService.getUserSignumByEmail(userDetailsAccessModel, userToken);
    }
    @RequestMapping(value = "/getUserSignumByEmailHRMS", method = RequestMethod.POST)
    public List<UserDetailsAccessModel> getUserSignumByEmailHRMS(@RequestBody UserDetailsAccessModel userDetailsAccessModel) {
        return this.accessManagementService.getUserSignumByEmailHRMS(userDetailsAccessModel);
    }
    @RequestMapping(value = "/searchAccessDetails/{accessProfileID}/{roleID}/{organisationID}", method = RequestMethod.GET)
    public List<AccessProfileUserDetailModel> searchAccessDetails(@PathVariable int accessProfileID,
                                                                     @PathVariable int roleID,
                                                                     @PathVariable int organisationID) {
        return this.accessManagementService.searchAccessDetails(accessProfileID,roleID,organisationID);
    }
    
    @RequestMapping(value = "/getCPMdetails/{signumID}", method = RequestMethod.GET)
    public List<CPMModel> getCPMdetails(@PathVariable String signumID) throws NamingException {
    	 LOG.info("getCPMdetails: Success for signumID: {} ", signumID);
        return this.accessManagementService.getCPMdetails(signumID);
    }
    
    @RequestMapping(value = "/getUserByFilter/{roleID}/{orgID}", method = RequestMethod.GET)
    public List<String> getUserByFilter(@PathVariable int roleID,
                                        @PathVariable int orgID) {
    	 LOG.info("getUserByFilter: Success for roleID, orgID: " + roleID + ", " + orgID);
        return this.accessManagementService.getUserByFilter(roleID, orgID);
    }
    
    @RequestMapping(value = "/getUsersByAlias/{alias}/{orgID}", method = RequestMethod.GET)
    public List<String> getUsersByAlias(@PathVariable String alias,
                                        @PathVariable int orgID) {
        return this.accessManagementService.getUsersByAlias(alias, orgID);
    }
    
    @RequestMapping(value = "/getUserDetailsForDR/{roleID}", method = RequestMethod.GET)
    public List<String> getUserDetailsForDR(@PathVariable String roleID,@RequestHeader("MarketArea") String marketArea) {
    	 LOG.info("getUserDetailsForDR: Success for roleID: " + roleID);
        return this.accessManagementService.getUserDetailsForDR(roleID,marketArea,"");
    }
    @RequestMapping(value = "/getUserDetailsForDR/{roleID}/{projectID}", method = RequestMethod.GET)
    public List<String> getUserDetailsForDRByPID(@PathVariable String roleID,
    		@PathVariable String projectID,@RequestHeader("MarketArea") String marketArea) {
    	 LOG.info("getUserDetailsForDR: Success for roleID: " + roleID+" and projectID: "+projectID);
        return this.accessManagementService.getUserDetailsForDR(roleID,marketArea,projectID);
    }
    @RequestMapping(value = "/getRPMByMarket", method = RequestMethod.GET)
    public List<String> getRPMByMarket(@RequestHeader("MarketArea") String marketArea) {
    	 LOG.info("getRPMByMarket: Success for market area: " + marketArea);
        return this.accessManagementService.getRPMByMarket(marketArea);
    }
    
    @RequestMapping(value = "/deleteDeliveryResponsible/{deliveryResponsibleID}/{modifiedBy}", method = RequestMethod.POST)
    public void deleteDeliveryResponsible(@PathVariable int deliveryResponsibleID, @PathVariable String modifiedBy){
        this.accessManagementService.deleteDeliveryResponsible(deliveryResponsibleID,modifiedBy);
        LOG.info(AppConstants.DELETE_ACCESS_PROFILE_SUCCESS);
    }
    
      @RequestMapping(value = "/getCapability", method = RequestMethod.GET)
    public List<CapabilityModel> getCapability() {
        LOG.info("REFDATA.TBL_ROLE: Success");
        return this.accessManagementService.getCapability();
        
    }
      @RequestMapping(value = "/getAccessEmployee", method = RequestMethod.GET)
      public List<HashMap<String, Object>> getAccessEmployee() {
          LOG.info("Success");
          return this.accessManagementService.getAccessEmployee();
          
      }
    
    @RequestMapping(value = "/editAccessProfile", method = RequestMethod.POST)
    public void editAccessProfile(@RequestBody AccessProfileModel accessProfileModel){
        this.accessManagementService.editAccessProfile(accessProfileModel);
        LOG.info("Edit Access Profile: Success");
    }
    
    @RequestMapping(value = "/deleteAccessProfile/{accessProfileID}", method = RequestMethod.POST)
    public void deleteAccessProfile(@PathVariable int accessProfileID){
        this.accessManagementService.deleteAccessProfile(accessProfileID);
        LOG.info(AppConstants.DELETE_ACCESS_PROFILE_SUCCESS);
    }
    
    
    @RequestMapping(value = "/authorize", method = RequestMethod.POST)
    public boolean authorize(@RequestBody AuthRequestModel authRequest) throws NamingException{
        
    	return this.accessManagementService.authenticate(authRequest.getUsername(), authRequest.getPassword());
    }
    
    @RequestMapping(value = "/saveLoginDetails", method = RequestMethod.POST)
    public ResponseEntity<Response<Integer>> saveLoginDetails(@RequestBody UserLoginModel userLogin) throws IOException{
        return this.accessManagementService.saveLoginDetails(userLogin);
    }
    
    @RequestMapping(value = "/saveLogoutDetails", method = RequestMethod.POST)
    public ResponseEntity<Response<Integer>> saveLogoutDetails(@RequestBody UserLoginModel userLogin) throws IOException{
        return this.accessManagementService.saveLoginDetails(userLogin);
    }
    
    @RequestMapping(value = "/getLoginHistory/{signumID}", method = RequestMethod.GET)
    public List<UserLoginModel> getLoginHistory(@PathVariable ("signumID") String signumID){
        return this.accessManagementService.getLoginHistory(signumID);
    }
    
    @RequestMapping(value = "/updateLoginHistory/{signumID}/{logID}/{logoutTime}", method = RequestMethod.POST)
    public void updateLoginHistory(@PathVariable ("signumID") String signumID,
                                 @PathVariable ("logID") int logID,
                                 @PathVariable ("logoutTime") String logoutTime) throws ParseException{
        this.accessManagementService.updateLoginHistory(signumID,logID,logoutTime);
    }
     
    @RequestMapping(value = "/checkEmpAsPM/{signumID}/{projectID}", method = RequestMethod.GET)
    public List<Map<String,String>> checkEmpAsPM(@PathVariable ("signumID") String signumID, @PathVariable ("projectID") int projectID) throws ParseException{
         return this.accessManagementService.checkEmpAsPM(signumID,projectID);
    }
    
    //Getting all access roles:
    @RequestMapping(value = "/getAllAccessRoles", method = RequestMethod.GET)
    public List<TblAccessRole> getAllAccessRoles(){
        return this.accessManagementHibService.getAllAccessRoles();
    }

    @RequestMapping(value = "/addAccessRole/{role}/{alias}", method = RequestMethod.POST)
    public RpaApiResponse addAccessRole(@PathVariable String role, @PathVariable String alias) {
         return this.accessManagementHibService.addAccessRole(role, alias);
    }
    @RequestMapping(value = "/updateAccessRole", method = RequestMethod.POST)
    public void updateAccessRole(@RequestBody RoleModel roleModel ) throws ParseException{
        this.accessManagementService.updateAccessRole(roleModel);
    }
   
    
    @RequestMapping(value = "/deleteAccessRole/{accessRoleID}", method = RequestMethod.POST)
    public void deleteAccessRole(@PathVariable int accessRoleID){
        this.accessManagementService.deleteAccessRole(accessRoleID);
        LOG.info("Delete Access Role: Success");
    }

    
    @RequestMapping(value = "/getProjectScope/{signumID}", method = RequestMethod.GET)
	public List<ProjectScope> getProjectScope(@PathVariable ("signumID") String signumID){
        return this.accessManagementHibService.getProjectScopes(signumID);
    } 
    
    

    //Getting all access roles:
    @RequestMapping(value = "/getAllCapabilities", method = RequestMethod.GET)
    public List<TblCapabilityId> getAllCapabilities(){
        return this.accessManagementHibService.getAllCapabilities();
    }
    @RequestMapping(value = "/getCapabilities", method = RequestMethod.GET)
    public List<TblCapabilityId> getCapabilities(){
        return this.accessManagementHibService.getCapabilities();
    }
    
    @RequestMapping(value = "/addCapability", method = RequestMethod.POST)
    public RpaApiResponse addCapability(@RequestBody TblCapabilityId cap){
    	return this.accessManagementHibService.addCapability(cap);
    }

    
    @RequestMapping(value = "/deleteCapability/{pageId}", method = RequestMethod.POST)
    public RpaApiResponse deleteCapability(@PathVariable Integer pageId){
    	return this.accessManagementHibService.deleteCapability(pageId);
    }
    
    //Getting TBL_ROLE_CAPABILITY:
    @RequestMapping(value = "/getAllRoleCapabilities", method = RequestMethod.GET)
    public List<TblRoleCapabilityId> getAllRoleCapabilities(){
        return this.accessManagementHibService.getAllRoleCapabilities();
    }

    @RequestMapping(value = "/addRoleCapability", method = RequestMethod.POST)
    public RpaApiResponse addRoleCapability(@RequestBody TblRoleCapabilityId cap){
    	return this.accessManagementHibService.addRoleCapability(cap);
    }
    
    
    /**
     * @modifiedBy elkpain
     * @page View Access
     * 
     * @param signum
     * @param accessRequestApprovalModel
     * @return Response<Void>
     */
    @RequestMapping(value = "deleteAccessProfileBySignum", method = RequestMethod.POST)
    public Response<List<AccessRequestApprovalModel>> deleteAccessProfileBySignum(@RequestHeader("signum") String signum,@RequestBody List<AccessRequestApprovalModel> accessRequestApprovalModel,
    		@RequestHeader("role") String role){
    	
        LOG.info(AppConstants.DELETE_ACCESS_PROFILE_SUCCESS);
        return this.accessManagementService.deleteAccessProfileBySignum(signum,accessRequestApprovalModel,role);
    }
    
    
    @RequestMapping(value = "/getDownStatus", method = RequestMethod.GET)
    public Map<String,Object>getDownStatus()  {
         Map<String, Object> data = this.accessManagementService.getDownStatus();
    	String isDown = (data.get("isDown").toString());
    		if (isDown.trim().equals("1") && ( data.get(AppConstants.START_DATE) == null && data.get(AppConstants.END_DATE) == null) ){
    			return data;
    		}
    		
		return data;
		
    }
   
    /**
     * 
     * @param Signum
     * @param dynamicMessageModel
     * @return
     */
    @RequestMapping(value = "/insertDownStatus", method = RequestMethod.POST)
    public  Response<Void> insertDownStatus(@RequestHeader(value="Signum") String signum,
    		@RequestBody DynamicMessageModel dynamicMessageModel) {
    	return this.accessManagementService.insertDownStatus(signum,dynamicMessageModel);
    }
    @RequestMapping(value = "/getMarketAreaBySignum/{signumID}", method = RequestMethod.GET)
    public List<String> getMarketAreaBySignum(@PathVariable String signumID) {
    	 LOG.info("getMarketArea: Success for signumID: " + signumID);
        return this.accessManagementService.getMarketAreaBySignum(signumID);
    }

    @RequestMapping(value = "/addUserPreferences", method = RequestMethod.POST)
    public Map<String,String> addUserPreferences(@RequestBody UserPreferencesModel preferences){
        return this.accessManagementService.addUserPreferences(preferences);
    }

    @RequestMapping(value = "/getUserPreferences/{signum}", method = RequestMethod.GET)
    public UserPreferencesModel getUserPreferences(@PathVariable String signum) {
    	long startTime=System.currentTimeMillis();
    	LOG.info("START TIME: "+startTime+" ms");
    	
    	UserPreferencesModel  getUserPreferences = this.accessManagementService.getUserPreferences(signum);
    	long endTime=System.currentTimeMillis();

    	LOG.info("END TIME: "+endTime+" ms");
    	LOG.info("TOTAL TIME ELAPSED IN v1/@getUserPreferences API : "+(endTime-startTime)+" ms");
    	return getUserPreferences;
    }
    
    
    /**
     * @modifiedBy elkpain
     * @page Profile Access [Approve/Reject]
     * 
     * @param role
     * @param signum
     * @return Map<String, Object>
     */
    @RequestMapping(value = "getAccessRequestsBySignum",method = RequestMethod.POST)
    public Map<String, Object> getAccessRequestsBySignum(@RequestParam("signum") String signum,@RequestHeader("role") String role, HttpServletRequest request){
    	
		 DataTableRequest dataTableReq = new DataTableRequest(request);

    	return this.accessManagementService.getAccessRequestsBySignum(signum,role, dataTableReq);
    }
    
    /**
     * @modifiedBy ekmbuma
     * @page Profile Access [Approve/Reject]
     * 
     * @param accessRequestApprovalModel
     * @param role
     * @return Response<Void>
     */
    @RequestMapping(value = "updateAccessRequestStatus",method = RequestMethod.POST)
    public Response<Void> updateAccessRequestStatus(@RequestBody List<AccessRequestApprovalModel> accessRequestApprovalModelList,@RequestHeader("role") String role){
    	
    	return accessManagementService.updateAccessRequestStatus(accessRequestApprovalModelList,role);
    }
    
    /**
     * @page Profile Access [Renew/Revoke]
     * 
     * @param signum
     * @return List<AccessRequestApprovalModel>
     */
    @RequestMapping(value = "getRenewRequestsBySignum",method = RequestMethod.GET)
    public List<AccessRequestApprovalModel> getRenewRequestsBySignum(@RequestParam("signum") String signum,@RequestHeader("role") String role){
    	
    	return this.accessManagementService.getRenewRequestsBySignum(signum,role);
    }
    
    /**
     * @page Profile Access [Renew/Revoke]
     * 
     * @param accessRequestApprovalModel
     * @return Response<Void>
     */
    @RequestMapping(value = "updateAccessStatus",method = RequestMethod.POST)
    public Response<Void> renewRequests(@RequestBody List<AccessRequestApprovalModel> accessRequestApprovalModel){
    	
    	return this.accessManagementService.updateAccessStatus(accessRequestApprovalModel);
    }
    
    @RequestMapping(value = "checkIfAspRegistered",method = RequestMethod.GET)
    public String checkIfAspRegistered(@RequestParam("email") String email){
    	return this.accessManagementService.checkIfAspRegistered(email.trim());
    }
    
    @RequestMapping(value = "getPageAccessDetailsByAccessProfileId", method = RequestMethod.GET)
    public List<Map<String,Object>> getPageAccessDetailsByAccessProfileId(@RequestParam("accessProfileId") Integer accessProfileId) {
        LOG.info("getPageAccessDetailsByAccessProfileId: Success for AccessProfileId: " + accessProfileId);
        return this.accessManagementService.getPageAccessDetailsByAccessProfileId(accessProfileId);
    }
    @RequestMapping(value = "/getLoginData", method = RequestMethod.POST)
    public HashMap<String, Object> getLoginData(@RequestBody UserDetailsAccessModel userDetailsAccessModel, @RequestHeader("X-Auth-Token") String userToken) throws ServletException {
        return this.accessManagementService.getLoginData(userDetailsAccessModel, userToken);
    }
    @RequestMapping(value = "/UpdateEmailFromLDAPHRMS", method = RequestMethod.GET)
    public void  UpdateEmailFromLDAPHRMS(){
    	this.accessManagementService.UpdateEmailFromLDAPHRMS();
    }
    
    @RequestMapping(value = "/getSubscribedApiList", method = RequestMethod.GET)
    public List<Map<String,Object>> getSubscribedApiList(@RequestParam(value="externalSourceName") String externalSourceName) {
    	return this.accessManagementService.getSubscribedApiList(externalSourceName);
    }

    @RequestMapping(value = "/getUserTokenList", method = RequestMethod.POST)
    public List<Map<String,Object>> getUserTokenList(@RequestHeader(value="signum") String signum) {
    	return accessManagementService.getUserTokenList(signum);
    }
    
    @RequestMapping(value = "disableActiveToken", method = RequestMethod.POST)
    public boolean disableActiveToken(@RequestParam(value="externalSourceName") String externalSourceName, @RequestHeader(value="signum") String signum) {
    	return this.accessManagementService.disableActiveToken(externalSourceName, signum);
    }
    
    @RequestMapping(value = "testUpdateWOModifiedDate", method = RequestMethod.POST)
    public boolean testUpdateWOModifiedDate(@RequestParam(value="signum") String signum) {
    	return this.accessManagementService.testUpdateWOModifiedDate(signum);
    }
    
    @RequestMapping(value = "/saveUserProfileHistory", method = RequestMethod.POST)
    public  Response<Void>  saveUserProfileHistory(@RequestBody UserProfileHistoryModel userProfileHistoryModel) {
    	return this.accessManagementService.saveUserProfileHistory(userProfileHistoryModel);
    }
    
    
    @RequestMapping(value= "/getCityByCountryID", method= RequestMethod.POST)
    public List<CityModel> getCityByCountryID(@RequestBody CityModel cityModel) {
		return this.accessManagementService.getCityByCountryID(cityModel);
	}
    
    /**
     * 
     * @param Signum
     * @param userLocationAddressModel
     * @return  Response<Void>
     */
    @RequestMapping(value = "/saveUserLocation", method = RequestMethod.POST)
    public  Response<Void> saveUserLocation(@RequestHeader(value="Signum") String Signum, @RequestBody  UserLocationAddressModel userLocationAddressModel) {
    	return this.accessManagementService.saveUserLocation(Signum,userLocationAddressModel);
    }  
    
    @RequestMapping(value = "/getUserLocationBySignum", method = RequestMethod.GET)
    public  CityModel getUserLocationBySignum(@RequestHeader(value="Signum") String Signum) {
    	return this.accessManagementService.getUserLocationBySignum(Signum);
    }  
    
    @RequestMapping(value = "/getUserAccessProfileBySignum", method = RequestMethod.GET)
    public List<AccessProfileModel> getUserAccessProfileBySignum(@RequestHeader("Signum") String signum) {
        return this.accessManagementService.getUserAccessProfileBySignum(signum);
    }
    
    @RequestMapping(value = "/getAccessProfileOfUser/{signumID}", method = RequestMethod.GET)
    public Response<List<UserDetailsAccessModel>> getAccessProfileOfUser(@PathVariable String signumID) {
        LOG.info("getUserAccessProfile: Success for signumID: {}",signumID);
        return this.accessManagementService.getAccessProfileOfUser(signumID);
    }
    
   /**
   *  @author emntiuk
   * purpose: This API is used for get location Type.
   * @return List<LocationTypeModel>
   */
    
    @RequestMapping(value = "/getLocationType", method = RequestMethod.GET)
    public List<LocationTypeModel>  getLocationType() {
    	return this.accessManagementService.getLocationType();
    }
    
    /**
     * @author emntiuk
     * purpose: This API is used for to get All location Types in Master page.
     * @return List<LocationTypeModel>
     */
    @RequestMapping(value = "/getAllLocationTypes", method = RequestMethod.GET)
    public List<LocationTypeModel>  getAllLocationTypes() {
    	return this.accessManagementService.getAllLocationTypes();
    }
    
    /**
     * @author epnwsia
     * @param Signum
     * @param locationTypeModel
     * @return
     */
    @RequestMapping(value = "/addLocationType", method = RequestMethod.POST)
    public  Response<Void> addLocationType(@RequestHeader(value="Signum") String signum, @RequestBody LocationTypeModel locationTypeModel) {
    	return this.accessManagementService.addLocationType(signum,locationTypeModel);
    } 
    
  
    /**
     * @author epnwsia
     * @param locationTypeModel
     * @return Response<Void>
     */
    @RequestMapping(value = "/changeLocationTypeStatus", method = RequestMethod.POST)
    public Response<Void> changeLocationTypeStatus(@RequestHeader(value="signum") String signum
    		,@RequestBody LocationTypeModel locationTypeModel) {
    	return this.accessManagementService.changeLocationTypeStatus(signum,locationTypeModel);
    } 
    
    /**
     * @author emntiuk
     * @param signum
     * @param locationTypeModel
     * @return Response<Void>
     */
    @RequestMapping(value = "/updateDefaultLocationType", method = RequestMethod.POST)
    public Response<Void> updateDefaultLocationType(@RequestHeader(value="signum") String signum,
    		@RequestBody LocationTypeModel locationTypeModel) {
    	return this.accessManagementService.updateDefaultLocationType(signum, locationTypeModel);
    } 
    /**
     * @author epnwsia
     * @param signum
     * @return
     */
    @RequestMapping(value = "/getActiveSessionForUser", method = RequestMethod.GET)
    public Response<Boolean> getActiveSessionForUser(@RequestHeader(value="signum") String signum) {
    	return this.accessManagementService.getActiveSessionForUser(signum);
    } 
  
	@RequestMapping(value="/testSignalR", method = RequestMethod.GET)
	public String testSignalR() {
		
		String response = StringUtils.EMPTY;
		RestTemplate restTemplate = new RestTemplate();
		String url = configurations.getStringProperty(ConfigurationFilesConstants.SIGNALR_APPLICATION_URL)
				+ AppConstants.TEST_SIGNALR_API;
		
		LOG.info("url called============: {}",url);		
		
		try {
			response = restTemplate.getForObject(url, String.class);
			
			LOG.info("Test signalR : {}",response);

		} catch (RestClientException ex) {
			LOG.debug(String.format("Exception thrown  %s", ex.getMessage()));
			response = ex.getMessage();
		}
		return response;
	}


    /**
     * @author ekmbuma
     * @page View Access
     * 
     * @param signumID
     * @param roleID
     * @param organisationID
     * @param lineManagerSignum
     * @param request
     * @return DownloadTemplateModel
     * @throws ParseException 
     */
    @RequestMapping(value = "/downloadViewAccessPageDataInExcel/{signumID}/{roleID}/{organisationID}/{lineManagerSignum}", method = RequestMethod.GET)
    public DownloadTemplateModel downloadViewAccessPageDataInExcel(@PathVariable String signumID,
                                    @PathVariable int roleID,
                                    @PathVariable int organisationID,
                                    @PathVariable String lineManagerSignum) throws ParseException {

        return accessManagementService.downloadViewAccessPageDataInExcel(signumID,roleID,organisationID,lineManagerSignum);
    }
    
    
    
    /**
     * @author elkpain
     * 
     * @param signum
     * @param role
     * @return DownloadTemplateModel
     * @throws ParseException 
     */

    @RequestMapping(value = "downloadProfileAccessPageDataInExcel", method = RequestMethod.GET)
    public DownloadTemplateModel downloadProfileAccessPageDataInExcel(@RequestParam("signum") String signum,@RequestParam("role") String role)
    		throws ParseException {

        return accessManagementService.downloadProfileAccessPageDataInExcel(signum,role);
    }
    
    /**
     * @author ekarmuj
     * Api name: /getMessageTable
     * Purpose: Used to get All dynamic message from table(active inactive both.)
     * @return Response<List<DynamicMessageModel>>
     */
    
    @RequestMapping(value = "/getMessageTable", method = RequestMethod.GET)
    public Response<List<DynamicMessageModel>> getMessageTable() {
    	return accessManagementService.getMessageTable();
    } 
    
      /**
        * @author elkpain
        * @param signum
        * @param dynamicMessageModel
        * @return Response<Void>
        * This API is used to enable/disable message on login message page
        */
       @RequestMapping(value = "/enableMessage", method = RequestMethod.POST)
       public Response<Void> enableMessage(@RequestHeader(value="signum") String signum,
       		@RequestBody DynamicMessageModel dynamicMessageModel) {
       	LOG.info("Enable/Disable Message: Success");
       	return this.accessManagementService.enableMessage(signum, dynamicMessageModel);
       }
       
       /**
        *@author emntiuk 
        * @return Response<List<DynamicMessageModel>>
        * This API used to get Enabled Message.
        */
       @RequestMapping(value = "/getEnabledMessage", method = RequestMethod.GET)
       public Response<List<DynamicMessageModel>> getEnabledMessage() {
       	return accessManagementService.getEnabledMessage();
       }
       
	/**
	 * @author ekarmuj
	 * Api Name:accessManagement/generateCustomAPIToken
	 * Purpose: Used to save user information in redis.
	 * @param sessionDataModel
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
       
    @Encrypt
	@RequestMapping(value = "/generateCustomAPIToken", method = RequestMethod.POST)
	public ResponseEntity<Response<String>> generateCustomAPIToken(@Decrypt @RequestBody SessionDataStoreModel sessionDataModel,
			HttpServletRequest request)  {
		return accessManagementService.generateCustomAPIToken(request, sessionDataModel);
}

	/**
	 * @author ekarmuj
	 * Api Name:accessManagement/getSessionId
	 * Purpose: Used to get user information from redis.
	 * @param sessionId
	 * @return user Information
	 */
	
	@RequestMapping(value = "/getSessionId", method = RequestMethod.GET)
	public ResponseEntity<Response<Object>> getSessionId(@RequestHeader("sessionId") String sessionId) {
		return accessManagementService.getSessionId(sessionId);
	}
	
	/**
	 * @author ekarmuj
	 * Api Name:accessManagement/destroySession
	 * Purpose: Used to delete user information from redis and save login details in history table.
	 * @param sessionId
	 * @return ResponseEntity<Response<String>>
	 */
	
	@RequestMapping(value ="/destroySession", method = RequestMethod.POST)
	public ResponseEntity<Response<String>> destroySession(@RequestBody SessionDataStoreModel sessionDataModel) {
		return accessManagementService.destroySession(sessionDataModel);
			
	}
	
	@RequestMapping(value = "/getUserDetails", method = RequestMethod.GET)
	public Response<UserDetailsAccessModel> getUserDetails(@RequestHeader("Signum") String signum ){
		long startTime=System.currentTimeMillis();
		LOG.info("START TIME: "+startTime);
		Response<UserDetailsAccessModel>response =accessManagementService.getUserDetails(signum);
		
		long endTime=System.currentTimeMillis();
		LOG.info("END TIME: "+endTime);
		LOG.info("TOTAL TIME ELAPSED IN @getUserDetails API : "+(endTime-startTime)+" ms");
		return response;
	}
	/**
	 * Api Name:accessManagement/insertDemandType
	 * Purpose: Used to insert Demand Type , Demand Type Description
	 * @param DemandTypeModel
	 * @return ResponseEntity<Response<Void>>
	 */
	@PostMapping(value="/insertDemandType")
	public ResponseEntity<Response<Void>> insertDemandType(@RequestBody DemandTypeModel demandTypeModel)
	{
		ResponseEntity<Response<Void>> response=accessManagementService.insertDemandType(demandTypeModel);
		LOG.info("Demand Type Added : SUCCESS");
		return response;
   }
	/**
	 * Api Name:accessManagement/updateDemandType
	 * Purpose: Used to update demand type details or we can inactive demand type
	 * @param DemandTypeModel
	 * @return ResponseEntity<Response<Void>>
	 */
	@PostMapping(value="/updateDemandType")
	public ResponseEntity<Response<Void>> updateDemandType(@RequestBody DemandTypeModel demandTypeModel)
	{
		ResponseEntity<Response<Void>> response=accessManagementService.updateDemandType(demandTypeModel);
		LOG.info("Demand Type Updated : SUCCESS");
		return response;
   }
	
	/**
	 * API Name: accessManagement/getDemandType
	 * Purpose: to fetch all the DemandTypes
	 * @author eioshgy
	 * @return Response<List<DemandTypeModel>>
	 * 
	 */
	@GetMapping(value = "/getDemandType")
	public Response<List<DemandTypeModel>> getDemandType(){
		
		Response<List<DemandTypeModel>>response =accessManagementService.getDemandType();
		
		return response;
	}
	
	
}
