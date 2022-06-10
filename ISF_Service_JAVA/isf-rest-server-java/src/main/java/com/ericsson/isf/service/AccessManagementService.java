/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ericsson.isf.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ericsson.isf.apiManager.ApiManagerService;
import com.ericsson.isf.apiManager.ApiManagerServiceBuilder;
import com.ericsson.isf.config.ApplicationConfigurations;
import com.ericsson.isf.dao.AccessManagementDAO;
import com.ericsson.isf.dao.ActivityMasterDAO;
import com.ericsson.isf.dao.AspManagementDao;
import com.ericsson.isf.exception.ApplicationException;
import com.ericsson.isf.jwtSecurity.JwtValidator;
import com.ericsson.isf.model.AccessProfileModel;
import com.ericsson.isf.model.AccessProfileRequestModel;
import com.ericsson.isf.model.AccessProfileUserDetailModel;
import com.ericsson.isf.model.AccessRequestApprovalModel;
import com.ericsson.isf.model.ApiManagerRequestModel;
import com.ericsson.isf.model.ApiManagerResponseModel;
import com.ericsson.isf.model.ApiManagerUserModel;
import com.ericsson.isf.model.AspExplorerModel;
import com.ericsson.isf.model.AspLoginModel;
import com.ericsson.isf.model.AspVendorModel;
import com.ericsson.isf.model.CPMModel;
import com.ericsson.isf.model.CapabilityModel;
import com.ericsson.isf.model.CapabilityPageGroupModel;
import com.ericsson.isf.model.CityModel;
import com.ericsson.isf.model.DataTableRequest;
import com.ericsson.isf.model.DemandTypeModel;
import com.ericsson.isf.model.DownloadTemplateModel;
import com.ericsson.isf.model.DynamicMessageModel;
import com.ericsson.isf.model.EmployeeModel;
import com.ericsson.isf.model.EricssonOrganizationModel;
import com.ericsson.isf.model.ExternalSourceModel;
import com.ericsson.isf.model.GroupMenuModel;
import com.ericsson.isf.model.JwtApiUser;
import com.ericsson.isf.model.JwtUser;
import com.ericsson.isf.model.LocationTypeModel;
import com.ericsson.isf.model.OrganizationModel;
import com.ericsson.isf.model.RequestRoleAccessModel;
import com.ericsson.isf.model.ResetLoginAspModel;
import com.ericsson.isf.model.Response;
import com.ericsson.isf.model.RoleModel;
import com.ericsson.isf.model.SessionDataStoreModel;
import com.ericsson.isf.model.TokenApiMappingModel;
import com.ericsson.isf.model.TokenMappingModel;
import com.ericsson.isf.model.UserAccessProfileModel;
import com.ericsson.isf.model.UserDetailsAccessModel;
import com.ericsson.isf.model.UserImageURIModel;
import com.ericsson.isf.model.UserLocationAddressModel;
import com.ericsson.isf.model.UserLoginModel;
import com.ericsson.isf.model.UserPreferencesModel;
import com.ericsson.isf.model.UserProfileHistoryModel;
import com.ericsson.isf.model.WorkFlowFilterModel;
import com.ericsson.isf.util.AppConstants;
import com.ericsson.isf.util.AppUtil;
import com.ericsson.isf.util.ConfigurationFilesConstants;
import com.ericsson.isf.util.DateTimeUtil;
import com.ericsson.isf.util.IsfCustomIdInsert;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author esanpup
 */
@Service
public class AccessManagementService {

	private static final String PLEASE_PROVIDE_CREATED_BY = "Please provide Created By";

	private static final String PLEASE_PROVIDE_VALID_CREATED_BY = "Please provide valid Created By";

	private static final String PLEASE_PROVIDE_CREATED_BY_AND_DEMAND_TYPE_DESCRIPTION = "Please provide Created By and Demand Type Description";

	private static final String PLEASE_PROVIDE_DEMAND_TYPE_AND_DEMAND_TYPE_DESCRIPTION = "Please provide Demand Type and Demand Type Description";

	private static final String PLEASE_PROVIDE_DEMAND_TYPE_AND_CREATED_BY = "Please provide Demand Type and Created By";

	private static final String DEMAND_TYPE_DESCRIPTION_CAN_HAVE_ONLY_THESE_$_SPECIAL_CHARACTERS = "Demand Type Description can have only these !$&'()*,.-:<=>@[]_`{}~ special characters";

	private static final String PLEASE_PROVIDE_DEMAND_TYPE_AND_DEMAND_TYPE_DESCRIPTION_PARAMETER_FOR_UPDATE = "Please provide Demand Type and Demand Type Description parameter for update";

	private static final String DEMAND_TYPE_ALREADY_EXISTS = "Demand Type already exists";

	private static final String DEMAND_TYPE_DESCRIPTION_CAN_T_HAVE_MORE_THAN_250_CHARACTERS = "Demand Type Description can't have more than 250 characters";

	private static final String DEMAND_TYPE_CAN_T_HAVE_ANY_SPECIAL_CHARACTERS = "Demand Type can't have any special characters";

	private static final String DEMAND_TYPE_CAN_HAVE_MAXIMUM_100_CHARACTERS = "Demand Type can have maximum 100 characters";

	private static final String PLEASE_PROVIDE_DEMAND_TYPE = "Please provide Demand Type";

	private static final String PLEASE_PROVIDE_DEMAND_TYPE_DESCRIPTION = "Please provide Demand Type Description";

	private static final String PLEASE_PROVIDE_VALID_DEMAND_TYPE_ID = "Please provide valid Demand Type Id";

	private static final String PLEASE_PROVIDE_DEMAND_TYPE_ID = "Please provide Demand Type Id";

	private static final String PLEASE_PROVIDE_LAST_MODIFIEDBY = "Please provide lastModifiedby";

	private static final String PLEASE_PROVIDE_VALID_LAST_MODIFIEDBY = "Please provide valid lastModifiedby";

	private static final String DEMAND_TYPE_UPDATED = "DemandType Updated";

	private static final String DEMAND_TYPE_INACTIVE = "DemandType inactive";

	private static final String SESSION_TOKEN = "sessionToken";

	private static final String EMAIL_DOES_NOT_EXIST = "Email does not exist!";

	private static final String DELIMITER = "|";

	private static final String DASH = "-";

	private static final String SESSION_ID = "sessionId";

	private static final String SESSION_ID_DELETED_SUCCESSFULLY = "sessionId deleted successfully!!!!";

	private static final String PLEASE_PROVIDE_VALUE = "Please provide valid %s!";
	
	@Autowired
	private ApplicationConfigurations configurations;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private IsfCustomIdInsert isfCustomIdInsert;
	
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	public static final String NOT_REGISTERED = "NEW";
	public static final String NOT_APPROVED = "REGISTERED";
	public static final String USER_TYPE = "ASP";
	public static final String ASP_LOGIN_ERROR_REGISTER = "You are not Registered with ISF. Kindly Register First !!";
	public static final String ASP_LOGIN_ERROR_LOCKED = "Your account is locked. Please use Forgot Password to unlock.";
	public static final String ASP_LOGIN_ERROR_VENDOR_INCORRECT = "Vendor Code is incorrect.";
	public static final String ASP_LOGIN_ERROR_PASSWORD_INCORRECT = "Please Enter a Valid Password !!";
	public static final String ASP_LOGIN_ERROR_BEFORE_ACCESS_START = "You will be able to login after ";
	public static final String ASP_LOGIN_ERROR_AFTER_ACCESS_FINISH = "Your account is EXPIRED. Please! contact your Manager.";
	public static final String ASP_LOGIN_SUCCESS = "Login Successfull";
	public static final String ASP_LOGIN_PROFILE_INACTIVE = "Not yet Approved by the Manager.";
	public static final String ASP_LOGIN_ERROR_3_ATTEMPTS = "Consecutive 3 unsuccessfull password attempts. Your account is now locked. Please use Forgot Password to unlock.";
	public static final String SUCCESS_APIM_SIGNUP ="Sucess API Manager Signup: %s";
	public static final String FAILURE_APIM_SIGNUP ="Failure API Manager Signup: %s";
	public static final String SUCCESS_APIM_REVOKE ="Sucess API Manager Revoke: %s";
	public static final String FAILURE_APIM_REVOKE ="Failure API Manager Revoke: %s";
	public static final String APIM_CONNECTION_PROBLEM ="Couldn't connect to Api Manager: %s";

	public static final Integer ASP_MAX_PASSWORD_TRIES = 3;
	public static final int NE_ACCESS_PROFILE_ID = 39;
	public static final int ASP_ACCESS_PROFILE_ID = 55;
	
	public static final String ID_CANNOT_BE_ZERO = "Id cannot be zero";
	public static final String MESSAGE_ENABLED = "Message enabled successfully!";
	public static final String MESSAGE_DISABLED = "Message disabled successfully!";
	public static final String MESSAGE_NOT_ENABLED = "Message enable unsuccessful";
	public static final String MESSAGE_ADDED_SUCCESSFULLY= " Message added successfully";
	public static final String DUPLICATE_MESSAGE= "Duplicate Message  : Message already Exists";
	public static final String MESSAGE_UPDATED_SUCCESSFULLY= " Message updated successfully";
	public static final String MESSAGE_NOT_UPDATED_SUCCESSFULLY= " Message not updated successfully";
	public static final String MESSAGE_NOT_ADDED_SUCCESSFULLY= " Message not added successfully";
	
	public static final String NETWORK_ENGINEER = "Network Engineer";
	public static final String HOME_PAGE = "Home,/Home";
	public static final String PAGE = "page";

	protected static final List<String> adminRoles = new ArrayList<>(Arrays.asList("Admin", "ISF Access Manager", "DS", "EU", "MDM Admin"));

	@Autowired
	private AccessManagementDAO accessManagementDAO;

	@Autowired /* Bind to bean/pojo */
	private FlowChartService flowChartService;

	@Autowired
	private OutlookAndEmailService emailService;

	@Autowired
	private AspManagementService aspManagement;

	@Autowired
	private ActivityMasterDAO activityMasterDAO;

	@Autowired
	private AspManagementDao aspManagementDao;

	@Autowired
	private AppService appService;
	
	@Autowired
	private ApiManagerServiceBuilder apiManagerServiceBuilder;

	@Autowired
	private ValidationUtilityService validationUtilityService;

	private static final Logger LOG = LoggerFactory.getLogger(AccessManagementService.class);

	public List<OrganizationModel> getOrganizationList() {
		return this.accessManagementDAO.getOrganizationList();
	}

	public List<AccessProfileModel> getAccessProfiles() {
		return this.accessManagementDAO.getAccessProfiles();
	}
	

	private static SearchControls getSearchControls() {
		SearchControls cons = new SearchControls();
		cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String[] attrIDs = { "cn", "mail", AppConstants.DISPLAY_NAME, AppConstants.THUMBNAILPHOTO };
		cons.setReturningAttributes(attrIDs);
		return cons;
	}

	private String BASE_64_DATA_TYPE = "data:image/png;base64,";

	static Random random = new Random();
	
	public String getUserImageURI(String signumID) {
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				configurations.getStringProperty(ConfigurationFilesConstants.INITIAL_CONTEXT_FACTORY));
		env.put(Context.PROVIDER_URL, configurations.getStringProperty(ConfigurationFilesConstants.LDAP_PROVIDER_URL));
		env.put(Context.SECURITY_PRINCIPAL,
				configurations.getStringProperty(ConfigurationFilesConstants.SECURITY_PRINCIPAL));
		env.put(Context.SECURITY_CREDENTIALS, AppUtil.DecryptText(
				configurations.getStringProperty(ConfigurationFilesConstants.SECURITY_CREDENTIALS), AppConstants.ISF123));
		InitialLdapContext ctx = null;
		try {
			// Create the initial context
			ctx = new InitialLdapContext(env, null);

			SearchControls ctrl = new SearchControls();
			ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);

			SearchControls cons = new SearchControls();
			cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String[] attrIDs = { AppConstants.THUMBNAILPHOTO };
			cons.setReturningAttributes(attrIDs);

			NamingEnumeration<SearchResult> answer = ctx.search(AppConstants.DC_ERICSSON, "(cn=" + signumID + ")", cons);
			if (answer.hasMore()) {
				Attributes attrs = answer.next().getAttributes();
				Object d = attrs.get(AppConstants.THUMBNAILPHOTO).get();
				byte[] encoded = Base64.getEncoder().encode((byte[]) d);
				return BASE_64_DATA_TYPE + (new String(encoded));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	public List<CPMModel> getCPMdetails(String signumID) throws NamingException {
		List<CPMModel> result = this.accessManagementDAO.getCPMdetails(signumID);
		if (result.isEmpty()) {
			Hashtable env = new Hashtable();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					configurations.getStringProperty(ConfigurationFilesConstants.INITIAL_CONTEXT_FACTORY));
			env.put(Context.PROVIDER_URL,
					configurations.getStringProperty(ConfigurationFilesConstants.LDAP_PROVIDER_URL));
			env.put(Context.SECURITY_PRINCIPAL,
					configurations.getStringProperty(ConfigurationFilesConstants.SECURITY_PRINCIPAL));
			env.put(Context.SECURITY_CREDENTIALS, AppUtil.DecryptText(
					configurations.getStringProperty(ConfigurationFilesConstants.SECURITY_CREDENTIALS), AppConstants.ISF123));
			
			InitialLdapContext ctx=null;

			try {
				// Create the initial context
				ctx = new InitialLdapContext(env, null);

					NamingEnumeration<SearchResult> answer = ctx.search(AppConstants.DC_ERICSSON, "(cn=" + signumID + ")",
							getSearchControls());
					if (answer.hasMore()) {
						Attributes attrs = answer.next().getAttributes();
						CPMModel a = new CPMModel();
						a.setMailID(attrs.get("mail").get().toString());
						a.setName(attrs.get(AppConstants.DISPLAY_NAME).get().toString());
						a.setSignum(attrs.get("cn").get().toString());
						List<CPMModel> newResult = Arrays.asList(a);
						this.accessManagementDAO.insertCpmDetails(a.getMailID(), a.getName(), a.getSignum());
						return newResult;
					}

			} catch (Exception e) {
				LOG.info(AppConstants.EXCEPTION, e.getMessage());
			}finally {
				if(ctx!=null) {
					ctx.close();
				}
			}

		}
		return result;
	}

	public boolean intialLdap(Hashtable env) {

		final Runnable stuffToDo = new Thread() {
			@Override
			public void run() {
				try {
					InitialLdapContext ctx = new InitialLdapContext(env, null);
					LOG.info("Check InitialLdapContext {} ",ctx);
				} catch (NamingException e) {
					LOG.info(AppConstants.EXCEPTION,e);
				}
			}
		};
		boolean flag = true;
		final ExecutorService executor = Executors.newSingleThreadExecutor();
		final Future<?> future = executor.submit(stuffToDo);
		executor.shutdown(); // This does not cancel the already-scheduled task.

		try {
			future.get(2, TimeUnit.SECONDS);
		} catch (InterruptedException ie) {
			/* Handle the interruption. Or ignore it. */
			LOG.warn("Interrupted exception!", ie);
			Thread.currentThread().interrupt();
		} catch (ExecutionException ee) {
			/* Handle the error. Or ignore it. */
		} catch (TimeoutException te) {
			/* Handle the timeout. Or ignore it. */
			flag = false;
		}
		if (!executor.isTerminated()) {
			executor.shutdownNow();
		}
		return flag;

	}

