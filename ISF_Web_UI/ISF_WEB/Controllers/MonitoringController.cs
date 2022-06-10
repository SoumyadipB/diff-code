using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using ISF_WEB.Filters;

namespace ISF_WEB.Controllers
{

    [AllowAnonymous]
   
    public class MonitoringController : Controller
    {
        // GET: Monitoring
        public ActionResult Index()
        {
            return View();
        }
        [CustomAuthorization(AuthorizationCheckRequired = false)]
        [SessionManager(SessionCheckRequired = false)]
        public ActionResult Monitoring()
        {
            return View();
        }
    }
}