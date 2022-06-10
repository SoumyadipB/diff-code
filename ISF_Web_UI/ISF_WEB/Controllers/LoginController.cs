using ISF.AzureRedisCache;
using ISF_WEB.Filters;
using ISF_WEB.Models;
using ISF_WEB.Sessions;
using ISF_WEB.Util;
using Microsoft.Owin.Security;
using Microsoft.Owin.Security.Cookies;
using Microsoft.Owin.Security.OpenIdConnect;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;

namespace ISF_WEB.Controllers
{
	[SessionManager(RoleCheckRequired = false)]
    public class LoginController : Controller
    {
        SessionData sessionData = new SessionData();


        // GET: Login
        public async Task<ActionResult> Index()
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(string.Empty);
            try
            {
                Session[ConstantKeywords.C_IsLoginModeAzureAd] = WebConfigDetail.IsLoginModeAzureAd;
                var userClaims = User.Identity as System.Security.Claims.ClaimsIdentity;
                ViewBag.Username = userClaims?.FindFirst(System.IdentityModel.Claims.ClaimTypes.Name)?.Value;
                if (WebConfigDetail.IsLoginModeAzureAd)
                {
                    if (Session[ConstantKeywords.C_UserInfoAlreadySaved] == null)
                    {
                        CreateLogsObj.WriteInformationLogs("Authentication has been completed");
                        dynamic result = await SaveUserDetail();
                        CreateLogsObj.WriteInformationLogs("User details are saved and API's transactions were successful for login process.");
                    }

                    if (Session[ConstantKeywords.C_Signum] == null)
                    {
                        await SignOut("HandleLoginException");
                    }

                    return RedirectToAction("AzureADLogin", "Login");
                }
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                await SignOut("HandleLoginException");
            }

            return View();
        }

        public ActionResult AspLogin()
        {

            return View();
        }
        public ActionResult AzureADLogin()
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            CreateLogsObj.WriteInformationLogs("Moving to AzureADLogin page to redirect to home page/User preference page");

            string referrerController;
            string referrerAction;
            referrerController = (Session[ConstantKeywords.C_ReferrerController] != null ?
                Session[ConstantKeywords.C_ReferrerController].ToString() : "");
            referrerAction = (Session[ConstantKeywords.C_ReferrerAction] != null ?
                Session[ConstantKeywords.C_ReferrerAction].ToString() : "");
            if (string.IsNullOrWhiteSpace(referrerController) == false
                && string.IsNullOrWhiteSpace(referrerAction) == false
                && string.Equals(referrerController, "Home") == false
                && string.Equals(referrerController, "Login") == false)
            {
                Session[ConstantKeywords.C_ReferrerController] = null;
                Session[ConstantKeywords.C_ReferrerAction] = null;
                return RedirectToAction(referrerAction, referrerController);
            }

