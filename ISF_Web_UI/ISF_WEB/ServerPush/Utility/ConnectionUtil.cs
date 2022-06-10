using ISF_WEB.Models;
using ISF_WEB.Util;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Formatting;
using System.Configuration;
using System.Text;
using System.Threading.Tasks;
using System.Web;

using System.Web.Mvc;

namespace ISF_WEB.ServerPush.Utility
{
    public class ConnectionUtil<T> 
    {
       

        //public string test(T key, string connId)
        //{
        //    string SystemURL = ConfigurationManager.AppSettings["DotNetRestAPIURL"];
        //    string URL = SystemURL + "/SignalRConnection";
        //    signalR obj = new signalR();
        //    obj.key = key.ToString();
        //    obj.connId = connId;


        //    string DATA = JsonConvert.SerializeObject(obj);

        //    HttpWebRequest request = (HttpWebRequest)WebRequest.Create(URL);
        //    request.Method = "POST";
        //    request.ContentType = "application/json";
        //    //request.ContentLength = Data.Length;
        //    StreamWriter requestWriter = new StreamWriter(request.GetRequestStream(), System.Text.Encoding.ASCII);
        //    requestWriter.Write(DATA);
        //    requestWriter.Close();

        //    try
        //    {
        //        WebResponse webResponse = request.GetResponse();
        //        Stream webStream = webResponse.GetResponseStream();
        //        StreamReader responseReader = new StreamReader(webStream);
        //        string response = responseReader.ReadToEnd();
        //        Console.Out.WriteLine(response);
        //        responseReader.Close();
        //    }
        //    catch (Exception e)
        //    {
        //        Console.Out.WriteLine("-----------------");
        //        Console.Out.WriteLine(e.Message);
        //    }


        //    //return string.Empty;
        //    //throw new NotImplementedException();

        //}

      
        public async Task<dynamic>  SetConnectionInDB(T key,  string connId)
        {

             System.Net.Http.HttpResponseMessage response = null;
            try
            {
                using (var client = new HttpClient())
                {
                    
                    signalR obj = new signalR();
                    obj.key = key.ToString();
                    obj.connId = connId;
                    var jsonString = JsonConvert.SerializeObject(obj);
                     
                    var content = new StringContent(jsonString, Encoding.UTF8, "application/json");
                    
                    client.Timeout = TimeSpan.FromSeconds(500);
                    string baseUrl = string.Empty;

                   
                        baseUrl = WebConfigDetail.DotNetRestApiUrl + "/SignalRConnection";
                  
                    string ApiUrl = string.Format("{0}", baseUrl);
                    //Uri requestUri = new Uri("http://10.184.19.202:8080/isf-rest-server-java/accessManagement/authorize"); //replace your Url  
                    Uri requestUri = new Uri(ApiUrl); //replace your Url  


                    response = await client.PostAsync(requestUri, content).ConfigureAwait(false);

                   
                    string responJsonText = await response.Content.ReadAsStringAsync();
                    return response;

                }

            }
            catch (Exception ex)
            {
                return ex.Message.ToString();
            }
           

        }

        [System.Web.Mvc.HttpGet]
        public async Task<dynamic> GetConnectionFromDB(T key)
        {

            try
            {
                using (var client = new HttpClient())
                {


                    string baseUrl = string.Empty;
                    baseUrl = WebConfigDetail.DotNetRestApiUrl + "/SignalRConnection?key=" + key;




                    string ApiUrl = string.Format("{0}", baseUrl);
                    dynamic responseApi = await client.GetAsync(ApiUrl).ConfigureAwait(false);
                    string responseText = await responseApi.Content.ReadAsStringAsync();
                    string connectionID = string.Empty;
                    signalR conndict = new signalR();

                    if (responseText != null)
                    {
                        dynamic result = JsonConvert.DeserializeObject(responseText);

                        foreach (var items in result)
                        {
                            foreach (var keys in items)
                            {
                                if (((Newtonsoft.Json.Linq.JProperty)keys).Name == "ConnectionID")
                                {
                                    connectionID = ((Newtonsoft.Json.Linq.JValue)((Newtonsoft.Json.Linq.JProperty)keys).Value).Value.ToString();
                                    break;
                                }
                                else continue;

                            }
                        }

                        return connectionID;

                    }

                    return string.Empty;
                }

            }
            catch (Exception er)
            {
                return await Task.FromResult<dynamic>(er.Message.ToString());
            }

        }


        [System.Web.Mvc.HttpDelete]
        public async Task<dynamic> RemoveConnectionFromDB(T key)
        {

            try
            {
                using (var client = new HttpClient())
                {


                    string baseUrl = string.Empty;
                    baseUrl = WebConfigDetail.DotNetRestApiUrl + "/SignalRConnection?key="+key;


                    string ApiUrl = string.Format("{0}", baseUrl);
                    dynamic responseApi = await client.DeleteAsync(ApiUrl).ConfigureAwait (false);
                    return responseApi;

                }


            }
            catch (Exception er)
            {
                return await Task.FromResult<dynamic>(er.Message.ToString());
            }

        }
      

    }
}