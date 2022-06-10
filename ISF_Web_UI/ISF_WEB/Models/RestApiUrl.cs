using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Configuration;

namespace ISF_WEB.Models
{
    public static class RestApiUrl
    {
        public static string JavaApiUrl { get; set; }
        public static string DotNetApiUrl { get; set; }

        static RestApiUrl()
        {

            if (String.IsNullOrWhiteSpace(WebConfigurationManager.AppSettings["JavaRestApiUrl"]) == false)
            {
                JavaApiUrl = WebConfigurationManager.AppSettings["JavaRestApiUrl"].ToString();
            }
            else
            {
                JavaApiUrl = "https://isfdevservices.internal.ericsson.com:8443/isf-rest-server-java_dev_major/";
            }

            if (String.IsNullOrWhiteSpace(WebConfigurationManager.AppSettings["DotNetRestApiUrl"]) == false)
            {
                DotNetApiUrl = WebConfigurationManager.AppSettings["DotNetRestApiUrl"].ToString();
            }
        }

    }
}