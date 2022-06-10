using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace ISF_WEB.Controllers
{
    public class TeamExplorerController : Controller
    {
        // GET: TeamExplorer
        public ActionResult Index()
        {
            return View();
        }
        public ActionResult ProfileAccess()
        {
            return View();
        }
        public ActionResult shiftTimeView()
        {
            return View();
        }
        public ActionResult userLocationView()
        {
            return View();
        }
    }
}