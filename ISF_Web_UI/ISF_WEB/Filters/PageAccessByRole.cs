using ISF_WEB.Models;
using ISF_WEB.Util;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace ISF_WEB.Filters
{
    public class PageAccessByRole : System.Web.Mvc.ActionFilterAttribute
    {
        public bool RoleCheckRequired { get; set; } = true;
        /// <summary>
        /// Called when [action executing].
        /// </summary>
        /// <param name="filterContext">The filter context.</param>
        public override void OnActionExecuting(ActionExecutingContext filterContext)
        {

            string actionName = filterContext.ActionDescriptor.ActionName;
            string controllerName = filterContext.ActionDescriptor.ControllerDescriptor.ControllerName;
            if (RoleCheckRequired == true)
            {
                var ctx = HttpContext.Current;

                if (ctx.Session != null)
                {
                    // check if a new session id was generated

                   if (ctx.Session[ConstantKeywords.C_PageAccessDetailsByRoleOfSignum] != null)
                    {
                        List<PageAccessDetailsByRoleOfSignum> pageAccessDetails = new List<PageAccessDetailsByRoleOfSignum>();
                        pageAccessDetails = JsonConvert.DeserializeObject<List<PageAccessDetailsByRoleOfSignum>>
                            (ctx.Session[ConstantKeywords.C_PageAccessDetailsByRoleOfSignum].ToString());

                    }

                }
            }

            base.OnActionExecuting(filterContext);
        }
    }
}