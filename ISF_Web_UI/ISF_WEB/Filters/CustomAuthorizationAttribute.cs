using ISF_WEB.Util;
using Microsoft.Owin.Security;
using Microsoft.Owin.Security.OpenIdConnect;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Policy;
using System.Web;
using System.Web.Mvc;
using System.Web.Routing;

namespace ISF_WEB.Filters
{
    [AttributeUsage(AttributeTargets.Method, AllowMultiple = false, Inherited = true)]
    public class CustomAuthorizationAttribute : AuthorizeAttribute
    {
        public bool AuthorizationCheckRequired { get; set; } = true;
        protected override bool AuthorizeCore(HttpContextBase httpContext)
        {
            bool disableAuthentication = false;

            #if DEBUG
                disableAuthentication = false;
                // disableAuthentication = true;
            #endif

            if (disableAuthentication)
                return true;

            return base.AuthorizeCore(httpContext);
        }
        public override void OnAuthorization(AuthorizationContext filterContext)
        {
            string actionName = string.Empty;
            string controllerName = string.Empty;
            var ctx = HttpContext.Current;
            bool isAnHttpPost;
            ActionDescriptor action;

            try
            {
            //Check that the HttpPost attribute is also applied
            action = filterContext.ActionDescriptor;
            isAnHttpPost = action.GetCustomAttributes(typeof(HttpPostAttribute), true).Count() > 0;
             actionName = filterContext.ActionDescriptor.ActionName;
             controllerName = filterContext.ActionDescriptor.ControllerDescriptor.ControllerName;

          
            if (WebConfigDetail.IsLoginModeAzureAd)
            {
                if (ctx.Request.IsAuthenticated)
                {

                    if (ctx.Session != null)
                    {
                        string userUpn = string.Empty;
                        ctx.Session[ConstantKeywords.C_IsLoginModeAzureAd] = WebConfigDetail.IsLoginModeAzureAd;
                        var userClaims = ctx.User.Identity as System.Security.Claims.ClaimsIdentity;
                        userUpn = userClaims?.FindFirst(System.IdentityModel.Claims.ClaimTypes.Upn)?.Value;
                        ctx.Session[ConstantKeywords.C_UserEmaiId] = userUpn;
                        ctx.Session[ConstantKeywords.C_UserGivenName] = userClaims?.FindFirst(System.IdentityModel.Claims.ClaimTypes.GivenName)?.Value;
                        ctx.Session[ConstantKeywords.C_UserSurName] = userClaims?.FindFirst(System.IdentityModel.Claims.ClaimTypes.Surname)?.Value;
                        ctx.Session[ConstantKeywords.C_UserUpn] = userUpn;
                        ctx.Session[ConstantKeywords.C_UserObjectIdentifier] = userClaims?.FindFirst("http://schemas.microsoft.com/identity/claims/objectidentifier")?.Value;
                        ctx.Session.Timeout = WebConfigDetail.SessionTimeOutInterval;
                        ctx.Session[ConstantKeywords.C_ExpirationTime] = ctx.Session.Timeout;
                        string[] iSignumData = userUpn.Split('@');
                            if (iSignumData.Length >= 2
                            && string.IsNullOrWhiteSpace(iSignumData[1]) == false
                                 && (string.Equals(iSignumData[1], ConstantKeywords.C_IsignumBaseEmail, StringComparison.OrdinalIgnoreCase)
                                   || (Char.ToUpper(iSignumData[0][0]) == ConstantKeywords.C_IsignumBaseCharSignum && iSignumData[0].Length <= 8
                                   && iSignumData[0].Contains('.') == false)))
                        {
                            ctx.Session[ConstantKeywords.C_Isignum] = iSignumData[0];
                            ctx.Session[ConstantKeywords.C_UserType] = ConstantKeywords.C_EmployeeTypeAsp;
                            ctx.Session[ConstantKeywords.C_UserEmaiId] = userClaims?.FindFirst(System.IdentityModel.Claims.ClaimTypes.Name)?.Value;
                            }
                        else
                        {
                            ctx.Session[ConstantKeywords.C_UserType] = ConstantKeywords.C_EmployeeTypeEricson;
                        }
                    }
                }
                    else if (AuthorizationCheckRequired)
                {
                    ctx.Session[ConstantKeywords.C_ReferrerController] = controllerName;
                    ctx.Session[ConstantKeywords.C_ReferrerAction] = actionName;
                    SignIn();
                }

            }
            base.OnAuthorization(filterContext);
        }
            catch (Exception ex) 
            {
                CreateLogs createLogsObj = CreateLogs.GetInstance(string.Empty);
                createLogsObj.WriteExceptionLogs(ex);
                HandleLoginException(filterContext);
            }
        }

        private void HandleLoginException(AuthorizationContext filterContext)
        {
            try
            {
                filterContext.Result =
               new RedirectToRouteResult(
                   new RouteValueDictionary{{ "controller", "Login" },
                                          { "action", "SignOut" },
                                            {"returnUrl", "HandleLoginException"}

                                                 });
            }
            catch(Exception ex)
            {
                CreateLogs createLogsObj = CreateLogs.GetInstance(string.Empty);
                createLogsObj.WriteExceptionLogs(ex);
            }
        }

        private void SignIn()
        {
            if (WebConfigDetail.IsLoginModeAzureAd)
            {
                if (!HttpContext.Current.Request.IsAuthenticated)
                {
                   
                    HttpContext.Current.GetOwinContext().Authentication.Challenge(
                        new AuthenticationProperties { RedirectUri = WebConfigDetail.RedirectUrl },
                        OpenIdConnectAuthenticationDefaults.AuthenticationType);
                }

            }


        }
    }
}