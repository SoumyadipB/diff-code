using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace ISF_WEB.Controllers
{
    public class WorkOrderController : Controller
    {
        // GET: WorkOrder
        public ActionResult Index()
        {
            return View();
        }

        public ActionResult Creation()
        {
            return View();
        }

        public ActionResult PlanLanding()
        {
            return View();
        }

        public ActionResult activityDaily()
        {
            return View();
        }

        public ActionResult ganttModal()
        {
            return View();
        }

        public ActionResult activityHourly()
        {
            return View();
        }

        public ActionResult activityMonthly()
        {
            return View();
        }
        public ActionResult deliveryTracker()
        {
            return View();
        }
        public ActionResult dashboard()
        {
            return View();
        }
    }
}