	@SuppressWarnings("unchecked")
	public List<CPMModel> getUserInfoUsingLDAP(String signumID) throws NamingException {
		List<CPMModel> newResult=new ArrayList<>();
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				configurations.getStringProperty(ConfigurationFilesConstants.INITIAL_CONTEXT_FACTORY));
		env.put(Context.PROVIDER_URL, configurations.getStringProperty(ConfigurationFilesConstants.LDAP_PROVIDER_URL));
		env.put(Context.SECURITY_PRINCIPAL,
				configurations.getStringProperty(ConfigurationFilesConstants.SECURITY_PRINCIPAL));
		env.put(Context.SECURITY_CREDENTIALS, AppUtil.DecryptText(
				configurations.getStringProperty(ConfigurationFilesConstants.SECURITY_CREDENTIALS), AppConstants.ISF123));
		InitialLdapContext ctx=null;
		try {
			// Create the initial context
			boolean flag = intialLdap(env);
			if (flag) {
				ctx = new InitialLdapContext(env, null);
					NamingEnumeration<SearchResult> answer = ctx.search(AppConstants.DC_ERICSSON, "(cn=" + signumID + ")",
							getSearchControls());
					if (answer.hasMore()) {
						Attributes attrs = answer.next().getAttributes();
						CPMModel a = new CPMModel();
						a.setMailID(attrs.get("mail").get().toString());
						a.setName(attrs.get(AppConstants.DISPLAY_NAME).get().toString());
						a.setSignum(attrs.get("cn").get().toString());
						setImageUriToCPMModel(a, attrs);
						newResult = Arrays.asList(a);
						return newResult;
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(ctx!=null) {
				ctx.close();
			}
		}
		return newResult;

	}
	private void setImageUriToCPMModel(CPMModel a, Attributes attrs) {
		try {
			Object d = attrs.get(AppConstants.THUMBNAILPHOTO).get();
			byte[] encoded = Base64.getEncoder().encode((byte[]) d);
			String b = BASE_64_DATA_TYPE + (new String(encoded));
			a.setImageURI(b);
		}catch (Exception e) {
			LOG.info(AppConstants.EXCEPTION, e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<CPMModel> getSignumUsingEmailLDAP(String email) throws NamingException {
		List<CPMModel> newResult=new ArrayList<CPMModel>();
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				configurations.getStringProperty(ConfigurationFilesConstants.INITIAL_CONTEXT_FACTORY));
		env.put(Context.PROVIDER_URL, configurations.getStringProperty(ConfigurationFilesConstants.LDAP_PROVIDER_URL));
		env.put(Context.SECURITY_PRINCIPAL,
				configurations.getStringProperty(ConfigurationFilesConstants.SECURITY_PRINCIPAL));
		env.put(Context.SECURITY_CREDENTIALS, AppUtil.DecryptText(
				configurations.getStringProperty(ConfigurationFilesConstants.SECURITY_CREDENTIALS), AppConstants.ISF123));
		InitialLdapContext ctx=null;
		try {
			// Create the initial context
			ctx = new InitialLdapContext(env, null);

				NamingEnumeration<SearchResult> answer = ctx.search(AppConstants.DC_ERICSSON, "(mail=" + email + ")",
						getSearchControls());
				if (answer.hasMore()) {
					Attributes attrs = answer.next().getAttributes();
					CPMModel a = new CPMModel();
					a.setMailID(attrs.get("mail").get().toString());
					a.setName(attrs.get(AppConstants.DISPLAY_NAME).get().toString());
					a.setSignum(attrs.get("cn").get().toString());
					newResult = Arrays.asList(a);
					return newResult;

				}

		} catch (Exception e) {
			LOG.info(AppConstants.EXCEPTION, e.getMessage());
		}finally {
			if(ctx!=null) {
				ctx.close();
			}
		}
		return newResult;

	}

	@SuppressWarnings("unchecked")
	public boolean authenticate(String username, String password) throws NamingException {
		InitialLdapContext ctx=null;
		try {
			String decryptString = decrypt(password);
			List<String> ascii = Arrays.asList(decryptString.split("\\|"));
			Collections.reverse(ascii);
			String newPass = "";
			for (String item : ascii) {
				char tmpPass = ASCIIToChar(Integer.parseInt(item));
				newPass += tmpPass;
			}

			Hashtable env = new Hashtable();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					configurations.getStringProperty(ConfigurationFilesConstants.INITIAL_CONTEXT_FACTORY));
			env.put(Context.PROVIDER_URL,
					configurations.getStringProperty(ConfigurationFilesConstants.LDAP_PROVIDER_URL));
			env.put(Context.SECURITY_PRINCIPAL, username);
			env.put(Context.SECURITY_CREDENTIALS, newPass);

			// Create the initial context
			ctx = new InitialLdapContext(env, null);

				NamingEnumeration<SearchResult> answer = ctx.search(AppConstants.DC_ERICSSON,
						"(cn=" + username.substring(username.lastIndexOf("\\") + 1) + ")", getSearchControls());
				if (answer.hasMore()) {
					Attributes attrs = answer.next().getAttributes();
					if (!"".equals(attrs.get("cn").get().toString())) {
						return true;
					}

				}
		} catch (Exception e) {
			LOG.info(AppConstants.EXCEPTION, e.getMessage());
		}finally {
			if(ctx!=null) {
				ctx.close();
			}
		}
		return false;

	}

	public String decrypt(String encrypted) {
		Base64.Decoder decoder = Base64.getUrlDecoder();
		// Decoding URl
		String dStr = new String(decoder.decode(encrypted));
		return dStr;

	}

	public static char ASCIIToChar(final int ascii) {
		return (char) ascii;
	}

	public List<RoleModel> getRoleList(String userType) {
		if (userType == null) {
			return this.accessManagementDAO.getRoleList();
		} else {
			return this.accessManagementDAO.getRoleListByType(userType);
		}
	}

	@Transactional("transactionManager")
	public String assignAccessToUser(int roleID, int accessProfileID, String empSignumID, String loggedInUser) {
		 String role = this.accessManagementDAO.getAccessProfileName(roleID,accessProfileID);

		Boolean checkifdetailsExist = this.accessManagementDAO.checkIFAccessDetailsExists(accessProfileID, empSignumID);
		 if (role !=null) {
			if (checkifdetailsExist)
			{
				return "Access details already exists!!!";
			} 
			else 
			{
				String dDays = appService.getAccessProfileDays();
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DATE, Integer.parseInt(dDays));
				String message= this.accessManagementDAO.assignAccessToUser(roleID, accessProfileID, empSignumID, loggedInUser, c.getTime());
				
				if (role.equalsIgnoreCase(AppConstants.BOT_DEVELOPER)) {
					createApiManagerUser(empSignumID);
				}
				
				boolean checkifRequestExist = this.accessManagementDAO.checkifProfileRequestExist(accessProfileID,empSignumID);
				if (checkifRequestExist)
				{
					accessManagementDAO.updateProfileRequest(accessProfileID, empSignumID, loggedInUser);
					
				}
				
				return message;
			}
		}
	else

	{
		return "Invalid input... RoleID or AccessProfileID doesn't exists!!!";
	}
	}

	public void createApiManagerUser(String signumID) {
		try {
			
			ApiManagerUserModel apiManagerUserModel=accessManagementDAO.getUserAzureObjectDetails(signumID);
			if(apiManagerUserModel !=null && apiManagerUserModel.getIdentities().get(0).getId()!=null) {
				apiManagerUserModel.setState("Active");
				apiManagerUserModel.getIdentities().get(0).setProvider("Aad");
				apiManagerUserModel.setAppType(AppConstants.DEVELOPER_PORTAL);
						
				ApiManagerService apiManagerService = apiManagerServiceBuilder.getApiManagerService();
				retrofit2.Response<ApiManagerResponseModel> response;
				response = apiManagerService
						.users(apiManagerUserModel.getIdentities().get(0).getId(),
								AppConstants.AZURE_API_VERSION,true,new ApiManagerRequestModel(apiManagerUserModel)).execute();
				if (response.isSuccessful()) {
					LOG.info( String.format(SUCCESS_APIM_SIGNUP, response.body().toString()));
				} else {
					LOG.info(String.format(FAILURE_APIM_SIGNUP, IOUtils.toString(response.errorBody().byteStream(), StandardCharsets.UTF_8.name())));
				}
			}
			else {
				LOG.info(signumID+": doesn't have valid entry in [refData].[TBL_EMPlOYEE_AZURE_OBJECT_DETAILS]");
			}
		} catch (IOException ie) {
			LOG.error(String.format(APIM_CONNECTION_PROBLEM,ie.getMessage()));
			ie.printStackTrace();
		} catch (Exception e) {
			LOG.error("Error createApiManagerUse:"+e.getMessage());
			e.printStackTrace();
		}
	}

	
	public void revokeApiManagerUser(String signumID) {
		try {
			
			ApiManagerUserModel apiManagerUserModel=accessManagementDAO.getUserAzureObjectDetails(signumID);
			if(apiManagerUserModel !=null && apiManagerUserModel.getIdentities().get(0).getId()!=null) {
				apiManagerUserModel.setState("BLOCKED");
				apiManagerUserModel.getIdentities().get(0).setProvider("Aad");
				apiManagerUserModel.setAppType(AppConstants.DEVELOPER_PORTAL);
				
				ApiManagerService apiManagerService = apiManagerServiceBuilder.getApiManagerService();
				retrofit2.Response<ApiManagerResponseModel> response;
				response = apiManagerService
						.users(apiManagerUserModel.getIdentities().get(0).getId(),AppConstants.AZURE_API_VERSION,null,
								new ApiManagerRequestModel(apiManagerUserModel)).execute();
				if (response.isSuccessful()) {
					LOG.info( String.format(SUCCESS_APIM_REVOKE, response.body().toString()));
				} else {
					LOG.info(String.format(FAILURE_APIM_REVOKE, IOUtils.toString(response.errorBody().byteStream(), StandardCharsets.UTF_8.name())));
				}
			}
			else {
				LOG.info(signumID+": doesn't have valid entry in [refData].[TBL_EMPlOYEE_AZURE_OBJECT_DETAILS]");
			}
		} catch (IOException ie) {
			LOG.error(String.format(APIM_CONNECTION_PROBLEM,ie.getMessage()));
			ie.printStackTrace();
		} catch (Exception e) {
			LOG.error("Error revokeApiManagerUser:"+e.getMessage());
			e.printStackTrace();
		}
	}
	@Transactional("transactionManager")
	public void disableAccessToUser(int userAccessProfID, int accessProfileID, String signumID, String modifiedBy) {
		Boolean checkSignum = this.accessManagementDAO.IsSignumExists(signumID);
		if (checkSignum) {
			this.accessManagementDAO.disableAccessToUser(userAccessProfID, accessProfileID, signumID, modifiedBy);
		} else {
			throw new ApplicationException(500, "Invalid input... SignumID doesn't exists !!!");
		}

	}

	@Transactional("transactionManager")
	public void updateApprovalStatus(int userAccessProfID, String signumID, String approvalStatus, String approvedBy) {
		this.accessManagementDAO.updateApprovalStatus(userAccessProfID, signumID, approvalStatus, approvedBy);
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
	public Map<String, Object> searchAccessDetailsByFilter(String signumID, int roleID, int organisationID, String lineManagerSignum, DataTableRequest dataTableReq) {

		Map<String, Object> response = new HashMap<String, Object>();
		List<UserAccessProfileModel> accessList = accessManagementDAO.searchAccessDetailsByFilter(signumID, roleID,
				organisationID,lineManagerSignum,dataTableReq);
		
		
		response.put(AppConstants.DATA_IN_RESPONSE, accessList);
		response.put(AppConstants.DRAW, dataTableReq.getDraw());
		
		if(accessList.size() != 0) {
			response.put(AppConstants.RECORD_TOTAL, accessList.get(0).getTotalCounts());
			response.put(AppConstants.RECORD_FILTERED, accessList.get(0).getTotalCounts());			
		} else {
			response.put(AppConstants.RECORD_TOTAL, 0);
			response.put(AppConstants.RECORD_FILTERED, 0);			
		}
		return response;
	}

	@Transactional("transactionManager")
	public void createNewAccessProfile(String profileName, int role, int organization) {
		Boolean checkProfile = this.accessManagementDAO.checkIFProfileExists(profileName);
		if (checkProfile) {
			throw new ApplicationException(500, "Access Profile details already exists!!!");
		} else {
			this.accessManagementDAO.createNewAccessProfile(profileName, role, organization);
		}
	}

	public List<AccessProfileModel> getAccessProfilesByRole(int roleID) {
		Boolean checkRole = this.accessManagementDAO.IsRoleExists(roleID);
		if (!checkRole) {
			throw new ApplicationException(500, "The roleID specified does not exists!!!");
		} else {
			return this.accessManagementDAO.getAccessProfilesByRole(roleID);
		}
	}

	public Response<List<UserDetailsAccessModel>> getUserAccessProfile(String signumID) {

		Response<List<UserDetailsAccessModel>> response = new Response<>();
		boolean checkRole = this.accessManagementDAO.IsSignumExists(signumID);
		
		
		try {		
			if (checkRole) {
				List<UserDetailsAccessModel> userData = this.accessManagementDAO.getUserAccessProfile(signumID);
				if (!userData.isEmpty()) {
					//// For Image Using LDAP
						getUserImageFromDB(signumID, userData);
				}
				response.setResponseData(userData);
			} else {
				boolean checkEmp = this.accessManagementDAO.IsSignumExistsHRMS(signumID);
				
				if(!checkEmp) {
					throw new ApplicationException(200, AppConstants.SIGNUM_NOT_FOUND);
				}
					boolean checkEmpTbl = this.accessManagementDAO.isSignumExistsEmp(signumID);
					if (!checkEmpTbl) {
						this.accessManagementDAO.saveUserDetailsHRMS(signumID);
					}
					this.accessManagementDAO.saveUserAccessProfileHRMS(signumID);
					//// For Email Using LDAP
						getEmailFromLDAP(signumID);
					List<UserDetailsAccessModel> userData = this.accessManagementDAO.getUserAccessProfile(signumID);
					response.setResponseData(userData);
			}
		}
		catch (ApplicationException e) {
			response.addFormError(AppConstants.SIGNUM_NOT_FOUND);
			LOG.info("The signumID specified does not exists!!! {}", e.getMessage());
			return response;
		}
		catch (Exception e) {
			LOG.info(AppConstants.EXCEPTION, e);
			response.addFormError(e.getMessage());
		}
		return response;
	}

	private void getEmailFromLDAP(String signumID) throws NamingException {
		List<String> empList = this.accessManagementDAO.getEmployeeWithNoEmailBySignum(signumID);
		for (String emp : empList) {
			List<CPMModel> empDetail = getUserInfoUsingLDAP(emp);
			if (empDetail != null) {
				this.accessManagementDAO.updateEmployeeEmail(empDetail.get(0));
			}
		}
	}

	public List<RoleModel> getRoleList() {
		return this.accessManagementDAO.getRoleList();
	}

	public List<CapabilityModel> getCapability() {
		return this.accessManagementDAO.getCapability();
	}

	public List<HashMap<String, Object>> getAccessEmployee() {
		return this.accessManagementDAO.getAccessEmployee();
	}

	public List<String> getUserByFilter(int roleID, int orgID) {
		Boolean checkRole = this.accessManagementDAO.IsRoleAndOrgExists(roleID, orgID);
		if (checkRole) {
			return this.accessManagementDAO.getUserByFilter(roleID, orgID);
		} else {
			throw new ApplicationException(500, "The values specified does not exists!!!");
		}
	}

	public List<String> getUsersByAlias(String alias, int orgID) {
		return this.accessManagementDAO.getUsersByAlias(alias, orgID);
	}

	@Transactional("transactionManager")
	public void editAccessProfile(AccessProfileModel accessProfileModel) {
		Boolean checkProfile = this.accessManagementDAO.checkAccessProfileID(accessProfileModel.getAccessProfileID());
		if (checkProfile) {
			this.accessManagementDAO.editAccessProfile(accessProfileModel);
		} else {
			throw new ApplicationException(500, "The Access Profile specified does not exists!!!");
		}
	}

	public List<AccessProfileUserDetailModel> searchAccessDetails(int accessProfileID, int roleID, int organisationID) {
		List<AccessProfileUserDetailModel> accessProfileUserDetailModel = accessManagementDAO
				.searchAccessDetails(accessProfileID, roleID, organisationID);
		return accessProfileUserDetailModel;
	}

	@Transactional("transactionManager")
	public void deleteAccessProfile(int accessProfileID) {
		Boolean checkProfile = this.accessManagementDAO.checkAccessProfileID(accessProfileID);
		if (checkProfile) {
			this.accessManagementDAO.deleteAccessProfile(accessProfileID);
		} else {
			throw new ApplicationException(500, "The Access Profile specified does not exists!!!");
		}
	}

	public List<UserDetailsAccessModel> getUserProfileByFilter(String signumID, int roleID, int organisationID) {
		return this.accessManagementDAO.getUserProfileByFilter(signumID, roleID, organisationID);
	}

	public List<String> getUserDetailsForDR(String roleID, String marketArea, String projectID) {
		Boolean checkRole = this.accessManagementDAO.IsRoleDetailsExists(roleID);
		if (checkRole) {
			return this.accessManagementDAO.getUserDetailsForDR(roleID, marketArea, projectID);
		} else {
			throw new ApplicationException(500, "The values specified does not exists!!!");
		}
	}

	public List<String> getRPMByMarket(String marketArea) {
		return this.accessManagementDAO.getRPMByMarket(marketArea);
	}

	@Transactional("transactionManager")
	public void deleteDeliveryResponsible(int deliveryResponsibleID, String signumID) {
		Boolean checkProfile = this.accessManagementDAO.checkDeliveryResponsibleID(deliveryResponsibleID);
		if (checkProfile) {
			this.accessManagementDAO.deleteDeliveryResponsible(deliveryResponsibleID, signumID);
		} else {
			throw new ApplicationException(500, "The Specified Delivery Responsible does not exists!!!");
		}
	}

	@Transactional("transactionManager")
	public boolean updateAccessProfileStatus(AccessProfileRequestModel profileRequest) {
		Boolean checkifdetailsExist;
		if (profileRequest.getStatus() == 1) {

			checkifdetailsExist = this.accessManagementDAO
					.checkIFAccessDetailsExists(profileRequest.getAccessProfileID(), profileRequest.getSignum());

			if (checkifdetailsExist) {
				throw new ApplicationException(500, "Access details already exists!!!");
			} else {
				this.accessManagementDAO.insertAccessProfileStatus(profileRequest);
			}

		}
		return this.accessManagementDAO.updateAccessProfileStatus(profileRequest);
	}

	public static final String REQUEST_RAISED = "Your Access request is raised Successfully.";
	public static final String REQUEST_ALREADY_APPROVED_ADMIN = "You already have access to this Profile.";
	public static final String REQUEST_ALREADY_APPROVED = "Your Access request is already APPROVED.Please contact your Manager for renewal.";
	public static final String REQUEST_ALREADY_RAISED = "Your Access request is already RAISED. Kindly wait for approval.";

	
	public Response<Void> requestAccessProfile(RequestRoleAccessModel requestRoleAccess) {
		Response<Void> response = new Response<>();

		String result=this.accessManagementDAO.requestAccessProfile(requestRoleAccess);
		if(result.equalsIgnoreCase("SUCCESS")){
			
			response.addFormMessage(REQUEST_RAISED);

			// creating a separate thread to send mail
			Runnable r=() -> {
				try {
				    emailService.sendMail(AppConstants.PROFILE_ACCESS_REQUEST, enrichMailForAccessRequest(requestRoleAccess));
				} catch (Exception e) {
					LOG.debug(AppConstants.EXCEPTION, e.getMessage());
				}
			};
			new Thread(r).start();
		} else {
			response.addFormWarning(result);
		}
		
		return response;
	}

	public Map<String, Object> enrichMailForAccessRequest(RequestRoleAccessModel req) {
		EmployeeModel employee = activityMasterDAO.getEmployeeBySignum(req.getSignum());
		String approvers = "";
		Map<String, Object> data = new HashMap<String, Object>();
		List<String> accessAdmins;
		String accessProfileName = this.accessManagementDAO.getProfileNameById(req.getAccessProfileID());
		if (adminRoles.contains(accessProfileName.split(DASH)[0])) {
			if(accessProfileName.equals(AppConstants.ADMIN_PROFILE_NAME) || accessProfileName.equals(AppConstants.ROLE_ISF_ACCESS_MANAGER)) {
				accessAdmins= this.accessManagementDAO.getAllEmailforRole(AppConstants.ADMIN.toString());
			}
			else {
				accessAdmins= this.accessManagementDAO.getAllEmailforRole(AppConstants.ROLE_ISF_ACCESS_MANAGER);
			}
			data.put(AppConstants.NOTIFICATION_URL_PATH, this.getPathByName("Request Approval"));
			approvers=accessAdmins.stream().collect(Collectors.joining(";"));
		} else {
			EmployeeModel manager = activityMasterDAO.getEmployeeBySignum(employee.getManagerSignum());
			approvers = manager.getEmployeeEmailId();
			data.put(AppConstants.NOTIFICATION_URL_PATH, this.getPathByName("Profile Access"));
		}

		data.put(AppConstants.REQUEST, req);
		data.put("accessProfileName", this.accessManagementDAO.getProfileNameById(req.getAccessProfileID()));
		data.put("emplyeeName", employee.getEmployeeName());
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, approvers);
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, employee.getEmployeeEmailId());
		return data;
	}

