using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace ISF_WEB.Controllers
{
    public class DemandForecastController : Controller
    {
        //
        // GET: /DemandForecast/
        public ActionResult DemandForecast()
        {
            return View();
        }

        public ActionResult ganttChartPositionView()
        {
            return View();
        }
    }
}