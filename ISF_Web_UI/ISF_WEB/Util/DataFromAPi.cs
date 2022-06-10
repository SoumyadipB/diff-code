using ISF_WEB.Models;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Web;

namespace ISF_WEB.Util
{
    public class DataFromAPi
    {
        string _marketArea = string.Empty;
        string _signum = string.Empty;
        string _role = string.Empty;
        public string _token = string.Empty;
        public DataFromAPi()
        {
            TokenGenerator token = new TokenGenerator();
            _token = token.GenerateSessionToken();
            if (HttpContext.Current.Session[ConstantKeywords.C_Signum] != null)
            {
                _signum = HttpContext.Current.Session[ConstantKeywords.C_Signum].ToString();
            }
            if (HttpContext.Current.Session[ConstantKeywords.C_UserRole] != null)
            {
                _role = HttpContext.Current.Session[ConstantKeywords.C_UserRole].ToString();
            }
            if (HttpContext.Current.Session[ConstantKeywords.C_Organisation] != null)
            {
                _marketArea = HttpContext.Current.Session[ConstantKeywords.C_Organisation].ToString();
            }

        }
        public DataFromAPi(string marketArea, string signum, string role)
        {
            TokenGenerator token = new TokenGenerator();
            _token = token.GenerateSessionToken();
            _marketArea = marketArea;
            _role = role;
            _signum = signum;
        }
        public DataFromAPi(string token)
        {
            _token = token;

        }
        public async System.Threading.Tasks.Task<dynamic> GetDataAsync(string paramController, string tech = "JAVA")
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(_signum);
            string serviceUrl = string.Empty;
            string baseUrl = string.Empty;


            var handler = new HttpClientHandler();
            handler.ClientCertificateOptions = ClientCertificateOption.Manual;
            handler.ServerCertificateCustomValidationCallback =
                (httpRequestMessage, cert, cetChain, policyErrors) =>
                {
                    return true;
                };


            using (var client = new HttpClient(handler))
            {

                try
                {
                    client.DefaultRequestHeaders.Add(ConstantKeywords.C_MarketArea, _marketArea);
                    client.DefaultRequestHeaders.Add(ConstantKeywords.C_Signum, _signum);
                    client.DefaultRequestHeaders.Add(ConstantKeywords.C_Role, _role);
                    client.DefaultRequestHeaders.Add(ConstantKeywords.C_X_Auth_Token, _token);
                    client.Timeout = TimeSpan.FromSeconds(500);
                    // System.Net.Http.HttpResponseMessage response = null;
                    if (string.Equals(tech, "JAVA", StringComparison.OrdinalIgnoreCase) == true)
                    {
                        serviceUrl = WebConfigDetail.JavaRestApiUrl;
                    }
                    else
                    {
                        serviceUrl = WebConfigDetail.DotNetRestApiUrl;
                    }

                    if (paramController == ConstantJavaApiControllerName.C_getUserPreferance)
                    {
                        paramController = paramController + _signum;
                    }
                    baseUrl = string.Format("{0}{1}", serviceUrl, paramController);

                    Uri requestUri = new Uri(baseUrl); //replace your Url  

                    // capture API request in logs
                    CreateLogsObj.WriteAPIRequestLogs(requestUri.ToString(), client.DefaultRequestHeaders);

                    using (System.Net.Http.HttpResponseMessage response = await client.GetAsync(requestUri))
                    {
                        // Get Certificate Here
                        var cert = ServicePointManager.FindServicePoint(requestUri).Certificate;

                        var responseModel = FetchAPIResponse.Instance.ExtractResponseFromAPIData(CreateLogsObj, requestUri, response);

                        return responseModel;
                    }

                }
                catch (Exception ex)
                {
                    CreateLogsObj.WriteExceptionLogs(ex);
                    //Logs.WriteLogs(ex.Message.ToString(), "PostSignumDataAsync");
                    return null;
                }

            }


        }

        

        public async Task<dynamic> PostDataAsync(dynamic contentBody, string paramController, string tech = "JAVA")
        {
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(_signum);
            string serviceUrl = string.Empty;
            string baseUrl = string.Empty;


            var handler = new HttpClientHandler();
            handler.ClientCertificateOptions = ClientCertificateOption.Manual;
            handler.ServerCertificateCustomValidationCallback =
                (httpRequestMessage, cert, cetChain, policyErrors) =>
                {
                    return true;
                };


            using (var client = new HttpClient(handler))
            {

                try
                {
                    client.DefaultRequestHeaders.Add(ConstantKeywords.C_MarketArea, _marketArea);
                    client.DefaultRequestHeaders.Add(ConstantKeywords.C_Signum, _signum);
                    client.DefaultRequestHeaders.Add(ConstantKeywords.C_Role, _role);
                    client.DefaultRequestHeaders.Add(ConstantKeywords.C_X_Auth_Token, _token);
                    client.Timeout = TimeSpan.FromSeconds(500);
                    // System.Net.Http.HttpResponseMessage response = null;
                    if (string.Equals(tech, "JAVA", StringComparison.OrdinalIgnoreCase) == true)
                    {
                        serviceUrl = WebConfigDetail.JavaRestApiUrl;
                    }
                    else
                    {
                        serviceUrl = WebConfigDetail.DotNetRestApiUrl;
                    }

                    baseUrl = string.Format("{0}{1}", serviceUrl, paramController);
                    Uri requestUri = new Uri(baseUrl); //replace your Url  

                    string jsonString = JsonConvert.SerializeObject(contentBody);

                    // capture API request in logs
                    CreateLogsObj.WriteAPIRequestLogs(baseUrl.ToString(), jsonString);

                    var content = new StringContent(jsonString, Encoding.UTF8, "application/json");

                    using (System.Net.Http.HttpResponseMessage response = await client.PostAsync(requestUri, content))
                    {
                        // Get Certificate Here
                        var cert = ServicePointManager.FindServicePoint(requestUri).Certificate;

                        var responseModel = FetchAPIResponse.Instance.ExtractResponseFromAPIData(CreateLogsObj, requestUri, response);

                        return responseModel;
                    }

                }
                catch (Exception ex)
                {
                    CreateLogsObj.WriteExceptionLogs(ex);
                    //Logs.WriteLogs(ex.Message.ToString(), "PostSignumDataAsync");
                    return null;
                }

            }


        }
    }
}