	public List<Map<String, Object>> requestAccessProfileDetail(RequestRoleAccessModel requestRoleAccess) {
		return this.accessManagementDAO.requestAccessProfileDetail(requestRoleAccess);
	}

	@Transactional("transactionManager")
	public ResponseEntity<Response<Integer>> saveLoginDetails(UserLoginModel userLogin) throws IOException {
		Response<Integer> response=new Response<Integer>();
		try {
			if (StringUtils.equalsIgnoreCase(AppConstants.LOGIN, userLogin.getStatus())) {
				List<Integer> openLogIDs=this.accessManagementDAO.getUserCurrentDeviceOpenSessions(userLogin);
				if(CollectionUtils.isNotEmpty(openLogIDs)) {
					this.accessManagementDAO.closeSessions(openLogIDs, userLogin.getSignumID());
				}
				this.accessManagementDAO.saveLoginDetails(userLogin);
			} else if (StringUtils.equalsIgnoreCase(AppConstants.LOGOUT, userLogin.getStatus())) {
				this.accessManagementDAO.updateLoginDetail(userLogin);
			}
			response.setResponseData(userLogin.getLogID());
		}
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
		}
		catch (Exception e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<Integer>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		return new ResponseEntity<Response<Integer>>(response, HttpStatus.OK);
	}

	public List<UserLoginModel> getLoginHistory(String signumID) {
		return this.accessManagementDAO.getLoginHistory(signumID);
	}

	@Transactional("transactionManager")
	public void updateLoginHistory(String signumID, int logID, String logoutTime) throws ParseException {
		Date logOutdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(logoutTime);
		this.accessManagementDAO.updateLoginHistory(signumID, logID, logOutdate);
	}

	@Transactional("transactionManager")
	@ApiOperation("This is Service layer")
	public List<CapabilityPageGroupModel> getUserMenu() {
		
		return this.accessManagementDAO.getUserMenu();
	}

	public Map<String, Object> getSiteStatus() {
		
		return this.accessManagementDAO.getSiteStatus();
	}

	public List<Map<String, String>> checkEmpAsPM(String signumID, int projectID) {
		return this.accessManagementDAO.checkEmpAsPM(signumID, projectID);
	}

	public List<EricssonOrganizationModel> getEricssonOrgDetails() {
		return this.accessManagementDAO.getEricssonOrgDetails();
	}

	public Map<String, Object> validateEricssonEmployesDetails(MultipartFile empFile, String signum)
			throws IOException, SQLException {

		String fileName = empFile.getName();
		fileName = AppUtil.getFileNameWithTimestamp(fileName);
		flowChartService.uploadFile(empFile, "EmployeeData", fileName);

		String filePath = appService.getConfigUploadFilePath() + AppConstants.FORWARD_SLASH + "EmployeeData" + AppConstants.FORWARD_SLASH + fileName;

		Map<String, Object> data = appService.CsvBulkUploadNewGenWothoutTmpTable(filePath, "TBL_Ericsson_Employees");
		if ((boolean) data.get(AppConstants.ISUPLOADERROR)) {
			List<String> addCount = this.accessManagementDAO.countToBeAdded();
			List<String> deleteCount = this.accessManagementDAO.countToBeDeleted();
			this.accessManagementDAO.deleteTmpTable();
			data.put("addCount", addCount.size());
			data.put("deleteCount", deleteCount.size());
			data.put("filePath", filePath);
		}
		return data;
	}

	public Map<String, Object> uploadEricssonEmployesDetails(String filePath, String signum) throws IOException {
		this.accessManagementDAO.deleteTmpTable();
		Map<String, Object> data = new HashMap<>();
		try {
			data = appService.CsvBulkUploadNewGenWothoutTmpTable(filePath, "TBL_Ericsson_Employees");
			if ((boolean) data.get(AppConstants.ISUPLOADERROR)) {
				this.accessManagementDAO.updateEmployeeData(signum);
				new File(filePath).deleteOnExit();
			}
		} 
		catch (SQLException e) {
			data.put(AppConstants.ISUPLOADERROR, AppConstants.FALSE);
			data.put("msg",
					"Following error may exist in File:\r\n" + "   1. Length in any column is more than expected\r\n"
							+ "   2. Null value in a mandatory column\r\n"
							+ "   3. Some fields may have different datatype\r\n"
							+ "	Kindly update the file and try again!!");
			data.put("ErrorFlag", true);
			data.put("Error", e.getMessage());
		} 
		catch (Exception e) {
			data.put(AppConstants.ISUPLOADERROR, AppConstants.FALSE);
			data.put("msg",
					"Following error may exist in File:\r\n" + "   1. Length in any column is more than expected\r\n"
							+ "   2. Null value in a mandatory column\r\n"
							+ "   3. Some fields may have different datatype\r\n"
							+ "	Kindly update the file and try again!!");
			data.put("ErrorFlag", true);
			data.put("Error", e.getMessage());
		}
		return data;
	}

	public void updateEricssonOrg(String data) {
		ArrayList<String> parts = new ArrayList<>(Arrays.asList(data.split(",")));
		List<String> newParts = new ArrayList<String>();
		for (String a : parts) {
			if (a.startsWith("C_")) {
				a = a.replaceFirst("C_", "");
				newParts.add(a);
			}
		}
		this.accessManagementDAO.inactiveEricssonOrg();
		this.accessManagementDAO.updateEricssonOrg(newParts);
	}

	@Transactional("transactionManager")
	public Map<String, Object> aspIsfRegistraion(AspLoginModel aspLogin) throws NoSuchAlgorithmException {

		Map<String, Object> aspData = new HashMap<>();
		String msg = null;
		boolean isSuccess = false;
		AspVendorModel aspVendor = aspManagement.getVendorDetailsByID(aspLogin.getVendorCode());

		if (aspVendor == null) {
			msg = "Please Enter Vendor Code";
			aspData.put("msg", msg);
			aspData.put(AppConstants.ISSUCCESS, isSuccess);
			return aspData;
		}

		aspLogin.setManagerName(aspVendor.getManagerSignum());

		Map<String, Object> empData = this.accessManagementDAO.checkIfAspExistsByEmail(aspLogin.getEmail());
		if (empData == null || empData.isEmpty()) {
			
			String emailPrefix = aspLogin.getEmail().trim().replaceAll("@.*$", "");

			String emailMD5 = AppUtil.generateMD5(emailPrefix);

			String aspManagerEmail = this.activityMasterDAO.getEmployeeBySignum(aspVendor.getManagerSignum())
					.getEmployeeEmailId();
			if (aspManagerEmail == null) {
				aspManagerEmail = this.accessManagementDAO.getEmployeeFromHrms(aspVendor.getManagerSignum());
			}

			this.accessManagementDAO.insertAspEmployee(aspLogin, AppUtil.generateMD5(emailMD5));
			msg = "Registration Successfull. Please wait for approval from vendor manager.";
			this.accessManagementDAO.insertAspEricssonEmployee(aspLogin);
			isSuccess = this.aspManagementDao.insertAspUserAccessProfile(aspLogin.getSignum(),
					aspManagementDao.getAspProfileIdByName(USER_TYPE));
			this.aspManagement.insertIntoUserAccessAsp(aspLogin.getSignum());
			emailService.sendMail(AppConstants.ISF_ASP_REGITER, enrichMailforaspLogin(aspLogin, emailMD5));
			emailService.sendMail(AppConstants.ISF_ASP_MANAGER_RESPONSE,
					enrichMailforaspLogin(aspLogin, aspManagerEmail, aspVendor.getVendorName()));

		} else {
			isSuccess = false;
			msg = "You are already regestered with ISF. Please go to Login page";
			aspData.put("data", empData);
		}

		aspData.put("msg", msg);
		aspData.put(AppConstants.ISSUCCESS, isSuccess);

		return aspData;
	}

