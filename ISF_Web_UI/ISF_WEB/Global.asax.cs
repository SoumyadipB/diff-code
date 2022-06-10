using ISF.AzureRedisCache.Configuration;
using ISF_WEB.Controllers;
using ISF_WEB.Filters;
using ISF_WEB.Models;
using ISF_WEB.ModelsBinder;
using ISF_WEB.Sessions;
using ISF_WEB.Util;
using Microsoft.Owin.Security;
using Microsoft.Owin.Security.Cookies;
using Microsoft.Owin.Security.OpenIdConnect;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Linq;
using System.Security.Policy;
using System.Threading;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using System.Web.Optimization;
using System.Web.Routing;
using ISF.AzureRedisCache;

namespace ISF_WEB
{
    public class MvcApplication : System.Web.HttpApplication
    {
       
        protected void Application_Start()
        {
            
            int minWorker, minIOC;
            // Get the current settings.
            ThreadPool.GetMinThreads(out minWorker, out minIOC);

            var minThreads= WebConfigDetail.MinThread;
            // Initialize custom telemetry cloude role name.
            AreaRegistration.RegisterAllAreas();
            FilterConfig.RegisterGlobalFilters(GlobalFilters.Filters);
            RouteConfig.RegisterRoutes(RouteTable.Routes);
            BundleConfig.RegisterBundles(BundleTable.Bundles);
            ModelBinders.Binders.DefaultBinder = new JsonModelBinder();
            ThreadPool.SetMinThreads(minThreads, minThreads);
            SessionEndModule.SessionEnd += new SessionEndEventHandler(SessionTimoutModule_SessionEnd);
            KeysVaultHelper keysVaultHelper = new KeysVaultHelper();
            var redisAccessUrl = Task.Run(() => keysVaultHelper.GetAccessKeyfromKeyVault(WebConfigDetail.AzVaultUrl, WebConfigDetail.RedisConnectionKey));
            string conInfoOfRedis = redisAccessUrl.Result;
            conInfoOfRedis = string.IsNullOrWhiteSpace(conInfoOfRedis) ? WebConfigDetail.RedisConnectionUrl : conInfoOfRedis;
            AzureRedisCacheConfiguration.PoolSize = WebConfigDetail.RedisPoolSize;
            AzureRedisCacheConfiguration.SyncTimeout = WebConfigDetail.RedisSyncTimeoutInMs;
            AzureRedisCacheConfiguration.ConnectionString=conInfoOfRedis;
            if(WebConfigDetail.IsCacheClear)
            {
                AzureRedisCacheOperations.Instance.Clear();

            }
            
        }

        protected void Application_End()
        {

        }
        protected void Session_Start(object sender, EventArgs e)
        {
            Session[ConstantKeywords.C_ExpirationTime] = Session.Timeout;
        }

        protected void Session_End(object sender, EventArgs e)
        {
            // event is raised when a session is abandoned or expires
            Session.Clear();
            Session.Clear();
            Session.Abandon();
            Session.RemoveAll();
        }

        private async static void SessionTimoutModule_SessionEnd(object sender, SessionEndedEventArgs e)
        {
            var ctx = HttpContext.Current;
            dynamic _signum = ctx != null ? ctx.Session[ConstantKeywords.C_Signum] : string.Empty;
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(_signum);
            try
            {

                // This will be the value in the session for the key specified in Application_Start
                dynamic sessionObject = e.SessionObject;
                dynamic userData = (sessionObject == null) ? "[null]" : sessionObject;

                UserLoginDetail loginDetail = new UserLoginDetail();
                loginDetail = userData;
                if (loginDetail != null)
                {
                    string token = string.Empty;
                    TokenGenerator tokenGenerator = new TokenGenerator();
                    token = tokenGenerator.GenerateSessionToken(e.SessionId.ToString(), loginDetail.emailID);
                    DataFromAPi dataFromAPi = new DataFromAPi(token);

                    dynamic result = await dataFromAPi.PostDataAsync(loginDetail, ConstantJavaApiControllerName.C_saveLogoutDetails);
                    result = await FetchAPIResponse.Instance.FilterResponse(result);

                    CreateLogsObj.WriteInformationLogs(ConstantJavaApiControllerName.C_saveLogoutDetails + " has been called.");

                    result = await dataFromAPi.PostDataAsync(loginDetail, ConstantJavaApiControllerName.C_pauseAllTaskOnLogout);
                    result = await FetchAPIResponse.Instance.FilterResponse(result);

                    CreateLogsObj.WriteInformationLogs(ConstantJavaApiControllerName.C_pauseAllTaskOnLogout + " has been called.");
                    CreateLogsObj.WriteInformationLogs("Manual Task Stopped  - " + DateTime.Now.ToString());
                }

                if (ctx != null)
                {
                    ctx.Request.GetOwinContext().Authentication.SignOut(
                  Microsoft.AspNet.Identity.DefaultAuthenticationTypes.ApplicationCookie,
                  OpenIdConnectAuthenticationDefaults.AuthenticationType,
                  CookieAuthenticationDefaults.AuthenticationType);           
                }
            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
            }


        }
      


    }
}
