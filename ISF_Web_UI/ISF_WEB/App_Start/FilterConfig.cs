using ISF_WEB.Filters;
using ISF_WEB.Util;
using System.Web;
using System.Web.Mvc;


namespace ISF_WEB
{
    public class FilterConfig
    {
        public static void RegisterGlobalFilters(GlobalFilterCollection filters)
        {
            filters.Add(new HandleErrorAttribute());
            if (WebConfigDetail.IsLoginModeAzureAd)
            {
                filters.Add(new CustomAuthorizationAttribute());
                filters.Add(new SessionManager());
            }
        }
    }
}
