using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Web;

namespace ISF_WEB.Util
{
	public static class WebConfigDetail
	{
		public static string JavaRestApiUrl { get; set; }
		public static string DotNetRestApiUrl { get; set; }
		public static string LoginMode { get; set; }
		public static bool IsLoginModeAzureAd { get; set; } = false;
		public static string ClientId { get; set; }
		public static string RedirectUrl { get; set; }
		public static string AbsUrl { get; set; }
		public static string Tenant { get; set; }
		public static string Authority { get; set; }
		public static int SessionTimeOutInterval { get; set; }
		public static bool IsUserLogOut { get; set; }
		public static bool IsUserSessionTimeOut { get; set; }
		public static bool IsLoggingEnabled { get; set; }
		public static string RedisConnectionUrl { get; set; }
		public static int MinThread { get; set; }
		public static string AzVaultUrl { get; set; }
		public static string AppId { get; set; }
		public static string AppSecret { get; set; }
		public static string RedisConnectionKeyVersion { get; set; }
		public static string RedisConnectionKey { get; set; }
		public static int RedisPoolSize { get; set; }
		public static int RedisSyncTimeoutInMs { get; set; }
		public static string AzureSignalRConnectionString { get; set; }
		public static string AzureSignalRHubURL { get; set; }
		public static int RetainedFileCountLimit { get; set; }
		public static string LogDirectoryPath { get; set; }
		public static string InstrumentationSecret { get; set; }
		public static int RedisCacheExpirationTime { get; set; }
		public static bool IsFileLoggingEnabled { get; set; }

		public static bool IsCacheClear { get; set; }
		public static int ConnectRetry { get; set; }
		public static int ReconnectRetry { get; set; }
		public static int KeepAlive { get; set; }

		public static string NotificationApiUrl { get; set; }
		public static string ApiAuthKey { get; set; }
		public static string userNameMQTT { get; set; }
		public static string passwordMQTT { get; set; }
		public static string hostNameMQTT { get; set; }
		public static string MQTTKey { get; set; }

		public static string CurrentEnvName { get; set; }


		static WebConfigDetail()
		{
			//JavaRestApiUrl  is for calling Java API

			JavaRestApiUrl = System.Configuration.ConfigurationManager.AppSettings["JavaRestApiUrl"];
			DotNetRestApiUrl = System.Configuration.ConfigurationManager.AppSettings["DotNetRestApiUrl"];
			//Login Mode is for enable or disable Azure AD Authentication or Basic Authentication
			LoginMode = System.Configuration.ConfigurationManager.AppSettings["LoginMode"];
			IsLoginModeAzureAd = string.Equals(LoginMode, "AZUREAD", StringComparison.OrdinalIgnoreCase);
			// The Client ID (a.k.a. Application ID) is used by the application to uniquely identify itself to Azure AD
			ClientId = System.Configuration.ConfigurationManager.AppSettings["ClientId"];

			// RedirectUri is the URL where the user will be redirected to after they sign in
			RedirectUrl = System.Configuration.ConfigurationManager.AppSettings["redirectUrl"];

			AbsUrl = System.Configuration.ConfigurationManager.AppSettings["AbsUrl"];
			// Tenant is the tenant ID (e.g. contoso.onmicrosoft.com, or 'common' for multi-tenant)
			Tenant = System.Configuration.ConfigurationManager.AppSettings["Tenant"];

			// Authority is the URL for authority, composed by Azure Active Directory endpoint and the tenant name (e.g. https://login.microsoftonline.com/contoso.onmicrosoft.com)
			Authority = System.Configuration.ConfigurationManager.AppSettings["Authority"];

			SessionTimeOutInterval = Convert.ToInt32(System.Configuration.ConfigurationManager.AppSettings["SessionTimeOutInterval"]);

			IsLoggingEnabled = string.Equals(System.Configuration.ConfigurationManager.AppSettings["IsLoggingEnabled"], "YES", StringComparison.OrdinalIgnoreCase);

			MinThread = Convert.ToInt32(System.Configuration.ConfigurationManager.AppSettings["MinThread"]);
			EncryptDecryptUtil decryptUtil = new EncryptDecryptUtil();
			AzVaultUrl = decryptUtil.Reverse(decryptUtil.Decryption(ConfigurationManager.AppSettings["AzVaultUrl"]));
			AppId = decryptUtil.Reverse(decryptUtil.Decryption(ConfigurationManager.AppSettings["AppId"]));
			AppSecret = decryptUtil.Reverse(decryptUtil.Decryption(System.Configuration.ConfigurationManager.AppSettings["AppSecret"]));
			RedisConnectionKeyVersion = decryptUtil.Reverse(decryptUtil.Decryption((ConfigurationManager.AppSettings["RedisConnectionKeyVersion"])));
			RedisConnectionKey = ConfigurationManager.AppSettings["RedisConnectionKey"];
			RedisPoolSize = Convert.ToInt32(ConfigurationManager.AppSettings["RedisPoolSize"]);
			RedisSyncTimeoutInMs = Convert.ToInt32(ConfigurationManager.AppSettings["RedisSyncTimeoutInMs"]);
			RedisConnectionUrl = ConfigurationManager.AppSettings["RedisConnection"];
			AzureSignalRConnectionString = System.Configuration.ConfigurationManager.AppSettings["AzureSignalRConnectionString"];
			AzureSignalRHubURL = System.Configuration.ConfigurationManager.AppSettings["AzureSignalRHubURL"];
			RetainedFileCountLimit = Convert.ToInt32(ConfigurationManager.AppSettings["LogFileRetainLimit"]);
			LogDirectoryPath = ConfigurationManager.AppSettings["LogDirectoryPath"];
			// get appinsight instrumentation key
			InstrumentationSecret = ConfigurationManager.AppSettings["InstrumentationSecret"];
			RedisCacheExpirationTime = Convert.ToInt32(ConfigurationManager.AppSettings["RedisCacheKeyExpired"]);
			IsCacheClear = Convert.ToBoolean(ConfigurationManager.AppSettings["isRedisCacheClear"]);
			if (bool.TryParse(ConfigurationManager.AppSettings["IsFileLoggingEnabled"], out bool result))
			{
				IsFileLoggingEnabled = result;
			}
			else
			{
				IsFileLoggingEnabled = result;
			}
			ApiAuthKey = ConfigurationManager.AppSettings["ApiAuthKey"];
			NotificationApiUrl = ConfigurationManager.AppSettings["NotificationApiUrl"];
			userNameMQTT = ConfigurationManager.AppSettings["userNameMQTT"];
			passwordMQTT = ConfigurationManager.AppSettings["passwordMQTT"];
			hostNameMQTT = ConfigurationManager.AppSettings["hostNameMQTT"];
			MQTTKey = ConfigurationManager.AppSettings["MQTT_KEY"];
			CurrentEnvName = ConfigurationManager.AppSettings["CurrentEnvName"];

		}

		public static string GetEnvName()
		{
			string envName = RedirectUrl;
			string fileName = string.Empty;
			fileName = envName.Substring(envName.LastIndexOf('/'), envName.Length);
			return fileName;
		}
	}
}