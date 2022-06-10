using ISF.AzureRedisCache;
using ISF_WEB.Models;
using ISF_WEB.Util;
using Newtonsoft.Json;
using System;
using System.Linq;
using System.Web;
using System.Web.Caching;
using System.Web.SessionState;

namespace ISF_WEB.Sessions
{
	/// <summary>
	/// Event handler for handling the SessionEnd event
	/// </summary>
	public delegate void SessionEndEventHandler(object sender, SessionEndedEventArgs e);

    /// <summary>
    /// SessionEndedEventArgs for use in the SessionEnd event
    /// </summary>
    public class SessionEndModule : IHttpModule
    {
        #region Private Variables

        private HttpApplication m_HttpApplication;
        #endregion

        #region Accessors

        /// <summary>
        /// This is the key of the item in the session which should be returned
        /// in the SessionEnd event (as the SessionObject).
        /// </summary>
        /// <example>
        ///	If you're storing the user ID in the session, under a key called 'UserId'
        /// and need to do something with it in the SessionEnd event, you would set
        /// this to 'UserId', which would cause the value of the session key called
        /// 'UserId' to be returned.
        /// </example>
        public static string SessionObjectKey { get; set; }

        #endregion

        #region IHttpModule Implementation

        public void Init(HttpApplication context)
        {
            //set connection string.
            m_HttpApplication = context;
            m_HttpApplication.PreRequestHandlerExecute += new EventHandler(OnProRequestHandlerExecute);
        }

        public void Dispose()
        {
            // Do Nothing
        }

        #endregion

        #region Events

        /// <summary>
        /// Event raised when the session ends
        /// </summary>
        /// 
       
        public static event SessionEndEventHandler SessionEnd;

        #endregion

