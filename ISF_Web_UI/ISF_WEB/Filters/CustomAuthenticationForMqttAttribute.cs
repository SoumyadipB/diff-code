using ISF_WEB.Util;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Mvc.Filters;
using System.Web.Routing;

namespace ISF_WEB.Filters
{
    public class CustomAuthenticationForMqttAttribute : ActionFilterAttribute, IAuthenticationFilter
    {
        public void OnAuthentication(AuthenticationContext filterContext)
        {
            if (filterContext.HttpContext.Request.Headers != null && filterContext.HttpContext.Request.Headers.Get("mqttToken") != WebConfigDetail.MQTTKey)
            {
                filterContext.Result = RedirectToError();
            }
        }

        public void OnAuthenticationChallenge(AuthenticationChallengeContext filterContext)
        {
            if (filterContext.HttpContext.Request.Headers != null && filterContext.HttpContext.Request.Headers.Get("mqttToken") != WebConfigDetail.MQTTKey)
            {
                filterContext.Result = RedirectToError();
            }
        }

        private dynamic RedirectToError()
        {
            return new RedirectToRouteResult(
            new RouteValueDictionary
            {
                     { "controller", "Data" },
                     { "action", "ErrorWhileFetchingMqtt" }
            });
            
        }
    }
}