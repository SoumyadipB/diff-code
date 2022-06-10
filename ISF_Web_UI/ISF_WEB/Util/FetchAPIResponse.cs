using ISF_WEB.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
namespace ISF_WEB.Util
{
    public class FetchAPIResponse
    {
        private readonly static Lazy<FetchAPIResponse> _instance = new Lazy<FetchAPIResponse>(() => new FetchAPIResponse());
        private readonly static CreateLogs CreateLogsObj = CreateLogs.GetInstance(string.Empty);

        public static FetchAPIResponse Instance
        {
            get
            {
                return _instance.Value;
            }
        }

        private FetchAPIResponse() { }

        public dynamic GetResponse(DataFromAPIModel model)
        {
            if(model != null)
            {
                if (model.IsSuccess)
                {
                    return model.response;
                }
            }

            return null;
        }

        public Task<dynamic> FilterResponse(dynamic result)
        {
            // filter result
            dynamic FilteredResult = GetResponse(result);
            //nahar validate json
            if (FilteredResult != null && ValidateJSON(FilteredResult))
            {
                return Task.FromResult<dynamic>(FilteredResult);
            }
            //nahar
            else {
                return null;
                //return FilteredResult;
            }

            
        }

        public DataFromAPIModel ExtractResponseFromAPIData(CreateLogs CreateLogsObj, Uri requestUri, HttpResponseMessage response)
        {
            DataFromAPIModel responseModel = new DataFromAPIModel();

            if (response.StatusCode == HttpStatusCode.OK || response.StatusCode == HttpStatusCode.Created)
            {
                var jsonOutputString = response.Content.ReadAsStringAsync().Result;

                if (jsonOutputString != null && ValidateJSON(jsonOutputString))
                {
                    CreateLogsObj.WriteAPIResponseLogs(requestUri.ToString(), jsonOutputString);
                    responseModel.IsSuccess = true;
                    responseModel.response = jsonOutputString;
                }
                else
                {
                    ErrorInResponse(CreateLogsObj, requestUri, response, responseModel);
                }
            }
            else
            {
                ErrorInResponse(CreateLogsObj, requestUri, response, responseModel);
            }

            return responseModel;
        }

        private static void ErrorInResponse(CreateLogs CreateLogsObj, Uri requestUri, HttpResponseMessage response, DataFromAPIModel responseModel)
        {
            CreateLogsObj.WriteAPIResponseLogs(requestUri.ToString(), response);
            responseModel.IsSuccess = false;
            responseModel.response = response;
            responseModel.ErrorMessage = string.Format("There is problem with response. API url: {0} Status Code: {1}", requestUri.ToString(), response.StatusCode.ToString());
            CreateLogsObj.WriteExceptionLogs(string.Format("There is problem with response, Response is empty. API url: {0} Status Code: {1}", requestUri.ToString(), response.StatusCode.ToString()));
        }

        //public static bool IsValidJson(string strInput)
        //{
        //    strInput = strInput.Trim();
        //    if ((strInput.StartsWith("{") && strInput.EndsWith("}")) || //For object
        //        (strInput.StartsWith("[") && strInput.EndsWith("]"))) //For array
        //    {
        //        try
        //        {
        //            var obj = JToken.Parse(strInput);
        //            return true;
        //        }
        //        catch (JsonReaderException jex)
        //        {
        //            //Exception in parsing json
        //            Console.WriteLine(jex.Message);
        //            return false;
        //            throw jex;
        //        }
        //        catch (Exception ex) //some other exception
        //        {
        //            Console.WriteLine(ex.ToString());
        //            return false;
        //            throw ex;
        //        }
        //    }
        //    else
        //    {
        //        return false;
        //    }
        //}

        public bool ValidateJSON(dynamic inputString)
        {
            try
            {
                JToken.Parse(inputString);
                return true;
            }
            catch (JsonReaderException ex)
            {
                CreateLogsObj.WriteExceptionLogs(ex);
                return false;
            }
        }

    }
}