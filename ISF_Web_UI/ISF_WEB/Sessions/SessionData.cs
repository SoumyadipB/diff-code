using ISF.AzureRedisCache;
using ISF_WEB.Models;
using ISF_WEB.Util;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Web;

namespace ISF_WEB.Sessions
{
    public class SessionData
    {

        dynamic ctx = HttpContext.Current;

        public async Task<dynamic> GetSignumFromApiAsync()
        {

            string signum = string.Empty;
            bool isAsp = false;
            var ctx = HttpContext.Current;
            if (ctx.Session != null && ctx.Session[ConstantKeywords.C_UserEmaiId] != null)
            {
                AdfsUserDetail userDetail = new AdfsUserDetail();
                userDetail.employeeEmailID = ctx.Session[ConstantKeywords.C_UserEmaiId].ToString();
                userDetail.employeeUpn = (ctx.Session[ConstantKeywords.C_UserUpn] != null
                        ? ctx.Session[ConstantKeywords.C_UserUpn].ToString() : null);
                userDetail.employeeName = string.Format("{0} {1}",
                    ctx.Session[ConstantKeywords.C_UserGivenName].ToString(),
                    ctx.Session[ConstantKeywords.C_UserSurName].ToString());
                userDetail.firstName = ctx.Session[ConstantKeywords.C_UserGivenName].ToString();
                userDetail.lastName = ctx.Session[ConstantKeywords.C_UserSurName].ToString();
                userDetail.employeeObjectID = ctx.Session[ConstantKeywords.C_UserObjectIdentifier].ToString();
                if (ctx.Session[ConstantKeywords.C_Isignum] != null)
                {
                    userDetail.employeeType = ConstantKeywords.C_EmployeeTypeAsp;
                    isAsp = true;
                }
                else
                {
                    userDetail.employeeType = ConstantKeywords.C_EmployeeTypeEricson;
                }
                DataFromAPi dataFromAPi = new DataFromAPi();

                dynamic result = await dataFromAPi.PostDataAsync(userDetail, ConstantJavaApiControllerName.C_getUserSignumByEmail);
                result = await FetchAPIResponse.Instance.FilterResponse(result);
                var jsonResult = JsonConvert.DeserializeObject(result);
                var responseData = JsonConvert.SerializeObject(jsonResult.responseData);
                ctx.Session[ConstantKeywords.C_SignumDetailsFromEmail] = responseData;
                if (result != null)
                {
                    List<UserSignumByEmail> convertedResult = JsonConvert.DeserializeObject<List<UserSignumByEmail>>(responseData);
                    signum = convertedResult.Select(x => x.signumID).FirstOrDefault();
                    ctx.Session[ConstantKeywords.C_AspStatus] = convertedResult.Select(x => x.aspStatus).FirstOrDefault();
                    ctx.Session[ConstantKeywords.C_IsAspUser] = isAsp;
                    return await Task.FromResult<dynamic>(signum);
                }
                return null;
            }
            else
            {
                return null;
            }
        }
        public async Task<dynamic> GetPageAccessDetailsByRoleOfSignum()
        {

            var ctx = HttpContext.Current;
            if (ctx.Session != null && ctx.Session[ConstantKeywords.C_Signum] != null)
            {
                try
                {
                    string paramController = string.Format("{0}", ConstantJavaApiControllerName.C_getPageAccessDetailsByRoleOfSignum);

                    DataFromAPi dataFromAPi = new DataFromAPi();
                    dynamic result = await dataFromAPi.GetDataAsync(paramController);
                    return await FetchAPIResponse.Instance.FilterResponse(result);
                }
                catch (Exception ex)
                {
                    return null;
                }
            }
            else
            {
                return null;
            }


        }
        public async Task<dynamic> GetUserAccessProfile()
        {

            var ctx = HttpContext.Current;
            if (ctx.Session != null && ctx.Session[ConstantKeywords.C_Signum] != null)
            {
                try
                {
                    string paramController = string.Format("{0}/{1}", ConstantJavaApiControllerName.C_getUserAccessProfile, ctx.Session[ConstantKeywords.C_Signum]);

                    DataFromAPi dataFromAPi = new DataFromAPi();
                    dynamic result = await dataFromAPi.GetDataAsync(paramController);
                    return await FetchAPIResponse.Instance.FilterResponse(result);
                }
                catch (Exception ex)
                {
                    return null;
                }
            }
            else
            {
                return null;
            }


        }