        private void OnProRequestHandlerExecute(object sender, EventArgs e)
        {
            CreateLogs CreateLogsObj = null;
            // We only want to update the session when an ASPX page is being viewed
            // We're also doing this in the PreRequestHandler, as doing it elsewhere
            // (like the PostRequestHandler) can cause some strange behaviour.
            // Ensure we have a HttpContext
            try
            {
                HttpSessionState currentSession = HttpContext.Current.Session;
                string requestPath = m_HttpApplication.Context.Request.Path;
                dynamic _signum = currentSession == null ? string.Empty : currentSession[ConstantKeywords.C_Signum];

                 CreateLogsObj = CreateLogs.GetInstance(_signum);
                if (requestPath.Contains("ExtendSession", StringComparison.OrdinalIgnoreCase) == true)
                {
                    if (HttpContext.Current == null)
                    {

                        return;
                    }

                    // Get the current session


                    // Ensure we have a current session
                    if (currentSession == null)
                    {
                        return;
                    }

                    // Get the session timeout
                    TimeSpan sessionTimeout = new TimeSpan(0, 0, currentSession.Timeout, 51, 0);

                    // Get the object in the session we want to retrieve when the session times out
                    if (currentSession[ConstantKeywords.C_UserEmaiId] != null && currentSession[ConstantKeywords.C_UserLoginId] != null)
                    {
                        CreateLogsObj.WriteInformationLogs("Extend Session - new logging");
                        CreateLogsObj.WriteInformationLogs(string.Format("new session timeout {0}", sessionTimeout));
                        UserLoginDetail loginDetail = new UserLoginDetail();
                        loginDetail.emailID = currentSession[ConstantKeywords.C_UserEmaiId].ToString();
                        loginDetail.sessionId = currentSession.SessionID;
                        loginDetail.signumID = (currentSession[ConstantKeywords.C_Signum] != null
                            ? currentSession[ConstantKeywords.C_Signum].ToString() : null);
                        loginDetail.status = ConstantKeywords.C_StatusLogout;
                        loginDetail.logID = Convert.ToInt32(currentSession[ConstantKeywords.C_UserLoginId]);
                        // Add the object to the cache with the current session id, and set a cache removal callback method
                        //HttpContext.Current.Cache.Insert(currentSession.SessionID, loginDetail, null, DateTime.MaxValue, sessionTimeout, CacheItemPriority.NotRemovable, new CacheItemRemovedCallback(CacheItemRemovedCallbackMethod));
                        string notificationKey = $"{ currentSession.SessionID}{ConstantKeywords.C_LoginDetails}";
                        string sessionKeySpace = $"{ConstantKeywords.C_SessionDetailsForLogout}_{currentSession[ConstantKeywords.C_Signum]}";
                        AzureRedisCacheOperations.Instance.SaveValueByKey(sessionKeySpace, JsonConvert.SerializeObject(loginDetail));
                        RedisKeyspaceNotifications.RedisKeysNotifications(ConnectionEstablishment.Instance.Connection, notificationKey,_signum);
                        AzureRedisCacheOperations.Instance.SaveValueByKey(notificationKey, JsonConvert.SerializeObject(loginDetail), sessionTimeout);

                    }
                }
                else if (requestPath.Contains("SignOut", StringComparison.OrdinalIgnoreCase) == true)
                {
                    TimeSpan sessionTimeout = new TimeSpan(0, 0, 0, 5, 0);

                    // Get the object in the session we want to retrieve when the session times out
                    UserLoginDetail loginDetail = new UserLoginDetail();
                    if (currentSession[ConstantKeywords.C_UserEmaiId] != null)
                    {
                        loginDetail.emailID = currentSession[ConstantKeywords.C_UserEmaiId].ToString();
                        loginDetail.sessionId = currentSession.SessionID;
                        loginDetail.signumID = (currentSession[ConstantKeywords.C_Signum] != null
                            ? currentSession[ConstantKeywords.C_Signum].ToString() : null);
                        loginDetail.status = ConstantKeywords.C_StatusLogout;
                        loginDetail.logID = Convert.ToInt32(currentSession[ConstantKeywords.C_UserLoginId]??0);
                        // Add the object to the cache with the current session id, and set a cache removal callback method
                        //HttpContext.Current.Cache.Insert(currentSession.SessionID, loginDetail, null, DateTime.MaxValue, sessionTimeout, CacheItemPriority.NotRemovable, new CacheItemRemovedCallback(CacheItemRemovedCallbackMethod));
                        
                        string notificationKey = $"{ currentSession.SessionID}{ConstantKeywords.C_LoginDetails}";
                        string sessionKeySpace = $"{ConstantKeywords.C_SessionDetailsForLogout}_{currentSession[ConstantKeywords.C_Signum]}";
                        AzureRedisCacheOperations.Instance.SaveValueByKey(sessionKeySpace, JsonConvert.SerializeObject(loginDetail));
                        RedisKeyspaceNotifications.RedisKeysNotifications(ConnectionEstablishment.Instance.Connection, notificationKey, _signum);
                        AzureRedisCacheOperations.Instance.SaveValueByKey(notificationKey, JsonConvert.SerializeObject(loginDetail), sessionTimeout);
                    }

                }
            }
            catch (Exception ex)
            {
                CreateLogsObj = CreateLogs.GetInstance(string.Empty);
                CreateLogsObj.WriteExceptionLogs(ex);
               // Logs.WriteExceptionLogs(ex);
            }
          

        }

        /// <summary>
        /// This method is fired when an item is removed from the cache.  It is used to detect when a cache item
        /// expires, indicating that the session has expired, and fires the SessionEnd event.
        /// </summary>
        private void CacheItemRemovedCallbackMethod(string key, dynamic value, CacheItemRemovedReason reason)
        {
            var CreateLogsObj = CreateLogs.GetInstance("");
            if (reason == CacheItemRemovedReason.Expired)
            {
                if (SessionEnd != null)
                {
                    CreateLogsObj.WriteInformationLogs(string.Format("cache expiration, key: {0}", key));
                    SessionEndedEventArgs e = new SessionEndedEventArgs(key, value);
                    SessionEnd(this, e);
                }
            }
        }
        /// <summary>
         /// This method is fired when an item is removed from the redis cache. 
         /// </summary>
         /// <param name="key"></param>
         /// <param name="value"></param>
        public void RedisCacheExpiration(string key, dynamic value)
        {
            var CreateLogsObj = CreateLogs.GetInstance("");
            if (SessionEnd != null)
            {
                CreateLogsObj.WriteInformationLogs(string.Format("Redis cache expiration, key: {0}", key));
                SessionEndedEventArgs e = new SessionEndedEventArgs(key, value);
                SessionEnd(this, e);
            }
        }
    }
}