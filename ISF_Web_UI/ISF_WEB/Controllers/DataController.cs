using ISF.AzureRedisCache;
using ISF_WEB.Filters;
using ISF_WEB.Models;
using ISF_WEB.Sessions;
using ISF_WEB.Util;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using System;
using System.Collections.Generic;
using System.Dynamic;
using System.IO;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;

namespace ISF_WEB.Controllers
{
	[SessionManager(RoleCheckRequired = false)]
    public class DataController : Controller
    {
        SessionData sessionData = new SessionData();
        bool isAspUser = false;
        bool IsAspApproved = false;
        dynamic _signum = null;
        // GET: Data
        public ActionResult Index()
        {
            return View();
        }
        [System.Web.Mvc.HttpGet]
        public async Task<dynamic> Get(string param, string technology)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);

            try
            {
                string marketArea = HeadersFromUrl.ReadHeadersByKey(Request, "MarketArea");
                string signum = HeadersFromUrl.ReadHeadersByKey(Request, "Signum");
                string role = HeadersFromUrl.ReadHeadersByKey(Request, "Role");

                using (var client = new HttpClient())
                {
                    client.DefaultRequestHeaders.Add("MarketArea", marketArea);
                    client.DefaultRequestHeaders.Add("Signum", signum);
                    client.DefaultRequestHeaders.Add("Role", role);

                    string baseUrl = string.Empty;

                    if (string.Equals(technology, "JAVA", StringComparison.OrdinalIgnoreCase) == true)
                    {
                        baseUrl = WebConfigDetail.JavaRestApiUrl;
                    }
                    else
                    {
                        baseUrl = WebConfigDetail.DotNetRestApiUrl;
                    }

                    string ApiUrl = string.Format("{0}{1}", baseUrl, param);
                    dynamic responseApi = await client.GetAsync(ApiUrl);
                    return responseApi;

                }


            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return await Task.FromResult<dynamic>(ex.Message.ToString());
            }

        }
        [System.Web.Mvc.HttpPost]
        public async Task<dynamic> Post([System.Web.Http.FromUri]string param, [System.Web.Http.FromUri]string technology, [System.Web.Http.FromBody] dynamic data)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            string marketArea = HeadersFromUrl.ReadHeadersByKey(Request, "MarketArea");
            string signum = HeadersFromUrl.ReadHeadersByKey(Request, "Signum");
            string role = HeadersFromUrl.ReadHeadersByKey(Request, "Role");

            System.Net.Http.HttpResponseMessage response = null;


            try
            {
                using (var client = new HttpClient())
                {
                    client.DefaultRequestHeaders.Add("MarketArea", marketArea);
                    client.DefaultRequestHeaders.Add("Signum", signum);
                    client.DefaultRequestHeaders.Add("Role", role);

                    client.Timeout = TimeSpan.FromSeconds(500);
                    string baseUrl = string.Empty;



                    Stream req = Request.InputStream;
                    req.Seek(0, System.IO.SeekOrigin.Begin);
                    string json = new StreamReader(req).ReadToEnd();



                    if (string.Equals(data.Technology, "JAVA", StringComparison.OrdinalIgnoreCase) == true)
                    {
                        baseUrl = WebConfigDetail.JavaRestApiUrl;
                    }
                    else
                    {
                        baseUrl = WebConfigDetail.DotNetRestApiUrl;
                    }
                    string ApiUrl = string.Format("{0}{1}", baseUrl, data.Param);
                    //Uri requestUri = new Uri("http://10.184.19.202:8080/isf-rest-server-java/accessManagement/authorize"); //replace your Url  
                    Uri requestUri = new Uri(ApiUrl); //replace your Url  

                    // capture API request in logs
                    CreateLogsObj.WriteAPIRequestLogs(requestUri.ToString(), Request);

                    response = await client.PostAsync(requestUri, null);
                    string responJsonText = await response.Content.ReadAsStringAsync();


                    // capture API response in logs
                    CreateLogsObj.WriteAPIResponseLogs(requestUri.ToString(), responJsonText);

                    return response;
                }

            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return ex.Message.ToString();
            }


        }

        public async Task<dynamic> GetFileFromApi(string apiUrl)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            string urlForFile = string.Empty;

            try
            {
                var handler = new HttpClientHandler();
                handler.ClientCertificateOptions = ClientCertificateOption.Manual;
                handler.ServerCertificateCustomValidationCallback =
                    (httpRequestMessage, cert, cetChain, policyErrors) =>
                    {
                        return true;
                    };


                using (var client = new HttpClient(handler))
                {

                    string marketArea = string.Empty;
                    string signum = string.Empty;
                    string role = string.Empty;
                    string token = string.Empty;
                    token = new TokenGenerator().GenerateSessionToken();


                    client.DefaultRequestHeaders.Add("MarketArea", marketArea);
                    client.DefaultRequestHeaders.Add("Signum", signum);
                    client.DefaultRequestHeaders.Add("Role", role);
                    client.DefaultRequestHeaders.Add("X-Auth-Token", token);

                    client.Timeout = TimeSpan.FromMinutes(30);

                    // System.Net.Http.HttpResponseMessage response = null;
                    string baseUrl = string.Empty;
                    Uri requestUri = new Uri(apiUrl); //replace your Url  
                    using (System.Net.Http.HttpResponseMessage response = await client.GetAsync(requestUri))
                    {
                        // Get Certificate Here
                        var cert = ServicePointManager.FindServicePoint(requestUri).Certificate;
                        //
                        var responseModel = FetchAPIResponse.Instance.ExtractResponseFromAPIData(CreateLogsObj, requestUri, response);

                        var result = await FetchAPIResponse.Instance.FilterResponse(responseModel);

                        if (result != null)
                        {
                            var convertedResult = JsonConvert.DeserializeObject<FileDownloadOutputDetail>(result);
                            byte[] fileBytes = convertedResult.pFileContent;

                            if (fileBytes == null)
                            {
                                string fileNotFound = "No data found for this file.";
                                fileBytes = Encoding.ASCII.GetBytes(fileNotFound);
                                convertedResult.pFileName = "File Not Found.txt";
                            }
                            return File(fileBytes, System.Net.Mime.MediaTypeNames.Application.Octet, convertedResult.pFileName);
                        }
                        else
                        {
                            return null;
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return ex.Message;
            }
        }

        [System.Web.Mvc.HttpGet]
        public async Task<dynamic> ExtendSession()
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            try
            {

                SessionToken sessionTokenData = new SessionToken();
                sessionTokenData.Signum = await GetSessionDataByKey(ConstantKeywords.C_Signum);
                sessionTokenData.Token = new TokenGenerator().GenerateSessionToken();
                Session.Timeout = WebConfigDetail.SessionTimeOutInterval;
                sessionTokenData.ExpirationTime = Session.Timeout;
                if (Session[ConstantKeywords.C_IsAspUser] != null)
                {
                    isAspUser = Convert.ToBoolean(Session[ConstantKeywords.C_IsAspUser]);
                }
                if (isAspUser == false || (string.Equals(Session[ConstantKeywords.C_AspStatus], ConstantKeywords.C_AspStatusApproved) == true))
                {
                    IsAspApproved = true;
                    sessionTokenData.Role = await GetSessionDataByKey(ConstantKeywords.C_UserRole);
                    sessionTokenData.Organisation = await GetSessionDataByKey(ConstantKeywords.C_Organisation);
                }
                return await Task.FromResult<dynamic>(JsonConvert.SerializeObject(sessionTokenData));
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return await Task.FromResult<dynamic>(ex.InnerException.ToString());
            }



        }

        public async Task<dynamic> GetConfiguration()
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            try
            {
                ConfigurationDetail configurationDetailData = new ConfigurationDetail();
                configurationDetailData.environment = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_environment];
                configurationDetailData.downloadISFSetupUrl = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_downloadISFSetup];
                configurationDetailData.JavaApiUrl = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_JavaRestApiUrl];
                configurationDetailData.JavaApiVmUrl = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_JavaRestVMApiUrl];
                configurationDetailData.gtagUrl = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_gtagUrl];
                configurationDetailData.UiRootDir = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_UiRootDir];
                configurationDetailData.downloadOutlookAddinUrl = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_downloadOutlookAddin];
                configurationDetailData.AzureSignalRHubUrl = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_AzureSignalRHubURL];
                configurationDetailData.AppInsightsInstrumentationKey = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_AppInsightsInstrumentationKey];
                configurationDetailData.botNestExternalUrl = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_botNestExternalUrl];
                configurationDetailData.uploadWIFile = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_uploadWIFile];
                configurationDetailData.uploadNE = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_uploadNE];
                configurationDetailData.pathUrlNE = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_pathUrlNE];
                configurationDetailData.limitValueForDRAccess = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_limitDRAccess];
                configurationDetailData.MANUAL_STEP_EXE_DEFAULT_VALUE = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_MANUAL_STEP_EXE_DEFAULT_VALUE];
                configurationDetailData.AUTOMATIC_STEP_EXE_DEFAULT_VALUE = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_AUTOMATIC_STEP_EXE_DEFAULT_VALUE];
                configurationDetailData.NO_OF_WO_NEID_DEFAULT_VALUE = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_NO_OF_WO_NEID_DEFAULT_VALUE];
                configurationDetailData.manualAutoStepLimitValue = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_MANUAL_AUTO_STEP_CREATION_LIMIT];
                configurationDetailData.decisionStepLimitValue = System.Configuration.ConfigurationManager.AppSettings[UIConfigurationKeysConstant.C_DECISION_STEP_CREATION_LIMIT];

                return await Task.FromResult<dynamic>(JsonConvert.SerializeObject(configurationDetailData));
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return await Task.FromResult<dynamic>(ex.InnerException.ToString());
            }



        }


        public async Task<dynamic> GetSessionDataByKey(string key)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            try
            {
                if (string.IsNullOrWhiteSpace(key) == true)
                {
                    key = Request.Params["key"].ToString();
                }

                if (Session != null && Session[key] != null)
                {

                    return await Task.FromResult<dynamic>(Session[key]);
                }
                else
                {
                    string signum = string.Empty;

                    if (Session[ConstantKeywords.C_Signum] == null)
                    {
                        signum = await sessionData.GetSignumFromApiAsync();
                        if (signum != null)
                        {
                            Session[ConstantKeywords.C_Signum] = signum;

                            dynamic result = await sessionData.SaveUserLoginDetail(ConstantKeywords.C_StatusLogin);

                            if (result != null)
                            {
                                Session[ConstantKeywords.C_UserInfoAlreadySaved] = true;
                                Session[ConstantKeywords.C_UserLoginId] = result;
                            }
                        }

                    }
                    if (Session[ConstantKeywords.C_IsAspUser] != null)
                    {
                        isAspUser = Convert.ToBoolean(Session[ConstantKeywords.C_IsAspUser]);
                    }
                    if ((signum != null && isAspUser == false)
                        || string.Equals(Session[ConstantKeywords.C_AspStatus].ToString(), ConstantKeywords.C_AspStatusApproved, StringComparison.OrdinalIgnoreCase) == true)
                    {
                        if (Session[ConstantKeywords.C_UserAccessProfile] == null)
                        {
                            dynamic result = await GetRoleAndOrganizationAsync();
                        }

                        if (Session[ConstantKeywords.C_UserMenuByRole] == null)
                        {
                            dynamic result = await GetUserMenuByRole();
                            Session[ConstantKeywords.C_UserMenuByRole] = result;
                        }

                        if (Session[ConstantKeywords.C_PageAccessDetailsByRoleOfSignum] == null)
                        {
                            dynamic result = await GetPageAccessDetailsByRoleOfSignum();
                            Session[ConstantKeywords.C_PageAccessDetailsByRoleOfSignum] = result;

                        }
                        if (Session[ConstantKeywords.C_UserTimeZone] == null)
                        {
                            dynamic result = await GetUserTimeZone();
                        }
                    }
                    if (Session[key] == null)
                    {
                        return await Task.FromResult<dynamic>("No data found this key");
                    }
                    return await Task.FromResult<dynamic>(Session[key]);

                }

            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return await Task.FromResult<dynamic>(Session[key]);
            }
        }
        public async Task<dynamic> GetAllSession()
        {
            string sigNum = string.Empty;
            sigNum = (string)(Session[ConstantKeywords.C_Signum] ?? string.Empty);
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(sigNum);
            try
            {
                SessionDetail sessionDetail = new SessionDetail();
                if (Session[ConstantKeywords.C_Signum] == null)
                {
                    Session.Clear();
                    Session.Abandon();
                    Session.RemoveAll();
                    HttpContext.Response.Cache.SetCacheability(HttpCacheability.NoCache);
                    HttpContext.Response.Cache.SetNoServerCaching();
                    HttpContext.Response.Cache.SetNoStore();
                    return RedirectToAction("SignOut", "Login");


                }
                else
                { 
					sessionDetail.IsLoginModeAzureAd = Session[ConstantKeywords.C_IsLoginModeAzureAd];
					sessionDetail.ExpirationTime = Session[ConstantKeywords.C_ExpirationTime];
					sessionDetail.Signum = sigNum;
                    if (Session[ConstantKeywords.C_IsAspUser] != null)
					{
						isAspUser = Convert.ToBoolean(Session[ConstantKeywords.C_IsAspUser]);
					}
					if (isAspUser == false || (string.Equals(Session[ConstantKeywords.C_AspStatus], ConstantKeywords.C_AspStatusApproved) == true))
					{
						IsAspApproved = true;

                        sessionDetail.PageAccessDetailsByRoleOfSignum = await GetSessionDataByKey(ConstantKeywords.C_PageAccessDetailsByRoleOfSignum);
                        sessionDetail.UserMenu = Session[ConstantKeywords.C_UserMenu];
                        sessionDetail.UserAccessProfile = Session[ConstantKeywords.C_UserAccessProfile];
                        sessionDetail.UserDetails = Session[ConstantKeywords.C_UserDetails] ?? null;
                        sessionDetail.PageAccess = Session[ConstantKeywords.C_PageAccess] ?? null;
                        sessionDetail.ActiveProfile = Session[ConstantKeywords.C_ActiveProfile];
                        sessionDetail.Organisation = Session[ConstantKeywords.C_Organisation];
                        sessionDetail.UserRole = Session[ConstantKeywords.C_UserRole];
                        sessionDetail.UserImageUri = Session[ConstantKeywords.C_UserImageUri];
                        sessionDetail.UserName = Session[ConstantKeywords.C_UserName];
                        sessionDetail.ProfileList = Session[ConstantKeywords.C_ProfileList] ?? null;
                        sessionDetail.MenuIdList = (Session[ConstantKeywords.C_MenuIdList] != null ? Session[ConstantKeywords.C_MenuIdList] : null);
                        sessionDetail.MenueIdsWithUrl = (Session[ConstantKeywords.C_MenueIdsWithUrl] != null ? Session[ConstantKeywords.C_MenueIdsWithUrl] : null);
                        sessionDetail.UserUpn = Session[ConstantKeywords.C_UserUpn] ?? null;
                        sessionDetail.UserPreference = Session[ConstantKeywords.C_UserPreference] ?? null;
                        sessionDetail.OrganisationId = Session[ConstantKeywords.C_SignumOrganisationId] ?? null;
                        sessionDetail.SetProfileHistoryStatus = Session[ConstantKeywords.C_ProfileHistoryStatus] ?? null;
                        sessionDetail.UserWiseDesktopResponse = Session[ConstantKeywords.C_IsDesktopVersionUpdateRequired] ?? null;

                        if (Session[ConstantKeywords.C_UserTimeZone] == null)
                        {
                            Task<dynamic> taskTimeZone = GetUserTimeZone();
                            dynamic result = await taskTimeZone;
                            Session[ConstantKeywords.C_UserTimeZone] = result;
                        }
                        sessionDetail.UserTimeZone = Session[ConstantKeywords.C_UserTimeZone];

                    }

                }

                try
                {
                    return await Task.FromResult<dynamic>(JsonConvert.SerializeObject(sessionDetail));
                }
                catch (Exception ex)
                {
                    CreateLogsObj.WriteExceptionLogs(ex);
                    return null;
                }
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return null;
            }
        }


        [HttpPost]
        public async Task<dynamic> SetSessionData([System.Web.Http.FromBody]string sessionData)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            try
            {
                string key = string.Empty;
                key = Request.Params["key"].ToString();

                if (Session[key] != null
                    || string.Equals(key, ConstantKeywords.C_NavigationList, StringComparison.OrdinalIgnoreCase) == true
                    || string.Equals(key, ConstantKeywords.C_MenuIdList, StringComparison.OrdinalIgnoreCase) == true
                    || string.Equals(key, ConstantKeywords.C_MenueIdsWithUrl, StringComparison.OrdinalIgnoreCase) == true
                    )
                {
                    Session[key] = sessionData;
                }
                if (string.Equals(key, ConstantKeywords.C_UserRole, StringComparison.OrdinalIgnoreCase))
                {
                    Session[ConstantKeywords.C_PageAccessDetailsByRoleOfSignum] = null;
                    Session[ConstantKeywords.C_PageAccessDetailsByRoleOfSignum] = await GetSessionDataByKey(ConstantKeywords.C_PageAccessDetailsByRoleOfSignum);
                }
                return await Task.FromResult<dynamic>("SUCCESS");
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return await Task.FromResult<dynamic>(ex.Message.ToString());
            }

        }

        public async Task<dynamic> GetToken()
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            try
            {
                dynamic token = new TokenGenerator().GenerateSessionToken();
                return await Task.FromResult<dynamic>(token);
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return await Task.FromResult<dynamic>(ex.Message.ToString());
            }


        }
        public async Task<dynamic> GetSignum()
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            try
            {
                dynamic result = await GetSessionDataByKey(ConstantKeywords.C_Signum);
                return await Task.FromResult<dynamic>(result);
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return await Task.FromResult<dynamic>(ex.Message.ToString());
            }
        }
        //public async Task<dynamic> GetUserMenu()
        //{
        //    dynamic result = await sessionData.GetUserMenu();
        //    if (result != null)
        //    {
        //        Session[ConstantKeywords.C_UserMenu] = result;
        //    }

        //    return result;
        //}
        public async Task<dynamic> GetUserMenuByRole()
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            try
            {
                dynamic result = await sessionData.GetUserMenuByRole();
                if (result != null)
                {
                    Session[ConstantKeywords.C_UserMenuByRole] = result;
                }

                return result;
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return await Task.FromResult<dynamic>(ex.Message.ToString());
            }
        }
        public async Task<dynamic> GetPageAccessDetailsByRoleOfSignum()
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            try
            {
                dynamic result = await sessionData.GetPageAccessDetailsByRoleOfSignum();
                if (result != null)
                {
                    Session[ConstantKeywords.C_PageAccessDetailsByRoleOfSignum] = result;
                }

                return result;
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return await Task.FromResult<dynamic>(ex.Message.ToString());
            }
        }

        public async Task<dynamic> GetUserTimeZone()
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            try
            {
                dynamic result = await sessionData.GetUserPreferance();
                if (result != null)
                {
                    dynamic data = JsonConvert.DeserializeObject(result);

                    dynamic timeZone = data["defaultZoneValue"];

                    string zone = timeZone.ToString();

                    Session[ConstantKeywords.C_UserTimeZone] = zone;
                }
                return result;

            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return await Task.FromResult<dynamic>(ex.Message.ToString());
            }
        }
        public async Task<dynamic> GetRoleAndOrganizationAsync()
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            try
            {
                dynamic result = await sessionData.GetUserAccessProfile();
                //bool flag = true;
                //flag = FetchAPIResponse.IsValidJson(result);

                if (result != null)
                {
                    dynamic data = JsonConvert.DeserializeObject(result);
                    dynamic resData = data["responseData"];
                    dynamic activeProfile = resData[0].lstAccessProfileModel[0];
                    // dynamic pageList = activeProfile.lstCapabilityModel;
                    dynamic profileList = resData[0].lstAccessProfileModel;
                    string role = activeProfile.role;
                    string organisation = activeProfile.organisation;
                    string userName = resData[0].employeeName;
                    string userImageUri = resData[0].userImageUri;

                    Session[ConstantKeywords.C_UserAccessProfile] = result;
                    Session[ConstantKeywords.C_UserDetails] = JsonConvert.SerializeObject(resData);
                    //Session[ConstantKeywords.C_PageAccess] = JsonConvert.SerializeObject(pageList);
                    Session[ConstantKeywords.C_ActiveProfile] = JsonConvert.SerializeObject(activeProfile);
                    Session[ConstantKeywords.C_ProfileList] = JsonConvert.SerializeObject(profileList);

                    Session[ConstantKeywords.C_UserName] = userName;
                    Session[ConstantKeywords.C_UserImageUri] = userImageUri;

                    Session[ConstantKeywords.C_UserRole] = role;
                    Session[ConstantKeywords.C_Organisation] = organisation;
                }

                return result;
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return await Task.FromResult<dynamic>(ex.Message.ToString());
            }
        }

        public async Task<dynamic> GetEmail()
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            try
            {
                var userClaims = User.Identity as System.Security.Claims.ClaimsIdentity;
                Session[ConstantKeywords.C_UserEmaiId] = userClaims?.FindFirst(System.IdentityModel.Claims.ClaimTypes.Name)?.Value;
                return await Task.FromResult<dynamic>(userClaims?.FindFirst(System.IdentityModel.Claims.ClaimTypes.Name)?.Value);
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return await Task.FromResult<dynamic>(ex.Message.ToString());
            }

        }

        public async Task<dynamic> GetAllClaims()
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            try
            {
                List<AdfsUserClaim> testClaims = new List<AdfsUserClaim>();
                foreach (var claim in ((System.Security.Claims.ClaimsIdentity)User.Identity).Claims)
                {
                    testClaims.Add(new AdfsUserClaim { Type = claim.Type, Value = claim.Value });
                }
                return await Task.FromResult<dynamic>(JsonConvert.SerializeObject(testClaims));
            }
            catch (JsonReaderException jex)
            {
                //Exception in parsing json
                Console.WriteLine(jex.Message);
                throw jex;
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return await Task.FromResult<dynamic>(ex.Message.ToString());
            }

        }
        public async Task<dynamic> GetSignumFromApi()
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            try
            {
                dynamic signum = await sessionData.GetSignumFromApiAsync();
                return await Task.FromResult<dynamic>(signum);
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return await Task.FromResult<dynamic>(ex.Message.ToString());
            }
        }

        public async Task<dynamic> GetSession(string key)
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            try
            {
                if (string.IsNullOrWhiteSpace(key) == true)
                {
                    key = Request.Params["key"].ToString();
                }
                return await Task.FromResult<dynamic>(Session[key]);
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return ex.InnerException.ToString();
            }
        }

        [HttpPost]
        public string SaveAutoSenseData(dynamic data)
        {
            if (data == null)
            {
                return "No data recieved";
            }
            else
            {
                Session.Add("AutoSenseData", data);
                return JsonConvert.SerializeObject("success");
            }
        }

        [HttpGet]
        public dynamic GetAutoSenseData()
        {
            if(Session != null)
            {
                var autoSenseData = string.Empty;

                if (Session["AutoSenseData"] != null)
                {
                    autoSenseData = JsonConvert.SerializeObject(Session["AutoSenseData"]);
                    return autoSenseData;
                }

            }

            return null;
        }

        //to get signalR End Point Communication String
        //public string GetSIgnalrServiceEndpoint()
        //{
        //    string signalREndPointConnectionString = "https://p4signalrtest.service.signalr.net/client/?hub=serverpush";
        //    if(signalREndPointConnectionString != null)
        //    {
        //        return signalREndPointConnectionString;
        //    }
        //    else
        //    {
        //        return null;
        //    }
        //}

        private string GetClientUrl(string endpoint, string hubName)
        {
            return $"{endpoint}/client/?hub={hubName}";
        }

        public void MessageFromServer(string message)
        {
            var msg = message;
        }

        public async Task<dynamic> signalRTokenDataGet()
        {
            var signum = Request.Headers.Get("Signum");
            var AzureSignalRConnectionString = WebConfigDetail.AzureSignalRConnectionString;
            TokenManager tokenManager = new TokenManager(AzureSignalRConnectionString);
            //  var url = GetClientUrl(tokenManager.Endpoint, "serverpush");
            var url = WebConfigDetail.AzureSignalRHubURL;
            var tokenSig = await Task.FromResult(tokenManager.GenerateAccessToken(url, signum));

            //string signalREndPointConnectionString = "value";
            if (tokenSig != null)
            {
                return tokenSig;
            }
            else
            {
                return null;
            }

        }

        [HttpPost]
        public async Task<string> MobileNotification(dynamic notificationDetails)
        {
            bool IsSuccess = false;
            if (notificationDetails != null)
            {
                try
                {
                    IsSuccess = await CallMobileNotificationAPI(notificationDetails);
                }
                catch (Exception ex)
                {
                    CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
                    CreateLogsObj.WriteExceptionLogs(ex);
                }
            }
            return JsonConvert.SerializeObject(IsSuccess);
        }

        private static async Task<bool> CallMobileNotificationAPI(dynamic notificationDetails)
        {
            var handler = new HttpClientHandler();
            handler.ClientCertificateOptions = ClientCertificateOption.Manual;
            handler.ServerCertificateCustomValidationCallback =
                (httpRequestMessage, cert, cetChain, policyErrors) =>
                {
                    return true;
                };

            using (var client = new HttpClient(handler))
            {
                string token = string.Empty;
                token = new TokenGenerator().GenerateSessionToken();

                client.DefaultRequestHeaders.Add("ApiAuthKey", WebConfigDetail.ApiAuthKey);

                client.Timeout = TimeSpan.FromMinutes(30);
                var request = JsonConvert.SerializeObject(notificationDetails);
                string apiurl = WebConfigDetail.NotificationApiUrl;
                Uri requestUri = new Uri(apiurl); //replace your Url  
                var content = new StringContent(request, Encoding.UTF8, "application/json");
                using (System.Net.Http.HttpResponseMessage response = await client.PostAsync(requestUri.ToString(), content))
                {
                    if(response.StatusCode == HttpStatusCode.OK)
                    {
                        return true;
                    }
                }
            }

            return false;
        }

        // get details for MQTT shared worker
        [CustomAuthenticationForMqtt]
        public dynamic getMqttDataDetails()
        {
            TokenGenerator tokenGenerator = new TokenGenerator();
            var signum = Session[ConstantKeywords.C_Signum];
            var userName = tokenGenerator.Encrypt(WebConfigDetail.userNameMQTT);
            var password = tokenGenerator.Encrypt(WebConfigDetail.passwordMQTT);
            var hostNameMQTT = tokenGenerator.Encrypt(WebConfigDetail.hostNameMQTT);
            MqttDataModel mqttDataModel = new MqttDataModel()
            {
                signum = signum.ToString(),
                userName = userName,
                password = password,
                hostNameMQTT = hostNameMQTT
            };

            string responseJson = JsonConvert.SerializeObject(mqttDataModel);
            return responseJson;
        }

        public string ErrorWhileFetchingMqtt()
        {
            return "Not Authorized";
        }

      
    }

}