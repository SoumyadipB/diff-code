using ISF_WEB.Models;
using ISF_WEB.Util;
using Microsoft.Owin.Security;
using Microsoft.Owin.Security.Cookies;
using Microsoft.Owin.Security.OpenIdConnect;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;
using System.Web.Routing;

namespace ISF_WEB.Filters
{
    public class SessionManager : System.Web.Mvc.ActionFilterAttribute
    {
        // Properties for Action Filter
        public bool SessionCheckRequired { get; set; } = true;
        public bool RoleCheckRequired { get; set; } = true;
        /// <summary>
        /// Called when [action executing].
        /// </summary>
        /// <param name="filterContext">The filter context.</param>
        public override void OnActionExecuting(ActionExecutingContext filterContext)
        {
            try
            {
                string actionName = filterContext.ActionDescriptor.ActionName;
                string controllerName = filterContext.ActionDescriptor.ControllerDescriptor.ControllerName;
                if (SessionCheckRequired == true)
                {
                    var ctx = HttpContext.Current;

                    if (ctx?.Session != null)
                    {
                        // check if a new session id was generated

                        if (ctx.Session[ConstantKeywords.C_UserEmaiId] == null)
                        {

                            UrlHelper url = new UrlHelper(filterContext.RequestContext);
                            var absoluteReturnUrl = url.Action("SessionTimeout", "Login", new { }, filterContext.HttpContext.Request.Url.Scheme);
                            string[] absUrlArr = absoluteReturnUrl.Split('/');
                            absUrlArr[2] = WebConfigDetail.AbsUrl;
                            var newabsoluteReturnUrl = string.Join("/", absUrlArr);

                            // If it's a new session, but an existing cookie exists, then it must have timed out hence it's redirected to signout page
                            var sessionCookie = ctx.Request.Headers["Cookie"];
                            if (!string.IsNullOrEmpty(sessionCookie) &&
                                (sessionCookie.IndexOf("ASP.NET_SessionId", StringComparison.InvariantCultureIgnoreCase) >= 0))
                            {
                                ctx.Session.Clear();
                                ctx.Session.Abandon();
                                ctx.Session.RemoveAll();
                                ctx.GetOwinContext().Authentication.SignOut(
                                    new AuthenticationProperties
                                    {
                                        RedirectUri = newabsoluteReturnUrl.Replace("http://", "https://")
                                    },
                                                        OpenIdConnectAuthenticationDefaults.AuthenticationType,
                                                        CookieAuthenticationDefaults.AuthenticationType);
                            }
                            else
                            {
                                ctx.Session.Clear();
                                ctx.Session.Abandon();
                                ctx.Session.RemoveAll();
                                ctx.GetOwinContext().Authentication.SignOut(
                                    new AuthenticationProperties
                                    {
                                        RedirectUri = newabsoluteReturnUrl.Replace("http://", "https://")
                                    },
                                                        OpenIdConnectAuthenticationDefaults.AuthenticationType,
                                                        CookieAuthenticationDefaults.AuthenticationType);
                            }
                        }
                        else if (RoleCheckRequired == true && ctx.Session[ConstantKeywords.C_PageAccessDetailsByRoleOfSignum] != null)
                        {

                            List<PageAccessDetailsByRoleOfSignum> pageAccessDetails = new List<PageAccessDetailsByRoleOfSignum>();
                            pageAccessDetails = JsonConvert.DeserializeObject<List<PageAccessDetailsByRoleOfSignum>>
                                (ctx.Session[ConstantKeywords.C_PageAccessDetailsByRoleOfSignum].ToString());

                            if (pageAccessDetails != null && pageAccessDetails.Count > 0)
                            {
                                var pageDetail = pageAccessDetails.Where(x =>
                                        x.SubMenuHref.Contains(actionName, StringComparison.OrdinalIgnoreCase) == true
                                        && x.SubMenuHref.Contains(controllerName, StringComparison.OrdinalIgnoreCase) == true
                                        && x.SubMenuHref.Contains
                                        (string.Format("{0}/{1}", controllerName, actionName), StringComparison.OrdinalIgnoreCase) == true).FirstOrDefault();

                                if (pageDetail == null)
                                {
                                    UnauthorizedPageAccess(filterContext, ctx);
                                }
                            }
                            else
                            {
                                UnauthorizedPageAccess(filterContext, ctx);
                            }
                        }

                    }
                }

                base.OnActionExecuting(filterContext);
            }
            catch(Exception ex)
            {
                HandleLoginException(filterContext);
            }
        }

        private void UnauthorizedPageAccess(ActionExecutingContext filterContext, HttpContext ctx)
        {
            dynamic _signum = ctx == null ? string.Empty : ctx.Session[ConstantKeywords.C_Signum];
            CreateLogs CreateLogsObj = CreateLogs.GetInstance(_signum);
            CreateLogsObj.WriteInformationLogs("User is not authorized to access this page.");
            HandleUnauthorizedRequest(filterContext);
        }

        private  void HandleUnauthorizedRequest(ActionExecutingContext filterContext)
        {
            filterContext.Result =
           new RedirectToRouteResult(
               new RouteValueDictionary{{ "controller", "Login" },
                                          { "action", "UnauthorizedWarning" }

                                             });
        }
        private void HandleLoginException(ActionExecutingContext filterContext)
        {
            filterContext.Result =
           new RedirectToRouteResult(
               new RouteValueDictionary{{ "controller", "Login" },
                                          { "action", "SignOut" },
                                            {"returnUrl", "HandleLoginException"}

                                             });

        }

    }
}