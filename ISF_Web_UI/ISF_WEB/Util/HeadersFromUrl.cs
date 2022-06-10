using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ISF_WEB.Util
{
    public static class HeadersFromUrl
    {
        public static string ReadHeadersByKey(HttpRequestBase request, string key)
        {
            string headerValue = string.Empty;
            try{
                headerValue = request.ServerVariables.Get(key);
            }
            catch(Exception ex)
            {
                headerValue = string.Empty;
            }
            return headerValue;
        }
    }
}