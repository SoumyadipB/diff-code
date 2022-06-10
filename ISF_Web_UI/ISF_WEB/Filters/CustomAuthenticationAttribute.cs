using ISF_WEB.Util;
using Microsoft.Owin.Security;
using Microsoft.Owin.Security.OpenIdConnect;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc.Filters;

namespace ISF_WEB.Filters
{
    [AttributeUsage(AttributeTargets.Method, AllowMultiple = false, Inherited = true)]
    public class CustomAuthenticationAttribute : Attribute, System.Web.Mvc.Filters.IAuthenticationFilter
    {
        public void OnAuthentication(AuthenticationContext filterContext)
        {
            var ctx = HttpContext.Current;

            if (ctx.Session != null)
            {

                ctx.Session["IsLoginModeAzureAd"] = WebConfigDetail.IsLoginModeAzureAd;
                var userClaims = filterContext.Principal.Identity as System.Security.Claims.ClaimsIdentity;
                ctx.Session["UserEmaiId"] = userClaims?.FindFirst(System.IdentityModel.Claims.ClaimTypes.Name)?.Value;
                
            }
        }

        public void OnAuthenticationChallenge(AuthenticationChallengeContext filterContext)
        {
            SignIn();
        }
        private void SignIn()
        {
            if (WebConfigDetail.IsLoginModeAzureAd)
            {
                if (!HttpContext.Current.Request.IsAuthenticated)
                {

                    HttpContext.Current.GetOwinContext().Authentication.Challenge(
                        new AuthenticationProperties { RedirectUri = "/" },
                        OpenIdConnectAuthenticationDefaults.AuthenticationType);
                }

            }


        }
    }
}