        public async Task<dynamic> GetAccessProfileOfUser()
        {

            var ctx = HttpContext.Current;
            if (ctx.Session != null && ctx.Session[ConstantKeywords.C_Signum] != null)
            {
                try
                {
                    string paramController = string.Format("{0}/{1}", ConstantJavaApiControllerName.C_getAccessProfileOfUser, ctx.Session[ConstantKeywords.C_Signum]);

                    DataFromAPi dataFromAPi = new DataFromAPi();
                    dynamic result = await dataFromAPi.GetDataAsync(paramController);
                    return await FetchAPIResponse.Instance.FilterResponse(result);

                }
                catch (Exception ex)
                {
                    return null;
                }


            }
            else
            {
                return null;
            }


        }
        public async Task<dynamic> GetUserPreferance()
        {

            var ctx = HttpContext.Current;
            if (ctx.Session != null && ctx.Session[ConstantKeywords.C_Signum] != null)
            {
                try
                {
                    string paramController = string.Format("{0}/{1}", ConstantJavaApiControllerName.C_getUserPreferance, ctx.Session[ConstantKeywords.C_Signum]);

                    DataFromAPi dataFromAPi = new DataFromAPi();
                    dynamic result = await dataFromAPi.GetDataAsync(paramController);
                    return await FetchAPIResponse.Instance.FilterResponse(result);
                }
                catch (Exception ex)
                {
                    return ex.InnerException.ToString();
                }


            }
            else
            {
                return null;
            }


        }

        public async Task<dynamic> GetUserMenu()
        {

            var ctx = HttpContext.Current;
            if (ctx.Session != null && ctx.Session[ConstantKeywords.C_Signum] != null)
            {
                try
                {
                    string paramController = string.Format("{0}", ConstantJavaApiControllerName.C_getUserMenu);
                    DataFromAPi dataFromAPi = new DataFromAPi();
                    dynamic result = await dataFromAPi.GetDataAsync(paramController);

                    dynamic FilteredResult = FetchAPIResponse.Instance.GetResponse(result);

                    if (FilteredResult != null)
                    {
                        ctx.Session[ConstantKeywords.C_UserMenu] = FilteredResult;
                        return Task.FromResult<dynamic>(FilteredResult);
                    }

                    return FilteredResult;
                }
                catch (Exception ex)
                {
                    return null;
                }


            }
            else
            {
                return null;
            }


        }

        public async Task<dynamic> GetUserMenuByRole()
        {

            var ctx = HttpContext.Current;
            if (ctx.Session != null && ctx.Session[ConstantKeywords.C_Signum] != null)
            {
                try
                {
                    string paramController = string.Format("{0}", ConstantJavaApiControllerName.C_getUserMenuByRole);
                    DataFromAPi dataFromAPi = new DataFromAPi();
                    dynamic result = await dataFromAPi.GetDataAsync(paramController);
                    //var jsonResult = JsonConvert.DeserializeObject(result.response);
                    //var responseData = JsonConvert.SerializeObject(jsonResult.responseData);
                    //Logs.WriteLogs("Plaese Wait");
                    //return await FetchAPIResponse.Instance.FilterResponse(result);
                    result = await FetchAPIResponse.Instance.FilterResponse(result);
                    var jsonResult = JsonConvert.DeserializeObject(result);
                    var responseData = JsonConvert.SerializeObject(jsonResult.responseData);
                    return responseData;
                }
                catch (Exception ex)
                {
                    return null;
                }


            }
            else
            {
                return null;
            }


        }

        public async Task<dynamic> SaveUserLoginDetail(string status)
        {

            var ctx = HttpContext.Current;
            if (ctx.Session != null && ctx.Session[ConstantKeywords.C_Signum] != null)
            {
                CreateLogs CreateLogsObj = CreateLogs.GetInstance(ctx.Session[ConstantKeywords.C_Signum]);
                DataFromAPi dataFromAPi = new DataFromAPi();
                UserLoginDetail loginDetail = new UserLoginDetail();
                loginDetail.sessionId = ctx.Session.SessionID;
                loginDetail.emailID = ctx.Session[ConstantKeywords.C_UserEmaiId].ToString();
                loginDetail.signumID = ctx.Session[ConstantKeywords.C_Signum].ToString();
                loginDetail.status = status;
                loginDetail.sourceDomain = ctx.Request.Url.Host;
                loginDetail.ipAddress = ctx.Request.UserHostAddress;
                loginDetail.browser = Regex.IsMatch(ctx.Request.UserAgent, @"Edg\/\d+") ? "Edge" : ctx.Request.Browser.Browser;
                loginDetail.device = (ctx.Request.Browser.IsMobileDevice == true ? "Mobile" : "Laptop/Desktop");
                // Generating Token: Task#1794611
                loginDetail.sessionToken = dataFromAPi._token;
                string loginInfoKey = ctx.Session.SessionID;
                bool isExists = AzureRedisCacheOperations.Instance.CheckKeyExistsOrNot(loginInfoKey);
                CreateLogsObj.WriteInformationLogs($"CheckKeyExistsOrNot method start with key:{loginInfoKey}, isExists:{ isExists}, signum:{ loginDetail.signumID }");
               
                if (isExists)
                {
                    AzureRedisCacheOperations.Instance.RemoveValueByKey(loginInfoKey);
                }

                CreateLogsObj.WriteInformationLogs($"SaveValueByKey method start with key:{loginInfoKey}, signum:{ loginDetail.signumID}");
                bool isSaved = AzureRedisCacheOperations.Instance.SaveValueByKey(loginInfoKey, JsonConvert.SerializeObject(loginDetail), new TimeSpan(WebConfigDetail.RedisCacheExpirationTime, 0, 0));
                CreateLogsObj.WriteInformationLogs("SaveValueByKey method End with result:" + Convert.ToString(isSaved));

                dynamic result = await dataFromAPi.PostDataAsync(loginDetail, ConstantJavaApiControllerName.C_saveLoginDetails);
                result = await FetchAPIResponse.Instance.FilterResponse(result);
                var jsonResult = JsonConvert.DeserializeObject(result);
                var responseData = JsonConvert.SerializeObject(jsonResult.responseData);
                return responseData;
            }
            return null;
        }

