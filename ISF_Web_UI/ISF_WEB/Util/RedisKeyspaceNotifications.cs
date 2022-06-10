using ISF.AzureRedisCache;
using ISF_WEB.Models;
using ISF_WEB.Sessions;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;
using StackExchange.Redis;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Configuration;

namespace ISF_WEB.Util
{
    public class RedisKeyspaceNotifications
    {
        public static void RedisKeysNotifications(ConnectionMultiplexer connection, string key, string signum)
        {
            var subscriber = connection.GetSubscriber();
            string db = "*";
            //string notificationChannel = ConstantKeywords.C_DBPattenFront + db + ConstantKeywords.C_DBPattenBack;  // all events - The Key-space channel receives as message the name of the event.
            string notificationChannel = "__keyspace@" + db + "__:" + key;  // all events - The Key-space channel receives as message the name of the event.
            UserLoginDetail userLoginDetail = new UserLoginDetail();
            CreateLogs createLogs = CreateLogs.GetInstance(string.Empty);

            try
            {
                subscriber.Subscribe(notificationChannel, (channel, notificationType) =>
                {
                     key = GetKey(channel);


                    if (notificationType == ConstantKeywords.C_RedisKeyExpiredEvent || notificationType == "expire")
                    {
                        createLogs.WriteInformationLogs(string.Format("{0} key is expired.", key));
                        string sessionKeySpace = $"{ConstantKeywords.C_SessionDetailsForLogout}_{signum}";
                        var loginDetailsJson = AzureRedisCacheOperations.Instance?.GetValueByKey(sessionKeySpace);

                        createLogs.WriteInformationLogs(string.Format("Login Details to be deleted: {0}", loginDetailsJson));

                        if (!string.IsNullOrEmpty(loginDetailsJson))
                        {
                            userLoginDetail = JsonConvert.DeserializeObject<UserLoginDetail>(loginDetailsJson);
                            string notificationKey = $"{ userLoginDetail.sessionId}{ConstantKeywords.C_LoginDetails}";
                            if (notificationKey == key)
                            {
								// Delete key from Redis cache
								bool IsKeyDeleted = AzureRedisCacheOperations.Instance.RemoveValueByKey(key);
                                createLogs.WriteInformationLogs(string.Format("{0} key is deleted from Redis cache: {1}", key, IsKeyDeleted));
                                IsKeyDeleted = AzureRedisCacheOperations.Instance.RemoveValueByKey(sessionKeySpace);
                                createLogs.WriteInformationLogs(string.Format("{0} key is deleted from Redis cache: {1}", sessionKeySpace, IsKeyDeleted));
                                SessionEndModule sessionEndModule = new SessionEndModule();
                                sessionEndModule.RedisCacheExpiration(key, userLoginDetail);
                                createLogs.WriteInformationLogs(string.Format("{0} key is deleted from Redis cache: {1}", key, IsKeyDeleted));
                            }
                        }
                    }
                });
            }
            catch (Exception ex)
            {
                createLogs.WriteExceptionLogs(ex);
            }
        }

        public static string GetKey(string channel)
        {
            var index = channel.IndexOf(':');
            if (index >= 0 && index < channel.Length - 1)
                return channel.Substring(index + 1);

            //we didn't find the delimeter, so just return the whole thing
            return channel;
        }
    }
}