	public Map<String, Object> enrichMailforaspLogin(AspLoginModel req, String pwd) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(AppConstants.REQUEST, req);
		data.put("pwd", pwd);
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, req.getEmail());
		return data;
	}

	public Map<String, Object> enrichMailforaspLogin(AspLoginModel req, String managerEmail, String vendorName) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(AppConstants.REQUEST, req);
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, managerEmail);
		data.put("name", vendorName);
		data.put(AppConstants.NOTIFICATION_URL_PATH, "ASP Explorer");
		return data;
	}

	public Map<String, Object> aspIsfReset(ResetLoginAspModel resetLoginAsp) throws NoSuchAlgorithmException {

		Map<String, Object> empData = this.accessManagementDAO.checkIfAspExists(resetLoginAsp);
		String msg = null;
		boolean isSuccess = false;
		Map<String, Object> aspData = new HashMap<String, Object>();
		String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
		if (empData == null || empData.isEmpty()) {
			msg = ASP_LOGIN_ERROR_REGISTER;
		} else if (resetLoginAsp.getNewPassword().matches(pattern)) {
			resetLoginAsp.setOldPassword(AppUtil.generateMD5(resetLoginAsp.getOldPassword().trim()));
			resetLoginAsp.setPasswordMD5(AppUtil.generateMD5(resetLoginAsp.getNewPassword().trim()));
			boolean flag = this.accessManagementDAO.updateNewPassword(resetLoginAsp);
			if (flag) {
				aspData.put("data", empData);
				msg = "Password Reset Successfully";
				String aspSignum = aspManagement.getAspSignumByEmail(resetLoginAsp.getEmail());
				aspManagement.setResetRequired(0, aspSignum);
				aspManagement.updateAspLastPasswordChange(aspSignum);
				isSuccess = true;
			} else {
				msg = "Old Password is not correct!! ";
			}

		} else if (!resetLoginAsp.getNewPassword().matches(pattern)) {
			msg = "Password Format is incorrect.";
		}

		aspData.put("msg", msg);
		aspData.put("data", empData);
		aspData.put(AppConstants.ISSUCCESS, isSuccess);
		return aspData;
	}

	public Response<Map<String, Object>> aspIsfLogin(ResetLoginAspModel resetLoginAsp) throws NoSuchAlgorithmException {

		Response<Map<String, Object>> response = new Response<>();
		String msg = null;
		boolean isSuccess = false;

		Map<String, Object> empData = this.accessManagementDAO.checkIfAspExists(resetLoginAsp);
		resetLoginAsp.setPasswordMD5(AppUtil.generateMD5(resetLoginAsp.getCurrentPassword().trim()));
		Map<String, Object> empValidData = this.accessManagementDAO.checkIfAspExists(resetLoginAsp);
		AspExplorerModel aspExplorerModel = null;
		Map<String, Object> aspData = new HashMap<>();

		if (MapUtils.isEmpty(empData)) {
			response.addFormError(ASP_LOGIN_ERROR_REGISTER);
			aspExplorerModel=new AspExplorerModel();
		} 
		else {
			aspExplorerModel = this.aspManagement
					.getAspFlags(this.aspManagement.getAspSignumByEmail(resetLoginAsp.getEmail()));
			if (MapUtils.isEmpty(empValidData)) {
				int failedAttempts = aspManagement.getFailedAttempts(aspExplorerModel.getSignum());
				if (failedAttempts == ASP_MAX_PASSWORD_TRIES) {
					aspManagement.setProfileLocked(1, aspExplorerModel.getSignum());
					msg = ASP_LOGIN_ERROR_3_ATTEMPTS;
				} else {
					aspManagement.updateFailedAttempts(aspExplorerModel.getSignum(), ++failedAttempts);
					msg = ASP_LOGIN_ERROR_PASSWORD_INCORRECT;
				}
				response.addFormError(msg);
			}
			else if (aspExplorerModel.getIsLocked()) {
				response.addFormError(ASP_LOGIN_ERROR_LOCKED);
			}else if (!resetLoginAsp.getVendorCode()
					.equals(aspManagement.getVendorByEmail(resetLoginAsp.getEmail()))) {
				response.addFormError(ASP_LOGIN_ERROR_VENDOR_INCORRECT);
			} else if (!aspExplorerModel.getIsProfileActive()) {
				response.addFormError(ASP_LOGIN_PROFILE_INACTIVE);
			} else {
				if (new Date().before(aspExplorerModel.getStartDate())
						|| new Date().equals(aspExplorerModel.getStartDate())) {
					response.addFormError(ASP_LOGIN_ERROR_BEFORE_ACCESS_START + aspExplorerModel.getStartDate());
				} else if (new Date().after(aspExplorerModel.getEndDate())) {
					response.addFormError(ASP_LOGIN_ERROR_AFTER_ACCESS_FINISH);
				} else {
					aspManagement.updateFailedAttempts(aspExplorerModel.getSignum(), 0);
					aspData.put("data", empData);
					isSuccess = true;
				}
			}
		}
		aspData.put("isResetRequired", aspExplorerModel.getIsResetRequired());
		aspData.put("data", empData);
		aspData.put(AppConstants.ISSUCCESS, isSuccess);
		response.setResponseData(aspData);
		
		return response;
	}

	@Transactional("transactionManager")
	public void updateAccessRole(RoleModel roleModel) throws ParseException {
		this.accessManagementDAO.updateAccessRole(roleModel);
	}

	@Transactional("transactionManager")
	public void deleteAccessRole(int accessRoleID) {

		this.accessManagementDAO.deleteAccessRole(accessRoleID);
	}

	public Map<String, Object> aspIsfForgetPassword(ResetLoginAspModel resetLoginAsp) throws NoSuchAlgorithmException {
		Map<String, Object> aspData = new HashMap<>();
		String msg = null;
		boolean isSuccess = false;

		Map<String, Object> empData = this.accessManagementDAO.checkIfAspExistsByEmail(resetLoginAsp.getEmail());
		if (empData == null || empData.isEmpty()) {
			msg = ASP_LOGIN_ERROR_REGISTER;
		} else {
			String emailPrefix = resetLoginAsp.getEmail().trim().replaceAll("@.*$", "");
			String emailMD5 = AppUtil.generateMD5(emailPrefix);
			resetLoginAsp.setPasswordMD5(AppUtil.generateMD5(emailMD5));
			this.accessManagementDAO.updateNewPassword(resetLoginAsp);
			emailService.sendMail(AppConstants.ISF_ASP_FORGOT_PASSWORD,
					enrichMailforaspForgetPassword(resetLoginAsp.getEmail(), emailMD5));
			msg = "Password Send Successfully !!";
			aspManagement.resetFlagsWhenForgetPassword(aspManagement.getAspSignumByEmail(resetLoginAsp.getEmail()));
			isSuccess = true;
		}

		aspData.put("msg", msg);
		aspData.put(AppConstants.ISSUCCESS, isSuccess);
		return aspData;
	}

	public Map<String, Object> enrichMailforaspForgetPassword(String email, String pwd) {
		Map<String, Object> data = new HashMap<>();
		data.put("email", email);
		data.put("pwd", pwd);
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, email);
		return data;

	}


	@Transactional("transactionManager")
	public Response<List<AccessRequestApprovalModel>> deleteAccessProfileBySignum(String signum,List<AccessRequestApprovalModel> accessRequestApprovalModelList, String role) {
		Response<List<AccessRequestApprovalModel>> response = new Response<>();
		String accessProfileName;
		if (CollectionUtils.isNotEmpty(accessRequestApprovalModelList)) {
			List<AccessRequestApprovalModel> responseData= new LinkedList<>();
			for (AccessRequestApprovalModel accessRequestApprovalModel : accessRequestApprovalModelList) {
				accessRequestApprovalModel.setActive(false);
				if(!StringUtils.equalsIgnoreCase(role,accessManagementDAO.getRoleByAccessProfileId(accessRequestApprovalModel.getAccessProfileId()))
						&& !StringUtils.equalsIgnoreCase(AppConstants.NE_ROLE,
								accessManagementDAO.getRoleByAccessProfileId(accessRequestApprovalModel.getAccessProfileId()))
						&& !StringUtils.equalsIgnoreCase(AppConstants.ADMIN,
								accessManagementDAO.getRoleByAccessProfileId(accessRequestApprovalModel.getAccessProfileId()))) {
					boolean isSuccess = this.accessManagementDAO.deleteAccessProfileBySignum(signum,accessRequestApprovalModel);
					if (isSuccess) {
						accessProfileName=this.accessManagementDAO.getAccessProfileName(0,Integer.parseInt(accessRequestApprovalModel.getAccessProfileId()));
						if(accessProfileName.equalsIgnoreCase(AppConstants.BOT_DEVELOPER)) {
							revokeApiManagerUser(signum);
						}
						response.addFormMessage(REVOKED_MESSAGE);		
					}
				}else {				
					responseData.add(accessRequestApprovalModel);
				}
			}
			if(CollectionUtils.isNotEmpty(responseData)) {
				response.addFormWarning(REVOKE_ERROR_MESSAGE);
			}
			response.setResponseData(responseData);
		}
		return response;
	}


	public void UpdateEmailFromLDAP() throws NamingException {
		
		List<String> empList = this.accessManagementDAO.getEmployeeWithNoEmail();
		for (String emp : empList) {
			List<CPMModel> empDetail = getUserInfoUsingLDAP(emp);
			if (empDetail != null) {
				this.accessManagementDAO.updateEmployeeEmail(empDetail.get(0));
			}

		}
	}

	public Map<String, Object> getDownStatus() {
		return this.accessManagementDAO.getDownStatus();
	}

	public List<String> getMarketAreaBySignum(String signumID) {

		return this.accessManagementDAO.getMarketAreaBySignum(signumID);
	}

	@Transactional("txManager")
	public Map<String, String> addUserPreferences(UserPreferencesModel preferences) {
		Map<String, String> respMap = new HashMap<>();

		try {
			LOG.info("preferences.getDefaultProfileId()-->: {}" , preferences.getDefaultProfileId());
			LOG.info("preferences.getDefaultProfileValue()-->: {}" , preferences.getDefaultProfileValue());
			LOG.info("preferences.getDefaultZoneId()-->: {}" , preferences.getDefaultZoneId());
			LOG.info("preferences.getDefaultZoneValue()-->: {}" , preferences.getDefaultZoneValue());
			LOG.info("preferences.getUserSignum()-->: {}" , preferences.getUserSignum());
			LOG.info("preferences.getCreatedBy()-->: {}" , preferences.getCreatedBy());
			LOG.info("preferences.getLastModifiedBy()-->: {}" , preferences.getLastModifiedBy());
			LOG.info("preferences.getDefaultPageId-->: {}" , preferences.getDefaultPageId());

			if (preferences.getDefaultProfileId() != null && preferences.getDefaultProfileValue() != null
					&& preferences.getDefaultZoneId() != null && preferences.getDefaultZoneValue() != null
					&& preferences.getDefaultPageId() != null && preferences.getDefaultPageName() != null) {
				List<Object[]> pref = this.accessManagementDAO.getActivePrefByVal(AppConstants.PROFILE, preferences.getUserSignum());
				LOG.info("Profile pref.size()-->: {}" , pref.size());
				if (pref == null || (pref != null && pref.size() < 1)) {
					this.accessManagementDAO.addPref(AppConstants.PROFILE, preferences);
				} else if (pref != null && pref.size() > 0
						&& !((String) pref.get(0)[1]).equalsIgnoreCase(preferences.getDefaultProfileValue())) {
					LOG.info("Existing Profile-->: {}" , (String) pref.get(0)[1]);
					// Deleting the old profile and adding new one:
					if (this.accessManagementDAO.deletePref((Integer) pref.get(0)[0],
							preferences.getLastModifiedBy()) > 0) {
						this.accessManagementDAO.addPref(AppConstants.PROFILE, preferences);
					}
				}

				pref = this.accessManagementDAO.getActivePrefByVal(AppConstants.TIMEZONE,
						preferences.getUserSignum());
				LOG.info("Timezone pref.size()-->:" + pref.size());
				if (pref == null || (pref != null && pref.size() < 1)) {
					this.accessManagementDAO.addPref(AppConstants.TIMEZONE, preferences);
				} else if (pref != null && pref.size() > 0
						&& !((String) pref.get(0)[1]).equalsIgnoreCase(preferences.getDefaultZoneValue())) {
					LOG.info("Existing Timezone-->:" + (String) pref.get(0)[1]);
					// Deleting the old timezone and adding new one:
					if (this.accessManagementDAO.deletePref((Integer) pref.get(0)[0],
							preferences.getLastModifiedBy()) > 0) {
						this.accessManagementDAO.addPref(AppConstants.TIMEZONE, preferences);
					}
				}

				pref = this.accessManagementDAO.getActivePrefByVal("page",
						preferences.getUserSignum());
				LOG.info("Page Name pref.size()-->:" + pref.size());
				if (pref == null || (pref != null && pref.size() < 1)) {
					this.accessManagementDAO.addPref("page", preferences);
				} else if (pref != null && pref.size() > 0
						&& !((String) pref.get(0)[1]).equalsIgnoreCase(preferences.getDefaultPageName())) {
					LOG.info("Existing Timezone-->:" + (String) pref.get(0)[1]);
					// Deleting the old timezone and adding new one:
					if (this.accessManagementDAO.deletePref((Integer) pref.get(0)[0],
							preferences.getLastModifiedBy()) > 0) {
						this.accessManagementDAO.addPref("page", preferences);
					}
				}
				LOG.info("Preferences added successfully");
				respMap.put("isApiSuccess", "true");
				respMap.put("msg", "preferences added successfully.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("Failed at addUserPreferences-->" + e.getMessage());
			respMap.put("isApiSuccess", AppConstants.FALSE);
			respMap.put("msg", "Error in adding preferences.");
		}
		return respMap;
	}

	@Transactional("txManager")
	public UserPreferencesModel getUserPreferences(String signum) {
		UserPreferencesModel pref = new UserPreferencesModel();
		try {
			//Boolean checkEmpTbl = this.accessManagementDAO.isSignumExistsEmp(signum);
			EmployeeModel employee = accessManagementDAO.getEmployeeGroupAndSignum(signum);
			if(employee.getSignum()!=null) {
			List<Object[]> prefList = this.accessManagementDAO.getActivePrefBySignum(signum);

//			String employeeGroup = accessManagementDAO.getEmployeeGroup(signum);
			
			//when prefList is empty
		    if(prefList.isEmpty()) {

					if (StringUtils.equalsIgnoreCase(employee.getEmployeeGroup(), USER_TYPE)) {
						pref.setDefaultProfileId(55);
						pref.setDefaultProfileValue(USER_TYPE);
						pref.setDefaultPageId(28);
						pref.setDefaultPageName(HOME_PAGE);
					} else {
						pref.setDefaultProfileId(39);
						pref.setDefaultProfileValue(NETWORK_ENGINEER);
						pref.setDefaultPageId(28);
						pref.setDefaultPageName(HOME_PAGE);						
					}
		        }
		    
			for (Object[] d : prefList) {
				if (d[0] != null && ((String) d[0]).equalsIgnoreCase(AppConstants.PROFILE)) {
					boolean  accessProfiles=this.accessManagementDAO.ifProfileExistswithsSignum(signum, (Integer) d[1]);
					if(!accessProfiles) {
						pref.setDefaultProfileId(39);
						pref.setDefaultProfileValue(NETWORK_ENGINEER);
					}
					else {
						pref.setDefaultProfileId((Integer) d[1]);
						pref.setDefaultProfileValue((String) d[2]);
					}
				} else if (d[0] != null && ((String) d[0]).equalsIgnoreCase(AppConstants.TIMEZONE)) {
					pref.setDefaultZoneId((Integer) d[1]);
					pref.setDefaultZoneValue((String) d[2]);
				} else if (d[0] != null && ((String) d[0]).equalsIgnoreCase(PAGE)) {
					pref.setDefaultPageId((Integer) d[1]);
					pref.setDefaultPageName((String) d[2]);
				}
			}
			
			//when page is empty
			 if(pref.getDefaultPageId()==null && StringUtils.isEmpty(pref.getDefaultPageName())) {
				pref.setDefaultPageId(28);
				pref.setDefaultPageName(HOME_PAGE);
			 }
			
			  //when profile is empty
			  if(pref.getDefaultProfileId()==null && StringUtils.isEmpty(pref.getDefaultProfileValue())) {
				 if (StringUtils.equalsIgnoreCase(employee.getEmployeeGroup(), USER_TYPE)) {
					pref.setDefaultProfileId(55);
					pref.setDefaultProfileValue(USER_TYPE);

				  } else {
					pref.setDefaultProfileId(39);
					pref.setDefaultProfileValue(NETWORK_ENGINEER);
					
				   }
				 }

		 }
			LOG.info("Getting User Preferences details successfully.");
		}
		catch (Exception e) {
			LOG.info("Failed at getUserPreferences {} ", e.getMessage());
		}
		return pref;
	}

	public static final String ERICSSON_EMPLOYEE = "ericsson";

	public ResponseEntity<Response<List<UserDetailsAccessModel>>> getUserSignumByEmail(UserDetailsAccessModel userDetailsAccessModel,
			String userToken) throws ServletException {
		Response<List<UserDetailsAccessModel>> response=new Response<>();
		List<UserDetailsAccessModel> userdata = null;
		try {
			if (configurations.getBooleanProperty(ConfigurationFilesConstants.APP_SECURITY_ENABLED, false)) {
				if (StringUtils.equalsIgnoreCase(userDetailsAccessModel.getEmployeeType(), ERICSSON_EMPLOYEE)) {
					String token = userToken.split(StringUtils.SPACE)[1].split(AppConstants.CHAR_HYPHEN)[0];
					if(StringUtils.isEmpty(token)) {
						throw new NullPointerException("Token is null!");
					}
						String empInfo = userToken.split(StringUtils.SPACE)[1].split(AppConstants.CHAR_HYPHEN)[1];
						empInfo = empInfo.substring(10, empInfo.length() - 10);
						String decryptString = decrypt(empInfo);
						List<String> ascii = Arrays.asList(decryptString.split(AppConstants.CSV_CHAR_PIPE));
						Collections.reverse(ascii);
						StringBuilder employeeEmail=new StringBuilder();
						for (String item : ascii) {
							char tmpPass = ASCIIToChar(Integer.parseInt(item)); 
							employeeEmail.append(tmpPass);
						}
						validateEmployeeEmailAndSignum(employeeEmail.toString());
						userdata=getUserdata(userDetailsAccessModel, token, userDetailsAccessModel.getEmployeeType());
				} else {
					userdata=getUserdata(userDetailsAccessModel, null, userDetailsAccessModel.getEmployeeType());
				}

			}else {
				// check for EmployeeType=Ericsson and app.security=false
				userdata=getUserdata(userDetailsAccessModel, StringUtils.EMPTY, userDetailsAccessModel.getEmployeeType());
			}
			response.setResponseData(userdata);
			
		}catch (ServletException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return  new ResponseEntity<>(response, HttpStatus.OK);
		}
		catch (Exception e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return  new ResponseEntity<>(response, HttpStatus.OK);
	}
	public boolean validateEmployeeEmailAndSignum(String employeeEmail) throws ServletException {
		boolean flag=false;
		if(this.accessManagementDAO.validateEmployeeEmailAndSignum(employeeEmail.trim())) {
			flag=true;
		}else {
			throw new ServletException("Email is not authorized in ISF!!"); 
		}
		return flag;
	}

	private List<UserDetailsAccessModel> getUserdata(UserDetailsAccessModel userDetailsAccessModel, String token, String employeeType){
		List<UserDetailsAccessModel> userdata=null;
		
		if(token==null || !StringUtils.equalsIgnoreCase(employeeType, ERICSSON_EMPLOYEE)) {
			userdata = new ArrayList<>();
			UserDetailsAccessModel asp = new UserDetailsAccessModel();
			asp.setSignumID(userDetailsAccessModel.getEmployeeUpn().split(AppConstants.CHAR_AT)[0]);
			userDetailsAccessModel.setSignumID(asp.getSignumID());
			asp.setEmployeeType(userDetailsAccessModel.getEmployeeType());
			asp.setAspStatus(this.checkIfAspRegistered(userDetailsAccessModel.getEmployeeEmailID()));
			userdata.add(asp);
			if (StringUtils.equalsIgnoreCase(NOT_REGISTERED, asp.getAspStatus()) 
					&& this.activityMasterDAO.getEmployeeBySignum(userDetailsAccessModel.getSignumID()) == null) {
					LOG.info(userDetailsAccessModel.getEmployeeEmailID());
					LOG.info(userDetailsAccessModel.getEmployeeName());
					LOG.info(userDetailsAccessModel.getSignumID());
					this.accessManagementDAO.insertUnregisteredAsp(userDetailsAccessModel);

			}
		}else {
			userdata = this.accessManagementDAO.getUserSignumByEmail(userDetailsAccessModel);
			if (CollectionUtils.isEmpty(userdata)) {
				userdata = this.accessManagementDAO.getUserSignumByEmailHRMS(userDetailsAccessModel);
				if (CollectionUtils.isNotEmpty(userdata)) {
					this.accessManagementDAO.saveUserDetailsHRMS(userdata.get(0).getSignumID());
					this.accessManagementDAO.saveUserAccessProfileHRMS(userdata.get(0).getSignumID());
				}
			}

		}

		accessManagementDAO.checkAndInsertUserAzureObjectDetails(userDetailsAccessModel,userdata.get(0).getSignumID());

		return userdata;
	}
	public String checkIfAspRegistered(String email) {
		String result = null;

		if (this.accessManagementDAO.checkIfAspRegistered(email)) {
			AspExplorerModel aspExplorerModel = this.aspManagement
					.getAspFlags(this.aspManagement.getAspSignumByEmail(email));
			if (aspExplorerModel.getIsProfileActive()) {
				result = APPROVAL_ACTION;
			} else {
				result = NOT_APPROVED;
			}
		} else {
			result = NOT_REGISTERED;
		}
		return result;
	}
    /**
     * @modifiedBy elkpain
     * @page Profile Access
     * 
     * @param role
     * @param signum
     * @return Map<String, Object>
     */
	public Map<String, Object> getAccessRequestsBySignum(String signum, String role, DataTableRequest dataTableReq) {
		
		Map<String, Object> response = new HashMap<>();
		List<AccessProfileRequestModel> accessProfileRequests = accessManagementDAO.getAccessRequestsBySignum(signum, role, dataTableReq);

		response.put(AppConstants.DATA_IN_RESPONSE, accessProfileRequests);
		response.put(AppConstants.DRAW,dataTableReq.getDraw());

		if(accessProfileRequests.size() != 0) {
			response.put(AppConstants.RECORD_TOTAL, accessProfileRequests.get(0).getTotalCounts());
			response.put(AppConstants.RECORD_FILTERED, accessProfileRequests.get(0).getTotalCounts());			
		} else {
			response.put(AppConstants.RECORD_TOTAL, 0);
			response.put(AppConstants.RECORD_FILTERED, 0);			
		}
		return response;
	}
	
	public static final String APPROVAL_MESSAGE = "Approved Successfully";
	public static final String APPROVAL_ACTION = "APPROVED";
	public static final String APPROVAL_ACTION_SINGLE = "SINGLEAPPROVED";
	public static final String REJECTION_MESSAGE = "Rejected Successfully";
	public static final String RENEWAL_MESSAGE = "Renewed Successfully";
	public static final String REVOKED_MESSAGE = "Revoked Successfully";
	public static final String RENEWAL_ACTION = "RENEWED";
	public static final String REJECTION_ACTION = "REJECTED";
	public static final String REJECTION_ACTION_SINGLE = "SINGLEREJECTED";
	public static final String REVOKED_ACTION = "REVOKED";
	
	public static final String REVOKE_ERROR_MESSAGE = "You don't have appropriate privileges to revoke this profile";

	private static final String NO_DATA_FOUND = "No Data Found!";
	


	@Transactional("transactionManager")
    public Response<Void> updateAccessRequestStatus(List<AccessRequestApprovalModel> accessRequestApprovalModelList,
                 String role) {
          Response<Void> response = new Response<>();

          if (CollectionUtils.isNotEmpty(accessRequestApprovalModelList)) {
                 for (AccessRequestApprovalModel accessRequestApprovalModel : accessRequestApprovalModelList) {
                       if (accessRequestApprovalModel.getApprovalStatus().equalsIgnoreCase(APPROVAL_ACTION)
                                    || accessRequestApprovalModel.getApprovalStatus().equalsIgnoreCase(APPROVAL_ACTION_SINGLE)) {
                              String checkCountResult = checkIfRequestCountExceedsLimit(
                                           accessRequestApprovalModel.getAccessProfileId(), accessRequestApprovalModel.getSignumId());
                              if (StringUtils.equalsIgnoreCase(checkCountResult, AppConstants.SUCCESS)) {
                                     accessRequestApprovalModel.setActive(true);
                                    boolean isSuccess = this.accessManagementDAO.approveAccessRequest(accessRequestApprovalModel,role);
                                    
                                    if (isSuccess) {
                                    this.accessManagementDAO.updateAccessRequestStatus(accessRequestApprovalModel);
                                           response.addFormMessage(APPROVAL_MESSAGE);
                                           if (accessRequestApprovalModel.getAccessProfileName()
                                                        .equalsIgnoreCase(AppConstants.BOT_DEVELOPER)) {
                                           createApiManagerUser(accessRequestApprovalModel.getSignumId());
                                           }
                                    accessRequestApprovalModel.setApprovalStatus(APPROVAL_ACTION);
                                           callMailer(role, accessRequestApprovalModel);
                                    } else {
                                           response.addFormWarning(checkCountResult);
                                           return response;
                                    }
                              }else {
                                     response.addFormWarning(checkCountResult);
                                    return response;
                              }
                       }else {
                              accessRequestApprovalModel.setActive(false);
                       accessRequestApprovalModel.setApprovalStatus(REJECTION_ACTION);
                              boolean isSuccess = this.accessManagementDAO
                                           .updateAccessRequestStatus(accessRequestApprovalModel);
                              if (isSuccess) {
                                     response.addFormMessage(REJECTION_MESSAGE);

                                    callMailer(role, accessRequestApprovalModel);
                              }
                       }
                 }
          }
          return response;
    }


	
		private String checkIfRequestCountExceedsLimit(String accessProfileId, String signumId) {
			return this.accessManagementDAO.checkIfRequestCountExceedsLimit(accessProfileId,signumId);
		}

	/**
	 * @author ekmbuma
	 * @purpose To send mail in a separate thread
	 * 
	 * @param role
	 * @param accessRequestApprovalModel
	 */
	private void callMailer(String role, AccessRequestApprovalModel accessRequestApprovalModel) {
		List<String> match=Arrays.asList(APPROVAL_ACTION, APPROVAL_ACTION_SINGLE);
		Runnable r =() -> {
				try {
				if(match.stream().anyMatch(accessRequestApprovalModel.getApprovalStatus()::equalsIgnoreCase)) {
					if (role.equals(AppConstants.ROLE_ISF_ACCESS_MANAGER) || role.equals(AppConstants.ADMIN)) {
						emailService.sendMail(AppConstants.ISF_ACCESS_REQUEST_APPROVE,
								enrichMailforProfileApproveReject(accessRequestApprovalModel, role));
					} else {
						emailService.sendMail(AppConstants.ISF_ACCESS_REQUEST_APPROVE,
								enrichMailforProfileApproveReject(accessRequestApprovalModel, null));
					}						
				} else {
					if (role.equals(AppConstants.ROLE_ISF_ACCESS_MANAGER) || role.equals(AppConstants.ADMIN)) {
						emailService.sendMail(AppConstants.ISF_ACCESS_REQUEST_REJECT,
								enrichMailforProfileApproveReject(accessRequestApprovalModel, role));
					}else {
					emailService.sendMail(AppConstants.ISF_ACCESS_REQUEST_REJECT,
							enrichMailforProfileApproveReject(accessRequestApprovalModel, null));
					}
				}
			} catch (Exception e) {
				LOG.debug(AppConstants.EXCEPTION, e.getMessage());
			}
		};
		new Thread(r).start();
	}

	public Map<String, Object> enrichMailforProfileApproveReject(AccessRequestApprovalModel req, String role) {
		EmployeeModel employee = activityMasterDAO.getEmployeeBySignum(req.getSignumId());
		EmployeeModel manager = activityMasterDAO.getEmployeeBySignum(req.getApprovedBy());
		String approvers = "";
		
		Map<String, Object> data = new HashMap<>();
		String accessProfileName = this.accessManagementDAO.getProfileNameById(Integer.parseInt(req.getAccessProfileId()));
		
		if (adminRoles.contains(accessProfileName.split(DASH)[0])) {
			List<String> accessAdmins = this.accessManagementDAO.getAllEmailforRole(role);
			approvers=accessAdmins.stream().collect(Collectors.joining(";"));
		} else {
			approvers = manager.getEmployeeEmailId();
			data.put(AppConstants.MANAGER_NAME, manager.getEmployeeName());
		}
	
		if (role != null) {
			data.put("isAdmin", "1");
		}
		data.put(AppConstants.REQUEST, req);
		data.put(AppConstants.MANAGER_NAME, manager.getEmployeeName());
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, employee.getEmployeeEmailId());
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, approvers);
		return data;
	}

	public Map<String, Object> validateUserPassword(String decodedUserName, String decodedPassword) {
		
		return this.accessManagementDAO.validateUserPassword(decodedUserName, decodedPassword);
	}

	public void insertTokenMappingDetails(String token, String type, int sourceID, Date activationDate,
			Date expirationDate, int active, String signum, String employeeEmailID) {
		this.accessManagementDAO.insertTokenMappingDetails(token, type, sourceID, activationDate, expirationDate,
				active, signum, employeeEmailID);
	}

	public void updateTokenMappingDetails(String token) {
		this.accessManagementDAO.updateTokenMappingDetails(token);
		accessManagementDAO.updateSourceTokenMappingDetails(token);
	}

	public Map<String, Object> getAndValidateSource(String userName, String token, String path) {
		
		return this.accessManagementDAO.getAndValidateSource(userName, token, path);
	}

	
	public List<AccessRequestApprovalModel> getRenewRequestsBySignum(String signum,String role) {
		return this.accessManagementDAO.getRenewRequestsBySignum(signum,role);
	}

	public Response<Void> updateAccessStatus(List<AccessRequestApprovalModel> accessRequestApprovalModelList) {
		Response<Void> response = new Response<>();

		if (CollectionUtils.isNotEmpty(accessRequestApprovalModelList)) {
			for (AccessRequestApprovalModel accessRequestApprovalModel : accessRequestApprovalModelList) {
				if (accessRequestApprovalModel.getApprovalStatus().equalsIgnoreCase(RENEWAL_ACTION)) {
					accessRequestApprovalModel.setActive(true);
					boolean isSuccess = this.accessManagementDAO.updateAccessStatus(accessRequestApprovalModel);
					if (isSuccess) {
						response.addFormMessage(RENEWAL_MESSAGE);
						
						callMailerForAccessStatus(AppConstants.ISF_ACCESS_RENEW,accessRequestApprovalModel);
					}
				} else {
					accessRequestApprovalModel.setActive(false);
					boolean isSuccess = this.accessManagementDAO.updateAccessStatus(accessRequestApprovalModel);
					if (isSuccess) {
						if(accessRequestApprovalModel.getAccessProfileName().equalsIgnoreCase("Bot Developer")) {
							revokeApiManagerUser(accessRequestApprovalModel.getSignumId());
						}
						response.addFormMessage(REVOKED_MESSAGE);
						
						callMailerForAccessStatus(AppConstants.ISF_ACCESS_REVOKE,accessRequestApprovalModel);

					}
				}
			}
		}
		return response;
	}

	/**
	 * @author ekmbuma
	 * @purpose send mail in a separate thread
	 * 
	 * @param templateId
	 * @param accessRequestApprovalModel
	 */
	private void callMailerForAccessStatus(String templateId, AccessRequestApprovalModel accessRequestApprovalModel) {
		Runnable r = () -> {
			try {
				emailService.sendMail(templateId,enrichMailforAccessRenewRevoke(accessRequestApprovalModel));
			} catch (Exception e) {
				LOG.debug(AppConstants.EXCEPTION, e.getMessage());
			}
		};
		new Thread(r).start();
	}

	public Map<String, Object> enrichMailforAccessRenewRevoke(AccessRequestApprovalModel req) {
		EmployeeModel employee = activityMasterDAO.getEmployeeBySignum(req.getSignumId());
		EmployeeModel manager = activityMasterDAO.getEmployeeBySignum(req.getApprovedBy());
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(AppConstants.REQUEST, req);
		data.put(AppConstants.MANAGER_NAME, manager.getEmployeeName());
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_TO, employee.getEmployeeEmailId());
		data.put(AppConstants.CUSTOM_NOTIFICATIONS_CC, manager.getEmployeeEmailId());
		return data;
	}

	public Map<String, Object> validateUiToken(String token) {
		
		return this.accessManagementDAO.validateUiToken(token);
	}

	public List<Map<String, Object>> getPageAccessDetailsByRoleOfSignum(String signumID, String role) {
		
		return this.accessManagementDAO.getPageAccessDetailsByRoleOfSignum(signumID, role);
	}

	public String getPathByName(String name) {
		List<GroupMenuModel> menus = accessManagementDAO.getAllPaths(name);
		String path = null;
		for (GroupMenuModel menu : menus) {
			if (menu.getSubMenuTitle() != null) {
				if (menu.getSubMenuTitle().equals(name)) {
					path = menu.getSubMenuHref();
					break;
				}
			} else {
				if (menu.getGroupTitle().equals(name)) {
					path = menu.getGroupHref();
					break;
				}
			}
		}

		return path;
	}

	public List<UserDetailsAccessModel> getUserSignumByEmailHRMS(UserDetailsAccessModel userDetailsAccessModel) {
		return this.accessManagementDAO.getUserSignumByEmailHRMS(userDetailsAccessModel);
	}


	

	public List<Map<String, Object>> getPageAccessDetailsByAccessProfileId(Integer accessProfileId) {

		return this.accessManagementDAO.getPageAccessDetailsByAccessProfileId(accessProfileId);
	}

	public HashMap<String, Object> getLoginData(UserDetailsAccessModel userDetailsAccessModel, String userToken)
			throws ServletException {
		HashMap<String, Object> loginData = new HashMap<String, Object>();
		// GetUserSignumByEmail API Data
		ResponseEntity<Response<List<UserDetailsAccessModel>>> response_userdata = this.getUserSignumByEmail(userDetailsAccessModel, userToken);
		List<UserDetailsAccessModel> userdata = response_userdata.getBody().getResponseData();
		// GetUserAccessProfile API Data
		Response<List<UserDetailsAccessModel>> response = this.getUserAccessProfile(userdata.get(0).getSignumID());
		// GetPageAccessDetailsByRoleOfSignum API Data
		Object pageAccessDetail = this.getPageAccessDetailsByRoleOfSignum(userdata.get(0).getSignumID(),
				response.getResponseData().get(0).getLstRoleModel().get(0).getRole());
		// GetUserMenu API Data
		List<CapabilityPageGroupModel> usermenu = this.getUserMenu();
		// Put all API data into single HashMap
		loginData.put("UserSignumByEmail", userdata);
		loginData.put("UserAccessProfile", response);
		loginData.put("PageAccessDetails", pageAccessDetail);
		loginData.put("UserMenu", usermenu);
		// Return HashMap loginData
		return loginData;
	}

	public void UpdateEmailFromLDAPHRMS() {
		List<String> empList = this.accessManagementDAO.getEmployeeWithNoEmailHRMS();
		for (String emp : empList) {
			try {
				List<CPMModel> empDetail = getUserInfoUsingLDAP(emp);
				if (empDetail != null) {
					this.accessManagementDAO.updateEmployeeEmailHRMS(empDetail.get(0));
					LOG.info("EmailID Updated For Signum : " + empDetail.get(0).getSignum());
				} else {
					LOG.info("Email Not Found in LDAP");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

	}

	public void insertTokenForExternalSource(JwtUser jwtUser, String token) {
		accessManagementDAO.insertTokenForExternalSource(jwtUser, token);

	}

	public boolean isRequestedAPIAllowedForSource(String requestedAPI, JwtUser jwtUser) {
		return accessManagementDAO.isRequestedAPIAllowedForSource(requestedAPI, jwtUser);
	}

	public void insertTokenMappingDetails(TokenMappingModel tokenMappingModel) {

		accessManagementDAO.insertTokenMappingDetails(tokenMappingModel);

	}

	public void insertHitCountForAPI(String requestedAPI, String token, JwtUser jwtUser) {
		accessManagementDAO.insertHitCountForAPI(requestedAPI, token, jwtUser);
	}

	public String getActiveToken(String externalSourceName) {
		if (StringUtils.isBlank(externalSourceName)) {
			return StringUtils.EMPTY;
		}
		try {
			String token = accessManagementDAO.getActiveToken(externalSourceName);
			if (StringUtils.isBlank(token)) {
				return StringUtils.EMPTY;
			} else {

				JwtValidator validator = new JwtValidator();
				if (validator.validate(token) == null) {
					return StringUtils.EMPTY;
				}
				return token;
			}
		} catch (Exception e) {
			return StringUtils.EMPTY;
		}
	}

	public List<UserDetailsAccessModel> getUserProfileBySignum(UserDetailsAccessModel userDetailsAccessModel) {
		
		return this.accessManagementDAO.getUserProfileBySignum(userDetailsAccessModel.getEmployeeEmailID());
	}

	public List<Map<String, Object>> getSubscribedApiList(String externalSourceName) {
		List<Map<String, Object>> allSubscribedAPIs=this.accessManagementDAO.getSubscribedApiList(externalSourceName);
		allSubscribedAPIs.removeIf(subscribedAPI->!(
				subscribedAPI.get(AppConstants.API_NAME).toString().contains("rpaController") 
				|| subscribedAPI.get(AppConstants.API_NAME).toString().contains("externalInterface")
				|| subscribedAPI.get(AppConstants.API_NAME).toString().contains("ivaManagement"))
				);
		
		return allSubscribedAPIs;
	}

	public void insertTokenApi(String token, JwtApiUser jwtApiUser, int requestedAPI) {
		Claims body = Jwts.parser().setSigningKey(AppConstants.SECRET).parseClaimsJws(token).getBody();

		Date oldCreationDate = body.getIssuedAt();
		Date oldExpirationDate = body.getExpiration();

		jwtApiUser.setLastModifiedBy(jwtApiUser.getOwnerSignum());

		TokenApiMappingModel tokenApiMappingModel = new TokenApiMappingModel.TokenApiBuilder()
				.setExternalRefID(jwtApiUser.getExternalRefID()).setSubscribedApiID(requestedAPI).setToken(token)
				.setActive(true).setCreatedOn(oldCreationDate).setExpirationDate(oldExpirationDate)
				.setOwnerSignum(jwtApiUser.getOwnerSignum()).setLastModifiedOn(new Date())
				.setLastModifiedBy(jwtApiUser.getLastModifiedBy()).build();

		accessManagementDAO.insertTokenApi(tokenApiMappingModel);
		long apiTokenAndInstanceId = isfCustomIdInsert.generateCustomId(tokenApiMappingModel.getApiTokenID());
		tokenApiMappingModel.setApiTokenID(apiTokenAndInstanceId);
	}

	public String getApiName(int requestedAPI) {
		return this.accessManagementDAO.getApiName(requestedAPI);
	}

	public String getActiveApiToken(int requestedAPI, JwtApiUser jwtApiUser) {
		String ownerSignum = jwtApiUser.getOwnerSignum();
		int externalRefID = jwtApiUser.getExternalRefID();
		if (requestedAPI == 0) {
			return "API NOT FOUND";
		}
		try {
			String token = accessManagementDAO.getActiveApiToken(requestedAPI, ownerSignum, externalRefID);
			if (StringUtils.isBlank(token)) {
				return StringUtils.EMPTY;
			} else {
				JwtValidator validator = new JwtValidator();
				if (validator.validateApiToken(token, jwtApiUser) == null) {
					return StringUtils.EMPTY;
				}
				return token;
			}
		} catch (Exception e) {
			return StringUtils.EMPTY;
		}
	}

	public String checkTokenForExternalRefId(JwtApiUser jwtApiUser) {
		String ownerSignum = jwtApiUser.getOwnerSignum();
		int externalRefID = jwtApiUser.getExternalRefID();

		try {
			String token = accessManagementDAO.checkTokenForExternalRefId(externalRefID, ownerSignum);
			LOG.info("token: {}", token);
			if (StringUtils.isBlank(token)) {
				return StringUtils.EMPTY;
			} else {
				JwtValidator validator = new JwtValidator();
				if (validator.validateApiToken(token, jwtApiUser) == null) {
					return StringUtils.EMPTY;
				}
				return token;
			}
		} catch (Exception e) {
			return StringUtils.EMPTY;
		}
	}

	public List<Map<String, Object>> getUserTokenList(String signum) {
		// get and set all the required data here..
		return this.accessManagementDAO.getUserTokenList(signum);
	}

	public boolean disableActiveToken(String externalSourceName, String signum) {
		return this.accessManagementDAO.disableActiveToken(externalSourceName, signum);
	}

	public ExternalSourceModel getExternalReferenceDetails(int externalRefId) {
		return this.accessManagementDAO.getExternalReferenceDetails(externalRefId);
	}

	public boolean testUpdateWOModifiedDate(String signum) {
		
		return this.accessManagementDAO.testUpdateWOModifiedDate(signum);
	}

	public Response<Void> saveUserProfileHistory(UserProfileHistoryModel userProfileHistoryModel) {
		Response<Void> response = new Response<>();
		if (StringUtils.isBlank(userProfileHistoryModel.getSignum())) {
			response.addFormError("Signum cannot be null/blank");
		} else {
			try {
				boolean isSuccess = this.accessManagementDAO.saveUserProfileHistory(userProfileHistoryModel);

				if (isSuccess) {
					response.addFormMessage("History saved Successfully");
				} else {
					response.addFormMessage("History not saved Successfully");
				}
			} catch (Exception e) {
				response.addFormError("Error while saving History - " + e.getMessage());
			}
		}
		return response;

	}

	public Response<Void> saveUserLocation(String Signum, UserLocationAddressModel userLocationAddressModel) {
		Response<Void> response = new Response<>();

		String contact = userLocationAddressModel.getContactNumber();
		String countryCode=userLocationAddressModel.getCountryCode();
		try {
			boolean flag=false;
			if (userLocationAddressModel.getCurrentLocationOptionID() != 1) {
				// for onsite domestic & international
				
				boolean isContactValid = true;
				boolean isCountryCodeValid = true;
				
	boolean checkEndDate=validateDate(userLocationAddressModel.getStartDate(), userLocationAddressModel.getEndDate());
	 if(!checkEndDate)
	 {
		 response.addFormError("end date cannot be before than start date or today's date.");
		 return response;
	 }
	 
			

				if (StringUtils.isNotEmpty(userLocationAddressModel.getContactNumber())) {
					isContactValid = validateContactNumber(contact);
					
				}
				if (StringUtils.isNotEmpty(userLocationAddressModel.getCountryCode())) {
					isCountryCodeValid = validateCountryCode(countryCode);
					
				}

				if(StringUtils.isNotEmpty(userLocationAddressModel.getContactNumber())
						||StringUtils.isNotEmpty(userLocationAddressModel.getCountryCode())) {
					if (!isContactValid) {
						response.addFormError("Contact number not valid");
						flag=true;
					}
					else if(StringUtils.isNotEmpty(userLocationAddressModel.getCountryCode())&&
							StringUtils.isEmpty(userLocationAddressModel.getContactNumber())&& isCountryCodeValid)
							{
						response.addFormError("Only Country Code not acceptable. Please provide Contact Number also!");
						flag=true;
							}
					if(!isCountryCodeValid) {
						response.addFormError("Country code cannot exceed 5 digits");
						flag=true;
					}
				}
				
				else {
					boolean isSuccess = this.accessManagementDAO.saveUserLocation(Signum, userLocationAddressModel);

					if (isSuccess) {
						response.addFormMessage(AppConstants.USER_LOCATION_SAVED);
						flag=true;
					} else {
						response.addFormMessage(AppConstants.USER_LOCATION_NOT_SAVED);
					}
				}

			} else {

				boolean isSuccess = this.accessManagementDAO.saveUserLocation(Signum, userLocationAddressModel);

				if (isSuccess) {
					response.addFormMessage(AppConstants.USER_LOCATION_SAVED);
					flag=true;
				} else {
					response.addFormMessage(AppConstants.USER_LOCATION_NOT_SAVED);
				}
			}
			
			if(!flag) {
				boolean isSuccess = this.accessManagementDAO.saveUserLocation(Signum, userLocationAddressModel);

				if (isSuccess) {
					response.addFormMessage(AppConstants.USER_LOCATION_SAVED);
				} else {
					response.addFormMessage(AppConstants.USER_LOCATION_NOT_SAVED);
				}
			}
			
			
		} catch (Exception e) {
			response.addFormError(e.getMessage());
		}
		
		return response;
	}

	private boolean validateDate(Date startDate, Date endDate) throws ParseException {
		
		String pattern = AppConstants.DATE_PATTERN;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Date todayDate = simpleDateFormat.parse(DateTimeUtil.convertDateToString(new Date(),AppConstants.DATE_PATTERN ));
		
	      if(startDate.compareTo(endDate)<=0 && endDate.compareTo(todayDate)>=0)
	      {
	    	  return true;
	    	  
	      }
	      
	      else
	    	  return false;
		
	}

	private static boolean validateCountryCode(String countryCode) {

		Pattern p = Pattern.compile("\\d{0,5}");

		Matcher m = p.matcher(countryCode);
		return (m.find() && m.group().equals(countryCode));

	}

	public static boolean validateContactNumber(String contactNumber) {

		Pattern p = Pattern.compile("\\d{10,15}");

		Matcher m = p.matcher(contactNumber);
		return (m.find() && m.group().equals(contactNumber));

	}

	public List<CityModel> getCityByCountryID(CityModel cityModel) {

		return this.accessManagementDAO.getCityByCountryID(cityModel);

	}

	public CityModel getUserLocationBySignum(String Signum) {

		boolean notficationFlag = notifyUserForDateValidity(Signum);

		CityModel cityModelObject = accessManagementDAO.getUserLocationBySignum(Signum);
		if (cityModelObject != null) {

			if (cityModelObject.getCurrentLocationOptionID() == 0
					|| cityModelObject.getCurrentLocationOptionID() == 1) {
				cityModelObject.setCurrentLocationOptionID(1);
				cityModelObject.setCurrentLocationOption("notonsite");
			}

			else {
				cityModelObject.setCurrentLocationOption("onsite");
			}

			cityModelObject.setNotificationFlag(notficationFlag);

		}

		return cityModelObject;

	}

	public ResponseEntity<Response<List<CapabilityPageGroupModel>>> getUserMenuByRole(String signumID, String role) {
		
		Response<List<CapabilityPageGroupModel>> response=new Response<List<CapabilityPageGroupModel>>();
		try {
			int roleID = 0;
			if (StringUtils.isNotBlank(role) && StringUtils.isNotBlank(signumID)) {
				roleID = accessManagementDAO.getRoleIDByRoleName(role);
			} else {
				roleID = accessManagementDAO.getRoleIDByAccessProfileId(signumID);			
			}
			response.setResponseData(accessManagementDAO.getUserMenuByRole(roleID));
		}
		catch (ApplicationException e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<List<CapabilityPageGroupModel>>>(response, HttpStatus.OK);
		}
		catch (Exception e) {
			LOG.error(e.getMessage());
			response.addFormError(e.getMessage());
			return new ResponseEntity<Response<List<CapabilityPageGroupModel>>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Response<List<CapabilityPageGroupModel>>>(response, HttpStatus.OK);
	}

	public List<AccessProfileModel> getUserAccessProfileBySignum(String signum) {
		return accessManagementDAO.getUserAccessProfileBySignum(signum);
	}

	@Transactional("txManager")
	public Response<List<UserDetailsAccessModel>> getAccessProfileOfUser(String signumID) {
		Response<List<UserDetailsAccessModel>> response = new Response<>();
		List<UserDetailsAccessModel> userData = this.accessManagementDAO.getAccessProfileOfUser(signumID);
	
		if (userData.size() > 0) {
				
				try {
					getUserImageFromDB(signumID, userData);
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			int accProfId = 0;
			String employeeGroup = userData.get(0).getEmployeeGroup();
		
			UserPreferencesModel userPreference = getUserPreferences(signumID);
			userData.get(0).setUserPreference(userPreference);
			accProfId=userPreference.getDefaultProfileId();

			
			List<AccessProfileModel> lstAccessProfileModel = accessManagementDAO
					.getUserAccessProfileBySignumAndAccProfID(signumID, accProfId);
			userData.get(0).setLstAccessProfileModel(lstAccessProfileModel);
     		response.setResponseData(userData);

		} else {
			response.addFormError("The signumID specified does not exists!!!");
		}
		return response;

	}

	private void getUserImageFromDB(String signumID, List<UserDetailsAccessModel> userData) {
		UserImageURIModel imgData = this.accessManagementDAO.getUserImageURIFromDB(signumID);
		if (imgData != null) {
			userData.get(0).setUserImageUri(imgData.getUserImage());
		} else {
			userData.get(0).setUserImageUri("");
		}
	}

	public List<LocationTypeModel> getLocationType() {

		return accessManagementDAO.getLocationType();
	}

	public List<LocationTypeModel> getAllLocationTypes() {

		return accessManagementDAO.getAllLocationTypes();
	}

	/**
	 * 
	 * @param signum
	 * @param locationTypeModel
	 * @return Response<Void>
	 */
	public Response<Void> addLocationType(String signum, LocationTypeModel locationTypeModel) {
		Response<Void> response = new Response<>();
		boolean isSuccess = false;
		try {
			if (locationTypeModel.getLocationTypeID() == 0) {
				if (!accessManagementDAO.checkDuplicateLocation(locationTypeModel)) {
					isSuccess = this.accessManagementDAO.addLocationType(signum, locationTypeModel);
					if (isSuccess) {
						response.addFormMessage(" location type added");
					} else {
						response.addFormMessage(" location type not added Successfully");
					}
				} else {
					response.addFormError("Duplicate Location type entry  : Location already Exists");
				}

			} else {

				if (accessManagementDAO.checkDuplicateLocation(locationTypeModel)) {
					throw new ApplicationException(200, "Duplicate Location type entry : Location already Exists ");
				}
				isSuccess = this.accessManagementDAO.editLocationType(signum, locationTypeModel);
				if (!isSuccess) {
					throw new ApplicationException(200, "location not edited Successfully");
				} 
				response.addFormMessage("location edited");
			}

		}
		catch (ApplicationException e) {
			response.addFormMessage(e.getMessage());
		}
		catch (Exception e) {
			response.addFormError("Error while adding/editing location - " + e.getMessage());
		}
		return response;
	}

	public Response<Void> changeLocationTypeStatus(String signum, LocationTypeModel locationTypeModel) {
		Response<Void> response = new Response<>();
		if (accessManagementDAO.isLocationTypeValidForChange(signum, locationTypeModel)) {
			response.addFormError("Not allowed to change the status to inactive for this location id");
		} else {
			try {
				accessManagementDAO.changeLocationTypeStatus(signum, locationTypeModel);
				response.addFormMessage("Status changed to Active/Inactive successfully");
			} catch (Exception e) {
				response.addFormError("Status not updated " + e.getMessage());
			}
		}

		return response;
	}

	public Response<Void> updateDefaultLocationType(String signum, LocationTypeModel locationTypeModel) {
		Response<Void> response = new Response<>();
		try {
			if (locationTypeModel.getPreviousDefaultID() != 0) {
				accessManagementDAO.updatePreviousDefaultLocationType(signum, locationTypeModel);
				accessManagementDAO.updateCurrentDefaultLocationType(signum, locationTypeModel);
			} else {
				accessManagementDAO.updateCurrentDefaultLocationType(signum, locationTypeModel);
			}

			response.addFormMessage("Status changed to Default Selected successfully");

		} catch (Exception e) {
			response.addFormError("Status not updated " + e.getMessage());
		}

		return response;
	}

	private boolean notifyUserForDateValidity(String signum) {
		boolean flag = false;
		Calendar calendar = Calendar.getInstance();
		Calendar calendar_day1 = Calendar.getInstance();
		Calendar calendar_day2 = Calendar.getInstance();
		calendar_day1.add(Calendar.DATE, +1);
		calendar_day2.add(Calendar.DATE, +2);
		SimpleDateFormat formatter = new SimpleDateFormat(AppConstants.DATE_PATTERN);
		String endDate = formatter.format(calendar.getTime());
		String endDate_day1 = formatter.format(calendar_day1.getTime());
		String endDate_day2 = formatter.format(calendar_day2.getTime());
		UserLocationAddressModel userAddressModelObject = accessManagementDAO.getLocationTypeModelData(signum);
		if (userAddressModelObject != null) {
			 Date date = userAddressModelObject.getEndDate();
			String userEndDate = formatter.format(date);
			if (date.before(calendar.getTime())|| userEndDate.equals(endDate) || userEndDate.equals(endDate_day1) || userEndDate.equals(endDate_day2)) {
				flag = true;
			}
		}

		return flag;
	}

	public Response<Boolean> getActiveSessionForUser(String signum) {
		Response<Boolean> response = new Response<>();
		try {
			
			response.setResponseData(accessManagementDAO.getActiveSessionForUser(signum));
		

		} catch (Exception e) {
			response.addFormMessage("Did not get any data " + e.getMessage());
		}

		return response;
	}

	public boolean validateReferer(String referer, String apiName) {
		return accessManagementDAO.validateReferer(referer, apiName);
		
	}
    /**
     * @author ekmbuma
     * 
     * @param signumID
     * @param roleID
     * @param organisationID
     * @param lineManagerSignum
     * @param request
     * @return DownloadTemplateModel
     * @throws ParseException 
     */
	public DownloadTemplateModel downloadViewAccessPageDataInExcel(String signumID, int roleID, int organisationID,
			String lineManagerSignum) throws ParseException {
		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();

		String fName = "ISF-View_Access_" + signumID + AppConstants.UNDERSCORE +(DateTimeUtil.convertDateToString(new Date(), AppConstants.SIMPLE_DATE_FORMAT))
				+ AppConstants.EXTENSION_XLSX;

		List<UserAccessProfileModel> accessList = accessManagementDAO.searchAccessDetailsByFilter(signumID, roleID,
				organisationID,lineManagerSignum,null);
		
		byte[] fData = null;
		if (accessList.size() != 0) {
			fData = generateXlsFile(accessList);
			
		}
		downloadTemplateModel.setpFileContent(fData);
		downloadTemplateModel.setpFileName(fName);

		return downloadTemplateModel;
	
	}
	public Response<Boolean> revokeApiManagerExpiredUsers() {
		Response<Boolean> response = new Response<>();
		List<ApiManagerUserModel> expiredUserDetails = accessManagementDAO.getExpiredUsersDetails();
		List<String> revokedSignums=new LinkedList<>();

		for (ApiManagerUserModel apiManagerUserModel : expiredUserDetails) {
			try {
				apiManagerUserModel.setState("BLOCKED");
				apiManagerUserModel.getIdentities().get(0).setProvider("Aad");
				apiManagerUserModel.setAppType(AppConstants.DEVELOPER_PORTAL);

				ApiManagerService apiManagerService = apiManagerServiceBuilder.getApiManagerService();
				retrofit2.Response<ApiManagerResponseModel> apiManagerResponse;
				apiManagerResponse = apiManagerService.users(apiManagerUserModel.getIdentities().get(0).getId(),
						AppConstants.AZURE_API_VERSION, null, new ApiManagerRequestModel(apiManagerUserModel))
						.execute();
				if (apiManagerResponse.isSuccessful()) {
					LOG.info(String.format(SUCCESS_APIM_REVOKE, apiManagerResponse.body().toString()));
					revokedSignums.add(apiManagerUserModel.getSignum());
					
				} else {
					LOG.info(String.format(FAILURE_APIM_REVOKE, IOUtils
							.toString(apiManagerResponse.errorBody().byteStream(), StandardCharsets.UTF_8.name())));
				}
			} catch (IOException ie) {
				LOG.error(String.format(APIM_CONNECTION_PROBLEM, ie.getMessage()));
				ie.printStackTrace();
			}

		}
		if(!revokedSignums.isEmpty())
			accessManagementDAO.updateRevokedUsers(revokedSignums);
		
		
		response.setResponseData(true);

		return response;
	}


	/**
	 * @author ekmbuma
	 * 
	 * @param result
	 * @return byte[]
	 * @throws ParseException
	 */
	private byte[] generateXlsFile(List<UserAccessProfileModel> result) throws ParseException {

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			Row row ;
			int col = 0;
			
	        // create header
	        Row header = sheet.createRow(0);
	        header.createCell(0).setCellValue("Signum");
	        header.createCell(1).setCellValue("Access Profile Name");
	        header.createCell(2).setCellValue("Role");
	        header.createCell(3).setCellValue("Access Organisation");
	        header.createCell(4).setCellValue("Line Manager");
	        header.createCell(5).setCellValue("Access Expiry");
	        header.createCell(6).setCellValue("Approved BY");
	        
	        // write data
			for (int i = 1; i <= result.size(); i++) {
				col = 0;
				row = sheet.createRow(i);
				
				Object[] objects = {
						result.get(i-1).getSignumID(),
						result.get(i-1).getAccessProfileName(),
						result.get(i-1).getRole(),
						result.get(i-1).getOrganisationName(),
						result.get(i-1).getManagerSignum(),
						result.get(i-1).getEndDate(),
						result.get(i-1).getApprovedBy()};
				
				for(Object obj : objects) {
					Cell cell = row.createCell(col++);
                    if (obj instanceof String) {
                    	cell.setCellValue((String) obj);
                    } else if (obj instanceof Double){
                    	cell.setCellValue((Double) obj);
                    } else if (obj instanceof Date) {
                    	// change format of Date to 2020-06-14
                    	cell.setCellValue(DateTimeUtil.convertDateToString((Date)obj, AppConstants.DEFAULT_DATE_FORMAT));
                    }
				}
			}

			workbook.write(baos);

		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] xls = baos.toByteArray();

		return xls;
	}
	

	/**
	 * 
	 * @param signum
	 * @param role
	 * @return DownloadTemplateModel
	 * @throws ParseException
	 */
	public DownloadTemplateModel downloadProfileAccessPageDataInExcel(String signum, String role) throws ParseException {
		DownloadTemplateModel downloadTemplateModel = new DownloadTemplateModel();

		String fName = "ISF-Profile_Approve_Reject_" + signum + AppConstants.UNDERSCORE + (DateTimeUtil.convertDateToString(new Date(), AppConstants.SIMPLE_DATE_FORMAT))
				+ AppConstants.EXTENSION_XLSX;

		List<AccessProfileRequestModel> accessList = accessManagementDAO.getAccessRequestsBySignum(signum, role,null);
		
		byte[] fData = null;
		if (accessList.size() != 0) {
			fData = generateXLSAccessProfileFile(accessList);
			
		}
		
		downloadTemplateModel.setpFileContent(fData);
		downloadTemplateModel.setpFileName(fName);
		
		return downloadTemplateModel;
	}

	/**
	 * 
	 * @param result
	 * @return byte[]
	 * @throws ParseException
	 */
	private byte[] generateXLSAccessProfileFile(List<AccessProfileRequestModel> result) throws ParseException {

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			Row row;
			int col = 0;
			
	        // create header
	        Row header = sheet.createRow(0);
	        header.createCell(0).setCellValue("Signum/EmployeeName");
	        header.createCell(1).setCellValue(AppConstants.PROFILE);
	        header.createCell(2).setCellValue("Organization");
	        header.createCell(3).setCellValue("Requested Date");
	       
	        
	        // write data
			for (int i = 1; i <= result.size(); i++) {
				col = 0;
				row = sheet.createRow(i);
				
				Object[] objects = {
						result.get(i-1).getSignum() + "/" + result.get(i-1).getEmployeeName(),
						result.get(i-1).getAccessProfileName(),
						result.get(i-1).getOrganisation(),
						result.get(i-1).getCreatedOn()};
				
				for(Object obj : objects) {
					Cell cell = row.createCell(col++);
                    if (obj instanceof String) {
                    	cell.setCellValue((String) obj);
                    } else if (obj instanceof Double){
                    	cell.setCellValue((Double) obj);
                    } else if (obj instanceof Date) {
                    	cell.setCellValue(DateTimeUtil.convertDateToString((Date)obj, AppConstants.DEFAULT_DATE_FORMAT));
                    }
				}
			}

			workbook.write(baos);

		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] xls = baos.toByteArray();

		return xls;
	}


	public Response<List<DynamicMessageModel>> getMessageTable() {
		Response<List<DynamicMessageModel>> response =  new Response<>();
		
		try {
			response.setResponseData(accessManagementDAO.getMessageTable());
		}
		catch (ApplicationException e) {
			LOG.info(e.getMessage());
			response.addFormError(e.getMessage());
		} catch (Exception e1) {
			LOG.info(AppConstants.EXCEPTION,e1.getMessage());
		}
		return response;
	}

	public Response<Void> insertDownStatus(String signum, DynamicMessageModel dynamicMessageModel) {
		Response<Void> response = new Response<>();
		try {
			validationUtilityService.validateMessageField(dynamicMessageModel);
			
			if (dynamicMessageModel.getId() == 0) {
				if (accessManagementDAO.checkDuplicateMessage(dynamicMessageModel)) {
					throw new ApplicationException(200, DUPLICATE_MESSAGE);
				} 
					if(dynamicMessageModel.isDown()) {
						accessManagementDAO.disablePreviousEnabledMessage(signum, dynamicMessageModel);
					}
					this.accessManagementDAO.insertDownStatus(signum, dynamicMessageModel);
					response.addFormMessage(MESSAGE_ADDED_SUCCESSFULLY);

			} else {
				if (accessManagementDAO.checkDuplicateMessageForEdit(dynamicMessageModel)) {
					throw new ApplicationException(200, DUPLICATE_MESSAGE);
				}
				if(dynamicMessageModel.isDown()) {
					accessManagementDAO.disablePreviousEnabledMessage(signum, dynamicMessageModel);
				}
					this.accessManagementDAO.updateDownStatus(signum, dynamicMessageModel);
					response.addFormMessage(MESSAGE_UPDATED_SUCCESSFULLY);

			}

		}
		catch (ApplicationException e) {
			LOG.info(e.getMessage());
			response.addFormError(e.getMessage());
		}
		catch (Exception e) {
			response.addFormError(e.getMessage());
			LOG.info(AppConstants.EXCEPTION,e.getMessage());
		}
		return response;
	}

	public Response<Void> enableMessage(String signum, DynamicMessageModel dynamicMessageModel) {
		Response<Void> response = new Response<>();
		try {
			if (dynamicMessageModel.getId()==0) {
				response.addFormError(ID_CANNOT_BE_ZERO);
			} 
			else {
			if(dynamicMessageModel.isDown()==true) {
				accessManagementDAO.disableCurrentMessage(signum, dynamicMessageModel);
				response.addFormMessage(MESSAGE_DISABLED);
			}
			else {
				accessManagementDAO.enableCurrentMessage(signum, dynamicMessageModel);
				response.addFormMessage(MESSAGE_ENABLED);
			}
			}
		} catch (Exception e) {
			response.addFormError(MESSAGE_NOT_ENABLED);
		}
		return response;
	}

	public Response<List<DynamicMessageModel>> getEnabledMessage() {
     Response<List<DynamicMessageModel>> response =  new Response<>();
		
		try {
			List<DynamicMessageModel> enabledMessage = accessManagementDAO.getEnabledMessage();
			if (CollectionUtils.isEmpty(enabledMessage)) {
				response.addFormMessage(AppConstants.NO_DETAILS_EXISTS);
			} else {
				response.setResponseData(enabledMessage);
			}
		}
		catch (ApplicationException e) {
			LOG.info(e.getMessage());
			response.addFormError(e.getMessage());
		} catch (Exception e1) {
			LOG.info(AppConstants.EXCEPTION,e1.getMessage());
		}
		return response;
		
	}
	
	public ResponseEntity<Response<String>> generateCustomAPIToken(HttpServletRequest request2,
			SessionDataStoreModel sessionDataModel) {
		Response<String> result = new Response<>();
		try {
			LOG.info("start execution of generateCustomAPIToken Api");
			String signum=validateVAlidUserForGenerateToken(sessionDataModel);
			sessionDataModel.setSignum(signum);
			Map<String,String> map = createSession( sessionDataModel,request2);
			String sessionId=map.get(SESSION_ID);
			String sessionToken=map.get(SESSION_TOKEN);
			sessionDataModel.setSessionId(sessionId);
			sessionDataModel.setSessionToken("Session "+sessionToken);
			ValueOperations<Object, Object> ops = this.redisTemplate.opsForValue();
			if (!Boolean.TRUE.equals(this.redisTemplate.hasKey(sessionId))) {
				ops.set(sessionId, sessionDataModel);
}
			LOG.info("end execution of generateCustomAPIToken Api");
			result.setResponseData(sessionToken);
		} catch (ApplicationException exe) {
			result.addFormError(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	private String validateVAlidUserForGenerateToken(SessionDataStoreModel sessionDataModel) {
		validationUtilityService.validateGenerateCustomAPIToken(sessionDataModel, "");
		String signum = "";
		if (StringUtils.equalsIgnoreCase(sessionDataModel.getEmployeeType(), ERICSSON_EMPLOYEE)) {
			signum = accessManagementDAO.getUserSignum(sessionDataModel.getEmailID(),
					StringUtils.lowerCase(sessionDataModel.getEmployeeType()));
		}
		if (StringUtils.equalsIgnoreCase(sessionDataModel.getEmployeeType(), USER_TYPE)) {
			signum = accessManagementDAO.getUserSignum(sessionDataModel.getEmailID(),
					StringUtils.lowerCase(sessionDataModel.getEmployeeType()));
		}
		if (StringUtils.isBlank(signum)) {

			throw new ApplicationException(200, EMAIL_DOES_NOT_EXIST);
		}
		sessionDataModel.setSignum(signum);
		return signum;
	}



	public Map<String,String> createSession(SessionDataStoreModel sessionDataModel, HttpServletRequest request) throws UnsupportedEncodingException {
		Map<String,String> map=new HashMap<>();
		HttpSession newSession = request.getSession();
		StringBuilder stringBuilder= new StringBuilder();
		Base64.Encoder encoder = Base64.getUrlEncoder();  
		
		String asciiSignum=StringUtils.reverse(sessionDataModel.getSignum())
				 .chars()
				 .mapToObj(Integer::toString)
				 .collect(Collectors.joining("|"));
		
		String asciiMail=StringUtils.reverse(sessionDataModel.getEmailID())
				 .chars()
				 .mapToObj(Integer::toString)
				 .collect(Collectors.joining("|"));
		
		System.out.println(new String(encoder.encode(asciiSignum.getBytes("UTF-8"))));
		
		stringBuilder.append(newSession.getId())
					 .append("_")
					 .append(new String(encoder.encode(asciiSignum.getBytes("UTF-8"))));
		String sessionID=stringBuilder.toString();
		
		stringBuilder.append("-")
					 .append(RandomPassword())
					 .append(new String(encoder.encode(asciiMail.getBytes("UTF-8"))))
					 .append(RandomPassword());
		
		map.put(SESSION_ID, sessionID);
		map.put(SESSION_TOKEN, stringBuilder.toString());
		return map;
	}
	
	
	 public static String RandomPassword()
     {
		 Random random = new Random();
         StringBuilder builder = new StringBuilder();
         builder.append(RandomString(4, true));
         builder.append(random.nextInt(8999)+1000);
         builder.append(RandomString(2, false));
         return builder.toString();
     }
	 
	 public static String RandomString(int size, boolean lowerCase)
     {
         StringBuilder builder = new StringBuilder();
         Random random = new Random();
         char ch;
         for (int i = 0; i < size; i++)
         {
             ch = (char)((int)(Math.floor(26 * random.nextDouble() + 65)));
             builder.append(ch);
         }
         if (lowerCase)
             return builder.toString().toLowerCase();
         return builder.toString();
     }
	 
	 
	 public ResponseEntity<Response<Object>> getSessionId(String sessionId) {
			Response<Object> result = new Response<>();
			try {
				LOG.info("start execution of getSessionId Api");
				validationUtilityService.validateStringForBlank(sessionId, SESSION_ID);
				ValueOperations<Object, Object> ops = this.redisTemplate.opsForValue();
				Object userData = ops.get(sessionId);
				if (userData == null) {
					result.setResponseData(NO_DATA_FOUND);
				} else {
					result.setResponseData(userData);
				}
				LOG.info("end execution of getSessionId Api");
			} catch (ApplicationException exe) {
				result.addFormError(exe.getMessage());
				return new ResponseEntity<>(result, HttpStatus.OK);
			} catch (Exception ex) {
				result.addFormError(ex.getMessage());
				return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			return new ResponseEntity<>(result, HttpStatus.OK);

		}

	@Transactional("transactionManager")
	public ResponseEntity<Response<String>> destroySession(SessionDataStoreModel sessionDataModel) {
		Response<String> result = new Response<>();
		try {
			LOG.info("start execution of destroySession Api");
			validateDestroySession(sessionDataModel);
			UserLoginModel userLogin = getLoginDetailsModel(sessionDataModel.getSessionId(),sessionDataModel.getLogID());
			saveLoginDetails(userLogin);
			redisTemplate.delete(sessionDataModel.getSessionId());
			result.setResponseData(SESSION_ID_DELETED_SUCCESSFULLY);
			LOG.info("end execution of destroySession Api");
		} catch (ApplicationException exe) {
			result.addFormError(exe.getMessage());
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception ex) {
			result.addFormError(ex.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	private void validateDestroySession(SessionDataStoreModel sessionDataModel) {
		validationUtilityService.validateStringForBlank(sessionDataModel.getSessionId(), SESSION_ID);
		validationUtilityService.validateIntegerForNull(sessionDataModel.getLogID(), AppConstants.LOGID);
	}

		private UserLoginModel getLoginDetailsModel(String sessionId,int logId) {
			ValueOperations<Object, Object> ops = this.redisTemplate.opsForValue();
			Object userData =  ops.get(sessionId);
			Gson gson= new Gson();
			SessionDataStoreModel obj = gson.fromJson(gson.toJson(userData),SessionDataStoreModel.class);
			UserLoginModel userLogin=new UserLoginModel();
			userLogin.setSessionId(sessionId);
			userLogin.setBrowser(obj.getBrowser());
			userLogin.setDevice(obj.getSourceName());
			userLogin.setEmailID(obj.getEmailID());
			userLogin.setIpAddress(obj.getIpAddress());
			userLogin.setSignumID(obj.getSignum());
			userLogin.setSourceDomain(obj.getSourceDomain());
			userLogin.setStatus(AppConstants.LOGOUT);
			userLogin.setSessionToken(obj.getSessionToken());
			userLogin.setLogID(logId);
			return userLogin;
		}
		
		public Response<UserDetailsAccessModel> getUserDetails(String signum) {
			Response<UserDetailsAccessModel> response = new Response<>();			
			
			try {
				UserDetailsAccessModel userData = this.accessManagementDAO.getUserDetails(signum);

				if (userData!=null && !userData.getLstAccessProfileModel().isEmpty()) {
					response.setResponseData(userData);
				} else {
					boolean checkEmp = this.accessManagementDAO.IsSignumExistsHRMS(signum);
					
					if(!checkEmp) {
						throw new ApplicationException(200, AppConstants.SIGNUM_NOT_FOUND);
					}
					
					if (userData==null) {
							this.accessManagementDAO.saveUserDetailsHRMS(signum);
					}
					this.accessManagementDAO.saveUserAccessProfileHRMS(signum);
						//// For Email Using LDAP
					getEmailFromLDAP(signum);
					response.setResponseData(this.accessManagementDAO.getUserDetails(signum));
				}
			}
			catch (ApplicationException e) {
				response.addFormError(AppConstants.SIGNUM_NOT_FOUND);
				LOG.info( e.getMessage());
			}
			catch (Exception e) {
				LOG.info(AppConstants.EXCEPTION, e);
				response.addFormError(e.getMessage());
			}
			return response;
		}

		public ResponseEntity<Response<Void>> insertDemandType(DemandTypeModel demandTypeModel) {
			Response<Void> response = new Response<>();
			try {
				LOG.info("insertDemandType : START");
				prepareDemandTypeDetails(demandTypeModel);
				validateDemandTypeDetails(demandTypeModel);
				demandTypeModel.setCreatedBy(StringUtils.upperCase(demandTypeModel.getCreatedBy()));
				accessManagementDAO.insertDemandType(demandTypeModel.getDemandType(), demandTypeModel.getCreatedBy(),
						demandTypeModel.getDemandTypeDescription());
				response.addFormMessage("DemandType Inserted");
				LOG.info("insertDemandType : END");
			}

			catch (ApplicationException exe) {
				response.addFormError(exe.getMessage());
				return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);
			} catch (Exception e) {
				response.addFormError(e.getMessage());
				return new ResponseEntity<>(response, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);

		}

		private void validateDemandTypeDetails(DemandTypeModel demandTypeModel) {

			validateAllMandatoryFieldPresent(demandTypeModel);

			validateDemandType(demandTypeModel.getDemandType());

			validateCreatedBy(demandTypeModel.getCreatedBy());

			validateDemandTypeDescription(demandTypeModel.getDemandTypeDescription());

		}
		
		private void validateDemandTypeDescription(String demandTypeDescription) {
			if (StringUtils.isNotEmpty(demandTypeDescription)) {
				if (demandTypeDescription.length() > 250) {
					throw new ApplicationException(500, DEMAND_TYPE_DESCRIPTION_CAN_T_HAVE_MORE_THAN_250_CHARACTERS);
				}

				validateNotAllowedSpecialCharacter(demandTypeDescription);

			} else {
				throw new ApplicationException(500, PLEASE_PROVIDE_DEMAND_TYPE_DESCRIPTION);
			}

		}

		private void validateAllMandatoryFieldPresent(DemandTypeModel demandTypeModel) {
			if (StringUtils.isEmpty(demandTypeModel.getDemandType())
					&& StringUtils.isEmpty(demandTypeModel.getCreatedBy())
					&& StringUtils.isNotEmpty(demandTypeModel.getDemandTypeDescription())) {
				throw new ApplicationException(500, PLEASE_PROVIDE_DEMAND_TYPE_AND_CREATED_BY);
			}
			if (StringUtils.isEmpty(demandTypeModel.getDemandType())
					&& StringUtils.isNotEmpty(demandTypeModel.getCreatedBy())
					&& StringUtils.isEmpty(demandTypeModel.getDemandTypeDescription())) {
				throw new ApplicationException(500, PLEASE_PROVIDE_DEMAND_TYPE_AND_DEMAND_TYPE_DESCRIPTION);
			}
			if (StringUtils.isNotEmpty(demandTypeModel.getDemandType())
					&& StringUtils.isEmpty(demandTypeModel.getCreatedBy())
					&& StringUtils.isEmpty(demandTypeModel.getDemandTypeDescription())) {
				throw new ApplicationException(500, PLEASE_PROVIDE_CREATED_BY_AND_DEMAND_TYPE_DESCRIPTION);
			}

		}

		private void validateCreatedBy(String createdBy) {
			if (StringUtils.isNotEmpty(createdBy)) {
				boolean checkEmpTbl = this.accessManagementDAO.isSignumExistsEmpAndNotResigned(createdBy);

				if (!checkEmpTbl) {
					throw new ApplicationException(500, PLEASE_PROVIDE_VALID_CREATED_BY);
				}

			} else {
				throw new ApplicationException(500, PLEASE_PROVIDE_CREATED_BY);
			}
		}

		private void validateDemandType(String demandType) {
			if (StringUtils.isNotEmpty(demandType)) {
				if (demandType.length() > 100) {
					throw new ApplicationException(500, DEMAND_TYPE_CAN_HAVE_MAXIMUM_100_CHARACTERS);
				}

				if (validateContainSpecialCharacter(demandType)) {
					throw new ApplicationException(500, DEMAND_TYPE_CAN_T_HAVE_ANY_SPECIAL_CHARACTERS);
				}

				boolean check = accessManagementDAO.isSameDemandTypeAlreadyExist(demandType);
				if (check) {
					throw new ApplicationException(500, DEMAND_TYPE_ALREADY_EXISTS);
				}

			} else {
				throw new ApplicationException(500, PLEASE_PROVIDE_DEMAND_TYPE);
			}

		}

		private boolean validateContainSpecialCharacter(String demandType) {
			Pattern p = Pattern.compile("[^A-Za-z0-9 ]");
			Matcher m = p.matcher(demandType);
			return m.find();
		}
		
		private void prepareDemandTypeDetails(DemandTypeModel demandTypeModel) {
			demandTypeModel.setDemandType(StringUtils.trim(demandTypeModel.getDemandType()));
			demandTypeModel.setLastModifyBy(StringUtils.trim(demandTypeModel.getLastModifyBy()));
			demandTypeModel.setCreatedBy(StringUtils.trim(demandTypeModel.getCreatedBy()));
			demandTypeModel.setDemandTypeDescription(StringUtils.trim(demandTypeModel.getDemandTypeDescription()));
		}

		public ResponseEntity<Response<Void>> updateDemandType(DemandTypeModel demandTypeModel) {

			Response<Void> response = new Response<>();
			try {
				LOG.info("updateDemandType : START");
				prepareDemandTypeDetails(demandTypeModel);
				if (StringUtils.isNotEmpty(demandTypeModel.getLastModifyBy())) {
					demandTypeModel.setLastModifyBy(StringUtils.upperCase(demandTypeModel.getLastModifyBy()));
					validateDemandTypeId(demandTypeModel.getDemandTypeId());

					boolean checkEmpTbl = this.accessManagementDAO
							.isSignumExistsEmpAndNotResigned(demandTypeModel.getLastModifyBy());

					if (checkEmpTbl) {
						if (demandTypeModel.getInactivateDemandType()) {
							accessManagementDAO.inactiveDemandType(demandTypeModel.getDemandTypeId(),
									demandTypeModel.getLastModifyBy());
							response.addFormMessage(DEMAND_TYPE_INACTIVE);
						} else {
							validateDemandTypeDetailsPresent(demandTypeModel);
							validateDemandTypeDetailsForUpdate(demandTypeModel);
							accessManagementDAO.updateDemandType(demandTypeModel.getDemandTypeId(),
									demandTypeModel.getDemandType(), demandTypeModel.getDemandTypeDescription(),
									demandTypeModel.getLastModifyBy());
							response.addFormMessage(DEMAND_TYPE_UPDATED);
						}
					} else {
						throw new ApplicationException(200, PLEASE_PROVIDE_VALID_LAST_MODIFIEDBY);
					}
				} else {
					throw new ApplicationException(200, PLEASE_PROVIDE_LAST_MODIFIEDBY);
				}
				LOG.info("updateDemandType : END");
			} catch (ApplicationException exe) {
				response.addFormError(exe.getMessage());
				return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);
			} catch (Exception e) {
				response.addFormError(e.getMessage());
				return new ResponseEntity<>(response, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<>(response, org.springframework.http.HttpStatus.OK);

		}

		private void validateDemandTypeDetailsPresent(DemandTypeModel demandTypeModel) {
			if (StringUtils.isNotEmpty(demandTypeModel.getDemandType())
					&& StringUtils.isEmpty(demandTypeModel.getDemandTypeDescription())) {
				throw new ApplicationException(500, PLEASE_PROVIDE_DEMAND_TYPE_DESCRIPTION);
			}
			if (StringUtils.isEmpty(demandTypeModel.getDemandType())
					&& StringUtils.isNotEmpty(demandTypeModel.getDemandTypeDescription())) {
				throw new ApplicationException(500, PLEASE_PROVIDE_DEMAND_TYPE);
			}

		}

		private void validateDemandTypeDetailsForUpdate(DemandTypeModel demandTypeModel) {
			if (StringUtils.isNotEmpty(demandTypeModel.getDemandType())
					&& StringUtils.isNotEmpty(demandTypeModel.getDemandTypeDescription())) {
				if (demandTypeModel.getDemandType().length() > 100) {
					throw new ApplicationException(500, DEMAND_TYPE_CAN_HAVE_MAXIMUM_100_CHARACTERS);
				}
				if (validateContainSpecialCharacter(demandTypeModel.getDemandType())) {
					throw new ApplicationException(500, DEMAND_TYPE_CAN_T_HAVE_ANY_SPECIAL_CHARACTERS);
				}
				if (demandTypeModel.getDemandTypeDescription().length() > 250) {
					throw new ApplicationException(500, DEMAND_TYPE_DESCRIPTION_CAN_T_HAVE_MORE_THAN_250_CHARACTERS);
				}

				validateNotAllowedSpecialCharacter(demandTypeModel.getDemandTypeDescription());

				String PreviousOneDemandType = accessManagementDAO
						.getPreviousOneDemandType(demandTypeModel.getDemandTypeId());

				boolean check = false;

				if (demandTypeModel.getDemandType() != PreviousOneDemandType) {
					check = accessManagementDAO.isSameDemandTypeAlreadyExistOtherThanThePresentOne(
							demandTypeModel.getDemandTypeId(), demandTypeModel.getDemandType());
				}

				if (check) {
					throw new ApplicationException(500, DEMAND_TYPE_ALREADY_EXISTS);
				}
			}

			else {

				throw new ApplicationException(500,
						PLEASE_PROVIDE_DEMAND_TYPE_AND_DEMAND_TYPE_DESCRIPTION_PARAMETER_FOR_UPDATE);

			}

		}
    
		private void validateNotAllowedSpecialCharacter(String demandTypeDescription) {
			String specialChars = "\\#^%?;|/";
			boolean check = false;
			// Use for loop to check special characters
			for (int i = 0; i < specialChars.length(); i++) {
				String strChar = Character.toString(specialChars.charAt(i));
				// Check whether String contains special character or not
				if (demandTypeDescription.contains(strChar)) {
					check = true;
					LOG.info("----------------------Special character found-------------");
					break;
				}
			}
			if (check) {
				throw new ApplicationException(500, DEMAND_TYPE_DESCRIPTION_CAN_HAVE_ONLY_THESE_$_SPECIAL_CHARACTERS);
			}

		}

		private void validateDemandTypeId(int demandTypeId) {
			if (demandTypeId == 0) {
				throw new ApplicationException(500, PLEASE_PROVIDE_DEMAND_TYPE_ID);
			}

			boolean check = accessManagementDAO.isValidDemandTypeId(demandTypeId);

			if (!check) {
				throw new ApplicationException(500, PLEASE_PROVIDE_VALID_DEMAND_TYPE_ID);
			}

		}
		public Response<List<DemandTypeModel>> getDemandType() {
			Response<List<DemandTypeModel>> response = new Response<>();
			try {
				List<DemandTypeModel> demandTypes = this.accessManagementDAO.getDemandType(); 
				if(demandTypes!=null) {
					response.setResponseData(demandTypes);
				}
				else {
					throw new ApplicationException(200,"Demand Types not present");
				}
					
			}
			catch (Exception e) {
				LOG.info(AppConstants.EXCEPTION,e);
				response.addFormError(e.getMessage());
			}
			return response;
		}

		
		
}