        public async Task<dynamic> SaveUserLogoutDetail(string status)
        {

            var ctx = HttpContext.Current;
            if (ctx.Session != null && ctx.Session[ConstantKeywords.C_Signum] != null)
            {
                UserLoginDetail loginDetail = new UserLoginDetail();
                loginDetail.sessionId = ctx.Session.SessionID;
                loginDetail.emailID = ctx.Session[ConstantKeywords.C_UserEmaiId].ToString();
                loginDetail.signumID = ctx.Session[ConstantKeywords.C_Signum].ToString();
                loginDetail.status = status;

                DataFromAPi dataFromAPi = new DataFromAPi();
                dynamic result = await dataFromAPi.PostDataAsync(loginDetail, ConstantJavaApiControllerName.C_saveLogoutDetails);
                return await FetchAPIResponse.Instance.FilterResponse(result);

            }
            return null;
        }
        public async Task<dynamic> PauseAllManualTask(string status)
        {

            var ctx = HttpContext.Current;
            if (ctx.Session != null && ctx.Session[ConstantKeywords.C_Signum] != null)
            {
                UserLoginDetail loginDetail = new UserLoginDetail();
                loginDetail.sessionId = ctx.Session.SessionID;
                loginDetail.emailID = ctx.Session[ConstantKeywords.C_UserEmaiId].ToString();
                loginDetail.signumID = ctx.Session[ConstantKeywords.C_Signum].ToString();
                loginDetail.status = status;

                DataFromAPi dataFromAPi = new DataFromAPi();
                dynamic result = await dataFromAPi.PostDataAsync(loginDetail, ConstantJavaApiControllerName.C_pauseAllTaskOnLogout);
                return await FetchAPIResponse.Instance.FilterResponse(result);

            }
            return null;
        }

        public async Task<dynamic> getUserAccessProfileBySignum()
        {
            if (ctx.Session != null && ctx.Session[ConstantKeywords.C_Signum] != null)
            {
                string paramController = string.Format("{0}", ConstantJavaApiControllerName.C_getUserAccessProfileBySignum);
                DataFromAPi dataFromAPi = new DataFromAPi();
                dynamic result = await dataFromAPi.GetDataAsync(paramController);
                return await FetchAPIResponse.Instance.FilterResponse(result);

            }
            return null;
        }

        public async Task<dynamic> getUserPreferences()
        {
            if (ctx.Session != null && ctx.Session[ConstantKeywords.C_Signum] != null)
            {
                string paramController = string.Format("{0}", ConstantJavaApiControllerName.C_getUserPreferance);
                DataFromAPi dataFromAPi = new DataFromAPi();
                dynamic result = await dataFromAPi.GetDataAsync(paramController);
                return await FetchAPIResponse.Instance.FilterResponse(result);
            }
            return null;
        }

        public async Task<dynamic> saveUserProfileHistory(dynamic contentBody)
        {
            if (ctx.Session != null && ctx.Session[ConstantKeywords.C_Signum] != null)
            {
                string paramController = string.Format("{0}", ConstantJavaApiControllerName.C_saveUserProfileHistory);
                DataFromAPi dataFromAPi = new DataFromAPi();
                dynamic result = await dataFromAPi.PostDataAsync(contentBody, paramController);

                return await FetchAPIResponse.Instance.FilterResponse(result);

            }
            return null;
        }


        public async Task<dynamic> CheckDesktopVersion(string signum)
        {
            dynamic responseData =string.Empty;
            string paramController = string.Format("{0}?signum={1}", ConstantJavaApiControllerName.C_isInstalledDesktopVersionUpdated, signum);
            DataFromAPi dataFromAPi = new DataFromAPi();
            dynamic result = await dataFromAPi.GetDataAsync(paramController);
            result = await FetchAPIResponse.Instance.FilterResponse(result);
            if (result != null)
            {
                APIResponse response = JsonConvert.DeserializeObject<APIResponse>(result);
                var jsonResult = response.responseData;
                responseData = JsonConvert.SerializeObject(jsonResult);

            }
            return responseData;

        }
    }
}