using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using ISF_WEB.Models;
namespace ISF_WEB.Controllers
{
    public class WorkFlowController : Controller
    {

        public ActionResult Creation()
        {
            return View();
        }
        public ActionResult DeliveryScope()
        {
            return View();
        }

        public ActionResult WorkFlowView()
        {
            return View();
        }
        
    }
}