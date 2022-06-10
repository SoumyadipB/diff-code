using ISF_WEB.Filters;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace ISF_WEB.Controllers
{
    [AllowAnonymous]
    [SessionManager(SessionCheckRequired = false)]
    public class ErrorController : Controller
    {
        // GET: Error
        [CustomAuthorization(AuthorizationCheckRequired = false)]
        public ActionResult Index()
        {
            return View();
        }
        [CustomAuthorization(AuthorizationCheckRequired = false)]
        public ActionResult PageNotFound()
        {
            return View();
        }
    }
}