            return View();

        }
        [AllowAnonymous]
        [CustomAuthorization(AuthorizationCheckRequired = false)]
        [SessionManager(SessionCheckRequired = false)]
        public ActionResult SessionTimeout()
        {
            
            return View();

        }

        [AllowAnonymous]
        [CustomAuthorization(AuthorizationCheckRequired = false)]
        [SessionManager(SessionCheckRequired = false)]
        public ActionResult ZeroDowntimeDemo()
        {

            return View();

        }

        [AllowAnonymous]
        [CustomAuthorization(AuthorizationCheckRequired = false)]
        [SessionManager(SessionCheckRequired = false)]
        public ActionResult Logout()
        {
            return View();
        }
    
        
        public ActionResult UnauthorizedWarning()
        {
            return View();
        }
        [AllowAnonymous]
        [CustomAuthorization(AuthorizationCheckRequired = false)]
        [SessionManager(SessionCheckRequired = false)]
        public ActionResult ErrorLogout()
        {
            return View();
        }
        /// <summary>
        /// Send an OpenID Connect sign-out request.
        /// </summary>
        /// 

        [AllowAnonymous]
        [CustomAuthorization(AuthorizationCheckRequired = false)]
        [SessionManager(SessionCheckRequired = false)]
        public ActionResult HandleLoginException()
        {
            return View();
        }
        [AllowAnonymous]
        [CustomAuthorization(AuthorizationCheckRequired = false)]
        [SessionManager(SessionCheckRequired = false)]
        public async Task SignOut(string returnUrl = null)
        {
            Session[ConstantKeywords.C_UserEmaiId] = null;
            HttpContext ctx = System.Web.HttpContext.Current;
            string loginInfoKey = ctx.Session.SessionID;
            CreateLogs createLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            createLogsObj.WriteInformationLogs($"RemoveKeyFromCacheUsingKey() method start with signum: {Session[ConstantKeywords.C_Signum]}, keyinfo:{loginInfoKey}");
            bool isDeleted = AzureRedisCacheOperations.Instance.RemoveValueByKey(loginInfoKey);
            createLogsObj.WriteInformationLogs($"RemoveKeyFromCacheUsingKey() method end with result: {isDeleted.ToString()}");
            Session.Timeout = 1;
			Session.Clear();
			Session.Abandon();
			Session.RemoveAll();
			var absoluteReturnUrl = string.IsNullOrEmpty(returnUrl)
                ? Url.Action("Logout", "Login", new { }, Request.Url.Scheme)
                : Url.Action(returnUrl, "Login", new { }, Request.Url.Scheme);
            string[] absUrlArr = absoluteReturnUrl.Split('/');
            absUrlArr[2] = WebConfigDetail.AbsUrl;
            var newabsoluteReturnUrl = string.Join("/", absUrlArr);

            HttpContext.Response.Cache.SetCacheability(HttpCacheability.NoCache);
            HttpContext.Response.Cache.SetNoServerCaching();
            HttpContext.Response.Cache.SetNoStore();
            var httpCookies= new HttpCookie("ASP.NET_SessionId", string.Empty);
            httpCookies.Expires= DateTime.UtcNow.AddDays(-7);
            HttpContext.Response.Cookies.Add(httpCookies);
            createLogsObj.WriteInformationLogs($"absoluteReturnUrl: {absoluteReturnUrl}");
            createLogsObj.WriteInformationLogs($"NewabsoluteReturnUrl: {newabsoluteReturnUrl}");
            Request.GetOwinContext().Authentication.SignOut(
                new AuthenticationProperties
                {
                    RedirectUri = newabsoluteReturnUrl.Replace("http://", "https://")
                },
                Microsoft.AspNet.Identity.DefaultAuthenticationTypes.ApplicationCookie,
                OpenIdConnectAuthenticationDefaults.AuthenticationType,
                CookieAuthenticationDefaults.AuthenticationType);
        }
        private async Task<dynamic> SaveUserDetail()
        {
            try
            {
                string signumId = string.Empty;

                SessionData sessionData = new SessionData();
                signumId = await sessionData.GetSignumFromApiAsync();
                if (!string.IsNullOrWhiteSpace(signumId))
                {
                    Session[ConstantKeywords.C_Signum] = signumId;

                    dynamic result = await sessionData.SaveUserLoginDetail(ConstantKeywords.C_StatusLogin);
                   //check User Desktop Version status.
                    await GetUserWiseDesktopVersionStatus();

                    // get notificationData from Redis
                    await GetNotificationDataFromRedis();
                    if (result != null) {
                        Session[ConstantKeywords.C_UserInfoAlreadySaved] = true;
                        Session[ConstantKeywords.C_UserLoginId] = result;

                        if (Session[ConstantKeywords.C_IsAspUser] != null && Convert.ToBoolean(Session[ConstantKeywords.C_IsAspUser]) == true
                            && (string.Equals(Session[ConstantKeywords.C_AspStatus], ConstantKeywords.C_AspStatusApproved) == false))
                        {
                            return await Task.FromResult<dynamic>("NewASPUser");
                        }

                        result = await GetRoleAndOrganizationAsync();

                        result = await sessionData.GetUserMenuByRole();
                        Session[ConstantKeywords.C_UserMenuByRole] = result;

                        result = await sessionData.GetPageAccessDetailsByRoleOfSignum();
                        Session[ConstantKeywords.C_PageAccessDetailsByRoleOfSignum] = result;

                        result = await GetPreferance();
                    }
                    else
                    {
                        await SignOut("HandleLoginException");
                    }


                }
            }
            catch (Exception ex)
            {
                throw (ex);
            }

            return await Task.FromResult<dynamic>("SUCCESS");
        }
        /// <summary>
        /// return/check user wise desktop version details from api side.
        /// </summary>
        /// <returns></returns>
        public async Task GetUserWiseDesktopVersionStatus()
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            string signumLatestDesktopVersion = $"{Session[ConstantKeywords.C_Signum]?.ToString()?.ToLower()}{ConstantKeywords.C_RedisKey}";
            string isLatestDesktopVersion = AzureRedisCacheOperations.Instance.GetValueByKey(signumLatestDesktopVersion);
            CreateLogsObj.WriteInformationLogs($"Start Method GetUserDesktopVersionStatus redis key: {isLatestDesktopVersion}");
            if (string.IsNullOrEmpty(isLatestDesktopVersion))
            {
                var signum = Session[ConstantKeywords.C_Signum].ToString();
                dynamic isUpdateRequired = await sessionData.CheckDesktopVersion(signum);
                Session[ConstantKeywords.C_IsDesktopVersionUpdateRequired] = isUpdateRequired;
            }
            else
            {
                DesktopVersionResponseModal responseModal = new DesktopVersionResponseModal();
                responseModal.result = false;
                responseModal.VersionNumber = string.Empty;
                dynamic responseRedis = JsonConvert.SerializeObject(responseModal);
                Session[ConstantKeywords.C_IsDesktopVersionUpdateRequired] = responseRedis;
            }
            CreateLogsObj.WriteInformationLogs($"End Method GetUserDesktopVersionStatus");
        }
        public async Task<dynamic> GetPreferance()
        {

            SessionData sessionData = new SessionData();
            try
            {
                dynamic result = await sessionData.GetUserPreferance();

                if (result != null)
                {
                    dynamic data = JsonConvert.DeserializeObject(result);
                    dynamic timeZone = data["defaultZoneValue"];
                    string zone = timeZone.ToString();

                    Session[ConstantKeywords.C_UserTimeZone] = zone;
                    return result;

                }
            }
            catch (Exception ex) {
                CreateLogs createLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
                createLogsObj.WriteExceptionLogs(ex);
                throw ex;
            }

            return null;
        }
        public async Task<dynamic> GetRoleAndOrganizationAsync()
        {
            try
            {
                SessionData sessionData = new SessionData();

                // new API GetAccessProfileOfUser/signum to get only selected and important details from GetUserAccessProfile
                dynamic result = await sessionData.GetAccessProfileOfUser();

                if (result != null)
                {
                    APIResponse data;
                    data = JsonConvert.DeserializeObject<APIResponse>(result);

                    dynamic resData = data.responseData;

                    dynamic activeProfile = resData[0].lstAccessProfileModel[0];
                    Session[ConstantKeywords.C_ActiveProfile] = JsonConvert.SerializeObject(activeProfile);

                    string role = activeProfile.role;
                    Session[ConstantKeywords.C_UserRole] = role;

                    string organisation = activeProfile.organisation;
                    Session[ConstantKeywords.C_Organisation] = organisation;

                    Session[ConstantKeywords.C_ActiveProfile] = JsonConvert.SerializeObject(activeProfile);


                    // Call new API for getting profile list
                    dynamic profileListResult = await sessionData.getUserAccessProfileBySignum();


                    if (profileListResult != null)
                    {
                        dynamic profileListResultObj = JsonConvert.DeserializeObject(profileListResult);

                        string userName = resData[0].employeeName;
                        string userImageUri = resData[0].userImageUri;

                        Session[ConstantKeywords.C_UserName] = userName;
                        Session[ConstantKeywords.C_UserImageUri] = userImageUri;

                        Session[ConstantKeywords.C_UserAccessProfile] = result;
                        Session[ConstantKeywords.C_UserDetails] = JsonConvert.SerializeObject(resData);
                        Session[ConstantKeywords.C_ProfileList] = JsonConvert.SerializeObject(profileListResultObj);
                    }

                }
                return result;
            }
            catch (Exception ex)
            {
                CreateLogs createLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
                createLogsObj.WriteExceptionLogs(ex);
                throw ex;
            }
        }
        //</SigInAndSignOut>

        private bool DoesMenuExist()
        {
            if (Session[ConstantKeywords.C_UserMenuByRoleDictionary] != null)
            {
                List<Dictionary<string, dynamic>> MenuSessionValues = Session[ConstantKeywords.C_UserMenuByRoleDictionary] as List<Dictionary<string, dynamic>>;
                var MenuSessionValuesByRole = MenuSessionValues.Find(item => item.ContainsKey(Session[ConstantKeywords.C_UserRole].ToString()));

                return MenuSessionValuesByRole == null || MenuSessionValuesByRole.Count == 0 ? false : true;
            }

            return false;
        }
        private async Task<dynamic> GetUserMenuByRoleAsync()
        {
            SessionData sessionData = new SessionData();
            List<UserMenu> userMenu = new List<UserMenu>();

            dynamic result = await sessionData.GetUserMenuByRole();

            if (result != null)
            {
                // Adding user menu details with role so that we can check if role has changed and we need call API again to get latest menu
                Dictionary<string, dynamic> SessionData_UserMenu = new Dictionary<string, dynamic>();
                SessionData_UserMenu.Add(Session[ConstantKeywords.C_UserRole].ToString(), result);

                if (Session[ConstantKeywords.C_UserMenuByRoleDictionary] != null && !DoesMenuExist())
                {
                    List<Dictionary<string, dynamic>> MenuSessionValues = Session[ConstantKeywords.C_UserMenuByRoleDictionary] as List<Dictionary<string, dynamic>>;
                    MenuSessionValues.Add(SessionData_UserMenu);
                    Session[ConstantKeywords.C_UserMenuByRoleDictionary] = MenuSessionValues;
                }
                else
                {
                    List<Dictionary<string, dynamic>> MenuToBeSaved = new List<Dictionary<string, dynamic>>();
                    MenuToBeSaved.Add(SessionData_UserMenu);
                    Session[ConstantKeywords.C_UserMenuByRoleDictionary] = MenuToBeSaved;
                }

                userMenu = JsonConvert.DeserializeObject<List<UserMenu>>(result);

            }

                return userMenu;
        }

        [HttpPost]
        public async Task<ActionResult> GetUserMenuSessionDetails(string UiRootDir)
        {
            bool isAspUser = false;
            var userMenu = new List<UserMenu>();
            if (Session[ConstantKeywords.C_IsAspUser] != null)
            {
                isAspUser = Convert.ToBoolean(Session[ConstantKeywords.C_IsAspUser]);
            }
            if (isAspUser == false || (string.Equals(Session[ConstantKeywords.C_AspStatus], ConstantKeywords.C_AspStatusApproved) == true))
            {
                // Session doesn't exist for menu
                if (!DoesMenuExist())
                {
                    userMenu = await GetUserMenuByRoleAsync();
                }
                else
                {
                    // if session is available for current role
                    List<Dictionary<string, dynamic>> MenuSessionValues = Session[ConstantKeywords.C_UserMenuByRoleDictionary] as List<Dictionary<string, dynamic>>;
                    if (Session[ConstantKeywords.C_UserRole] != null)
                    {
                        var MenuSessionValuesByRole = MenuSessionValues.Find(item => item.ContainsKey(Session[ConstantKeywords.C_UserRole].ToString()));
                       

                        if (MenuSessionValuesByRole[Session[ConstantKeywords.C_UserRole].ToString()] != null)
                        {
                            userMenu = JsonConvert.DeserializeObject<List<UserMenu>>(MenuSessionValuesByRole[Session[ConstantKeywords.C_UserRole].ToString()]);
                        }
                    }
                       
                }
            }

            ViewBag.UiRootDir = UiRootDir;

            return PartialView("_UserMenu", userMenu);

        }

        public async Task<ActionResult> OpenDefaultPageForUser()
        {

            dynamic userPreferences;

            try {
                userPreferences = await OpenDefaultPage();

                if (userPreferences != null)
                {
                    userPreferences = JsonConvert.SerializeObject(userPreferences);
                }
                var OrganisationId = ViewBag.signumOrganisationId != null ? string.IsNullOrEmpty(ViewBag.signumOrganisationId.ToString()) ? null : int.Parse(ViewBag.signumOrganisationId.ToString()) : null;

                // set data to be needed on client side in session which will be retrieved when Menu will be implemented
                Session[ConstantKeywords.C_UserTimeZone] = ViewBag.UserTimeZone;
                Session[ConstantKeywords.C_SignumOrganisationId] = OrganisationId;
                Session[ConstantKeywords.C_ProfileHistoryStatus] = ViewBag.SetProfileHistoryStatus;


                var result = Json(new { Data = userPreferences, UserTimeZone = ViewBag.UserTimeZone, signumOrganisationId = OrganisationId, SetProfileHistoryStatus = ViewBag.SetProfileHistoryStatus });
                return result;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                throw ex;
            }


        }

        public async Task<dynamic> OpenDefaultPage()
        {
            dynamic userPreferences = await setUserPreference();
            return userPreferences;
        }

        public async Task<dynamic> setUserPreference()
        {
            dynamic result;
            dynamic parsedResult;

            result = await sessionData.getUserPreferences();
           
            if (result != null)
            {
                parsedResult = string.IsNullOrEmpty(result) ? null : JsonConvert.DeserializeObject(result);
            }
            else
            {
                parsedResult = null;
            }

            if (parsedResult == null || Session[ConstantKeywords.C_ActiveProfile] == null)
            {
                return null;
            }

            Session[ConstantKeywords.C_UserPreference] = result;

            if (Session[ConstantKeywords.C_ActiveProfile] != null)
            {
                dynamic activeProfileData = JObject.Parse(Session[ConstantKeywords.C_ActiveProfile].ToString());
                string accessProfileName = activeProfileData["accessProfileName"] == null ? string.Empty : activeProfileData["accessProfileName"].ToString();
                string defaultZoneValue = parsedResult["defaultZoneValue"].ToString();
                string defaultProfileValue = parsedResult["defaultProfileValue"].ToString();
                ViewBag.UserTimeZone = GetZoneValue(defaultZoneValue);

                if (!string.IsNullOrEmpty(defaultProfileValue) && defaultProfileValue != accessProfileName)
                {
                    await switchProfileUserPreference(parsedResult, activeProfileData);
                }
                else
                {
                    // save already active session in history
                    ViewBag.SetProfileHistoryStatus = await SetProfileHistory(parsedResult, activeProfileData, true);
                }
            }

            return parsedResult;
        }

        private dynamic GetZoneValue(string defaultZoneValue)
        {
            if (!string.IsNullOrEmpty(defaultZoneValue) && defaultZoneValue.IndexOf('-') != -1)
            {
                var DefaultZoneValueArray = defaultZoneValue.Split('-');
                if (DefaultZoneValueArray[DefaultZoneValueArray.Length - 1] != null)
                {
                    return DefaultZoneValueArray[DefaultZoneValueArray.Length - 1].Trim();
                }              
            }

            return defaultZoneValue;
        }

        public async Task switchProfileUserPreference(dynamic data, dynamic activeProfileData)
        {
            var activateProfileName = data["defaultProfileValue"].ToString();
            var profileListJson = Session[ConstantKeywords.C_ProfileList].ToString();


            List<ProfileDetails> profileList = new List<ProfileDetails>();
            if (profileListJson != null)
            {
                 profileList = JsonConvert.DeserializeObject<List<ProfileDetails>>(profileListJson);
            }
            
            ProfileDetails profile = profileList.FirstOrDefault(item => item.accessProfileName == activateProfileName);

            if (profile != null)
            {
                Session[ConstantKeywords.C_Organisation] = profile.organisation;
                Session[ConstantKeywords.C_UserRole] = profile.role;
                Session[ConstantKeywords.C_ActiveProfile] = JsonConvert.SerializeObject(profile);
                if (data["defaultZoneValue"] != null)
                {
                    Session[ConstantKeywords.C_UserTimeZone] = string.IsNullOrEmpty(data["defaultZoneValue"].ToString()) ? string.Empty : GetZoneValue(data["defaultZoneValue"].ToString());
                }
                ViewBag.signumOrganisationId = activeProfileData["organisationID"];

                try {
                    // to send as input for saving in profile history
                    dynamic profileJson = JsonConvert.SerializeObject(profile);
                    dynamic parsedJson = JObject.Parse(profileJson);

                    //save latest profile coming from get user preference in profile history
                    ViewBag.SetProfileHistoryStatus = await SetProfileHistory(parsedJson, activeProfileData, false);

                    // On Switch Profile, we need to update page details as well
                    var resultObj = await sessionData.GetPageAccessDetailsByRoleOfSignum();
                    Session[ConstantKeywords.C_PageAccessDetailsByRoleOfSignum] = resultObj;
                }

                catch (JsonReaderException jex)
                {
                    //Exception in parsing json
                    Console.WriteLine(jex.Message);
                    throw jex;
                }

            }

            if (data["defaultZoneValue"] != null)
            {
                ViewBag.UserTimeZone = GetZoneValue(data["defaultZoneValue"].ToString());
            }
        }

        private async Task<bool> SetProfileHistory(dynamic jsonData, dynamic ActiveProfileSession, bool isActiveSession)
        {
            bool status = false;
            string preferredAccessProfileID;
            string preferredAccessProfileName;

            // check if we need to save active session in history or profile data from preference (jsonData)
            if (!isActiveSession && jsonData["accessProfileID"] != null && jsonData["accessProfileName"] != null)
            {
                preferredAccessProfileID = jsonData["accessProfileID"];
                preferredAccessProfileName = jsonData["accessProfileName"].ToString();
            }
            else
            {
                preferredAccessProfileID = ActiveProfileSession["accessProfileID"];
                preferredAccessProfileName = ActiveProfileSession["accessProfileName"].ToString();
            }

            ProfileHistoryDetails profileHistory = new ProfileHistoryDetails();
            profileHistory.signum = Session[ConstantKeywords.C_Signum].ToString();
            profileHistory.accessTime = string.Empty;
            profileHistory.accessProfileID = int.Parse(preferredAccessProfileID.ToString());
            profileHistory.accessProfileName = preferredAccessProfileName;

            dynamic result = await sessionData.saveUserProfileHistory(profileHistory);

           if (result != null)
            {
                APIResponse response = JsonConvert.DeserializeObject<APIResponse>(result);

                if (response.formErrorCount == 0 && response.formMessageCount > 0 && response.formMessages != null)
                {
                    status = response.formMessages["0"].ToString() == "History saved Successfully" ? true : false;
                }
            }

            return status;

        }

        [AllowAnonymous]
        [CustomAuthorization(AuthorizationCheckRequired = false)]
        [SessionManager(SessionCheckRequired = false)]
        public ActionResult RedisLoadTest()
        {
            var ctx = HttpContext.Session.SessionID;
            if (string.IsNullOrEmpty(ctx) == false)
            {
                try
                {
                    ViewBag.Jsondata = AzureRedisCacheOperations.Instance.GetValueByKey(ctx);
                    ViewBag.Result = "Success";
                }
                catch
                {
                    ViewBag.Result = "Failed";
                }

            }
            return View();
        }

        [AllowAnonymous]
        [CustomAuthorization(AuthorizationCheckRequired = false)]
        [SessionManager(SessionCheckRequired = false)]
        public ActionResult HealthCheck()
        {
            ViewBag.Result = "Success";
            return View();
        }
        public Task GetNotificationDataFromRedis()
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);
            string notificationRedisKey = $"Notification_{Session[ConstantKeywords.C_Signum]?.ToString()?.ToUpper()}_{WebConfigDetail.CurrentEnvName}";
            var data = AzureRedisCacheOperations.Instance.GetValueByKey(notificationRedisKey);
            CreateLogsObj.WriteInformationLogs($"Start Method GetNotificationDataFromRedis redis key: {data}");

            Console.WriteLine($"Error: {notificationRedisKey} -> {data}");

            if (!string.IsNullOrEmpty(data))
            {
                NotificationModel responseModal = JsonConvert.DeserializeObject<NotificationModel>(data);
                dynamic responseRedis = JsonConvert.SerializeObject(responseModal);
                Session[ConstantKeywords.C_NotificationData] = responseRedis;
            }
            CreateLogsObj.WriteInformationLogs($"End Method GetNotificationDataFromRedis");
            return Task.CompletedTask;
        }
    }
}