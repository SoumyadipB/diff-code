
using Microsoft.IdentityModel.Clients.ActiveDirectory;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using Microsoft.Azure.KeyVault;

namespace ISF_WEB.Util
{
	public class KeysVaultHelper
	{


        public async Task<string> GetAccessKeyfromKeyVault(string vaultUrl,string accessKeyName)
        {

            string redisConnectionAccessKey = String.Empty;
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(string.Empty);
            try
            {
                CreateLogsObj.WriteInformationLogs("GetAccessKeysfromKeyVault method start with appid: "+ WebConfigDetail.AppId);
                var keyVaultClient = new KeyVaultClient(new KeyVaultClient.AuthenticationCallback(GetAccessToken));

                var secret = await keyVaultClient.GetSecretAsync(vaultUrl, accessKeyName);

                redisConnectionAccessKey= secret.Value;
                CreateLogsObj.WriteInformationLogs("GetAccessKeysfromKeyVault method end");

            }
            catch (Exception ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex.Message);
            }

            return redisConnectionAccessKey;
        }

        public static async Task<string> GetAccessToken(string authority, string resource, string scope)
        {
            var clientId = WebConfigDetail.AppId;
            var clientSecret = WebConfigDetail.AppSecret;
            ClientCredential clientCredential = new ClientCredential(clientId, clientSecret);

            var context = new AuthenticationContext(authority, TokenCache.DefaultShared);
            var result = await context.AcquireTokenAsync(resource, clientCredential);

            return result.AccessToken;
        }
    }
}