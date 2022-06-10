using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using ISF_WEB.Models;
namespace ISF_WEB.Controllers
{
    public class ResourceController : Controller
    {
        // GET: /Project/
        public ActionResult Index()
        {
            return View();
        }

        public ActionResult Request()
        {
            return View();
        }
        public ActionResult ResourceEngagement()
        {
            return View();
        }

        public ActionResult Calendar()
        {
            return View();
        }

        public ActionResult ChangeManagement()
        {
            return View();
        }
        public ActionResult SearchAllocateResource()
        {
            return View();
        }
        public ActionResult AllocatedResources()
        {
            return View();
        }
        public ActionResult ViewPositions()
        {
            return View();
        }
        //public ActionResult TestDashboard()
        //{
        //    return View();
        //}

    }
}