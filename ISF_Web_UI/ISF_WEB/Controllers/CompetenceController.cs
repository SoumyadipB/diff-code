using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace ISF_WEB.Controllers
{
    public class CompetenceController : Controller
    {
        // GET: Competence
        public ActionResult CompetenceAndTraining()
        {
            return View();
        }
        public ActionResult MDMCompetenceUpload()
        {
            return View();
        }
    }
}