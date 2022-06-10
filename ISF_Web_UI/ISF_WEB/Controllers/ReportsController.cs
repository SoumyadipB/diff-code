using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace ISF_WEB.Controllers
{
    public class ReportsController : Controller
    {
        // GET: Reports
        public ActionResult Index()
        {
            return View();
        }
       
        public ActionResult ConfigureReports()
        {
            return View();
        }
        public ActionResult ReportIndex()
        {

            return View();
        }
        public ActionResult Tableau_Report()
        {

            return View();
        }
        public ActionResult ReportIndexSP()
        {

            return View();
        }
    }
}