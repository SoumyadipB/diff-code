using ISF_WEB.Filters;
using ISF_WEB.Models;
using ISF_WEB.Util;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;

namespace ISF_WEB.Controllers
{
    public class DeliveryExecutionController : Controller
    {
        // GET: DeliveryExecution
        public ActionResult Index()
        {
            return View();
        }

        // GET: DeliveryExecution
        public ActionResult PlannedAssignment()
        {
            return View();
        }

        // GET: DeliveryExecution
        public ActionResult ProgAssignments()
        {
            return View();
        }

        public ActionResult FlowChart()
        {
            return View();
        }
        public ActionResult FlowChartEdit()
        {
            return View();
        }
        public ActionResult FlowChartView()
        {
            return View();
        }
        public ActionResult FlowChartExecution()
        {
            return View();
        }
        public ActionResult WorkorderAndTask()
        {
            return View();
        }
        public ActionResult FlViewWithoutComment()
        {
            return View();
        }

        [SessionManager(RoleCheckRequired = false)]
        public async Task<FileResult> FileFromApiInByteArray(string fileName,string folderPath)
        {
            string urlForFile = string.Empty;
            // return Json(new { FileUrl = urlForFile }, JsonRequestBehavior.AllowGet);
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(Session[ConstantKeywords.C_Signum]);

            using (var client = new HttpClient())
            {
                try
                {
                    string jsonString = CreateRequest(fileName, folderPath);
                    var content = new StringContent(jsonString, Encoding.UTF8, "application/json");
                    System.Net.Http.HttpResponseMessage response = null;
                    string baseUrl = RestApiUrl.JavaApiUrl;
                    string controlName = ConstantJavaApiControllerName.C_botDowload;
                    string ApiUrl = string.Format("{0}{1}", baseUrl, controlName);
                    Uri requestUri = new Uri(ApiUrl); //replace your Url  

                    CreateLogsObj.WriteAPIRequestLogs(requestUri, jsonString);

                    response = await client.PostAsync(ApiUrl, content);

                    var responseModel = FetchAPIResponse.Instance.ExtractResponseFromAPIData(CreateLogsObj, requestUri, response);

                    var result =  await FetchAPIResponse.Instance.FilterResponse(responseModel);
                    //bool flag = true;
                    //flag = FetchAPIResponse.IsValidJson(result);



                    if (result != null)
                    {
                        var convertedResult = JsonConvert.DeserializeObject<FileDownloadOutputDetail>(result);
                        byte[] fileBytes = convertedResult.pFileContent;
                        return File(fileBytes, System.Net.Mime.MediaTypeNames.Application.Octet, fileName);
                    }
                }
                catch (Exception ex)
                {
                    CreateLogsObj.WriteExceptionLogs(ex);
                }

                return null;
            }
          //  throw new HttpResponseException(HttpStatusCode.Unauthorized);
        }

        private static string CreateRequest(string fileName, string folderPath)
        {
            try {
                FileDownloadInputDetail _fileDownloadDetail = new FileDownloadInputDetail();
                _fileDownloadDetail.botId = fileName;
                _fileDownloadDetail.downloadBasePath = folderPath;
                _fileDownloadDetail.botName = "etc";
                _fileDownloadDetail.isFixedPath = "n";
                string jsonString = JsonConvert.SerializeObject(_fileDownloadDetail);
                return jsonString;
            }

            catch (JsonReaderException jex)
            {
                //Exception in parsing json
                Console.WriteLine(jex.Message);
                throw jex;
            }

        }

        public FileResult DownloadFile(string fileName)
        {
           
          // byte[] fileBytes = FileFromApiInByteArray(fileName).Result;

           byte[] fileBytes = System.IO.File.ReadAllBytes(@"C:\Users\exzccdj\Desktop/cert.txt");

            return File(fileBytes, System.Net.Mime.MediaTypeNames.Application.Octet, fileName);
        }

        public async Task<FileResult> GetFileContentInByteArray(string fileName)
        {
            using (var client = new HttpClient())
            {
                try
                {
                  
                    System.Net.Http.HttpResponseMessage response = null;
                    string baseUrl = @"http://localhost:65177/FileContent?fileNameWithPath=E:\SharedFolder\botStore\UploadedOutput\289997_eznispa_127932_01Dec2018135713.zip";

                   // baseUrl = RestApiUrl.JavaApiUrl;
                   // string ApiUrl = string.Format("{0}{1}", baseUrl, "botStore/botOutputDownload");

                    Uri requestUri = new Uri(baseUrl); //replace your Url  
                    response = await client.GetAsync(baseUrl);
                   
                    byte[] fileBytes = response.Content.ReadAsByteArrayAsync().Result;
                    return File(fileBytes, System.Net.Mime.MediaTypeNames.Application.Octet, fileName);

                }
                catch (Exception ex)
                {

                }

            }



            throw new HttpResponseException(HttpStatusCode.Unauthorized);
        }

      

        public async Task<string> SaveFile(string fileName)
        {
            byte[] fileBytes = null;
            using (var client = new HttpClient())
            {
                try
                {

                    System.Net.Http.HttpResponseMessage response = null;
                    string baseUrl = @"http://localhost:65177/FileContent?fileNameWithPath=E:\SharedFolder\botStore\UploadedOutput\289997_eznispa_127932_01Dec2018135713.zip";

                    // baseUrl = RestApiUrl.JavaApiUrl;
                    // string ApiUrl = string.Format("{0}{1}", baseUrl, "botStore/botOutputDownload");

                    Uri requestUri = new Uri(baseUrl); //replace your Url  
                    response = await client.GetAsync(baseUrl);

                     fileBytes = response.Content.ReadAsByteArrayAsync().Result;
                   

                }
                catch (Exception ex)
                {

                }

            }
            using (var client = new HttpClient())
            {
                try
                {
                    fileName = @"289997_eznispa_127932_01Dec2018135713.zip";
                   
                    System.Net.Http.HttpResponseMessage response = null;
                    string baseUrl = @"https://isf.ericsson.net/IsfProxy_SIT/Data?param=appUtil/botUpload/";
                   // string baseUrl = @"http://localhost:39048//Data?param=appUtil/botUpload/";

                   FileUploadInputDetail fd = new FileUploadInputDetail();
                   
                    fd.pFileContent = fileBytes;
                    fd.zipFileName = fileName;

                    string jsonString = JsonConvert.SerializeObject(fd);
                    var content = new StringContent(jsonString, Encoding.UTF8, "application/json");
                    
                    Uri requestUri = new Uri(baseUrl); //replace your Url  
                    response = await client.PostAsync(baseUrl, content);
                   var jsonOutputString = response.Content.ReadAsStringAsync().Result;
                    var convertedResult = JsonConvert.DeserializeObject<string>(jsonOutputString);

                    return convertedResult;
                    
                    //return null;
                }
                catch (JsonReaderException jex)
                {
                    //Exception in parsing json
                    Console.WriteLine(jex.Message);
                    throw jex;
                }
                catch (Exception ex)
                {

                }

            }



            throw new HttpResponseException(HttpStatusCode.Unauthorized);
        }

    }
}